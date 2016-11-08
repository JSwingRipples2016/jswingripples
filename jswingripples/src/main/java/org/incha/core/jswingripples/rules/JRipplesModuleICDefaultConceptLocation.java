/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.incha.core.jswingripples.JRipplesICModuleInterface;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICDefaultConceptLocation implements JRipplesICModuleInterface {
	private JSwingRipplesEIGNode currentNode = null;
	private final JSwingRipplesEIG eig;

	/**
     * @param eig the eig.
     */
    public JRipplesModuleICDefaultConceptLocation(final JSwingRipplesEIG eig) {
        super();
        this.eig = eig;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.severe.jripples.modules.interfaces.JRipplesICModuleInterface#GetAvailableRulesForMark(java.lang.String)
	 */
	@Override
    public Set<String> GetAvailableRulesForMark(final String mark) {

		if (mark == null) {

			return null;
		} else if (mark.compareTo(EIGStatusMarks.BLANK) == 0) {
			return null;

		} else if (mark.compareTo(EIGStatusMarks.NEXT_VISIT) == 0) {
			final String marks[] = { EIGStatusMarks.LOCATED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED };
			return (new LinkedHashSet<>(Arrays.asList(marks)));
		} else if (mark.compareTo(EIGStatusMarks.LOCATED) == 0) {
			final String marks[] = { EIGStatusMarks.LOCATED};
			return (new LinkedHashSet<>(Arrays.asList(marks)));
		} else if (mark.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0) {
			final String marks[] = { EIGStatusMarks.LOCATED,EIGStatusMarks.VISITED_CONTINUE};
			return (new LinkedHashSet<>(Arrays.asList(marks)));
		} else {
			return null;
		}
	}

    @Override
    public void InitializeStage(JRipplesModuleRunner moduleRunner) {
        JSwingRipplesEIGNode[] nodes = null;
        do {
               nodes = eig.getAllNodes();
        } while (nodes == null);

        
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].setMark(EIGStatusMarks.BLANK);
            }

            if (eig.getMainClass() != null) {
                final JSwingRipplesEIGNode mainType = getType(nodes);
                if (mainType != null) {
                    mainType.setMark(EIGStatusMarks.NEXT_VISIT);
                    currentNode = mainType;
                }
            }
        }
        moduleRunner.moduleFinished();
        eig.getHistory().clear();
    }
/*    @Override
    public void InitializeStage(JRipplesModuleRunner moduleRunner) {
        if(!eig.isInitialized()){
        }
            
    }*/

	/*
	 * (non-Javadoc)
	 *
	 * @see org.severe.jripples.modules.interfaces.JRipplesICModuleInterface#ApplyRuleAtNode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
    public void ApplyRuleAtNode(final String rule, final JSwingRipplesEIGNode node, final int granularity) {
        currentNode=node;
        if (rule.compareTo(EIGStatusMarks.LOCATED) == 0) {
            CommonEIGRules.assignMarkToNodeAndParents(eig, node,EIGStatusMarks.LOCATED);
        } else if (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0) {
            CommonEIGRules.applyRuleToNode(eig, node,rule,granularity);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, node,rule,0);
        }

	}

	@Override
    public void ApplyRuleAtNode(final String rule, final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if (rule.compareTo(EIGStatusMarks.LOCATED) == 0) {
            CommonEIGRules.assignMarkToNodeAndParents(eig, nodeFrom,EIGStatusMarks.LOCATED);

        } else if (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo, rule,EIGStatusMarks.NEXT_VISIT);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
        }
	}

    @Override
    public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
        InitializeStage(moduleRunner);
    }

    private JSwingRipplesEIGNode getType(final JSwingRipplesEIGNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            final IMember member = nodes[i].getNodeIMember();
            //Searching for the class main class (full name, with package name)
            //Example: if the main class of the project Dynamic is called DynamicScope and it belongs
            //to dynamicscope package, the main class should be called dynamicscope.DynamicScope
            if (member instanceof IType && ((IType) member).getElementName().equals(
                    eig.getMainClass().replaceFirst("[.][^.]+$", ""))) {
                return nodes[i];
            }
        }
        return null;
    }
}
