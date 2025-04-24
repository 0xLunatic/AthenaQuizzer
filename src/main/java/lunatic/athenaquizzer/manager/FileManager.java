package lunatic.athenaquizzer.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    // Create or load the config
    public void setup() {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveDefaultConfig(); // Saves from jar if not exists
            Bukkit.getServer().getLogger().info("new Config YML loaded");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // Reload config from disk
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // Save current config to disk
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml!");
            e.printStackTrace();
        }
    }

    // Get config object
    public FileConfiguration getConfig() {
        return config;
    }
    // Get a value from the config with a given path
    public Object get(String path) {
        return config.get(path);
    }

    // Get a String specifically
    public String getString(String path) {
        return config.getString(path);
    }
    public FileConfiguration loadCustomConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);  // Save the file if it doesn't exist
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    // Optional: getInt, getBoolean, etc.
    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

}
