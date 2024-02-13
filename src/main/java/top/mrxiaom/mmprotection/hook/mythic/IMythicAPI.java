package top.mrxiaom.mmprotection.hook.mythic;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface IMythicAPI {
    Collection<String> getSkillNames();
    boolean castSkill(Player player, String spell);
}
