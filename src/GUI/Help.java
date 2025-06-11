package GUI;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.swing.*;
public class Help extends JDialog {
    public Help(JFrame owner) {
        super(owner, "帮助", false);
        setSize(585, 720);
        setResizable(false);
        var icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GUI/images/icon.png")));
        setIconImage(icon.getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(new Point(owner.getLocation().x + 720, owner.getLocation().y));
        var textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        try {
            // load help file
            textPane.setPage(getClass().getResource("/GUI/html/HelpText.html"));
        } catch (IOException e) {
            //help file doesn't exist
            textPane.setText("<h2>当前帮助内容不可用</h2>");
        }
        // enable scrolling
        var scrollPane = new JScrollPane(
                textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        add(scrollPane);
    }
}
