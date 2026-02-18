package com.jomi.Handlers.Item;

import java.util.ArrayList;
import java.util.List;

import com.jomi.Util.ModRoller;

public class LoadedItem {
    private int itemLevel;
    private String itemRarity;
    private String loadedItemClass;

    private List<ModRoller.RolledMod> prefix = new ArrayList<>();
    private List<ModRoller.RolledMod> suffix = new ArrayList<>();

    public int getItemLevel() {
        return itemLevel;
    }
    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }
    public String getItemRarity() {
        return itemRarity;
    }
    public void setItemRarity(String itemRarity) {
        this.itemRarity = itemRarity;
    }
    public String getLoadedItemClass() {
        return loadedItemClass;
    }
    public void setLoadedItemClass(String loadedItemClass) {
        this.loadedItemClass = loadedItemClass;
    }
    public List<ModRoller.RolledMod> getPrefix() {
        return prefix;
    }
    public void setPrefix(List<ModRoller.RolledMod> prefix) {
        this.prefix = prefix;
    }
    public List<ModRoller.RolledMod> getSuffix() {
        return suffix;
    }
    public void setSuffix(List<ModRoller.RolledMod> suffix) {
        this.suffix = suffix;
    }


}
