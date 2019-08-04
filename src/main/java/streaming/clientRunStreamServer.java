package streaming;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
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
    Scanner inFromServer;
    Boolean stopStream;
    // private ThreadPool threadPool;

    @Override
    public void update(Observable o, Object arg) {
        if (arg.toString().equals("end")) {
            try {
                inFromServer.close();
                clientSocket.close();
                stopStream = true;
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
        stopStream = false;
        try {
            clientSocket = new Socket(ip, puerto);
            inFromServer = new Scanner(clientSocket.getInputStream());
            Integer inicioBloque = 0;
            LinkedList<Byte> imLink = new LinkedList<Byte>();
            try {
                while (!stopStream) {

                    inText = inFromServer.nextLine();
                    String separador = utils
                            .encodeStringToBase64String(String.join("", Collections.nCopies(64128, "0")));
                    while (!inText.equals(separador)) {
                        byte[] chunkBytes = utils.DecodeBase64ToByteArray(inText);
                        for (byte chunkb : chunkBytes) {
                            imLink.add(chunkb);
                        }
                        inText = inFromServer.nextLine();
                        inicioBloque += 64128;
                    }
                    byte[] imagen = new byte[imLink.size()];
                    imLink.toArray();
                    Integer i = 0;
                    for (byte b : imLink) {
                        imagen[i] = b;
                        i += 1;
                    }
                    // System.out.println("Updating frame by " + this.id);
                    observable.setNStreamData(this.id, utils.encodeBytesToBase64String(imagen));
                    // inText = inFromServer.nextLine();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}