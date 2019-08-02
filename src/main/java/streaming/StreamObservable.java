package streaming;

import java.util.Observable;

public class StreamObservable extends Observable {
    String mensaje;

    public StreamObservable() {
        mensaje = "idle";
    }

    public String getMensaje() {
        return mensaje;
    }

    public void cambiarMensaje(String m) {
        mensaje = m;
        // Marcamos el objeto observable como objeto que ha cambiado
        setChanged();
        // Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(mensaje);
        // notifyObservers(); Este metodo solo notifica que hubo cambios en el objeto
    }
}
