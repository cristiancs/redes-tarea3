package streaming;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Observer;

class StreamingClient implements Observer {
    String ip;
    Integer puerto;
    Integer controlPort;
    LinkedList<Integer> streamPorts;
    String inText;
    Utils utils;
    ThreadPool threadPool;
    StreamObservable observable;

    @Override
    public void update(Observable o, Object arg) {
        if (arg.toString().equals("close")) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
                System.exit(0);
            } catch (Exception e) {
                System.exit(0);
            }

        }

    }

    StreamingClient(String ip, Integer puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    public void start() {
        observable = new StreamObservable();
        utils = new Utils();
        observable.addObserver(this);
        this.threadPool = new ThreadPool(20, 8);
        streamPorts = new LinkedList<Integer>();

        try {
            Socket clientSocket = new Socket(ip, puerto);
            Scanner inFromServer = new Scanner(clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(utils.encodeStringToBase64String("req"));
            String inText = inFromServer.nextLine();
            String mensaje = utils.DecodeBase64ToString(inText);

            if (mensaje.equals("ok")) {
                out.println(utils.encodeStringToBase64String("ack"));
                inText = inFromServer.nextLine();

                mensaje = utils.DecodeBase64ToString(inText);
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
                    this.threadPool.submitTask(new ClientWindow(this.observable, this.streamPorts.size()));
                    Integer id = 1;
                    for (Integer port : streamPorts) {
                        this.threadPool.submitTask(
                                new clientRunStreamServer(this.threadPool, this.observable, this.ip, port, id));
                        id += 1;
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}