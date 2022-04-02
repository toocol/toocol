package com.toocol.security.hash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MurmurHash is a non-encrypt hash algorithm,
 * which is quicker and more efficient, and it has lower probability in hash collision.
 * It transfer a String to a Long by it's special computing.
 *
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/3/24 13:46
 */
public class MurmurHash {
    public static long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        long m = 0xc6a4a7935bd1e995L;
        int r = 47;
        long h = seed ^ (buf.remaining() * m);
        long k;

        while (buf.remaining() >= 8) {
            k = buf.getLong();
            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        buf.order(byteOrder);
        return h;
    }
}
