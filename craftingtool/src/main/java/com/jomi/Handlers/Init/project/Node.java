package com.jomi.Handlers.Init.project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jomi.Util.ModRoller;

public class Node {
    private String id;
    private String type;
    private double xPos, yPos;
    private boolean iterator;
    private boolean active;

    private List<ModRoller.RolledMod> forcedTarget = new ArrayList<>();

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



    public boolean isIterator() {
        return iterator;
    }

    public void setIterator(boolean iterator) {
        this.iterator = iterator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<ModRoller.RolledMod> getForcedTarget() {
        return forcedTarget;
    }

    public void clearForcedTarget() {
        forcedTarget.clear();
    }

    public void setForcedTarget(List<ModRoller.RolledMod> forcedtarget) {
        this.forcedTarget.clear();
        this.forcedTarget = forcedtarget;
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
