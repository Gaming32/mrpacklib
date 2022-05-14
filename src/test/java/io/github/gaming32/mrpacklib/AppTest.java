package io.github.gaming32.mrpacklib;

import java.io.IOException;
import java.util.zip.ZipFile;

import io.github.gaming32.mrpacklib.Mrpack.EnvSide;

public class AppTest {
    public static void main(String[] args) throws IOException {
        try (Mrpack pack = new Mrpack(new ZipFile("test/the-modpack.mrpack"))) {
            System.out.println(pack.getAllFiles(EnvSide.SERVER));
        }
    }
}
