package org.incha.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.graphstream.graph.Graph;
import org.incha.core.jswingripples.GraphBuilder;

public class NodeChangeAction implements ActionListener {
	int type;
	public NodeChangeAction(int i){
		type = i;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Graph g1 = GraphBuilder.getInstance().getDependencyGraph();
		Graph g2 = GraphBuilder.getInstance().getImpactSetGraph();
		int size = GraphBuilder.getInstance().getSize();
		if (type == 0){
		size = size + 4;
		}else{
			if (size > 4){
				size = size - 4;
				}
		}
		GraphBuilder.getInstance().setSize(size);
		String style = "node {size:" + Integer.toString(size) + "px," + Integer.toString(size) + "px;} node.blank {size:" + Integer.toString(size) + "px," + Integer.toString(size) + "px;} node.changed {size:" + Integer.toString(size) + "px," + Integer.toString(size) + "px;} node.impacted {size:" + Integer.toString(size) + "px," + Integer.toString(size) + "px;} node.next {size:" + Integer.toString(size) + "px," + Integer.toString(size) + "px;} node.propagating {size:" + Integer.toString(size) + "px," + Integer.toString(size) + "px;}";
		g1.addAttribute("ui.stylesheet", style);
		g2.addAttribute("ui.stylesheet", style);

	}

}
