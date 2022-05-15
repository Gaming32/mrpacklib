package io.github.gaming32.mrpacklib.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class GsonHelper {
    public static final Gson GSON = new GsonBuilder()
        .registerTypeHierarchyAdapter(byte[].class, new TypeAdapter<byte[]>() {
            @Override
            public void write(JsonWriter out, byte[] value) throws IOException {
                out.value(toHexString(value));
            }

            @Override
            public byte[] read(JsonReader in) throws IOException {
                return fromHexString(in.nextString());
            }
        })
        .setPrettyPrinting()
        .create();

    private GsonHelper() {
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static String toHexString(byte[] arr) {
        StringBuilder result = new StringBuilder(arr.length << 1);
        for (byte v : arr) {
            result.append(HEX_CHARS[(v & 0xff) >> 4]);
            result.append(HEX_CHARS[v & 0xf]);
        }
        return result.toString();
    }

    public static byte[] fromHexString(String str) {
        if ((str.length() & 1) != 0) {
            throw new IllegalArgumentException("Odd length hex string");
        }
        byte[] result = new byte[str.length() >> 1];
        for (int i = 0; i < result.length; i++) {
            int j = i << 1;
            result[i] = (byte)(
                (Character.digit(str.charAt(j), 16) << 4) |
                Character.digit(str.charAt(j + 1), 16)
            );
        }
        return result;
    }

    public static <T> T deserialize(Reader json, Class<T> clazz) {
        return postDeserialize(GSON.fromJson(json, clazz));
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return postDeserialize(GSON.fromJson(json, clazz));
    }

    private static <T> T postDeserialize(T result) {
        try {
            Method validate = result.getClass().getDeclaredMethod("validate");
            validate.invoke(result);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e.getCause();
            }
            if (e.getCause() instanceof Error) {
                throw (Error)e.getCause();
            }
            throw new RuntimeException(e.getCause());
        } catch (ReflectiveOperationException e) {
        }
        return result;
    }
}
