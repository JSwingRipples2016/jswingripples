package org.incha.ui;

import org.incha.core.JavaProject;

/**
 * Created by rodrigo on 22-04-2016.
 */
public class ImportSource extends SourcesEditor {

	private static final long serialVersionUID = -9129106022897886891L;

    public ImportSource() {
        super(new JavaProject());
        this.addFile();
    }

    public JavaProject getProject() {
        return project;
    }

}
