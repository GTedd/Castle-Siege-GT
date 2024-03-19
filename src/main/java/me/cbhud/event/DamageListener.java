package me.cbhud.event;

import me.cbhud.Main;
import me.cbhud.gui.Manager;
import me.cbhud.kits.KitType;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.entity.Player;
import java.util.Random;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener implements Listener {
    private final Main plugin;

    Random rand = new Random();
    private final TeamManager teamManager;

    public DamageListener(Main plugin) {

        this.plugin = plugin;
        this.teamManager = plugin.getTeamManager();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();

            if (plugin.getGame().getState() == GameState.LOBBY || plugin.getGame().getState() == GameState.END) {
                event.setCancelled(true);
                return;
            }
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;


                if (damageByEntityEvent.getDamager() instanceof Player || damageByEntityEvent.getDamager() instanceof Projectile) {
                    Player damager = null;

                    if (damageByEntityEvent.getDamager() instanceof Player) {
                        damager = (Player) damageByEntityEvent.getDamager();
                    } else if (damageByEntityEvent.getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) damageByEntityEvent.getDamager();
                        if (projectile.getShooter() instanceof Player) {
                            damager = (Player) projectile.getShooter();
                        }
                    }

                    if (damager != null) {
                        // Check if the damaged player and the damager are in the same team
                        Team damagedPlayerTeam = teamManager.getTeam(damagedPlayer);
                        Team damagerTeam = teamManager.getTeam(damager);

                        if (damagedPlayerTeam != null && damagedPlayerTeam.equals(damagerTeam)) {
                            // Players in the same team, cancel the damage event
                            event.setCancelled(true);
                        }
                        if(damager.getInventory().getItemInMainHand().getItemMeta().equals(Manager.sword.getItemMeta()) && plugin.getPlayerKitManager().getSelectedKit(damager) == KitType.WIZARD){
                            int n = rand.nextInt(15) + 1;
                            if (n == 6){
                                damagedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
                            }
                        }
                    }
                }
            }
        }
    }
}

