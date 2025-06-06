package gui.PlayerSystem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
public class NewPlayerDial extends JDialog{
    private String name = null;
    private JLabel nameLabel, passwordLabel, confirmLabel, errorInfo;
    private JButton confirmButton, cancelButton;
    private JTextField nameTextField;
    private JPasswordField passwordField, confirmField;
    private final static PlayersIO PLAYERS_IO = new PlayersIO();
    public NewPlayerDial(JDialog owner){
        super(owner,"新玩家",true);
        setWindowBasics(owner);
        setComponents();
        setActions();
        setLayout();
    }
    private void setWindowBasics(JDialog owner){
        setSize(300, 200);
        var location = new Point(owner.getLocation().x + 10, owner.getLocation().y + 10);
        setLocation(location);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    private void setComponents(){
        nameLabel = new JLabel("用户名：    ");
        passwordLabel = new JLabel("密码：        ");
        confirmLabel = new JLabel("确认密码：");
        errorInfo = new JLabel();
        errorInfo.setVisible(false);
        errorInfo.setForeground(Color.RED);
        nameTextField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmField = new JPasswordField(20);
        confirmButton = new JButton("确认");
        confirmButton.setBackground(Color.GRAY);
        confirmButton.setForeground(Color.WHITE);
        cancelButton = new JButton("取消");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
    }
    private void setActions(){
        var confirm = new AbstractAction () {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameTextField.getText();
                char[] password = passwordField.getPassword();
                char[] confirm = confirmField.getPassword();
                char[] nameArray = name.toCharArray();
                for (char i : nameArray) {
                	if ((i < 48 || i > 57) && (i < 65 || i > 90) && (i != 95) && (i < 97 || i > 122)) {
                		setError("用户名只允许字母、数字和下划线");
                		return;
                	}
                }
                if (password.length == 0) {
                	setError("密码不能为空");
                	return;
                }
                if (password.length != confirm.length){
                    setError("两次输入的密码不相同");
                    return;
                }
                for (int i = 0; i < password.length; i++){
                    if (password[i] != confirm[i]){
                        setError("两次输入的密码不相同");
                        return;
                    }
                }
                if (name.equals("Guest")){
                    setError("用户名不能是“Guest”");
                    return;
                }
                if (PLAYERS_IO.newPlayer(name, password) == PlayersIO.CREATE_SUCCEED){
                    errorInfo.setVisible(false);
                    dispose();
                }
                else setError(name + "已被注册");
            }
        };
        confirmButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"confirm");
        confirmButton.getActionMap().put("confirm", confirm);
        confirmButton.addActionListener(confirm);
        cancelButton.addActionListener((ignoredEvent) -> dispose());
    }
    private void setError(String message){
        errorInfo.setText(message);
        errorInfo.setVisible(true);
    }
    private void setLayout(){
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        var nameSGroup = gl.createSequentialGroup()
            .addComponent(nameLabel)
            .addComponent(nameTextField);
        var passwordSGroup = gl.createSequentialGroup()
            .addComponent(passwordLabel)
            .addComponent(passwordField);
        var confirmSGroup = gl.createSequentialGroup()
            .addComponent(confirmLabel)
            .addComponent(confirmField);
        var errorSGroup = gl.createSequentialGroup()
            .addComponent(errorInfo)
            .addGap(0,0,Integer.MAX_VALUE);
        var buttonSGroup = gl.createSequentialGroup()
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(confirmButton)
            .addComponent(cancelButton);
        var namePGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
            .addComponent(nameLabel)
            .addComponent(nameTextField);
        var passwordPGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
            .addComponent(passwordLabel)
            .addComponent(passwordField);
        var confirmPGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
            .addComponent(confirmLabel)
            .addComponent(confirmField);
        var errorPGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
            .addComponent(errorInfo)
            .addGap(0,0,Integer.MAX_VALUE);
        var buttonPGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(confirmButton)
            .addComponent(cancelButton);
        gl.setHorizontalGroup(
            gl.createParallelGroup()
                .addGroup(nameSGroup)
                .addGroup(passwordSGroup)
                .addGroup(confirmSGroup)
                .addGroup(errorSGroup)
                .addGroup(buttonSGroup)
        );
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGroup(namePGroup)
                .addGroup(passwordPGroup)
                .addGroup(confirmPGroup)
                .addGroup(errorPGroup)
                .addGap(0,0,Integer.MAX_VALUE)
                .addGroup(buttonPGroup)
        );
    }
}