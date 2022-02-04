package tr.elite.eliterising.Events;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Iterator;

import static tr.elite.eliterising.CommandStart.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Utilities.Teams.*;
import static tr.elite.eliterising.Events.PlayerQuit.*;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (IS_STARTED) {
            if (bbs.getPlayers().contains(player)) {
                bbs.removePlayer(player);
            }
            bbs.addPlayer(player);
            bbs.setVisible(true);

            if (oldTeams.containsKey(player.getUniqueId()) || keyContains(player.getUniqueId(),oldTeams)) {
                String team = oldTeams.get(player.getUniqueId());
                addPlayer(player,team);
                oldTeams.remove(player.getUniqueId(),team);
            }
        } else {
            player.setGameMode(GameMode.ADVENTURE);

            Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
            while (iterator.hasNext()) {
                AdvancementProgress progress = e.getPlayer().getAdvancementProgress(iterator.next());
                for (String criteria : progress.getAwardedCriteria()) {
                    progress.revokeCriteria(criteria);
                }
            }

            player.sendMessage(ChatColor.GOLD + "EliteRising Lav Yükseliyor" + ChatColor.AQUA + "'a hoş geldiniz");
            player.sendMessage(ChatColor.AQUA + "Oyunu başlatmak için: " + ChatColor.GOLD + "/başlat <Başlangıç Modu> <Takım Modu>");
            player.sendMessage(ChatColor.AQUA + "Takım komutları için: " + ChatColor.GOLD + "/takım <oluştur|ayrıl|davet|mesaj|list>");
            player.sendMessage(ChatColor.AQUA + "Sohbette genele mesaj atmak istiyorsanız mesajın başına " + ChatColor.GOLD + "!" + ChatColor.AQUA + " koymanız yeterlidir fakat takımınız yoksa başına hiçbir şey koymanıza gerek yok.");
            player.sendMessage(ChatColor.GOLD + "Sadece Emir " + ChatColor.AQUA + "tarafından yapılmıştır.");
        }
        e.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " oyuna katıldı!");
    }
}