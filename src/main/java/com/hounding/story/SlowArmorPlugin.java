package com.hounding.story;

import com.hounding.story.helpers.WeightManager;
import com.hounding.story.listeners.InventoryListener;
import com.hounding.story.listeners.SlowListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.*;
import java.util.*;

@Getter
@Setter
public class SlowArmorPlugin extends JavaPlugin {
    private WeightManager weightManager = null;
    private static String message;
    private static SlowArmorPlugin plugin;

    public SlowArmorPlugin()
    {
        super();
    }

    protected SlowArmorPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        this.weightManager = new WeightManager(this);
        weightManager.setBukkitVersion(Bukkit.getBukkitVersion());
        if(weightManager.getBukkitVersion().contains("1.16")){
            weightManager.setNetherite(true);
        }
        //generateConfig();
        this.saveDefaultConfig();
        loadConfig();
        registerListeners();

        Bukkit.getServer().getConsoleSender().sendMessage("[SlowArmor] " + ChatColor.GREEN + "Configuration Loaded, Plugin enabled!");
    }
    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage("[SlowArmor] " + ChatColor.DARK_RED + "Plugin disabled!");
        plugin = null;
    }

    private void registerListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SlowListener(weightManager,this),this);
    }


    private Map<String,Integer> loadMap(String path){
        Set<String> weightSet = getConfig().getConfigurationSection(path).getKeys(false);
        Map<String,Integer> weightMap = new HashMap<>();
        for(String w : weightSet){
            if(w.contains("nether") && !this.weightManager.isNetherite()){
                continue;
            }
            weightMap.put(w,getConfig().getInt(path + "." + w));
        }
        return weightMap;
    }

    public void loadConfig(){
        this.weightManager.setWorlds(getConfig().getStringList("Worlds"));
        this.weightManager.setMaterialWeights(loadMap("Material-Weight"));
        this.weightManager.setArmorPartsWeights(loadMap("ArmorPart-Weight"));
    }
}
