package magicbees.item;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.main.CommonProxy;
import magicbees.main.utils.compat.ThaumcraftHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.IRepairableExtended;
import thaumcraft.api.ThaumcraftApi;

@Optional.InterfaceList({
    @Optional.Interface(iface = "thaumcraft.api.IRepairableExtended", modid = ThaumcraftHelper.Name, striprefs = true)
})
public class ItemThaumiumGrafter extends ItemGrafter implements IRepairableExtended {
    public ItemThaumiumGrafter() {
        super();
        this.setMaxDamage(15);
        this.setUnlocalizedName("thaumiumGrafter");
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        return 1f;
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, Block block, int metadata) {
        return ForgeHooks.isToolEffective(itemStack, block, metadata) ? 4.8f : func_150893_a(itemStack, block);
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
    public boolean doRepair(ItemStack stack, EntityPlayer player, int enchantLevel) {
        boolean flag = false;
        if (stack.getItemDamage() > 0) {
            flag = true;
            player.addExhaustion(0.6f * (enchantLevel * enchantLevel));
        }
        return flag;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":thaumiumGrafter");
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}
