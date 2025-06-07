package gui;
import gui.Game.Game;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;
public class Time extends JLabel{
    public final static int COUNT_DOWN = 1, COUNT_UP = 0;
    private int timeSec, timeMin;
    private int mode;
    private int currentMin, currentSec;
    private Game game;
    public Timer timer;
    public Time(){
        // initialize appearance
        setBackground(Color.CYAN);
        setForeground(Color.BLACK);
        setOpaque(true);
        // set timer icon
        setHorizontalAlignment(SwingConstants.CENTER);
        Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/gui/images/stopWatch.png")))
            .getImage()
            .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
        setFont(new Font("微软雅黑",Font.BOLD,18));
        setMaximumSize(new Dimension(160,50));
        setMinimumSize(new Dimension(160,32));
        setPreferredSize(new Dimension(160,32));
    }
    public void setMode(Game game, int mode, int timeMin, int timeSec){
        this.mode = mode;
        this.timeMin = timeMin;
        this.timeSec = timeSec;
        this.game = game;
        this.currentMin = timeMin;
        this.currentSec = timeSec;
        proceedTime();
        setText(timeMin + String.format(":%02d    ", timeSec));
    }
    // if sec > 60, set min ++
    private void proceedTime(){
        while (currentSec >= 60){
            currentMin ++;
            currentSec -= 60;
        }
    }
    private void setUI(){
        setText(currentMin + String.format(":%02d    ", currentSec));
    }
    public void run(){
        timer = new Timer(1000, e -> {
            switch (mode) {
                case COUNT_UP:
                    if (++currentSec == 60){
                        currentSec = 0;
                        currentMin ++;
                    }
                    setUI();
                    break;
                case COUNT_DOWN:
                    if (--currentSec < 0){
                        currentSec = 59;
                        if (--currentMin < 0){
                            currentMin = 0;
                            currentSec = 0;
                            setText("Time Up");
                            ((Timer)e.getSource()).stop();
                            game.endGame(Game.TIME_UP);
                            break;
                        }
                    }
                    setUI();
                    break;
            }
            //scores update every second
            game.calculateScore();
        });
        timer.start();
    }
    public int getSecondPast(){
        return Math.abs((60 * currentMin + currentSec) - (timeSec + 60 * timeMin));
    }
}