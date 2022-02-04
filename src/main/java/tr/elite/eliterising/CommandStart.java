package tr.elite.eliterising;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import tr.elite.eliterising.Utilities.LavaUtils;
import tr.elite.eliterising.Utilities.TeamModes;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Utilities.TeamModes.*;
import static tr.elite.eliterising.Utilities.Teams.*;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info("Komutu gönderen bir oyuncu olmalı!");
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("start"))) {
            sendError("Bu komutu kullanmak için gereken iznin yok.",player);
            return true;
        }

        if (IS_STARTED || IS_RISING) {
            sendError("Oyun zaten başladı!",player);
            return true;
        }

        if (args.length >= 1) {
            START_MODE = args[0];
        }

        if (args.length >= 2) {
            TEAM_MODE = getModeByName(args[1]);
            if (TEAM_MODE == null) {
                sendError("Lütfen geçerli bir takım modu giriniz.",player);
                return true;
            }
        }

        IS_STARTED = true;

        clearAllInventories();
        setTakeDamage(false);

        ItemStack netherite_pickaxe = getPickaxe();

        //#region Start Modes
        if (START_MODE.equalsIgnoreCase("elytra")) {
            ItemStack elitra = new ItemStack(Material.ELYTRA);
            ItemStack fireworks = new ItemStack(Material.FIREWORK_ROCKET,5);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                player1.getInventory().addItem(netherite_pickaxe);
                player1.getInventory().addItem(elitra);
                player1.getInventory().addItem(fireworks);
            }
        } else if (START_MODE.equalsIgnoreCase("op")) {
            ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
            helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);

            ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);

            ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);

            ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
            boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);

            ItemStack gapple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,16);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                player1.getInventory().addItem(netherite_pickaxe);
                player1.getInventory().addItem(helmet);
                player1.getInventory().addItem(chestplate);
                player1.getInventory().addItem(leggings);
                player1.getInventory().addItem(boots);
                player1.getInventory().addItem(gapple);
            }
        } else if (START_MODE.equalsIgnoreCase("archery")) {
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_DAMAGE,3);
            bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,1);

            ItemStack arrow = new ItemStack(Material.ARROW,10);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                player1.getInventory().addItem(netherite_pickaxe);
                player1.getInventory().addItem(bow);
                player1.getInventory().addItem(arrow);
            }
        } else if (START_MODE.equalsIgnoreCase("build")) {
            ItemStack cobblestone = new ItemStack(Material.COBBLESTONE,64);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                player1.getInventory().addItem(netherite_pickaxe);
                player1.getInventory().addItem(cobblestone);
                player1.getInventory().addItem(cobblestone);
            }
        } else if (START_MODE.equalsIgnoreCase("normal")) {
            ItemStack apple = new ItemStack(Material.APPLE,4);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                player1.getInventory().addItem(netherite_pickaxe);
                player1.getInventory().addItem(apple);
            }
        }
        //#endregion

        //#region Team Modes
        if (TEAM_MODE == DUO) {
            if (getPlayers().size() % DUO.getNumber() > 0) {
                sendError("Oyuncu sayısı bu moda uymuyor.",player);
                return true;
            }
            distributeTeams(TeamModes.DUO);
        } else if (TEAM_MODE == TRIPLE) {
            if (getPlayers().size() % TRIPLE.getNumber() > 0) {
                sendError("Oyuncu sayısı bu moda uymuyor.",player);
                return true;
            }
            distributeTeams(TRIPLE);
        } else if (TEAM_MODE == SQUAD) {
            if (getPlayers().size() % SQUAD.getNumber() > 0) {
                sendError("Oyuncu sayısı bu moda uymuyor.",player);
                return true;
            }
            distributeTeams(SQUAD);
        } else if (TEAM_MODE == HALF) {
            if (getPlayers().size() % DUO.getNumber() > 0) {
                sendError("Oyuncu sayısı bu moda uymuyor.",player);
                return true;
            }
            distributeTeams(HALF);
        }
        //#endregion

        getServer().getWorlds().get(0).setTime(1000);

        bbs.setTitle(ChatColor.GOLD + "Kalan Süre " + ChatColor.YELLOW + gracePeriod + " Dakika");

        for (Player players : Bukkit.getOnlinePlayers()) {
            bbs.addPlayer(players);
            bbs.setVisible(true);
            players.setGameMode(GameMode.SURVIVAL);

            for (PotionEffect pot : players.getActivePotionEffects()) {
                players.removePotionEffect(pot.getType());
            }

            players.setHealth(20);
            players.setSaturation(20);
            players.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 255, false, false));

            players.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Oyun Başladı!",capitalize(START_MODE) + " Mod", 5, 100, 5);
        }

        setGameModes(GameMode.SURVIVAL);

        if (task == null) {
            task = new BukkitRunnable() {
                int seconds = gracePeriod * 60;
                @Override
                public void run() {
                    if ((seconds -= 1) == 0) {
                        task.cancel();
                    } else {
                        int sec = seconds % 60;
                        int min = seconds / 60;
                        int hrs = min / 60;
                        double progress = seconds / ((double) gracePeriod * 60);
                        bbs.setProgress(progress);
                        String TITLE = ChatColor.YELLOW + "Kalan Süre " + ChatColor.GOLD + (hrs > 0 ? hrs + " Saat " : "") + (min > 0 ? min + " Dakika " : "") + (sec > 0 ? sec + " Saniye" : "");
                        bbs.setTitle(TITLE);
                    }
                }
            }.runTaskTimer(instance, 0L, 20L);
        }

        getScheduler().scheduleSyncDelayedTask(instance, () -> {
            doLava();
            setTakeDamage(true);
            IS_RISING = true;
            getWorlds().get(0).setPVP(true);
            getServer().getWorlds().get(0).setGameRule(GameRule.FIRE_DAMAGE,true);
            getServer().getWorlds().get(0).setGameRule(GameRule.DROWNING_DAMAGE,true);
            getServer().getWorlds().get(0).setGameRule(GameRule.FALL_DAMAGE,true);
        }, 20L * 60L * ((long) gracePeriod));

        return true;
    }

    public static String START_MODE = "normal";
    public static TeamModes TEAM_MODE = NORMAL;
    private BukkitTask task;
    public static int gracePeriod = instance.getConfiguration().getInt("gracePeriod",10);

    public static BossBar bbs = createBossBar("EliteRising", BarColor.YELLOW, BarStyle.SEGMENTED_10);

    public static ItemStack getPickaxe() {
        ItemStack netherite_pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        netherite_pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
        netherite_pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
        netherite_pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
        ItemMeta m = netherite_pickaxe.getItemMeta();
        assert m != null;
        m.setDisplayName(ChatColor.GOLD + "Elite" + ChatColor.YELLOW + "Rising");
        netherite_pickaxe.setItemMeta(m);
        return netherite_pickaxe;
    }

    private void lavaTimer() {
        getScheduler().scheduleSyncDelayedTask(instance, this::doLava, 0L);
    }

    private void doLava() {
        bbs.setProgress(1);
        bbs.setTitle(ChatColor.YELLOW + "10 Saniye Sonra Lav Yükselecek!");

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 9 saniye kaldı
            bbs.setProgress(0.9);
            bbs.setTitle(ChatColor.YELLOW + "9 Saniye Sonra Lav Yükselecek!");
        }, 20L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 8 saniye kaldı
            bbs.setProgress(0.8);
            bbs.setTitle(ChatColor.YELLOW + "8 Saniye Sonra Lav Yükselecek!");
        }, 20L * 2L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 7 saniye kaldı
            bbs.setProgress(0.7);
            bbs.setTitle(ChatColor.YELLOW + "7 Saniye Sonra Lav Yükselecek!");
        }, 20L * 3L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 6 saniye kaldı
            bbs.setProgress(0.6);
            bbs.setTitle(ChatColor.YELLOW + "6 Saniye Sonra Lav Yükselecek!");
        }, 20L * 4L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 5 saniye kaldı
            bbs.setProgress(0.5);
            bbs.setTitle(ChatColor.YELLOW + "5 Saniye Sonra Lav Yükselecek!");
        }, 20L * 5L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 4 saniye kaldı
            bbs.setProgress(0.4);
            bbs.setTitle(ChatColor.YELLOW + "4 Saniye Sonra Lav Yükselecek!");
        }, 20L * 6L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 3 saniye kaldı
            bbs.setProgress(0.3);
            bbs.setTitle(ChatColor.YELLOW + "3 Saniye Sonra Lav Yükselecek!");
        }, 20L * 7L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 2 saniye kaldı
            bbs.setProgress(0.2);
            bbs.setTitle(ChatColor.YELLOW + "2 Saniye Sonra Lav Yükselecek!");
        }, 20L * 8L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 1 saniye kaldı
            bbs.setProgress(0.1);
            bbs.setTitle(ChatColor.YELLOW + "1 Saniye Sonra Lav Yükselecek!");
        }, 20L * 9L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // Lav Yükseliyor!
            bbs.setProgress(1);
            bbs.setTitle(ChatColor.DARK_RED + "Lav Yükseliyor!");
            bbs.setColor(BarColor.RED);

            LavaUtils lavaUtils = instance.getLavaUtils();

            World world = lavaUtils.bottomRight.getWorld();
            Location edgeMin = lavaUtils.bottomRight;
            Location edgeMax = lavaUtils.topLeft;

            if (edgeMin.getBlockY() >= 320) {
                IS_RISING = false;
                setTakeDamage(false);
                bbs.setColor(BarColor.RED);
                bbs.setTitle(ChatColor.DARK_RED + "Lav Maksimum Seviyede!");
                bbs.setProgress(1);
            } else {
                for (int x = edgeMin.getBlockX(); x <= edgeMax.getBlockX(); x++) {
                    for (int y = edgeMin.getBlockY(); y <= edgeMax.getBlockY(); y++) {
                        for (int z = edgeMin.getBlockZ(); z <= edgeMax.getBlockZ(); z++) {
                            Block block = new Location(world, x, y, z).getBlock();

                            if (block.getType() == Material.AIR) {
                                block.setType(instance.getBlock());
                            }
                        }
                    }
                }
                lavaUtils.IncreaseCurrentLevel();
                lavaTimer();

                bbs.setColor(BarColor.YELLOW);
            }
        }, 20L * 10L);
    }
}