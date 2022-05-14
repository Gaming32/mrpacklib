package io.github.gaming32.mrpacklib.packindex;

import com.google.gson.annotations.SerializedName;

import io.github.gaming32.mrpacklib.util.GsonHelper;

public final class PackDependencies {
    private String minecraft;
    private String forge;
    @SerializedName("fabric-loader")
    private String fabricLoader;
    @SerializedName("quilt-loader")
    private String quiltLoader;

    public PackDependencies validate() throws IllegalArgumentException {
        return this;
    }

    public String getMinecraft() {
        return minecraft;
    }

    public String getForge() {
        return forge;
    }

    public String getFabricLoader() {
        return fabricLoader;
    }

    public String getQuiltLoader() {
        return quiltLoader;
    }

    @Override
    public String toString() {
        return GsonHelper.GSON.toJson(this);
    }
}
