package tr.elite.eliterising.TabCompletes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StartComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("normal");
            completions.add("elytra");
            completions.add("op");
            completions.add("archery");
            completions.add("build");
        } else if (args.length == 2) {
            completions.add("normal");
            completions.add("solo");
            completions.add("duo");
            completions.add("triple");
            completions.add("squad");
            completions.add("half");
        } else if (args.length > 2) {
            completions.add("");
        }

        return completions;
    }
}
