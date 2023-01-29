package magicbees.client.gui;

import java.util.ArrayList;
import java.util.List;

import magicbees.main.Config;
import magicbees.main.utils.VersionInfo;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class ModGuiConfig extends GuiConfig {

    public ModGuiConfig(GuiScreen guiScreen) {
        super(
                guiScreen,
                configElements(),
                VersionInfo.ModName,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(Config.configuration.toString()));
    }

    private static List<IConfigElement> configElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.add(categoryElement(Config.CATEGORY_GENERAL, "General", "magicbees.config.category.general"));
        list.add(categoryElement(Config.CATEGORY_DEBUG, "Debug", "magicbees.config.category.debug"));
        list.add(categoryElement(Config.CATEGORY_MODULES, "Modules", "magicbees.config.category.modules"));
        list.add(categoryElement(Config.CATEGORY_BOTANIA, "Botania Plugin", "magicbees.config.category.botania"));

        return list;
    }

    private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
        return new DummyConfigElement.DummyCategoryElement(
                name,
                tooltip_key,
                new ConfigElement(Config.configuration.getCategory(category)).getChildElements());
    }
}
