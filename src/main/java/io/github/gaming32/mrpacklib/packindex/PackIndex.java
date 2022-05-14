package io.github.gaming32.mrpacklib.packindex;

import java.util.List;

import io.github.gaming32.mrpacklib.util.GsonHelper;

public final class PackIndex {
    private int formatVersion;
    private String game;
    private String versionId;
    private String name;
    private String summary;
    private List<PackFile> files;
    private PackDependencies dependencies;

    public PackIndex validate() throws IllegalArgumentException {
        if (formatVersion != 1) {
            throw new IllegalArgumentException("formatVersion != 1");
        }
        if (!"minecraft".equals(game)) {
            throw new IllegalArgumentException("game != \"minecraft\"");
        }
        if (versionId == null) {
            throw new IllegalArgumentException("missing versionId");
        }
        if (name == null) {
            throw new IllegalArgumentException("missing name");
        }
        if (files == null) {
            throw new IllegalArgumentException("missing files");
        }
        for (PackFile file : files) {
            file.validate();
        }
        if (dependencies == null) {
            throw new IllegalArgumentException("missing dependencies");
        }
        dependencies.validate();
        return this;
    }

    public int getFormatVersion() {
        return formatVersion;
    }

    public String getGame() {
        return game;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public List<PackFile> getFiles() {
        return files;
    }

    public PackDependencies getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return GsonHelper.GSON.toJson(this);
    }
}
