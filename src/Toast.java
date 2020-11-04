import javax.swing.*;
import java.awt.*;

public class Toast extends JFrame {

    String s;
    JWindow w = new JWindow();

    public void ToastMessage(String message, int x, int y) {
        w = new JWindow();

        w.setBackground(new Color(0, 0, 0, 0));

        JPanel p = new JPanel() {
            public void paintComponent(Graphics g)
            {
                int wid = g.getFontMetrics().stringWidth(s);
                int hei = g.getFontMetrics().getHeight();

                g.setColor(Color.black);
                g.fillRect(10, 10, wid + 30, hei + 10);
                g.setColor(Color.black);
                g.drawRect(10, 10, wid + 30, hei + 10);

                // set the color of text
                g.setColor(new Color(255, 255, 0, 240));
                g.drawString(s, 25, 27);
                int t = 250;

                for (int i = 0; i < 4; i++) {
                    t -= 60;
                    g.setColor(new Color(0, 0, 0, t));
                    g.drawRect(10 - i, 10 - i, wid + 30 + i * 2,
                            hei + 10 + i * 2);
                }
            }
        };

        w.add(p);
        w.setLocation(x, y);
        w.setSize(900, 900);
    }

    public void display() {
        try {
            w.setOpacity(1);
            w.setVisible(true);

            for (double d = 1.0; d > 0.2; d -= 0.1) {
                Thread.sleep(300);
                w.setOpacity((float)d);
            }

            w.setVisible(false);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

