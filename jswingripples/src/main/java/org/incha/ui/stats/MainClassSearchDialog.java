package org.incha.ui.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.ModuleConfiguration;
import org.incha.core.Statistics;
import org.incha.ui.jripples.JRipplesDefaultModulesConstants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.compiler.dom.JavaDomUtils;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jdt.core.IPackageDeclaration;

public class MainClassSearchDialog extends JDialog {
    private static final long serialVersionUID = 6788138046337076311L;
    final String[] ArrMainClass = null;
    JList list = null;
    private StartAnalysisDialog startAnalysisDialogCallback;
    ArrayList<String> Filenames= new ArrayList<String>();
    final JButton ok = new JButton("Ok");

    @Override
    public void setTitle(String title) {
        super.setTitle(title); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Default constructor.
     */
    public MainClassSearchDialog(final StartAnalysisDialog callback, JavaProject project){//final Window owner, 
        //super(owner);
        startAnalysisDialogCallback = callback;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ok.setEnabled(false);
        final int SIZE = 3;
 
      Container c = getContentPane();
      c.setLayout( new BorderLayout(30,30 ) );
 
      Box box= Box.createVerticalBox();
      JLabel label1 = new JLabel("");
      JLabel label2 = new JLabel("");
        
        box.add(label1);
        box.add(new JLabel(" "));
        box.add(label2);
 
      

      
        
        ArrayList<String> arr = new ArrayList<String>();
        String pattern = "void\\s*main\\s*\\(";
        Pattern r = Pattern.compile(pattern);
        try {
            JSwingRipplesEIG eig = new JSwingRipplesEIG(project);
            final ICompilationUnit[] units = JavaDomUtils.getCompilationUnitsWithoutMonitor(eig.getJavaProject());
            
            Map<String,String> M = new HashMap<String,String>();
            for (int i =0 ; i<units.length; i++){
                ICompilationUnit u = units[i];
                IPackageDeclaration[] P = u.getPackageDeclarations();
                IType[] T = u.getAllTypes();
                for (int j=0; j<P.length; j++){                    
                    System.out.println(T[j].getElementName()+"--->"+P[j].getElementName()+"."+T[j].getElementName());
                    M.put(T[j].getElementName(),P[j].getElementName()+"."+T[j].getElementName());
                }
                
            }

             final IType types[] = JavaDomUtils.getAllTypes(units);
             for (int i = 0; i < types.length; i++) {
                System.out.println(types[i]);
                String declaration = types[i].toString();
                 

                // Create a Pattern object
                Matcher m = r.matcher(declaration);
                if (m.find( )) {
                    arr.add(M.get(types[i].getElementName()));
                }
                

             }
        } catch (IOException ex) {
            Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaModelException ex) {
            Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      
      
      
      
        // list
         Object a  = "";
         int n = arr.size();
         Object[] data = new Object[n+1];
         for (int i=0; i<n; i++){
             data[i] = arr.get(i);
         }
         data[n] = "";
        
        list = new JList(data); //data has type Object[]
        list.setVisibleRowCount(-1);
        list.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        list.setSize(new Dimension(500, 200));
        list.setPreferredSize(new Dimension(500, 200));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(500, 200));
        getContentPane().add(list);
        box.add(list);
        
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 1) {
                    ok.setEnabled(true);
                } 
                if (evt.getClickCount() == 2) {
                    try {
                        // Double-click detected
                        doOk();
                    } catch (IOException ex) {
                        Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        
        
        
        
        //south pane
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    doOk();
                } catch (IOException ex) {
                    Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        south.add(ok);

        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doCancel();
            }
        });
        south.add(cancel);
        box.add(south);
        //setPreferredSize(new Dimension(400,500));
        
        // create horizontal glue and add buttons to panel
      JPanel panel = new JPanel();
      panel.setLayout(
         new BoxLayout( panel, BoxLayout.Y_AXIS ) );
      
      
        
      c.add( box, BorderLayout.CENTER );
        
        
    }

   
    /**
     *
     */
    protected void doCancel() {
        dispose();
    }
    /**
     *
     */
    protected void doOk() throws IOException {
        dispose();
        int index = list.getSelectedIndex();
        if (index!= -1){
            String selectedItem = list.getSelectedValue().toString();
            startAnalysisDialogCallback.setClassName(selectedItem);//, Filenames.get(index));
        }
        
    }
}
