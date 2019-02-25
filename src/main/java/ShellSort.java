import java.awt.Dimension;

public class ShellSort extends SortingPanel {

    public ShellSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        int h = 1;
        do
            h = 3 * h + 1;
        while (h < super.sortNumElem);
        do {
            h /= 3;
            for (int i = h; i < super.sortNumElem; i++) {
                int v = i;
                for (int j = i; j >= h && isLessThan(j, j - h); j -= h) {
                    if (super.killProcess)
                        return;
                    swap(j, j - h);
                }

            }

        } while (h > 1);
        setSortDone();
    }
}
