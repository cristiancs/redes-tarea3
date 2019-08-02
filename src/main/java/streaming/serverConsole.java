package streaming;

import java.io.File;

public class serverConsole implements Runnable {
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
                inConsole = System.console().readLine();
                File tempFile = new File("./media/" + inConsole);
                boolean exists = tempFile.exists();
                if (exists) {
                    // To Do start player
                } else {
                    System.out.println("Video no disponible");
                }

            } else if (inConsole.equals("3")) {
                // To Do: should close connections
            } else {
                System.out.println("Comando no reconocido");
            }

        }
    }

}