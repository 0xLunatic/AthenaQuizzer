package lunatic.athenaquizzer.event;

import lunatic.athenaquizzer.Main;
import lunatic.athenaquizzer.manager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EventListener implements Listener {

    private final Main plugin;
    private final FileManager fileManager;
    private final Map<Player, Integer> attempts = new HashMap<>();
    private final Map<Player, String> currentAnswers = new HashMap<>();
    private String currentQuestion = "";
    private String correctAnswer = "";

    private List<QuestionAndAnswer> quizQuestions = new ArrayList<>();

    public EventListener(Main plugin) {
        this.plugin = plugin;
        this.fileManager = new FileManager(plugin);
        loadQuestionsFromYAML();
    }

    private void loadQuestionsFromYAML() {
        FileConfiguration questionConfig = fileManager.loadCustomConfig("question_and_answer.yml");
        List<Map<?, ?>> questions = questionConfig.getMapList("questions");

        for (Map<?, ?> questionData : questions) {
            String question = (String) questionData.get("question");
            String answer = (String) questionData.get("answer");
            quizQuestions.add(new QuestionAndAnswer(question, answer));
        }
    }

    private void executeRewardCommand(Player winner) {
        String rewardCommand = fileManager.getString("rewards.command");
        rewardCommand = rewardCommand.replace("<player>", winner.getName());
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), rewardCommand);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String answer = event.getMessage().trim().toLowerCase();

        if (!currentAnswers.containsKey(player)) {
            return;
        }

        event.setCancelled(true);

        if (answer.equals(correctAnswer)) {
            player.sendMessage(ChatColor.GREEN + fileManager.getString("quiz.correct_message"));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§a§l" + player.getName() + " §fjust won the §d§lQuiz§f!");
            Bukkit.broadcastMessage("");
            executeRewardCommand(player);
            currentAnswers.remove(player);
            for (Player players : Bukkit.getOnlinePlayers()) {
                Sound sound = Sound.valueOf(plugin.getConfig().getString("sound.quiz_answered.sound"));
                float volume = (float) plugin.getConfig().getDouble("sound.quiz_answered.volume");
                float pitch = (float) plugin.getConfig().getDouble("sound.quiz_answered.pitch");
                players.playSound(player, sound, volume, pitch);
            }
        } else {
            int attemptsLeft = attempts.getOrDefault(player, 1);
            if (attemptsLeft == fileManager.getInt("quiz.max_attempts")) {
                player.sendMessage(ChatColor.RED + fileManager.getString("quiz.reveal_answer_message").replace("{answer}", correctAnswer));
                currentAnswers.remove(player);
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§c§l" + player.getName() + " §cfailed to answer the §c§lQuiz§f!");
                Bukkit.broadcastMessage("");
                for (Player players : Bukkit.getOnlinePlayers()) {
                    Sound sound = Sound.valueOf(plugin.getConfig().getString("sound.quiz_failed.sound"));
                    float volume = (float) plugin.getConfig().getDouble("sound.quiz_failed.volume");
                    float pitch = (float) plugin.getConfig().getDouble("sound.quiz_failed.pitch");
                    players.playSound(player, sound, volume, pitch);
                }
            } else {
                attempts.put(player, attemptsLeft + 1);
                player.sendMessage(ChatColor.RED + fileManager.getString("quiz.incorrect_message"));
                Sound sound = Sound.valueOf(plugin.getConfig().getString("sound.quiz_attempt.sound"));
                float volume = (float) plugin.getConfig().getDouble("sound.quiz_attempt.volume");
                float pitch = (float) plugin.getConfig().getDouble("sound.quiz_attempt.pitch");
                player.playSound(player, sound, volume, pitch);
            }
        }
    }

    public void startInstantQuiz() {
        if (!quizQuestions.isEmpty()) {
            QuestionAndAnswer randomQuestion = quizQuestions.get(new Random().nextInt(quizQuestions.size()));
            currentQuestion = randomQuestion.getQuestion();
            correctAnswer = randomQuestion.getAnswer();

            Bukkit.getServer().broadcastMessage("");
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Quiz Time! " + currentQuestion);
            Bukkit.getServer().broadcastMessage("");

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                currentAnswers.put(player, correctAnswer);
                Sound sound = Sound.valueOf(plugin.getConfig().getString("sound.quiz_start.sound"));
                float volume = (float) plugin.getConfig().getDouble("sound.quiz_start.volume");
                float pitch = (float) plugin.getConfig().getDouble("sound.quiz_start.pitch");
                player.playSound(player, sound, volume, pitch);
            }
        }
    }

    public void startQuizInterval() {
        int interval = fileManager.getInt("quiz.interval_seconds");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!quizQuestions.isEmpty()) {
                    QuestionAndAnswer randomQuestion = quizQuestions.get(new Random().nextInt(quizQuestions.size()));
                    currentQuestion = randomQuestion.getQuestion();
                    correctAnswer = randomQuestion.getAnswer();

                    Bukkit.getServer().broadcastMessage("");
                    Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Quiz Time! " + currentQuestion);
                    Bukkit.getServer().broadcastMessage("");

                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        currentAnswers.put(player, correctAnswer);
                        Sound sound = Sound.valueOf(plugin.getConfig().getString("sound.quiz_start.sound"));
                        float volume = (float) plugin.getConfig().getDouble("sound.quiz_start.volume");
                        float pitch = (float) plugin.getConfig().getDouble("sound.quiz_start.pitch");
                        player.playSound(player, sound, volume, pitch);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval * 20L);
    }

    private static class QuestionAndAnswer {
        private final String question;
        private final String answer;

        public QuestionAndAnswer(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
