package com.custom.serialization.encrypt;

import java.io.IOException;

public interface Encryptor {
    byte[] encrypt(byte[] data) throws IOException;
    byte[] decrypt(byte[] data) throws IOException;
}
