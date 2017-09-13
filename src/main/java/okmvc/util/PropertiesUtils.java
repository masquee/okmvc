package okmvc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtils.class);

    private static final String CONFIG_FILENAME = "config.properties";

    private static final String ROOT_PACKAGE_CONFIG = "root.package";

    public static Properties loadConfigFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream(CONFIG_FILENAME);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("config error: cannot find config.properties");
        }
        return properties;
    }

    public static String getRootPackage() {
        String rootPackage = loadConfigFile().getProperty(ROOT_PACKAGE_CONFIG);
        if (rootPackage == null) {
            throw new RuntimeException("config error: cannot find root.package");
        }
        return rootPackage;
    }

    private PropertiesUtils() {
        throw new UnsupportedOperationException();
    }
}
