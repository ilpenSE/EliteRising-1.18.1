package tr.elite.eliterising.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tr.elite.eliterising.EliteRising;

import static tr.elite.eliterising.Utilities.Teams.*;

public class PlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player chatter = e.getPlayer();
        if (getTeam(chatter) == null) {
            // Takımı yok bu yüzden genele mesaj atacak.
            e.setFormat(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Genel" +  ChatColor.GOLD + "] " + chatter.getName() + ChatColor.YELLOW + ": " + EliteRising.capitalize(e.getMessage()));
        } else {
            String teamChatter = getTeam(chatter);
            if (e.getMessage().startsWith("!")) {
                // Takımı var ama genele mesaj atıyor
                e.setFormat(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Genel" +  ChatColor.GOLD + "] " + getTeamColor(teamChatter) + chatter.getName() + ChatColor.YELLOW + ": " + EliteRising.capitalize(e.getMessage().substring(1)));
            } else {
                // Takımı var ve takıma msg atıyor
                e.setCancelled(true);
                if (getTeamPlayers(teamChatter).size() == 1) {
                    chatter.sendMessage(ChatColor.GOLD + "[" + getTeamColor(teamChatter) + "Takım" +  ChatColor.GOLD + "] " + chatter.getName() + ChatColor.YELLOW + ": " + ChatColor.AQUA + EliteRising.capitalize(e.getMessage()));
                } else {
                    for (Player p : getTeamPlayers(teamChatter)) {
                        p.sendMessage(ChatColor.GOLD + "[" + getTeamColor(teamChatter) + "Takım" +  ChatColor.GOLD + "] " + chatter.getName() + ChatColor.YELLOW + ": " + ChatColor.AQUA + EliteRising.capitalize(e.getMessage()));
                    }
                }
            }
        }
    }
}
