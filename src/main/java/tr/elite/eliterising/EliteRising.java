package tr.elite.eliterising;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tr.elite.eliterising.Events.*;
import tr.elite.eliterising.TabCompletes.StartComplete;
import tr.elite.eliterising.TabCompletes.TeamComplete;
import tr.elite.eliterising.Utilities.ColorLang;
import tr.elite.eliterising.Utilities.LavaUtils;

import java.util.*;

import static org.bukkit.Bukkit.*;

/**
 * =============================================
 *  <h1>EliteRising</h1>
 *  <p>EliteRising <span style="font-weight:bold">v1.1</span> lava rising package. This package is only <span style="font-weight:bold">Turkish</span> for now.</p>
 *  <p>Sadece Emir, Founder of <span style="font-weight:bold">EliteDevelopment</span></p>
 *  <p>VIA: <a href="https://www.spigotmc.org/resources/the-floor-is-lava.70255/">TheFloorIsLava</a></p>
 *  <p>NOTE: This plugin is modded package of TheFloorIsLava</p>
 *  <p>Commands: {@link CommandStart}, {@link CommandTeam}</p>
 * =============================================
 */
public final class EliteRising extends JavaPlugin {
    public static EliteRising instance;
    FileConfiguration configuration = getConfig();

    public static Location spawn;
    private LavaUtils lavaUtils;

    public int size;
    private int startLevel;
    private int increaseAmount;

    public static boolean IS_RISING = false;
    public static boolean IS_STARTED = false;

    public static String TEMPLATE = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "EliteRising" + ChatColor.GOLD + "] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        instance = this;

        configuration.options().copyDefaults(true);
        saveConfig();

        size = configuration.getInt("borderSize",200);
        startLevel = configuration.getInt("startHeight",-54);
        increaseAmount = configuration.getInt("increaseAmount",2);

        // MODDED
        if (Material.getMaterial(getConfigration("block",configuration)) == null) {
            getLogger().info("Seçenekler dosyasında geçersiz blok bulundu: ('" + configuration.getString("block") + "') 'LAVA' olarak düzeltildi.");
            configuration.set("block", "LAVA");
        }

        spawn = getServer().getWorlds().get(0).getSpawnLocation();

        getServer().getWorlds().get(0).getWorldBorder().setCenter(spawn);
        getServer().getWorlds().get(0).getWorldBorder().setSize(size);
        getServer().setSpawnRadius(0);

        Location bottomRight = spawn.clone().subtract((double) size / 2D, 0, (double) size / 2D);
        Location topLeft = spawn.clone().add((double) size / 2D, 0, (double) size / 2D);

        lavaUtils = new LavaUtils(bottomRight, topLeft, startLevel, increaseAmount);

        Bukkit.getWorlds().get(0).setPVP(false);
        new ColorLang().init();
        for (char ch : "c9ae4126bd57f380".toCharArray()) {
            teamColors.add(ChatColor.getByChar(ch));
        }
        setTakeDamage(false);

        // ADDED
        getServer().getWorlds().get(0).setGameRule(GameRule.FIRE_DAMAGE,false); // v1.1
        getServer().getWorlds().get(0).setGameRule(GameRule.DROWNING_DAMAGE,false); // v1.1
        getServer().getWorlds().get(0).setGameRule(GameRule.FALL_DAMAGE,false); // v1.1
        getServer().getWorlds().get(0).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
        getServer().getWorlds().get(0).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
        getServer().getWorlds().get(0).setGameRule(GameRule.KEEP_INVENTORY,false); // v1.1

        // MODDED & ADDED
        instance.getCommand("başlat").setExecutor(new CommandStart());
        instance.getCommand("takım").setExecutor(new CommandTeam());
        instance.getCommand("davet").setExecutor(new CommandInvite());

        // ADDED
        instance.getCommand("başlat").setTabCompleter(new StartComplete());
        instance.getCommand("takım").setTabCompleter(new TeamComplete());

        // ADDED
        getServer().getPluginManager().registerEvents(new PlayerDeath(),instance);
        getServer().getPluginManager().registerEvents(new PlayerJoin(),instance);
        getServer().getPluginManager().registerEvents(new PortalCreate(),instance);
        getServer().getPluginManager().registerEvents(new PlayerQuit(),instance);
        getServer().getPluginManager().registerEvents(new PlayerChat(),instance);
        getServer().getPluginManager().registerEvents(new PlayerDamage(),instance);
        getServer().getPluginManager().registerEvents(new PlayerMove(),instance);
    }

    public static void sendMessage(String msg, Player p) {
        p.sendMessage(TEMPLATE + msg);
    }

    public static void sendMessage(String msg) {
        Bukkit.broadcastMessage(TEMPLATE + msg);
    }

    public static void sendError(String msg, Player p) {
        p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "EliteRising/HATA" + ChatColor.GOLD + "] " + ChatColor.RED + msg);
    }

    public static void clearAllInventories() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                p.getInventory().clear();
            }
        }
    }

    public static void destroyItems() {
        World world = getWorlds().get(0);
        List<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof Item) {
                entity.remove();
            }
        }
    }

    public static void setGameModes(GameMode gm) {
        for (Player player : getOnlinePlayers()) {
            player.setGameMode(gm);
        }
    }

    public static String getConfigration(String s, FileConfiguration config) {
        return Objects.requireNonNull(config.getString(s));
    }

    public Location getSpawn() {
        return spawn;
    }

    public LavaUtils getLavaUtils() {
        return lavaUtils;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public Material getBlock() {
        return Material.getMaterial(Objects.requireNonNull(configuration.getString("block")));
    }

    public static ArrayList<ChatColor> teamColors = new ArrayList<>();

    public static ArrayList<ChatColor> getColors(boolean isTeamColor) {
        if (isTeamColor) {
            return teamColors;
        } else {
            ArrayList<ChatColor> colors = new ArrayList<>();
            for (char ch : "123456780abcdef".toCharArray()) {
                colors.add(ChatColor.getByChar(ch));
            }
            return colors;
        }
    }

    public static String translateColors(ChatColor color) {
        // DARK_RED -> Koyu Kırmızı
        return ColorLang.translateToTR(color.name());
    }

    public static ChatColor reverseColors(String color) {
        // Koyu Kırmızı -> DARK_RED
        return ChatColor.valueOf(ColorLang.translateToEN(color));
    }

    public static boolean keyContains(UUID key, HashMap<UUID,String> map) {
        ArrayList<UUID> uuids = new ArrayList<>(map.keySet());
        return uuids.contains(key);
    }

    public static void setTakeDamage(boolean value) {
        TAKE_DAMAGE = value;
    }

    public static boolean TAKE_DAMAGE = false;

    public static ArrayList<Player> getPlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    // ADDED
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }
}