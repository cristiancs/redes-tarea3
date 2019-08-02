package streaming;

import java.io.File;

public class serverConsole implements Runnable {
    private StreamObservable observable;

    serverConsole(StreamObservable observable) {
        this.observable = observable;
    }

    @Override
    public void run() {
        String inConsole = "";

        while (true) {
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
                inConsole = "prueba...25.mp4";// System.console().readLine();
                File tempFile = new File("./media/" + inConsole);
                boolean exists = tempFile.exists();
                if (exists) {
                    String parts[] = inConsole.split("\\.\\.\\.");
                    parts = parts[1].split("\\.");
                    String fps = parts[0];
                    this.observable.cambiarMensaje("streaming " + fps);
                    System.out.println("Ingrese detener para detener la transmisión");
                    inConsole = System.console().readLine();
                    while (!inConsole.equals("detener")) {
                        inConsole = System.console().readLine();
                    }
                    this.observable.cambiarMensaje("stop");

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