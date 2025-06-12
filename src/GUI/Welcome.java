package GUI;
import GUI.StartGame.StartGameWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;

public class Welcome extends JFrame{
    private JLabel words;
    private ImageIcon imageIcon;
    public Welcome(){
        initUI();
    }
    private void initUI(){
        setWindowBasics();
        setWelcomeWord();
        setAction();
        add(words);
    }
    private void setWindowBasics(){
        setTitle("24点");
        setSize(720,480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GUI/images/icon.png")));
        setIconImage(icon.getImage());
        setMinimumSize(new Dimension(300,200));
    }
    //initialize welcome icon
    private void setWelcomeWord(){
        words = new JLabel();
        imageIcon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/GUI/images/TitleImage.png"))
        );
        Image titleImage = imageIcon.getImage().getScaledInstance(720, 480, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(titleImage);
        getContentPane().setBackground(new Color(248, 241, 243));
        words.setIcon(imageIcon);
        words.setHorizontalAlignment(SwingConstants.CENTER);
        this.addWindowStateListener((WindowEvent ignoredEvent) -> titleImageResize());
        this.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e){
                titleImageResize();
            }
        });
    }
    // resize imageIcon when component (JLabel) resized
    private void titleImageResize(){
        words.setSize((getContentPane().getWidth()), (getContentPane().getHeight()));
        double width, height;
        width = words.getWidth();
        height = words.getHeight();
        if (width > height * 720 / 480)
            width = height * 720 / 480;
        else if (width < height * 720 / 480)
            height = width*480/720;
        Image titleImage = imageIcon.getImage().getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(titleImage);
        words.setIcon(newIcon);
    }
    // initialize start button
    private void setAction(){
        var continueAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                var ignored = new StartGameWindow();
            }
        };
        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                continueAction.actionPerformed(null);
            }
        });
        // bind the button with enter
        words.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"continueAction");
        words.getActionMap().put("continueAction", continueAction);
    }
}