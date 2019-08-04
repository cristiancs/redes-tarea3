package streaming;

import java.util.HashMap;
import java.util.Observable;

public class StreamObservable extends Observable {
    String mensaje;
    Integer controlPort = 0;
    Integer stream1port = 0;
    Integer stream2port = 0;
    String stream1data = "";
    String stream2data = "";
    HashMap<Integer, String> inData = new HashMap<Integer, String>();

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

    public String getStreamData(Integer id) {
        if (id == 1) {
            return stream1data;
        }
        return stream2data;
    }

    public String getNStreamData(Integer id) {
        return inData.get(id);
    }

    public void setNStreamData(Integer id, String data) {
        // System.out.println("id" + id);
        if (inData.containsKey(id - 1)) {
            inData.put(id - 1, "");
        }
        inData.put(id - 1, data);

    }

    public void sendStream(String stream, Integer channel) {

        if (channel == 1) {
            stream1data = stream;
        } else {
            stream2data = stream;
        }
        setChanged();
        notifyObservers(channel);
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
        setChanged();
        notifyObservers(mensaje);
    }
}
