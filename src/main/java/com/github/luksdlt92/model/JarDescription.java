package com.github.luksdlt92.model;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JarDescription {

    private final File mFile;
    private final String mClassPath;

    public JarDescription(@NotNull  File file, @NotNull String classPath) {
        mFile = file;
        mClassPath = classPath;
    }

    public File getFile() {
        return mFile;
    }

    public String getClassName() {
        return mClassPath;
    }
}
