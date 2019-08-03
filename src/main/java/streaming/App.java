package streaming;

import streaming.StreamingServer;

public class App {
  Boolean isStreaming = false;

  public static void main(String[] args) {

    System.out.println(
        "----------------------------------------\nBienvenido al servicio de streaming\n----------------------------------------");

    String inConsole = "";
    while (true) {
      System.out.println("Seleccione método de funcionamiento\n\n 1) Servidor\n 2) Cliente\n");
      inConsole = System.console().readLine();
      if (inConsole.equals("1")) {
        System.out.println("Starting Server Mode 2");
        StreamingServer ss = new StreamingServer();
        ss.start();
        break;
      } else if (inConsole.equals("2")) {
        System.out.println("Starting Client Mode");
        System.out.println("Ingrese Puerto");
        inConsole = "6666";// System.console().readLine();
        Integer puerto = Integer.parseInt(inConsole);
        System.out.println("Ingrese IP");
        inConsole = "127.0.0.1";// System.console().readLine();
        String ip = inConsole;

        StreamingClient sc = new StreamingClient(ip, puerto);
        sc.start();
        break;
      }

    }

  }

}
