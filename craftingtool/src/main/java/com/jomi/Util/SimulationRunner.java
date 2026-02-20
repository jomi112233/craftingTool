package com.jomi.Util;

import java.util.List;
import java.util.Map;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.Init.project.Node;

public class SimulationRunner {

    public static LoadedItem run(Project project, Map<String, NodeView> nodeViews) {

        // 1. Find StartNode
        Node start = project.getNodes().stream()
            .filter(n -> "start".equals(n.getType()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No StartNode found"));

        LoadedItem item = null;
        Node current = start;

        // 2. Traverse until EndNode
        while (true) {

            NodeView view = nodeViews.get(current.getId());
            if (view == null) {
                throw new RuntimeException("No NodeView found for node " + current.getId());
            }

            // 3. Execute this node
            item = view.execute(item);

            // 4. Find next node
            Node next = getNextNode(project, current);

            if (next == null) {
                // EndNode or dead end
                return item;
            }

            current = next;
        }
    }

    private static Node getNextNode(Project project, Node node) {
        return project.getConnections().stream()
            .filter(c -> c.getFromNodeId().equals(node.getId()))
            .findFirst()
            .flatMap(c -> project.getNodes().stream()
                .filter(n -> n.getId().equals(c.getToNodeID()))
                .findFirst())
            .orElse(null);
    }
}
