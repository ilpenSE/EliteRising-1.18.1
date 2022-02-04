package tr.elite.eliterising.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static tr.elite.eliterising.EliteRising.*;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!IS_STARTED) {
            e.setCancelled(true);
        }
    }
}
