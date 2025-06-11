package GUI.PlayerSystem;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
public class LogInDial extends JDialog{
	private String name = null;
	private char[] password;
	private JLabel nameLabel, passwordLabel, localInfo, incorrectInfo;
	private JButton confirmButton, guestButton, newPlayer;
	private JTextField nameTextField;
	private JPasswordField passwordField;
	private final static PlayersIO PLAYERS_IO = new PlayersIO();
    private boolean playerSet = false;
	public LogInDial(JFrame owner, CurrentPlayer player){
		super(owner,"登录",true);
        setResizable(false);
        setSize(300,200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(owner);
        setComponents();
        setActions(player);
        setLayout();
    }
    private void setComponents(){
        var icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GUI/images/icon.png")));
        setIconImage(icon.getImage());
        nameLabel = new JLabel("用户名：");
        passwordLabel = new JLabel("密码：    ");
        localInfo = new JLabel("用户信息保存在本地。");
        incorrectInfo = new JLabel("用户名或密码错误");
        incorrectInfo.setForeground(Color.RED);
        incorrectInfo.setVisible(false);
        nameTextField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmButton = new JButton("确认");
        confirmButton.setBackground(Color.GRAY);
        confirmButton.setForeground(Color.WHITE);
        guestButton = new JButton("以游客身份游玩");
        guestButton.setBackground(Color.GRAY);
        guestButton.setForeground(Color.WHITE);
        newPlayer = new JButton("新玩家...");
        newPlayer.setBackground(Color.GRAY);   
        newPlayer.setForeground(Color.WHITE);     
    }
    private void setActions(CurrentPlayer player) {
        var confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ignoredEvent) {
                name = nameTextField.getText();
                password = passwordField.getPassword();
                if (PLAYERS_IO.checkPlayerPass(name, password)) {
                    incorrectInfo.setVisible(false);
                    player.setName(name);
                    player.setHighScoreArray(PLAYERS_IO.getPlayerHigh(name));
                    playerSet = true;
                    dispose();
                } else {
                    incorrectInfo.setVisible(true);
                }
            }
        };
        confirmButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirm");
        confirmButton.getActionMap().put("confirm", confirm);
        confirmButton.addActionListener(confirm);
        guestButton.addActionListener((ignoredEvent) -> {
            player.setName("Guest");
            player.setHighScoreArray(PLAYERS_IO.getPlayerHigh("Guest"));
            playerSet = true;
            dispose();
        });
        newPlayer.addActionListener((ignoredEvent) -> {
            var newPlayerDial = new NewPlayerDial(this);
            newPlayerDial.setVisible(true);
        });
    }
    private void setLayout(){
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        var nameSGroup = gl.createSequentialGroup()
            .addComponent(nameLabel)
            .addComponent(nameTextField)
            .addGap(0,0,Integer.MAX_VALUE);
        var passwordSGroup = gl.createSequentialGroup()
            .addComponent(passwordLabel)
            .addComponent(passwordField)
            .addGap(0,0,Integer.MAX_VALUE);
        var buttonSGroup = gl.createSequentialGroup()
            .addComponent(confirmButton)
            .addComponent(guestButton)
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(newPlayer);
        var namePGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING,false)
            .addComponent(nameLabel)
            .addComponent(nameTextField)
            .addGap(0,0,Integer.MAX_VALUE);
        var passwordPGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING,false)
            .addComponent(passwordLabel)
            .addComponent(passwordField)
            .addGap(0,0,Integer.MAX_VALUE);
        var buttonPGroup = gl.createParallelGroup(GroupLayout.Alignment.LEADING,false)
            .addComponent(confirmButton)
            .addComponent(guestButton)
            .addGap(0,0,Integer.MAX_VALUE)
            .addComponent(newPlayer);
        gl.setHorizontalGroup(
            gl.createParallelGroup()
                .addGroup(nameSGroup)
                .addGroup(passwordSGroup)
                .addComponent(localInfo)
                .addComponent(incorrectInfo)
                .addGroup(buttonSGroup)
        );
        gl.setVerticalGroup(
            gl.createSequentialGroup()
            .addGroup(namePGroup)
            .addGroup(passwordPGroup)
            .addComponent(localInfo)
            .addComponent(incorrectInfo)
            .addGap(0,0,Integer.MAX_VALUE)
            .addGroup(buttonPGroup)
        );
    }
    public boolean isPlayerSet(){
        return playerSet;
    }
}