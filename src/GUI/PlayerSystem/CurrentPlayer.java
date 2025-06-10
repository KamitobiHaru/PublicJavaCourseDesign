package GUI.PlayerSystem;
public class CurrentPlayer {
    private String name;
    private int[] highScore;
    public CurrentPlayer(){
        name = "";
        highScore = new int[2];
    }
    public void setName(String name){
        this.name = name;
    }
    public void setHighScoreArray(int... highScore){
        this.highScore = highScore;
    }
    public String getName(){
        return name;
    }
    public int[] getHighScoreArray(){
        return highScore;
    }
}
