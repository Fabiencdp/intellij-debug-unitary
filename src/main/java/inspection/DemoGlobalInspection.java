package inspection;

import com.intellij.codeInspection.GlobalInspectionTool;

public class DemoGlobalInspection extends GlobalInspectionTool {

    public DemoGlobalInspectionVisitor runInspection() {
        return new DemoGlobalInspectionVisitor();
    }
}
