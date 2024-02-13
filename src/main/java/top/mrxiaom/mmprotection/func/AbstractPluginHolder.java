package top.mrxiaom.mmprotection.func;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.mmprotection.utils.ColorHelper;
import top.mrxiaom.mmprotection.MythicMobsProtection;
import top.mrxiaom.mmprotection.utils.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"unused"})
public abstract class AbstractPluginHolder {
    private static final Map<Class<?>, AbstractPluginHolder> registeredHolders = new HashMap<>();
    public final MythicMobsProtection plugin;
    public AbstractPluginHolder(MythicMobsProtection plugin) {
        this.plugin = plugin;
    }


    public void reloadConfig(MemoryConfiguration config) {

    }
    public void onDisable() {

    }
    protected void registerEvents(Listener listener) {
        try {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        } catch (Throwable t) {
            warn("An exception has been thrown when registering listener `" + this.getClass().getSimpleName() + "`", t);
        }
    }
    protected void registerCommand(String label, Object inst) {
        PluginCommand command = plugin.getCommand(label);
        if (command != null) {
            if (inst instanceof CommandExecutor){
                command.setExecutor((CommandExecutor) inst);
            } else{
                warn(inst.getClass().getSimpleName() + " is not a command executor");
            }
            if (inst instanceof TabCompleter) command.setTabCompleter((TabCompleter) inst);
        } else {
            info("Can't register command /" + label);
        }
    }
    protected void register() {
        registeredHolders.put(getClass(), this);
    }
    protected void unregister() {
        registeredHolders.remove(getClass());
    }
    protected boolean isRegistered() {
        return registeredHolders.containsKey(getClass());
    }

    public void info(String... lines) {
        for (String line : lines) {
            plugin.getLogger().info(line);
        }
    }
    public void warn(String... lines) {
        for (String line : lines) {
            plugin.getLogger().warning(line);
        }
    }
    public void warn(String s, Throwable t) {
        plugin.getLogger().warning(s);
        plugin.getLogger().warning(Util.stackTraceToString(t));
    }

    @Nullable
    @SuppressWarnings({"unchecked"})
    public static <T extends AbstractPluginHolder> T getOrNull(Class<T> clazz) {
        return (T) registeredHolders.get(clazz);
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends AbstractPluginHolder> Optional<T> get(Class<T> clazz) {
        T inst = (T) registeredHolders.get(clazz);
        if (inst == null) return Optional.empty();
        return Optional.of(inst);
    }

    public static void reloadAllConfig(MemoryConfiguration config) {
        for (AbstractPluginHolder inst : registeredHolders.values()) {
            inst.reloadConfig(config);
        }
    }

    public static void disableAllModule() {
        for (AbstractPluginHolder inst : registeredHolders.values()) {
            inst.onDisable();
        }
        registeredHolders.clear();
    }

    public static boolean t(CommandSender sender, String... msg) {
        sender.sendMessage(ColorHelper.parseColor(String.join("\n&r", msg)));
        return true;
    }
}
