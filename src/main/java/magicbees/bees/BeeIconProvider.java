package magicbees.bees;

import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBeeIconProvider;

import magicbees.main.CommonProxy;

public enum BeeIconProvider implements IBeeIconProvider {
	SKULKING(CommonProxy.DOMAIN + ":bees/skulking/"),
	DOCTORAL(CommonProxy.DOMAIN + ":bees/doctoral/");

	@SideOnly(Side.CLIENT)
	private IIcon[][] icons;
	private final String iconPath;

	BeeIconProvider(String iconPath) {
		this.iconPath = iconPath;
	}

	@Override
	public void registerIcons(IIconRegister itemMap) {
		this.icons = new IIcon[EnumBeeType.VALUES.length][3];

		IIcon body1 = itemMap.registerIcon(iconPath + "body1");

		for (int i = 0; i < EnumBeeType.VALUES.length; i++) {
			EnumBeeType beeType = EnumBeeType.VALUES[i];
			if (beeType == EnumBeeType.NONE)
				continue;

			String beeTypeIconPath = iconPath + beeType.toString().toLowerCase(Locale.ENGLISH);

			icons[i][0] = itemMap.registerIcon(beeTypeIconPath + ".outline");
			icons[i][1] = (beeType != EnumBeeType.LARVAE) ? body1 : itemMap.registerIcon(beeTypeIconPath + ".body");
			icons[i][2] = itemMap.registerIcon(beeTypeIconPath + ".body2");
		}
	}

	@Override
	public IIcon getIcon(EnumBeeType type, int renderPass) {
		return icons[type.ordinal()][Math.min(renderPass, 2)];
	}
}
