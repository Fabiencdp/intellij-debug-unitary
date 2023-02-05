package inspection;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DebuggerLineMarkerProvider implements LineMarkerProvider {
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (
                !(element instanceof LeafPsiElement) ||
                ((LeafPsiElement) element).getElementType() != JSTokenTypes.FUNCTION_KEYWORD ||
                !((LeafPsiElement) element).canNavigate()
        )
        {
            return null;
        }


        GutterIconNavigationHandler<PsiElement> navHandler = new GutterIconNavigationHandler<PsiElement>() {
            @Override
            public void navigate(MouseEvent e, PsiElement elt) {
                System.out.println("don't click on me");
            }
        };


        return new RunLineMarkerInfo(
                (LeafPsiElement) element,
                AllIcons.Actions.Execute,
                createActionGroup(element),
                navHandler,
                createToolTipProvider(element)
        );
    }

    @Contract(pure = true)
    private Function createToolTipProvider(PsiElement element) {
        return (Function) element1 -> "Tooltip calculated at ";
    }

    public DefaultActionGroup createActionGroup(PsiElement element) {
        DefaultActionGroup group = new DefaultActionGroup();
//        group.addAction("dq");
        return group;
    }
}

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
        // TODO: test navHandler
        super(element.getParent(), element.getParent().getTextRange(), icon, tooltipProvider, null, GutterIconRenderer.Alignment.RIGHT);
        this.actionGroup = actionGroup;
        this.element = element;
    }

    @Override
    public GutterIconRenderer createGutterRenderer() {
        return new LineMarkerGutterIconRenderer<>(this) {
            @Override
            public AnAction getClickAction() {
                DemoAction action = new DemoAction();
                action.setElement((JSFunctionImpl) (element).getParent());
                return action;
            }

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