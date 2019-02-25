import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JApplet;

public class JavaSortApplet extends JApplet {
    private class MyMouseListener
            implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            windowShowing = windowShowing ^ true;
            frame.setVisible(windowShowing);
        }

        public void mousePressed(MouseEvent mouseevent) {
        }

        public void mouseReleased(MouseEvent mouseevent) {
        }

        public void mouseEntered(MouseEvent mouseevent) {
        }

        public void mouseExited(MouseEvent mouseevent) {
        }

        MyMouseListener() {
        }
    }


    public void init() {
        MyMouseListener listener = new MyMouseListener();
        frame = new UserInterface("Java Sort", this);
        frame.pack();
        frame.setExtendedState(6);
        frame.setVisible(windowShowing);
        addMouseListener(listener);
    }

    public void closeFrame() {
        windowShowing = false;
        frame.setVisible(windowShowing);
    }

    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.drawString("Click to open window", 30, 30);
    }

    public JavaSortApplet() {
        windowShowing = false;
    }

    public UserInterface frame;
    public boolean windowShowing;
}
