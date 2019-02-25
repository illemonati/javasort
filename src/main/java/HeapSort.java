import java.awt.Color;
import java.awt.Dimension;

public class HeapSort extends SortingPanel {

    public HeapSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        heapSize = super.sortNumElem;
        setSelectedColor(Color.orange);
        setCompareColor(Color.orange);
        for (int i = heapSize / 2; i >= 0; i--) {
            if (super.killProcess)
                return;
            heapify(i);
        }

        for (int i = super.sortNumElem - 1; i >= 1; i--) {
            if (super.killProcess)
                return;
            swap(i, 0);
            setIsSorted(heapSize - 1);
            heapSize--;
            heapify(0);
        }

        setSortDone();
    }

    public void heapify(int i) {
        if (super.killProcess)
            return;
        int left = 2 * i;
        int right = 2 * i + 1;
        int largest;
        if (left < heapSize && left != i && isGreater(left, i))
            largest = left;
        else
            largest = i;
        if (right < heapSize && right != largest && isGreater(right, largest))
            largest = right;
        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }

    private int heapSize;
}
