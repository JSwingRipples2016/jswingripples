package org.incha.ui.stats;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.incha.core.JavaProject;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.compiler.dom.JavaDomUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IPackageDeclaration;
import org.incha.ui.util.NullMonitor;

public class MainClassSearchDialog extends JDialog {
    private StartAnalysisDialog startAnalysisDialogCallback;
    private JList mainClassesListAdapter = new JList();
    private JButton okButton = new JButton();
    private JButton cancelButton = new JButton();
    private JScrollPane mainScrollPane = new JScrollPane();

    public MainClassSearchDialog(final StartAnalysisDialog callback, JavaProject project) throws IOException{
        startAnalysisDialogCallback = callback;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final Map<String, String> mainClassToFileName = findMainClasses(project);
        configureList(mainClassToFileName);
        configureOkButton(mainClassToFileName);
        configureCancelButton();
        configureLayout();
        mainScrollPane.setViewportView(mainClassesListAdapter);
    }
    
    private Map<String, String> findMainClasses(JavaProject project) throws IOException {
        String patternRegex = "void\\s*main\\s*\\(";
        Pattern pattern = Pattern.compile(patternRegex);
        Map<String, String> mainClassToFileName = new HashMap<>();
        try {
            final ICompilationUnit[] units = JavaDomUtils.getCompilationUnits(project, new NullMonitor());
            for (ICompilationUnit unit : units) {
                IPackageDeclaration[] packageDeclarations = unit.getPackageDeclarations();
                IType[] allTypes = unit.getAllTypes();
                for (int j = 0; j < packageDeclarations.length; j++){
                    Matcher matcher = pattern.matcher(allTypes[j].toString());
                    if (matcher.find()) {
                        mainClassToFileName.put(
                                packageDeclarations[j].getElementName() + "." + allTypes[j].getElementName(),
                                unit.getPath().toString().replaceAll("/", Matcher.quoteReplacement(File.separator)));
                    }
                }
            }
        } catch (JavaModelException ex) {
            Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainClassToFileName;
    }

    private void configureList(final Map<String, String> mainClassToFileName) {
        mainClassesListAdapter.setModel(
                new DefaultComboBoxModel(new ArrayList<Object>(mainClassToFileName.keySet()).toArray()));
        mainClassesListAdapter.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    okButton.setEnabled(true);
                } 
                if (evt.getClickCount() >= 2) {
                    try {
                        okActionPerformed(mainClassToFileName);
                    } catch (IOException ex) {
                        Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void okActionPerformed(Map<String, String> mainClassToFileName) throws IOException {
        dispose();
        if (mainClassesListAdapter.getSelectedIndex() != -1){
            String selectedItem = mainClassesListAdapter.getSelectedValue().toString();
            startAnalysisDialogCallback.setClassName(selectedItem, mainClassToFileName.get(selectedItem));
            startAnalysisDialogCallback.enableOkButton();
        }
    }

    private void configureOkButton(final Map<String, String> mainClassToFileName) {
        okButton.setText("Ok");
        okButton.setEnabled(false);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    okActionPerformed(mainClassToFileName);
                } catch (IOException ex) {
                    Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void configureCancelButton() {
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
    }

    private void configureLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainScrollPane)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(
                                        okButton,
                                        GroupLayout.PREFERRED_SIZE,
                                        97,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(
                                        cancelButton,
                                        GroupLayout.PREFERRED_SIZE,
                                        98,
                                        GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(
                                        mainScrollPane,
                                        GroupLayout.PREFERRED_SIZE,
                                        245,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(okButton)
                                        .addComponent(cancelButton))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
}