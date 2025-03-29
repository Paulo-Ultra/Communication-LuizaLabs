package br.com.pauloultra.desafioluizalabs.utils;

import org.apache.coyote.BadRequestException;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Utils {
    public static byte[] convertGuidStringToBytes(String guid) {
        try {
            UUID uuid = UUID.fromString(guid);
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de GUID inválido. Use o formato: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
        }
    }

    public static String convertBytesToGuidString(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits).toString();
    }
}