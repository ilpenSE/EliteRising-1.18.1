package tr.elite.eliterising.TabCompletes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("oluştur");
            completions.add("ayrıl");
            completions.add("davet");
            completions.add("mesaj");
            completions.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("davet")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            } else if (args[0].equalsIgnoreCase("mesaj")) {
                completions.add("");
            }
        }

        return completions;
    }
}
