package com.custom.serialization.test;

import com.custom.serialization.Serializer;
import com.custom.serialization.Deserializer;
import com.custom.serialization.compress.GZIPCompressor;
import com.custom.serialization.encrypt.AESEncryptor;

public class SerializationTest {

    public static void main(String[] args) {
        try {
            GZIPCompressor gzipCompressor = new GZIPCompressor();
            AESEncryptor aesEncryptor = new AESEncryptor("secretKey123");

            Serializer serializer = new Serializer(gzipCompressor, aesEncryptor);
            Deserializer deserializer = new Deserializer(gzipCompressor, aesEncryptor);

            TestClassV2 object1 = new TestClassV2(1, "Object 1", true, "New Field");
            System.out.println("Original Object: " + object1);

            byte[] serializedData = serializer.serialize(object1);
            System.out.println("Serialized Data: " + serializedData.length + " bytes");

            TestClassV2 deserializedObject = (TestClassV2) deserializer.deserialize(serializedData);
            System.out.println("Deserialized Object: " + deserializedObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
