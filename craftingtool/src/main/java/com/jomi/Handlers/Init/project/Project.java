package com.jomi.Handlers.Init.project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {

    private final String id;
    private String name;

    private List<Node> nodes;
    private List<Connection> connections;

    public Project() {
        this.id = UUID.randomUUID().toString();
        this.name = "Untitled Project";

        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getNodeSize() {
        return nodes.size();
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public List<String> getAllNodeIds() {
        List<String> ids = new ArrayList<>();
        for (Node node : nodes) {
            ids.add(node.getId());
        }
        return ids;
    }

    public int connectionSize() {
        return connections.size();
    }


}
