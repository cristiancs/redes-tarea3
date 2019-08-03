package streaming;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class clientRunStreamServer implements Observer, Runnable {
    private StreamObservable observable;
    String ip;
    Integer puerto;
    Integer id;
    Socket clientSocket;
    String inText;
    // private ThreadPool threadPool;
    private byte[] imagen;

    @Override
    public void update(Observable o, Object arg) {
        if (arg.toString().equals("end")) {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }

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
            clientSocket = new Socket(ip, puerto);
            Scanner inFromServer = new Scanner(clientSocket.getInputStream());
            Integer inicioBloque = 0;
            try {
                inText = inFromServer.nextLine();
                String mensaje = utils.DecodeBase64ToString(inText);

                while (!mensaje.equals("end")) {
                    byte[] chunkBytes = utils.DecodeBase64ToByteArray(inText);
                    System.arraycopy(chunkBytes, 0, imagen, inicioBloque, chunkBytes.length);
                    inText = inFromServer.nextLine();
                    mensaje = utils.DecodeBase64ToString(inText);
                    if (mensaje.equals("end") || mensaje.equals("end")) {
                        break;
                    }
                    inicioBloque += 64128;
                }
                observable.setNStreamData(this.id, utils.encodeBytesToBase64String(imagen));
            } catch (Exception e) {
                System.out.println("Stream interumpido");
            }

            inFromServer.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}