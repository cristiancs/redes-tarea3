package streaming;

import java.util.Observable;

public class StreamObservable extends Observable {
    String mensaje;
    Integer controlPort = 0;
    Integer stream1port = 0;
    Integer stream2port = 0;

    public StreamObservable() {
        mensaje = "idle";
    }

    public String getMensaje() {
        return mensaje;
    }

    public Integer getControlPort() {
        return controlPort;
    }

    public Integer getStream1Port() {
        return stream1port;
    }

    public Integer getStream2Port() {
        return stream2port;
    }

    public void cambiarMensaje(String m) {
        if (m.startsWith("control_socket:")) {
            controlPort = Integer.parseInt(m.split(":")[1]);
        }
        if (m.startsWith("stream_socket:")) {
            if (m.split(":")[1].equals("1")) {
                stream1port = Integer.parseInt(m.split(":")[2]);
            } else {
                stream2port = Integer.parseInt(m.split(":")[2]);
            }
        }
        mensaje = m;
        // Marcamos el objeto observable como objeto que ha cambiado
        setChanged();
        // Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(mensaje);
        // notifyObservers(); Este metodo solo notifica que hubo cambios en el objeto
    }
}
