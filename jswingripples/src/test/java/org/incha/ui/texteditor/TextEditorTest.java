package org.incha.ui.texteditor;

import junit.framework.TestCase;
import org.eclipse.jdt.core.ISourceRange;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.incha.core.texteditor.FileOpen;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by hlib on 02.11.17.
 */
public class TextEditorTest extends TestCase {

    @Override
    protected void runTest() throws Throwable {
        // Do nothing if the precondition does not hold.
        if (!GraphicsEnvironment.isHeadless()) {
            super.runTest();
        }
    }

    public void testOpenFileWhenOpeningClass1Class2ThenClass1Again() throws Exception {
        /* given */
        final String stubFileName1 = "stub1.java";
        final String stubFileName2 = "stub2.java";
        FileOpen fileOpenStub1 = createFileOpenStub(stubFileName1);
        FileOpen fileOpenStub2 = createFileOpenStub(stubFileName2);

        FileOpen.FileOpenFactory fileOpenFactoryMock = mock(FileOpen.FileOpenFactory.class);
        when(fileOpenFactoryMock.create(stubFileName1)).thenReturn(fileOpenStub1);
        when(fileOpenFactoryMock.create(stubFileName2)).thenReturn(fileOpenStub2);

        JTabbedPane jTabbedPaneMock = spy(JTabbedPane.class);

        TextEditor.TextEditorElementsFactory textEditorElementsFactoryMock
                = mock(TextEditor.TextEditorElementsFactory.class);
        when(textEditorElementsFactoryMock.createJTabbedPane())
                .thenReturn(jTabbedPaneMock);

        TextEditor textEditor = new TextEditor(null,
                fileOpenFactoryMock, textEditorElementsFactoryMock);

        Runnable dummyRunnable = new Runnable(){
            @Override
            public void run() {}
        };

        ISourceRange sourceRange = mock(ISourceRange.class);
        when(sourceRange.getOffset()).thenReturn(5);

        /* when Class1*/
        textEditor.openFile(stubFileName1, sourceRange);
        //waiting for awt dispatch thread to finish execution
        SwingUtilities.invokeAndWait(dummyRunnable);

        /* then */
        List<FileOpen> openFiles = getOpenFiles(textEditor);
        assertEquals(1, openFiles.size());

        assertEquals(fileOpenStub1, openFiles.get(0));

        verify(jTabbedPaneMock).addTab(eq(stubFileName1), any(Component.class));

        assertEquals(0, jTabbedPaneMock.getSelectedIndex());

        /* when Class2*/
        textEditor.openFile(stubFileName2, sourceRange);
        //waiting for awt dispatch thread to finish execution
        SwingUtilities.invokeAndWait(dummyRunnable);

        /*then*/
        openFiles = getOpenFiles(textEditor);
        assertEquals(2, openFiles.size());

        assertEquals(fileOpenStub2, openFiles.get(1));

        verify(jTabbedPaneMock).addTab(eq(stubFileName2), any(Component.class));

        assertEquals(1, jTabbedPaneMock.getSelectedIndex());

        /* when Class1 again*/
        textEditor.openFile(stubFileName1, sourceRange);
        //waiting for awt dispatch thread to finish execution
        SwingUtilities.invokeAndWait(dummyRunnable);

        /*then*/
        openFiles = getOpenFiles(textEditor);
        assertEquals(2, openFiles.size());

        verify(jTabbedPaneMock, times(1)).addTab(eq(stubFileName1), any(Component.class));

        assertEquals(0, jTabbedPaneMock.getSelectedIndex());
    }

    @SuppressWarnings("unchecked")
    private List<FileOpen> getOpenFiles(TextEditor textEditor) throws NoSuchFieldException, IllegalAccessException {
        Field f = textEditor.getClass().getDeclaredField("openFiles");
        f.setAccessible(true);
        return (List<FileOpen>) f.get(textEditor);
    }

    private FileOpen createFileOpenStub(String path) {
        FileOpen fileOpenStub = mock(FileOpen.class);
        when(fileOpenStub.getPath()).thenReturn(path);
        RSyntaxTextArea area = new RSyntaxTextArea();
        area.setText("package" + path + ";");
        when(fileOpenStub.getText()).thenReturn(area);
        when(fileOpenStub.getFileName()).thenReturn(path);
        when(fileOpenStub.open()).thenReturn(true);

        return fileOpenStub;
    }

}