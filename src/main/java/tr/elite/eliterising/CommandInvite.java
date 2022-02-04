package tr.elite.eliterising;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Utilities.Teams.*;

public class CommandInvite implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Komutu gönderen bir oyuncu olmalı!");
            return true;
        }
        Player player = (Player) sender;

        if (IS_RISING) {
            sendError("Davet komutu sadece lav yükselmeden önce kullanılabilir!",player);
            return true;
        }

        /* ERROR CODES
         * 1 ERROR CODE = INVITER ISN'T ONLINE.
         * 1 HATA KODU = DAVET EDEN AKTİF DEĞİL.
         */
        String executor = args[0];
        if (executor.equalsIgnoreCase("kabul")) {
            // /davet kabul INVITER
            Player inviter = getPlayer(args[1]);
            if (inviter == null) {
                EliteRising.sendError("Davet Geçersiz. (1)",player);
                return true;
            }
            String join_team = getTeam(inviter);
            if (getTeam(player) != null) {
                dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + player.getName() + " {\"text\":\"[\",\"color\":\"gold\",\"extra\":[{\"text\":\"EliteRising\",\"color\":\"dark_red\"},{\"text\":\"] \",\"color\":\"gold\"},{\"text\":\""+join_team+"\",\"color\":\"gold\"},{\"text\":\" takımına katılırken şuanki takımından ayrılacaksın. Onaylıyor musun? \",\"color\":\"yellow\"},{\"text\":\"[EVET]\",\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Onaylıyorum\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet reKabul "+ player.getName() +" "+inviter.getName()+"\"}},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"[HAYIR]\",\"color\":\"red\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Onaylamıyorum\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet ret "+inviter.getName()+"\"}}]}");
                return true;
            }
            addPlayer(player,join_team);
            EliteRising.sendMessage(ChatColor.GREEN + "Yeni takımına hoş geldin!",player);
            EliteRising.sendMessage(ChatColor.GREEN + "Takım davetin kabul edildi!",inviter);
        } else if (executor.equalsIgnoreCase("ret")) {
            Player inviter = Bukkit.getPlayer(args[1]);
            if (inviter == null) {
                EliteRising.sendError("Davet Geçersiz. (1)",player);
                return true;
            }
            EliteRising.sendMessage(ChatColor.RED + "Daveti reddettin.",player);
            EliteRising.sendMessage(ChatColor.RED + "Takım davetin reddedildi!",inviter);
        } else if (executor.equalsIgnoreCase("reKabul")) {
            // /davet reKabul INVITED INVITER
            Player inviter = Bukkit.getPlayer(args[2]);
            if (inviter == null) {
                EliteRising.sendError("Davet Geçersiz. (1)",player);
                return true;
            }
            removePlayer(player,getTeam(player));
            addPlayer(player,getTeam(inviter));
            EliteRising.sendMessage(ChatColor.GREEN + "Daveti kabul ettin!",player);
            EliteRising.sendMessage(ChatColor.GREEN + "Takım davetin kabul edildi!",inviter);
        }
        return true;
    }
}
