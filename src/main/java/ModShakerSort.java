import java.awt.Dimension;

public class ModShakerSort extends SortingPanel {

    public ModShakerSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        boolean inOrder = false;
        boolean done = true;
        int lastDone = super.sortNumElem;
        int frontLine = -1;
        int lastSwapped = 0;
        do {
            done = true;
            for (int i = frontLine + 1; i < lastDone - 1; i++) {
                if (super.killProcess)
                    return;
                if (isGreater(i, i + 1)) {
                    swap(i, i + 1);
                    lastSwapped = i + 1;
                    done = false;
                }
            }

            for (int i = lastSwapped; i < lastDone; i++)
                setIsSorted(i);

            lastDone = lastSwapped;
            for (int i = lastDone - 1; i > frontLine + 1; i--) {
                if (super.killProcess)
                    return;
                if (isLessThan(i, i - 1)) {
                    swap(i, i - 1);
                    lastSwapped = i - 1;
                    done = false;
                }
            }

            for (int i = lastSwapped; i > frontLine; i--)
                setIsSorted(i);

            frontLine = lastSwapped;
            if (frontLine + 1 == lastDone - 1)
                done = true;
        } while (!done);
        setSortDone();
    }
}
