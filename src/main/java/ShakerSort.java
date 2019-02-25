import java.awt.Dimension;

public class ShakerSort extends SortingPanel {

    public ShakerSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        boolean inOrder = false;
        boolean done = true;
        int lastDone = super.sortNumElem;
        int frontLine = -1;
        int lastSwapped = 0;
        for (int i = 0; i < super.sortNumElem / 2; i++) {
            for (int j = i; j < super.sortNumElem - i - 1; j++) {
                if (super.killProcess)
                    return;
                if (isGreater(j, j + 1))
                    swap(j, j + 1);
            }

            setIsSorted(super.sortNumElem - i - 1);
            for (int j = super.sortNumElem - i - 2; j > i; j--) {
                if (super.killProcess)
                    return;
                if (isLessThan(j, j - 1))
                    swap(j, j - 1);
            }

            setIsSorted(i);
        }

        setSortDone();
    }
}
