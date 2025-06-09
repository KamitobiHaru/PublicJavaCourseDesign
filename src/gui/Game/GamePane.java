package gui.Game;
import gui.Cards.*;
import gui.ExceptionDial;
import gui.StartGameWindow;
import gui.Symbols.*;
import gui.Time;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;
//this class is mainly responsible for the graph ui of the game
public class GamePane extends JPanel {
    public Time gameTimer;
    JLabel cardStack, resultLabel, scoreLabel, solvedProblems;
    int[] question;
    String key;
    CardFrame[] cardFrames, cardSelects;
    Operators[] ops;
    Pars[] leftPars, rightPars;
    JButton check, end;
    GroupLayout gl;
    JTextField inputField;
    StartGameWindow owner;
    int cardFrameZOrder;
    private GameLogic gameLogic = new GameLogic();
    private final Cards cards = new Cards();
    //this constructor is called before the select mode button is pressed
    //any statements or variable in this constructor however not related to the graph is because \
    //calling it in advance can enhance the performance.
    public GamePane(StartGameWindow owner) {
        this.owner = owner;
        // so that question can be set background
        gameLogic.start();
        initUI();
        setLayout();
    }
    private void initUI() {
        Font font = new Font("微软雅黑", Font.BOLD, 16);
        gameTimer = new Time();
        cardFrames = new CardFrame[4];
        cardSelects = new CardFrame[4];
        cardStack = new JLabel(); // icon...
        ImageIcon a = new ImageIcon(Objects.requireNonNull(getClass().getResource("/gui/images/cardFrame.png")));
        ImageIcon emptyIcon = new ImageIcon(
                a.getImage().getScaledInstance(40, 60, Image.SCALE_SMOOTH)
        );
        cardStack.setIcon(emptyIcon);
        resultLabel = new JLabel();
        resultLabel.setBackground(Color.CYAN);
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setOpaque(true);
        resultLabel.setText(String.format("= %d", 0));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        scoreLabel = new JLabel();
        scoreLabel.setText(String.format("得分：%d", 0));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(font);
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(Color.CYAN);
        scoreLabel.setForeground(Color.BLACK);
        solvedProblems = new JLabel();
        solvedProblems.setText(String.format("解题：%d", 0));
        solvedProblems.setHorizontalAlignment(SwingConstants.CENTER);
        solvedProblems.setFont(font);
        solvedProblems.setOpaque(true);
        solvedProblems.setBackground(Color.CYAN);
        solvedProblems.setForeground(Color.BLACK);
        ops = new Operators[3];
        leftPars = new Pars[4];
        rightPars = new Pars[4];
        inputField = new JTextField();
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        check = new JButton("检查");
        check.setFont(font);
        check.setBackground(Color.GRAY);
        check.setForeground(Color.WHITE);
        end = new JButton("结束游戏");
        end.setFont(font);
        end.setBackground(Color.GRAY);
        end.setForeground(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            cardFrames[i] = new CardFrame();
            cardSelects[i] = new CardFrame();
            leftPars[i] = new Pars(Pars.LEFT);
            rightPars[i] = new Pars(Pars.RIGHT);
        }
        for (int i = 0; i < 3; i++) {
            ops[i] = new Operators();
        }
    }

