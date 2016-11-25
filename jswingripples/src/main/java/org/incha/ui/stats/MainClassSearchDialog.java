package org.incha.ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.incha.core.JavaProject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.compiler.dom.JavaDomUtils;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
import org.eclipse.jdt.core.IPackageDeclaration;

public class MainClassSearchDialog extends JDialog {
    JList list = null;
    private StartAnalysisDialog startAnalysisDialogCallback;
    JButton ok = new JButton();
    HashMap<String, String> hmap = new HashMap<String, String>();
    @Override
    public void setTitle(String title) {
        super.setTitle(title); 
        ok.setEnabled(false);
    }

    /**
     * Default constructor.
     */
    public MainClassSearchDialog(final StartAnalysisDialog callback, JavaProject project){

        startAnalysisDialogCallback = callback;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        //-----------------------
        final ArrayList<String> arr = new ArrayList<String>();
        String pattern = "void\\s*main\\s*\\(";
        Pattern r = Pattern.compile(pattern);
        try {
            JSwingRipplesEIG eig = new JSwingRipplesEIG(project);
            final ICompilationUnit[] units = JavaDomUtils.getCompilationUnitsWithoutMonitor(eig.getJavaProject());
            
            for (int i =0 ; i<units.length; i++){
                ICompilationUnit u = units[i];
                IPackageDeclaration[] P = u.getPackageDeclarations();
                IType[] T = u.getAllTypes();
                for (int j=0; j<P.length; j++){                    
                    
                    Matcher m = r.matcher(T[j].toString());
                    if (m.find()) {
                        arr.add(P[j].getElementName()+"."+T[j].getElementName());
                        String fileName = u.getPath().toString().replaceAll("/", Matcher.quoteReplacement(File.separator));
                        hmap.put(P[j].getElementName()+"."+T[j].getElementName(),fileName);
                        
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaModelException ex) {
            Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
        
        //-----------------------
        JScrollPane jScrollPane1 = new JScrollPane();
        list = new JList();
        
        JButton cancel = new JButton();
        
        list.setModel(new AbstractListModel() {
            Object[] strings = arr.toArray();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    ok.setEnabled(true);
                } 
                if (evt.getClickCount() == 2) {
                    try {
                        // Double-click detected
                        okActionPerformed(null);
                    } catch (IOException ex) {
                        Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        jScrollPane1.setViewportView(list);
        
        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    okActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        cancel.setText("Cancel");
        cancel.setToolTipText("");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok)
                    .addComponent(cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        
        
        
    }
    
    private void okActionPerformed(ActionEvent evt) throws IOException {                                   
        dispose();
        int index = list.getSelectedIndex();
        if (index!= -1){
            String selectedItem = list.getSelectedValue().toString();
            startAnalysisDialogCallback.setClassName(selectedItem,hmap.get(selectedItem));
        }
    }                                  

    private void cancelActionPerformed(ActionEvent evt) {                                       
        dispose();
    }        


}
