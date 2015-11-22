package resource;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Properties;
import java.io.IOException;
/**
 * Created by uschsh on 22.11.15.
 */
public class Configuration {
    private static final String CONF_DIR = "src/main/resources/cfg/";
    @NotNull private Map<String, String> serverProperties = new HashMap<>();

    public Configuration(String propertiesFile){
        try (final FileInputStream fis = new FileInputStream(CONF_DIR + propertiesFile)) {
            final Properties properties = new Properties();
            properties.load(fis);

            serverProperties.put("port", properties.getProperty("server.port"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return Integer.parseInt(serverProperties.get("port"));
    }

}
