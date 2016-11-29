package org.incha.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import junit.framework.TestCase;

public class BuildPathTest extends TestCase implements PropertyChangeListener {
    /**
     * Currently handled property change event.
     */
    private PropertyChangeEvent propertyChangeEvent;
    /**
     * Build path to test.
     */
    private BuildPath buildPath;

    /**
     * Default constructor.
     */
    public BuildPathTest() {
        super();
    }
    /**
     * @param name the test case name.
     */
    public BuildPathTest(final String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        buildPath = new JavaProject("JUnit").getBuildPath();
        buildPath.addPropertyChangeListener(this);
    }

    public void testAddSource() {
        final File f = new File("/a/b/c/d");

        buildPath.addSource(f);

        //check source added.
        assertEquals(1, buildPath.getSources().size());
        //check property changed event has sent
        assertNotNull(propertyChangeEvent);
        //check the event name is correct
        assertEquals(BuildPath.SOURCES, propertyChangeEvent.getPropertyName());
        //check old value is correct
        assertEquals(0, ((List<?>) propertyChangeEvent.getOldValue()).size());
        //check new value is correct
        assertEquals(1, ((List<?>) propertyChangeEvent.getNewValue()).size());

        //clear property change event
        propertyChangeEvent = null;
        //check can't add duplicate
        buildPath.addSource(f);

        assertEquals(1, buildPath.getSources().size());
        assertNull(propertyChangeEvent);
    }
    public void testDeleteSource() {
        final File f1 = new File("/a/b/c/d");
        final File f2 = new File("/a/b/c/e");

        buildPath.addSource(f1);
        buildPath.addSource(f2);

        //run test
        buildPath.deleteSource(f2);

        //check source added.
        assertEquals(1, buildPath.getSources().size());
        //check property changed event has sent
        assertNotNull(propertyChangeEvent);
        //check the event name is correct
        assertEquals(BuildPath.SOURCES, propertyChangeEvent.getPropertyName());
        //check old value is correct
        assertEquals(2, ((List<?>) propertyChangeEvent.getOldValue()).size());
        //check new value is correct
        assertEquals(1, ((List<?>) propertyChangeEvent.getNewValue()).size());

        //clear property change event
        propertyChangeEvent = null;
        //check can't add duplicate
        buildPath.deleteSource(f2);

        assertEquals(1, buildPath.getSources().size());
        assertNull(propertyChangeEvent);
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        propertyChangeEvent = evt;
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        propertyChangeEvent = null;
        buildPath.removePropertyChangeListener(this);
    }
}
