package org.incha.ui.stats;

import java.util.LinkedList;
import java.util.List;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdgeEvent;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEvent;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGListener;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNodeEvent;
import org.incha.core.jswingripples.eig.JSwingRipplesEvent;
import org.incha.core.jswingripples.eig.history.EdgeAddedAction;
import org.incha.core.jswingripples.eig.history.EdgeRemovedAction;
import org.incha.core.jswingripples.eig.history.MarkSetAction;
import org.incha.core.jswingripples.eig.history.NodeAddedAction;
import org.incha.core.jswingripples.eig.history.NodeRemovedAction;
import org.incha.core.jswingripples.eig.history.ProbabilitySetAction;
import org.incha.core.jswingripples.eig.history.UndoAction;

public class UndoActionsBuilder implements JSwingRipplesEIGListener {
    private final List<UndoAction> actions = new LinkedList<UndoAction>();

    /**
     * Default constructor.
     */
    public UndoActionsBuilder() {
        super();
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.JSwingRipplesEIGListener#jRipplesEIGChanged(org.incha.core.jswingripples.eig.JSwingRipplesEIGEvent)
     */
    @Override
    public void jRipplesEIGChanged(final JSwingRipplesEIGEvent eigEvent) {
        final JSwingRipplesEvent[] events = eigEvent.getEvents();

        for (final JSwingRipplesEvent evt : events) {
            if (evt instanceof JSwingRipplesEIGNodeEvent) {
                final JSwingRipplesEIGNodeEvent e = (JSwingRipplesEIGNodeEvent) evt;

                switch (e.getEventType()) {
                    case JSwingRipplesEIGNodeEvent.NODE_ADDED:
                        this.actions.add(new NodeAddedAction(e.getSource()));
                        break;
                    case JSwingRipplesEIGNodeEvent.NODE_REMOVED:
                        this.actions.add(new NodeRemovedAction(e.getSource()));
                        break;
                    case JSwingRipplesEIGNodeEvent.NODE_MARK_CHANGED:
                        this.actions.add(new MarkSetAction(e.getSource(),
                                e.getOldValue(), e.getNewValue()));
                        break;
                    case JSwingRipplesEIGNodeEvent.NODE_PROBABILITY_CHANGED:
                        this.actions.add(new ProbabilitySetAction(e.getSource(),
                                e.getOldValue(), e.getNewValue()));
                        break;
                        default:
                            //impossible
                }
            } else if (evt instanceof JSwingRipplesEIGEdgeEvent) {
                final JSwingRipplesEIGEdgeEvent e = (JSwingRipplesEIGEdgeEvent) evt;

                switch (e.getEventType()) {
                    case JSwingRipplesEIGEdgeEvent.EDGE_ADDED:
                        this.actions.add(new EdgeAddedAction(e.getSource()));
                        break;
                    case JSwingRipplesEIGEdgeEvent.EDGE_COUNT_CHANGED:
                        break;
                    case JSwingRipplesEIGEdgeEvent.EDGE_FROM_NODE_CHANGED:
                        break;
                    case JSwingRipplesEIGEdgeEvent.EDGE_MARK_CHANGED:
                        break;
                    case JSwingRipplesEIGEdgeEvent.EDGE_PROBABILITY_CHANGED:
                        break;
                    case JSwingRipplesEIGEdgeEvent.EDGE_REMOVED:
                        this.actions.add(new EdgeRemovedAction(e.getSource()));
                        break;
                    case JSwingRipplesEIGEdgeEvent.EDGE_TO_NODE_CHANGED:
                        break;
                }
            }
        }
    }

    /**
     * @return
     */
    public List<UndoAction> getActions() {
        return actions;
    }
}
