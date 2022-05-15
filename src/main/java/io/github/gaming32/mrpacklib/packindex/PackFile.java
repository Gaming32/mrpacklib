package io.github.gaming32.mrpacklib.packindex;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

    private static final Set<String> ALLOWED_HOSTS = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    static {
        ALLOWED_HOSTS.addAll(Arrays.asList(
            "cdn.modrinth.com",
            "edge.forgecdn.net",
            "media.forgecdn.net",
            "github.com",
            "raw.githubusercontent.com"
        ));
    }

    private String path;
    private Map<String, byte[]> hashes;
    private FileEnv env;
    private List<URL> downloads;
    private long fileSize = -1;

    public PackFile validate() throws IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException("missing path");
        }
        if (hashes == null) {
            throw new IllegalArgumentException("missing hashes");
        }
        if (hashes.containsValue(null)) {
            throw new IllegalArgumentException("hashes contains null hash");
        }
        if (!hashes.containsKey("sha1")) {
            throw new IllegalArgumentException("missing hashes.sha1");
        }
        if (hashes.get("sha1").length != 20) {
            throw new IllegalArgumentException("hashes.sha1.length != 20");
        }
        if (!hashes.containsKey("sha512")) {
            throw new IllegalArgumentException("missing hashes.sha512");
        }
        if (hashes.get("sha512").length != 64) {
            throw new IllegalArgumentException("hashes.sha512.length != 64");
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
        for (URL download : downloads) {
            if (download == null) {
                throw new IllegalArgumentException("null download");
            }
            if (!download.getProtocol().equals("https")) {
                throw new IllegalArgumentException("download.protocol != \"https\"");
            }
            if (!ALLOWED_HOSTS.contains(download.getHost())) {
                throw new IllegalArgumentException("\"" + download.getHost() + "\" not an allowed host");
            }
        }
        if (fileSize == -1) {
            throw new IllegalArgumentException("missing fileSize");
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

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return GsonHelper.GSON.toJson(this);
    }
}
