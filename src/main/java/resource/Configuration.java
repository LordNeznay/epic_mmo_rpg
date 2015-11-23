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
public final class Configuration {
    private static final String CONF_DIR = "src/main/resources/cfg/";
    @NotNull private Map<String, String> serverProperties = new HashMap<>();

    private static Configuration s_instance = new Configuration("config.properties");

    public static Configuration getInstance(){
        return s_instance;
    }

    private Configuration(String propertiesFile){
        try (final FileInputStream fis = new FileInputStream(CONF_DIR + propertiesFile)) {
            final Properties properties = new Properties();
            properties.load(fis);

            serverProperties.put("port", properties.getProperty("server.port"));

            serverProperties.put("stepTime", properties.getProperty("mechanics.stepTime"));
            serverProperties.put("playerToStart", properties.getProperty("mechanics.playerToStart"));
            serverProperties.put("amountPlayerInCommand", properties.getProperty("mechanics.amountPlayerInCommand"));

            serverProperties.put("pointsToWin", properties.getProperty("flag.pointsToWin"));
            serverProperties.put("delayOnePoint", properties.getProperty("flag.delayOnePoint"));
            serverProperties.put("captureTime", properties.getProperty("flag.captureTime"));

            serverProperties.put("maxHitPoints", properties.getProperty("entity.maxHitPoints"));
            serverProperties.put("moveDelay", properties.getProperty("entity.moveDelay"));
            serverProperties.put("abilityDelay", properties.getProperty("entity.abilityDelay"));

            getAbilityConfig(properties, "OrdinaryHit");
            getAbilityConfig(properties, "OrdinaryHealing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAbilityConfig(Properties properties, String abilityName){
        serverProperties.put(abilityName + ".range", properties.getProperty("ability." + abilityName + ".range"));
        serverProperties.put(abilityName + ".cooldown", properties.getProperty("ability." + abilityName + ".cooldown"));
    }

    public int getAbilityRange(String abilityName){
        return Integer.parseInt(serverProperties.get(abilityName + ".range"));
    }

    public int getAbilityCooldown(String abilityName){
        return Integer.parseInt(serverProperties.get(abilityName + ".cooldown"));
    }

    public int getPort() {
        return Integer.parseInt(serverProperties.get("port"));
    }

    public int getPlayerToStart(){
        return Integer.parseInt(serverProperties.get("playerToStart"));
    }

    public int getStepTime(){
        return Integer.parseInt(serverProperties.get("stepTime"));
    }

    public int getAmountPlayerInCommand(){
        return Integer.parseInt(serverProperties.get("amountPlayerInCommand"));
    }

    public int getPointsToWin(){
        return Integer.parseInt(serverProperties.get("pointsToWin"));
    }

    public int getDelayOnePoint(){
        return Integer.parseInt(serverProperties.get("delayOnePoint"));
    }

    public int getCaptureTime(){
        return Integer.parseInt(serverProperties.get("captureTime"));
    }

    public int getMaxHitPoints(){
        return Integer.parseInt(serverProperties.get("maxHitPoints"));
    }

    public int getMoveDelay(){
        return Integer.parseInt(serverProperties.get("moveDelay"));
    }

    public int getAbilityDelay(){
        return Integer.parseInt(serverProperties.get("abilityDelay"));
    }
}
