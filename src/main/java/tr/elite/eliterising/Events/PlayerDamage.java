package tr.elite.eliterising.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import tr.elite.eliterising.EliteRising;
import tr.elite.eliterising.Utilities.Teams;
import static tr.elite.eliterising.EliteRising.*;

public class PlayerDamage implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (IS_STARTED) {
            if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
                String damagerTeam = Teams.getTeam((Player) e.getDamager());
                String defenderTeam = Teams.getTeam((Player) e.getEntity());
                if (damagerTeam == null || defenderTeam == null) {
                    return;
                }
                if (damagerTeam.equals(defenderTeam)) {
                    e.setCancelled(true);
                    EliteRising.sendError("Takım arkadaşına vuramazsın.",(Player) e.getDamager());
                }
            }
        }
        if (!TAKE_DAMAGE) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }
}
