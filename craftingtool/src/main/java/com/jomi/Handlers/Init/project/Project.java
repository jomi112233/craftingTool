package com.jomi.Handlers.Init.project;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jomi.Handlers.ConfigLoader;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ItemRegistry;

public class Project {

    private final String id;
    private String name;
    private String itemType;
    @JsonIgnore
    private LoadedItem baseItem;
    private String projectFolderString;

    private LoadedItem finalSimulatedItem;

    private final LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private List<Node> nodes;
    private List<Connection> connections;





    public LoadedItem getFinalSimulatedItem() {
        return finalSimulatedItem;
    }

    public void setFinalSimulatedItem(LoadedItem item) {
        this.finalSimulatedItem = item;
    }



    @JsonProperty("projectFolder")
    public void setProjectFolderString(String folder) {
        this.projectFolderString = folder;
    }


    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }



    public Project() {
        this.id = UUID.randomUUID().toString();
        this.name = "Untitled Project";
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    

    public Project(String name) {
        this();
        this.name = name;
    }

    @JsonProperty("projectFolder")
    public String getProjectFolderString() {
        return getProjectFolder().toUri().toString();
    }

    @JsonIgnore
    public Path getProjectFolder() {
        if (projectFolderString != null) {
            return Path.of(URI.create(projectFolderString));
        }

        // fallback for new projects
        return ConfigLoader.getDataFolder()
                .resolve("saveState")
                .resolve("itemData")
                .resolve(id);
    }


    @JsonIgnore
    public LoadedItem getBaseItem() { 
        return baseItem; 
    }

    public void setBaseItem(LoadedItem baseItem) {
        this.baseItem = baseItem; 
    }

    @JsonIgnore
    public void saveBaseItem() {
        if (baseItem == null) {
            return;
        }
        
        Path folder = getProjectFolder();

        try {
            ItemRegistry.saveToJson(baseItem, folder.resolve("baseitem.json"));
            System.out.println("Base item saved.");
        } catch (Exception e) {
            System.err.println("Failed to save base item: " + e.getMessage());
        }
    }

    @JsonIgnore
    public void saveCompletedBaseItem() {
        if (finalSimulatedItem == null) {
            System.err.println("No final simulated item to save.");
            return;
        }

        Path folder = getProjectFolder();

        try {
            ItemRegistry.saveToJson(finalSimulatedItem, folder.resolve("baseItemCompleted.json"));
            System.out.println("Final item saved.");
        } catch (Exception e) {
            System.err.println("Failed to save final item: " + e.getMessage());
        }
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
        return List.copyOf(nodes);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    @JsonIgnore
    public int getNodeSize() {
        return nodes.size();
    }

    public List<Connection> getConnections() {
        return List.copyOf(connections);
    }

    public void addNode(Node node) {
        nodes.add(node);
    }


    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void removeConnection(Connection connection) {
        if (connection == null) {
            System.out.println("Connection Deletion failed: connection is NULL");
            return;
        }

        System.out.println("Deleting connection with object: " + connection);

        try {
            System.out.println("Connection ID = " + connection.getConnectionID());
        } catch (Exception ex) {
            System.out.println("ERROR: getConnectionID() does not exist or returns null");
            ex.printStackTrace();
            return;
        }

        String id = connection.getConnectionID();

        connections.removeIf(c -> {
            System.out.println("Comparing with: " + c.getConnectionID());
            return c.getConnectionID().equals(id);
        });
    }



    public void removeConnectionsOf(Node node) {
        connections.removeIf(conn -> 
                conn.getToNodeID().equals(node.getId()) ||
                conn.getFromNodeId().equals(node.getId())
        );
    }


    public void removeNodeAndConnection(Node node) {
        nodes.remove(node);

        connections.removeIf(conn -> 
            conn.getToNodeID().equals(node.getId()) ||
            conn.getFromNodeId().equals(node.getId())
        );
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


    @JsonIgnore
    public void clearItemRuns() {
        Path runsFolder = getProjectFolder().resolve("itemruns");

        try {
            if (Files.exists(runsFolder)) {
                Files.walk(runsFolder)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try { Files.delete(path); } catch (Exception ignored) {}
                    });
            }
        } catch (Exception e) {
            System.err.println("Failed to clear itemruns: " + e.getMessage());
        }
    }



}
