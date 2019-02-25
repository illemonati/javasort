import java.awt.Color;
import java.awt.Dimension;

public class BubbleSort extends SortingPanel {

    public BubbleSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        boolean sorted = false;
        setSelectedColor(Color.yellow);
        setCompareColor(Color.yellow);
        for (int i = super.sortNumElem - 1; !sorted && i >= 0; i--) {
            sorted = true;
            for (int j = 0; j < i; j++) {
                if (super.killProcess)
                    return;
                if (isGreater(j, j + 1)) {
                    swap(j, j + 1);
                    sorted = false;
                }
            }

            setIsSorted(i);
        }

        setSortDone();
    }
}
