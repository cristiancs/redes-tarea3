package streaming;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;

public class serverControlSocket implements Runnable {

    private Socket socket;

    serverControlSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hola desde el servidor de control");
            socket.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Socket de control " + socket + " con problemas");
        }

    }

}