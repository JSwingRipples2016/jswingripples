package org.incha.core.jswingripples;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.search.Highlight;
import org.incha.core.search.Searcher;

public class NodeSearchBuilder {

    private JSwingRipplesEIG eig;
    private static NodeSearchBuilder instance = null; // singleton
    private Graph graph;    
    private JSwingRipplesEIGNode[] eigNodes;    
    private List<String> actual_nodes = new ArrayList<String>();
        
    
   private NodeSearchBuilder()
    {
       
	   graph = new DefaultGraph("Search Graph"); 

    }
   
   
   public void setSearch(){
	  // search = s;
	   System.out.println("PREPARING GRAPH");
	   prepareGraph();
	   System.out.println("GRAPH PREPARED");
   }
   
   public void clearGraph(){
	   graph.clear();
   }
   
   public static NodeSearchBuilder getInstance() {
       if ( instance == null )
       {
           instance = new NodeSearchBuilder();
       }

       return instance;
   }


    public void addEIG(JSwingRipplesEIG eig) throws CloneNotSupportedException
    {
    	
        this.eig = (JSwingRipplesEIG)eig.clone();
        eigNodes = eig.getAllNodes();
    }
    
    private JSwingRipplesEIGNode[] addRelatedNodes(JSwingRipplesEIGNode node){
        addNode(node);
    	JSwingRipplesEIGNode[] neighbors = eig.getAllAnyNodeNeighbors(node);
    	for (JSwingRipplesEIGNode n : neighbors){
    		//if (graph.getNode(n.getShortName()) == null){
    			addNode(n);
    		//}
    	}
    	return neighbors;
    }
    
    private void addNode(JSwingRipplesEIGNode node){
    	System.out.println("Current Nodes : " + String.valueOf(graph.getNodeCount()));
    	if (graph.getNode(node.getShortName()) == null){
    		if (!actual_nodes.contains(node.getShortName())) {
    			Node n = graph.addNode(node.getShortName());
    	    	n.addAttribute("label", node.getShortName());   	
    	    	actual_nodes.add(node.getShortName());
    		}    	   	
    	
    	}
    	System.out.println("After add : " +  String.valueOf(graph.getNodeCount()));
    }
    
    private void addEdgeIfExisting(JSwingRipplesEIGNode node1, JSwingRipplesEIGNode node2){
    	JSwingRipplesEIGEdge edge = eig.getEdge(node1, node2);
    	if (edge != null){
    		String eid = node1.getShortName() + " -> " + node2.getShortName();
    		if (graph.getEdge(eid) == null){    			
    		   graph.addEdge(eid, graph.getNode(node1.getShortName()), graph.getNode(node2.getShortName()));
    		}
    	}
    }
    
    private void computeAndAddEdges(JSwingRipplesEIGNode node, JSwingRipplesEIGNode[] neighbors){      	
    	for (JSwingRipplesEIGNode n : neighbors){
    		addEdgeIfExisting(node,n);
    	}
    	for (JSwingRipplesEIGNode n : neighbors){
    		for (JSwingRipplesEIGNode m : neighbors){
    			if (!m.getShortName().equals(node.getShortName()) && !m.getShortName().equals(n.getShortName())) {
    				addEdgeIfExisting(n,m);
    			}    			
    		}
    	}
    }

    private void prepareGraph()
    {
    	System.out.println("Looking at EIG nodes");
    	if (eig == null){
    		System.out.println("This is empty");
    	}
        //JSwingRipplesEIGNode[] eigNodes = eig.getAllNodes();
        System.out.println("Looking at EIG nodes");
        for ( JSwingRipplesEIGNode node : eigNodes ){
        	for (String searched_node : Searcher.getInstance().getResults().keySet()) {
            	if (searched_node.equals(node.getShortName())) {
            		computeAndAddEdges(node,addRelatedNodes(node));   
            		int result_index = Searcher.getInstance().getResults().get(searched_node);
                    openNode(node, Searcher.getInstance().getResults().size(), result_index);
            	}
            }
        }           	
        System.out.println("No more to look");
    }
    
    private void openNode(JSwingRipplesEIGNode node, int total_words, int result_index) { //Node node, int total_words) {
    	String filename = node.getShortName();
		int node_size;
    	Color color = Highlight.getColor(filename);
        String rgb = "rgb(" + color.getRed() + 
        		     "," + color.getGreen() + 
        		     "," + color.getBlue() + ")";
    	if (total_words >= 6) {
    		node_size = 50;
    	}
    	else {
    		node_size = 300 - 20*(total_words - 1);
    	}
    	
    	int num_lin = (int)Searcher.getInstance().getResInfo().get(result_index)[2];
    	String result_line = (String)Searcher.getInstance().getResInfo().get(result_index)[0];
    	int char_num = 25; //número de caracteres que se verán de result_line
    	if (result_line.length()> char_num) {
    		result_line = result_line.substring(0, char_num) + "...";
    	}
    	graph.getNode(filename).setAttribute("label", node.getShortName().toUpperCase() + " \nline " + num_lin + ": " + result_line); // Se coloca el nombre del nodo y la línea comprometida con el searchquery
    	graph.getNode(filename).addAttribute("ui.style", "fill-color: " + rgb + "; "
    			                      + "size: " + node_size + "px;"
    			                      + "text-style: bold; "
    			                      + "text-size: 15px; "
    			                      + "stroke-mode: plain;" + "text-color: rgb(122,187,74);" //text-background-mode: plain; text-padding: 10px; text-background-color: " + rgb + ";"
    			                      + "stroke-color: black;");
    }   
    
    
    private void cleanActualNodes() {
		actual_nodes.clear();		
	}
    
    public Graph getGraph(){
    	return graph;
    }    
    
}

