package com.jomi.Handlers.registry;

import com.jomi.GUI.simlatorParts.Nodes.StartNode;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.GUI.simlatorParts.Nodes.BasicCurrencyNode;
import com.jomi.GUI.simlatorParts.Nodes.EndNode;
import com.jomi.GUI.simlatorParts.Nodes.GenericNode;
import com.jomi.GUI.simlatorParts.Nodes.IteratorNode;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

public class NodeRegister {
    public static NodeView createNodeView(Node node, Project project) {
        return switch (node.getType()) {
            case "start" -> new StartNode(node, project);
            case "basic" -> new BasicCurrencyNode(node, project);
            case "end" -> new EndNode(node, project);
            case "iterator" -> new IteratorNode(node, project);
            default -> new GenericNode(node, project);
        };
    }
}
