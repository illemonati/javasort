import java.awt.Dimension;

public class SelectionSort extends SortingPanel {

    public SelectionSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        int least = 0;
        for (int i = 0; i < super.sortNumElem - 1; i++) {
            least = i;
            for (int j = i; j < super.sortNumElem; j++) {
                if (super.killProcess)
                    return;
                if (!isLessThan(least, j))
                    least = j;
            }

            if (i != least)
                swap(least, i);
            setIsSorted(i);
        }

        setSortDone();
    }
}
