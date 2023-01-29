package magicbees.item;

import magicbees.main.CommonProxy;
import magicbees.main.utils.compat.BotaniaHelper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.IToolScoop;

@Optional.InterfaceList({ @Optional.Interface(
        iface = "vazkii.botania.api.mana.IManaUsingItem",
        modid = BotaniaHelper.Name,
        striprefs = true) })
public class ItemManasteelScoop extends Item implements IManaUsingItem, IToolScoop {

    public static final int MANA_PER_DAMAGE = 30;

    public ItemManasteelScoop() {
        super();
        this.maxStackSize = 1;
        this.setMaxDamage(20);
        this.setCreativeTab(forestry.api.core.Tabs.tabApiculture);
        this.setUnlocalizedName("manasteelScoop");
        this.setHarvestLevel("scoop", 3);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
            EntityLivingBase player) {
        stack.damageItem(1, player);
        return true;
    }

    @Override
    public boolean usesMana(ItemStack stack) {
        return true;
    }

    @Override
    @Optional.Method(modid = BotaniaHelper.Name)
    public boolean getIsRepairable(ItemStack toolStack, ItemStack material) {
        return (material.getItem() == BotaniaHelper.itemManaResource
                && material.getItemDamage() == BotaniaHelper.ManaResource.MANASTEEL.ordinal())
                || super.getIsRepairable(toolStack, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":manasteelScoop");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
        // Unsure if the server-side exclusion is necessary, but boy it looks disconcerting on the client if it's in.
        if (
        /* !world.isRemote && */ stack.getItemDamage() > 0 && player instanceof EntityPlayer) {
            if (BotaniaHelper.requestMana(stack, (EntityPlayer) player, MANA_PER_DAMAGE, 1)) {
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
        }
    }
}
