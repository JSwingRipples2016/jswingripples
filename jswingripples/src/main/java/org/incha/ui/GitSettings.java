package org.incha.ui;


import org.eclipse.jgit.api.errors.GitAPIException;
import org.incha.core.JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;


class GitSettings extends JPanel {

    private JTextField url = new JTextField(20);
    private JButton select = new JButton("Select");
    private SourcesEditor sourcesEditor;
    private final JFrame principalFrame;

    GitSettings(JavaProject project){
        sourcesEditor = new SourcesEditor(project);

        principalFrame = new JFrame("Clone From GitHub");
        principalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        principalFrame.getContentPane().setLayout(new BorderLayout(0, 3));

        JPanel view = fieldsPanel();
        principalFrame.getContentPane().add(view, BorderLayout.CENTER);

        //set frame location
        final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        principalFrame.setSize(2*size.width / 5, 2*size.height / 5);
        principalFrame.setLocationRelativeTo(JSwingRipplesApplication.getInstance());
        //show frame
        principalFrame.setVisible(true);
    }

    private JPanel fieldsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        //url
        final JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JLabel jLabel = new JLabel("URL Project GitHub: ",JLabel.LEFT);
        north.add(jLabel,BorderLayout.WEST);
        north.add(url,BorderLayout.EAST);
        panel.add(north, BorderLayout.NORTH);
        // dir
        final JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JLabel dirSelect = new JLabel("Select Directory: ",JLabel.LEFT);
        final JLabel selected = new JLabel("", JLabel.LEFT);
        center.add(dirSelect,BorderLayout.WEST);
        center.add(selected);
        final SelectedFileHolder holder = new SelectedFileHolder();
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = sourcesEditor.selectFile();
                if(f!=null) {
                    selected.setText(f.toString());
                    holder.file = f;
                }
            }
        });
        center.add(select,BorderLayout.EAST);
        panel.add(center, BorderLayout.CENTER);
        // ok button
        final JPanel okPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    handleOk(holder.file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                principalFrame.dispose();
            }
        });
        okPanel.add(ok);
        panel.add(okPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void handleOk(File selectedFile) throws IOException {
        String remoteUrl = url.getText();
        if (selectedFile == null || remoteUrl.equals("")) {
            JOptionPane.showMessageDialog(principalFrame, "You must enter an url and the path to clone the repository."
                    , "Inane error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // create file to clone
        File fileForRepository;
        try {
            fileForRepository = File.createTempFile("GiHubProject", "", selectedFile);
            if (!fileForRepository.delete()) {
                throw new IOException("Could not delete temporary file " + fileForRepository);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(principalFrame, "Problems creating file, please try again."
                    , "Inane error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // then clone
        try {
            Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(fileForRepository)
                    .call();
            sourcesEditor.addFileToProject(fileForRepository);
        }catch (org.eclipse.jgit.api.errors.TransportException e) {
            JOptionPane.showMessageDialog(principalFrame, "Connection error, please check internet.",
                    "Inane error", JOptionPane.ERROR_MESSAGE);
        } catch (org.eclipse.jgit.api.errors.InvalidRemoteException c){
            JOptionPane.showMessageDialog(principalFrame, "Incorrect url.", "Inane error", JOptionPane.ERROR_MESSAGE);
        }catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private class SelectedFileHolder{
        File file;
    }
}
