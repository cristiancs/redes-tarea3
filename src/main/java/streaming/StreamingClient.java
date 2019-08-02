package streaming;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.librealsense.frame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.*;

class StreamingClient {
    public static void main(String args[]) {

    }

    public void start() {

        JFrame frame = new JFrame();

        Boolean add1 = false;

        ImageIcon imagen1 = new ImageIcon("prueba2.jpg");
        ImageIcon imagen2 = new ImageIcon("prueba.jpg");
        JLabel label = new JLabel(imagen1);
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
        while (true) {
            label.setIcon(add1 ? imagen1 : imagen2);

            try {
                TimeUnit.MILLISECONDS.sleep(1000 / 25);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            add1 = !add1;
        }

    }
}