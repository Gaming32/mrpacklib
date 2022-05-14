package io.github.gaming32.mrpacklib;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.annotations.SerializedName;

import io.github.gaming32.mrpacklib.packindex.PackFile;
import io.github.gaming32.mrpacklib.packindex.PackIndex;
import io.github.gaming32.mrpacklib.util.GsonHelper;

public final class Mrpack implements AutoCloseable {
    public static enum EnvSide {
        CLIENT, SERVER
    }

    public static enum EnvCompatibility {
        @SerializedName("optional") OPTIONAL,
        @SerializedName("required") REQUIRED,
        @SerializedName("unsupported") UNSUPPORTED
    }

    private final ZipFile zipFile;
    private PackIndex packIndex;

    public Mrpack(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    public PackIndex getPackIndex() throws IOException {
        PackIndex index;
        if ((index = packIndex) == null) {
            ZipEntry indexEntry = zipFile.getEntry("modrinth.index.json");
            if (indexEntry == null) {
                throw new IllegalArgumentException("Illegal mrpack: missing modrinth.index.json");
            }
            try (Reader reader = new InputStreamReader(zipFile.getInputStream(indexEntry))) {
                packIndex = index = GsonHelper.deserialize(reader, PackIndex.class);
            }
        }
        return index;
    }

    public List<PackFile> getAllFiles() throws IOException {
        return getPackIndex().getFiles();
    }

    /**
     * Gets all mods compatible with the given side
     */
    public List<PackFile> getAllFiles(EnvSide side) throws IOException {
        List<PackFile> mods = new ArrayList<>();
        for (PackFile mod : getAllFiles()) {
            if (mod.getEnv().get(side) != EnvCompatibility.UNSUPPORTED) {
                mods.add(mod);
            }
        }
        return mods;
    }

    /**
     * Gets all mods on the given side that fit the compatibility level
     */
    public List<PackFile> getAllFiles(EnvSide side, EnvCompatibility compatibility) throws IOException {
        List<PackFile> mods = new ArrayList<>();
        for (PackFile mod : getAllFiles()) {
            if (mod.getEnv().get(side) == compatibility) {
                mods.add(mod);
            }
        }
        return mods;
    }

    public List<ZipEntry> getGlobalOverrides() {
        return getOverrides(null);
    }

    public List<ZipEntry> getOverrides(EnvSide side) {
        List<ZipEntry> entries = new ArrayList<>();
        String root = side == null
            ? "overrides/"
            : side == EnvSide.CLIENT
                ? "client-overrides/"
                : "server-overrides/";
        Enumeration<? extends ZipEntry> entryEnumeration = zipFile.entries();
        while (entryEnumeration.hasMoreElements()) {
            ZipEntry entry = entryEnumeration.nextElement();
            if (entry.getName().startsWith(root)) {
                entries.add(entry);
            }
        }
        return entries;
    }

    @Override
    public void close() throws IOException {
        zipFile.close();
    }
}
