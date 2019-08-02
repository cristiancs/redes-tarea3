
package streaming;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Jonathan
 */
public class StreamObserver implements Observer {
    private Socket socket;

    StreamObserver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            if (mensaje.equals("close")) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e) {
        }
        // System.out.println("Nueva Actualizacion: " + o + " -> " + arg);
    }

}