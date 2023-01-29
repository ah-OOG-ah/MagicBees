package magicbees.block;

import magicbees.main.CommonProxy;
import magicbees.main.utils.TabMagicBees;
import magicbees.tileentity.TileEntityManaAuraProvider;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockManaAuraProvider extends Block implements ITileEntityProvider {

    @SideOnly(Side.CLIENT)
    private IIcon topIcon;

    public BlockManaAuraProvider() {
        super(Material.rock);
        this.setCreativeTab(TabMagicBees.tabMagicBees);
        this.setBlockName("manaAuraProvider");
        this.setHardness(2f);
        this.setResistance(3f);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityManaAuraProvider();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":manaApiaryBooster0");
        this.topIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":manaApiaryBooster1");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? this.topIcon : this.blockIcon;
    }
}
