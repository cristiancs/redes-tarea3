package streaming;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.io.IOException;
import java.io.PrintWriter;

public class serverNewUsers implements Observer, Runnable {
    ThreadPool threadPool;
    Integer controlSocket = 0;
    private Socket socket;
    private StreamObservable observable;
    private StreamObservable internaObservable;

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
        if (arg.toString().startsWith("control_socket")) {
            controlSocket = Integer.parseInt(arg.toString().split(":")[1]);
        }
    }

    serverNewUsers(Socket socket, ThreadPool threadPool, StreamObservable observable,
            StreamObservable internaObservable) {
        this.socket = socket;
        this.threadPool = threadPool;
        this.observable = observable;
        this.internaObservable = internaObservable;
    }

    @Override
    public void run() {
        internaObservable.addObserver(this);
        controlSocket = internaObservable.getControlPort();
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(controlSocket + ";");
            socket.close();

        } catch (Exception e) {
            System.out.println("Socket " + socket + " perdido");
        }
    }

}