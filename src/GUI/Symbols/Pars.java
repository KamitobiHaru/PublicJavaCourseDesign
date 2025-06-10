package GUI.Symbols;
import java.awt.*;
import javax.swing.*;
public class Pars extends JPanel{
    final static public int LEFT = 0, RIGHT = 1;
    JToggleButton[] parButtons;
    private final int side;
    public Pars(int side){
        this.side = side;
        initUI();
    }
    private void initUI(){
        String text = (side == LEFT)?"(":(side == RIGHT)?")":null;
        parButtons = new JToggleButton[2];
        parButtons[0] = new JToggleButton(text);
        parButtons[0].setBackground(Color.GRAY);
        parButtons[0].setForeground(Color.WHITE);
        parButtons[1] = new JToggleButton(text);
        parButtons[1].setBackground(Color.GRAY);
        parButtons[1].setForeground(Color.WHITE);
        parButtons[0].setFont(new Font("Arial", Font.BOLD, 20));
        parButtons[0].setMargin(new Insets(2,2,2,2));
        parButtons[1].setFont(new Font("Arial", Font.BOLD,20));
        parButtons[1].setMargin(new Insets(2,2,2,2));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        parButtons[0].setMaximumSize(new Dimension(30, 30));
        parButtons[0].setMinimumSize(new Dimension(25,25));
        parButtons[0].setPreferredSize(new Dimension(25,25));
        parButtons[1].setMaximumSize(new Dimension(30, 30));
        parButtons[1].setMinimumSize(new Dimension(25,25));
        parButtons[1].setPreferredSize(new Dimension(25,25));
        add(parButtons[0]);
        add(parButtons[1]);
    }
    public boolean[] getSelected(){
        return new boolean[] {
            (parButtons[0].getSelectedObjects() != null), 
            (parButtons[1].getSelectedObjects() != null)
        };
    }
    public void setEmpty(){
        parButtons[0].setSelected(false);
        parButtons[1].setSelected(false);
    }
}