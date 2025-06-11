package GUI;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ExceptionDial extends JDialog {
    public ExceptionDial(String message){
        initUI(message + "请检查设置或向我们反映。");
    }
    private void initUI(String message){
        setTitle("异常");
        var icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GUI/images/icon.png")));
        setIconImage(icon.getImage());
        setModal(true);
        setSize(300, 200);
        setAlwaysOnTop(true);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        var mess = new JLabel(message, SwingConstants.CENTER);
        mess.setVerticalAlignment(SwingConstants.CENTER);
        mess.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        add(mess);
        setVisible(true);
    }
}
