package tr.elite.eliterising.Events;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.EliteRising.sendMessage;
import static tr.elite.eliterising.Utilities.Teams.*;


public class PlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if ((!IS_STARTED) || (!IS_RISING)) {
            return;
        }
         Player player = event.getEntity();
         Entity killer = player.getKiller();
         player.setGameMode(GameMode.SPECTATOR);
         if (killer == null) {
             event.setDeathMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " öldü.");
         } else {
             player.teleport(killer.getLocation());
             event.setDeathMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW +  ", " + ChatColor.RED + killer.getName() + ChatColor.YELLOW + " tarafından öldürüldü.");
         }

        player.setPlayerListName(ChatColor.GRAY + "[ELENDİ] " + ChatColor.STRIKETHROUGH + player.getName());
        player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ÖLDÜN!","",5,100,5);
        diedPlayersList.add(player);
        player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(999999,255));

        ArrayList<String> uniqueTeams = new ArrayList<>();
        for (Player p : getOnlinePlayers()) {
            if (p != player && p.getGameMode().equals(GameMode.SURVIVAL)) {
                if (getTeam(p) == null) {
                    uniqueTeams.add("$" + p.getName());
                } else {
                    if (!(uniqueTeams.contains(getTeam(p)))) {
                        uniqueTeams.add(getTeam(p));
                    }
                }
            }
        }

        if (uniqueTeams.size() == 1) {
            for (Player p : getOnlinePlayers()) {
                if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                    p.getInventory().clear();
                }
            }
            String winnerTeam = uniqueTeams.get(0);
            String subtitle = "";
            if (winnerTeam.startsWith("$")) {
                // ${PLAYER_NAME} = Takımı olmayan oyuncular içindir.
                // Kazanan takımı olmayan biriyse (örn: uniqueTeams = [$SadeceEmir])
                Player winner = getPlayer(winnerTeam.substring(1));
                subtitle = ChatColor.RED + winner.getName() + ChatColor.GOLD + " oyunu kazandı";

                winner.setAllowFlight(true);
                sendMessage("Artık Uçabilirsin!",winner);
                winner.getInventory().clear();

                winner.setPlayerListName(setColorize("[KAZANAN] ",false) + ChatColor.GOLD + winner.getDisplayName());
                winner.sendTitle(setColorize("KAZANDIN!",true),"",5,100,5);

                Location location = winner.getLocation();
                Firework fw = winner.getWorld().spawn(winner.getEyeLocation(), Firework.class);
                FireworkMeta meta = fw.getFireworkMeta();
                FireworkEffect fe1 = FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BALL).build();
                meta.addEffect(fe1);
                fw.setFireworkMeta(meta);
                fw.setVelocity(winner.getLocation().getDirection().multiply(1));
                winner.getWorld().spawn(location,fw.getClass());
            } else {
                // Kazanan bir takımsa
                ArrayList<Player> winners = getTeamPlayers(winnerTeam);
                for (Player winner : winners) {
                    winner.setAllowFlight(true);
                    sendMessage("Artık Uçabilirsin!",winner);
                    winner.getInventory().clear();
                    subtitle = getTeamColor(winnerTeam) + winnerTeam + ChatColor.GOLD + " takımı oyunu kazandı";

                    winner.setPlayerListName(setColorize("[KAZANAN] ",false) + ChatColor.GOLD + winner.getDisplayName());
                    winner.sendTitle(setColorize("KAZANDIN!",true),"",5,100,5);

                    Location location = winner.getLocation();
                    Firework fw = winner.getWorld().spawn(winner.getEyeLocation(), Firework.class);
                    FireworkMeta meta = fw.getFireworkMeta();
                    FireworkEffect fe1 = FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BALL).build();
                    meta.addEffect(fe1);
                    fw.setFireworkMeta(meta);
                    fw.setVelocity(winner.getLocation().getDirection().multiply(1));
                    winner.getWorld().spawn(location,fw.getClass());
                }
            }
            for (Player diedPlayer : diedPlayersList) {
                String title = ChatColor.DARK_RED + "" + ChatColor.BOLD + "OYUN BİTTİ!";
                diedPlayer.sendTitle(title,subtitle,5,100,5);
            }
            IS_RISING = false;
            getServer().getWorlds().get(0).setGameRule(GameRule.FIRE_DAMAGE,false);
            getServer().getWorlds().get(0).setGameRule(GameRule.DROWNING_DAMAGE,false);
            getServer().getWorlds().get(0).setGameRule(GameRule.FALL_DAMAGE,false);
            clearAllInventories();
            destroyTeams();
            destroyItems();
        } else {
            int alivePlayers = getOnlinePlayers().size() - diedPlayersList.size();
            player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(999999,255));
            sendMessage("Son " + alivePlayers + " kişi kaldı.");
        }
    }

    public static ArrayList<Player> diedPlayersList = new ArrayList<>();

    public static String setColorize(String text, boolean bold) {
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(text.split("(?!^)")));
        ArrayList<ChatColor> colors = new ArrayList<>(getColors(false));
        int c = 0;
        String out = "";
        for(String s : chars) {
            out += colors.get(c) + "" + ((bold) ? ChatColor.BOLD : "") + s;
            c++;
            if(c >= colors.size()) {
                c = 0;
                Collections.shuffle(colors);
            }
        }
        return out;
    }
}
