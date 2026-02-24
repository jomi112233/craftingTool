package com.jomi.GUI.simlatorParts.Nodes;

import java.util.List;

import com.jomi.GUI.simlatorParts.baseNode.NodeSection;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;

public class GenericNode extends NodeView {

    public GenericNode(Node node, Project project) {
        super(node, project);
        setTitle("Generic Node");
    }

    @Override
    protected List<NodeSection> getSections() {
        return List.of(
            new NodeSection("Info", List.of(
                "This is a generic node.",
                "It doesn't do anything.",
                "But it looks cool."
            ))
        );
    }

    @Override
    protected void onSectionItemClicked(String section, String item) {
        // Generic node has no actions
    }

    @Override
    public LoadedItem execute(LoadedItem item) {
        return item; // does nothing
    }

    @Override
    protected void edit() {
        // No editable fields
    }
}
