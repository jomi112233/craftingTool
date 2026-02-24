package com.jomi.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.project.Node;

public class SimulationRunner {

    /**
     * Entry point: returns ALL final items produced by all branches.
     */
    public static List<LoadedItem> run(Project project, Map<String, NodeView> nodeViews) {

        // 1. Find StartNode
        Node start = project.getNodes().stream()
            .filter(n -> "start".equals(n.getType()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No StartNode found"));

        // 2. Begin recursive branching simulation
        return runFromNode(project, nodeViews, start, null);
    }


    /**
     * Recursive branch executor.
     * Each outgoing connection becomes a new branch.
     * No merging — each branch produces its own final LoadedItem.
     */
    private static List<LoadedItem> runFromNode(
            Project project,
            Map<String, NodeView> nodeViews,
            Node node,
            LoadedItem item
    ) {
        NodeView view = nodeViews.get(node.getId());
        if (view == null) {
            throw new RuntimeException("No NodeView found for node " + node.getId());
        }

        // Execute this node
        item = view.execute(item);

        // Find all outgoing connections
        List<Node> nextNodes = getNextNodes(project, node);

        // If no outgoing nodes → this is an EndNode
        if (nextNodes.isEmpty()) {
            return List.of(item);
        }

        // Branch into all outgoing paths
        List<LoadedItem> results = new ArrayList<>();
        for (Node next : nextNodes) {
            // Each branch gets its own copy of the item
            results.addAll(runFromNode(project, nodeViews, next, item.copy()));
        }

        return results;
    }


    /**
     * Returns ALL nodes connected as outputs from the given node.
     */
    private static List<Node> getNextNodes(Project project, Node node) {
        return project.getConnections().stream()
            .filter(c -> c.getFromNodeId().equals(node.getId()))
            .map(c -> project.getNodes().stream()
                .filter(n -> n.getId().equals(c.getToNodeID()))
                .findFirst()
                .orElse(null))
            .filter(n -> n != null)
            .toList();
    }
}
