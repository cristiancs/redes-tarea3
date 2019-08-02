package streaming;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientWindow implements Observer, Runnable {
    private StreamObservable observable;
    Integer fps = 1;
    JFrame frame;

    ClientWindow(StreamObservable observable) {
        this.observable = observable;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        if (mensaje.startsWith("streaming")) {
            this.fps = Integer.parseInt(mensaje.split(" ")[1]);
            this.show();
        } else if (mensaje.equals("stop")) {
            this.close();
        }

    }

    @Override
    public void run() {
        observable.addObserver(this);
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void show() {
        frame = new JFrame();

        ImageIcon imagen2 = new ImageIcon("prueba.jpg");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(imagen2);
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
        label.setIcon(imagen2);
        // while (true) {
        // label.setIcon(add1 ? imagen1 : imagen2);

        // try {
        // TimeUnit.MILLISECONDS.sleep(1000 / fps);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // add1 = !add1;
        // }
    }

    public void close() {
        System.out.println("Disposing");
        try {
            frame.setVisible(false);
            frame.dispose();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}