package tr.elite.eliterising;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.Utilities.TeamModes;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Utilities.Teams.*;
import static tr.elite.eliterising.CommandStart.*;

public class CommandTeam implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info("[EliteRising] Komutu gönderen bir oyuncu olmalı!");
            return true;
        }
        Player player = (Player) sender;
        ChatColor teamColor = getColors(true).get(0);
        String teamName = EliteRising.translateColors(teamColor);

        if (!(IS_STARTED)) {
            sendError("Takım komutunun çalışabilmesi için oyunun başlaması gerek!",player);
            return true;
        }

        if (TEAM_MODE != TeamModes.NORMAL) {
            sendError("Takım ile ilgili komutlar sadece normal takım modunda çalışır!",player);
            return true;
        }

        String operator = args[0];
        if (operator == null) {
            sendError("Lütfen bir işlem seçiniz.",player);
            return true;
        }
        String[] valid_executors = new String[]{"oluştur","ayrıl","davet","mesaj","list"};
        if (!Arrays.toString(valid_executors).contains(operator)) {
            sendError("Lütfen geçerli bir işlem giriniz.",player);
            return true;
        }
        if (operator.equalsIgnoreCase("oluştur")) {
            if (IS_RISING) {
                sendError("Takım oluşturma komutu sadece lav yükselmeden önce kullanılabilir!",player);
                return true;
            }
            if (hasTeam(player)) {
                sendError("Zaten bir takımın var!",player);
                return true;
            }
            addPlayer(player,teamName);
            teamColors.remove(reverseColors(teamName));
            EliteRising.sendMessage(ChatColor.GREEN + "Takımın başarıyla oluşturuldu!",player);
        } else if (operator.equalsIgnoreCase("ayrıl")) {
            if (IS_RISING) {
                sendError("Takımdan ayrılma komutu sadece lav yükselmeden önce kullanılabilir!",player);
                return true;
            }
            if (!hasTeam(player)) {
                sendError("Ayrılacağın bir takımın olmalı!",player);
                return true;
            }
            removePlayer(player,getTeam(player));
            EliteRising.sendMessage(ChatColor.RED + "Takımından başarıyla ayrıldın!",player);
        } else if (operator.equalsIgnoreCase("davet")) {
            if (IS_RISING) {
                sendError("Takıma davet komutu sadece lav yükselmeden önce kullanılabilir!",player);
                return true;
            }
            if (!hasTeam(player)) {
                sendError("Bir takımın yok!",player);
                return true;
            }
            Player toInvitePlayer = Bukkit.getPlayer(args[1]);
            if (toInvitePlayer == null) {
                sendError("Oyuncu bulunamadı.",player);
                return true;
            }
            dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + toInvitePlayer.getName() + " {\"text\":\"[\",\"color\":\"gold\",\"extra\":[{\"text\":\"EliteRising\",\"color\":\"dark_red\"},{\"text\":\"]\",\"color\":\"gold\"},{\"text\":\" "+player.getName()+" \",\"color\":\"red\"},{\"text\":\"size takım daveti gönderdi.\",\"color\":\"yellow\"},{\"text\":\"\\n\"},{\"text\":\"[KABUL ET]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet kabul "+ player.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Daveti kabul et.\"}},{\"text\":\" \"},{\"text\":\"[REDDET]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet ret "+ player.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Daveti reddet.\"}}]}");
            sendMessage("Takım daveti " + toInvitePlayer.getName() + " adlı oyuncuya gönderildi!",player);
        } else if (operator.equalsIgnoreCase("mesaj")) {
            if (!hasTeam(player)) {
                sendError("Mesaj atacak bir takımın yok!",player);
                return true;
            }
            String msg = "";
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(args));
            for (String s : strings) {
                if (!(strings.get(0).equals(s))) {
                    msg += s + " ";
                }
            }
            String modified_msg = ChatColor.GOLD + "[" + getTeamColor(getTeam(player)) + "Takım" + ChatColor.GOLD + "] " + ChatColor.RED + player.getName() + ChatColor.YELLOW + ": " + ChatColor.AQUA + capitalize(msg);
            for (Player p : getTeamPlayers(getTeam(player))) {
                p.sendMessage(modified_msg);
                // [Takım] SadeceEmir: Sa knk
            }
        } else if (operator.equalsIgnoreCase("list")) {
            ArrayList<String> teams = new ArrayList<>(getTeams());
            if (teams.size() == 0) {
                sendMessage("Tüm takımlar: Takım yok" + ChatColor.RESET +
                        "\nSenin takımın: " + ((!hasTeam(player)) ? "Yok" : getTeamColor(getTeam(player)) + getTeam(player)),player);
                return true;
            }
            ArrayList<String> allTeams = new ArrayList<>();
            for (String team : teams) {
                allTeams.add(getTeamColor(team) + team);
            }
            String list = allTeams.toString().substring(1).replaceAll("]","");
            sendMessage("Tüm takımlar: " + list + ChatColor.RESET +
                    "\nSenin takımın: " + ((!hasTeam(player)) ? "Yok" : getTeamColor(getTeam(player)) + getTeam(player)),player);
        }
        return true;
    }
}
