package streaming;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientWindow implements Observer, Runnable {
    StreamObservable observable;
    Integer fps = 1;
    Boolean isStreaming = false;
    Integer streamChannels;
    JFrame frame;
    JLabel label;
    Boolean stopWorking = false;

    ClientWindow(StreamObservable observable, Integer streamChannels) {
        this.observable = observable;
        this.streamChannels = streamChannels;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        if (mensaje.startsWith("streaming")) {
            this.fps = Integer.parseInt(mensaje.split(" ")[1]);
            this.show();
        } else if (mensaje.equals("stop")) {
            this.close();
        } else if (mensaje.equals("close")) {
            this.stopWorking = true;
            this.close();

        }

    }

    @Override
    public void run() {
        this.observable.addObserver(this);
        while (!stopWorking) {
            try {
                Integer currentFrame = 0;
                while (isStreaming) {
                    try {
                        String frame = this.observable.getNStreamData(currentFrame);
                        currentFrame = (currentFrame + 1) % streamChannels;
                        updateFrame(frame);
                        TimeUnit.MILLISECONDS.sleep(1000 / fps);
                    } catch (Exception e) {
                        break;
                    }
                }
                TimeUnit.MILLISECONDS.sleep(40);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void show() {
        isStreaming = true;
        frame = new JFrame();

        ImageIcon imagen2 = new ImageIcon("prueba.jpg");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        label = new JLabel(imagen2);
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

    public void updateFrame(String imagen_base64) {
        Utils utils = new Utils();
        ImageIcon imagen2 = new ImageIcon(utils.DecodeBase64ToByteArray(imagen_base64));
        label.setIcon(imagen2);

    }

    public void close() {
        isStreaming = false;
        System.out.println("Disposing");
        try {
            frame.setVisible(false);
            frame.dispose();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}