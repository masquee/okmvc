package okmvc.util;

import okmvc.exception.InitializingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyUtils.class);

    private static final String CONFIG_FILENAME = "config.properties";

    private static final String ROOT_PACKAGE_PROPERTY = "root.package";

    private static Properties loadConfigFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream(CONFIG_FILENAME);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new InitializingException("error occurs when loading config file : cannot find config.properties");
        }
        return properties;
    }

    public static String getRootPackageName() {
        String rootPackage = loadConfigFile().getProperty(ROOT_PACKAGE_PROPERTY);
        if (rootPackage == null) {
            throw new InitializingException("cannot find root.package property from config file");
        }
        return rootPackage;
    }

    private PropertyUtils() {
        throw new UnsupportedOperationException();
    }
}
