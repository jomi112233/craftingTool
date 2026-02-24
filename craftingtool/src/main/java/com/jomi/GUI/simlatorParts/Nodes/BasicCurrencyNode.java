package com.jomi.GUI.simlatorParts.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.jomi.GUI.simlatorParts.baseNode.NodeSection;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.Omenregistry;
import com.jomi.Handlers.registry.OrbRegistry;
import com.jomi.Util.craftingAction.BasicCurrencyAction;

public class BasicCurrencyNode extends NodeView {

    public BasicCurrencyNode(Node node, Project project) {
        super(node, project);
        setTitle("Basic Currency");
        refresh();
    }

    /**
     * NEW: Instead of building JavaFX UI, we return structured data.
     */
    @Override
    protected List<NodeSection> getSections() {

        Node data = getNode();
        List<String> actions = data.getActions();

        List<NodeSection> sections = new ArrayList<>();

        // SECTION 1 — Selected actions
        if (!actions.isEmpty()) {

            List<String> selectedItems = new ArrayList<>();

            // First action = currency
            selectedItems.add("Currency: " + actions.get(0));

            // Remaining actions = omens
            for (int i = 1; i < actions.size(); i++) {
                selectedItems.add("Omen: " + actions.get(i));
            }

            sections.add(new NodeSection("Selected", selectedItems));
        }

        // SECTION 2 — Dropdown options
        if (actions.isEmpty()) {
            // No currency selected → show currency list
            List<String> currencyList = OrbRegistry.getAll().stream()
                    .map(Orb::id)
                    .toList();

            sections.add(new NodeSection("Select Currency", currencyList));

        } else {
            // Currency selected → show omens
            List<String> omenList = Omenregistry.getAll().stream()
                    .map(Omen::id)
                    .toList();

            sections.add(new NodeSection("Add Omen", omenList));
        }

        return sections;
    }

    /**
     * NodeView will render the section items as labels.
     * But we still need to handle clicks on items.
     */
    @Override
    protected void onSectionItemClicked(String sectionTitle, String itemText) {

        Node data = getNode();

        if (sectionTitle.equals("Select Currency")) {
            data.setAction(itemText);
            refresh();
            return;
        }

        if (sectionTitle.equals("Add Omen")) {
            data.setAction(itemText);
            refresh();
        }
    }

    @Override
    public LoadedItem execute(LoadedItem item) {

        List<String> actions = getNode().getActions();

        if (actions.isEmpty()) {
            return item; // nothing to do
        }

        // First action = currency
        String orbId = actions.get(0);
        Orb orb = OrbRegistry.get(orbId);

        // Remaining actions = omens
        List<Omen> omens = new ArrayList<>();
        for (int i = 1; i < actions.size(); i++) {
            Omen o = Omenregistry.get(actions.get(i));
            if (o != null) omens.add(o);
        }

        BasicCurrencyAction.apply(item, orb, omens);

        return item;
    }

    @Override
    protected void edit() {
        System.out.println("BasicCurrencyNode has no editable fields.");
    }
}
