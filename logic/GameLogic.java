package logic;
/*
 * Name: Arshia, Anita, Oliver, Daniel
 * Date: 2025-01-17
 * Project: Guess Who ISP
 * 
 */
import java.util.*;

public class GameLogic {
    // Characters name based on how they are saved on file
    private final String[][] characters = {
        {"Barry", "Bruce B.", "Bruce W.", "Clark", "Damian", "Diana"},
        {"Dione", "Gamora", "Gwen", "Jessica", "Logan", "Matt"},
        {"Natasha", "Oliver", "Peter", "Remy", "Sam", "Scott"},
        {"Stephen", "Steve", "T'Challa", "Thor", "Tony", "Venom"}
    };
    
    //a multidimentional matrix array that contains all the answers/attributes based on the available questions.
    private final String[][] attributes = {
        {"Yes", "Brown", "Yes", "No", "No", "DC", "Male"},
        {"No", "Black", "No", "No", "No", "Marvel", "Male"},
        {"Yes", "Black", "Yes", "No", "No", "DC", "Male"},
        {"No", "Black", "Yes", "No", "No", "DC", "Male"},
        {"Yes", "Black", "Yes", "No", "No", "DC", "Male"},
        {"Yes", "Brown", "No", "No", "No", "DC", "Female"},
        {"No", "Bald!", "No", "No", "Yes", "Marvel", "Male"},
        {"No", "Magenta", "No", "No", "Yes", "Marvel", "Female"},
        {"Yes", "Blonde", "No", "No", "No", "Marvel", "Female"},
        {"No", "Black", "No", "No", "No", "Marvel", "Female"},
        {"Yes", "Black", "No", "Yes", "No", "Marvel", "Male"},
        {"Yes", "Black", "No", "No", "No", "Marvel", "Male"},
        {"No", "Orange", "No", "No", "No", "Marvel", "Female"},
        {"Yes", "Black", "Yes", "No", "No", "Marvel", "Male"},
        {"Yes", "Brown", "No", "No", "No", "Marvel", "Male"},
        {"Yes", "Brown", "No", "No", "No", "Marvel", "Male"},
        {"Yes", "Black", "No", "Yes", "No", "Marvel", "Male"},
        {"Yes", "Black", "No", "No", "No", "Marvel", "Male"},
        {"No", "Black", "Yes", "Yes", "No", "Marvel", "Male"},
        {"Yes", "Blonde", "No", "No", "No", "Marvel", "Male"},
        {"Yes", "Black", "No", "No", "No", "Marvel", "Male"},
        {"No", "Blonde", "Yes", "Yes", "No", "Marvel", "Male"},
        {"Yes", "Black", "No", "Yes", "No", "Marvel", "Male"},
        {"Yes", "Black", "No", "Yes", "Yes", "Marvel", "Male"}
    };
    
    //the list of questions saved in a array list
    public final List<String> questions = Arrays.asList(
        "Does your character wear a mask?",
        "What is your character’s hair color?",
        "Does your character wear a hood/cape?",
        "Does your character have facial hair?",
        "Is your character an alien?",
        "Is your character in DC or Marvel?",
        "Is your character male or female?"
    );

    // Game State Variables
    private final List<Integer> remainingCharacters;
    private final Set<Integer> eliminatedCharacters;
    private final Set<Integer> aiAskedQuestions;
    private int aiCharacterIndex;
    private int playerCharacterIndex;
    private boolean isPlayerTurn;

    // Constructor for the ai's character
    public GameLogic() {
        this.remainingCharacters = new ArrayList<>();
        for (int i = 0; i < characters.length * characters[0].length; i++) {
            remainingCharacters.add(i);
        }
        this.eliminatedCharacters = new HashSet<>();
        this.aiAskedQuestions = new HashSet<>();
        this.aiCharacterIndex = -1; // Not assigned initially
        this.playerCharacterIndex = -1; // Not assigned initially
        this.isPlayerTurn = true;
    }

