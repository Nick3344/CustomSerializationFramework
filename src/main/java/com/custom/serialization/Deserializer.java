package com.custom.serialization;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Deserializer {

    private Map<Integer, Object> objectReferences = new HashMap<>();
    private int referenceId = 0;
    private Compressor compressor;
    private Encryptor encryptor;

    public Deserializer() {}

    public Deserializer(Compressor compressor, Encryptor encryptor) {
        this.compressor = compressor;
        this.encryptor = encryptor;
    }

    public CustomSerializable deserialize(byte[] data) throws IOException, ClassNotFoundException {
        if (encryptor != null) {
            try {
                data = encryptor.decrypt(data);
            } catch (Exception e) {
                throw new IOException("Decryption failed", e);
            }
        }

        if (compressor != null) {
            data = compressor.decompress(data);
        }

        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);

        return (CustomSerializable) deserializeObject(dataStream);
    }

    private Object deserializeObject(DataInputStream dataStream) throws IOException, ClassNotFoundException {
        boolean isReference = dataStream.readBoolean();
        if (isReference) {
            int refId = dataStream.readInt();
            return objectReferences.get(refId);
        }

        String className = dataStream.readUTF();
        Class<?> clazz = Class.forName(className);

        try {
            CustomSerializable object = (CustomSerializable) clazz.getDeclaredConstructor().newInstance();
            objectReferences.put(referenceId++, object);

            // Read version information
            int version = dataStream.readInt();
            System.out.println("Deserializing version: " + version);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type == int.class) {
                    field.setInt(object, dataStream.readInt());
                } else if (type == float.class) {
                    field.setFloat(object, dataStream.readFloat());
                } else if (type == double.class) {
                    field.setDouble(object, dataStream.readDouble());
                } else if (type == long.class) {
                    field.setLong(object, dataStream.readLong());
                } else if (type == boolean.class) {
                    field.setBoolean(object, dataStream.readBoolean());
                } else if (type == String.class) {
                    field.set(object, dataStream.readUTF());
                } else if (CustomSerializable.class.isAssignableFrom(type)) {
                    field.set(object, deserializeObject(dataStream));
                }
            }
            return object;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Deserialization failed", e);
        }
    }
}
