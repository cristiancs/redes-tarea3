package streaming;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class clientRunStreamServer implements Observer, Runnable {
    private StreamObservable observable;
    String ip;
    Integer puerto;
    Integer id;
    // private ThreadPool threadPool;
    private byte[] imagen;

    @Override
    public void update(Observable o, Object arg) {

    }

    clientRunStreamServer(ThreadPool threadPool, StreamObservable observable, String ip, Integer puerto, Integer id) {
        this.observable = observable;
        this.ip = ip;
        this.puerto = puerto;
        // this.threadPool = threadPool;
        this.id = id;
    }

    @Override
    public void run() {
        Utils utils = new Utils();
        try {
            Socket clientSocket = new Socket(ip, puerto);
            Scanner inFromServer = new Scanner(clientSocket.getInputStream());
            Integer inicioBloque = 0;
            String inText = inFromServer.nextLine();

            String mensaje = utils.DecodeBase64ToString(inText);

            System.out.println(mensaje);

            while (!mensaje.equals("end")) {
                byte[] chunkBytes = utils.DecodeBase64ToByteArray(inText);
                System.arraycopy(chunkBytes, 0, imagen, inicioBloque, chunkBytes.length);
                inText = inFromServer.nextLine();
                inicioBloque += 64128;
            }
            observable.setNStreamData(this.id, utils.encodeBytesToBase64String(imagen));

            inFromServer.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}