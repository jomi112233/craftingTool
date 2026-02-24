package com.jomi.GUI.simlatorParts.Nodes;

import java.util.List;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.baseNode.NodeSection;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller.RolledMod;
import com.jomi.Util.NodeShit.NodeEditorWindow;
import com.jomi.Util.NodeShit.searchAction;

public class StartNode extends NodeView {

    private final LoadedItem item;

    public StartNode(Node node, Project project) {
        super(node, project);

        this.item = project.getBaseItem();

        setTitle(extractClassName(item.getLoadedItemClass()));
        setInfo(String.valueOf(item.getItemLevel()));
        applyRarityColor(item.getItemRarity());
    }

    @Override
    protected List<NodeSection> getSections() {

        List<String> prefixItems = item.getPrefix().stream()
                .map(this::formatMod)
                .toList();

        List<String> suffixItems = item.getSuffix().stream()
                .map(this::formatMod)
                .toList();

        return List.of(
                new NodeSection("Prefix", prefixItems),
                new NodeSection("Suffix", suffixItems),

                // Inline search fields
                new NodeSection("Modifiers", List.of(
                        "search:prefix"
                ))
        );
    }

    /**
     * Called when the user selects a result from an inline search field.
     */
   @Override
    protected void onSearchResultSelected(String section, String key, String result) {

        // 1. Find the selected mod
        Mod mod = ModRegistry.getAll().stream()
                .filter(m -> m.getName().equals(result))
                .findFirst()
                .orElse(null);

        if (mod == null) return;

        // 3. Save the base item
        getProject().saveBaseItem();

        // 4. Refresh the node UI
        edit();
    }


    /**
     * Provides the list of searchable mods for inline search.
     */
    @Override
    protected List<? extends searchAction> getSearchSource(String section, String key) {
        return ModRegistry.getAll().stream().toList();
    }

    /**
     * Format a rolled mod into a readable string.
     */
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
