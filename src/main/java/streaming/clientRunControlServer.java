package streaming;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class clientRunControlServer implements Observer, Runnable {
    private StreamObservable observable;
    String ip;
    Integer puerto;
    // private ThreadPool threadPool;

    @Override
    public void update(Observable o, Object arg) {

    }

    clientRunControlServer(ThreadPool threadPool, StreamObservable observable, String ip, Integer puerto) {
        this.observable = observable;
        this.ip = ip;
        this.puerto = puerto;
        // this.threadPool = threadPool;
    }

    @Override
    public void run() {
        Utils utils = new Utils();
        try {
            Socket clientSocket = new Socket(ip, puerto);
            Scanner inFromServer = new Scanner(clientSocket.getInputStream());
            String inText = inFromServer.nextLine();
            String mensaje = utils.DecodeBase64ToString(inText);

            System.out.println("Control " + mensaje);

            while (!mensaje.equals("close")) {

                if (mensaje.startsWith("streaming") || mensaje.equals("stop")) {
                    observable.cambiarMensaje(mensaje);
                }
                inText = inFromServer.nextLine();
                mensaje = utils.DecodeBase64ToString(inText);
                System.out.println("Control " + mensaje);
            }
            if (mensaje.equals("close")) {
                observable.cambiarMensaje("close");
            }
            inFromServer.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}