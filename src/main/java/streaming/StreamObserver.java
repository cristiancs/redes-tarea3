
package streaming;

import java.net.Socket;
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
        if (mensaje.startsWith("streaming ")) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(mensaje);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        System.out.println("Nueva Actualizacion: " + o + " -> " + arg);
    }

}