package magicbees.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.block.types.HiveType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Allen on 7/29/2014.
 */
public class ItemMagicHive extends ItemBlock {
    public ItemMagicHive(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    protected Block getBlock() {
        return field_150939_a;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.getBlock().getIcon(1, meta);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return getBlock().getUnlocalizedName() + "."
                + HiveType.getHiveFromMeta(itemstack.getItemDamage()).name().toLowerCase();
    }
}
