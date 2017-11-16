package com.xyz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private PropertiesUtil(){}

    public static final Properties loadProps(String fileName) {
        InputStream is = null;
        Properties properties = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null)
                throw new FileNotFoundException(fileName + " not found");
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("load file failure.", e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
