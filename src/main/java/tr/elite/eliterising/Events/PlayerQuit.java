package tr.elite.eliterising.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.UUID;

import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Utilities.Teams.*;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.GOLD + e.getPlayer().getName() +  ChatColor.YELLOW + " oyundan ayrıldı!");
        Player player = e.getPlayer();
        if (IS_STARTED && hasTeam(player)) {
            String team = getTeam(player);
            removePlayer(player,team);
            oldTeams.put(player.getUniqueId(), team);
        }
    }

    public static HashMap<UUID, String> oldTeams = new HashMap<>();
}
