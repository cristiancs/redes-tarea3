package streaming;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

class StreamingClient {
    String ip;
    Integer puerto;
    Integer controlPort;
    LinkedList<Integer> streamPorts;
    String inText;
    Utils utils;
    ThreadPool threadPool;
    StreamObservable observable;

    StreamingClient(String ip, Integer puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    public void start() {
        observable = new StreamObservable();
        utils = new Utils();
        this.threadPool = new ThreadPool(20, 8);
        streamPorts = new LinkedList<Integer>();

        try {
            Socket clientSocket = new Socket(ip, puerto);
            Scanner inFromServer = new Scanner(clientSocket.getInputStream());
            inText = inFromServer.nextLine();

            String mensaje = utils.DecodeBase64ToString(inText);
            String[] parts = mensaje.split(";");
            Integer i = 0;
            for (String puerto : parts) {

                if (i == 0) {
                    controlPort = Integer.parseInt(puerto);
                } else {
                    this.streamPorts.add(Integer.parseInt(puerto));
                }
                i += 1;
            }
            inFromServer.close();
            clientSocket.close();

            try {
                this.threadPool.submitTask(
                        new clientRunControlServer(this.threadPool, this.observable, this.ip, this.controlPort));
                this.threadPool.submitTask(new ClientWindow(this.observable));
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}