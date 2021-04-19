package com.hounding.story;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.tags.MaterialTagMock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SlowArmorPluginTest {

    private ServerMock serverMock;
    private SlowArmorPlugin plugin;

    @Before
    public void setUp(){
        serverMock = MockBukkit.mock();
        plugin = (SlowArmorPlugin) MockBukkit.load(SlowArmorPlugin.class);
    }

    @Test
    public void loadArmorPartWeightsTest(){
        Assert.assertEquals(5,(int)plugin.getWeightManager().getArmorPartsWeights().get("helmet"));
        Assert.assertEquals(8,(int)plugin.getWeightManager().getArmorPartsWeights().get("chestplate"));
        Assert.assertEquals(7,(int)plugin.getWeightManager().getArmorPartsWeights().get("leggings"));
        Assert.assertEquals(4,(int)plugin.getWeightManager().getArmorPartsWeights().get("boots"));
        Assert.assertEquals(4,plugin.getWeightManager().getArmorPartsWeights().size());
    }

    @Test
    public void loadMaterialWeightsTest(){
        Assert.assertEquals(5,(int)plugin.getWeightManager().getMaterialWeights().get("netherite"));
        Assert.assertEquals(4,(int)plugin.getWeightManager().getMaterialWeights().get("diamond"));
        Assert.assertEquals(1,(int)plugin.getWeightManager().getMaterialWeights().get("leather"));
        Assert.assertEquals(2,(int)plugin.getWeightManager().getMaterialWeights().get("chain"));
        Assert.assertEquals(3,(int)plugin.getWeightManager().getMaterialWeights().get("iron"));
        Assert.assertEquals(3,(int)plugin.getWeightManager().getMaterialWeights().get("gold"));
        Assert.assertEquals(6,plugin.getWeightManager().getMaterialWeights().size());
    }

    @Test
    public void getMaterialWeightTest(){

        Assert.assertEquals(5,plugin.getWeightManager().getMaterialWeight(new ItemStack(Material.NETHERITE_HELMET)));
        Assert.assertEquals(4,plugin.getWeightManager().getMaterialWeight(new ItemStack(Material.DIAMOND_HELMET)));
        Assert.assertEquals(3,plugin.getWeightManager().getMaterialWeight(new ItemStack(Material.IRON_HELMET)));
        Assert.assertEquals(3,plugin.getWeightManager().getMaterialWeight(new ItemStack(Material.GOLDEN_HELMET)));
        Assert.assertEquals(2,plugin.getWeightManager().getMaterialWeight(new ItemStack(Material.CHAINMAIL_HELMET)));
        Assert.assertEquals(1,plugin.getWeightManager().getMaterialWeight(new ItemStack(Material.LEATHER_HELMET)));
    }

    @Test
    public void getArmorPartMultiplierTest(){
        Assert.assertEquals(5,plugin.getWeightManager().getArmorPartMultipiler(new ItemStack(Material.NETHERITE_HELMET)));
        Assert.assertEquals(5,plugin.getWeightManager().getArmorPartMultipiler(new ItemStack(Material.GOLDEN_HELMET)));
        Assert.assertEquals(8,plugin.getWeightManager().getArmorPartMultipiler(new ItemStack(Material.IRON_CHESTPLATE)));
        Assert.assertEquals(7,plugin.getWeightManager().getArmorPartMultipiler(new ItemStack(Material.CHAINMAIL_LEGGINGS)));
        Assert.assertEquals(4,plugin.getWeightManager().getArmorPartMultipiler(new ItemStack(Material.DIAMOND_BOOTS)));
    }


    @Test
    public void calculateWeightTest(){
        PlayerMock player = serverMock.addPlayer();
        Assert.assertEquals(0,plugin.getWeightManager().calculateArmorWeight(player));
        ItemStack[] armor = new ItemStack[4];
        armor[0] = null;
        armor[1] = null;
        armor[2] = null;
        armor[3] = new ItemStack(Material.DIAMOND_HELMET, 1);

        player.getInventory().setArmorContents(armor);

        //Assert.assertEquals(Material.DIAMOND_HELMET.toString(),Material.DIAMOND_HELMET.toString());
        Assert.assertEquals(Material.DIAMOND_HELMET,player.getInventory().getHelmet().getType());
        Assert.assertEquals(4,plugin.getWeightManager().getMaterialWeight(player.getInventory().getHelmet()));
        Assert.assertEquals(5,plugin.getWeightManager().getArmorPartMultipiler(player.getInventory().getHelmet()));
        Assert.assertEquals(20,plugin.getWeightManager().calculateArmorWeight(player));

        armor[0] = new ItemStack(Material.GOLDEN_BOOTS, 1); //4x3
        armor[1] = new ItemStack(Material.DIAMOND_LEGGINGS, 1); //7x4
        armor[2] = new ItemStack(Material.NETHERITE_CHESTPLATE, 1); //8x5
        armor[3] = new ItemStack(Material.IRON_HELMET, 1); //5x3
        player.getInventory().setArmorContents(armor);
        //12 + 28 + 40 + 15 = 95
        Assert.assertEquals(95,plugin.getWeightManager().calculateArmorWeight(player));
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}