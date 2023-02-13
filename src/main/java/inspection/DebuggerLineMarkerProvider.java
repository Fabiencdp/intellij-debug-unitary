package inspection;

import com.google.common.io.Resources;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class DebuggerLineMarkerProvider implements LineMarkerProvider {
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
//        if (element instanceof PsiIdentifier &&
//                (parent = element.getParent()) instanceof PsiMethod &&
//                ((PsiMethod) parent).getMethodIdentifier() == element)
//            ) { // aha, we are at method name
//            return new LineMarkerInfo(element, element.getTextRange(), icon, null,null, alignment);
//        }

        if (
            !(element instanceof LeafPsiElement) ||
            ((LeafPsiElement) element).getElementType() != JSTokenTypes.FUNCTION_KEYWORD ||
            !((LeafPsiElement) element).canNavigate()
        )
        {
            return null;
        }

        System.out.println(element.getClass().getName());

        return new RunLineMarkerInfo(
                (LeafPsiElement) element,
                AllIcons.Actions.Execute,
                createActionGroup(element),
                createNavigationHandler(element),
                createToolTipProvider(element)
        );
    }

    @Contract(pure = true)
    private Function createToolTipProvider(PsiElement element) {
        return (Function) element1 -> "Tooltip calculated at ";
    }

    public DefaultActionGroup createActionGroup(PsiElement element) {
        DefaultActionGroup group = new DefaultActionGroup();
        // group.addAction("dq");
        return group;
    }

    private GutterIconNavigationHandler<PsiElement> createNavigationHandler(PsiElement element) {
        return new GutterIconNavigationHandler<PsiElement>() {
            @Override
            public void navigate(MouseEvent event, PsiElement element) {
                Project currentProject = element.getProject();

                String extension = element.getContainingFile().getFileType().getDefaultExtension();
                String fileName = ((JSFunctionImpl) element).getName().concat("." + extension);

                VirtualFile file = ScratchRootType
                        .getInstance()
                        .createScratchFile(currentProject, fileName, element.getLanguage(), element.getText());

                FileEditorManager.getInstance(currentProject).openTextEditor(
                        new OpenFileDescriptor(currentProject, file),
                        true // request focus to editor
                );
            }
        };
    }
}


/**
 * RunLineMarkerInfo
 */
class RunLineMarkerInfo extends LineMarkerInfo {
    private final ActionGroup actionGroup;

    private final LeafPsiElement element;

    public RunLineMarkerInfo(
            @NotNull LeafPsiElement element,
            @NotNull Icon icon,
            @NotNull ActionGroup actionGroup,
            @NotNull GutterIconNavigationHandler navHandler,
            @Nullable Function<LeafPsiElement, String> tooltipProvider
    ) {
        super(element.getParent(), element.getParent().getTextRange(), icon, tooltipProvider, navHandler, GutterIconRenderer.Alignment.RIGHT);
        this.actionGroup = actionGroup;
        this.element = element;
    }

    @Override
    public GutterIconRenderer createGutterRenderer() {
        return new LineMarkerGutterIconRenderer<>(this) {
            // @Override
            // public AnAction getClickAction() {
            //     DemoAction action = new DemoAction();
            //     action.setElement((JSFunctionImpl) (element).getParent());
            //     return action;
            // }

            @Override
            public boolean isNavigateAction() {
                return super.isNavigateAction();
            }

            @Override
            public @Nullable ActionGroup getPopupMenuActions() {
                return actionGroup;
            }
        };
    }
}