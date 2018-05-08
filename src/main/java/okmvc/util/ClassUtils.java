package okmvc.util;

import okmvc.exception.InitializingException;
import okmvc.exception.ReflectionException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


// TODO refactor
public class ClassUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtils.class);

    public static Collection<File> listFiles(String packageName) {
        String classpath = getClasspath();
        String packagePath = classpath + packageName.replace(".", "/");
        File dir = new File(packagePath);
        if (dir.isFile()) {
            throw new InitializingException("config error: invalid root.package config");
        }
        Collection<File> files = FileUtils.listFiles(dir, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".class")) return true;
                else return false;
            }

            @Override
            public boolean accept(File file, String s) {
                return false;
            }
        }, TrueFileFilter.INSTANCE);

        return files;
    }


    public static List<String> listClassNames(String packageName) {
        Collection<File> files = listFiles(packageName);
        String classpath = getClasspath();
        List<String> classNames = new ArrayList<>(files.size());
        for (File file : files) {
            String className = file.getAbsolutePath().replace(classpath, "")
                    .replace("/", ".").replace(".class", "");
            classNames.add(className);
        }
        return classNames;
    }

    public static String getClasspath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Object newInstance(Class<?> clazz) {
        Object object;
        try {
            object = clazz.newInstance();
        } catch (Exception e) {
            throw new ReflectionException("cannot instantiate class " + clazz.getName());
        }
        return object;
    }

    private ClassUtils() {
        throw new UnsupportedOperationException();
    }
}
