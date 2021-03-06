package me.neatmonster.nocheatplus.config;

/**
 * These are the default settings for NoCheatPlus. They will be used
 * in addition to/in replacement of configurations given in the
 * config.yml file
 * 
 */
public class DefaultConfiguration extends NoCheatPlusConfiguration {

    public DefaultConfiguration() {

        super();

        options().header("Main configuration file for NoCheatPlus. Read \"Instructions.txt\"");

        /** CLIENT MODS **/

        set(ConfPaths.CLIENT_MODS_ALLOW, false);

        /** LOGGING **/

        set(ConfPaths.LOGGING_ACTIVE, true);
        set(ConfPaths.LOGGING_SHOWACTIVECHECKS, false);
        set(ConfPaths.LOGGING_DEBUGMESSAGES, false);
        set(ConfPaths.LOGGING_PREFIX, "&4NCP&f: ");
        set(ConfPaths.LOGGING_FILENAME, "nocheatplus.log");
        set(ConfPaths.LOGGING_LOGTOFILE, true);
        set(ConfPaths.LOGGING_LOGTOCONSOLE, true);
        set(ConfPaths.LOGGING_LOGTOINGAMECHAT, true);

        /*** INVENTORY ***/

        set(ConfPaths.INVENTORY_DROP_CHECK, true);
        set(ConfPaths.INVENTORY_DROP_TIMEFRAME, 20);
        set(ConfPaths.INVENTORY_DROP_LIMIT, 100);
        set(ConfPaths.INVENTORY_DROP_ACTIONS, "log:drop:0:1:cif cmd:kick");

        set(ConfPaths.INVENTORY_INSTANTBOW_CHECK, true);
        set(ConfPaths.INVENTORY_INSTANTBOW_ACTIONS, "log:ibow:2:5:if cancel");

        set(ConfPaths.INVENTORY_INSTANTEAT_CHECK, true);
        set(ConfPaths.INVENTORY_INSTANTEAT_ACTIONS, "log:ieat:2:5:if cancel");

        /*** MOVING ***/

        set(ConfPaths.MOVING_RUNFLY_CHECK, true);
        set(ConfPaths.MOVING_RUNFLY_ALLOWFASTSNEAKING, false);
        set(ConfPaths.MOVING_RUNFLY_MAXCOOLDOWN, 10000);
        set(ConfPaths.MOVING_RUNFLY_ACTIONS,
                "log:moveshort:3:5:f cancel vl>100 log:moveshort:0:5:if cancel vl>400 log:movelong:0:5:cif cancel");

        set(ConfPaths.MOVING_RUNFLY_CHECKNOFALL, true);
        set(ConfPaths.MOVING_RUNFLY_NOFALLAGGRESSIVE, true);
        set(ConfPaths.MOVING_RUNFLY_NOFALLACTIONS, "log:nofall:0:5:cif cancel");

        set(ConfPaths.MOVING_RUNFLY_TRACKER_CHECK, true);
        set(ConfPaths.MOVING_RUNFLY_TRACKER_ACTIONS, "cmd:kick");

        set(ConfPaths.MOVING_RUNFLY_FLYING_ALLOWALWAYS, false);
        set(ConfPaths.MOVING_RUNFLY_FLYING_ALLOWINCREATIVE, true);
        set(ConfPaths.MOVING_RUNFLY_FLYING_SPEEDLIMITHORIZONTAL, 60);
        set(ConfPaths.MOVING_RUNFLY_FLYING_SPEEDLIMITVERTICAL, 100);
        set(ConfPaths.MOVING_RUNFLY_FLYING_HEIGHTLIMIT, 128);
        set(ConfPaths.MOVING_RUNFLY_FLYING_ACTIONS,
                "log:moveshort:3:5:f cancel vl>100 log:moveshort:0:5:if cancel vl>400 log:movelong:0:5:cif cancel");

        set(ConfPaths.MOVING_MOREPACKETS_CHECK, true);
        set(ConfPaths.MOVING_MOREPACKETS_ACTIONS, "log:morepackets:3:2:if cancel vl>20 log:morepackets:0:2:if cancel");

        set(ConfPaths.MOVING_MOREPACKETSVEHICLE_CHECK, true);
        set(ConfPaths.MOVING_MOREPACKETSVEHICLE_ACTIONS, "log:morepackets:0:2:if cancel");

        set(ConfPaths.MOVING_WATERWALK_CHECK, true);
        set(ConfPaths.MOVING_WATERWALK_ACTIONS,
                "log:waterwalk:3:5:f cancel vl>100 log:waterwalk:0:5:if cancel vl>400 log:waterwalk:0:5:cif cancel");

        /*** BLOCKBREAK ***/

        set(ConfPaths.BLOCKBREAK_FASTBREAK_CHECK, true);
        set(ConfPaths.BLOCKBREAK_FASTBREAK_INTERVALSURVIVAL, 45);
        set(ConfPaths.BLOCKBREAK_FASTBREAK_INTERVALCREATIVE, 145);
        set(ConfPaths.BLOCKBREAK_FASTBREAK_ACTIONS,
                "cancel vl>100 log:bbfastbreak:3:5:cif cancel vl>2000 log:bbfastbreak:3:5:cif cmd:kick cancel");

        set(ConfPaths.BLOCKBREAK_REACH_CHECK, true);
        set(ConfPaths.BLOCKBREAK_REACH_ACTIONS, "cancel vl>5 log:bbreach:0:2:if cancel");

        set(ConfPaths.BLOCKBREAK_DIRECTION_CHECK, true);
        set(ConfPaths.BLOCKBREAK_DIRECTION_PRECISION, 50);
        set(ConfPaths.BLOCKBREAK_DIRECTION_PENALTYTIME, 300);
        set(ConfPaths.BLOCKBREAK_DIRECTION_ACTIONS, "cancel vl>10 log:bbdirection:0:5:if cancel");

        set(ConfPaths.BLOCKBREAK_NOSWING_CHECK, true);
        set(ConfPaths.BLOCKBREAK_NOSWING_ACTIONS, "log:bbnoswing:3:2:if cancel");

        /*** BLOCKPLACE ***/

        set(ConfPaths.BLOCKPLACE_FASTPLACE_CHECK, true);
        set(ConfPaths.BLOCKPLACE_FASTPLACE_INTERVAL, 145);
        set(ConfPaths.BLOCKPLACE_FASTPLACE_ACTIONS,
                "cancel vl>100 log:bpfastplace:3:5:cif cancel vl>2000 log:bpfastplace:3:5:cif cmd:kick cancel");

        set(ConfPaths.BLOCKPLACE_REACH_CHECK, true);
        set(ConfPaths.BLOCKPLACE_REACH_ACTIONS, "cancel vl>5 log:bpreach:0:2:if cancel");

        set(ConfPaths.BLOCKPLACE_DIRECTION_CHECK, true);
        set(ConfPaths.BLOCKPLACE_DIRECTION_PRECISION, 75);
        set(ConfPaths.BLOCKPLACE_DIRECTION_PENALTYTIME, 100);
        set(ConfPaths.BLOCKPLACE_DIRECTION_ACTIONS, "cancel vl>10 log:bpdirection:0:3:if cancel");

        set(ConfPaths.BLOCKPLACE_PROJECTILE_CHECK, true);
        set(ConfPaths.BLOCKPLACE_PROJECTILE_INTERVAL, 150);
        set(ConfPaths.BLOCKPLACE_PROJECTILE_ACTIONS,
                "cancel vl>150 log:bpprojectile:3:5:if cancel vl>1000 log:bpprojectile:3:5:cif cancel vl>4000 log:bpprojectile:3:5:cif cancel cmd:kick");

        /*** CHAT ***/

        set(ConfPaths.CHAT_HIDECOMMANDS, false);

        set(ConfPaths.CHAT_NOPWNAGE_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_WARNPLAYERS, false);
        set(ConfPaths.CHAT_NOPWNAGE_WARNOTHERS, false);
        set(ConfPaths.CHAT_NOPWNAGE_WARNLEVEL, 400);
        set(ConfPaths.CHAT_NOPWNAGE_WARNTIMEOUT, 30000);
        set(ConfPaths.CHAT_NOPWNAGE_BANLEVEL, 800);
        set(ConfPaths.CHAT_NOPWNAGE_ACTIONS, "log:nopwnage:2:5:cf cmd:kick cmd:ban cmd:ban-ip");

        set(ConfPaths.CHAT_NOPWNAGE_MOVE_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_MOVE_WEIGHTBONUS, 200);
        set(ConfPaths.CHAT_NOPWNAGE_MOVE_WEIGHTMALUS, 200);
        set(ConfPaths.CHAT_NOPWNAGE_MOVE_TIMEOUT, 30000);

        set(ConfPaths.CHAT_NOPWNAGE_REPEAT_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_REPEAT_WEIGHT, 150);
        set(ConfPaths.CHAT_NOPWNAGE_REPEAT_TIMEOUT, 5000);

        set(ConfPaths.CHAT_NOPWNAGE_SPEED_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_SPEED_WEIGHT, 200);
        set(ConfPaths.CHAT_NOPWNAGE_SPEED_TIMEOUT, 500);

        set(ConfPaths.CHAT_NOPWNAGE_FIRST_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_FIRST_WEIGHT, 200);
        set(ConfPaths.CHAT_NOPWNAGE_FIRST_TIMEOUT, 3000);

        set(ConfPaths.CHAT_NOPWNAGE_GLOBAL_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_GLOBAL_WEIGHT, 100);
        set(ConfPaths.CHAT_NOPWNAGE_GLOBAL_TIMEOUT, 5000);

        set(ConfPaths.CHAT_NOPWNAGE_BANNED_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_BANNED_WEIGHT, 200);
        set(ConfPaths.CHAT_NOPWNAGE_BANNED_TIMEOUT, 2000);

        set(ConfPaths.CHAT_NOPWNAGE_RELOG_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_RELOG_TIME, 1500);
        set(ConfPaths.CHAT_NOPWNAGE_RELOG_WARNINGS, 1);
        set(ConfPaths.CHAT_NOPWNAGE_RELOG_TIMEOUT, 60000);

        set(ConfPaths.CHAT_NOPWNAGE_CAPTCHA_CHECK, true);
        set(ConfPaths.CHAT_NOPWNAGE_CAPTCHA_TRIES, 20);
        set(ConfPaths.CHAT_NOPWNAGE_CAPTCHA_LENGTH, 4);
        set(ConfPaths.CHAT_NOPWNAGE_CAPTCHA_CHARACTERS,
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");

        set(ConfPaths.CHAT_ARRIVALSLIMIT_CHECK, false);
        set(ConfPaths.CHAT_ARRIVALSLIMIT_PLAYERSLIMIT, 3);
        set(ConfPaths.CHAT_ARRIVALSLIMIT_TIMEFRAME, 5000);
        set(ConfPaths.CHAT_ARRIVALSLIMIT_COOLDOWNDELAY, 5000);
        set(ConfPaths.CHAT_ARRIVALSLIMIT_NEWTIME, 600000);
        set(ConfPaths.CHAT_ARRIVALSLIMIT_KICKMESSAGE, "Please try again later!");
        set(ConfPaths.CHAT_ARRIVALSLIMIT_ACTIONS, "cancel");

        set(ConfPaths.CHAT_COLOR_CHECK, true);
        set(ConfPaths.CHAT_COLOR_ACTIONS, "log:color:0:1:if cancel");

        /*** FIGHT ***/

        set(ConfPaths.FIGHT_DIRECTION_CHECK, true);
        set(ConfPaths.FIGHT_DIRECTION_PRECISION, 75);
        set(ConfPaths.FIGHT_DIRECTION_PENALTYTIME, 500);
        set(ConfPaths.FIGHT_DIRECTION_ACTIONS,
                "cancel vl>5 log:fdirection:3:5:f cancel vl>20 log:fdirection:0:5:if cancel vl>50 log:fdirection:0:5:cif cancel");

        set(ConfPaths.FIGHT_NOSWING_CHECK, true);
        set(ConfPaths.FIGHT_NOSWING_ACTIONS, "log:fnoswing:0:5:cif cancel");

        set(ConfPaths.FIGHT_REACH_CHECK, true);
        set(ConfPaths.FIGHT_REACH_LIMIT, 400);
        set(ConfPaths.FIGHT_REACH_PENALTYTIME, 500);
        set(ConfPaths.FIGHT_REACH_ACTIONS, "cancel vl>10 log:freach:2:5:if cancel");

        set(ConfPaths.FIGHT_SPEED_CHECK, true);
        set(ConfPaths.FIGHT_SPEED_ATTACKLIMIT, 15);
        set(ConfPaths.FIGHT_SPEED_ACTIONS, "log:fspeed:0:5:if cancel");

        set(ConfPaths.FIGHT_GODMODE_CHECK, true);
        set(ConfPaths.FIGHT_GODMODE_ACTIONS, "log:fgod:2:5:if cancel");

        set(ConfPaths.FIGHT_INSTANTHEAL_CHECK, true);
        set(ConfPaths.FIGHT_INSTANTHEAL_ACTIONS, "log:fheal:1:1:if cancel");

        set(ConfPaths.STRINGS + ".drop",
                "[player] failed [check]: Tried to drop more items than allowed. VL [violations]");
        set(ConfPaths.STRINGS + ".moveshort", "[player] failed [check]. VL [violations]");
        set(ConfPaths.STRINGS + ".movelong",
                "[player] in [world] at [location] moving to [locationto] over distance [movedistance] failed check [check]. Total violation level so far [violations]");
        set(ConfPaths.STRINGS + ".nofall",
                "[player] failed [check]: tried to avoid fall damage for ~[falldistance] blocks. VL [violations]");
        set(ConfPaths.STRINGS + ".morepackets",
                "[player] failed [check]: Sent [packets] more packets than expected. Total violation level [violations]");
        set(ConfPaths.STRINGS + ".waterwalk",
                "[player] failed [check]: tried to walk on water. Total violation level [violations]");
        set(ConfPaths.STRINGS + ".bbfastbreak",
                "[player] failed [check]: tried to break too much blocks. Total violation level [violations]");
        set(ConfPaths.STRINGS + ".bbreach",
                "[player] failed [check]: tried to interact with a block over distance [reachdistance]. VL [violations]");
        set(ConfPaths.STRINGS + ".bbdirection",
                "[player] failed [check]: tried to interact with a block out of line of sight. VL [violations]");
        set(ConfPaths.STRINGS + ".bbnoswing", "[player] failed [check]: Didn't swing arm. VL [violations]");
        set(ConfPaths.STRINGS + ".bpfastplace",
                "[player] failed [check]: tried to place too much blocks. Total violation level [violations]");
        set(ConfPaths.STRINGS + ".bpreach",
                "[player] failed [check]: tried to interact with a block over distance [reachdistance]. VL [violations]");
        set(ConfPaths.STRINGS + ".bpdirection",
                "[player] failed [check]: tried to interact with a block out of line of sight. VL [violations]");
        set(ConfPaths.STRINGS + ".bpprojectile",
                "[player] failed [check]: tried to throw items too quicly. VL [violations]");
        set(ConfPaths.STRINGS + ".nopwnage", "Commands run for [player] ([ip]): [reason]!");
        set(ConfPaths.STRINGS + ".color",
                "[player] failed [check]: Sent colored chat message '[text]'. VL [violations]");
        set(ConfPaths.STRINGS + ".fdirection",
                "[player] failed [check]: tried to interact with a block out of line of sight. VL [violations]");
        set(ConfPaths.STRINGS + ".freach",
                "[player] failed [check]: tried to attack entity out of reach. VL [violations]");
        set(ConfPaths.STRINGS + ".fspeed",
                "[player] failed [check]: tried to attack more than [limit] times per second. VL [violations]");
        set(ConfPaths.STRINGS + ".fnoswing", "[player] failed [check]: Didn't swing arm. VL [violations]");
        set(ConfPaths.STRINGS + ".fgod", "[player] failed [check]: Avoided taking damage or lagging. VL [violations]");
        set(ConfPaths.STRINGS + ".fheal",
                "[player] failed [check]: Tried to regenerate health faster than normal. VL [violations]");
        set(ConfPaths.STRINGS + ".ibow", "[player] failed [check]: Fires bow to fast. VL [violations]");
        set(ConfPaths.STRINGS + ".ieat", "[player] failed [check]: Eats food [food] too fast. VL [violations]");
        set(ConfPaths.STRINGS + ".kick", "kick [player]");
        set(ConfPaths.STRINGS + ".ban", "ban [player]");
        set(ConfPaths.STRINGS + ".ban-ip", "ban-ip [ip]");

        // Update internal factory based on all the new entries to the "actions" section
        regenerateActionLists();
    }
}
