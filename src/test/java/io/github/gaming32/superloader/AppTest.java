package io.github.gaming32.superloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import io.github.gaming32.mrpacklib.PackIndex;
import io.github.gaming32.mrpacklib.util.GsonHelper;

public class AppTest {
    public static void main(String[] args) throws IOException {
        try (Reader reader = new InputStreamReader(AppTest.class.getResourceAsStream("modrinth.index.json"))) {
            System.out.println(GsonHelper.deserialize(reader, PackIndex.class));
        }
    }
}
