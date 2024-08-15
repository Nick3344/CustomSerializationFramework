package com.custom.serialization.compress;

import java.io.IOException;

public interface Compressor {
    byte[] compress(byte[] data) throws IOException;
    byte[] decompress(byte[] data) throws IOException;
}
