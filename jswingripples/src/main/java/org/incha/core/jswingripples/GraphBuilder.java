package org.incha.core.jswingripples;

import org.apache.commons.logging.LogFactory;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import org.incha.core.jswingripples.eig.*;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.ui.TaskProgressMonitor;
import org.incha.ui.jripples.EIGStatusMarks;
import org.incha.ui.stats.DependencyGraphViewerListener;
import org.incha.ui.stats.ImpactGraphViewerListener;


/**
 * Created by Manuel Olguín (molguin@dcc.uchile.cl) on 4/26/2016.
 * Part of org.incha.core.jswingripples.
 */

public class GraphBuilder implements JSwingRipplesEIGListener{
	
	String style = "graph{fill-color:#fff4e6; } node {size-mode:dyn-size; size: 40px, 40px;shape: circle;fill-color: #854442;text-alignment: at-right;text-size:20;text-color: #3c2f2f;stroke-mode:plain; stroke-color:#3c2f2f; stroke-width:2;} edge{fill-color:#3c2f2f; shape:cubic-curve;size:3;} node:clicked { fill-color: darkmagenta;} node:selected{stroke-mode:plain; stroke-color:black; stroke-width:5;} node.blank {size-mode:dyn-size; size: 40px, 40px;shape: circle;fill-color: black;text-alignment: at-right;text-size:20;text-color: #3c2f2f;stroke-mode:plain; stroke-color:#3c2f2f; stroke-width:2;} node.changed {size-mode:dyn-size; size: 40px, 40px;shape: circle;fill-color: red;text-alignment: at-right;text-size:20;text-color: #3c2f2f;stroke-mode:plain; stroke-color:#3c2f2f; stroke-width:2;} node.impacted {size-mode:dyn-size; size: 40px, 40px;shape: circle;fill-color: darkred;text-alignment: at-right;text-size:20;text-color: #3c2f2f;stroke-mode:plain; stroke-color:#3c2f2f; stroke-width:2;} node.next {size-mode:dyn-size; size: 40px, 40px;shape: circle;fill-color: green;text-alignment: at-right;text-size:20;text-color: #3c2f2f;stroke-mode:plain; stroke-color:#3c2f2f; stroke-width:2;} node.propagating {size-mode:dyn-size; size: 40px, 40px;shape: circle;fill-color: #FF8C00;text-alignment: at-right;text-size:20;text-color: #3c2f2f;stroke-mode:plain; stroke-color:#3c2f2f; stroke-width:2;}";

    private static GraphBuilder instance = null; // singleton

    private JSwingRipplesEIG eig;
    private Graph graph;
    private Graph impactSetGraph;
    private Viewer impactViewer;
    private Viewer dependencyViewer;
    private DependencyGraphViewerListener depListener;
    private ImpactGraphViewerListener impListener;
    private int size;
    private int text_size;


    /**
     * Private constructor for singleton instance.
     */
    private GraphBuilder()
    {
        eig = null;
        resetGraphs();
    }

