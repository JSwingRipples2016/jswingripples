package org.incha.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.incha.core.jswingripples.GraphBuilder;

public class NodeColorChangeAction implements ActionListener {
	String c;
	public NodeColorChangeAction(String color){
		c = color;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String style = "node {fill-color:"+c+";} node.blank {fill-color:"+c+";} node.changed {fill-color:"+c+";} node.impacted {fill-color:"+c+";}  node.next {fill-color:"+c+";}  node.propagating {fill-color:"+c+";}";
		GraphBuilder.getInstance().getDependencyGraph().addAttribute("ui.stylesheet", style);
		GraphBuilder.getInstance().getImpactSetGraph().addAttribute("ui.stylesheet", style);
	}

}
