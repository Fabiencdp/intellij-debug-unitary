// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package inspection;

import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPlainText;
import com.intellij.psi.PsiPlainTextFile;
import kotlin.text.Regex;
import org.apache.xmlbeans.XmlToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Range.is;

//class Match {
//  private String value = ".*test.*";
//
//  private String match = ".*test.*";
//
//  private String text = "It work";
//
//  public Match(final String value) {
//    this.value = value;
//  }
//
//  public String getValue() { return this.value; }
//}

public class DemoInspectionVisitor extends PsiElementVisitor {

  String[] matches = { ".*test.*", ".*cool.*" };

  Pattern pattern = Pattern.compile("(.*test.*|.*cool.*)", Pattern.CASE_INSENSITIVE);

  @Override
  public void visitElement(@NotNull PsiElement element) {
//    System.out.println(pattern.pattern());
    PsiElement child = element.getFirstChild();
    PsiElement parent = element.getParent();
//    PsiElement type = element.getParent().getElementType().getDebugName();
    if (parent instanceof JSFunctionImpl) {
      String functionContent = parent.getText();
      System.out.println(parent.getText());
      Matcher m = pattern.matcher(child.getText());

      while (m.find()) {
        System.out.println("pater = " + m.pattern()) ;
        System.out.println("groupe = " + m.group()) ;

        Optional<String> result = Arrays.stream(this.matches).parallel()
                .filter(value -> value.equals(m.group())).findAny();

        System.out.println(result);
      }
    }

//    if (element.getContext() == "XmlToken.XML_DATA_CHARACTERS" && element.textMatches("test")) {
//      System.out.println("Match");
//      System.out.println(element);
//      System.out.println(element.getContext());
//      System.out.println(element.getText());
//      System.out.println("next");
//    }

    super.visitElement(element);
  }

  @Override
  public void visitPlainText(@NotNull PsiPlainText content) {
    System.out.println(content);
    super.visitElement(content);
  }


  @Override
  public void visitPlainTextFile(@NotNull PsiPlainTextFile file) {
    System.out.print("Plain Test");
    super.visitPlainTextFile(file);
  }
}
