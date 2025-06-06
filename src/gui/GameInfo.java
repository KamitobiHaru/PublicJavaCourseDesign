package gui;
import java.awt.Font;
import javax.swing.*;
public class GameInfo extends JDialog {
    private JLabel icon, name, author, description;
    public GameInfo(JFrame owner){
        super(owner, "关于", true);
        initUI();
        setLayout();
    }
    private void initUI(){
        setSize(400,300);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        name = new JLabel("24点");
        name.setFont(new Font("微软雅黑", Font.BOLD, 20));
        author = new JLabel("By 张思远，程亚迪");
        description = new JLabel("Java课程设计    ver 0.8.2-alpha");
    }
    private void setLayout(){
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
                                        .addComponent(author)
                                        .addComponent(description)
                        )
                        .addGap(0,0,Integer.MAX_VALUE)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addComponent(name)
                        .addGap(0,0,Integer.MAX_VALUE)
                        .addComponent(author)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(description)
                        .addGap(0,0,Integer.MAX_VALUE)
        );
    }
}