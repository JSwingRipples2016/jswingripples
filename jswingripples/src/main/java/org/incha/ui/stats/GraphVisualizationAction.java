package org.incha.ui.stats;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.incha.core.jswingripples.GraphBuilder;
import org.incha.ui.JSwingRipplesApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Manuel Olguín (molguin@dcc.uchile.cl) on 4/28/16.
 * Part of org.incha.ui.
 */
public class GraphVisualizationAction implements ActionListener{

    public GraphVisualizationAction() { super(); }

    private void showGraph()
    {
        JSwingRipplesApplication.getInstance()
                .addComponentAsTab(
                        GraphBuilder.getInstance().getDependencyViewer().addDefaultView(false),
                        "Dependencies");
    }
    
    /*public NodeSizeChangeAction get_Node_size(){
    	return GraphBuilder.getInstance().get_node_size_changer();
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        showGraph();
    }
}

