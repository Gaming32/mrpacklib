package io.github.gaming32.mrpacklib.packindex;

import java.net.URL;
import java.util.List;
import java.util.Map;

import io.github.gaming32.mrpacklib.Mrpack.EnvCompatibility;
import io.github.gaming32.mrpacklib.Mrpack.EnvSide;
import io.github.gaming32.mrpacklib.util.GsonHelper;

public final class PackFile {
    public static final class FileEnv {
        private EnvCompatibility client;
        private EnvCompatibility server;

        public EnvCompatibility getClient() {
            return client;
        }

        public EnvCompatibility getServer() {
            return server;
        }

        public EnvCompatibility get(EnvSide side) {
            return side == EnvSide.CLIENT ? client : server;
        }
    }

    private String path;
    private Map<String, byte[]> hashes;
    private FileEnv env;
    private List<URL> downloads;

    public PackFile validate() throws IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException("missing path");
        }
        if (hashes == null) {
            throw new IllegalArgumentException("missing hashes");
        }
        if (!hashes.containsKey("sha1")) {
            throw new IllegalArgumentException("missing hashes.sha1");
        }
        if (hashes.containsValue(null)) {
            throw new IllegalArgumentException("hashes contains null hash");
        }
        if (hashes.get("sha1").length != 20) {
            throw new IllegalArgumentException("hashes.sha1.length != 20");
        }
        if (env == null) {
            env = new FileEnv();
            env.client = EnvCompatibility.REQUIRED;
            env.server = EnvCompatibility.REQUIRED;
        } else {
            if (env.getClient() == null) {
                throw new IllegalArgumentException("missing env.client");
            }
            if (env.getServer() == null) {
                throw new IllegalArgumentException("missing env.client");
            }
        }
        if (downloads == null || downloads.size() == 0) {
            throw new IllegalArgumentException("missing downloads");
        }
        if (downloads.contains(null)) {
            throw new IllegalArgumentException("null download");
        }
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, byte[]> getHashes() {
        return hashes;
    }

    public FileEnv getEnv() {
        return env;
    }

    public List<URL> getDownloads() {
        return downloads;
    }

    @Override
    public String toString() {
        return GsonHelper.GSON.toJson(this);
    }
}
