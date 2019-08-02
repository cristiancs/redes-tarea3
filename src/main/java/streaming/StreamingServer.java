package streaming;

import streaming.StreamObservable;
import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

/**
 * Server
 */
public class StreamingServer {
    ThreadPool threadPool;
    StreamObservable observable;
    StreamObservable internaObservable;
    Integer controlPort;

    public void start() {

        // Iniciamos el threadPool y los threads necesarios para la consola y el
        // servidor en si
        this.threadPool = new ThreadPool(20, 8);
        this.observable = new StreamObservable();
        this.internaObservable = new StreamObservable();

        try {
            this.threadPool.submitTask(new serverRunControlServer(this.threadPool, this.observable, internaObservable));
            this.threadPool.submitTask(new serverRunNewUserServer(this.threadPool, this.observable, internaObservable));
            this.threadPool.submitTask(new serverConsole(this.observable, internaObservable));

        } catch (Exception e) {
            System.out.println("Can't create threads");
        }

    }
}