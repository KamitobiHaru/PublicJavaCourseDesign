package GUI.StartGame;
import GUI.Game.*;
import GUI.GameInfo;
import GUI.Help;
import GUI.PlayerSystem.CurrentPlayer;
import GUI.PlayerSystem.LogInDial;
import GUI.PlayerSystem.PlayerInfo;
import GUI.PlayerSystem.PlayersIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
public class StartGameWindow extends JFrame{
    public CurrentPlayer player;
    public JLabel statusBarLabel;
    private WindowMenuBar windowMenuBar;
    private final static PlayersIO PLAYERS_IO = new PlayersIO();
    private SelectModePane selectModePane;
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
        windowMenuBar = new WindowMenuBar();
        setJMenuBar(windowMenuBar);
    }
    private void setMenuActions(){
        windowMenuBar.userInfo.addActionListener((ignored) -> {
            var userInfo = new PlayerInfo(this, player);
            userInfo.setVisible(true);
        });
        windowMenuBar.helpItem.addActionListener((ignored) -> {
            var help = new Help(this);
            help.setVisible(true);
        });
        windowMenuBar.helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        windowMenuBar.gameInfo.addActionListener((ignored) -> {
            GameInfo info = new GameInfo(this);
            info.setVisible(true);
        });
        windowMenuBar.logOut.addActionListener((ignored) -> {
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
        windowMenuBar.exit.addActionListener((ignored) -> System.exit(0));
        windowMenuBar.exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
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
        selectModePane = new SelectModePane();
        selectModePane.iButton.addActionListener((ignored) -> {
            game = new InfiniteGame(gamePane, player);
            startNewGame(gamePane);
        });
        selectModePane.tButton.addActionListener((ignored) -> {
            game = new TimedGame(gamePane, player);
            startNewGame(gamePane);
        });
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
            setStatusBar();
            setSelectModePane();
            selectModePane.setVisible(true);
            initGamePane();
        }
    }
    private void initGamePane(){
        var initPaneGraphThread = new Thread(() -> {
            EventQueue.invokeLater(() -> {
                selectModePane.iButton.setEnabled(false);
                selectModePane.tButton.setEnabled(false);
            });
            gamePane = new GamePane(StartGameWindow.this);
            gamePane.newQuestion();
            EventQueue.invokeLater(() -> {
                selectModePane.iButton.setEnabled(true);
                selectModePane.tButton.setEnabled(true);
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