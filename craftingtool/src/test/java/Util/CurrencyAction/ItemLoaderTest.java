package Util.CurrencyAction;

import java.util.List;

import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Util.ModRoller;

public class ItemLoaderTest {

    public static LoadedItem emptyItem(String rarity) {
        return new LoadedItem(
            80,
            rarity,
            "weapons/warStaff",
            List.of(),
            List.of()
        );
    }

    public static LoadedItem withMods(String rarity, List<ModRoller.RolledMod> prefix, List<ModRoller.RolledMod> suffix) {
        return new LoadedItem(
            80,
            rarity,
            "weapons/warStaff",
            prefix,
            suffix
        );
    }
}

