package org.incha.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.JavaProjectsModel;
import org.incha.core.JavaProject;
import org.incha.core.ModelSerializer;
import org.incha.utils.CollectionUtils;

public class ModelSaver implements PropertyChangeListener {
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(ModelSaver.class);
    /**
     * Model.
     */
    private final JavaProjectsModel model;
    /**
     * File to save model.
     */
    private final File file;

    private PropertyChangeListener buildPathLitener = new PropertyChangeListener() {
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            save();
        }
    };

    /**
     * @param model model to save.
     * @param f target file.
     */
    public ModelSaver(final JavaProjectsModel model, final File f) {
        super();
        this.model = model;
        this.file = f;
        model.addPropertyChangeListener(this);

        //synchronize state
        for (final JavaProject p : model.getProjects()) {
            p.getBuildPath().addPropertyChangeListener(buildPathLitener);
        }
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        CollectionUtils.synchronize(
            (List<JavaProject>) evt.getOldValue(),
            (List<JavaProject>) evt.getNewValue(),
            new CollectionUtils.SynchHandler<JavaProject>() {
                /* (non-Javadoc)
                 * @see org.incha.utils.CollectionUtils.SynchHandler#itemAdded(java.lang.Object)
                 */
                @Override
                public void itemAdded(final JavaProject item) {
                    projectAdded(item);
                }
                /* (non-Javadoc)
                 * @see org.incha.utils.CollectionUtils.SynchHandler#itemDeleted(java.lang.Object)
                 */
                @Override
                public void itemDeleted(final JavaProject item) {
                    projectDeleted(item);
                }
            });
    }

    /**
     * @param item
     */
    protected void projectDeleted(final JavaProject item) {
        item.getBuildPath().removePropertyChangeListener(buildPathLitener);
        save();
    }
    /**
     * @param item
     */
    protected void projectAdded(final JavaProject item) {
        item.getBuildPath().addPropertyChangeListener(buildPathLitener);
        save();
    }

    /**
     * Starts save application thread.
     */
    private  void save() {
        log.info("Save application");
        new Thread("Save application thread") {
            @Override
            public void run() {
                doSave();
            };
        }.start();

    }
    /**
     * Saves the application.
     */
    public synchronized void doSave() {
        try {
            final Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            try {
                new ModelSerializer().save(model, wr);
            } finally {
                wr.close();
            }
        } catch (final Exception e) {
            log.error("Failed to save model", e);
        }
    }
}
