package top.mrxiaom.mmprotection.hook.mythic;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MythicAPI5 implements IMythicAPI {
    MythicBukkit mythic = MythicBukkit.inst();

    @Override
    public Collection<String> getSkillNames() {
        return mythic.getSkillManager().getSkillNames();
    }

    @Override
    public boolean castSkill(Player player, String spell) {
        LivingEntity target = MythicUtil.getTargetedEntity(player);
        List<Entity> targets = new ArrayList<>();
        targets.add(target);
        return mythic.getAPIHelper().castSkill(player, spell, player, player.getLocation(), targets, null, 1.0F);
    }
}
