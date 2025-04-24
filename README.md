# AthenaQuizzer

AthenaQuizzer is a Minecraft plugin designed to create and manage quizzes within the game. It allows server admins to set up trivia questions that players can answer via chat. The plugin features:

- **Customizable Questions**: Quiz questions and answers are loaded from an external YAML file, allowing for easy customization.
- **Reward System**: Upon correctly answering a question, players can receive a configurable reward (e.g., in-game items or commands executed as console).
- **Sound Effects**: Customizable sound effects are triggered at various stages, including question start, correct answer, failed attempts, and quiz completion.
- **Quiz Timing**: Supports both instant quizzes and quizzes that start at regular intervals, with configurable time settings.
- **Player Attempts**: Players are given a set number of attempts to answer a question correctly, after which the correct answer is revealed.
- **Multi-Player Support**: The plugin allows multiple players to participate in quizzes simultaneously.

## Setup

1. Download the plugin and place it in the `plugins` folder of your Minecraft server.
2. Edit the `config.yml` and `question_and_answer.yml` files to configure quiz settings and questions.
3. Restart the server to apply the changes.
4. Use the `/quiz` command to start a quiz or enable interval-based quizzes.

## Commands

- `/quizzer start`: Start an instant quiz with a random question.
- `/quizzer reload`: To reload config.

## Configuration

- **config.yml**: Configure quiz settings such as the interval, sound effects, and reward commands.
- **question_and_answer.yml**: Add your trivia questions and their corresponding answers.

## Dependencies

- **Spigot** or **Paper** (Recommended for optimal performance)

## License

This plugin is released under the MIT License.

