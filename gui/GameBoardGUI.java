package gui;
/*
 * Name: Arshia, Anita, Oliver, Daniel
 * Date: 2025-01-17
 * Project: Guess Who ISP
 * 
 */
import javax.swing.*;
import javax.sound.sampled.*;
import logic.GameLogic;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardGUI {
    
    private GameLogic gameLogic;
    private int selectedCharacterIndex = -1;
    private JButton[] characterButtons;
    private JLabel timerLabel;
    private int timeRemaining = 300; // 5 minutes in seconds
    private Timer timer;

    // This constructor calls the game logic class and starts the game by implementing the gui and the logic together.
    public GameBoardGUI(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        //this creates the GUI layout
        createGUI();
        //starts a timer that starts from 5 min and ticks down. once its over it will end the game.
        startTimer();
        //background music to make the game more dramatic
        
        playBackgroundMusic("src/sounds/background.wav");
    }

    // GUI Setup
    public void createGUI() {
        JFrame frame = setupFrame();
        JPanel mainPanel = setupMainPanel();

        JPanel characterPanel = setupCharacterPanel(frame);
        mainPanel.add(characterPanel, BorderLayout.CENTER);

        JPanel rightPanel = setupRightPanel(frame);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    //this method sets up the frame and colour and the name
    private JFrame setupFrame() {
        JFrame frame = new JFrame("Guess Who - Game Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        return frame;
    }
    

    private JPanel setupMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);
        return mainPanel;
    }
    
    //this sets up the main panel where all the characters will be placed in as buttons
    private JPanel setupCharacterPanel(JFrame frame) {
        JPanel characterPanel = new JPanel(new GridLayout(4, 6, 5, 5));
        characterPanel.setBackground(Color.DARK_GRAY);
        characterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Characters"));
        ((javax.swing.border.TitledBorder) characterPanel.getBorder()).setTitleColor(Color.WHITE);

        String[][] characterImages = getCharacterImages();
        characterButtons = new JButton[characterImages.length];

        for (int i = 0; i < characterImages.length; i++) {
            setupCharacterButton(frame, characterPanel, characterImages[i], i);
        }

        return characterPanel;
    }
    
    //this method imports all the character images and the crossed out image after picking a characterf rom the images folder.
    private String[][] getCharacterImages() {
        return new String[][]{
            {"images/barry.png", "Barry"}, {"images/bruce_b.png", "Bruce B."}, {"images/bruce_w.png", "Bruce W."},
            {"images/clark.png", "Clark"}, {"images/damian.png", "Damian"}, {"images/diana.png", "Diana"},
            {"images/dione.png", "Dione"}, {"images/gamora.png", "Gamora"}, {"images/gwen.png", "Gwen"},
            {"images/jessica.png", "Jessica"}, {"images/logan.png", "Logan"}, {"images/matt.png", "Matt"},
            {"images/natasha.png", "Natasha"}, {"images/oliver.png", "Oliver"}, {"images/peter.png", "Peter"},
            {"images/remy.png", "Remy"}, {"images/sam.png", "Sam"}, {"images/scott.png", "Scott"},
            {"images/stephen.png", "Stephen"}, {"images/steve.png", "Steve"}, {"images/t_challa.png", "T'Challa"},
            {"images/thor.png", "Thor"}, {"images/tony.png", "Tony"}, {"images/venom.png", "Venom"}
        };
    }
    
    //this methods turns all the characters into buttons.
    private void setupCharacterButton(JFrame frame, JPanel characterPanel, String[] character, int i) {
        try {
            JButton button = createCharacterButton(character);
            int characterIndex = i;

            button.addActionListener(e -> handleCharacterSelection(frame, character, characterIndex, button));
            characterPanel.add(button);
            characterButtons[i] = button;
        } catch (Exception e) {
            JLabel placeholderButton = new JLabel(character[1], SwingConstants.CENTER);
            placeholderButton.setPreferredSize(new Dimension(125, 150));
            placeholderButton.setForeground(Color.WHITE);
            characterPanel.add(placeholderButton);
        }
    }
    
    //this method will add the characters images onto the buttons.
    private JButton createCharacterButton(String[] character) throws IOException {
        ImageIcon icon = new ImageIcon(character[0]);
        Image scaledImage = icon.getImage().getScaledInstance(125, 150, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        JButton button = new JButton(icon);
        button.setBackground(Color.DARK_GRAY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setToolTipText(character[1]);

        return button;
    }

    private void handleCharacterSelection(JFrame frame, String[] character, int characterIndex, JButton button) {
        playSoundEffect("src/sounds/click.wav");
        if (selectedCharacterIndex == -1) {
            selectedCharacterIndex = characterIndex;
            gameLogic.setPlayerCharacter(character[1]);
            JOptionPane.showMessageDialog(frame, "You selected: " + character[1]);
        } else {
            disableCharacterButton(frame, button);
        }
    }
    
    //after deciding which characters to exclude, clicking the button will replace them with an x to show that they dont count.
    private void disableCharacterButton(JFrame frame, JButton button) {
        try {
            ImageIcon xIcon = new ImageIcon("images/x.png");
            Image xScaledImage = xIcon.getImage().getScaledInstance(125, 150, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(xScaledImage));
            button.setToolTipText("Character eliminated");
            button.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error loading 'X' image.");
        }
    }
    
    //this right panel contains the timer, the questions, the guess character button, and the ask button. basically everything other than the characters
    private JPanel setupRightPanel(JFrame frame) {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.DARK_GRAY);
        rightPanel.setPreferredSize(new Dimension(300, 700));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Game Info"));
        ((javax.swing.border.TitledBorder) rightPanel.getBorder()).setTitleColor(Color.WHITE);

        rightPanel.add(setupTimerPanel(), BorderLayout.NORTH);
        rightPanel.add(setupQuestionPanel(frame), BorderLayout.CENTER);
        rightPanel.add(setupButtonPanel(frame), BorderLayout.SOUTH);

        return rightPanel;
    }
    
    //this panel is the housing for the timer
    private JPanel setupTimerPanel() {
        JPanel timerPanel = new JPanel();
        timerPanel.setBackground(Color.DARK_GRAY);

        timerLabel = new JLabel("Time Left: 5:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.WHITE);
        timerPanel.add(timerLabel);

        return timerPanel;
    }

    private JPanel setupQuestionPanel(JFrame frame) {
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        questionPanel.setBackground(Color.DARK_GRAY);
        questionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Questions"));
        ((javax.swing.border.TitledBorder) questionPanel.getBorder()).setTitleColor(Color.WHITE);

        JTextArea questionList = setupQuestionList();
        JScrollPane questionScrollPane = new JScrollPane(questionList);
        questionScrollPane.setPreferredSize(new Dimension(250, 120));

        JPanel questionInputPanel = setupQuestionInputPanel(frame);

        questionPanel.add(questionScrollPane, BorderLayout.CENTER);
        questionPanel.add(questionInputPanel, BorderLayout.SOUTH);

        return questionPanel;
    }
    
    //this adds the questions from the logic class into the gui and incorpirates it in the GUI
    private JTextArea setupQuestionList() {
        JTextArea questionList = new JTextArea(6, 20);
        questionList.setEditable(false);
        questionList.setBackground(Color.DARK_GRAY);
        questionList.setForeground(Color.WHITE);

        for (int i = 0; i < gameLogic.questions.size(); i++) {
            questionList.append((i + 1) + ". " + gameLogic.questions.get(i) + "\n");
        }

        return questionList;
    }
    
    //this method has a panel that has the question input tab.
    private JPanel setupQuestionInputPanel(JFrame frame) {
        JPanel questionInputPanel = new JPanel(new BorderLayout(5, 5));
        questionInputPanel.setBackground(Color.DARK_GRAY);

        JLabel questionLabel = new JLabel("Enter question #: ");
        questionLabel.setForeground(Color.WHITE);
        JTextField questionInput = new JTextField();
        JButton askButton = new JButton("Ask");

        questionInputPanel.add(questionLabel, BorderLayout.WEST);
        questionInputPanel.add(questionInput, BorderLayout.CENTER);
        questionInputPanel.add(askButton, BorderLayout.EAST);

        askButton.addActionListener(e -> handleQuestion(frame, questionInput));

        return questionInputPanel;
    }
    
    //this method handles the questions result aspect of the game.
    private void handleQuestion(JFrame frame, JTextField questionInput) {
        playSoundEffect("src/sounds/click.wav");
        if (selectedCharacterIndex == -1) {
        	//if you have not initially selected your character it will pop up with this
            JOptionPane.showMessageDialog(frame, "Please select a character first.");
            return;
        }
        try {
            int questionIndex = Integer.parseInt(questionInput.getText()) - 1;
            if (questionIndex < 0 || questionIndex >= gameLogic.questions.size()) {
            	//if the question number chosen is not within the valid questions list, it will show this instead of an error
                JOptionPane.showMessageDialog(frame, "Invalid question number.");
                return;
            }
            int aiCharacterIndex = gameLogic.getAiCharacterIndex();
            String answer = gameLogic.getAnswerForCharacter(aiCharacterIndex, questionIndex);
            //the game will then call the game logic to get the AI's characters attributes, and based on that it will answer the question.
            JOptionPane.showMessageDialog(frame, "AI's answer: " + answer);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid question number.");
        }
    }
    
    //this is the guess character panel. this pannel consists of only one button which is the guess character button. The action event of the button is bellow.
    private JPanel setupButtonPanel(JFrame frame) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);

        JButton guessCharacterButton = new JButton("Guess Character");
        guessCharacterButton.addActionListener(e -> handleGuessCharacter(frame));

        buttonPanel.add(guessCharacterButton);

        return buttonPanel;
    }
    
    //the option pane thay pops up to the user the chance to guess the character
    private void handleGuessCharacter(JFrame frame) {
        playSoundEffect("src/sounds/click.wav");
        String guessedCharacter = JOptionPane.showInputDialog(frame, "Enter your guess for the AI's character:");
        if (guessedCharacter != null && !guessedCharacter.trim().isEmpty()) {
            String result = gameLogic.playerGuess(guessedCharacter.trim());
            JOptionPane.showMessageDialog(frame, result);
            frame.dispose();
        }
    }

    // The method where the timer is set. this method displays the timer for 5 min (300) seconds
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeRemaining == 0) {
                    timer.cancel();
                    JOptionPane.showMessageDialog(null, "Time's up! AI wins!");
                    System.exit(0);
                }
                timeRemaining--;
                SwingUtilities.invokeLater(() -> timerLabel.setText(
                        "Time Left: " + (timeRemaining / 60) + ":" + String.format("%02d", timeRemaining % 60)));
            }
        }, 1000, 1000);
    }
    
    //this method plays the background music
    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            JOptionPane.showMessageDialog(null, "Error loading background music: " + e.getMessage());
        }
    }
    
    //this method plays the sound effects for the button presses
    private void playSoundEffect(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound effect: " + e.getMessage());
        }
    }
}
