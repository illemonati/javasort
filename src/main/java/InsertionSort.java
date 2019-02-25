import java.awt.Color;
import java.awt.Dimension;

public class InsertionSort extends SortingPanel {

    public InsertionSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        boolean inOrder = false;
        setSelectedColor(Color.yellow);
        setCompareColor(null);
        for (int i = 1; i < super.sortNumElem; i++) {
            setIsSorted(0);
            inOrder = false;
            for (int j = i; j > 0 && !inOrder; j--) {
                if (super.killProcess)
                    return;
                if (isLessThan(j, j - 1)) {
                    swap(j, j - 1);
                } else {
                    setIsSorted(j);
                    inOrder = true;
                }
            }

        }

        setSortDone();
    }
}
