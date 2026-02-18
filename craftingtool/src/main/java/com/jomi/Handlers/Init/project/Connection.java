package com.jomi.Handlers.Init.project;

import java.util.UUID;

public class Connection {
    private String fromNodeId;
    private String toNodeId;
    
    private boolean fromIsOutput;
    private boolean toIsInput;   

    private String connectionID;

    public Connection() {
        
    }

    public Connection(String from, boolean fromIsOutput, String to, boolean toIsInput ) {
        this.connectionID = UUID.randomUUID().toString();
        this.fromNodeId = from;
        this.toNodeId = to;
        this.fromIsOutput = fromIsOutput;
        this.toIsInput = toIsInput;

    }

    public String getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public String getToNodeID() {
        return toNodeId;
    }

    public void setToNodeID(String toNodeID) {
        this.toNodeId = toNodeID;
    }

    public String getConnectionID() {
        return connectionID;
    }

    public boolean isFromIsOutput() {
        return fromIsOutput;
    }

    public void setFromIsOutput(boolean fromIsOutput) {
        this.fromIsOutput = fromIsOutput;
    }

    public boolean isToIsInput() {
        return toIsInput;
    }

    public void setToIsInput(boolean toIsInput) {
        this.toIsInput = toIsInput;
    }

    

}
