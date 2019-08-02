package streaming;

import streaming.StreamObservable;
import java.net.ServerSocket;

/**
 * Server
 */
public class StreamingServer {
    ThreadPool threadPool;
    StreamObservable observable;

    public class RunNewUserServer implements Runnable {
        ThreadPool threadPool;
        private StreamObservable observable;

        RunNewUserServer(ThreadPool threadPool, StreamObservable observable) {
            this.threadPool = threadPool;
            this.observable = observable;
        }

        public void StartServer(Integer port) throws Exception {
            try (ServerSocket listener = new ServerSocket(port)) {
                System.out.println("Connections socket started on port: " + port);
                while (true) {
                    this.threadPool.submitTask(new serverNewUsers(listener.accept(), this.threadPool, this.observable));
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

    public void start() {

        // Iniciamos el threadPool y los threads necesarios para la consola y el
        // servidor en si
        this.threadPool = new ThreadPool(20, 8);
        this.observable = new StreamObservable();
        try {
            this.threadPool.submitTask(new RunNewUserServer(this.threadPool, this.observable));
            this.threadPool.submitTask(new serverConsole(this.observable));

        } catch (Exception e) {
            System.out.println("Can't create threads");
        }

    }
}