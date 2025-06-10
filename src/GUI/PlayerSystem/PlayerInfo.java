package GUI.PlayerSystem;
import javax.swing.*;
import java.awt.*;
public class PlayerInfo extends JDialog{
    public PlayerInfo(JFrame owner, CurrentPlayer player){
        super(owner, "当前玩家信息", false);
        initUI(owner, player);
    }
    private void initUI(JFrame owner, CurrentPlayer player){
        setSize(300,200);
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Font font = new Font("微软雅黑", Font.PLAIN, 22);
        JLabel name, iHighScore, tHighScore;
        name = new JLabel("用户名：" + player.getName());
        name.setFont(new Font("微软雅黑", Font.BOLD, 20));
        iHighScore = new JLabel("无限模式最高得分：" + player.getHighScoreArray()[0], SwingConstants.CENTER);
        iHighScore.setFont(font);
        tHighScore = new JLabel("计时模式最高得分：" + player.getHighScoreArray()[1], SwingConstants.CENTER);
        tHighScore.setFont(font);
        var gl = new GroupLayout(getContentPane());
        getContentPane().setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addGroup(
                                gl.createParallelGroup(GroupLayout.Alignment.CENTER,false)
                                        .addComponent(name)
                                        .addComponent(iHighScore)
                                        .addComponent(tHighScore)
                        )
                        .addGap(0,0,Integer.MAX_VALUE)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addComponent(name)
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addComponent(iHighScore)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tHighScore)
                        .addGap(0,0,Integer.MAX_VALUE)
        );
    }
}
