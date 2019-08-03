package streaming;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import streaming.StreamObservable;
import streaming.Utils;

public class VideoHandler implements Observer, Runnable {
    private StreamObservable observable;
    private StreamObservable interObservable;
    private Boolean continuePlaying = false;
    String archivo;

    VideoHandler(StreamObservable observable, StreamObservable interObservable) {
        this.observable = observable;
        this.interObservable = interObservable;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        if (mensaje.startsWith("set_file")) {
            archivo = mensaje.split(" ")[1];
        }
        if (mensaje.equals("stop")) {
            continuePlaying = false;
        } else if (mensaje.startsWith("streaming")) {
            continuePlaying = true;
        }

    }

    @Override
    public void run() {
        observable.addObserver(this);
        interObservable.addObserver(this);
        while (true) {
            if (continuePlaying && archivo != null) {
                try {
                    System.out.println(archivo);
                    FFmpegFrameGrabber g = new FFmpegFrameGrabber(archivo);
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    Utils utils = new Utils();
                    g.start();
                    Frame frame = null;
                    while ((frame = g.grab()) != null && continuePlaying) {
                        if (frame.image != null) {
                            BufferedImage buf = converter.convert(frame);
                            Integer server = 0;
                            if (buf != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(buf, "jpg", baos);
                                baos.flush();
                                byte[] imageInByte = baos.toByteArray();

                                observable.sendStream(utils.encodeBytesToBase64String(imageInByte), server);
                                // System.out.println(utils.encodeBytesToBase64String(imageInByte));
                                baos.close();
                                server = (server + 1) % 2;

                            }
                        }
                    }

                    g.stop();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(40);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }

}
