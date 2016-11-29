package org.incha.core.texteditor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.incha.ui.texteditor.TextEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.io.*;
import java.util.HashMap;

/**
 * Created by rodrigo on 05-05-2016.
 */
public class FileOpen {
    private RSyntaxTextArea text;
    private String path;
    private String extension;
    private String content;
    private static HashMap< String , String > extensionMap = new HashMap<>();
    private boolean changed = false;
    private FileOpenDocListener doclistener; 

    /**
     * Constructor to the FileOpen.
     * @param absolutePath Path of the file.
     */
    public FileOpen( String absolutePath ) {
        path = absolutePath;
        text = new RSyntaxTextArea();        
    }

    /**
     * Read the file and copy the content in the RSyntaxTextArea text.
     * @return true if the file was read and false if not.
     */
    public boolean open() {
        try {
            BufferedReader reader = new BufferedReader( new FileReader( path ) );
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while ( line != null ) {
                sb.append( line + "\n" );
                line = reader.readLine();
            }
            
            reader.close(); // For performance reasons

            // See if we can set its highlighting
            if ( path.indexOf('.') != -1 ) {
                extension = path.substring( path.lastIndexOf('.') + 1 );
                try {
                    setSyntax( getSyntax( extension ) );
                }
                catch( Exception e ) {
                    e.printStackTrace();
                }
            }
            else{
                extension = "";
            }
            content = sb.toString();
            text.setText( content );
            doclistener = new FileOpenDocListener();
            text.getDocument().addDocumentListener(doclistener);
            return true;

        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Syntax Option.
     * @param extension the extension of the file.
     * @return the SyntaxConstants of the file.
     */
    private static String getSyntax( String extension ) {
        if( extensionMap.isEmpty() ) {
            extensionMap.put("c", SyntaxConstants.SYNTAX_STYLE_C);
            extensionMap.put("cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            extensionMap.put("cs", SyntaxConstants.SYNTAX_STYLE_CSHARP);
            extensionMap.put("html", SyntaxConstants.SYNTAX_STYLE_HTML);
            extensionMap.put("java", SyntaxConstants.SYNTAX_STYLE_JAVA);
            extensionMap.put("js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            extensionMap.put("php", SyntaxConstants.SYNTAX_STYLE_PHP);
            extensionMap.put("py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
            extensionMap.put("rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
            extensionMap.put("sql", SyntaxConstants.SYNTAX_STYLE_SQL);
            extensionMap.put("xml", SyntaxConstants.SYNTAX_STYLE_XML);
        }
        return extensionMap.get( extension );
    }

    /**
     * Closer to the File Open.
     * @param frame The TextEditor.
     */
    public void close( TextEditor frame ) {
    	if(changed){
    		int confirm = JOptionPane.showConfirmDialog(
    				frame ,
    				path + "\nhas been modified, would you like to save the changes?" ,
    				"Confirm" ,
    				JOptionPane.YES_NO_CANCEL_OPTION );
            if( confirm == JOptionPane.OK_OPTION ) {
                try {
                    save(); 
                    frame.closeSelectedTab();
                }
                catch (Exception e){

                }
            }
            else if (confirm == JOptionPane.NO_OPTION){
            	frame.closeSelectedTab();
            }
    	}
    	else {
    		frame.closeSelectedTab();
    	}
    	         
    }

    /**
     * Read all the text and Save the content in the Path of this File.
     * rewrite content to save the new initial state.
     */
    public void save() throws IOException {
        BufferedWriter output = null;
        output = new BufferedWriter( new FileWriter( path ) );
        output.write( text.getText() );
        output.close();
        content = text.getText();
    }

    /**
     * set the syntax to the text.
     * @param syntax the variable of the syntax.
     */
    public void setSyntax( String syntax ) {
        text.setSyntaxEditingStyle( syntax );
    }

    /**
     * getter.
     * @return the text in format RSyntaxTextArea,
     * have the syntax, and other propetries.
     */
    public RSyntaxTextArea getText() {
        return text;
    }

    /**
     * getter.
     * @return the path of the file.
     */
    public String getPath(){
        return path;
    }

    /**
     * getter.
     * @return the name of the file without extension.
     */
    public String getFileName(){
        return path.substring( path.lastIndexOf( "/" ) + 1 );
    }

    /**
     * Change the actual text for the initial text without close the file.
     */
    public void revertText(){
        text.setText(content);
    }
    
    
    private void setChange(){
    	changed = true;
    	// The listener isn't needed anymore
    	text.getDocument().removeDocumentListener(doclistener);
    }    
    
    private class FileOpenDocListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			setChange();
			
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setChange();
			
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// Plain text components don't fire these events.			
		}
    	
    }
}

