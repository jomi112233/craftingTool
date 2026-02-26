package com.jomi.GUI.simlatorParts.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.baseNode.NodeSection;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller;
import com.jomi.Util.ModRoller.RolledMod;
import com.jomi.Util.NodeShit.searchAction;
import com.jomi.Util.NodeShit.Modifier.NodeEditorWindow;

public class StartNode extends NodeView {

    private final LoadedItem item;
    private List<NodeSection> sections;

    public StartNode(Node node, Project project) {
        super(node, project);

        this.item = project.getBaseItem();
        buildSections();

        setTitle(extractClassName(item.getLoadedItemClass()));
        setInfo(String.valueOf(item.getItemLevel()));
        applyRarityColor(item.getItemRarity());
    }

    // ---------------------------------------------------------
    // BUILD SECTIONS
    // ---------------------------------------------------------
    private void buildSections() {

        List<String> prefixItems = new ArrayList<>(
                item.getPrefix().stream()
                        .map(this::formatMod)
                        .toList()
        );
        prefixItems.add("add");

        List<String> suffixItems = new ArrayList<>(
                item.getSuffix().stream()
                        .map(this::formatMod)
                        .toList()
        );
        suffixItems.add("add");

        sections = new ArrayList<>();
        sections.add(new NodeSection("Prefix", prefixItems));
        sections.add(new NodeSection("Suffix", suffixItems));
    }

    @Override
    protected List<NodeSection> getSections() {
        return sections;
    }

    // ---------------------------------------------------------
    // SEARCH RESULT SELECTED
    // ---------------------------------------------------------
    @Override
    protected void onSearchResultSelected(String section, String key, String result) {

        // Update UI
        replaceSectionItem(section, "search:" + key, "• " + result);

        // Extract name and tier
        String name = result.substring(0, result.lastIndexOf("(T")).trim();
        int tierNum = Integer.parseInt(
                result.substring(result.lastIndexOf("(T") + 2, result.lastIndexOf(")"))
        );

        // Find the mod
        Mod mod = ModRegistry.searchByName(name).stream()
                .findFirst()
                .orElse(null);

        if (mod != null) {

            // Find the specific tier
            ModTier tier = mod.tiers().stream()
                    .filter(t -> t.tier() == tierNum)
                    .findFirst()
                    .orElse(null);

            if (tier != null) {

                // Roll EXACT tier
                RolledMod rolled = new RolledMod(mod.id(), mod.name(), mod.tags(), tier);

                if (section.equals("Prefix")) {
                    item.getPrefix().add(rolled);
                } else {
                    item.getSuffix().add(rolled);
                }

                getProject().setBaseItem(item);
                getProject().saveBaseItem();
            }
        }

        ensureAddButton(section);
        refresh();
    }

    // ---------------------------------------------------------
    // CLICK HANDLING
    // ---------------------------------------------------------
    @Override
    protected void onSectionItemClicked(String sectionTitle, String itemText) {

        String cleaned = itemText.trim().toLowerCase();

        if (cleaned.equals("add")) {
            replaceSectionItem(sectionTitle, itemText, "search:" + sectionTitle.toLowerCase());
        } else {
            removeMod(sectionTitle, itemText);
        }

        refresh();
    }

    private void removeMod(String section, String itemText) {

        List<NodeSection> updated = new ArrayList<>();

        for (NodeSection sec : sections) {
            if (sec.title().equals(section)) {

                List<String> items = new ArrayList<>(sec.items());

                items.removeIf(i -> i.trim().equalsIgnoreCase(itemText.trim()));

                if (!items.contains("add")) {
                    items.add("add");
                }

                updated.add(new NodeSection(section, items));
            } else {
                updated.add(sec);
            }
        }

        sections = updated;

        removeFromLoadedItem(section, itemText);
    }

