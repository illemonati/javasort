import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class SortingPanel extends JPanel
        implements Runnable {
    protected abstract class AbstractJPanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

        protected AbstractJPanel() {
        }
    }

    protected class Element {

        public void translate(int dx) {
            xCoord += dx;
            xCoordDot += dx;
        }

        public void setSize(int size) {
            this.size = size;
            barHeight = (int) (((panelSize.getHeight() - (double) SortingPanel.MIN_BAR_HEIGHT - (double) SortingPanel.PANEL_MARGIN_TOP - (double) SortingPanel.PANEL_MARGIN_BOTTOM) / (double) totalElements) * (double) size) + SortingPanel.MIN_BAR_HEIGHT;
            yCoord = panelSize.height - barHeight - SortingPanel.PANEL_MARGIN_BOTTOM;
        }

        public void setID(int id) {
            this.id = id;
            xCoord = (int) (((panelSize.getWidth() - (double) SortingPanel.PANEL_MARGIN_LEFT - (double) SortingPanel.PANEL_MARGIN_RIGHT) / (double) totalElements) * (double) id) + SortingPanel.PANEL_MARGIN_LEFT;
            resetBarWidth();
            xCoordDot = xCoord + barWidth / 2;
        }

        public void resetBarWidth() {
            barWidth = ((int) (((panelSize.getWidth() - (double) SortingPanel.PANEL_MARGIN_LEFT - (double) SortingPanel.PANEL_MARGIN_RIGHT) / (double) totalElements) * (double) (id + 1)) + SortingPanel.PANEL_MARGIN_LEFT) - xCoord;
            if (barWidth <= 0)
                barWidth = 1;
        }

        public void setColor(Color c) {
            if (c != null)
                color = c;
        }

        public void resetColor() {
            isSelected = isSorted;
            if (isSorted)
                color = sortedColor;
            else
                color = nonSelectedColor;
        }

        public void setSorted(boolean isSorted) {
            this.isSorted = isSorted;
            if (isSorted)
                color = sortedColor;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public void drawDot(Graphics g) {
            g.setColor(dotColor);
            g.fillRect(xCoordDot, yCoord, SortingPanel.DOT_SIZE, SortingPanel.DOT_SIZE);
        }

        public void drawBar(Graphics g) {
            if (barWidth == 1) {
                if (isSelected)
                    g.setColor(color);
                else
                    g.setColor(borderColor);
                g.drawLine(xCoord, yCoord, xCoord, yCoord + barHeight);
            } else if (barWidth == 2) {
                g.setColor(color);
                g.drawLine(xCoord + 1, yCoord, xCoord + 1, yCoord + barHeight);
                g.setColor(borderColor);
                g.drawRect(xCoord, yCoord, 3, barHeight);
            } else {
                g.setColor(color);
                g.fillRect(xCoord + 1, yCoord, barWidth, barHeight);
                g.setColor(borderColor);
                g.drawRect(xCoord, yCoord, barWidth, barHeight);
            }
        }

        public int id;
        public int size;
        public int totalElements;
        public int xCoord;
        public int yCoord;
        public int xCoordDot;
        public int barHeight;
        public int barWidth;
        public Color color;
        public boolean isSelected;
        public boolean isSorted;

        public Element(int id, int size, Color c) {
            this.size = size;
            color = c;
            totalElements = numElements;
            isSelected = false;
            isSorted = false;
            setID(id);
            barHeight = (int) (((panelSize.getHeight() - (double) SortingPanel.MIN_BAR_HEIGHT - (double) SortingPanel.PANEL_MARGIN_TOP - (double) SortingPanel.PANEL_MARGIN_BOTTOM) / (double) totalElements) * (double) size) + SortingPanel.MIN_BAR_HEIGHT;
            yCoord = panelSize.height - barHeight - SortingPanel.PANEL_MARGIN_BOTTOM;
        }
    }


    public SortingPanel() {
        sortedColor = Color.blue.brighter();
        selectedColor = Color.orange;
        compareColor = Color.yellow;
        nonSelectedColor = Color.green;
        borderColor = Color.black;
        dotColor = Color.black;
        graphDotColor = Color.black;
        graphAxisColor = Color.black;
        fullyInitialized = false;
        graphIsInitialized = false;
    }

    public SortingPanel(ToolBar toolBar, Dimension parentPanelSize) {
        sortedColor = Color.blue.brighter();
        selectedColor = Color.orange;
        compareColor = Color.yellow;
        nonSelectedColor = Color.green;
        borderColor = Color.black;
        dotColor = Color.black;
        graphDotColor = Color.black;
        graphAxisColor = Color.black;
        fullyInitialized = false;
        graphIsInitialized = false;
        this.toolBar = toolBar;
        numElements = toolBar.getNumElements();
        beginList = toolBar.getBeginSortType();
        action = toolBar.getAction();
        showDots = toolBar.showDots();
        showBars = toolBar.showBars();
        showSwaps = toolBar.showSwaps();
        showCompares = toolBar.showCompares();
        thisPanelSize = parentPanelSize;
        panelSize = new Dimension((int) thisPanelSize.getWidth(), (int) thisPanelSize.getHeight() / 2);
        killProcess = false;
        setLayout(new BoxLayout(this, 1));
        if (action == ToolBar.ACTION_SORT) {
            dotsPanel = new AbstractJPanel() {

                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawDots(g);
                }

            };
            dotsPanel.setMinimumSize(panelSize);
            dotsPanel.setPreferredSize(panelSize);
            dotsPanel.setMaximumSize(panelSize);
            dotsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
            add(dotsPanel);
            barsPanel = new AbstractJPanel() {

                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawBars(g);
                }

            };
            barsPanel.setMinimumSize(panelSize);
            barsPanel.setPreferredSize(panelSize);
            barsPanel.setMaximumSize(panelSize);
            barsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
            add(barsPanel);
        } else if (action == ToolBar.ACTION_E_TEST) {
            swapsGraph = new AbstractJPanel() {

                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawSwaps(g);
                }

            };
            swapsGraph.setMinimumSize(panelSize);
            swapsGraph.setPreferredSize(panelSize);
            swapsGraph.setMaximumSize(panelSize);
            swapsGraph.setBorder(BorderFactory.createLoweredBevelBorder());
            add(swapsGraph);
            comparesGraph = new AbstractJPanel() {

                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawCompares(g);
                }

            };
            comparesGraph.setMinimumSize(panelSize);
            comparesGraph.setPreferredSize(panelSize);
            comparesGraph.setMaximumSize(panelSize);
            comparesGraph.setBorder(BorderFactory.createLoweredBevelBorder());
            add(comparesGraph);
        }
    }

    protected void makeElementArray() {
        elementArray = new Element[sortNumElem];
        if (beginList == ToolBar.BEGIN_RANDOM) {
            Random gen = new Random();
            int ints[] = new int[sortNumElem];
            for (int i = 0; i < sortNumElem; i++)
                ints[i] = i;

            for (int i = 0; i < sortNumElem; i++) {
                int rand1 = Math.abs(toolBar.randInt(sortNumElem));
                int temp = ints[i];
                ints[i] = ints[rand1];
                ints[rand1] = temp;
            }

            for (int i = 0; (double) i < 0.5D * (double) sortNumElem; i++) {
                int rand1 = Math.abs(toolBar.randInt(sortNumElem));
                int rand2 = Math.abs(toolBar.randInt(sortNumElem));
                int temp = ints[rand1];
                ints[rand1] = ints[rand2];
                ints[rand2] = temp;
            }

            for (int i = 0; i < sortNumElem; i++)
                elementArray[i] = new Element(i, ints[i], nonSelectedColor);

        } else if (beginList == ToolBar.BEGIN_SORTED) {
            for (int i = 0; i < sortNumElem; i++)
                elementArray[i] = new Element(i, i, nonSelectedColor);

        } else if (beginList == ToolBar.BEGIN_REVERSED) {
            for (int i = 0; i < sortNumElem; i++)
                elementArray[i] = new Element(i, sortNumElem - i - 1, nonSelectedColor);

        }
        fullyInitialized = true;
        repaint();
    }

    public void run() {
        if (numElements <= 0) {
            toolBar.setSortDone();
            return;
        }
        if (action == ToolBar.ACTION_SORT) {
            sortNumElem = numElements;
            makeElementArray();
            runSort();
        } else if (action == ToolBar.ACTION_E_TEST)
            runEfficiencyTest();
        toolBar.setSortDone();
    }

    protected void runEfficiencyTest() {
        if (numElements + 1 < MAX_AF_TESTS)
            sortNumElemCount = numElements + 1;
        else
            sortNumElemCount = MAX_AF_TESTS;
        sortNumElemArray = new int[sortNumElemCount];
        swapsCountArray = new long[sortNumElemCount];
        comparesCountArray = new long[sortNumElemCount];
        maxSwapCount = 1L;
        maxCompareCount = 1L;
        for (int i = 0; i < sortNumElemCount; i++)
            sortNumElemArray[i] = -1;

        sortNumElemArray[0] = 0;
        swapsCountArray[0] = 0L;
        comparesCountArray[0] = 0L;
        graphIsInitialized = true;
        swapsCount = 0L;
        comparesCount = 0L;
        sortNumElem = numElements;
        makeElementArray();
        runSort();
        sortNumElemArray[sortNumElemCount - 1] = sortNumElem;
        swapsCountArray[sortNumElemCount - 1] = swapsCount;
        comparesCountArray[sortNumElemCount - 1] = comparesCount;
        if (swapsCount > 1L)
            maxSwapCount = swapsCount;
        if (comparesCount > 1L)
            maxCompareCount = comparesCount;
        repaint();
        for (int i = 1; i < sortNumElemCount - 1; i++) {
            try {
                Thread.sleep(15L);
            } catch (InterruptedException _ex) {
            }
            if (killProcess)
                return;
            swapsCount = 0L;
            comparesCount = 0L;
            sortNumElem = (int) (((double) numElements / (double) (sortNumElemCount - 1)) * (double) i);
            makeElementArray();
            runSort();
            sortNumElemArray[i] = sortNumElem;
            swapsCountArray[i] = swapsCount;
            comparesCountArray[i] = comparesCount;
            if (swapsCount > maxSwapCount)
                maxSwapCount = swapsCount;
            if (comparesCount > maxCompareCount)
                maxCompareCount = comparesCount;
            repaint();
        }

    }

    protected void runSort() {
    }

    protected void killProcess() {
        killProcess = true;
    }

    protected boolean isLessThanNG(int a, int b) {
        return isGreaterNG(b, a);
    }

    protected boolean isGreaterNG(int a, int b) {
        if (action == ToolBar.ACTION_SORT)
            toolBar.addCompareCount();
        else if (action == ToolBar.ACTION_E_TEST)
            comparesCount++;
        return elementArray[a].size > elementArray[b].size;
    }

    protected boolean isLessThanOrEqualNG(int a, int b) {
        return isGreaterNG(a, b) ^ true;
    }

    protected boolean isGreaterOrEqualNG(int a, int b) {
        return isGreaterNG(b, a) ^ true;
    }

    protected boolean isLessThan(int a, int b) {
        return isGreater(b, a, false);
    }

    protected boolean isGreater(int a, int b) {
        return isGreater(a, b, true);
    }

    protected boolean isLessThanOrEqual(int a, int b) {
        return isGreater(a, b, true) ^ true;
    }

    protected boolean isGreaterOrEqual(int a, int b) {
        return isGreater(b, a, false) ^ true;
    }

    protected boolean isGreater(int a, int b, boolean firstSelected) {
        if (action == ToolBar.ACTION_SORT) {
            while (toolBar.currentSortAction() == ToolBar.SORT_PAUSED) {
                if (killProcess)
                    return true;
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException _ex) {
                }
            }
            if (toolBar.showCompares()) {
                elementArray[a].setColor(firstSelected ? selectedColor : compareColor);
                elementArray[a].setSelected(true);
                elementArray[b].setColor(firstSelected ? compareColor : selectedColor);
                elementArray[b].setSelected(true);
                repaint();
                if (toolBar.currentSortAction() == ToolBar.SORT_STEP)
                    while (!toolBar.takeNextStep() && toolBar.currentSortAction() == ToolBar.SORT_STEP) {
                        if (killProcess)
                            return true;
                        try {
                            Thread.sleep(1L);
                        } catch (InterruptedException _ex) {
                        }
                    }
                else if (toolBar.currentSortAction() == ToolBar.SORT_RUNNING)
                    try {
                        Thread.sleep(toolBar.getCompareDelay());
                    } catch (InterruptedException _ex) {
                    }
                elementArray[a].resetColor();
                elementArray[b].resetColor();
            }
            toolBar.addCompareCount();
        } else if (action == ToolBar.ACTION_E_TEST)
            comparesCount++;
        return elementArray[a].size > elementArray[b].size;
    }

    protected void swap(int a, int b) {
        if (action == ToolBar.ACTION_SORT) {
            while (toolBar.currentSortAction() == ToolBar.SORT_PAUSED) {
                if (killProcess)
                    return;
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException _ex) {
                }
            }
            if (toolBar.showSwaps()) {
                elementArray[a].setColor(selectedColor);
                elementArray[a].setSelected(true);
                elementArray[b].setColor(compareColor);
                elementArray[b].setSelected(true);
                repaint();
                if (toolBar.currentSortAction() == ToolBar.SORT_RUNNING)
                    try {
                        Thread.sleep(toolBar.getSwapDelay());
                    } catch (InterruptedException _ex) {
                    }
            }
            Element temp = elementArray[a];
            elementArray[a] = elementArray[b];
            elementArray[a].setID(a);
            elementArray[b] = temp;
            elementArray[b].setID(b);
            repaint();
            if (toolBar.showSwaps()) {
                if (toolBar.currentSortAction() == ToolBar.SORT_STEP)
                    while (!toolBar.takeNextStep() && toolBar.currentSortAction() == ToolBar.SORT_STEP) {
                        if (killProcess)
                            return;
                        try {
                            Thread.sleep(1L);
                        } catch (InterruptedException _ex) {
                        }
                    }
                else if (toolBar.currentSortAction() == ToolBar.SORT_RUNNING)
                    try {
                        Thread.sleep(toolBar.getSwapDelay());
                    } catch (InterruptedException _ex) {
                    }
                repaint();
                elementArray[a].resetColor();
                elementArray[b].resetColor();
            }
            toolBar.addSwapCount();
        } else if (action == ToolBar.ACTION_E_TEST) {
            swapsCount++;
            int temp = elementArray[a].size;
            elementArray[a].setSize(elementArray[b].size);
            elementArray[b].setSize(temp);
        }
    }

    protected void setIsSorted(int id) {
        if (action == ToolBar.ACTION_E_TEST) {
            return;
        } else {
            elementArray[id].setSorted(true);
            elementArray[id].setSelected(true);
            return;
        }
    }

    protected void setSortDone() {
        if (action == ToolBar.ACTION_E_TEST)
            return;
        for (int i = 0; i < sortNumElem; i++) {
            elementArray[i].setSorted(true);
            elementArray[i].setSelected(true);
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    protected void drawDots(Graphics g) {
        if (fullyInitialized && showDots) {
            for (int i = 0; i < sortNumElem; i++)
                elementArray[i].drawDot(g);

        }
    }

    protected void drawBars(Graphics g) {
        if (fullyInitialized && showBars) {
            for (int i = 0; i < sortNumElem; i++)
                elementArray[i].drawBar(g);

        }
    }

    protected void drawSwaps(Graphics g) {
        if (graphIsInitialized && showSwaps) {
            int x1 = PANEL_MARGIN_LEFT + GRAPH_MARGIN_LEFT;
            int x2 = panelSize.width - PANEL_MARGIN_RIGHT - GRAPH_MARGIN_RIGHT;
            int y1 = PANEL_MARGIN_TOP + GRAPH_MARGIN_TOP;
            int y2 = panelSize.height - PANEL_MARGIN_BOTTOM - GRAPH_MARGIN_BOTTOM;
            int x3 = x2 - (int) ((double) (x1 + x2) * 0.029999999999999999D);
            int y3 = y1 + (int) ((double) (y1 + y2) * 0.029999999999999999D);
            Point bottomRight = new Point(x1 + (int) ((double) (x1 + x2) * 0.050000000000000003D), y2);
            g.setColor(graphAxisColor);
            g.drawLine(x1, y1, x1, y2);
            g.drawLine(x1, y2, x2, y2);
            g.drawLine(x1 - 2, y3, x1 + 3, y3);
            g.drawLine(x3, y2 - 3, x3, y2 + 2);
            g.drawString(String.valueOf(maxSwapCount), x1 - 12 - 6 * Long.toString(maxSwapCount).length(), y1 + 15);
            g.drawString("# of", x1 - 55, (y3 + (y2 - y3) / 2) - 7);
            g.drawString("Swaps", x1 - 60, y3 + (y2 - y3) / 2 + 8);
            g.drawString("0", x1 - 10, y2 + 15);
            g.drawString("# of Elements", x1 + (int) ((double) (x3 - x1) * 0.45000000000000001D), y2 + (PANEL_MARGIN_BOTTOM + GRAPH_MARGIN_BOTTOM) / 2);
            g.drawString(String.valueOf(numElements), x3 - 3 * Integer.toString(numElements).length(), y2 + 15);
            g.setColor(graphDotColor);
            for (int i = 0; i < sortNumElemCount; i++) {
                if (sortNumElemArray[i] == -1)
                    break;
                g.fillRect((int) (((double) sortNumElemArray[i] / (double) numElements) * (double) (x3 - x1) + (double) x1) - DOT_SIZE / 2, y2 - (int) (((double) swapsCountArray[i] / (double) maxSwapCount) * (double) (y2 - y3)) - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
            }

        }
    }

    protected void drawCompares(Graphics g) {
        if (graphIsInitialized && showCompares) {
            int x1 = PANEL_MARGIN_LEFT + GRAPH_MARGIN_LEFT;
            int x2 = panelSize.width - PANEL_MARGIN_RIGHT - GRAPH_MARGIN_RIGHT;
            int y1 = PANEL_MARGIN_TOP + GRAPH_MARGIN_TOP;
            int y2 = panelSize.height - PANEL_MARGIN_BOTTOM - GRAPH_MARGIN_BOTTOM;
            int x3 = x2 - (int) ((double) (x1 + x2) * 0.029999999999999999D);
            int y3 = y1 + (int) ((double) (y1 + y2) * 0.029999999999999999D);
            Point bottomRight = new Point(x1 + (int) ((double) (x1 + x2) * 0.050000000000000003D), y2);
            g.setColor(graphAxisColor);
            g.drawLine(x1, y1, x1, y2);
            g.drawLine(x1, y2, x2, y2);
            g.drawLine(x1 - 2, y3, x1 + 3, y3);
            g.drawLine(x3, y2 - 3, x3, y2 + 2);
            g.drawString(String.valueOf(maxCompareCount), x1 - 12 - 6 * Long.toString(maxCompareCount).length(), y1 + 15);
            g.drawString("# of", x1 - 55, (y3 + (y2 - y3) / 2) - 7);
            g.drawString("Compares", x1 - 70, y3 + (y2 - y3) / 2 + 8);
            g.drawString("0", x1 - 10, y2 + 15);
            g.drawString("# of Elements", x1 + (int) ((double) (x3 - x1) * 0.45000000000000001D), y2 + (PANEL_MARGIN_BOTTOM + GRAPH_MARGIN_BOTTOM) / 2);
            g.drawString(String.valueOf(numElements), x3 - 3 * Integer.toString(numElements).length(), y2 + 15);
            g.setColor(graphDotColor);
            for (int i = 0; i < sortNumElemCount; i++) {
                if (sortNumElemArray[i] == -1)
                    break;
                g.fillRect((int) (((double) sortNumElemArray[i] / (double) numElements) * (double) (x3 - x1) + (double) x1) - DOT_SIZE / 2, y2 - (int) (((double) comparesCountArray[i] / (double) maxCompareCount) * (double) (y2 - y3)) - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
            }

        }
    }

    public void setSortedColor(Color c) {
        sortedColor = c;
    }

    public void setSelectedColor(Color c) {
        selectedColor = c;
    }

    public void setCompareColor(Color c) {
        compareColor = c;
    }

    public void setNonSelectedColor(Color c) {
        nonSelectedColor = c;
    }

    public void setBarBorderColor(Color c) {
        borderColor = c;
    }

    public void setDotColor(Color c) {
        dotColor = c;
    }

    public void setGraphDotColor(Color c) {
        graphDotColor = c;
    }

    public void setGraphAxisColor(Color c) {
        graphAxisColor = c;
    }

    public static int MAX_AF_TESTS = 100;
    protected Color sortedColor;
    protected Color selectedColor;
    protected Color compareColor;
    protected Color nonSelectedColor;
    protected Color borderColor;
    protected Color dotColor;
    protected Color graphDotColor;
    protected Color graphAxisColor;
    public static int PANEL_MARGIN_TOP = 15;
    public static int PANEL_MARGIN_LEFT = 15;
    public static int PANEL_MARGIN_RIGHT = 15;
    public static int PANEL_MARGIN_BOTTOM = 40;
    public static int GRAPH_MARGIN_TOP = 10;
    public static int GRAPH_MARGIN_LEFT = 80;
    public static int GRAPH_MARGIN_RIGHT = 10;
    public static int GRAPH_MARGIN_BOTTOM = 20;
    public static int MIN_BAR_HEIGHT = 10;
    public static int DOT_SIZE = 5;
    protected ToolBar toolBar;
    protected int numElements;
    protected int beginList;
    protected int action;
    protected boolean showDots;
    protected boolean showBars;
    protected boolean showSwaps;
    protected boolean showCompares;
    protected int sortNumElem;
    protected Element elementArray[];
    protected boolean fullyInitialized;
    protected boolean killProcess;
    protected Dimension panelSize;
    protected Dimension thisPanelSize;
    protected AbstractJPanel dotsPanel;
    protected AbstractJPanel barsPanel;
    protected AbstractJPanel swapsGraph;
    protected AbstractJPanel comparesGraph;
    protected long swapsCount;
    protected long comparesCount;
    protected int sortNumElemArray[];
    protected int sortNumElemCount;
    protected long swapsCountArray[];
    protected long comparesCountArray[];
    protected long maxSwapCount;
    protected long maxCompareCount;
    protected boolean graphIsInitialized;

}
