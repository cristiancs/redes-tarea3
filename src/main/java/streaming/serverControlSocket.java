package streaming;

import java.net.Socket;
import java.util.Observer;
import java.io.IOException;
import java.io.PrintWriter;

public class serverControlSocket implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private StreamObservable observable;

    serverControlSocket(Socket socket, StreamObservable observable) {
        this.socket = socket;
        this.observable = observable;
    }

    @Override
    public void run() {

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hola desde el servidor de control");

            StreamObserver observador = new StreamObserver(this.socket);
            this.observable.addObserver(observador);
            // socket.close();

        } catch (Exception e) {
            System.out.println("Socket de control " + socket + " con problemas");
        }

    }
}