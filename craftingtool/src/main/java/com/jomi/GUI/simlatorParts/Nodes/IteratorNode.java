package com.jomi.GUI.simlatorParts.Nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.Nodes.StartNode.TierSearchEntry;
import com.jomi.GUI.simlatorParts.baseNode.NodeSection;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Handlers.registry.Omenregistry;
import com.jomi.Handlers.registry.OrbRegistry;
import com.jomi.Util.ModRoller;
import com.jomi.Util.ModRoller.RolledMod;
import com.jomi.Util.NodeShit.searchAction;
import com.jomi.Util.craftingAction.iterationAction;

public class IteratorNode extends NodeView{
    
    private List<NodeSection> sections;
    private final LoadedItem item;
    private iterationAction.BinomialResult lastResult;


    public IteratorNode(Node node, Project project) {
        super(node, project);
        
        node.setIterator(true);
        this.item = project.getBaseItem();
        buildSections();

        setTitle("Iterator");
    }



    private void buildSections() {
        Node data = getNode();
        List<String> actions =  data.getActions();
        List<ModRoller.RolledMod> targets = data.getForcedTarget();

        sections = new ArrayList<>();

        List<String> currencyItems = new ArrayList<>();
        if (!actions.isEmpty()) {
            currencyItems.add("• " + actions.get(0));
        }
        currencyItems.add("add");
        
        List<String> omenItems = new ArrayList<>();
        for (int i = 1 ; i < actions.size() ; i++) {
            omenItems.add("• " + actions.get(i));
        }
        omenItems.add("add");

        List<String> targetItems = new ArrayList<>(
            targets.stream()
                .map(this::formatMod)
                .toList()
        );
        targetItems.add("add");

        List<String> probItems = new ArrayList<>();
        if (lastResult != null) {
            probItems.add("Per-roll: " + String.format("%.4f%%", lastResult.perRollChance() * 100));
            probItems.add("25% at: " + lastResult.rolls25());
            probItems.add("50% at: " + lastResult.rolls50());
            probItems.add("75% at: " + lastResult.rolls75());
            
        }


        sections.add(new NodeSection("Currency", currencyItems));
        sections.add(new NodeSection("Omen", omenItems));
        sections.add(new NodeSection("Target", targetItems));
        sections.add(new NodeSection("Probability", probItems));

    }

    @Override
    protected List<NodeSection> getSections() {
        return sections;
    }

    @Override
    protected void onSectionItemClicked(String sectionTitle, String itemtext) {

        String cleaned = itemtext.trim().toLowerCase();

        if (cleaned.equals("add")) {
            replaceSectionItem(sectionTitle, itemtext, "search:" + sectionTitle.toLowerCase());
        } else {
            removeItem(sectionTitle, itemtext);
        }

        refresh();
    }

    private void removeItem(String section, String itemText) {

        Node data = getNode();
        List<String> actions = new ArrayList<>(data.getActions());
        List<ModRoller.RolledMod> target = new ArrayList<>(data.getForcedTarget());
        String cleaned = itemText.replace("•", "").trim();

        if (section.equals("Currency")) {
            actions.clear();
        }

        if (section.equals("Omen")) {
            actions.removeIf(a -> a.equalsIgnoreCase(cleaned));
        }

        if (section.equals("Target")) {
            target.clear();
        }

        data.setActions(actions);
        data.setForcedTarget(target);
        buildSections();
        App.getInstance().getProjectManager().saveProject(getProject());
    }

    @Override
    protected void onSearchResultSelected(String section, String key, String result) {

        Node data = getNode();
        List<String> actions = new ArrayList<>(data.getActions());
        List<ModRoller.RolledMod> target = new ArrayList(data.getForcedTarget());

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

        if (section.equals("Target")) {
            String name = result.substring(0, result.lastIndexOf("(T")).trim();
            int tierNum = Integer.parseInt(result.substring(result.lastIndexOf("(T") + 2, result.lastIndexOf(")")));


            Mod mod = ModRegistry.searchByName(name).stream()
                .findFirst()
                .orElse(null);

            if (mod != null) {

                ModTier tier = mod.tiers().stream()
                    .filter(t -> t.tier() == tierNum)
                    .findFirst()
                    .orElse(null);

                if (tier != null) {
                    ModRoller.RolledMod rolled = new ModRoller.RolledMod(
                        mod.id(),
                        mod.name(),
                        mod.tags(),
                        tier
                    );
                    target.clear();
                    target.add(rolled);
                }
            }
        }

        data.setActions(actions);
        data.setForcedTarget(target);

        buildSections();
        refresh();
        App.getInstance().getProjectManager().saveProject(getProject());
    }

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

        if (section.equalsIgnoreCase("Target")) {
            var usedMods = new ArrayList<String>();
            item.getPrefix().forEach(m -> usedMods.add(m.modId()));
            item.getSuffix().forEach(m -> usedMods.add(m.modId()));

            return ModRegistry.getAll().stream()
                .filter(m -> m.prefix() || m.suffix()) 
                .filter(m -> !usedMods.contains(m.id()))
                .flatMap(mod -> mod.tiers().stream()
                    .map(tier -> new TierSearchEntry(mod, tier))
                )
                .toList();
        }

        return List.of();
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


        List<String> actions = getNode().getActions();
        List<ModRoller.RolledMod> forcedTarget = getNode().getForcedTarget();

        // No currency selected → nothing to do
        if (actions.isEmpty()) {
            System.out.println("IteratorNode: No currency selected.");
            return item;
        }

        // Must have exactly one forced target
        if (forcedTarget.isEmpty()) {
            System.out.println("IteratorNode: No forced target selected.");
            return item;
        }

        // Extract orb
        String orbId = actions.get(0);
        Orb orb = OrbRegistry.get(orbId);
        if (orb == null) {
            System.out.println("IteratorNode: Invalid orb ID: " + orbId);
            return item;
        }

        // Extract omen (optional)
        Omen omen = null;
        if (actions.size() > 1) {
            String omenId = actions.get(1);
            omen = Omenregistry.get(omenId);
        }

        // If no omen, create a dummy one with BOTH
        if (omen == null) {
            omen = new Omen(
                "none",
                "No Omen",
                orb.id(),
                orb.currencyType(),
                "BOTH",
                false,
                0
            );
        }

        // Forced target (only one allowed)
        RolledMod target = forcedTarget.get(0);

        // Number of attempts (for now hardcode)

        // Run probability calculation
        iterationAction.calculateBinomial(
            item,
            orb,
            omen,
            target
        );

        lastResult = iterationAction.calculateBinomial(item, orb, omen, target);
        buildSections();
        refresh();


        return item;
    }


    @Override
    protected void edit() {
        System.out.println("notihng to edit here");
    }
}
