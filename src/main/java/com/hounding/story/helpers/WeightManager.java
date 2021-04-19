package com.hounding.story.helpers;

import com.hounding.story.SlowArmorPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.management.BufferPoolMXBean;
import java.util.*;

@Getter
@Setter
public class WeightManager {
    private static SlowArmorPlugin slowArmorPlugin;
    private String bukkitVersion;
    private List<String> worlds = new ArrayList<>();
    private Map<String,Integer>  materialWeights = new HashMap<>();
    private Map<String,Integer> armorPartsWeights = new HashMap<>();
    private int message;
    private boolean isNetherite;

    public WeightManager(SlowArmorPlugin slowArmorPlugin){
        this.bukkitVersion = Bukkit.getVersion();
        this.slowArmorPlugin = slowArmorPlugin;
    }

    int getValueFromMap(Map<String,Integer> weightMap, String matName, List<String> contMatList){
        for(String s : contMatList){
            if(matName.contains(s)) {
                return weightMap.get(s);
            }
        }
        return 0;
    }

    public int getMaterialWeight(ItemStack armorContent){
        if(this.isNetherite &&
                armorContent.getType().toString().toLowerCase().contains("netherite"))
        {
            return this.materialWeights.get("netherite");
        }
        String materialName = armorContent.getType().toString().toLowerCase();

        return getValueFromMap(this.materialWeights,
                materialName,
                Arrays.asList("diamond","iron","gold","leather","chain"));
    }

    public int getArmorPartMultipiler(ItemStack armorContent){
        String materialName = armorContent.getType().toString().toLowerCase();

        return getValueFromMap(
                this.armorPartsWeights,
                materialName,
                Arrays.asList("chestplate","leggings","helmet","boots")
        );
    }

    public int calculateArmorWeight(Player player){
        int totalWeight = 0;
        for (ItemStack armorContent : player.getInventory().getArmorContents()) {
            if (armorContent != null && armorContent.getType() != Material.AIR) {
                totalWeight += getMaterialWeight(armorContent)*getArmorPartMultipiler(armorContent);
            }
        }
        return totalWeight;
    }
}
