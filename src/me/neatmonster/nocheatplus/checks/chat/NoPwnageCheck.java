package me.neatmonster.nocheatplus.checks.chat;

import java.util.Locale;
import java.util.Random;

import me.neatmonster.nocheatplus.NoCheatPlus;
import me.neatmonster.nocheatplus.NoCheatPlusPlayer;
import me.neatmonster.nocheatplus.actions.ParameterName;
import me.neatmonster.nocheatplus.config.ConfPaths;
import me.neatmonster.nocheatplus.data.SimpleLocation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NoPwnageCheck extends ChatCheck {
    private String       lastBanCausingMessage;
    private long         lastBanCausingMessageTime;
    private String       lastGlobalMessage;
    private long         lastGlobalMessageTime;
    private int          globalRepeated;

    private final Random random = new Random();

    public NoPwnageCheck(final NoCheatPlus plugin) {
        super(plugin, "chat.nopwnage");

        // Store the players' location
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final ChatData data = ChatCheck.getData(plugin.getPlayer(player));
            data.location.setLocation(player.getLocation());
        }
    }

    public boolean check(final NoCheatPlusPlayer player, final ChatData data, final ChatConfig cc) {

        boolean cancel = false;

        if (data.commandsHaveBeenRun || !player.getPlayer().isOnline())
            return false;

        // Player is supposed to fill out a captcha
        if (cc.noPwnageCaptchaCheck && data.captchaDone)
            // His reply was valid, he isn't a spambot
            return false;

        if (cc.noPwnageCaptchaCheck && data.captchaStarted) {
            // Correct answer?
            if (data.message.equals(data.captchaAnswer))
                data.captchaDone = true;
            else {
                // Display the question again
                player.sendMessage(data.captchaQuestion);

                // He failed too much times
                if (data.captchaTries > cc.noPwnageCaptchaTries)
                    if (player.getPlayer().isOnline())
                        // Execute the commands, it's a spambot
                        runCommands(player, "failed captcha", data, cc);

                // Increment the number of times he replied
                data.captchaTries++;
            }
            return true;
        }

        // Do some pre-testing for the next check
        final long now = System.currentTimeMillis();
        final SimpleLocation location = new SimpleLocation();
        location.setLocation(player.getPlayer().getLocation());

        double suspicion = 0;

        if (data.location == null)
            data.setLocation(location);
        else if (!data.compareLocation(location)) {
            data.setLocation(location);
            data.lastMovedTime = now;
        }

        // Banned check (cf. documentation)
        if (!data.isCommand && cc.noPwnageBannedCheck && now - lastBanCausingMessageTime < cc.noPwnageBannedTimeout
                && similar(data.message, lastBanCausingMessage))
            suspicion += cc.noPwnageBannedWeight;

        // First check (cf. documentation)
        if (cc.noPwnageFirstCheck && now - data.joinTime <= cc.noPwnageFirstTimeout)
            suspicion += cc.noPwnageFirstWeight;

        // Global check (cf. documentation)
        if (!data.isCommand && cc.noPwnageGlobalCheck && now - lastGlobalMessageTime < cc.noPwnageGlobalTimeout
                && similar(data.message, lastGlobalMessage)) {
            final int added = (globalRepeated + 2) * cc.noPwnageGlobalWeight / 2;
            globalRepeated++;
            suspicion += added;
        } else
            globalRepeated = 0;

        // Speed check (cf. documentation)
        if (cc.noPwnageSpeedCheck && now - data.lastMessageTime <= cc.noPwnageSpeedTimeout) {
            int added = (data.speedRepeated + 2) * cc.noPwnageSpeedWeight / 2;
            data.speedRepeated++;
            if (data.isCommand)
                added /= 4;
            suspicion += added;
        } else
            data.speedRepeated = 0;

        // Repeat check (cf. documentation)
        if (!data.isCommand && cc.noPwnageRepeatCheck && now - data.lastMessageTime <= cc.noPwnageRepeatTimeout
                && similar(data.message, data.lastMessage)) {

            final int added = (data.messageRepeated + 2) * cc.noPwnageRepeatWeight / 2;
            data.messageRepeated++;
            suspicion += added;
        } else
            data.messageRepeated = 0;

        boolean warned = false;
        if (cc.noPwnageWarnPlayers && now - data.lastWarningTime <= cc.noPwnageWarnTimeout) {
            suspicion += 100;
            warned = true;
        }

        // Move checks (cf. documentation)
        if (cc.noPwnageMoveCheck && now - data.lastMovedTime <= cc.noPwnageMoveTimeout)
            suspicion -= cc.noPwnageMoveWeightBonus;
        else
            suspicion += cc.noPwnageMoveWeightMalus;

        // Warn player
        if (cc.noPwnageWarnPlayers && suspicion >= cc.noPwnageWarnLevel && !warned) {
            data.lastWarningTime = now;
            warnPlayer(player);
        } else if (suspicion >= cc.noPwnageBanLevel)
            if (cc.noPwnageCaptchaCheck && !data.captchaStarted) {
                // Display the captcha to the player
                data.captchaStarted = true;
                final String captcha = generateCaptcha(cc);
                data.captchaAnswer = captcha;
                data.captchaQuestion = ChatColor.RED + "Please type '" + ChatColor.GOLD + captcha + ChatColor.RED
                        + "' to continue sending messages/commands.";
                cancel = true;
                player.sendMessage(data.captchaQuestion);
            } else if (player.getPlayer().isOnline()) {
                // Execute the commands, it's a spambot
                lastBanCausingMessage = data.message;
                lastBanCausingMessageTime = now;
                data.lastWarningTime = now;
                if (cc.noPwnageWarnOthers)
                    warnOthers(player);
                runCommands(player, "spambotlike behaviour", data, cc);
                return true;
            }

        // Remember his message and some other data
        data.lastMessage = data.message;
        data.lastMessageTime = now;

        lastGlobalMessage = data.message;
        lastGlobalMessageTime = now;

        return cancel;
    }

    private String generateCaptcha(final ChatConfig cc) {

        final StringBuilder b = new StringBuilder();

        for (int i = 0; i < cc.noPwnageCaptchaLength; i++)
            b.append(cc.noPwnageCaptchaCharacters.charAt(random.nextInt(cc.noPwnageCaptchaCharacters.length())));

        return b.toString();
    }

    @Override
    public String getParameter(final ParameterName wildcard, final NoCheatPlusPlayer player) {

        if (wildcard == ParameterName.REASON)
            return String.format(Locale.US, "%d", getData(player).reason);
        else if (wildcard == ParameterName.IP)
            return String.format(Locale.US, "%d", getData(player).ip);
        else
            return super.getParameter(wildcard, player);
    }

    public void handleJoin(final NoCheatPlusPlayer player, final ChatData data, final ChatConfig cc) {
        final long now = System.currentTimeMillis();

        // Relog check (cf. documentation)
        if (cc.noPwnageRelogCheck && now - data.leaveTime <= cc.noPwnageRelogTime) {
            if (now - data.lastRelogWarningTime >= cc.noPwnageRelogTimeout)
                data.relogWarnings = 0;

            if (data.relogWarnings < cc.noPwnageRelogWarnings) {
                player.sendMessage(player.getConfigurationStore().getConfiguration()
                        .getString(ConfPaths.LOGGING_PREFIX)
                        + ChatColor.DARK_RED
                        + "You relogged really fast! If you keep doing that, you're going to be banned.");
                data.lastRelogWarningTime = now;
                data.relogWarnings++;
            } else if (now - data.lastRelogWarningTime < cc.noPwnageRelogTimeout)
                // Run the commands, it's a spambot
                runCommands(player, "relogged too fast", data, cc);
        }

        // Remember his location
        final SimpleLocation location = new SimpleLocation();
        location.setLocation(player.getPlayer().getLocation());
        data.setLocation(location);
        data.joinTime = now;

        data.commandsHaveBeenRun = false;
    }

    private int minimum(final int a, final int b, final int c) {
        int mi;

        mi = a;
        if (b < mi)
            mi = b;
        if (c < mi)
            mi = c;
        return mi;
    }

    private void runCommands(final NoCheatPlusPlayer player, final String reason, final ChatData data,
            final ChatConfig cc) {
        data.reason = reason;
        data.ip = player.getPlayer().getAddress().toString().substring(1).split(":")[0];

        if (player.getPlayer().isOnline()) {
            data.commandsHaveBeenRun = true;
            executeActions(player, cc.noPwnageActions, 0);
        }
    }

    private boolean similar(final String message1, final String message2) {
        return message1 != null && message2 != null
                && stringDifference(message1, message2) < 1 + message1.length() / 10;
    }

    private int stringDifference(final String s, final String t) {
        int d[][];
        int n;
        int m;
        int i;
        int j;
        char s_i;
        char t_j;
        int cost;

        n = s.length();
        m = t.length();
        if (n == 0)
            return m;
        if (m == 0)
            return n;
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++)
            d[i][0] = i;

        for (j = 0; j <= m; j++)
            d[0][j] = j;
        for (i = 1; i <= n; i++) {

            s_i = s.charAt(i - 1);

            for (j = 1; j <= m; j++) {

                t_j = t.charAt(j - 1);

                if (s_i == t_j)
                    cost = 0;
                else
                    cost = 1;

                d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);

            }

        }
        return d[n][m];
    }

    private void warnOthers(final NoCheatPlusPlayer player) {
        plugin.getServer().broadcastMessage(
                ChatColor.YELLOW + player.getName() + ChatColor.DARK_RED + " has set off the autoban!");
        plugin.getServer().broadcastMessage(
                ChatColor.DARK_RED + " Please do not say anything similar to what the user said!");
    }

    private void warnPlayer(final NoCheatPlusPlayer player) {
        player.sendMessage(player.getConfigurationStore().getConfiguration().getString(ConfPaths.LOGGING_PREFIX)
                + ChatColor.DARK_RED + "Our system has detected unusual bot activities coming from you.");
        player.sendMessage(ChatColor.DARK_RED
                + "Please be careful with what you say. DON'T repeat what you just said either, unless you want to be banned.");
    }
}
