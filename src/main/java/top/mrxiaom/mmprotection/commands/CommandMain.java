package top.mrxiaom.mmprotection.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.mmprotection.MythicMobsProtection;
import top.mrxiaom.mmprotection.func.AbstractPluginHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static top.mrxiaom.mmprotection.utils.Util.startsWith;

public class CommandMain extends AbstractPluginHolder implements CommandExecutor, TabCompleter {
    public CommandMain(MythicMobsProtection plugin) {
        super(plugin);
        registerCommand("mythicmobsprotection", this);
    }

    public static Optional<Player> getOnlinePlayer(String name) {
        Player player = Bukkit.getOnlinePlayers().stream()
                .filter(it -> name.equalsIgnoreCase(it.getName()))
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(player);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length > 2) {
            if (args[0].equalsIgnoreCase("skill")) {
                Player player = getOnlinePlayer(args[1]).orElse(null);
                if (player == null) {
                    return t(sender, "&7[&eMythicMobsProtection&7] &cPlayer " + args[1] + " not online!");
                }
                String spell = args[2];
                plugin.getMythicAPI().castSkill(player, spell);
                return true;
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            plugin.fixSkill = !plugin.fixSkill;
            t(sender, "&7[&eMythicMobsProtection&7] &fProtection has been " + (plugin.fixSkill ? "&aEnabled" : "&cDisabled"));
        }
        return true;
    }

    private static final List<String> emptyList = Lists.newArrayList();
    private static final List<String> listArg0 = emptyList;
    private static final List<String> listOpArg0 = Lists.newArrayList(
            "skill", "toggle");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(args[0], sender.isOp() ? listOpArg0 : listArg0);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("skill")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(it -> it.startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("skill")) {
                return plugin.getMythicAPI().getSkillNames().stream()
                        .filter(it -> it.startsWith(args[2]))
                        .collect(Collectors.toList());
            }
        }
        return emptyList;
    }
}
