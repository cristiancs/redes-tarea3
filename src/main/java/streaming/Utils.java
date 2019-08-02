package streaming;

import java.util.Base64;

public class Utils {
    public String encodeBytesToBase64String(byte[] data) {
        byte[] encoded = Base64.getEncoder().encode(data);
        return new String(encoded);
    }

    public String encodeStringToBase64String(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }
}