package com.jomi.Handlers.Init.project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Node {
    private String id;
    private String type;
    private double xPos, yPos;
    private boolean inputPort;
    private boolean outputPort;

    private final List<Action> actions = new ArrayList<>();

    public Node(String type, double xPos, double yPos, boolean input, boolean output) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.inputPort = input;
        this.outputPort = output;
    }

    public void addActionA(Action action) {
        actions.add(action);
    }

    public List<Action> geActions() {
        return actions;
    }

    public void run() {
        for (Action action : actions) {
            action.execute();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public boolean getInputPort() {
        return inputPort;
    }

    public boolean getOutputPort() {
        return outputPort;
    }
}
