package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
public class GameResultDial extends JDialog{
    JLabel name, mode, score, oriHi, uniHi;
    JButton confirm;
    public GameResultDial(JFrame owner, String name, int mode, int score, int oriHighScore, int uniHighScore){
        super(owner, "游戏结果", true);
        initUI(owner, name, mode, score, oriHighScore, uniHighScore);
        setConfirmAction();
        setLayout();
    }
    private void initUI(JFrame owner, String name, int mode, int score, int oriHighScore, int uniHighScore) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setResizable(false);
        setLocationRelativeTo(owner);
        this.name = new JLabel("玩家：" + name);
        this.name.setFont(new Font("微软雅黑", Font.BOLD, 20));
        String m = (mode == 0) ? "无限模式" : ((mode == 1) ? "计时模式" : "-1");
        this.mode = new JLabel("模式：" + m);
        this.mode.setFont(new Font("微软雅黑", Font.BOLD, 20));
        this.score = new JLabel("得分：" + score);
        this.score.setFont(new Font("微软雅黑", Font.BOLD, 20));
        this.oriHi = new JLabel("玩家历史最高得分：" + oriHighScore);
        this.oriHi.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        if (score > oriHighScore)
            this.oriHi.setText(oriHi.getText() + "  新纪录！");
        this.uniHi = new JLabel("本地玩家最高得分：" + oriHighScore);
        this.uniHi.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        confirm = new JButton("返回标题界面");
        confirm.setFont(new Font("微软雅黑", Font.BOLD, 18));
        confirm.setBackground(Color.GRAY);
        confirm.setForeground(Color.WHITE);
        if (score > uniHighScore)
            this.uniHi.setText(uniHi.getText() + "  新纪录！");
    }
    private void setConfirmAction(){
        var conf = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        };
        confirm.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "conf");
        confirm.getActionMap()
                .put("conf", conf);
        confirm.addActionListener(conf);
    }
    private void setLayout(){
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        var sg1 = gl.createSequentialGroup()
                .addGap(5,5,Integer.MAX_VALUE)
                .addComponent(this.name)
                .addGap(5,5,Integer.MAX_VALUE);
        var sg2 = gl.createSequentialGroup()
                .addGap(5,5,Integer.MAX_VALUE)
                .addComponent(this.mode)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.score)
                .addGap(5,5,Integer.MAX_VALUE);
        var sg3 = gl.createSequentialGroup()
                .addGap(5,5,Integer.MAX_VALUE)
                .addComponent(this.oriHi)
                .addGap(5,5,Integer.MAX_VALUE);
        var sg4 = gl.createSequentialGroup()
                .addGap(5,5,Integer.MAX_VALUE)
                .addComponent(this.uniHi)
                .addGap(5,5,Integer.MAX_VALUE);
        var sg5 = gl.createSequentialGroup()
                .addGap(5,5,Integer.MAX_VALUE)
                .addComponent(confirm)
                .addGap(5,5,Integer.MAX_VALUE);
        var pg1 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(this.name);
        var pg2 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(this.mode)
                .addComponent(this.score);
        var pg3 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(this.oriHi);
        var pg4 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(this.uniHi);
        var pg5 = gl.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(confirm);
        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER,false)
                                .addGroup(sg1)
                                .addGroup(sg2)
                                .addGroup(sg3)
                                .addGroup(sg4)
                                .addGroup(sg5))
                        .addGap(0,0,Integer.MAX_VALUE)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addGroup(pg1)
                        .addGroup(pg2)
                        .addGroup(pg3)
                        .addGroup(pg4)
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addGroup(pg5)
                        .addGap(0,0,Integer.MAX_VALUE)
        );
    }
}
