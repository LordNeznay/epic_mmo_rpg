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
public final class ServerConfiguration {
    private static final String CONF_DIR = "src/main/resources/cfg/";
    @NotNull private Map<String, String> serverProperties = new HashMap<>();

    private static ServerConfiguration s_instance = new ServerConfiguration("config.properties");

    public static ServerConfiguration getInstance(){
        return s_instance;
    }

    private ServerConfiguration(String propertiesFile){
        try (final FileInputStream fis = new FileInputStream(CONF_DIR + propertiesFile)) {
            final Properties properties = new Properties();
            properties.load(fis);

            serverProperties.put("port", properties.getProperty("server.port"));
            serverProperties.put("maxAmountGameMap", properties.getProperty("server.maxAmountGameMap"));

            serverProperties.put("stepTime", properties.getProperty("mechanics.stepTime"));
            serverProperties.put("playerToStart", properties.getProperty("mechanics.playerToStart"));
            serverProperties.put("amountPlayerInCommand", properties.getProperty("mechanics.amountPlayerInCommand"));

            serverProperties.put("pointsToWin", properties.getProperty("flag.pointsToWin"));
            serverProperties.put("delayOnePoint", properties.getProperty("flag.delayOnePoint"));
            serverProperties.put("captureTime", properties.getProperty("flag.captureTime"));

            serverProperties.put("servletResponseTime", properties.getProperty("frontend.servletResponseTime"));

            serverProperties.put("maxHitPoints", properties.getProperty("entity.maxHitPoints"));
            serverProperties.put("moveDelay", properties.getProperty("entity.moveDelay"));
            serverProperties.put("abilityDelay", properties.getProperty("entity.abilityDelay"));

            serverProperties.put("dialect", properties.getProperty("hibernate.dialect"));
            serverProperties.put("driver_class", properties.getProperty("hibernate.connection.driver_class"));
            serverProperties.put("connection_url", properties.getProperty("hibernate.connection.url"));
            serverProperties.put("connection_username", properties.getProperty("hibernate.connection.username"));
            serverProperties.put("connection_password", properties.getProperty("hibernate.connection.password"));
            serverProperties.put("show_sql", properties.getProperty("hibernate.show_sql"));
            serverProperties.put("hbm2ddl_auto", properties.getProperty("hibernate.hbm2ddl.auto"));
            serverProperties.put("hbm2ddl_auto_test", properties.getProperty("hibernate.hbm2ddl.auto_test"));
            serverProperties.put("connection_url_test", properties.getProperty("hibernate.connection.url_test"));



            getAbilityConfig(properties, "OrdinaryHit");
            getAbilityConfig(properties, "OrdinaryHealing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAbilityConfig(Properties properties, String abilityName){
        serverProperties.put(abilityName + ".range", properties.getProperty("ability." + abilityName + ".range"));
        serverProperties.put(abilityName + ".cooldown", properties.getProperty("ability." + abilityName + ".cooldown"));
        serverProperties.put(abilityName + ".minEffect", properties.getProperty("ability." + abilityName + ".minEffect"));
        serverProperties.put(abilityName + ".maxEffect", properties.getProperty("ability." + abilityName + ".maxEffect"));
    }

    public int getAbilityRange(String abilityName){
        return Integer.parseInt(serverProperties.get(abilityName + ".range"));
    }

    public int getAbilityCooldown(String abilityName){
        return Integer.parseInt(serverProperties.get(abilityName + ".cooldown"));
    }

    public int getAbilityMinEffect(String abilityName){
        return Integer.parseInt(serverProperties.get(abilityName + ".minEffect"));
    }

    public int getAbilityMaxEffect(String abilityName){
        return Integer.parseInt(serverProperties.get(abilityName + ".maxEffect"));
    }

    public int getPort() {
        return Integer.parseInt(serverProperties.get("port"));
    }

    public int getMaxAmountGameMap() {
        return Integer.parseInt(serverProperties.get("maxAmountGameMap"));
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

    public int getServletResponseTime(){
        return Integer.parseInt(serverProperties.get("servletResponseTime"));
    }

    public String getDialect(){
        return serverProperties.get("dialect");
    }

    public String getDriverClass(){
        return serverProperties.get("driver_class");
    }

    public String getConnectionUrl(){
        return serverProperties.get("connection_url");
    }

    public String getConnectionUrlTest(){
        return serverProperties.get("connection_url_test");
    }

    public String getConnectionUsername(){
        return serverProperties.get("connection_username");
    }

    public String getConnectionPassword(){
        return serverProperties.get("connection_password");
    }

    public String getShowSql(){
        return serverProperties.get("show_sql");
    }

    public String getHbm2ddAuto(){
        return serverProperties.get("hbm2ddl_auto");
    }

    public String getHbm2ddAutoTest(){
        return serverProperties.get("hbm2ddl_auto_test");
    }
}
