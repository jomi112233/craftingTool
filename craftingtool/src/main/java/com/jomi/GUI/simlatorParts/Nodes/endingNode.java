package com.jomi.GUI.simlatorParts.Nodes;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

public class endingNode extends NodeView {
    
    public endingNode(Node node, Project project) {
        super(node, project);

        setTitle("End Node");
    }
}
