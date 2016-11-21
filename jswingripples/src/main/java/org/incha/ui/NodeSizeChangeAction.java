package org.incha.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.incha.core.jswingripples.GraphBuilder;

public class NodeSizeChangeAction implements ActionListener {
	int state;
	int s;
	int b;
	double alfa;
	double maxsize;
	double minsize;
	public NodeSizeChangeAction(){
		state = 0;
		s = -1;
		b = -1;
		alfa = 1.0;
		maxsize = alfa*60;
		minsize = alfa*10;
	}
	
	
	 private void changeSize()
	    {
	        /*Graph graph = GraphBuilder.getInstance().getDependencyGraph();

	        Viewer v = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
	        v.enableAutoLayout();*/
		 	Graph g = GraphBuilder.getInstance().getDependencyGraph();
		 	if (s == -1 && b == -1){
	        for(Node nn:g.getEachNode()){
	        	if(nn.getDegree() > b || b == -1){
	        	b = nn.getDegree();
	        	}
	        	if(nn.getDegree() < s || s == -1){
	        	s = nn.getDegree();
	        	}
	        }
		 	}
	        
	        for(Node nn:g.getEachNode()){
	        	double scale = (double)(nn.getDegree() - s) / (double) (b-s);
	        	
	        	nn.addAttribute("ui.size", Math.round((scale*maxsize)+minsize));
	        	/*nn.addAttribute("ui.size", 15);*/
	        }
	        state = 1;
	        /* g.addAttribute("ui.stylesheet", s); */
	        

	    }
	 private void revertSize(){
		 Graph g = GraphBuilder.getInstance().getDependencyGraph();
		 for(Node nn:g.getEachNode()){
			  	nn.addAttribute("ui.size", 5);
	        	nn.removeAttribute("ui.size");
	        	/*nn.addAttribute("ui.size", 15);*/
	        }
		 /*g.removeAttribute("ui.stylesheet");*/
		 state = 0;
	 }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (state == 0){
		changeSize();
		}else{
			revertSize();
		}

	}

}
