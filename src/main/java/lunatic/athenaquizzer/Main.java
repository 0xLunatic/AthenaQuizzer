package lunatic.athenaquizzer;

import lunatic.athenaquizzer.commands.CommandListener;
import lunatic.athenaquizzer.event.EventListener;
import lunatic.athenaquizzer.manager.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public FileManager fileManager;
    private EventListener eventListener;

    @Override
    public void onEnable() {
        // Plugin startup logic with colored logging
        getLogger().info(ChatColor.GREEN + "========================================");
        getLogger().info(ChatColor.GREEN + "AthenaQuizzer has been enabled!");
        getLogger().info(ChatColor.GREEN + "========================================");
        fileManager = new FileManager(this);
        eventListener = new EventListener(this);

        // Register events and command
        getServer().getPluginManager().registerEvents(eventListener, this);
        this.getCommand("quizzer").setExecutor(new CommandListener(this));

        // Start auto quiz
        eventListener.startQuizInterval();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic with colored logging
        getLogger().info(ChatColor.RED + "========================================");
        getLogger().info(ChatColor.RED + "AthenaQuizzer has been disabled!");
        getLogger().info(ChatColor.RED + "========================================");
    }
    public EventListener getEventListener() {
        return eventListener;
    }
    public FileManager getFileManager(){
        return fileManager;
    }
}
