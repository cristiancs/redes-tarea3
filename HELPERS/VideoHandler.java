package streaming_temp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.util.concurrent.TimeUnit;

import streaming.StreamObservable;
import streaming.Utils;

public class VideoHandler implements Observer, Runnable {
    private StreamObservable observable;
    private Boolean continuePlaying = false;

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();

        if (arg.toString().equals("stop")) {
            continuePlaying = false;
        } else if (arg.toString().startsWith("streaming")) {
            continuePlaying = true;
        }

    }

    @Override
    public void start() {
        while (true) {
            if (continuePlaying) {
                try {
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
            TimeUnit.MILLISECONDS.sleep(40);
        }

    }

}
