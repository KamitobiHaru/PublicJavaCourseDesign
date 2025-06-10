package GUI.Game;
import GUI.GameResultDial;
import GUI.PlayerSystem.CurrentPlayer;
import GUI.PlayerSystem.PlayersIO;
public class InfiniteGame extends Game {
    private final static PlayersIO PLAYERS_IO = new PlayersIO();
    public InfiniteGame(GamePane gamePane, CurrentPlayer player){
        super(gamePane, INFINITE_MODE, 0, 0, player);
    }
    @Override
    public void endGame(int source) {
        final int uniHi = PLAYERS_IO.getUniversalHigh()[mode];
        gamePane.gameTimer.timer.stop();
        calculateScore();
        result = new GameResultDial(gamePane.owner, player.getName(), mode, score, player.getHighScoreArray()[mode], uniHi);
        if (score > player.getHighScoreArray()[mode]){
            player.setHighScoreArray(score, player.getHighScoreArray()[1]);
            PLAYERS_IO.setPlayerHigh(player.getName(), score, mode);
        }
        result.setVisible(true);
        gamePane.owner.finishGame((source != LOG_OUT));
    }
}
