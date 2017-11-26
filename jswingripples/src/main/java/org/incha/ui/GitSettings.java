package org.incha.ui;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.incha.core.JavaProject;
import org.incha.utils.ProjectNamingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;


class GitSettings extends JPanel {

	private static final long serialVersionUID = 5644243718981468086L;
	private final JTextField url = new JTextField(20);
    private final JButton select = new JButton("Select");
    private final JButton cloneButton = new JButton("Clone");
    private final SourcesEditor sourcesEditor;
    private final JFrame principalFrame;
    private JavaProject project;
    private SourceLoadingListener sourceLoadingListener;

    @SuppressWarnings("deprecated")
    GitSettings(SourceLoadingListener sourceLoadingListener){
    	this.project = new JavaProject();
    	this.sourceLoadingListener = sourceLoadingListener;
        final ThreadHolder downloadThreadHolder = new ThreadHolder();
        sourcesEditor = new SourcesEditor(project);

        principalFrame = new JFrame("Clone From GitHub");
        principalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        principalFrame.getContentPane().setLayout(new BorderLayout(0, 3));

        JPanel view = fieldsPanel(downloadThreadHolder);
        principalFrame.getContentPane().add(view, BorderLayout.CENTER);

        //set frame location
        final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        principalFrame.setSize(2*size.width / 5, 2*size.height / 5);
        principalFrame.setLocationRelativeTo(JSwingRipplesApplication.getInstance());
        //show frame
        principalFrame.setVisible(true);
        addWindowListener(downloadThreadHolder);

    }

    private JPanel fieldsPanel(final ThreadHolder downloadThreadHolder) {
        JPanel panel = new JPanel(new BorderLayout());
        final JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JLabel jLabel = new JLabel("URL Project GitHub: ",JLabel.LEFT);
        north.add(jLabel,BorderLayout.WEST);
        north.add(url,BorderLayout.EAST);
        panel.add(north, BorderLayout.NORTH);
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
        final JPanel okPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cloneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    handleOk(holder.file, principalFrame, downloadThreadHolder);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        okPanel.add(cloneButton);
        panel.add(okPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void handleOk(File selectedFile, final JFrame contextFrame, ThreadHolder downloadThreadHolder) throws IOException {
        final String remoteUrl = url.getText();
        if (selectedFile == null || remoteUrl.equals("")) {
            JOptionPane.showMessageDialog(principalFrame, "You must enter an url and the path to clone the repository."
                    , "Inane error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // create file to clone
        final File fileForRepository;
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
        startDownloadThread(remoteUrl, fileForRepository, contextFrame, downloadThreadHolder);
    }

    private void startDownloadThread(final String remoteUrl,
                                     final File fileForRepository,
                                     final JFrame contextFrame,
                                     ThreadHolder downloadThreadHolder) {
        downloadThreadHolder.downloadThread = new Thread() {
            @Override
            public void run() {
                cloneButton.setText("In progress...");
                cloneButton.setEnabled(false);
                try {
                    Git.cloneRepository()
                            .setURI(remoteUrl)
                            .setDirectory(fileForRepository)
                            .call();
                    String gitProjectName = ProjectNamingUtils.getGitProjectNameFromUrl(remoteUrl);
                    project.setProjectName(gitProjectName);
                    sourceLoadingListener.onSourcesLoaded(project);
                } catch (org.eclipse.jgit.api.errors.TransportException e) {
                    JOptionPane.showMessageDialog(principalFrame, "Connection error, please check internet.",
                            "Inane error", JOptionPane.ERROR_MESSAGE);
                } catch (org.eclipse.jgit.api.errors.InvalidRemoteException c){
                    JOptionPane.showMessageDialog(principalFrame, "Incorrect url.", "Inane error", JOptionPane.ERROR_MESSAGE);
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }
                sourcesEditor.addFileToProject(fileForRepository);
                project.getGHRepo().replaceRepository(GitHubSettings.formatURLRepo(remoteUrl));
                contextFrame.dispose();
            }
        };
        downloadThreadHolder.downloadThread.start();
    }

    private void addWindowListener(final ThreadHolder downloadThreadHolder) {
        principalFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String possibleChoices[] = {"Interrupt", "No"};
                int interruptDownloadChoice = JOptionPane.NO_OPTION;
                if (downloadThreadHolder.downloadThread != null
                    && downloadThreadHolder.downloadThread.isAlive()
                    && !downloadThreadHolder.downloadThread.isInterrupted()) {
                    interruptDownloadChoice = JOptionPane.showOptionDialog(
                            null,
                            "A download is in progress.\nAre you sure you want to exit?",
                            "Exit?",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            possibleChoices,
                            possibleChoices[1]);
                }
                if (interruptDownloadChoice == JOptionPane.YES_OPTION) {
                    downloadThreadHolder.downloadThread.stop();
                }
                principalFrame.dispose();
            }
        });
    }

    private class SelectedFileHolder{
        File file;
    }

    private class ThreadHolder {
        Thread downloadThread;
    }
}
