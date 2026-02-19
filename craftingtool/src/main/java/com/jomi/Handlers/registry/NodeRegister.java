package com.jomi.Handlers.registry;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.GUI.simlatorParts.Nodes.StartNode;
import com.jomi.GUI.simlatorParts.Nodes.GenericNode;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

public class NodeRegister {
    public static NodeView createNodeView(Node node, Project project) {
        return switch (node.getType()) {
            case "start" -> new StartNode(node, project);
            
            default -> new GenericNode(node, project);
        };
    }
}
