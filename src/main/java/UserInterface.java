import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class UserInterface extends JFrame {
    private class MyWindowListener
            implements WindowListener {

        public void windowClosing(WindowEvent e) {
            closeWindow();
        }

        public void windowClosed(WindowEvent windowevent) {
        }

        public void windowOpened(WindowEvent windowevent) {
        }

        public void windowIconified(WindowEvent windowevent) {
        }

        public void windowDeiconified(WindowEvent windowevent) {
        }

        public void windowActivated(WindowEvent windowevent) {
        }

        public void windowDeactivated(WindowEvent windowevent) {
        }

        MyWindowListener() {
        }
    }


    public UserInterface(String name, JavaSortApplet javaSortApplet) {
        super(name);
        initialize();
        this.javaSortApplet = javaSortApplet;
    }

    public void initialize() {
        addWindowListener(new MyWindowListener());
        getContentPane().setLayout(new BorderLayout());
        statusField = new JTextField();
        statusField.setText(" Welcome!");
        statusField.setEditable(false);
        statusField.setMinimumSize(new Dimension(300, 23));
        statusField.setPreferredSize(new Dimension(800, 24));
        statusField.setBorder(BorderFactory.createLoweredBevelBorder());
        getContentPane().add(statusField, "South");
        setJMenuBar(createMenus());
        toolBar = new ToolBar(this, statusField);
        toolBar.setMinimumSize(new Dimension(200, 600));
        scrollpane = new JScrollPane(toolBar);
        scrollpane.setMinimumSize(new Dimension(200, 600));
        scrollpane.setPreferredSize(new Dimension(222, 800));
        scrollpane.setHorizontalScrollBarPolicy(31);
        scrollpane.setBorder(BorderFactory.createLoweredBevelBorder());
        getContentPane().add(scrollpane, "East");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, 1));
        sortingPanel = new SortingPanel();
        mainPanel.add(sortingPanel);
        getContentPane().add(mainPanel, "Center");
    }

    public void restart() {
        try {
            Class sortClass = Class.forName(toolBar.getSortAlgClass());
            Constructor sortConstructor = sortClass.getConstructor(new Class[]{
                    ToolBar.class, Dimension.class
            });
            sortingPanel = (SortingPanel) sortConstructor.newInstance(new Object[]{
                    toolBar, mainPanel.getSize()
            });
            mainPanel.removeAll();
            mainPanel.add(sortingPanel);
            mainPanel.updateUI();
            (new Thread(sortingPanel)).start();
        } catch (Exception e) {
            System.out.println("Error occurred loading class \n" + e);
        }
    }

    public void abortSort() {
        sortingPanel.killProcess();
    }

    public void closeWindow() {
        abortSort();
        if (javaSortApplet == null)
            System.exit(0);
        else
            javaSortApplet.closeFrame();
    }

    protected JMenuBar createMenus() {
        final JFrame parentFrame = this;
        JMenuBar mb = new JMenuBar();
        int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        JMenu m = new JMenu("File");
        m.setMnemonic('f');
        m.addSeparator();
        JMenuItem mi;
        m.add(mi = new JMenuItem("Quit"));
        mi.setAccelerator(KeyStroke.getKeyStroke(81, menuMask));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }

        });
        mb.add(m);
        m = new JMenu("Help");
        m.setMnemonic('h');
        m.add(mi = new JMenuItem("Min Requirements"));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(parentFrame, "<html><br><center>Min Screen Resolution: 800 x 600<br>JVM version: 1.3 or newer</center></html>", "Minimum System Requirements", 1);
            }

        });
        m.add(mi = new JMenuItem("About JavaSort"));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(parentFrame, "<html><br><center>Created by Tony Wang<br>Creation Date: 11/21/2003<br>Version Info: v1.0</center></html>", "About JavaSort", 1);
            }

        });
        mb.add(m);
        return mb;
    }

    private JPanel mainPanel;
    private SortingPanel sortingPanel;
    private JTextField statusField;
    private ToolBar toolBar;
    private JScrollPane scrollpane;
    private JavaSortApplet javaSortApplet;
}
