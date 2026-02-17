package com.jomi;

import java.nio.file.Path;

import com.jomi.Handlers.Item.ItemLoader;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Util.CurrencyAction;

public class CraftingRun {
    public void run() {
        LoadedItem item = ItemLoader.loadFromJson(Path.of("data/saveState/temp/testItem.json"));

        for (int i = 0 ; i < 6 ; i++) {
            item = CurrencyAction.apply(item, "exalted_orb");
        }

        ItemLoader.saveToJson(item, Path.of("data/saveState/temp/newItem.json"));

        System.out.println("run comlete");
    }
}
