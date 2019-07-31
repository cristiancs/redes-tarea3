package streaming;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import streaming.Utils;

public class App {

  public void start() {
    try {
      FFmpegFrameGrabber g = new FFmpegFrameGrabber("media/prueba...25.mp4");
      Java2DFrameConverter converter = new Java2DFrameConverter();
      Utils utils = new Utils();
      g.start();
      Frame frame = null;
      while ((frame = g.grab()) != null) {
        if (frame.image != null) {
          BufferedImage buf = converter.convert(frame);
          if (buf != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buf, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            System.out.println(utils.encodeBytesToBase64String(imageInByte));
            baos.close();

          }
        }
      }

      g.stop();
    } catch (Exception e) {
      System.out.println("Error: " + e);
    }
  }

  public static void main(String[] args) {
    App app = new App();
    app.start();

  }

}
