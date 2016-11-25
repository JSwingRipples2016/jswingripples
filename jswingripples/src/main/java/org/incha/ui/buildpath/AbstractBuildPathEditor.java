package org.incha.ui.buildpath;

import org.incha.core.JavaProject;
import org.incha.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

public abstract class AbstractBuildPathEditor extends JPanel {
    protected final JavaProject project;
    private final JButton add = new JButton();
    private final JButton delete = new JButton();

    private JTable files = new JTable(new FileTableModel());

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param project the java project.
     */
    public AbstractBuildPathEditor(final JavaProject project) {
        super(new BorderLayout(5, 0));
        this.project = project;

        add(createButtonsPanel(), BorderLayout.EAST);

        //configure files table
        files.setAutoCreateColumnsFromModel(true);
        files.setColumnSelectionAllowed(false);
        files.setShowHorizontalLines(false);
        files.setDragEnabled(false);
        files.setTableHeader(null);
        files.getColumnModel().getColumn(0).setCellRenderer(createCellRenderer());

        final JPanel filesPane = createFilesPanel();
        filesPane.setBorder(new CompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                new BevelBorder(BevelBorder.LOWERED)));
        add(filesPane, BorderLayout.CENTER);

        project.getBuildPath().addPropertyChangeListener(new PropertyChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                fireFilesChanged(
                        evt.getPropertyName(),
                        (List<File>) evt.getOldValue(), (List<File>) evt.getNewValue());
            }
        });

        //apply project to table
        synchronizeBuildPath();
    }

    /**
     * @return the cell renderer.
     */
    protected TableCellRenderer createCellRenderer() {
        return new DirectoryCellRenderer();
    }
    /**
     * Synchronized the project state with view.
     */
    protected abstract void synchronizeBuildPath();
    /**
     * @return buttons panel.
     */
    private JPanel createButtonsPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final JPanel container = new JPanel(new GridLayout(3, 1));
        panel.add(container);

        //'Add' button
        add.setText("Add");
        container.add(add);

        //'Delete' button
        delete.setText("Delete");
        container.add(delete);

        //add listeners
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addFile();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                deleteFile();
            }
        });

        return panel;
    }
    /**
     * @return the folder panel.
     */
    private JPanel createFilesPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        final JScrollPane sp = new JScrollPane(files);
        sp.getViewport().setBackground(Color.WHITE);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Deleted the file from build path.
     */
    protected void deleteFile() {
        final int row = files.getSelectedRow();
        if (row > -1) {
            final File file = (File) files.getModel().getValueAt(row, 0);
            deleteFileFromProject(file);
        }
    }
    /**
     * @param file the file to delete from project.
     */
    protected abstract void deleteFileFromProject(final File file);
    /**
     * @param f the file to add to project.
     */
    protected abstract void addFileToProject(final File f);

    /**
     * Adds the file to project.
     */
    protected void addFile() {
        //create file chooser
        final JFileChooser chooser = createFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File f = chooser.getSelectedFile();
            if (f != null) {
                addFileToProject(f);
            }
        }
    }

    /**
     * Use the file chooser to select a file
     * @return the file selected
     */
    public File selectFile(){
        //create file chooser
        final JFileChooser chooser = createFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        return null;
    }
    /**
     * @return the file chooser.
     */
    protected JFileChooser createFileChooser() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        return chooser;
    }
    /**
     * @param propertyName the property name.
     * @param oldFiles old list of files.
     * @param newFiles new list of files.
     */
    protected void fireFilesChanged(
            final String propertyName, final List<File>  oldFiles, final List<File> newFiles) {
        CollectionUtils.synchronize(oldFiles, newFiles, new CollectionUtils.SynchHandler<File>() {
            /* (non-Javadoc)
             * @see org.incha.utils.CollectionUtils.SynchHandler#itemAdded(java.lang.Object)
             */
            @Override
            public void itemAdded(final File item) {
                handleFileAdded(item);
            }
            /* (non-Javadoc)
             * @see org.incha.utils.CollectionUtils.SynchHandler#itemDeleted(java.lang.Object)
             */
            @Override
            public void itemDeleted(final File item) {
                handleFileDeleted(item);
            }
        });
    }

    /**
     * @param f the d deleted file.
     */
    protected void handleFileDeleted(final File f) {
        ((FileTableModel) files.getModel()).remove(f);
    }
    /**
     * @param f the added file.
     */
    protected void handleFileAdded(final File f) {
        ((FileTableModel) files.getModel()).add(f);
    }
}
