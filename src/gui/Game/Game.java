package gui.Game;
import gui.Cards.CardFrame;
import gui.Cards.Cards;
import gui.ExceptionDial;
import gui.GameResultDial;
import gui.PlayerSystem.CurrentPlayer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
public abstract class Game {
    protected GamePane gamePane;
    protected int score = 0;
    protected int mode;
    public static final int
            INFINITE_MODE = 0,
            TIMED_MODE = 1;
    public static final int
            PLAYER_END = -1,
            TIME_UP = -2,
            LOG_OUT = -3;
    // error codes ends at -28562 because 13*13*13*13 = 28561
    public final static int
            NO_OPERATOR = -28562,
            PARS_NOT_PAIRS = -28563,
            NO_VALUE = -28564,
            SYNTAX_ERROR = -28565,
            WRONG_OPERATORS = -28566,
            WRONG_CARD_VALUES = -28567;
    public final static int ADD = 0,
            SUBTRACT = 1,
            MULTIPLY = 2,
            DIVIDE = 3;
    public final static int INIT_SUCCEED = 0;
    private boolean dragging;
    private Cards.Card cardSelected;
    private CardFrame cardDragged;
    private Point dragOffset;
    private int questionCompleted = 0;
    protected CurrentPlayer player;
    protected GameResultDial result;
    private final Operator[] operators = {
            new Operator('+'),
            new Operator('-'),
            new Operator('*'),
            new Operator('/')
    };
    public Game(GamePane gamePane, int mode, int timeMin, int timeSec, CurrentPlayer player){
        this.gamePane = gamePane;
        this.player = player;
        setActions();
        this.mode = mode;
        gamePane.gameTimer.setMode(this, mode, timeMin, timeSec);
    }
    private void setActions(){
        //dragging issues:
        //  >user should drag a copy of the frame instead of the frame itself
        //  >can have transparent JLabel over card frames so that
        //   it won't be necessary to place target frames above dragged frame
        dragging = false;
        var dragAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                if (!((CardFrame)e.getSource()).isEmpty()){
                    //set selected card frame bottom of card frames, while still top of others
                    gamePane.setComponentZOrder((CardFrame)e.getSource(), gamePane.cardFrameZOrder - 1);
                    cardDragged = (CardFrame)e.getSource();
                    cardSelected = ((CardFrame)e.getSource()).card;
                    dragging = true;
                    gamePane.setLayout(null);
                    //mouse location on screen
                    var location = e.getLocationOnScreen();
                    //mouse location on this panel
                    SwingUtilities.convertPointFromScreen(location, gamePane);
                    //!!set minus offset
                    cardDragged.setLocation(location.x - dragOffset.x, location.y - dragOffset.y);
                    //set offset or the location is the left-up corner
                }
            }
        };
        var releaseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                if (dragging) {
                    Point releasingPoint = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(releasingPoint, gamePane);
                    //get the top component (card frames only) at this point
                    Component target = gamePane.getComponentAt(releasingPoint);
                    //drag to card frames only
                    //although card frames are already set top, there are still other components
                    //which aren't on this panel (for instance, menu bar)
                    if (target instanceof CardFrame){
                        if (((CardFrame)target).isEmpty()){
                            ((CardFrame)target).setFilled(cardSelected);
                            cardDragged.setEmpty();
                            cardSelected = null;
                            dragging = false;
                        }
                    }
                    else {
                        cardDragged = null;
                        cardSelected = null;
                        dragging = false;
                    }
                    gamePane.setLayout(gamePane.gl);
                    gamePane.revalidate();
                }
            }
            @Override
            //!!get offset between mouse and the component left-up corner when pressed
            public void mousePressed(MouseEvent e){
                dragOffset = e.getPoint();
            }
        };
        for (int i = 0; i < 4; i ++){
            gamePane.cardFrames[i].addMouseMotionListener(dragAdapter);
            gamePane.cardFrames[i].addMouseListener(releaseAdapter);
            gamePane.cardSelects[i].addMouseMotionListener(dragAdapter);
            gamePane.cardSelects[i].addMouseListener(releaseAdapter);
        }
        var checkAction = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ignored){
                double result1 = checkSelectResult(),
                        result2 = checkInputResult(gamePane.inputField.getText());
                String result1String, result2String;
                boolean result1IsNum = false, result2IsNum = false;
                result1String = (switch ((int)result1){
                    case NO_OPERATOR -> "缺少符号";
                    case PARS_NOT_PAIRS -> "括号错误";
                    case NO_VALUE ->  "缺少卡牌";
                    default -> {
                        // keep 3 decimals
                        var df = new DecimalFormat("#.###");
                        result1IsNum = true;
                        yield String.format("= %s", df.format(result1));
                    }
                });
                result2String = (switch ((int)result2){
                    case SYNTAX_ERROR -> "语法错误";
                    case PARS_NOT_PAIRS -> "括号错误";
                    case NO_VALUE ->  "缺少数值";
                    case WRONG_OPERATORS -> "错误的运算符";
                    case WRONG_CARD_VALUES -> "数值与卡牌不符";
                    default -> {
                        var df = new DecimalFormat("#.###");
                        result2IsNum = true;
                        yield String.format("= %s", df.format(result2));
                    }
                });
                //priority: 24 > 1 is num > 2 is num > either not empty > 2
                if (result1IsNum && result2IsNum){
                    if (Math.abs(result1 - 24) < 1e-3)
                        gamePane.resultLabel.setText(result1String);
                    else if (Math.abs(result2 - 24) < 1e-3)
                        gamePane.resultLabel.setText(result2String);
                    else gamePane.resultLabel.setText(result1String);
                } else if (result1IsNum)
                    gamePane.resultLabel.setText(result1String);
                else if (result2IsNum)
                    gamePane.resultLabel.setText(result2String);
                else if (gamePane.inputField.getText().isEmpty())
                    gamePane.resultLabel.setText(result1String);
                else
                    gamePane.resultLabel.setText(result2String);
                //double value errors: to enhance robustness, change to BigDecimal
                if (Math.abs(result1 - 24) < 1e-3 || Math.abs(result2 - 24) < 1e-3){
                    questionCompleted ++;
                    gamePane.newQuestion();
                }
            }
        };
        gamePane.check.addActionListener(checkAction);
        gamePane.check.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                .put(
                        KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                        "checkAction"
                );
        gamePane.check.getActionMap().put("checkAction",checkAction);
        gamePane.cardStack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                showKey();
            }
        });
        gamePane.end.addActionListener((ignoredEvent) -> endGame(PLAYER_END));
    }

    private void showKey(){
        var show = new JDialog(gamePane.owner, "答案", true);
        show.setSize(300,200);
        show.setResizable(false);
        show.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        show.setLocationRelativeTo(gamePane.owner);
        var keyLabel = new JLabel(gamePane.key);
        var nextButton = new JButton("下一题");
        keyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        keyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        nextButton.setBackground(Color.GRAY);
        nextButton.setForeground(Color.WHITE);
        var gl = new GroupLayout(show.getContentPane());
        show.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        var sq1 = gl.createSequentialGroup()
                .addGap(0,0,Integer.MAX_VALUE)
                .addComponent(keyLabel)
                .addGap(0,0,Integer.MAX_VALUE);
        var sq2 = gl.createSequentialGroup()
                .addGap(0,0,Integer.MAX_VALUE)
                .addComponent(nextButton)
                .addGap(0,0,Integer.MAX_VALUE);
        gl.setHorizontalGroup(
                gl.createParallelGroup()
                        .addGroup(sq1)
                        .addGroup(sq2)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addComponent(keyLabel)
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addComponent(nextButton)
        );
        var nextAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                show.dispose();
            }
        };
        nextButton.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "nextAction");
        nextButton.getActionMap().put("nextAction", nextAction);
        nextButton.addActionListener(nextAction);
        //the game timer is paused only when the answer is shown
        //in case of players use pause time to cheat
        gamePane.gameTimer.timer.stop();
        show.setVisible(true);
        gamePane.resultLabel.setText("= 0");
        show.validate();
        show.repaint();
        EventQueue.invokeLater(() -> {
            gamePane.newQuestion();
            gamePane.gameTimer.timer.start();
        });
    }
    //scores should be calculated every second as the timer updates
    public void calculateScore(){
        score = (gamePane.gameTimer.getSecondPast() != 0) ?
                (int)(1.0 * questionCompleted / gamePane.gameTimer.getSecondPast() * 5000) :
                0;
        gamePane.scoreLabel.setText(String.format("得分：%d", score));
        gamePane.solvedProblems.setText(String.format("解题：%d", questionCompleted));
    }
    private double checkSelectResult(){
        double result = NO_VALUE;
        //check if card is empty
        if (gamePane.cardSelects[0].isEmpty() || gamePane.cardSelects[1].isEmpty() || gamePane.cardSelects[2].isEmpty())
            return result;
        for (int i = 0; i < 3; i ++) {
            if (gamePane.ops[i].getSelectedIndex() - 1 == -1)
                return NO_OPERATOR;
        }
        // operators in JComboBoxes are indexed as 1, 2, 3, 4,
        // while those in arrays of Operator is 0, 1, 2, 3
        ArrayList<SubExpression> subExpressions = new ArrayList<>();
        subExpressions.add(new SubExpression(
                gamePane.cardSelects[0].card.getValue(),
                gamePane.cardSelects[1].card.getValue(),
                operators[gamePane.ops[0].getSelectedIndex() - 1]
        ));
        subExpressions.add(new SubExpression(
                gamePane.cardSelects[1].card.getValue(),
                gamePane.cardSelects[2].card.getValue(),
                operators[gamePane.ops[1].getSelectedIndex() - 1]
        ));
        subExpressions.add(new SubExpression(
                gamePane.cardSelects[2].card.getValue(),
                gamePane.cardSelects[3].card.getValue(),
                operators[gamePane.ops[2].getSelectedIndex() - 1]
        ));
        // check brackets selected
        char[] parSelected = new char[16];
        for (int i = 0; i < 4; i++) {
            parSelected[4*i] = (gamePane.leftPars[i].getSelected()[0])?'(':'\0';
            parSelected[4*i+1] = (gamePane.leftPars[i].getSelected()[1])?'(':'\0';
            parSelected[4*i+2] = (gamePane.rightPars[i].getSelected()[0])?')':'\0';
            parSelected[4*i+3] = (gamePane.rightPars[i].getSelected()[1])?')':'\0';
        }
        //Check if pars are in pairs
        if (parsNotPairs(parSelected))
            return PARS_NOT_PAIRS;
        // counting level is not yet encapsulated as data in two modes are not store in the same way
        // this mode is not removed ...
        // considering that the position is set so that it is more efficient than transferring to string
        for (int opIndex = 0; opIndex < 3; opIndex++){ // opIndex: index of ops
            int level = setSelectLevels(opIndex, parSelected);
            subExpressions.get(opIndex).setLevel(subExpressions.get(opIndex).getLevel() + level);
        }
        return calculateArrays(subExpressions, result);
    }
    private double checkInputResult(String inputString){
        // convert input string to char array and simplify
        char[] simplifiedChars = simplifyInput(inputString);
        if (simplifiedChars == null){
            return SYNTAX_ERROR;
        }
        if (!checkNumLegal(simplifiedChars))
            return WRONG_CARD_VALUES;
        ArrayList<SubExpression> subExpressions = new ArrayList<>();
        int tempReturn;
        if ((tempReturn = initSubExpressionsArrayList(subExpressions, simplifiedChars)) != INIT_SUCCEED)
            return tempReturn;
        cancelRedundantPars(simplifiedChars);
        //check if pars are in pairs
        if (parsNotPairs(simplifiedChars))
            return PARS_NOT_PAIRS;
        addInputParsLevel(subExpressions, simplifiedChars);
        return(calculateArrays(subExpressions, NO_VALUE));
    }
    private boolean isDigit(char i){
        return (i > 47 && i < 58);
    }
    private boolean isPar(char i){
        return (i == '(' || i == ')');
    }
    private boolean isOperator(char i){
        return (i == '+' || i == '-' || i == '*' || i == '/');
    }
    private char[] simplifyInput(String s){
        StringBuilder simplifyStringBuilder = new StringBuilder();
        for (char charEach : s.toCharArray()){
            if (isDigit(charEach) || isOperator(charEach) || isPar(charEach)){
                simplifyStringBuilder.append(charEach);
            } else if (charEach != ' '){
                return null;
            }
        }
        return simplifyStringBuilder.toString().toCharArray();
    }
    private boolean checkNumLegal(char[] simplifiedChars){
        char SEPARATOR = ' ';
        StringBuilder numStringBuilder = new StringBuilder();
        for (char simplifiedChar : simplifiedChars){
            if (isDigit(simplifiedChar))
                numStringBuilder.append(simplifiedChar);
            else if (!numStringBuilder.isEmpty()
                    && isOperator(simplifiedChar)
                    && numStringBuilder.charAt(numStringBuilder.length() - 1) != SEPARATOR)
                numStringBuilder.append(SEPARATOR);
        }
        String[] numStrings = numStringBuilder.toString().split(Character.toString(SEPARATOR));
        if (numStrings.length != 4)
            return false;
        int[] values = new int[4];
        for (int value = 0; value < 4; value++){
            values[value] = Integer.parseInt(numStrings[value]);
        }
        Arrays.sort(values);
        Arrays.sort(gamePane.question);
        return Arrays.equals(values, gamePane.question);
    }
    private int initSubExpressionsArrayList(ArrayList<SubExpression> subExpressions, char[] simplifiedChars){
        StringBuilder numStringBuilder;
        //traverse through all operators to check the front and hind values and add to ArrayList cal
        for (int charIndex = 0; charIndex < simplifiedChars.length; charIndex++){
            if (isOperator(simplifiedChars[charIndex])) {
                SubExpression temp = new SubExpression();
                boolean[] isolated = {false, false};
                //parse left
                numStringBuilder = new StringBuilder();
                int frontCharIndex = charIndex - 1;
                if (frontCharIndex < 0)
                    return (simplifiedChars[charIndex] == '-')
                            ? WRONG_CARD_VALUES
                            : WRONG_OPERATORS;
                while (isPar(simplifiedChars[frontCharIndex])){
                    if (--frontCharIndex < 0)
                        return (simplifiedChars[charIndex] == '-')
                                ? WRONG_CARD_VALUES
                                : WRONG_OPERATORS;
                }
                while (isDigit(simplifiedChars[frontCharIndex])) {
                    numStringBuilder.insert(0, simplifiedChars[frontCharIndex]);
                    if (--frontCharIndex < 0)
                        break;
                }
                try {
                    if (!numStringBuilder.isEmpty())
                        temp.setNum1(Integer.parseInt(numStringBuilder.toString()));
                    else if (isOperator(simplifiedChars[charIndex])){
                        isolated[0] = true;
                    }
                } catch (NumberFormatException e) {
                    return SYNTAX_ERROR;
                }
                //parse right
                frontCharIndex = charIndex + 1;
                if (frontCharIndex > simplifiedChars.length - 1)
                    return WRONG_OPERATORS;
                numStringBuilder = new StringBuilder();
                while (isPar(simplifiedChars[frontCharIndex])){
                    if (++frontCharIndex >= simplifiedChars.length)
                        return WRONG_OPERATORS;
                }
                while (isDigit(simplifiedChars[frontCharIndex])) {
                    numStringBuilder.append(simplifiedChars[frontCharIndex]);
                    if (++frontCharIndex >= simplifiedChars.length)
                        break;
                }
                try {
                    if (!numStringBuilder.isEmpty())
                        temp.setNum2(Integer.parseInt(numStringBuilder.toString()));
                    else if (isOperator(simplifiedChars[charIndex])){
                        isolated[1] = true;
                    }
                } catch (NumberFormatException e) {
                    return SYNTAX_ERROR;
                }
                if (isolated[0] && simplifiedChars[charIndex] == '-')
                    return WRONG_CARD_VALUES;
                else if (isolated[0])
                    return WRONG_OPERATORS;
                temp.setOperator(operators[switch (simplifiedChars[charIndex]){
                    case '+' -> ADD;
                    case '-' -> SUBTRACT;
                    case '*' -> MULTIPLY;
                    case '/' -> DIVIDE;
                    default -> {
                        new ExceptionDial("运算符推演错误。");
                        throw new RuntimeException("WrongOperator");
                    }
                }]);
                temp.initLevel();
                temp.setOpIndex(charIndex);
                subExpressions.add(temp);
            }
        }
        return INIT_SUCCEED;
    }
    private void cancelRedundantPars(char[] simplifiedChars){
        for (int charIndexLeft = 0; charIndexLeft < simplifiedChars.length; charIndexLeft++){
            int charIndexRight;
            if (simplifiedChars[charIndexLeft] == '('){
                boolean toCancel = true;
                for (charIndexRight = charIndexLeft; charIndexRight < simplifiedChars.length && simplifiedChars[charIndexRight] != ')'; charIndexRight++){
                    if (isOperator(simplifiedChars[charIndexRight]))
                        toCancel = false;
                }
                if (toCancel){
                    simplifiedChars[charIndexLeft] = simplifiedChars[charIndexRight] = '\0';
                }
            }
        }
    }
    private boolean parsNotPairs(char[] parSet){
        char[] setTest = new char[parSet.length + 2];
        setTest[0] = '\0';
        System.arraycopy(parSet, 0, setTest, 1, parSet.length);
        // add '\0' to two ends to avoid out of bounds exception
        setTest[parSet.length+1] = '\0';
        int rightIndex = 0;
        for (int i = 0; i < setTest.length; i++){
            if (setTest[i] != '('){
                continue;
            }
            for (int j = Math.max(rightIndex, i); j < setTest.length; j ++){
                if (setTest[j] == ')'){
                    rightIndex = j + 1;
                    break;
                }
                if (j == setTest.length - 1){
                    return true;
                }
            }
        }
        int leftIndex = setTest.length - 1;
        for (int i = setTest.length - 1; i >= 0; i --){
            if (setTest[i] != ')')
                continue;
            for (int j = Math.min(leftIndex, i); j >= 0; j --){
                if (setTest[j] == '('){
                    leftIndex = j - 1;
                    break;
                }
                if (j == 0){
                    return true;
                }
            }
        }
        return false;
    }
    private int setSelectLevels(int i, char[] parSelected) {
        int level = 0;
        int leftCount = 0, rightCount = 0;
        //parse left
        for (int j = 4 * i + 3; j >= 0; j --){
            if (parSelected[j] == ')')
                break;
            if (parSelected[j] == '(')
                leftCount ++;
        }
        //parse right
        for (int j = 4 * (i + 1); j < 16; j ++){
            if (parSelected[j] == '(')
                break;
            if (parSelected[j] == ')')
                rightCount ++;
        }
        level += 10 * Math.max(leftCount, rightCount);
        return level;
    }
    private void addInputParsLevel(ArrayList<SubExpression> subExpressions, char[] simplifiedChars){
        // traverse through operators and count pars in the left and right of it
        for (SubExpression i : subExpressions){
            int leftCount = 0, rightCount = 0;
            //parse left
            for(int j = i.getOpIndex() - 1; j >= 0; j --){
                if (simplifiedChars[j] == ')')
                    break;
                if (simplifiedChars[j] == '(')
                    leftCount ++;
            }
            //parse right
            for (int j = i.getOpIndex() + 1; j < simplifiedChars.length; j ++){
                if (simplifiedChars[j] == '(')
                    break;
                if (simplifiedChars[j] == ')')
                    rightCount ++;
            }
            i.setLevel(i.getLevel() + 10 * Math.max(leftCount, rightCount));
        }
    }
    private double calculateArrays(ArrayList<SubExpression> subExpressions, double result){
        while (!subExpressions.isEmpty()){
            double maxLevel = 0;
            for (SubExpression subExpression : subExpressions) {
                maxLevel = Math.max(subExpression.getLevel(), maxLevel);
            }
            for (int i = 0; i < subExpressions.size(); i++){
                if (subExpressions.get(i).getLevel() == maxLevel){
                    double formerLevel = 0; double latterLevel = 0;
                    result = subExpressions.get(i).operate();
                    if (i + 1 < subExpressions.size())
                        latterLevel = subExpressions.get(i+1).getLevel();
                    if (i - 1 >= 0){
                        formerLevel = subExpressions.get(i-1).getLevel();
                    }
                    if (formerLevel > latterLevel){
                        subExpressions.get(i-1).setNum2(result);
                    } else if (latterLevel > formerLevel){
                        subExpressions.get(i+1).setNum1(result);
                    } else if (latterLevel > 0){
                        subExpressions.get(i-1).setNum2(result);
                    }
                    subExpressions.remove(i);
                    break;
                }
            }
        }
        return result;
    }
    public abstract void endGame(int source);
}
