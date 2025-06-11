package GUI.StartGame;
import javax.swing.*;
public class WindowMenuBar extends JMenuBar {
    JMenuItem gameInfo, exit, helpItem, logOut, userInfo;
    public WindowMenuBar(){
        initUI();
    }
    private void initUI(){
        JMenu game = new JMenu("游戏");
        JMenu user = new JMenu("玩家");
        JMenu help = new JMenu("帮助");
        gameInfo = new JMenuItem("关于...");
        userInfo = new JMenuItem("玩家信息...");
        logOut = new JMenuItem("登出...");
        exit = new JMenuItem("退出");
        helpItem = new JMenuItem("帮助文档...");
        game.add(gameInfo);
        game.addSeparator();
        game.add(exit);
        user.add(userInfo);
        user.add(logOut);
        help.add(helpItem);
        add(game);
        add(user);
        add(Box.createHorizontalGlue());
        add(help);
    }
}
