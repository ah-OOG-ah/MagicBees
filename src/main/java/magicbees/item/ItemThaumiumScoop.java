package magicbees.item;

import magicbees.main.CommonProxy;
import magicbees.main.utils.compat.ThaumcraftHelper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import thaumcraft.api.IRepairable;
import thaumcraft.api.ThaumcraftApi;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.IToolScoop;

@Optional.InterfaceList({
        @Optional.Interface(iface = "thaumcraft.api.IRepairable", modid = ThaumcraftHelper.Name, striprefs = true) })
public class ItemThaumiumScoop extends Item implements IRepairable, IToolScoop {

    public ItemThaumiumScoop() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(30);
        this.setCreativeTab(forestry.api.core.Tabs.tabApiculture);
        this.setUnlocalizedName("thaumiumScoop");
        this.setHarvestLevel("scoop", 3);
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        return 1f;
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, Block block, int metadata) {
        return ForgeHooks.isToolEffective(itemStack, block, metadata) ? 4.8f : func_150893_a(itemStack, block);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int j, int k, int l,
            EntityLivingBase entityliving) {
        itemstack.damageItem(1, entityliving);
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    @Optional.Method(modid = ThaumcraftHelper.Name)
    public int getItemEnchantability() {
        return ThaumcraftApi.toolMatThaumium.getEnchantability();
    }

    /**
     * Return the name for this tool's material.
     */
    @Optional.Method(modid = ThaumcraftHelper.Name)
    public String getToolMaterialName() {
        return ThaumcraftApi.toolMatThaumium.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Optional.Method(modid = ThaumcraftHelper.Name)
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return OreDictionary.itemMatches(ThaumcraftApi.toolMatThaumium.getRepairItemStack(), par2ItemStack, true)
                || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":thaumiumScoop");
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}
