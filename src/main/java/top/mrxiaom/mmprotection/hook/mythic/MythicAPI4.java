package top.mrxiaom.mmprotection.hook.mythic;

import io.lumine.xikage.mythicmobs.util.MythicUtil;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MythicAPI4 implements IMythicAPI {
    MythicMobs mythic = MythicMobs.inst();

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
