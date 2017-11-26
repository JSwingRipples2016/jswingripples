package org.incha.ui;

import org.incha.core.JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewProjectWizard extends JPanel {
    private static final long serialVersionUID = 1738212977882284711L;
    private boolean projectCreationApproved;

    /**
     * New java project.
     */
    private JTextField projectNameField;
    private CompleteListener listener;
    private JavaProject project;

    /**
     * Default constructor.
     * @param project
     */
    public NewProjectWizard(final JavaProject project) {
        super(new BorderLayout());
        this.projectCreationApproved = false;
        this.project = project;
        this.projectNameField = new JTextField(project.getName(), 20);

        //center panel
        final JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JLabel jLabel = new JLabel("Project Name: ",JLabel.LEFT);
        center.add(jLabel,BorderLayout.WEST);
        center.add(projectNameField, BorderLayout.EAST);

        add(center, BorderLayout.CENTER);

        //buttons panel
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //add 'ok' button
        final JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String text = projectNameField.getText();
                if (text != null && text.trim().length() > 0) {
                    NewProjectWizard.this.project.setProjectName(text);
                    projectCreationApproved = true;
                    handleCompleted();
                }
            }
        });
        south.add(ok);

        //add 'cancel' button
        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                NewProjectWizard.this.project = null;
                handleCompleted();
            }
        });

        south.add(cancel);
        add(south, BorderLayout.SOUTH);
    }
    /**
     * @param listener the listener to set
     */
    public void setListener(final CompleteListener listener) {
        this.listener = listener;
    }

    /**
     * Handles the wizard completed.
     */
    protected void handleCompleted() {
        if (listener != null) {
            listener.hasCompleted(this);
        }
    }
    /**
     * @return the project
     */
    public JavaProject getProject() {
        return projectCreationApproved ? project : null;
    }

    /**
     * @return java project.
     */
    public static JavaProject showDialog(final JFrame owner, final JavaProject project) {
        final JDialog dialog = new JDialog(owner, "Please enter project name", ModalityType.APPLICATION_MODAL);

        final NewProjectWizard wizard = new NewProjectWizard(project);
        wizard.setListener(new CompleteListener() {
            @Override
            public void hasCompleted(final Object obj) {
                dialog.dispose();
            }
        });
        dialog.setContentPane(wizard);
        dialog.pack();
        dialog.setLocationRelativeTo(owner.getContentPane());
        dialog.setVisible(true);

        return wizard.getProject();
    }
}
