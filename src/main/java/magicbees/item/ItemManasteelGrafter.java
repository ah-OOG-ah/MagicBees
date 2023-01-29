package magicbees.item;

import magicbees.main.CommonProxy;
import magicbees.main.utils.compat.BotaniaHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.InterfaceList({ @Optional.Interface(
        iface = "vazkii.botania.api.mana.IManaUsingItem",
        modid = BotaniaHelper.Name,
        striprefs = true) })
public class ItemManasteelGrafter extends ItemGrafter implements IManaUsingItem {

    public static final int MANA_PER_DAMAGE = 90;

    public ItemManasteelGrafter() {
        super();
        this.setMaxDamage(15);
        this.setUnlocalizedName("manasteelGrafter");
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
        this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":manasteelGrafter");
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

    @Override
    public boolean usesMana(ItemStack arg0) {
        return true;
    }
}
