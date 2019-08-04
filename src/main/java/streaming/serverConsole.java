package streaming;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class serverConsole implements Observer, Runnable {
    private StreamObservable observable;
    private StreamObservable internaObservable;
    private Boolean pararPlay;

    serverConsole(StreamObservable observable, StreamObservable internaObservable) {
        this.observable = observable;
        this.internaObservable = internaObservable;
    }

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = arg.toString();
        if (mensaje.equals("detener")) {
            this.pararPlay = true;
        }
    }

    @Override
    public void run() {
        String inConsole = "";

        while (true) {
            pararPlay = false;
            System.out.println(
                    "Seleccione un comando \n1) Mostrar videos disponibles \n2) Reproducir Video\n3) Apagar el Servidor");
            inConsole = System.console().readLine();
            if (inConsole.equals("1")) {
                File curDir = new File("./media");
                File[] filesList = curDir.listFiles();
                for (File f : filesList) {
                    System.out.println(f.getName());
                }
            } else if (inConsole.equals("2")) {
                System.out.println("Ingrese Nombre video");
                inConsole = System.console().readLine();
                File tempFile = new File("./media/" + inConsole);
                boolean exists = tempFile.exists();
                if (exists) {
                    String parts[] = inConsole.split("\\.\\.\\.");
                    parts = parts[1].split("\\.");
                    String fps = parts[0];
                    this.internaObservable.cambiarMensaje("set_file ./media/" + inConsole);
                    this.observable.cambiarMensaje("streaming " + fps);
                    System.out.println("Ingrese detener para detener la transmisión");
                    inConsole = System.console().readLine();

                    while (!inConsole.equals("detener") && !this.pararPlay) {
                        inConsole = System.console().readLine();
                    }
                    this.observable.cambiarMensaje("stop");
                    this.observable.cambiarMensaje("idle");

                } else {
                    System.out.println("Video no disponible");
                }

            } else if (inConsole.equals("3")) {
                // To Do: should close connections
                this.observable.cambiarMensaje("close");
                break;
            } else {
                System.out.println("Comando no reconocido");
            }

        }
    }

}