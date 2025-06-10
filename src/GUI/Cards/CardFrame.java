package GUI.Cards;
import java.awt.Image;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
public class CardFrame extends JLabel {
    private final ImageIcon emptyIcon;
    private ImageIcon icon;
    public Cards.Card card;
    public CardFrame(){
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GUI/images/cardFrame.png")));
        emptyIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(111, 166, Image.SCALE_SMOOTH));
        icon = emptyIcon;
        setIcon(icon);
        card = null;
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e){
                int height = Math.min((int)(getWidth()*1.5), getHeight());
                int width = Math.min(getWidth(), (int)(getHeight()/1.5));
                icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
                setIcon(icon);
            }
        });
    }
    public void setFilled(Cards.Card card){this.card = card;
        ImageIcon a = card.getIcon();
        icon = new ImageIcon(a.getImage().getScaledInstance(156, 234, Image.SCALE_SMOOTH));
        setIcon(icon);
        int height = Math.min((int)(getWidth()*1.5), getHeight());
        int width = Math.min(getWidth(), (int)(getHeight()/1.5));
        if (width == 0 || height == 0){
            width = 156;
            height = 234;
        }
        icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        setIcon(icon);
    }
    public void setEmpty(){
        icon = new ImageIcon(emptyIcon.getImage().getScaledInstance(150, 225, Image.SCALE_SMOOTH));
        setIcon(icon);
        int height = Math.min((int)(getWidth()*1.5), getHeight());
        int width = Math.min(getWidth(), (int)(getHeight()/1.5));
        if (width == 0 || height == 0){
            width = 150;
            height = 225;
        }
        icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        setIcon(icon);
        card = null;
    }
    public boolean isEmpty(){
        return (card == null);
    }
}
