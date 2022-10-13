package magicbees.main.utils.compat.botania;

import magicbees.main.utils.LocalizationManager;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.IAddonEntry;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BotaniaLexiconEntry extends LexiconEntry implements IAddonEntry {

    public BotaniaLexiconEntry(String unlocalizedName, LexiconCategory category) {
        super(unlocalizedName, category);
        BotaniaAPI.addEntry(this, category);
    }

    @Override
    public String getSubtitle() {
        return LocalizationManager.getLocalizedString("magicbees.botania.lexicon.subtitle");
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}
