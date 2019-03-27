package it.polimi.deib.rsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public PropertiesReader() {

    }

    public Properties getProperties() {

        InputStream defaultPropertiesFile = PropertiesReader.class.getResourceAsStream("/default.properties");
        Properties defaultProperties = new Properties();
        try {
            defaultProperties.load(defaultPropertiesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Properties properties = new Properties(defaultProperties);

        try {
            FileInputStream userPropertiesFile = new FileInputStream("./user.properties");
            properties.load(userPropertiesFile);
        } catch (IOException | NullPointerException e) {
            System.out.println("User's properties not found; loading defaults...");
        }

        return properties;
    }

}
