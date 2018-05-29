import javax.swing.*;

public class Main {

    public static void main(String args[]) {
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLayout(null);
        jf.setVisible(true);
        jf.setBounds(0, 0, 1680, 1050);
        jf.setResizable(false);
        jf.setTitle("Images Analysis");

        MyPanel myPanel = new MyPanel();
        jf.add(myPanel);
    }
}