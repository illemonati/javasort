import java.awt.Color;
import java.awt.Dimension;

public class RandomQuickSort extends SortingPanel {

    public RandomQuickSort(ToolBar toolBar, Dimension parentPanelSize) {
        super(toolBar, parentPanelSize);
    }

    public void runSort() {
        setSelectedColor(Color.yellow);
        setCompareColor(Color.yellow);
        doQuickSort(0, super.sortNumElem - 1, 0);
        setSortDone();
    }

    public void doQuickSort(int low, int high, int threshold) {
        if (super.killProcess)
            return;
        if (high - low > threshold) {
            int pivot = partition(low, high);
            doQuickSort(low, pivot - 1, threshold);
            for (int i = low; i <= pivot; i++)
                setIsSorted(i);

            doQuickSort(pivot + 1, high, threshold);
            for (int i = pivot; i <= high; i++)
                setIsSorted(i);

        }
    }

    public int partition(int low, int high) {
        int pivotpos = low + Math.abs(super.toolBar.randInt(high - low));
        swap(low, pivotpos);
        pivotpos = low;
        int pivot = pivotpos;
        for (int i = low + 1; i <= high; i++) {
            if (super.killProcess)
                return -1;
            if (isLessThan(i, pivot)) {
                pivotpos++;
                if (pivot == pivotpos)
                    pivot = i;
                else if (pivot == i)
                    pivot = pivotpos;
                if (pivotpos != i)
                    swap(pivotpos, i);
            }
        }

        if (low != pivotpos)
            swap(low, pivotpos);
        return pivotpos;
    }
}
