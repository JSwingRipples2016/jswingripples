package org.incha.ui.stats;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.ui.JSwingRipplesApplication;

public class ShowCurrentStateAction implements ActionListener {
    /**
     * Default constructor.
     */
    public ShowCurrentStateAction() {
        super();
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JavaProjectsModel model = JavaProjectsModel.getInstance();
        final List<JavaProject> projects = model.getProjects();

        final StatsDialog dialog = new StatsDialog(projects.toArray(new JavaProject[projects.size()]));
        dialog.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
        dialog.pack();
        dialog.setLocationRelativeTo(JSwingRipplesApplication.getInstance());
        dialog.setVisible(true);
    }
}
