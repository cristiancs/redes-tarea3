package streaming;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import streaming.StreamObservable;

/**
 * Server
 */
public class StreamingServer implements Observer {
    ThreadPool threadPool;
    StreamObservable observable;
    StreamObservable internaObservable;
    Integer controlPort;

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

    public void start() {

        // Iniciamos el threadPool y los threads necesarios para la consola y el
        // servidor en si
        this.threadPool = new ThreadPool(20, 8);
        this.observable = new StreamObservable();
        this.internaObservable = new StreamObservable();
        this.observable.addObserver(this);
        try {
            this.threadPool
                    .submitTask(new serverRunStreamServer(this.threadPool, this.observable, internaObservable, 1));
            this.threadPool
                    .submitTask(new serverRunStreamServer(this.threadPool, this.observable, internaObservable, 2));
            this.threadPool.submitTask(new serverRunControlServer(this.threadPool, this.observable, internaObservable));
            this.threadPool.submitTask(new serverRunNewUserServer(this.threadPool, this.observable, internaObservable));
            this.threadPool.submitTask(new serverConsole(this.observable, internaObservable));
            this.threadPool.submitTask(new VideoHandler(this.observable, internaObservable));

        } catch (Exception e) {
            System.out.println("Can't create threads");
        }

    }
}