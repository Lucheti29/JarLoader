package com.github.luksdlt92;

import com.github.luksdlt92.exception.InvalidJarLoadException;
import com.github.luksdlt92.model.JarDescription;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JarLoader {

    private final Logger mLogger = Logger.getLogger(getClass().getSimpleName());
    private final String mParentClassName;

    private JarLoader(List<JarDescription> list, String parentClassName) {
        mParentClassName = parentClassName;

        for (JarDescription description : list) {
            loadJar(description);
        }
    }

    private void loadJar(JarDescription description) {
        try {
            URL classUrl;
            URL[] classUrls;

            try {
                classUrl = description.getFile().toURI().toURL();
                classUrls = new URL[] { classUrl };
            } catch (MalformedURLException ex) {
                throw new InvalidJarLoadException("File URL malformed " + description.getFile().getName());
            }

            URLClassLoader child = new URLClassLoader(classUrls, this.getClass().getClassLoader());
            Class classToLoad;

            try {
                classToLoad = Class.forName(description.getClassName(), true, child);
            } catch (ClassNotFoundException ex) {
                throw new InvalidJarLoadException("Cannot find main class " + description.getClassName());
            }

            if (!classToLoad.getSuperclass().getSimpleName().equalsIgnoreCase(mParentClassName)) {
                throw new InvalidJarLoadException("Wrong inheritance for " + classToLoad.getSimpleName());
            }

            try {
                classToLoad.newInstance();
            } catch (InstantiationException ex) {
                throw new InvalidJarLoadException("The class " + classToLoad.getSimpleName() + " cannot be instantiated");
            } catch (IllegalAccessException ex) {
                throw new InvalidJarLoadException("The class " + classToLoad.getSimpleName() + " cannot be accessed");
            }
        } catch (InvalidJarLoadException ex) {
            mLogger.log(Level.SEVERE, ex.getMessage());
        }

    }

    public static void main(String [] args) {
        System.out.println("Hello world");

        List<JarDescription> list = new LinkedList<JarDescription>();
        list.add(new JarDescription(new File("ExamplePlugin.jar"), "com.github.luksdlt92.ExamplePlugin"));

        new JarLoader(list, "JavaPlugin");
    }
}
