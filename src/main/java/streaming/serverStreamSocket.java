package streaming;

import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.io.IOException;
import java.io.PrintWriter;

public class serverStreamSocket implements Observer, Runnable {

    private Socket socket;
    private PrintWriter out;
    private StreamObservable observable;
    private StreamObservable internaObservable;
    private Integer id;
    private Utils utils;

    serverStreamSocket(Socket socket, StreamObservable observable, StreamObservable internaObservable, Integer id) {
        this.socket = socket;
        this.observable = observable;
        this.internaObservable = internaObservable;
        this.id = id;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        String separador = utils.encodeStringToBase64String(String.join("", Collections.nCopies(64128, "0")));
        if (mensaje.equals(Integer.toString(id - 1))) {
            String initialString = internaObservable.getStreamData(id);
            byte[] imagen = utils.DecodeBase64ToByteArray(initialString);
            Integer largo = imagen.length;
            Integer inicioBloque = 0;
            while (inicioBloque < largo) {
                Integer finRango = Math.min(inicioBloque + 64128, largo);
                byte[] toSend = Arrays.copyOfRange(imagen, inicioBloque, finRango);
                this.out.println(utils.encodeBytesToBase64String(toSend));
                inicioBloque += 64128;
            }
            System.out.println(id + " sends mensaje");
            this.out.println(separador);

        }
        if (arg.toString().equals("close")) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }

    }

    @Override
    public void run() {
        utils = new Utils();
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            internaObservable.addObserver(this);
            observable.addObserver(this);
            // out.println("Stream server" + id);

        } catch (Exception e) {
            observable.deleteObserver(this);
            internaObservable.deleteObserver(this);
            try {
                socket.close();
            } catch (IOException e1) {
            }
            System.out.println("Socket de control " + socket + " con problemas");
        }

    }
}