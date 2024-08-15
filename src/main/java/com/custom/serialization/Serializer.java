package com.custom.serialization; 

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

public class Serializer {

    private Map<Object, Integer> objectReferences = new IdentityHashMap<>();
    private int referenceId = 0;
    private Compressor compressor;
    private Encryptor encryptor;

    public Serializer() {}

    public Serializer(Compressor compressor, Encryptor encryptor) {
        this.compressor = compressor;
        this.encryptor = encryptor;
    }

    public byte[] serialize(CustomSerializable object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        serializeObject(object, dataStream);

        dataStream.flush();
        byte[] data = byteStream.toByteArray();

        if (compressor != null) {
            data = compressor.compress(data);
        }

        if (encryptor != null) {
            try {
                data = encryptor.encrypt(data);
            } catch (Exception e) {
                throw new IOException("Encryption failed", e);
            }
        }

        return data;
    }

    private void serializeObject(Object object, DataOutputStream dataStream) throws IOException {
        if (objectReferences.containsKey(object)) {
            dataStream.writeBoolean(true);
            dataStream.writeInt(objectReferences.get(object));
            return;
        }

        objectReferences.put(object, referenceId++);
        dataStream.writeBoolean(false);
        dataStream.writeUTF(object.getClass().getName());

        // Write version information
        if (object instanceof CustomSerializable) {
            dataStream.writeInt(((CustomSerializable) object).getVersion());
        }

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value instanceof Integer) {
                    dataStream.writeInt((int) value);
                } else if (value instanceof Float) {
                    dataStream.writeFloat((float) value);
                } else if (value instanceof Double) {
                    dataStream.writeDouble((double) value);
                } else if (value instanceof Long) {
                    dataStream.writeLong((long) value);
                } else if (value instanceof Boolean) {
                    dataStream.writeBoolean((boolean) value);
                } else if (value instanceof String) {
                    dataStream.writeUTF((String) value);
                } else if (value instanceof CustomSerializable) {
                    serializeObject(value, dataStream);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
