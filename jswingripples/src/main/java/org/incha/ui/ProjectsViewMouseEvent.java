package org.incha.ui;

public class ProjectsViewMouseEvent {
    /**
     * The left mouse pressed action ID.
     */
    public static final int LEFT_MOUSE_PRESSED = 0;
    /**
     * The mouse clicked action ID.
     */
    public static final int MOUSE_CLICKED = 1;

    private final Object[] path;
    private final int type;
    private final int x;
    private final int y;

    /**
     * Default constructor.
     */
    public ProjectsViewMouseEvent(final Object[] path, final int type, final int x, final int y) {
        super();
        this.path = path;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the path
     */
    public Object[] getPath() {
        return path;
    }
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }
    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
}