    /**
     * Resets the internal graphs.
     */
    public void resetGraphs()
    {
    	
        //stop viewers
        if(depListener != null)
            depListener.stopPumpThread();
        if(impListener != null)
            impListener.stopPumpThread();

        graph = new MultiGraph("Dependencies");
        impactSetGraph = new DefaultGraph("Impact Set");
        try {
        	
            /*graph.addAttribute("ui.stylesheet", "url(file://" + GraphBuilder.class.getClassLoader()
                    .getResource("graph.css").toString().substring(5) + ")"); */
        	graph.addAttribute("ui.stylesheet", style);
            /*impactSetGraph.addAttribute("ui.stylesheet", "url(file://" + GraphBuilder.class.getClassLoader()
                    .getResource("graph.css").toString().substring(5) + ")"); */
        	impactSetGraph.addAttribute("ui.stylesheet", style);
        }
        catch (NullPointerException e) {
            LogFactory.getLog(this.getClass()).error("Missing graph stylesheet! - graph.css");
            System.exit(1);
        }

        impactViewer = new Viewer(impactSetGraph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewerPipe pipe = impactViewer.newViewerPipe();
        impListener = new ImpactGraphViewerListener(pipe);
        pipe.addViewerListener(impListener);
        pipe.addSink(impactSetGraph);
        impactViewer.enableAutoLayout();
        /* dependencyViewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD); */
        dependencyViewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewerPipe piped = dependencyViewer.newViewerPipe();
        depListener = new DependencyGraphViewerListener(piped);
        piped.addViewerListener(depListener);
        piped.addSink(graph);
        dependencyViewer.enableAutoLayout();

    }

    public Viewer getImpactViewer()
    {
        return impactViewer;
    }

    public Viewer getDependencyViewer()
    {
        return dependencyViewer;
    }
    public int getSize(){
    	return size;
    }
    public void setSize(int newsize){
    	size = newsize;
    }

    /**
     * Returns the current instance of the singleton GraphBuilder object.
     * @return GraphBuilder object instance.
     */
    public static GraphBuilder getInstance() {
        if ( instance == null )
        {
            instance = new GraphBuilder();
        }

        return instance;
    }

    /**
     * Associates this GraphBuilder instance with the provided EIG.
     * @param eig The EIG from which we want to build a visual graph.
     */
    public void addEIG(JSwingRipplesEIG eig) { this.eig = eig; }

    /**
     * Constructs the Graph objects from the previously provided EIG instance.
     */
    public void prepareGraphs()
    {

        if ( eig == null )
            return;
        size = 40;
        text_size = 15;
        JSwingRipplesEIGNode[] eigNodes = eig.getAllNodes();
        JSwingRipplesEIGEdge[] eigEdges = eig.getAllEdges();

        for ( int i = 0; i < eigNodes.length; i++ ) {
            JSwingRipplesEIGNode node = eigNodes[i];
            if (graph.getNode(node.getFullName()) == null) {
                    Node n = graph.addNode(node.getFullName());
                    n.addAttribute("label", node.getShortName());
            }
        }
        for ( int i = 0; i < eigEdges.length; i++ )
        {
            JSwingRipplesEIGEdge edge = eigEdges[i];
            String eid = edge.getFromNode().getFullName() + " -> " + edge.getToNode().getFullName();
            if ( graph.getEdge(eid) == null){
                graph.addEdge(eid, edge.getFromNode().getFullName(), edge.getToNode().getFullName(), true);
            }
        }
    }

    public Graph getDependencyGraph() { return graph; }
    public Graph getImpactSetGraph() { return impactSetGraph; }
  

    @Override
    public void jRipplesEIGChanged(JSwingRipplesEIGEvent event) {

        if (!event.hasNodeEvents()) return;

        final JSwingRipplesEIGNodeEvent[] nodeEvents=event.getNodeTypedEvents(
                new int[] {JSwingRipplesEIGNodeEvent.NODE_MARK_CHANGED, JSwingRipplesEIGNodeEvent.NODE_REMOVED,
                        JSwingRipplesEIGNodeEvent.NODE_ADDED});
        if (nodeEvents.length==0) return;

        for (int i=0;i<nodeEvents.length;i++) {
            final JSwingRipplesEIGNode changedNode = nodeEvents[i].getSource();
            Node graphn = graph.getNode(changedNode.getFullName());
            Node impactn = impactSetGraph.getNode(changedNode.getFullName());
            switch (nodeEvents[i].getEventType()) {
                case JSwingRipplesEIGNodeEvent.NODE_REMOVED: {
                    //TODO
                    break;
                }
                case JSwingRipplesEIGNodeEvent.NODE_ADDED: {
                    //TODO
                    break;
                }
                case JSwingRipplesEIGNodeEvent.NODE_MARK_CHANGED: {
                    final String mark=changedNode.getMark();
                    if (mark!=null)
                    {
                        switch (mark)
                        {
                            case EIGStatusMarks.CHANGED:
                                graphn.setAttribute("ui.class", "changed");

                                if ( impactn == null ) {
                                    impactn = impactSetGraph.addNode(changedNode.getFullName());
                                    impactn.setAttribute("ui.class", "changed");
                                    impactn.setAttribute("label", changedNode.getShortName());
                                }
                                else
                                    impactn.setAttribute("ui.class", "changed");
                                break;
                            case EIGStatusMarks.IMPACTED:
                                graphn.setAttribute("ui.class", "impacted");

                                if ( impactn == null ) {
                                    impactn = impactSetGraph.addNode(changedNode.getFullName());
                                    impactn.setAttribute("ui.class", "impacted");
                                    impactn.setAttribute("label", changedNode.getShortName());
                                }
                                else
                                    impactn.setAttribute("ui.class", "impacted");

                                break;
                            case EIGStatusMarks.NEXT_VISIT:
                                graphn.setAttribute("ui.class", "next");

                                if ( impactn == null ) {
                                    impactn = impactSetGraph.addNode(changedNode.getFullName());
                                    impactn.setAttribute("ui.class", "next");
                                    impactn.setAttribute("label", changedNode.getShortName());
                                }
                                else
                                    impactn.setAttribute("ui.class", "next");

                                break;
                            case EIGStatusMarks.VISITED_CONTINUE:
                                graphn.setAttribute("ui.class", "propagating");

                                if ( impactn == null ) {
                                    impactn = impactSetGraph.addNode(changedNode.getFullName());
                                    impactn.setAttribute("ui.class", "propagating");
                                    impactn.setAttribute("label", changedNode.getShortName());
                                }
                                else
                                    impactn.setAttribute("ui.class", "propagating");

                                break;
                            default:
                                graphn.setAttribute("ui.class", "blank");

                                if ( impactn != null ) {
                                    impactSetGraph.removeNode(impactn);
                                    impactn = null;
                                }
                                break;
                        }

                    }
                    break;
                }
            }

            updateImpactSetEdges();
        }
    }

    private void updateImpactSetEdges()
    {
        JSwingRipplesEIGEdge[] edges = eig.getAllEdges();
        for(JSwingRipplesEIGEdge edge: edges)
        {
            String toID = edge.getToNode().getFullName();
            String fromID = edge.getFromNode().getFullName();
            String eID = fromID + " -> " + toID;
            if (impactSetGraph.getEdge(eID) != null) // edge exists
            {
                if(impactSetGraph.getNode(toID) == null || impactSetGraph.getNode(fromID) == null)
                    // edge exists but nodes don't -> remove edge
                    impactSetGraph.removeEdge(eID);
            }
            else // edge doesn't exist
            {
                if(impactSetGraph.getNode(toID) != null && impactSetGraph.getNode(fromID) != null)
                    // nodes exist -> add edge
                    impactSetGraph.addEdge(eID, fromID, toID);
            }

        }
    }

	public int getTextsize() {
		return text_size;
	}

	public void setTextSize(int text_size2) {
		text_size = text_size2;
		
	}
}
