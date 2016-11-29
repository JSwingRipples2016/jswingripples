package org.incha.ui.stats;

import org.incha.core.jswingripples.GraphBuilder;
import org.incha.ui.JSwingRipplesApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Manuel Olguín (molguin@dcc.uchile.cl) on 6/2/16.
 * Part of org.incha.ui.stats.
 */
public class ImpactGraphVisualizationAction implements ActionListener {

    public ImpactGraphVisualizationAction() { super(); }

    private void showGraph()
    {
        JSwingRipplesApplication.getInstance()
                .addComponentAsTab(
                        GraphBuilder.getInstance().getImpactViewer().addDefaultView(false),
                        "Impact Set");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showGraph();
    }
}
