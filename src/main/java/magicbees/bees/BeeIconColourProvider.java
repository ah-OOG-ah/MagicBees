package magicbees.bees;

import java.awt.Color;

import forestry.api.apiculture.IBeeIconColourProvider;

public enum BeeIconColourProvider implements IBeeIconColourProvider {
	RAINBOW {
		@Override
		public int getIconColour(int renderPass) {
			if (renderPass == 0) {
				int hue = (int) (System.currentTimeMillis() >> 2) % 360;
				return Color.getHSBColor(hue / 360f, 0.75f, 0.80f).getRGB();
			} else if (renderPass == 1) {
				int hue = (int) (System.currentTimeMillis() >> 3) % 360;
				hue += 60;
				hue = hue % 360;
				return Color.getHSBColor(hue / 360f, 0.5f, 0.6f).getRGB();
			}
			return 0xffffff;
		}
	}
}
