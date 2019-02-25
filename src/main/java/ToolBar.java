import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

public class ToolBar extends JPanel {
    public class SliderListener
            implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            JSlider s1 = (JSlider) e.getSource();
            if (s1 == swapSpeedBar) {
                setStatus(" Swapping Speed: " + s1.getValue());
                swapSpeedDelay = (ToolBar.MAX_SWAP_SPEED - s1.getValue()) + 1;
            } else if (s1 == compareSpeedBar) {
                setStatus(" Compare Speed: " + s1.getValue());
                compareSpeedDelay = (ToolBar.MAX_COMPARE_SPEED - s1.getValue()) + 1;
            } else if (s1 == unitElement) {
                numElements = s1.getValue() + 100 * hundredElement.getValue();
                if (unitElement.getValue() < ToolBar.MIN_ELEMENTS && hundredElement.getValue() == 0)
                    numElements = ToolBar.MIN_ELEMENTS;
                setStatus(" Number of Elements: " + numElements);
                numElementBor.setTitle("# of Elements: " + Integer.toString(numElements));
                numElemPanel.repaint();
            } else if (s1 == hundredElement) {
                numElements = s1.getValue() * 100 + unitElement.getValue();
                if (unitElement.getValue() < ToolBar.MIN_ELEMENTS && hundredElement.getValue() == 0)
                    numElements = ToolBar.MIN_ELEMENTS;
                setStatus(" Number of Elements: " + numElements);
                numElementBor.setTitle("# of Elements: " + Integer.toString(numElements));
                numElemPanel.repaint();
            } else {
                setStatus(" Value:" + s1.getValue());
            }
        }

        private JTextField sf;

        public SliderListener(JTextField sf) {
            this.sf = sf;
        }
    }


    public ToolBar(UserInterface parent, JTextField statusField) {
        SliderListener listener = new SliderListener(statusField);
        parentFrame = parent;
        this.statusField = statusField;
        generator = new Random(System.currentTimeMillis());
        setLayout(new BoxLayout(this, 1));
        add(Box.createRigidArea(VGAP5));
        swapCount = 0L;
        compareCount = 0L;
        swapCountLabel = new JLabel(Long.toString(swapCount));
        compareCountLabel = new JLabel(Long.toString(compareCount));
        JPanel p = new JPanel();
        p.setMinimumSize(new Dimension(200, 60));
        p.setPreferredSize(new Dimension(200, 60));
        p.setMaximumSize(new Dimension(200, 60));
        p.setLayout(new BoxLayout(p, 0));
        p.setBorder(new TitledBorder("Info Box"));
        p.add(Box.createRigidArea(HGAP3));
        JPanel p1 = new JPanel();
        p1.setMinimumSize(new Dimension(90, 60));
        p1.setPreferredSize(new Dimension(90, 60));
        p1.setMaximumSize(new Dimension(90, 60));
        p1.setLayout(new BoxLayout(p1, 1));
        p1.add(new JLabel("Swaps: "));
        p1.add(new JLabel("Comparisons: "));
        p.add(p1);
        p1 = new JPanel();
        p1.setMinimumSize(new Dimension(90, 60));
        p1.setPreferredSize(new Dimension(90, 60));
        p1.setMaximumSize(new Dimension(90, 60));
        p1.setLayout(new BoxLayout(p1, 1));
        p1.add(swapCountLabel);
        p1.add(compareCountLabel);
        p.add(p1);
        add(p);
        swapSpeedDelay = MAX_SWAP_SPEED / 2;
        p = new JPanel();
        p.setMinimumSize(new Dimension(200, 60));
        p.setPreferredSize(new Dimension(200, 60));
        p.setMaximumSize(new Dimension(200, 60));
        p.setLayout(new BoxLayout(p, 1));
        p.setBorder(new TitledBorder("Swapping Speed"));
        swapSpeedBar = new JSlider(0, 0, MAX_SWAP_SPEED, swapSpeedDelay);
        swapSpeedBar.addChangeListener(listener);
        swapSpeedBar.setPaintTicks(false);
        swapSpeedBar.setMajorTickSpacing(MAX_SWAP_SPEED + 1);
        swapSpeedBar.setPaintLabels(true);
        swapSpeedBar.getLabelTable().put(new Integer(0), new JLabel("", 0));
        swapSpeedBar.getLabelTable().put(new Integer((int) ((double) MAX_SWAP_SPEED * 0.070000000000000007D) + 1), new JLabel("Slow", 0));
        swapSpeedBar.getLabelTable().put(new Integer((int) ((double) MAX_SWAP_SPEED * 0.93999999999999995D)), new JLabel("Fast", 0));
        swapSpeedBar.setLabelTable(swapSpeedBar.getLabelTable());
        p.add(swapSpeedBar);
        add(p);
        compareSpeedDelay = MAX_COMPARE_SPEED / 2;
        p = new JPanel();
        p.setMinimumSize(new Dimension(200, 60));
        p.setPreferredSize(new Dimension(200, 60));
        p.setMaximumSize(new Dimension(200, 60));
        p.setLayout(new BoxLayout(p, 1));
        p.setBorder(new TitledBorder("Compare Speed"));
        compareSpeedBar = new JSlider(0, 0, MAX_COMPARE_SPEED, compareSpeedDelay);
        compareSpeedBar.addChangeListener(listener);
        compareSpeedBar.setPaintTicks(false);
        compareSpeedBar.setMajorTickSpacing(MAX_COMPARE_SPEED + 1);
        compareSpeedBar.setPaintLabels(true);
        compareSpeedBar.getLabelTable().put(new Integer(0), new JLabel("", 0));
        compareSpeedBar.getLabelTable().put(new Integer((int) ((double) MAX_COMPARE_SPEED * 0.070000000000000007D) + 1), new JLabel("Slow", 0));
        compareSpeedBar.getLabelTable().put(new Integer((int) ((double) MAX_COMPARE_SPEED * 0.93999999999999995D)), new JLabel("Fast", 0));
        compareSpeedBar.setLabelTable(compareSpeedBar.getLabelTable());
        p.add(compareSpeedBar);
        add(p);
        sortRunning = SORT_NULL;
        stepSort = false;
        p = new JPanel();
        p.setMinimumSize(new Dimension(200, 30));
        p.setPreferredSize(new Dimension(200, 30));
        p.setMaximumSize(new Dimension(200, 30));
        p.setLayout(new BoxLayout(p, 0));
        p.add(Box.createRigidArea(HGAP3));
        startButton = new JButton("Start");
        startButton.setMargin(buttonMargin);
        startButton.setMinimumSize(new Dimension(67, 15));
        startButton.setPreferredSize(new Dimension(67, 20));
        startButton.setMaximumSize(new Dimension(67, 25));
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (startButton.getText() == "Start") {
                    if (sortRunning == ToolBar.SORT_NULL) {
                        resetCounts();
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                parentFrame.restart();
                            }

                        });
                        abortButton.setEnabled(true);
                    }
                    sortRunning = ToolBar.SORT_RUNNING;
                    startButton.setText("Pause");
                } else if (startButton.getText() == "Resume") {
                    sortRunning = ToolBar.SORT_RUNNING;
                    startButton.setText("Pause");
                } else if (startButton.getText() == "Pause") {
                    sortRunning = ToolBar.SORT_PAUSED;
                    startButton.setText("Resume");
                }
            }

        });
        p.add(startButton);
        p.add(Box.createRigidArea(HGAP3));
        stepButton = new JButton("Step");
        stepButton.setMargin(buttonMargin);
        stepButton.setMinimumSize(new Dimension(53, 15));
        stepButton.setPreferredSize(new Dimension(53, 20));
        stepButton.setMaximumSize(new Dimension(53, 25));
        stepButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stepSort = true;
                startButton.setText("Resume");
                if (sortRunning == ToolBar.SORT_NULL) {
                    stepSort = false;
                    sortRunning = ToolBar.SORT_STEP;
                    resetCounts();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            parentFrame.restart();
                        }

                    });
                    abortButton.setEnabled(true);
                }
                sortRunning = ToolBar.SORT_STEP;
            }

        });
        p.add(stepButton);
        p.add(Box.createRigidArea(HGAP3));
        abortButton = new JButton("Abort");
        abortButton.setEnabled(false);
        abortButton.setMargin(buttonMargin);
        abortButton.setMinimumSize(new Dimension(58, 15));
        abortButton.setPreferredSize(new Dimension(58, 20));
        abortButton.setMaximumSize(new Dimension(58, 25));
        abortButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                startButton.setText("Start");
                abortButton.setEnabled(false);
                sortRunning = ToolBar.SORT_NULL;
                parentFrame.abortSort();
            }

        });
        p.add(abortButton);
        p.add(Box.createRigidArea(HGAP3));
        p1 = new JPanel();
        p1.setMinimumSize(new Dimension(200, 60));
        p1.setPreferredSize(new Dimension(200, 60));
        p1.setMaximumSize(new Dimension(200, 60));
        p1.setLayout(new BoxLayout(p1, 1));
        p1.setBorder(new TitledBorder("Commands"));
        p1.add(p);
        p1.add(Box.createRigidArea(VGAP3));
        add(p1);
        numElements = 25;
        numElementBor = new TitledBorder("# of Elements: " + Integer.toString(numElements));
        numElemPanel = new JPanel();
        numElemPanel.setMinimumSize(new Dimension(200, 90));
        numElemPanel.setPreferredSize(new Dimension(200, 90));
        numElemPanel.setMaximumSize(new Dimension(200, 90));
        numElemPanel.setLayout(new BoxLayout(numElemPanel, 1));
        numElemPanel.setBorder(numElementBor);
        unitElement = new JSlider(0, 0, 99, 25);
        unitElement.addChangeListener(listener);
        unitElement.setPaintTicks(false);
        unitElement.setMajorTickSpacing(100);
        unitElement.setPaintLabels(true);
        unitElement.getLabelTable().put(new Integer(0), new JLabel("", 0));
        unitElement.getLabelTable().put(new Integer(1), new JLabel("   0  ", 0));
        unitElement.getLabelTable().put(new Integer(96), new JLabel("  99  ", 0));
        unitElement.setLabelTable(unitElement.getLabelTable());
        numElemPanel.add(unitElement);
        hundredElement = new JSlider(0, 0, 99, 0);
        hundredElement.addChangeListener(listener);
        hundredElement.setPaintTicks(false);
        hundredElement.setMajorTickSpacing(100);
        hundredElement.setPaintLabels(true);
        hundredElement.getLabelTable().put(new Integer(0), new JLabel("", 0));
        hundredElement.getLabelTable().put(new Integer(2), new JLabel("000", 0));
        hundredElement.getLabelTable().put(new Integer(95), new JLabel("9900", 0));
        hundredElement.setLabelTable(hundredElement.getLabelTable());
        numElemPanel.add(hundredElement);
        numElemPanel.add(Box.createRigidArea(VGAP5));
        add(numElemPanel);
        p = new JPanel();
        p.setMinimumSize(new Dimension(200, 53));
        p.setPreferredSize(new Dimension(200, 53));
        p.setMaximumSize(new Dimension(200, 53));
        p.setLayout(new BoxLayout(p, 1));
        p.setBorder(new TitledBorder("Begining List"));
        beginSorted = new JComboBox(beginSortTypes);
        beginSorted.setMinimumSize(new Dimension(180, 20));
        beginSorted.setPreferredSize(new Dimension(180, 20));
        beginSorted.setMaximumSize(new Dimension(180, 25));
        beginSorted.setEditable(false);
        p.add(beginSorted);
        add(p);
        p = new JPanel();
        p.setMinimumSize(new Dimension(200, 53));
        p.setPreferredSize(new Dimension(200, 53));
        p.setMaximumSize(new Dimension(200, 53));
        p.setLayout(new BoxLayout(p, 1));
        p.setBorder(new TitledBorder("Action"));
        actionToTake = new JComboBox(actionsToChoose);
        actionToTake.setMinimumSize(new Dimension(180, 20));
        actionToTake.setPreferredSize(new Dimension(180, 20));
        actionToTake.setMaximumSize(new Dimension(180, 25));
        actionToTake.setEditable(false);
        actionToTake.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (actionToTake.getSelectedIndex() == ToolBar.ACTION_SORT)
                    stepButton.setEnabled(true);
                else if (actionToTake.getSelectedIndex() == ToolBar.ACTION_E_TEST)
                    stepButton.setEnabled(false);
            }

        });
        p.add(actionToTake);
        add(p);
        p = new JPanel();
        p.setMinimumSize(new Dimension(178, 130));
        p.setLayout(new BoxLayout(p, 1));
        algRadios = new ArrayList();
        ButtonGroup radioGroup = new ButtonGroup();
        for (int i = 0; i < algorithms.length; i++) {
            JRadioButton radio = new JRadioButton(algorithms[i][0]);
            p.add(radio);
            algRadios.add(radio);
            radioGroup.add(radio);
        }

        if (algRadios.size() > 0)
            ((JRadioButton) algRadios.get(0)).setSelected(true);
        JScrollPane scrollpane = new JScrollPane(p);
        scrollpane.setMinimumSize(new Dimension(200, 130));
        scrollpane.setPreferredSize(new Dimension(200, 130));
        scrollpane.setMaximumSize(new Dimension(200, 130));
        scrollpane.setBorder(new TitledBorder("Sorting Algorithm"));
        add(scrollpane);
        p = new JPanel();
        p.setMinimumSize(new Dimension(200, 127));
        p.setPreferredSize(new Dimension(200, 127));
        p.setMaximumSize(new Dimension(200, 127));
        p.setLayout(new BoxLayout(p, 0));
        p.setBorder(new TitledBorder("Preferences"));
        p.add(Box.createRigidArea(HGAP3));
        p1 = new JPanel();
        p1.setMinimumSize(new Dimension(180, 120));
        p1.setPreferredSize(new Dimension(180, 120));
        p1.setMaximumSize(new Dimension(180, 120));
        p1.setLayout(new BoxLayout(p1, 1));
        showSwaps = new JCheckBox("Show Swaps", true);
        p1.add(showSwaps);
        showCompares = new JCheckBox("Show Comparisons", true);
        p1.add(showCompares);
        showDots = new JCheckBox("Show Dots", true);
        p1.add(showDots);
        showBars = new JCheckBox("Show Bars", true);
        p1.add(showBars);
        p.add(p1);
        add(p);
        add(Box.createRigidArea(VGAP10));
    }

    public boolean randBoolean() {
        return generator.nextBoolean();
    }

    public int randInt() {
        return generator.nextInt();
    }

    public int randInt(int max) {
        return generator.nextInt() % max;
    }

    public int randInt(int min, int max) {
        return generator.nextInt() % (max - min) + min;
    }

    public long randLong() {
        return generator.nextLong();
    }

    public double randDouble() {
        return generator.nextDouble();
    }

    public void setStatus(String status) {
        statusField.setText(status);
    }

    public void setSortDone() {
        startButton.setText("Start");
        abortButton.setEnabled(false);
        sortRunning = SORT_NULL;
    }

    public void resetCounts() {
        swapCount = 0L;
        compareCount = 0L;
        swapCountLabel.setText(Long.toString(swapCount));
        compareCountLabel.setText(Long.toString(compareCount));
    }

    public long addSwapCount() {
        swapCountLabel.setText(Long.toString(++swapCount));
        return swapCount;
    }

    public long getSwapCount() {
        return swapCount;
    }

    public long addCompareCount() {
        compareCountLabel.setText(Long.toString(++compareCount));
        return compareCount;
    }

    public long getCompareCount() {
        return compareCount;
    }

    public int getSwapDelay() {
        return swapSpeedDelay;
    }

    public int getCompareDelay() {
        return compareSpeedDelay;
    }

    public int currentSortAction() {
        return sortRunning;
    }

    public synchronized boolean takeNextStep() {
        if (stepSort) {
            stepSort = false;
            return true;
        } else {
            return false;
        }
    }

    public int getNumElements() {
        return numElements;
    }

    public int getBeginSortType() {
        return beginSorted.getSelectedIndex();
    }

    public int getAction() {
        return actionToTake.getSelectedIndex();
    }

    public String getSortAlgName() {
        for (int i = 0; i < algRadios.size(); i++)
            if (((JRadioButton) algRadios.get(i)).isSelected())
                return algorithms[i][0];

        return "";
    }

    public String getSortAlgClass() {
        for (int i = 0; i < algRadios.size(); i++)
            if (((JRadioButton) algRadios.get(i)).isSelected())
                return algorithms[i][1];

        return "";
    }

    public boolean showSwaps() {
        return showSwaps.isSelected();
    }

    public boolean showCompares() {
        return showCompares.isSelected();
    }

    public boolean showDots() {
        return showDots.isSelected();
    }

    public boolean showBars() {
        return showBars.isSelected();
    }

    private UserInterface parentFrame;
    private JTextField statusField;
    private Random generator;
    public static String algorithms[][] = {
            {
                    "Bubble Sort", "BubbleSort", "Easiest low efficiency sort"
            }, {
            "Insertion Sort", "InsertionSort", "Also a low efficiency sort"
    }, {
            "Selection Sort", "SelectionSort", "Also a low efficiency sort"
    }, {
            "Shaker Sort", "ShakerSort", "Pretty low efficiency sort"
    }, {
            "Modified Shaker Sort", "ModShakerSort", "more efficient than regular shaker sort"
    }, {
            "Heap Sort", "HeapSort", "pretty nice"
    }, {
            "Shell Sort", "ShellSort", "pretty nice"
    }, {
            "Standard Quick Sort", "StandardQuickSort", "Using the standard quick sort method"
    }, {
            "Quick Sort - Random pivot", "RandomQuickSort", "Using a random pivot for the quick sort"
    }, {
            "Quick Sort - Median pivot", "MedianQuickSort", "Using the median for the pivot"
    }
    };
    public static Dimension HGAP3 = new Dimension(3, 1);
    public static Dimension VGAP3 = new Dimension(1, 3);
    public static Dimension HGAP5 = new Dimension(5, 1);
    public static Dimension VGAP5 = new Dimension(1, 5);
    public static Dimension HGAP10 = new Dimension(10, 1);
    public static Dimension VGAP10 = new Dimension(1, 10);
    public static int MAX_SWAP_SPEED = 100;
    public static int MAX_COMPARE_SPEED = 300;
    public static Insets buttonMargin = new Insets(0, 0, 0, 0);
    public static int MIN_ELEMENTS = 2;
    private JLabel swapCountLabel;
    private JLabel compareCountLabel;
    private long swapCount;
    private long compareCount;
    private JSlider swapSpeedBar;
    private JSlider compareSpeedBar;
    private int swapSpeedDelay;
    private int compareSpeedDelay;
    private JButton startButton;
    private JButton stepButton;
    private JButton abortButton;
    private int sortRunning;
    private boolean stepSort;
    public static int SORT_NULL = 0;
    public static int SORT_RUNNING = 1;
    public static int SORT_STEP = 2;
    public static int SORT_PAUSED = 3;
    private JPanel numElemPanel;
    private int numElements;
    private TitledBorder numElementBor;
    private JSlider unitElement;
    private JSlider hundredElement;
    private JComboBox beginSorted;
    private String beginSortTypes[] = {
            " Random", " Sorted", " Reversed"
    };
    public static int BEGIN_RANDOM = 0;
    public static int BEGIN_SORTED = 1;
    public static int BEGIN_REVERSED = 2;
    private JComboBox actionToTake;
    private String actionsToChoose[] = {
            " Single Sort", " Algorithm Efficiency Test"
    };
    public static int ACTION_SORT = 0;
    public static int ACTION_E_TEST = 1;
    private ArrayList algRadios;
    private JCheckBox showSwaps;
    private JCheckBox showCompares;
    private JCheckBox showDots;
    private JCheckBox showBars;


}