    private void setLayout() {
        gl = new GroupLayout(this);
        setLayout(gl);
        gl.setAutoCreateContainerGaps(false);
        gl.setAutoCreateGaps(false);
        var buttonMaxSize = new Dimension(100, 50);
        var buttonMinSize = new Dimension(64, 32);
        check.setMaximumSize(buttonMaxSize);
        check.setMinimumSize(buttonMinSize);
        check.setPreferredSize(buttonMinSize);
        end.setMaximumSize(buttonMaxSize);
        end.setMinimumSize(buttonMinSize);
        end.setPreferredSize(buttonMinSize);
        resultLabel.setMaximumSize(new Dimension(150, 50));
        resultLabel.setMinimumSize(new Dimension(150, 32));
        resultLabel.setPreferredSize(new Dimension(150, 32));
        scoreLabel.setMaximumSize(buttonMaxSize);
        scoreLabel.setMinimumSize(buttonMinSize);
        scoreLabel.setPreferredSize(buttonMinSize);
        scoreLabel.setPreferredSize(buttonMinSize);
        solvedProblems.setMaximumSize(buttonMaxSize);
        solvedProblems.setMinimumSize(buttonMinSize);
        solvedProblems.setPreferredSize(buttonMinSize);
        var cardFrameMaxSize = new Dimension(200, 300);
        var cardFrameMinSize = new Dimension(100, 150);
        var parMaxSize = new Dimension(60, 30);
        var parMinSize = new Dimension(50, 25);
        cardStack.setMaximumSize(new Dimension(40, 60));
        cardStack.setMinimumSize(new Dimension(40, 60));
        cardStack.setPreferredSize(new Dimension(40, 60));
        for (int i = 0; i < 4; i++) {
            cardSelects[i].setMaximumSize(cardFrameMaxSize);
            cardSelects[i].setMinimumSize(cardFrameMinSize);
            cardSelects[i].setPreferredSize(new Dimension(140, 210));
            cardFrames[i].setMaximumSize(cardFrameMaxSize);
            cardFrames[i].setMinimumSize(cardFrameMinSize);
            cardFrames[i].setPreferredSize(new Dimension(180, 270));
            leftPars[i].setMaximumSize(parMaxSize);
            leftPars[i].setMinimumSize(parMinSize);
            leftPars[i].setPreferredSize(parMinSize);
            rightPars[i].setMaximumSize(parMaxSize);
            rightPars[i].setMinimumSize(parMinSize);
            rightPars[i].setPreferredSize(parMinSize);
        }
        var opMaxSize = new Dimension(52, 41);
        var opMinSize = new Dimension(44, 33);
        for (int i = 0; i < 3; i++) {
            ops[i].setMaximumSize(opMaxSize);
            ops[i].setMinimumSize(opMaxSize);
            ops[i].setPreferredSize(opMinSize);
        }
        var sg1 = gl.createSequentialGroup()
                .addGap(7, 7, 15)
                .addComponent(gameTimer)
                .addGap(10, 10, Integer.MAX_VALUE)
                .addComponent(solvedProblems)
                .addGap(7, 7, 15)
                .addComponent(scoreLabel)
                .addGap(7, 7, 15)
                .addComponent(end)
                .addGap(7, 7, 15);
        var sg2 = gl.createSequentialGroup()
                .addGap(7, 7, Integer.MAX_VALUE)
                .addComponent(cardStack)
                .addGap(7, 7, Integer.MAX_VALUE);
        var sg3 = gl.createSequentialGroup()
                .addGap(30, 100, Integer.MAX_VALUE)
                .addComponent(cardFrames[0])
                .addGap(5, 30, 30)
                .addComponent(cardFrames[1])
                .addGap(5, 30, 30)
                .addComponent(cardFrames[2])
                .addGap(5, 30, 30)
                .addComponent(cardFrames[3])
                .addGap(30, 100, Integer.MAX_VALUE);
        var sg4 = gl.createSequentialGroup()
                .addGap(5, 5, 70)
                .addComponent(leftPars[0])
                .addGap(5, 5, 45)
                .addComponent(cardSelects[0])
                .addGap(5, 5, 45)
                .addComponent(rightPars[0])
                .addGap(5, 5, 45)
                .addComponent(ops[0])
                .addGap(5, 5, 45)
                .addComponent(leftPars[1])
                .addGap(5, 5, 45)
                .addComponent(cardSelects[1])
                .addGap(5, 5, 45)
                .addComponent(rightPars[1])
                .addGap(5, 5, 45)
                .addComponent(ops[1])
                .addGap(5, 5, 45)
                .addComponent(leftPars[2])
                .addGap(5, 5, 45)
                .addComponent(cardSelects[2])
                .addGap(5, 5, 45)
                .addComponent(rightPars[2])
                .addGap(5, 5, 45)
                .addComponent(ops[2])
                .addGap(5, 5, 45)
                .addComponent(leftPars[3])
                .addGap(5, 5, 45)
                .addComponent(cardSelects[3])
                .addGap(5, 5, 45)
                .addComponent(rightPars[3])
                .addGap(5, 5, 70);
        var sg5 = gl.createSequentialGroup()
                .addGap(5, 5, Integer.MAX_VALUE)
                .addComponent(inputField)
                .addGap(5, 5, Integer.MAX_VALUE);
        var sg6 = gl.createSequentialGroup()
                .addGap(10, 10, Integer.MAX_VALUE)
                .addComponent(check)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultLabel)
                .addGap(10, 10, Integer.MAX_VALUE);
        var pg1 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                .addComponent(gameTimer)
                .addComponent(solvedProblems)
                .addComponent(scoreLabel)
                .addComponent(end);
        var pg2 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                .addComponent(cardStack);
        var pg3 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(cardFrames[0])
                .addComponent(cardFrames[1])
                .addComponent(cardFrames[2])
                .addComponent(cardFrames[3]);
        var pg4 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(leftPars[0])
                .addComponent(cardSelects[0])
                .addComponent(rightPars[0])
                .addComponent(ops[0])
                .addComponent(leftPars[1])
                .addComponent(cardSelects[1])
                .addComponent(rightPars[1])
                .addComponent(ops[1])
                .addComponent(leftPars[2])
                .addComponent(cardSelects[2])
                .addComponent(rightPars[2])
                .addComponent(ops[2])
                .addComponent(leftPars[3])
                .addComponent(cardSelects[3])
                .addComponent(rightPars[3]);
        var pg5 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                .addComponent(inputField);
        var pg6 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                .addComponent(check)
                .addComponent(resultLabel);
        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addGroup(sg1)
                        .addGroup(sg2)
                        .addGroup(sg3)
                        .addGroup(sg4)
                        .addGroup(sg5)
                        .addGroup(sg6)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(10, 10, 20)
                        .addGroup(pg1)
                        .addGap(10, 10, Integer.MAX_VALUE)
                        .addGroup(pg2)
                        .addGap(10, 10, Integer.MAX_VALUE)
                        .addGroup(pg3)
                        .addGap(10, 10, Integer.MAX_VALUE)
                        .addGroup(pg4)
                        .addGap(10, 10, Integer.MAX_VALUE)
                        .addGroup(pg5)
                        .addGap(10, 10, Integer.MAX_VALUE)
                        .addGroup(pg6)
                        .addGap(15, 15, 25)
        );
        //set card frames at top and others at bottom
        Component[] components = getComponents();
        int otherZOrder = getComponentCount() - 1;
        cardFrameZOrder = 0;
        for (Component component : components) {
            if (component instanceof CardFrame) {
                setComponentZOrder(component, cardFrameZOrder++);
            } else {
                setComponentZOrder(component, otherZOrder--);
            }
        }
    }
    //generate the next question right after the last one is used
    public void newQuestion() {
        EventQueue.invokeLater(() -> { //invoke this after UI refreshes
            String[] questionAnswerStrings;
            inputField.setText("");
            if (gameLogic.isAlive()) {
                try {
                    gameLogic.join(); // to avoid calling the question before it is generated
                } catch (InterruptedException e) {
                    new ExceptionDial("线程异常。").setVisible(true);
                    throw new RuntimeException(e);
                }
            }
            questionAnswerStrings = gameLogic.getQuestionString().split(GameLogic.SEPARATOR);
            gameLogic = new GameLogic();
            gameLogic.start();
            question = new int[]{
                    Integer.parseInt(questionAnswerStrings[0]),
                    Integer.parseInt(questionAnswerStrings[1]),
                    Integer.parseInt(questionAnswerStrings[2]),
                    Integer.parseInt(questionAnswerStrings[3])
            };
            key = questionAnswerStrings[4];
            for (int i = 0; i < 4; i++) {
                cardFrames[i].setFilled(cards.cardsSet[question[i] - 1]);
                cardSelects[i].setEmpty();
                leftPars[i].setEmpty();
                rightPars[i].setEmpty();
            }
            ops[0].setEmpty();
            ops[1].setEmpty();
            ops[2].setEmpty();
            if (gameTimer.timer != null){
                gameTimer.timer.start();
            }
            //print key on console for demonstration and test
            System.out.println(key);
        });
    }
}