    private void removeFromLoadedItem(String section, String itemText) {
        String cleanedName =
                itemText.replace("•", "")
                        .replaceAll("\\(T\\d+\\)", "")
                        .trim();

        if (section.equals("Prefix")) {
            item.getPrefix().removeIf(m -> m.name().equalsIgnoreCase(cleanedName));
        } else {
            item.getSuffix().removeIf(m -> m.name().equalsIgnoreCase(cleanedName));
        }

        getProject().setBaseItem(item);
        getProject().saveBaseItem();
    }

    private void ensureAddButton(String section) {
        for (NodeSection sec : sections) {
            if (sec.title().equals(section)) {
                List<String> items = new ArrayList<>(sec.items());

                if (!items.contains("add")) {
                    items.add("add");
                }

                replaceSection(section, items);
                return;
            }
        }
    }

    private void replaceSection(String section, List<String> newItems) {
        List<NodeSection> updated = new ArrayList<>();

        for (NodeSection sec : sections) {
            if (sec.title().equals(section)) {
                updated.add(new NodeSection(section, newItems));
            } else {
                updated.add(sec);
            }
        }

        sections = updated;
    }

    @Override
    public void replaceSectionItem(String section, String oldItem, String newItem) {
        List<NodeSection> updated = new ArrayList<>();

        for (NodeSection sec : sections) {
            if (sec.title().equals(section)) {
                List<String> items = new ArrayList<>(sec.items());
                int index = items.indexOf(oldItem);
                if (index >= 0) {
                    items.set(index, newItem);
                }
                updated.add(new NodeSection(section, items));
            } else {
                updated.add(sec);
            }
        }

        sections = updated;
    }

    // ---------------------------------------------------------
    // SEARCH SOURCE — EXPAND TIERS
    // ---------------------------------------------------------
    @Override
    protected List<? extends searchAction> getSearchSource(String section, String key) {

        boolean isPrefix = section.equalsIgnoreCase("Prefix");
        boolean isSuffix = section.equalsIgnoreCase("Suffix");

        // Collect used mod IDs
        var usedMods = new ArrayList<String>();
        item.getPrefix().forEach(m -> usedMods.add(m.modId()));
        item.getSuffix().forEach(m -> usedMods.add(m.modId()));

        return ModRegistry.getAll().stream()
                .filter(m -> isPrefix ? m.prefix() : m.suffix())
                .filter(m -> !usedMods.contains(m.id())) // <-- HIDE ENTIRE MOD
                .flatMap(mod -> mod.tiers().stream()
                        .map(tier -> new TierSearchEntry(mod, tier))
                )
                .toList();
    }


    // ---------------------------------------------------------
    // TIER SEARCH ENTRY
    // ---------------------------------------------------------
    public record TierSearchEntry(Mod mod, ModTier tier) implements searchAction {

        @Override
        public String getName() {
            return mod.getName() + " (T" + tier.tier() + ")";
        }

        @Override
        public String getid() {
            // unique ID per tier
            return mod.id() + "_T" + tier.tier();
        }
    }


    // ---------------------------------------------------------
    // FORMAT MOD
    // ---------------------------------------------------------
    private String formatMod(RolledMod mod) {
        String[] parts = mod.name().split(",", 2);

        if (parts.length == 2) {
            return "• (T" + mod.tier().tier() + ") "
                    + parts[0].trim() + ",\n    "
                    + parts[1].trim();
        }
        return "• (T" + mod.tier().tier() + ") " + mod.name();
    }

    private String extractClassName(String full) {
        if (full == null) return "";
        int index = full.lastIndexOf('/');
        return index >= 0 ? full.substring(index + 1) : full;
    }

    @Override
    public LoadedItem execute(LoadedItem item) {
        return getProject().getBaseItem().copy();
    }

    @Override
    protected void edit() {
        NodeEditorWindow.open(item, () -> {
            getProject().saveBaseItem();
            refresh();
            App.getInstance().getGui().showSimulator();
        }, null);
    }
}
