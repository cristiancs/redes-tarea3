package streaming;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;

public class serverNewUsers implements Runnable {
    ThreadPool threadPool;
    private Socket socket;

    serverNewUsers(Socket socket, ThreadPool threadPool) {
        this.socket = socket;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Integer listenPort = 10000;
            while (true) {
                try (ServerSocket listener = new ServerSocket(listenPort)) {
                    out.println(listenPort + ";");
                    socket.close();

                    this.threadPool.submitTask(new serverControlSocket(listener.accept()));

                    break;

                } catch (Exception e) {
                    listenPort += 1;
                }
            }

        } catch (Exception e) {
            System.out.println("Socket " + socket + " perdido");
        }
    }

}