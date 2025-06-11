package GUI;
import GUI.StartGame.StartGameWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
public class Welcome extends JFrame{
    private JButton continueButton;
    private JLabel words;
    private ImageIcon imageIcon;
    public Welcome(){
        initUI();
    }
    private void initUI(){
        setWindowBasics();
        setButton();
        setWelcomeWord();
        setLayout();
    }
    private void setWindowBasics(){
        setTitle("24点");
        setSize(652,375);
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
        Image titleImage = imageIcon.getImage().getScaledInstance(521, 300, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(titleImage);
        words.setIcon(imageIcon);
        setLayout();
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
        words.setSize((int)(getWidth() * 0.8), (int)(getHeight() * 0.8));
        double width, height;
        width = words.getWidth();
        height = words.getHeight();
        if (width > height * 1500 / 680)
            width = height * 1500 / 680;
        else if (width < height * 1500 / 680)
            height = width*680/1500;
        Image titleImage = imageIcon.getImage().getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(titleImage);
        words.setIcon(newIcon);
    }
    // initialize start button
    private void setButton(){
        continueButton = new JButton("开始游戏 (Enter)");
        continueButton.setBackground(Color.GRAY);
        continueButton.setForeground(Color.WHITE);
        var continueAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                var ignored = new StartGameWindow();
            }
        };
        continueButton.addActionListener(continueAction);
        // bind the button with enter
        continueButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"continueAction");
        continueButton.getActionMap().put("continueAction", continueAction);
    }
    // use GroupLayout to set layout
    private void setLayout(){
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(0,0,Integer.MAX_VALUE)
                .addComponent(words,DEFAULT_SIZE,DEFAULT_SIZE,Integer.MAX_VALUE)
                .addComponent(continueButton,DEFAULT_SIZE,DEFAULT_SIZE,DEFAULT_SIZE)
                .addGap(0,0,Integer.MAX_VALUE)
        );
        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGap(0,0,Integer.MAX_VALUE)
                .addComponent(words)
                .addComponent(continueButton)
                .addGap(0,0,Integer.MAX_VALUE)
        );
    } 
}