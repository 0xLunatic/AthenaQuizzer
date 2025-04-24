package lunatic.athenaquizzer.commands;

import lunatic.athenaquizzer.Main;
import lunatic.athenaquizzer.event.EventListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor {

    private final Main plugin;

    public CommandListener(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&',
                plugin.getFileManager().getString("messages.quiz_prefix"));

        if (args.length == 0) {
            sender.sendMessage(prefix + ChatColor.WHITE + " Please use " +
                    ChatColor.RED + "/quizzer <start|reload>" +
                    ChatColor.WHITE + " or just type in chat to answer!");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (!sender.hasPermission("quizplugin.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            EventListener eventListener = plugin.getEventListener();
            eventListener.startInstantQuiz();

            sender.sendMessage(prefix + ChatColor.GREEN + " Quiz forced successfully!");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("quizplugin.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            plugin.getFileManager().reloadConfig();
            sender.sendMessage(prefix + ChatColor.AQUA + " Configuration reloaded.");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /quizzer start or /quizzer reload");
        return true;
    }
}
