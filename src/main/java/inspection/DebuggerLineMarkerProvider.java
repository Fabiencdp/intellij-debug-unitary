package inspection;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DebuggerLineMarkerProvider implements LineMarkerProvider {
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof LeafPsiElement) || ((LeafPsiElement) element).getElementType() != JSTokenTypes.FUNCTION_KEYWORD){
            return null;
        }

        return new LineMarkerInfo(element, element.getTextRange(), AllIcons.Actions.Execute, null,null, GutterIconRenderer.Alignment.RIGHT);
    }


//    public DefaultActionGroup createActionGroup(@NotNull PsiElement element) {
////        val linkDestinationElement =
////                findChildElement(element, MarkdownTokenTypeSets.LINK_DESTINATION, null)
////        val linkDestination = linkDestinationElement?.text
//        ActionGroup group = new DefaultActionGroup();
////        group.actionPerformed();
////        group.add(ActionManager.getInstance().getAction("MyPlugin.OpenToolWindowAction"));
//        return group;
//    }
}