package org.incha.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.graphstream.graph.Graph;
import org.incha.core.jswingripples.GraphBuilder;

public class Text_size_changer implements ActionListener {
	int type;
	public Text_size_changer(int i){
		type = i;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Graph g1 = GraphBuilder.getInstance().getDependencyGraph();
		Graph g2 = GraphBuilder.getInstance().getImpactSetGraph();
		int text_size = GraphBuilder.getInstance().getTextsize();
		if (type == 0){
			text_size = text_size + 3;
		}else{
			if(text_size > 4){
				text_size = text_size - 3;
			}
		}
		GraphBuilder.getInstance().setTextSize(text_size);
		String style = "node {text-size:" + Integer.toString(text_size) + ";}  node.blank {text-size:" + Integer.toString(text_size) + ";}  node.changed {text-size:" + Integer.toString(text_size) + ";}  node.impacted {text-size:" + Integer.toString(text_size) + ";}  node.next {text-size:" + Integer.toString(text_size) + ";}  node.propagating {text-size:" + Integer.toString(text_size) + ";}";
		g1.addAttribute("ui.stylesheet", style);
		g2.addAttribute("ui.stylesheet", style);
	}

}
