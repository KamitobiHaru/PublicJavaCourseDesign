package gui.Game;
import gui.GameResultDial;
import gui.PlayerSystem.CurrentPlayer;
import gui.PlayerSystem.PlayersIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static java.awt.Component.LEFT_ALIGNMENT;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
public class TimedGame extends Game {
    private final int uniHi;
    private final static PlayersIO PLAYERS_IO = new PlayersIO();
    public TimedGame(GamePane gamePane, CurrentPlayer player){
        super(gamePane, TIMED_MODE, 5, 0, player);
        uniHi = PLAYERS_IO.getUniversalHigh()[mode];
    }
    @Override
    public void endGame(int source) {
        calculateScore();
        if (source == PLAYER_END || source == LOG_OUT){
            quitGame(source);
        } else if (source == TIME_UP){
            gamePane.gameTimer.timer.stop();
            showResult(score);
            if (score > player.getHighScoreArray()[mode]){
                player.setHighScoreArray(player.getHighScoreArray()[0], score);
                PLAYERS_IO.setPlayerHigh(player.getName(), score, mode);
            }
            gamePane.owner.finishGame(true);
        }
    }
    private void quitGame(int source){
        var confirmDial = new JDialog(gamePane.owner, "放弃游戏", true);
        confirmDial.setLocationRelativeTo(gamePane.owner);
        confirmDial.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        confirmDial.setResizable(false);
        confirmDial.setSize(300,200);
        var info = new JLabel("您确实要放弃这局计时游戏吗？");
        var yesButton = new JButton("确认");
        var noButton = new JButton("取消");
        yesButton.setBackground(Color.GRAY);
        yesButton.setForeground(Color.WHITE);
        noButton.setBackground(Color.GRAY);
        noButton.setForeground(Color.WHITE);
        var rootPane = confirmDial.getContentPane();
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.Y_AXIS));
        rootPane.add(Box.createVerticalGlue());
        rootPane.add(info, LEFT_ALIGNMENT);
        rootPane.add(Box.createVerticalGlue());
        var componentsPane = new JPanel();
        componentsPane.setLayout(new BoxLayout(componentsPane, BoxLayout.X_AXIS));
        componentsPane.add(Box.createHorizontalGlue());
        componentsPane.add(yesButton);
        componentsPane.add(Box.createHorizontalGlue());
        componentsPane.add(noButton);
        componentsPane.add(Box.createHorizontalGlue());
        rootPane.add(componentsPane);
        rootPane.add(Box.createVerticalGlue());
        var conf = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                confirmDial.dispose();
                gamePane.gameTimer.timer.stop();
                showResult(0);
                gamePane.gameTimer.timer.stop();
                gamePane.owner.finishGame((source != LOG_OUT));
            }
        };
        yesButton.addActionListener(conf);
        yesButton.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "conf");
        yesButton.getActionMap()
                .put("conf", conf);
        noButton.addActionListener((ignoredEvent) -> confirmDial.dispose());
        confirmDial.setVisible(true);
    }
    private void showResult(int score){
        result = new GameResultDial(
                gamePane.owner, player.getName(), mode, score, player.getHighScoreArray()[mode], uniHi
        );
        result.setVisible(true);
    }
}