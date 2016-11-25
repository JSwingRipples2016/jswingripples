package org.incha.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class CollectionUtils {
    public interface SynchHandler<I> {
        /**
         * @param item deleted item.
         */
        public void itemAdded(I item);
        /**
         * @param item added item.
         */
        public void itemDeleted(I item);
    }
    /**
     * Default constructor.
     */
    private CollectionUtils() {
        super();
    }

    /**
     * @param list list of items.
     * @param item the template item
     * @return the item from item list which has equal by template.
     */
    public static <M> M getEquals(final List<M> list, final M item) {
        for (final M f : list) {
            if (f.equals(item)) {
                return f;
            }
        }
        return null;
    }
    /**
     * @param originOldList old list.
     * @param originNewList new list.
     * @param handler synchronization handler.
     */
    public static <M> void synchronize(final List<M> originOldList, final List<M> originNewList,
            final SynchHandler<M> handler) {
        final List<M> oldList = new LinkedList<M>(originOldList);
        final List<M> newList = new LinkedList<M>(originNewList);

        //handle added files
        Iterator<M> iter = newList.iterator();
        while (iter.hasNext()) {
            final M item = iter.next();
            if (!oldList.remove(item)) {
                handler.itemAdded(item);
            }

            iter.remove();
        }

        //handle deleted files
        iter = oldList.iterator();
        while(iter.hasNext()) {
            handler.itemDeleted(iter.next());
        }
    }
}
