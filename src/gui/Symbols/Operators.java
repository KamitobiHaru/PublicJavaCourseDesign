package gui.Symbols;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
public class Operators extends JComboBox<String>{
    public Operators(){
        super(new String[] {"  ","+","-","*","/"});
        setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }
    public void setEmpty(){
        setSelectedIndex(0);
    }
}
