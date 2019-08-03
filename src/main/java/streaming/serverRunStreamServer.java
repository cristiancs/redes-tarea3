package streaming;

import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

public class serverRunStreamServer implements Observer, Runnable {
    ThreadPool threadPool;
    Integer id;
    private StreamObservable observable;
    private boolean cerrarSocket = false;
    private StreamObservable internaObservable;

    @Override
    public void update(Observable o, Object arg) {

        if (arg.toString().equals("close")) {
            this.cerrarSocket = true;
        }
    }

    serverRunStreamServer(ThreadPool threadPool, StreamObservable observable, StreamObservable internaObservable,
            Integer id) {
        this.threadPool = threadPool;
        this.observable = observable;
        this.internaObservable = internaObservable;
        this.id = id;
    }

    public void StartServer(Integer port) throws Exception {
        try (ServerSocket listener = new ServerSocket(port)) {
            // System.out.println("Started stream server " + id + " on port " + port);

            internaObservable.cambiarMensaje("stream_socket:" + id + ":" + port);

            while (!this.cerrarSocket) {
                this.threadPool.submitTask(
                        new serverStreamSocket(listener.accept(), this.observable, this.internaObservable, id));
            }
        }
    }

    @Override
    public void run() {
        Integer listenPort = 20000;
        while (!this.cerrarSocket) {
            try {

                this.StartServer(listenPort);

            } catch (Exception e) {
                listenPort += 1;
            }
        }
    }

}