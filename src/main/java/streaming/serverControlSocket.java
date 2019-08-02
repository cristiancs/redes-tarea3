package streaming;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.io.IOException;
import java.io.PrintWriter;

public class serverControlSocket implements Observer, Runnable {

    private Socket socket;
    private PrintWriter out;
    private StreamObservable observable;
    private StreamObservable internaObservable;

    serverControlSocket(Socket socket, StreamObservable observable, StreamObservable internaObservable) {
        this.socket = socket;
        this.observable = observable;
        this.internaObservable = internaObservable;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        try {

            out.println(mensaje);
            if (mensaje.equals("close")) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void run() {

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            out.println(this.observable.getMensaje());
            if (this.observable.getMensaje().equals("close")) {
                socket.close();
            }

            this.observable.addObserver(this);
            // socket.close();

        } catch (Exception e) {
            System.out.println("Socket de control " + socket + " con problemas");
        }

    }
}