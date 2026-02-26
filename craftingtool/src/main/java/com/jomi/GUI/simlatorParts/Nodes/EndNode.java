package com.jomi.GUI.simlatorParts.Nodes;

import java.util.List;

import com.jomi.GUI.simlatorParts.baseNode.NodeSection;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Util.ModRoller.RolledMod;

public class EndNode extends NodeView {

    private LoadedItem finalItem;

    public EndNode(Node node, Project project) {
        super(node, project);
        setTitle("End Node");
    }

    @Override
    protected List<NodeSection> getSections() {

        if (finalItem == null) {
            return List.of(
                new NodeSection("Waiting", List.of("Run simulation to see final item."))
            );
        }

        List<String> prefixItems = finalItem.getPrefix().stream()
                .map(this::formatMod)
                .toList();

        List<String> suffixItems = finalItem.getSuffix().stream()
                .map(this::formatMod)
                .toList();

        return List.of(
                new NodeSection("Prefix", prefixItems),
                new NodeSection("Suffix", suffixItems)
        );
    }

    private String formatMod(RolledMod mod) {
        String[] parts = mod.name().split(",", 2);

        if (parts.length == 2) {
            return "• (T" + mod.tier().tier() + ") "
                    + parts[0].trim() + ",\n    "
                    + parts[1].trim();
        }
        return "• (T" + mod.tier().tier() + ") " + mod.name();
    }

    @Override
    public LoadedItem execute(LoadedItem item) {
        this.finalItem = item;
        refresh();
        applyRarityColor(finalItem.getItemRarity());
        setInfo(String.valueOf(finalItem.getItemLevel()));
        return item;      
    }

    @Override
    protected void edit() {
        System.out.println("EndNode has no editable fields.");
    }
}
