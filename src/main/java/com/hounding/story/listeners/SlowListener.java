package com.hounding.story.listeners;

import com.hounding.story.SlowArmorPlugin;
import com.hounding.story.helpers.WeightManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.Map;

public class SlowListener implements Listener {
    private WeightManager manager;
    private final SlowArmorPlugin plugin;

    public SlowListener(WeightManager manager, SlowArmorPlugin plugin){
        this.manager = manager;
        this.plugin = plugin;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player ->{
                    int playerArmorWeight = manager.calculateArmorWeight(player);
                    player.sendMessage("player weight " + playerArmorWeight);

                });
            }
        },0,20L);

    }

}
