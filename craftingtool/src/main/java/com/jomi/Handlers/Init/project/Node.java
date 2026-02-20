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



    private final List<String> actions = new ArrayList<>();

    public Node() {

    }

    public Node(String type, double xPos, double yPos, boolean input, boolean output) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.inputPort = input;
        this.outputPort = output;
    }

    public void addActionA(String action) {
        actions.add(action);
    }

    public List<String> getActions() {
        return List.copyOf(actions);
    }

    public void setActions(List<String> actions) {
        this.actions.clear();
        this.actions.addAll(actions);
    }

    public void setAction(String action) {
        actions.add(action);
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
