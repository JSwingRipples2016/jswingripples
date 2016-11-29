package org.incha.ui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.incha.core.JavaProject;

public class NewProjectWizard extends JPanel {
    private static final long serialVersionUID = 1738212977882284711L;

    /**
     * New java project.
     */
    private JTextField projectName = new JTextField(20);
    private CompleteListener listener;
    private JavaProject project;

    /**
     * Default constructor.
     */
    public NewProjectWizard() {
        super(new BorderLayout());

        //center panel
        final JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JLabel jLabel = new JLabel("Project Name: ",JLabel.LEFT);
        center.add(jLabel,BorderLayout.WEST);
        center.add(projectName,BorderLayout.EAST);

        add(center, BorderLayout.CENTER);

        //buttons panel
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //add 'ok' button
        final JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String text = projectName.getText();
                if (text != null && text.trim().length() > 0) {
                    project = new JavaProject(text);
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
                project = null;
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
        return project;
    }

    /**
     * @return java project.
     */
    public static JavaProject showDialog(final JFrame owner) {
        final JDialog dialog = new JDialog(owner, "Please enter project name", ModalityType.APPLICATION_MODAL);

        final NewProjectWizard wizard = new NewProjectWizard();
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
