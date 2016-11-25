package org.incha.core.jswingripples.parser;

import org.eclipse.jdt.core.IMember;

public class Edge {
    private IMember from;
    private IMember to;

    /**
     * Default constructor.
     */
    public Edge(final IMember from, final IMember to) {
        super();
        if (from == null || to == null) {
            throw new NullPointerException("Edge");
        }
        this.from = from;
        this.to = to;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Edge)) {
            return false;
        }

        final Edge other = (Edge) obj;;
        return from.equals(other.from) && to.equals(other.to);
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 31 * from.hashCode() + to.hashCode();
    }
    /**
     * @return the from
     */
    public IMember getFrom() {
        return from;
    }
    /**
     * @return the to
     */
    public IMember getTo() {
        return to;
    }
}
