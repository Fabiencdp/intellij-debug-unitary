package inspection;

import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.apache.pdfbox.io.ScratchFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DemoAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Set the availability based on whether a project is open
        Project currentProject = event.getProject();
        event.getPresentation().setEnabledAndVisible(currentProject != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // Using the event, create and show a dialog
        Project currentProject = event.getProject();

        StringBuilder message =
                new StringBuilder(event.getPresentation().getText() + " Selected!");
        // If an element is selected in the editor, add info about it.
        Navigatable selectedElement = event.getData(CommonDataKeys.NAVIGATABLE);
        if (selectedElement != null && selectedElement.canNavigate()) {
            try {
                String extension = ((JSFunctionImpl) selectedElement).getContainingFile().getFileType().getDefaultExtension();
                Language language = ((JSFunctionImpl) selectedElement).getLanguage();
                String fileName = ((JSFunctionImpl<?>) selectedElement).getName().concat("." + extension);
                String text = ((JSFunctionImpl<?>) selectedElement).getText();
                ScratchRootType.getInstance().createScratchFile(currentProject, fileName, language, text);

//                String directory = event.getData(CommonDataKeys.VIRTUAL_FILE).getParent().getPath();
////                Writer myWriter = Files.newBufferedWriter(Paths.get(directory, "test-2.js"), Charset.defaultCharset());
//                Writer myWriter = Files.newBufferedWriter(Paths.get("/home/fabien/www/OTHERS/jetbrains/intellij-debug-unitary/build/idea-sandbox/config/scratches/test.js"), Charset.defaultCharset());
//
//                myWriter.write(((JSFunctionImpl<?>) selectedElement).getText());
//
//                myWriter.close();
            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            message.append("\nSelected Element: ").append(selectedElement);
        }


        String title = event.getPresentation().getDescription();
//        Messages.showMessageDialog(
//                currentProject,
//                message.toString(),
//                title,
//                Messages.getInformationIcon());
    }
}