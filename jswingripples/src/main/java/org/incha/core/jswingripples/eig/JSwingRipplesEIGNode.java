/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.eig;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.incha.ui.jripples.EIGStatusMarks;
/**
 * JRipplesEIGNode class represents a node of the EIG (Evolving Interoperation
 * Graph). Node serves as the container for the components of the project under
 * analysis, as well as the container for the extra information that can be associated
 * with this class like Incremental Change status(mark) and probability.
 * @see JSwingRipplesEIG
 * @author Maksym Petrenko
 *
 */
public  class JSwingRipplesEIGNode {
	// public static JRipplesEIGNode[] NONE = new JRipplesEIGNode[] {};

	private String mark = EIGStatusMarks.BLANK;
	private String probability;

	private final IMember nodeMember; 

    private JSwingRipplesEIGNode node;
    private JSwingRipplesEIG eig;

	/**
	 * Constructor - creates a node with the specified IMember and
	 * sets node's mark and probability to empty string.<br>
	 * Please note that nodes, created directly with the constructor,
	 * will not be handeleded in the EIG. To creare a node that is
	 * handeled by the EIG, use {@link JSwingRipplesEIG#addNode(IMember)}
	 * instead.
	 *
	 * @param member
	 *            IMember, which this node represents
	 */

	public JSwingRipplesEIGNode(final JSwingRipplesEIG eig, final IMember member) {
		if (member==null) {
		    throw new NullPointerException("Null IMember");
		}
		this.eig = eig;
		node = this;
        this.nodeMember = member;
	}

	/**
	 * Returns node's short name - simple, human-readable string, which is used
	 * for node presentation in GUI. Simple name is obtained by calling
	 * {@link org.eclipse.jdt.core.IMember.getElementName()}.
	 * Couple different nodes within the same EIG can have the same short name.
	 * Use {@link #getFullName()} instead if you need to get node's unique name.
	 *
	 * @return short name of the node's underlying <code>IMember<code> if it is set,
	 * empty string otherwise
	 * @domain
	 * EIG node
	 */

	public String getShortName() {

		if(nodeMember==null) return "";
		final String name= nodeMember.getElementName();
		if (name!=null) return name;
		return "";

	}

	/**
	 * Returns node's fully qualified name. For if the underlying memeber is
	 * of type IType, the returned full name is equal to the
	 * fully qualified name of the node's underlying IMember. For IMethod and
	 * IField type of IMember, the full name is composed by taking fully qualified name
	 * of the top declaring class of this IMember, and adding the short name
	 * of the <code>IMember<code> to it, separated by a "::" string.
	 *
	 * Use {@link #getShortName()} if you need to get node's simlple name
	 * to present this node in a GUI.
	 *
	 * @return fully qualified name of the underlying member if it is set,
	 * 	empty string otherwise
	 */

	public String getFullName() {
		String fullName;
		if(nodeMember==null) return "";
		if (nodeMember instanceof IType) fullName=((IType) nodeMember).getFullyQualifiedName();
			else {fullName=JSwingRipplesIMemberServices.getTopDeclaringType(nodeMember).getFullyQualifiedName()+"::"+nodeMember.getElementName();

			}

		if (fullName!=null) return fullName;
		return "";
	}

	public String getAbsolutePath() {
		String absPath = null;
		if(nodeMember == null) return "";
		if(nodeMember.getCompilationUnit() != null) absPath = nodeMember.getCompilationUnit().getPath().toString();
		if(absPath != null) return absPath;
		return "";
	}

	/**
	 * associates EIG mark with the node. Typically, EIG marks are used to
	 * denote the status of the node's underlying member during Incremental
	 * Change process.
	 *
	 * @return EIG mark of the node if there is a one; blank string
	 *         otherwise.
	 * @see #setMark(String)
	 */

	public String getMark() {
		if (this.mark==null) this.mark="";
		return mark;
	}

	/**
	 * Returns node's underlying {@link org.eclipse.jdt.core.IMember}  -
	 * the actual component of <code>IMember<code> type of the project ander analysis,
	 * which this node represents.
	 *
	 * @return underlying instance of <code>IMember<code> of the node if there is a one; <code>null</code>
	 *         otherwise.
	 * @see #setNodeIMember(IMember)
	 */

	public IMember getNodeIMember() {
		return nodeMember;
	}

	/**
	 * Return probability value associated with the node. Typically, probabilities
	 * are used to denote the probability of some event, that can happen with the
	 * node's underlying member. Probabilities can be evaluated through different
	 * software metrics during Incremental Change process.
	 *
	 * @return probability, associated with the node if there is a one;
	 *         <code>null</code> otherwise.
	 * @see #setProbability(String)
	 */

	public String getProbability() {
		return probability;
	}

	/**
	 * Tests if the node is a top-level class. This is done by investigating
	 * underlying <code>IMember<code> java element.
	 *
	 * @return <code>true<code>, if the class is a top class;
	 *         <code>false</code> otherwise.
	 * @see #getNodeIMember()
	 *
 	 */

	public boolean isTop() {
		try {
			if ((nodeMember instanceof IType) && (!((IType)nodeMember).isMember())) return true;
		} catch (final Exception e) {
			//do nothing
		}
		return false;
	}

	/**
	 * associates EIG mark with the node. Typically, EIG marks are used to
	 * denote the status of the node's underlying member during Incremental
	 * Change process.
	 *
	 * @param mark
	 *            EIG mark to be associated with this node
	 * @see #getMark()
	 */
	public void setMark(final String mark) {
		if (isEquals(this.mark, mark)) {
		    return;
		}

		final String oldMark = this.mark;
		this.mark = mark;
		eig.fireJRipplesEIGChanged(node, JSwingRipplesEIGNodeEvent.NODE_MARK_CHANGED, oldMark, mark);
	}

	/**
     * @param mark1
     * @param mark2
     * @return
     */
    private boolean isEquals(final String mark1, final String mark2) {
        return mark1 == null && mark2 == null || mark1 != null && mark1.equals(mark2);
    }

    /**
	 * associate probability value with the node. Typically, probabilities are
	 * used to denote the probability of some event, that can happen with the
	 * node's underlying member. Probabilities can be evaluated through different
	 * software metrics during Incremental Change process.
	 *
	 * @param probability
	 *            probability to be associated with this node
	 * @see #getProbability()
	 */

	public void setProbability(final String probability) {
		if ((probability!=null) && this.probability!=null)
			if (probability.compareTo(this.probability)==0) return;

		final String oldProbability = this.probability;
		this.probability = probability;
		/*undoHistory.addFirst(this.getProbability());
		undoHistory.addFirst("setProbability");
		if (!JRipplesEIG.redoInProgress) clearRedoHistory();
		this.probability = probability;
		*/
		//will not be put into the EIG history

		eig.fireJRipplesEIGChanged(node, JSwingRipplesEIGNodeEvent.NODE_PROBABILITY_CHANGED,
		        oldProbability, probability);
	}

	/**
	 * Returns node's fully qualified name - see {@link #getFullName()}.
	 * @return node's fully qualified name - see {@link #getFullName()} for more details.
	 */
	@Override
    public String toString() {
		return this.getFullName();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
	    if (!(obj instanceof JSwingRipplesEIGNode)) {
	        return false;
	    }

	    final JSwingRipplesEIGNode node = (JSwingRipplesEIGNode) obj;

	    return getNodeIMember().equals(node.getNodeIMember());
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    return getNodeIMember().hashCode();
	}
	/**
     * @return the eig
     */
    public JSwingRipplesEIG getEig() {
        return eig;
    }
}
