package com.jomi.GUI.simlatorParts.Nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jomi.App;
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
import com.jomi.Util.NodeShit.searchAction;

public class BasicCurrencyNode extends NodeView {

    private List<NodeSection> sections = new ArrayList<>();

    public BasicCurrencyNode(Node node, Project project) {
        super(node, project);
        setTitle("Basic Currency");
        buildSections();
        refresh();
    }

    // ---------------------------------------------------------
    // BUILD SECTIONS (StartNode-style)
    // ---------------------------------------------------------
    private void buildSections() {

        Node data = getNode();
        List<String> actions = data.getActions();

        List<String> currencyItems = new ArrayList<>();
        if (!actions.isEmpty()) {
            currencyItems.add("• " + actions.get(0));
        }
        currencyItems.add("add");

        List<String> omenItems = new ArrayList<>();
        for (int i = 1; i < actions.size(); i++) {
            omenItems.add("• " + actions.get(i));
        }
        omenItems.add("add");

        sections = new ArrayList<>();
        sections.add(new NodeSection("Currency", currencyItems));
        sections.add(new NodeSection("Omen", omenItems));
    }

    @Override
    protected List<NodeSection> getSections() {
        return sections;
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
            removeItem(sectionTitle, itemText);
        }

        refresh();
    }

    private void removeItem(String section, String itemText) {

        Node data = getNode();
        List<String> actions = new ArrayList<>(data.getActions());
        String cleaned = itemText.replace("•", "").trim();

        if (section.equals("Currency")) {
            actions.clear();

        }

        if (section.equals("Omen")) {
            actions.removeIf(a -> a.equalsIgnoreCase(cleaned));
        }

        data.setActions(actions);
        buildSections();
        App.getInstance().getProjectManager().saveProject(getProject());
    }

    // ---------------------------------------------------------
    // SEARCH RESULT SELECTED
    // ---------------------------------------------------------
    @Override
    protected void onSearchResultSelected(String section, String key, String result) {

        Node data = getNode();
        List<String> actions = new ArrayList<>(data.getActions());

        if (section.equals("Currency")) {

            Orb orb = OrbRegistry.getAll().stream()
                .filter(o -> o.getName().equalsIgnoreCase(result))
                .findFirst()
                .orElse(null);

            actions.clear();
            actions.add(orb.getid());
        }

        if (section.equals("Omen")) {
            Omen omen = Omenregistry.getAll().stream()
                .filter(o -> o.getName().equalsIgnoreCase(result))
                .findFirst()
                .orElse(null);
            actions.add(omen.id());
        }

        data.setActions(actions);
        
        buildSections();
        refresh();
        App.getInstance().getProjectManager().saveProject(getProject());
    }

    // ---------------------------------------------------------
    // SEARCH SOURCE
    // ---------------------------------------------------------
    @Override
    protected List<? extends searchAction> getSearchSource(String section, String key) {

        if (section.equalsIgnoreCase("Currency")) {
            return OrbRegistry.getAll().stream().toList();
        }

        if (section.equalsIgnoreCase("Omen")) {

            List<String> actions = getNode().getActions();

            if (actions.isEmpty()) {
                return List.of();
            }

            // The first action is the orb ID
            String orbId = actions.get(0);

            // Look up the orb
            Orb orb = OrbRegistry.get(orbId);
            if (orb == null) {
                return List.of();
            }

            String omenTarget = orb.omenTarget();

            return Omenregistry.getAll().stream()
                    .filter(o -> o.usedWith().equalsIgnoreCase(omenTarget))
                    .filter(o -> !actions.contains(o.id())) // prevent duplicates
                    .collect(Collectors.toMap(
                            Omen::id,
                            o -> o,
                            (a, b) -> a
                    ))
                    .values()
                    .stream()
                    .toList();
        }

        return List.of();
    }



    // ---------------------------------------------------------
    // REPLACE SECTION ITEM (StartNode-style)
    // ---------------------------------------------------------
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
    // EXECUTION
    // ---------------------------------------------------------
    @Override
    public LoadedItem execute(LoadedItem item) {

        List<String> actions = getNode().getActions();

        if (actions.isEmpty()) {
            return item;
        }

        Orb orb = OrbRegistry.get(actions.get(0));

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
