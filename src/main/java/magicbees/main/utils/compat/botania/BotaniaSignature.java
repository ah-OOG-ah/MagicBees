package magicbees.main.utils.compat.botania;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import vazkii.botania.api.subtile.signature.SubTileSignature;

public class BotaniaSignature extends SubTileSignature {

    private String name;
    IIcon icon;

    public BotaniaSignature(String subtileName) {
        this.name = subtileName;
    }

    @Override
    public IIcon getIconForStack(ItemStack stack) {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        icon = register.registerIcon("magicbees:" + name);
    }

    @Override
    public String getUnlocalizedNameForStack(ItemStack stack) {
        return "tile.botania:flower." + name;
    }

    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack stack) {
        return "tile.botania:flower." + name + ".lore";
    }
}
