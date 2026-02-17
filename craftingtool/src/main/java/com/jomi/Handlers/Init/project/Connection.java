package com.jomi.Handlers.Init.project;

import java.util.UUID;

public class Connection {
    private String fromNodeId;
    private String toNodeID;
    private String connectionID;

    public Connection(String from, String to) {
        this.connectionID = UUID.randomUUID().toString();
        this.fromNodeId = from;
        this.toNodeID = to;
    }

    public String getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public String getToNodeID() {
        return toNodeID;
    }

    public void setToNodeID(String toNodeID) {
        this.toNodeID = toNodeID;
    }

    public String getConnectionID() {
        return connectionID;
    }

}
