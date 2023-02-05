package inspection;

import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public class DemoAction extends AnAction {
    JSFunctionImpl element;

    public void setElement(JSFunctionImpl element) {
        this.element = element;
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Set the availability based on whether a project is open
        Project currentProject = event.getProject();
        event.getPresentation().setEnabledAndVisible(currentProject != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        if (!element.canNavigate()) {
            return;
        }
        // Using the event, create and show a dialog
        Project currentProject = event.getProject();

        // If an element is selected in the editor, add info about it.
        // Navigatable selectedElement = event.getData(CommonDataKeys.NAVIGATABLE);

        try {
            String extension = this.element.getContainingFile().getFileType().getDefaultExtension();
            Language language = this.element.getLanguage();
            String fileName = this.element.getName().concat("." + extension);
            String text = this.element.getText();

            VirtualFile file = ScratchRootType
                    .getInstance()
                    .createScratchFile(currentProject, fileName, language, text);

            FileEditorManager.getInstance(currentProject).openTextEditor(
                new OpenFileDescriptor(currentProject, file),
                true // request focus to editor
            );
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}