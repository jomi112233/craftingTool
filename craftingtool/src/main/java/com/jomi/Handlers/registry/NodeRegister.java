package com.jomi.Handlers.registry;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.GUI.simlatorParts.Nodes.StartNode;
import com.jomi.GUI.simlatorParts.Nodes.basicCurrencyNode;
import com.jomi.GUI.simlatorParts.Nodes.endingNode;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

public class NodeRegister {
    public static NodeView createNodeView(Node node, Project project) {
        return switch (node.getType()) {
            case "start" -> new StartNode(node, project);
            case "basic" -> new basicCurrencyNode(node, project);
            case "end" -> new endingNode(node, project);
            default -> new NodeView(node, project);
        };
    }
}
