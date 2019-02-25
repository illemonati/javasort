import java.awt.*;

public class JavaSort {

    public static void main(String args[]) {
        frame = new UserInterface("Java Sort", null);
        frame.setResizable(false);
        frame.pack();
        frame.setExtendedState(6);
        frame.setVisible(true);
    }

    public JavaSort() {
    }

    public static UserInterface frame;
}
