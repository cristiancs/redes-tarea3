package streaming;

import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

public class serverRunControlServer implements Observer, Runnable {
    ThreadPool threadPool;
    private StreamObservable observable;
    private boolean cerrarSocket = false;
    private StreamObservable internaObservable;

    @Override
    public void update(Observable o, Object arg) {

        if (arg.toString().equals("close")) {
            System.out.println(arg);
            this.cerrarSocket = true;
        }
    }

    serverRunControlServer(ThreadPool threadPool, StreamObservable observable, StreamObservable internaObservable) {
        this.threadPool = threadPool;
        this.observable = observable;
        this.internaObservable = internaObservable;
    }

    public void StartServer(Integer port) throws Exception {
        try (ServerSocket listener = new ServerSocket(port)) {
            internaObservable.cambiarMensaje("control_socket:" + port);

            while (!this.cerrarSocket) {
                this.threadPool.submitTask(
                        new serverControlSocket(listener.accept(), this.observable, this.internaObservable));
            }
        }
    }

    @Override
    public void run() {
        Integer listenPort = 10000;
        while (true) {
            try {
                this.StartServer(listenPort);
                break;

            } catch (Exception e) {
                listenPort += 1;
            }
        }
    }

}