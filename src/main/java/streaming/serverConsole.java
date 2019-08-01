package streaming;

public class serverConsole implements Runnable {
    @Override
    public void run() {
        String inConsole = "";
        System.out.println(
                "Seleccione un comando \n1) Mostrar videos disponibles \n2) Reproducir Video\n3) Apagar el Servidor");
        while (true) {
            inConsole = System.console().readLine();
            System.out.println(inConsole);
        }
    }

}