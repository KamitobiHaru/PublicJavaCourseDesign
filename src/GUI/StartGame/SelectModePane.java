package GUI.StartGame;
import javax.swing.*;
import java.awt.*;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
public class SelectModePane extends JPanel {
    public JButton iButton, tButton;
    public SelectModePane(){
        initUI();
    }
    private void initUI(){
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
        GroupLayout gl = new GroupLayout(this);
        setLayout(gl);
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
    }
}
