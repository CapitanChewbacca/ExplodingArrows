package com.capitanchewbacca.explodingarrows;

import com.capitanchewbacca.explodingarrows.util.FireworkEffectPlayer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Lorenzo on 08/06/2014.
 */
public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        if (this.getConfig().get("enabled") == null) this.getConfig().set("enabled", false);
        this.saveConfig();
    }

    @EventHandler
    public void onBowShoot(final EntityShootBowEvent e) {
        if (e.getProjectile() instanceof Arrow && ((Arrow) e.getProjectile()).getShooter() instanceof Player && this.getConfig().getBoolean("enabled")) {
            if (e.getBow().getItemMeta().getDisplayName() != null && e.getBow().getItemMeta().getDisplayName().startsWith("§r§bExplodingArrows")) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                    FireworkEffectPlayer fplayer = new FireworkEffectPlayer();
                    Random r = new Random();

                    FireworkEffect fe = FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255))).flicker(r.nextBoolean()).trail(r.nextBoolean()).build();

                    @Override
                    public void run() {
                        Location l = e.getProjectile().getLocation();
                        try {
                            fplayer.playFirework(l.getWorld(), l, fe);
                            e.getProjectile().remove();
                        } catch (Exception exc) {
                            exc.getStackTrace();
                        }
                    }
                }, 20);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("explodingarrows")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD+"---------- ExplodingArrows -------------------");
                sender.sendMessage(ChatColor.DARK_AQUA+"Status: "+((this.getConfig().getBoolean("enabled")) ? ChatColor.GREEN+"ENABLED" : ChatColor.RED+"DISABLED"));
                return true;
            }
            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                this.getConfig().set("enabled", Boolean.valueOf(args[0]));
                this.saveConfig();
                sender.sendMessage(ChatColor.AQUA+"Status of ExplodingArrows changed to "+((Boolean.valueOf(args[0])) ? ChatColor.GREEN+"ENABLED" : ChatColor.RED+"DISABLED"));
                return true;
            }
        }
        return true;
    }
}
