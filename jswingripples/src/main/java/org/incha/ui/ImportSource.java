package org.incha.ui;

import org.incha.core.JavaProject;

/**
 * Created by rodrigo on 22-04-2016.
 */
public class ImportSource extends SourcesEditor {
    /**
     * @param project the java project.
     */
    public ImportSource(JavaProject project) {
        super(project);
        this.addFile();
    }

}
