package magicbees.item;

import magicbees.main.Config;
import magicbees.main.utils.compat.ThaumcraftHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import forestry.api.arboriculture.IToolGrafter;

public class ItemGrafter extends Item implements IToolGrafter {

    public ItemGrafter() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(forestry.api.core.Tabs.tabArboriculture);
        this.setHarvestLevel("grafter", 3);
    }

    @Override
    public float getSaplingModifier(ItemStack stack, World world, EntityPlayer player, int x, int y, int z) {
        return 100f;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z,
            EntityLivingBase entityLiving) {
        int damage = 1;
        if (ThaumcraftHelper.isActive()) {
            if (block == ThaumcraftHelper.leaf) {
                int meta = world.getBlockMetadata(x, y, z) & 1;
                if (meta == 0 || meta == 1) {
                    double chance = Math.random();
                    if (chance <= Config.thaumcraftSaplingDroprate) {
                        this.dropItem(world, x, y, z, new ItemStack(ThaumcraftHelper.plant, 1, meta));
                    }
                }
            }
        }
        itemstack.damageItem(damage, entityLiving);
        return true;
    }

    protected void dropItem(World world, int x, int y, int z, ItemStack item) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            float f = 0.7F;
            double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double) x + d0, (double) y + d1, (double) z + d2, item);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
}
