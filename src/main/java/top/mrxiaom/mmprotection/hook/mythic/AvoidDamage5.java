package top.mrxiaom.mmprotection.hook.mythic;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import top.mrxiaom.mmprotection.MythicMobsProtection;
import top.mrxiaom.mmprotection.func.AbstractPluginHolder;

public class AvoidDamage5 extends AbstractPluginHolder implements Listener {
    MythicBukkit mythic = MythicBukkit.inst();
    public AvoidDamage5(MythicMobsProtection plugin) {
        super(plugin);
        registerEvents(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMythicDamage(MythicDamageEvent e) {
        if (!plugin.fixSkill) return;
        AbstractEntity casterEntity = e.getCaster().getEntity();
        Player caster = null;
        if (casterEntity.getBukkitEntity() instanceof Player) {
            caster = (Player) casterEntity.getBukkitEntity();
        }
        if (caster == null) {
            ActiveMob mob = mythic.getMobManager().getMythicMobInstance(casterEntity);
            if (mob != null) {
                caster = mob.getOwner().map(Bukkit::getPlayer).orElse(null);
            }
        }
        if (caster != null) {
            Entity entity = e.getTarget().getBukkitEntity();
            handleResidence(caster, entity, e);
            handleGuild(caster, entity, e);
        }
    }

    private void handleResidence(Player caster, Entity entity, MythicDamageEvent e) {
        if (!plugin.isResidenceEnabled || e.isCancelled()) return;

        ClaimedResidence res = ResidenceApi.getResidenceManager().getByLoc(entity.getLocation());
        if (res == null) return;

        if (entity instanceof Player) {
            // PVP权限
            if (!Residence.getInstance().getPermsByLoc(entity.getLocation()).has(Flags.pvp, FlagPermissions.FlagCombo.TrueOrNone)) {
                e.setCancelled(true);
            }
        } else if (entity instanceof Animals) {
            // 动物击杀权限
            if (res.getPermissions().playerHas(caster, Flags.animalkilling, FlagPermissions.FlagCombo.OnlyFalse)) {
                e.setCancelled(true);
            }
        } else if (entity instanceof Monster) {
            // 怪物击杀权限
            if (res.getPermissions().playerHas(caster, Flags.mobkilling, FlagPermissions.FlagCombo.OnlyFalse)) {
                e.setCancelled(true);
            }
        }
    }

    private void handleGuild(Player caster, Entity entity, MythicDamageEvent e) {
        if (!plugin.isGuildsEnabled || e.isCancelled()) return;

        if (entity instanceof Player) {
            Player player = (Player) entity;
            Guild guild1 = Guilds.getApi().getGuild(caster);
            Guild guild2 = Guilds.getApi().getGuild(player);
            if (guild1 == null || guild2 == null) return;
            if (guild1.getId().equals(guild2.getId())) {
                if (!caster.hasPermission("guilds.ffa.guild")) {
                    e.setCancelled(true);
                }
            }
            else if (guild1.getAllies().contains(guild2.getId())) {
                if (!caster.hasPermission("guilds.ffa.ally")) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
