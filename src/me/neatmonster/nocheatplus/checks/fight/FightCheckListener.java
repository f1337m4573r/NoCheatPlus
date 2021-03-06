package me.neatmonster.nocheatplus.checks.fight;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.neatmonster.nocheatplus.EventManager;
import me.neatmonster.nocheatplus.NoCheatPlus;
import me.neatmonster.nocheatplus.NoCheatPlusPlayer;
import me.neatmonster.nocheatplus.config.ConfigurationCacheStore;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerAnimationEvent;

/**
 * Central location to listen to events that are
 * relevant for the fight checks
 * 
 */
public class FightCheckListener implements Listener, EventManager {

    private final List<FightCheck> checks = new ArrayList<FightCheck>(4);

    private final GodmodeCheck     godmodeCheck;
    private final InstanthealCheck instanthealCheck;
    private final NoCheatPlus      plugin;

    public FightCheckListener(final NoCheatPlus plugin) {

        // Keep these in a list, because they can be executed in a bundle
        checks.add(new SpeedCheck(plugin));
        checks.add(new NoswingCheck(plugin));
        checks.add(new DirectionCheck(plugin));
        checks.add(new ReachCheck(plugin));

        godmodeCheck = new GodmodeCheck(plugin);
        instanthealCheck = new InstanthealCheck(plugin);

        this.plugin = plugin;
    }

    /**
     * We listen to PlayerAnimationEvent because it is used for arm swinging
     * 
     * @param event
     *            The PlayerAnimationEvent
     */
    @EventHandler(
            priority = EventPriority.MONITOR)
    protected void armSwing(final PlayerAnimationEvent event) {
        // Set a flag telling us that the arm has been swung
        FightCheck.getData(plugin.getPlayer(event.getPlayer())).armswung = true;
    }

    /**
     * There is an unofficial agreement that if a plugin wants an attack to
     * not get checked by NoCheatPlus, it either has to use a Damage type different
     * from ENTITY_ATTACK or fire an event with damage type CUSTOM and damage
     * 0 directly before the to-be-ignored event.
     * 
     * @param event
     *            The EntityDamageByEntityEvent
     */
    private void customDamage(final EntityDamageByEntityEvent event) {

        final Player damager = (Player) event.getDamager();
        final NoCheatPlusPlayer player = plugin.getPlayer(damager);

        final FightData data = FightCheck.getData(player);

        // Skip the next damage event, because it is with high probability
        // something from the Heroes plugin
        data.skipNext = true;
    }

    /**
     * We listen to death events to prevent a very specific method of doing
     * godmode.
     * 
     * @param event
     *            The EntityDeathEvent
     */
    @EventHandler(
            priority = EventPriority.MONITOR)
    protected void death(final EntityDeathEvent event) {
        // Only interested in dying players
        if (!(event.getEntity() instanceof CraftPlayer))
            return;

        godmodeCheck.death((CraftPlayer) event.getEntity());
    }

    /**
     * We listen to EntityDamage events for obvious reasons
     * 
     * @param event
     *            The EntityDamage Event
     */
    @EventHandler(
            ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void entityDamage(final EntityDamageEvent event) {

        // Filter some unwanted events right now
        if (!(event instanceof EntityDamageByEntityEvent))
            return;

        final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        if (!(e.getDamager() instanceof Player))
            return;

        if (e.getCause() == DamageCause.ENTITY_ATTACK)
            normalDamage(e);
        else if (e.getCause() == DamageCause.CUSTOM)
            customDamage(e);
    }

    /**
     * We listen to EntityDamage events (again) for obvious reasons
     * 
     * @param event
     *            The EntityDamage Event
     */
    @EventHandler(
            ignoreCancelled = true, priority = EventPriority.LOW)
    public void entityDamageForGodmodeCheck(final EntityDamageEvent event) {

        // Filter unwanted events right here
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player) || entity.isDead())
            return;

        final NoCheatPlusPlayer player = plugin.getPlayer((Player) entity);
        final FightConfig cc = FightCheck.getConfig(player);

        if (!godmodeCheck.isEnabled(cc) || player.hasPermission(godmodeCheck.permission))
            return;

        final FightData data = FightCheck.getData(player);

        // Run the godmode check on the attacked player
        final boolean cancelled = godmodeCheck.check(plugin.getPlayer((Player) entity), data, cc);

        // It requested to "cancel" the players invulnerability, so set his
        // noDamageTicks to 0
        if (cancelled)
            // Remove the invulnerability from the player
            player.getPlayer().setNoDamageTicks(0);
    }

    @Override
    public List<String> getActiveChecks(final ConfigurationCacheStore cc) {
        final LinkedList<String> s = new LinkedList<String>();

        final FightConfig f = FightCheck.getConfig(cc);

        if (f.directionCheck)
            s.add("fight.direction");
        if (f.noswingCheck)
            s.add("fight.noswing");
        if (f.reachCheck)
            s.add("fight.reach");
        if (f.speedCheck)
            s.add("fight.speed");
        if (f.godmodeCheck)
            s.add("fight.godmode");
        if (f.instanthealCheck)
            s.add("fight.instantHeal");
        return s;
    }

    /**
     * A player attacked something with DamageCause ENTITY_ATTACK. That's most
     * likely what we want to really check.
     * 
     * @param event
     *            The EntityDamageByEntityEvent
     */
    private void normalDamage(final EntityDamageByEntityEvent event) {

        final Player damager = (Player) event.getDamager();

        final NoCheatPlusPlayer player = plugin.getPlayer(damager);
        final FightConfig cc = FightCheck.getConfig(player);
        final FightData data = FightCheck.getData(player);

        // For some reason we decided to skip this event anyway
        if (data.skipNext) {
            data.skipNext = false;
            return;
        }

        boolean cancelled = false;

        // Get the attacked entity and remember it
        data.damagee = ((CraftEntity) event.getEntity()).getHandle();

        // Run through the four main checks
        for (final FightCheck check : checks)
            // If it should be executed, do it
            if (!cancelled && check.isEnabled(cc) && !player.hasPermission(check.permission))
                cancelled = check.check(player, data, cc);

        // Forget the attacked entity (to allow garbage collecting etc.
        data.damagee = null;

        // One of the checks requested the event to be cancelled, so do it
        if (cancelled)
            event.setCancelled(cancelled);
    }

    /**
     * We listen to EntityRegainHealth events of type "Satiated"
     * for instantheal check
     * 
     * @param event
     *            The EntityRegainHealth Event
     */
    @EventHandler(
            ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void satiatedRegen(final EntityRegainHealthEvent event) {

        if (!(event.getEntity() instanceof Player) || event.getRegainReason() != RegainReason.SATIATED)
            return;

        boolean cancelled = false;

        final NoCheatPlusPlayer player = plugin.getPlayer((Player) event.getEntity());
        final FightConfig config = FightCheck.getConfig(player);

        if (!instanthealCheck.isEnabled(config) || player.hasPermission(instanthealCheck.permission))
            return;

        final FightData data = FightCheck.getData(player);

        cancelled = instanthealCheck.check(player, data, config);

        if (cancelled)
            event.setCancelled(true);
    }
}
