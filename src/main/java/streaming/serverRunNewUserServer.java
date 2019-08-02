package streaming;

import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

public class serverRunNewUserServer implements Observer, Runnable {
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

    serverRunNewUserServer(ThreadPool threadPool, StreamObservable observable, StreamObservable internaObservable) {
        this.threadPool = threadPool;
        this.observable = observable;
        this.internaObservable = internaObservable;
    }

    public void StartServer(Integer port) throws Exception {
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("Connections socket started on port: " + port);
            this.observable.addObserver(this);
            while (!this.cerrarSocket) {
                this.threadPool.submitTask(new serverNewUsers(listener.accept(), this.threadPool, this.observable,
                        this.internaObservable));
            }
        }
    }

    @Override
    public void run() {
        Integer listenPort = 6666;
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