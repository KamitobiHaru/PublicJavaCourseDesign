package GUI;
import GUI.Game.*;
import GUI.PlayerSystem.CurrentPlayer;
import GUI.PlayerSystem.LogInDial;
import GUI.PlayerSystem.PlayerInfo;
import GUI.PlayerSystem.PlayersIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
public class StartGameWindow extends JFrame{
    public CurrentPlayer player;
    public JLabel statusBarLabel;
    private JMenuItem gameInfo, exit, helpItem, logOut, userInfo;
    private final static PlayersIO PLAYERS_IO = new PlayersIO();
    private JPanel selectModePane;
    private JButton iButton, tButton;
    protected GamePane gamePane = null;
    protected Game game;
    public StartGameWindow(){
        initUI();
        initPlayer();
    }
    private void initUI(){
        setWindowBasics();
        this.setVisible(true);
    }
    private void setWindowBasics(){
        setTitle("24点");
        setSize(1080,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createBasicComponents();
        setMinimumSize(new Dimension(1036, 640));
    }
    //Basic Components are always visible, placed by BorderLayout
    private void createBasicComponents(){
        createStatusBar();
        createMenuBar();
        setMenuActions();
        setSelectModePane();
    }
    private void createStatusBar(){
        statusBarLabel = new JLabel();
        statusBarLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusBarLabel, BorderLayout.SOUTH);
    }
    private void createMenuBar(){
        JMenuBar jMenuBar = new JMenuBar();
        JMenu game = new JMenu("游戏");
        JMenu user = new JMenu("玩家");
        JMenu help = new JMenu("帮助");
        gameInfo = new JMenuItem("关于...");
        userInfo = new JMenuItem("玩家信息...");
        logOut = new JMenuItem("登出...");
        exit = new JMenuItem("退出");
        helpItem = new JMenuItem("帮助文档...");
        game.add(gameInfo);
        game.addSeparator();
        game.add(exit);
        user.add(userInfo);
        user.add(logOut);
        help.add(helpItem);
        jMenuBar.add(game);
        jMenuBar.add(user);
        jMenuBar.add(Box.createHorizontalGlue());
        jMenuBar.add(help);
        setJMenuBar(jMenuBar);
    }
    private void setMenuActions(){
        userInfo.addActionListener((ignored) -> {
            var userInfo = new PlayerInfo(this, player);
            userInfo.setVisible(true);
        });
        helpItem.addActionListener((ignored) -> {
            var help = new Help(this);
            help.setVisible(true);
        });
        helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        gameInfo.addActionListener((ignored) -> {
            GameInfo info = new GameInfo(this);
            info.setVisible(true);
        });
        logOut.addActionListener((ignored) -> {
            if (game != null){ // if game still in process...
                game.endGame(Game.LOG_OUT);
            }
            //refresh page
            revalidate();
            repaint();
            selectModePane.setVisible(false);
            statusBarLabel.setVisible(false);
            initPlayer();
        });
        exit.addActionListener((ignored) -> System.exit(0));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
    }
    public void setStatusBar(){
        statusBarLabel.setText(
            "玩家:"
            + player.getName()
            + "    无限模式玩家历史高分："
            + player.getHighScoreArray()[0]
            + "    计时模式玩家历史高分："
            + player.getHighScoreArray()[1]
            + "    无限模式本地最高纪录："
            + PLAYERS_IO.getUniversalHigh()[0]
            + "    计时模式本地最高记录："
            + PLAYERS_IO.getUniversalHigh()[1]
        );
    }
    private void setSelectModePane(){
        iButton = new JButton("无限模式");
        iButton.setEnabled(false);
        tButton = new JButton("计时模式");
        tButton.setEnabled(false);
        JLabel iInfo = new JLabel("不限时间。时间越短，得分越高。"),
                tInfo = new JLabel("限时五分钟。解题越多，得分越高。");
        iButton.setFont(new Font("微软雅黑", Font.BOLD, 40));
        tButton.setFont(new Font("微软雅黑", Font.BOLD, 40));
        iInfo.setFont(new Font("微软雅黑", Font.BOLD, 24));
        tInfo.setFont(new Font("微软雅黑", Font.BOLD, 24));
        iButton.setBackground(Color.GRAY);
        iButton.setForeground(Color.WHITE);
        tButton.setBackground(Color.GRAY);
        tButton.setForeground(Color.WHITE);
        iButton.addActionListener((ignored) -> {
            game = new InfiniteGame(gamePane, player);
            startNewGame(gamePane);
        });
        tButton.addActionListener((ignored) -> {
            game = new TimedGame(gamePane, player);
            startNewGame(gamePane);
        });
        selectModePane = new JPanel();
        GroupLayout gl = new GroupLayout(selectModePane);
        selectModePane.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        var sg1 = gl.createSequentialGroup()
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(iButton)
            .addGap(0,0,Integer.MAX_VALUE);
        var sg2 = gl.createSequentialGroup()
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(iInfo)
            .addGap(0,0,Integer.MAX_VALUE);
        var sg3= gl.createSequentialGroup()
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(tButton)
            .addGap(0,0,Integer.MAX_VALUE);
        var sg4 = gl.createSequentialGroup()
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(tInfo)
            .addGap(0,0,Integer.MAX_VALUE);
        var pl1 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
            .addComponent(iButton);
        var pl2 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
            .addComponent(iInfo);
        var pl3 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
            .addComponent(tButton);
        var pl4 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
            .addComponent(tInfo);
        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addGroup(sg1)
                .addGroup(sg2)
                .addGroup(sg3)
                .addGroup(sg4)
        );
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,DEFAULT_SIZE,Integer.MAX_VALUE)
                .addGroup(pl1)
                .addGap(20)
                .addGroup(pl2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,DEFAULT_SIZE,Integer.MAX_VALUE)
                .addGroup(pl3)
                .addGap(20)
                .addGroup(pl4)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,DEFAULT_SIZE,Integer.MAX_VALUE)
        );
        add(selectModePane, BorderLayout.CENTER);
        selectModePane.setVisible(false);
    }
    private void startNewGame (GamePane gamePane){
        add(gamePane, BorderLayout.CENTER);
        selectModePane.setVisible(false);
        gamePane.setVisible(true);
        revalidate();
        repaint();
        gamePane.gameTimer.run();
        //revalidate and repaint for immediate ui update
    }
    public void finishGame(boolean goOn){
        remove(gamePane);
        game = null;
        gamePane = null;
        if (goOn){
            setSelectModePane();
            selectModePane.setVisible(true);
            initGamePane();
        }
    }
    private void initGamePane(){
        var initPaneGraphThread = new Thread(() -> {
            EventQueue.invokeLater(() -> {
                iButton.setEnabled(false);
                tButton.setEnabled(false);
            });
            gamePane = new GamePane(StartGameWindow.this);
            gamePane.newQuestion();
            EventQueue.invokeLater(() -> {
                iButton.setEnabled(true);
                tButton.setEnabled(true);
            });
        });
        initPaneGraphThread.start();
    }
    private void initPlayer(){
        player = new CurrentPlayer();
        LogInDial logIn = new LogInDial(this, player);
        logIn.setVisible(true);
        if (logIn.isPlayerSet()){
            setSelectModePane();
            selectModePane.setVisible(true);
            initGamePane();
            setStatusBar();
            statusBarLabel.setVisible(true);
        } else
            System.exit(0);
    }
}