package org.incha.ui;

import java.io.File;
import java.util.List;

import org.incha.core.BuildPath;
import org.incha.core.JavaProject;
import org.incha.ui.buildpath.AbstractBuildPathEditor;

public class SourcesEditor extends AbstractBuildPathEditor {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param project the java project.
     */
    public SourcesEditor(final JavaProject project) {
        super(project);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.AbstractBuildPathEditor#fireFilesChanged(java.lang.String, java.util.List, java.util.List)
     */
    @Override
    protected void fireFilesChanged(final String propertyName,
            final List<File> originOldFiles, final List<File> originNewFiles) {
        if (!BuildPath.SOURCES.equals(propertyName)) {
            return;
        }
        super.fireFilesChanged(propertyName, originOldFiles, originNewFiles);
    }

    /**
     * Synchronizes the view with project.
     */
    @Override
    protected void synchronizeBuildPath() {
        //synchronize source files.
        for (final File f : project.getBuildPath().getSources()) {
            handleFileAdded(f);
        }
    }
    /**
     * @param file the file to delete from project.
     */
    @Override
    protected void deleteFileFromProject(final File file) {
        project.getBuildPath().deleteSource(file);
    }
    /**
     * @param f the file to add to project.
     */
    @Override
    protected void addFileToProject(final File f) {
        project.getBuildPath().addSource(f);
    }
}
