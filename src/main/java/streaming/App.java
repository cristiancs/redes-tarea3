package streaming;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class App {

  public static void main(String[] args) {

    try {
      FFmpegFrameGrabber g = new FFmpegFrameGrabber("media/prueba...25.mp4");
      g.start();
      g.grab();
      g.stop();
    } catch (Exception e) {
      System.out.println("Error: " + e);
    }

  }

}
