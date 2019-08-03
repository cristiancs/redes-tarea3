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

    public String DecodeBase64ToString(String message) {
        byte[] decoded = Base64.getDecoder().decode(message);
        return new String(decoded);
    }

    public byte[] DecodeBase64ToByteArray(String message) {
        byte[] decoded = Base64.getDecoder().decode(message);
        return decoded;
    }
}