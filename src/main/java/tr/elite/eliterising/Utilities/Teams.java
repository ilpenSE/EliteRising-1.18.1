package tr.elite.eliterising.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tr.elite.eliterising.CommandTeam;
import tr.elite.eliterising.EliteRising;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static tr.elite.eliterising.EliteRising.*;

/**
 * <h1>Teams</h1>
 * <p>
 *     This class contains everything about Teams and Team command. Don't change this file.
 *     Methods: {@link #distributeTeams(TeamModes)}, {@link #destroyTeams()}, {@link #getTeams()},
 *     {@link #getTeam(Player)}, {@link #addPlayer(Player, String)}, {@link #removePlayer(Player, String)}
 *     {@link #getTeamColor(String)}, {@link #getTeamPlayers(String)}
 *
 * </p>
 * <p>The Teams: {@link #teams}</p>
 * @author EliteDev, SadeceEmir
 * @since EL-1.1 <br></br>
 * @see CommandTeam
 */
public class Teams {
    private static final HashMap<Player, String> teams = new HashMap<>();

    public static void addPlayer(Player p, String teamName) {
        teams.put(p,teamName);
        String customName = reverseColors(teamName) + "[" + teamName + "] " + ChatColor.GOLD + p.getName();
        p.setPlayerListName(customName);
    }

    public static void removePlayer(Player p, String team) {
        ChatColor teamColor = getTeamColor(team);
        teams.remove(p,team);
        if (getTeamPlayers(team).size() == 1) { // isDelete
            teamColors.add(teamColor);
        }
        p.setPlayerListName(ChatColor.RESET + p.getName());
    }

    public static String getTeam(Player player) {
        return teams.get(player);
    }

    public static HashMap<Player,String> getMap() {
        return teams;
    }

     public static ArrayList<Player> getTeamPlayers(String team) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (getTeam(p) != null && getTeam(p).equals(team)) {
                players.add(p);
            }
        }
        return players;
     }

     public static ChatColor getTeamColor(String team) {
        return EliteRising.reverseColors(team);
     }

    public static HashSet<String> getTeams() {
        return new HashSet<>(teams.values());
    }

    public static boolean hasTeam(Player p) {
        return getTeam(p) != null;
    }

    public static boolean isExitsTeam(String team) {
        return getTeams().contains(team);
    }

    public static void removeTeam(String team) {
        for (Player p : getTeamPlayers(team)) {
            teams.remove(p,team);
        }
    }

    public static void distributeTeams(TeamModes teamMode) {
        ArrayList<Player> allPlayers = getPlayers();
        ArrayList<Player> teamedPlayers = new ArrayList<>();
        for (int i = 0;i < allPlayers.size() / teamMode.getNumber();i++) {
            // Baş Oyuncular
            addPlayer(getPlayers().get(i),translateColors(getColors(true).get(i)));
            teamedPlayers.add(getPlayers().get(i));
        }
        for (int i = 0;i < allPlayers.size() - teamedPlayers.size();i++) {
            // Yan Oyuncular
            int i1 = i + teamedPlayers.size();
            addPlayer(getPlayers().get(i1),getTeam(getPlayers().get(i)));
        }
    }

    public static void destroyTeams() {
        // KEY=PLAYER, VALUE=TEAM, KEY=VALUE, PLAYER=TEAM
        for (String team : getTeams()) {
            removeTeam(team);
        }
    }
}
