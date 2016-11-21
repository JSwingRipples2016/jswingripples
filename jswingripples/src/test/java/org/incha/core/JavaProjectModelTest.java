package org.incha.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import junit.framework.TestCase;

public class JavaProjectModelTest extends TestCase implements PropertyChangeListener {
    /**
     * Currently handled property change event.
     */
    private PropertyChangeEvent propertyChangeEvent;
    /**
     * Model to test.
     */
    private JavaProjectsModel model;

    /**
     * Default constructor.
     */
    public JavaProjectModelTest() {
        super();
    }
    /**
     * @param name the test case name.
     */
    public JavaProjectModelTest(final String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        model = new JavaProjectsModel();
        model.addPropertyChangeListener(this);
    }

    public void testaddProject() {
        final JavaProject p = new JavaProject("prgname");

        model.addProject(p);

        //check source added.
        assertEquals(1, model.getProjects().size());
        //check property changed event has sent
        assertNotNull(propertyChangeEvent);
        //check the event name is correct
        assertEquals(JavaProjectsModel.PROJECTS, propertyChangeEvent.getPropertyName());
        //check old value is cSOURCESorrect
        assertEquals(0, ((List<?>) propertyChangeEvent.getOldValue()).size());
        //check new value is correct
        assertEquals(1, ((List<?>) propertyChangeEvent.getNewValue()).size());

        //clear property change event
        propertyChangeEvent = null;
        //check can't add duplicate
        model.addProject(p);

        assertEquals(1, model.getProjects().size());
        assertNull(propertyChangeEvent);
    }
    public void testDeleteSource() {
        final JavaProject f1 = new JavaProject("name1");
        final JavaProject f2 = new JavaProject("name2");

        model.addProject(f1);
        model.addProject(f2);

        //run test
        model.deleteProject(f2);

        //check source added.
        assertEquals(1, model.getProjects().size());
        //check property changed event has sent
        assertNotNull(propertyChangeEvent);
        //check the event name is correct
        assertEquals(JavaProjectsModel.PROJECTS, propertyChangeEvent.getPropertyName());
        //check old value is correct
        assertEquals(2, ((List<?>) propertyChangeEvent.getOldValue()).size());
        //check new value is correct
        assertEquals(1, ((List<?>) propertyChangeEvent.getNewValue()).size());

        //clear property change event
        propertyChangeEvent = null;
        //check can't add duplicate
        model.deleteProject(f2);

        assertEquals(1, model.getProjects().size());
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
        model.removePropertyChangeListener(this);
    }
}