    // Character Retrieval
    public String[] getCharacters() {
        return Arrays.stream(characters).flatMap(Arrays::stream).toArray(String[]::new);
    }

    // Player Character Selection
    public void setPlayerCharacter(String characterName) {
        for (int i = 0; i < characters.length; i++) {
            for (int j = 0; j < characters[i].length; j++) {
                if (characters[i][j].equalsIgnoreCase(characterName)) {
                    playerCharacterIndex = i * characters[0].length + j;
                    assignAICharacter();
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Character not found: " + characterName);
    }

    private void assignAICharacter() {
        Random random = new Random();
        do {
            aiCharacterIndex = random.nextInt(characters.length * characters[0].length);
        } while (aiCharacterIndex == playerCharacterIndex); // Ensure AI does not pick the player's character
    }

    public boolean isPlayerCharacterSet() {
        return playerCharacterIndex != -1;
    }

    // Character Elimination
    public void eliminateCharacter(int characterIndex) {
        if (eliminatedCharacters.contains(characterIndex)) {
            throw new IllegalStateException("Character already eliminated.");
        }
        eliminatedCharacters.add(characterIndex);
        remainingCharacters.remove((Integer) characterIndex);
    }

    // AI Questioning
    public String aiAskQuestion() {
        Random random = new Random();
        int questionIndex;
        
        do {
            questionIndex = random.nextInt(questions.size());
        } while (aiAskedQuestions.contains(questionIndex));

        aiAskedQuestions.add(questionIndex);
        String answer = getAnswerForCharacter(aiCharacterIndex, questionIndex);

        // AI eliminates characters based on the answer
        List<Integer> toRemove = new ArrayList<>();
        for (int index : remainingCharacters) {
            int row = index / characters[0].length;
            int col = index % characters[0].length;
            if (!attributes[row * characters[0].length + col][questionIndex].equalsIgnoreCase(answer)) {
                toRemove.add(index);
            }
        }
        remainingCharacters.removeAll(toRemove);

        return "AI asked: " + questions.get(questionIndex) + " | Answer: " + answer;
    }

    public String aiGuess() {
        if (remainingCharacters.size() == 1) {
            int guessIndex = remainingCharacters.get(0);
            if (guessIndex == playerCharacterIndex) {
                return "AI wins by guessing correctly!";
            } else {
                return "AI guessed " + characters[guessIndex / characters[0].length][guessIndex % characters[0].length] + " and was wrong.";
            }
        }
        return "AI is still gathering information.";
    }

    // Player Guessing
    public String playerGuess(String guessedCharacter) {
        if (aiCharacterIndex == -1) {
            return "AI character has not been assigned yet.";
        }

        String aiCharacterName = characters[aiCharacterIndex / characters[0].length][aiCharacterIndex % characters[0].length];

        if (guessedCharacter.equalsIgnoreCase(aiCharacterName)) {
            return "You win! You correctly guessed the AI's character: " + aiCharacterName;
        } else {
            return "AI wins! The AI's character was: " + aiCharacterName + ". Your guess was incorrect.";
        }
    }

    // These classes check to see if the game is over either by player loss or win.
    public boolean isGameOver() {
        return remainingCharacters.size() == 1 || playerCharacterIndex == aiCharacterIndex;
    }

    public void toggleTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    // Answer Retrieval for the players questions
    public String getAnswerForCharacter(int characterIndex, int questionIndex) {
        if (characterIndex < 0 || characterIndex >= attributes.length || questionIndex < 0 || questionIndex >= questions.size()) {
            throw new IllegalArgumentException("Invalid character or question index.");
        }
        int row = characterIndex / characters[0].length;
        int col = characterIndex % characters[0].length;
        return attributes[row * characters[0].length + col][questionIndex];
    }

    public int getAiCharacterIndex() {
        if (aiCharacterIndex == -1) {
            throw new IllegalStateException("AI character has not been assigned yet.");
        }
        return aiCharacterIndex;
    }
}
