package top.mrxiaom.mmprotection;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.mmprotection.commands.CommandMain;
import top.mrxiaom.mmprotection.func.AbstractPluginHolder;
import top.mrxiaom.mmprotection.hook.mythic.*;
import top.mrxiaom.mmprotection.utils.Util;

import static top.mrxiaom.mmprotection.utils.Util.stackTraceToString;

@SuppressWarnings({"unused"})
public class MythicMobsProtection extends JavaPlugin implements Listener, TabCompleter {
    private static MythicMobsProtection instance;
    public static MythicMobsProtection getInstance() {
        return instance;
    }
    IMythicAPI mythicAPI;

    public IMythicAPI getMythicAPI() {
        return mythicAPI;
    }

    public boolean fixSkill = true;
    public boolean isMythicMobsEnabled = false;
    public boolean isResidenceEnabled = false;
    public boolean isGuildsEnabled = false;
    @Override
    public void onEnable() {
        instance = this;

        loadHooks();

        loadFunctions();
        reloadConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("Plugin enabled");
    }

    @SuppressWarnings({"unchecked"})
    public void loadFunctions() {
        try {
            for (Class<?> clazz : Lists.newArrayList(CommandMain.class)) {
                clazz.getDeclaredConstructor(getClass()).newInstance(this);
            }
        } catch (Throwable t) {
            getLogger().warning(stackTraceToString(t));
        }
    }

    public void loadHooks() {
        isResidenceEnabled = Util.isPresent("com.bekvon.bukkit.residence.api.ResidenceApi");
        isGuildsEnabled = Util.isPresent("me.glaremasters.guilds.api.GuildsAPI");

        Plugin mythicPlugin = Bukkit.getPluginManager().getPlugin("MythicMobs");
        String mythicVer = "not installed";
        if (mythicPlugin != null) {
            mythicVer = mythicPlugin.getDescription().getVersion();
            if (mythicVer.startsWith("4.")) {
                mythicAPI = new MythicAPI4();
                new AvoidDamage4(this);
                isMythicMobsEnabled = true;
                getLogger().info("MythicMobs 4.x has been hooked");
            }
            if (mythicVer.startsWith("5.")) {
                mythicAPI = new MythicAPI5();
                new AvoidDamage5(this);
                isMythicMobsEnabled = true;
                getLogger().info("MythicMobs 5.x has been hooked");
            }
        }
        if (!isMythicMobsEnabled) {
            getLogger().warning("Unknown MythicMobs version `" + mythicVer + "`");
        }
        if (isResidenceEnabled) {
            getLogger().info("Residence has been hooked");
        }
        if (isGuildsEnabled) {
            getLogger().info("Guilds has been hooked");
        }
    }

    @Override
    public void onDisable() {
        AbstractPluginHolder.disableAllModule();
        getLogger().info("Plugin disabled");
    }
}
