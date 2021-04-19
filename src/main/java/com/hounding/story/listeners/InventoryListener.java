package com.hounding.story.listeners;

import com.hounding.story.SlowArmorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener implements Listener {

    private final SlowArmorPlugin plugin;


    public InventoryListener(SlowArmorPlugin plugin) {
        this.plugin = plugin;
        //this.worlds = plugin.getWorlds();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (getConfig().getBoolean("Server.Player.Weight.Enabled")
                        && player.getGameMode() != GameMode.CREATIVE
                        && player.getGameMode() != GameMode.SPECTATOR) {
                    int totalWeight = 0;
                    for (ItemStack armorContent : player.getInventory().getArmorContents()) {
                        if (armorContent != null && armorContent.getType() != Material.AIR) {
                            int weight = getWeight(armorContent);
                            if (weight != 0) {
                                totalWeight = totalWeight + (weight - 1);
                            }
                        }
                    }

                    if (totalWeight == 0) {
                        return;
                    }

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 25, totalWeight / 2));
                }
            });
        }, 0L, 20L);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,10,false,false));
        event.getPlayer().sendMessage("Test");
        player.sendMessage("Test2");
    }

    private int getWeight(ItemStack itemStack) {
        switch (itemStack.getType()) {
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
                return getConfig().getInt("Server.Player.Weight.Chainmail");
            /*case GOLD_BOOTS:
            case GOLD_LEGGINGS:
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
                return getConfig().getInt("Server.Player.Weight.Gold");*/
            case LEATHER_BOOTS:
            case LEATHER_LEGGINGS:
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
                return getConfig().getInt("Server.Player.Weight.Leather");
            case IRON_BOOTS:
            case IRON_LEGGINGS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
                return getConfig().getInt("Server.Player.Weight.Iron");
            case DIAMOND_BOOTS:
            case DIAMOND_LEGGINGS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
                return getConfig().getInt("Server.Player.Weight.Diamond");
            default:
                return 0;
        }
    }

    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
}
