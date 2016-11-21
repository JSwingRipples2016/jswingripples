package org.incha.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.incha.core.jswingripples.GraphBuilder;

public class Zoomed_text_changer implements ActionListener {
	int state;
	public Zoomed_text_changer(){
		state = 0;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String style = "";
		if (state == 0){
			style = "node {text-visibility-mode: under-zoom; text-visibility: 0.2;}  node.blank {text-visibility-mode: under-zoom; text-visibility: 0.2;}  node.changed {text-visibility-mode: under-zoom; text-visibility: 0.2;}  node.impacted {text-visibility-mode: under-zoom; text-visibility: 0.2;}  node.next {text-visibility-mode: under-zoom; text-visibility: 0.2;}  node.propagating {text-visibility-mode: under-zoom; text-visibility: 0.2;}";
			state = 1;
		}else{
			style = "node {text-visibility-mode: normal; text-visibility: 0.5;} node.blank {text-visibility-mode: normal; text-visibility: 0.5;} node.changed {text-visibility-mode: normal; text-visibility: 0.5;} node.impacted {text-visibility-mode: normal; text-visibility: 0.5;} node.next {text-visibility-mode: normal; text-visibility: 0.5;} node.propagating {text-visibility-mode: normal; text-visibility: 0.5;}";
			state = 0;
		}
		GraphBuilder.getInstance().getDependencyGraph().addAttribute("ui.stylesheet", style);
		GraphBuilder.getInstance().getImpactSetGraph().addAttribute("ui.stylesheet", style);

	}

}
