package gui.Cards;
import javax.swing.*;
import java.net.URL;
public class Cards {
    //index 0 ~ 12: value 1 ~ 13
    public Card[] cardsSet;
    public Cards(){
        cardsSet = new Card[13];
        for (int i = 0; i < 13; i++) {
            cardsSet[i] = new Card(getClass().getResource("/gui/images/Cards/" + (i + 1) + ".png"), i + 1);
        }
    }
    public static class Card{ // public or private: public
        private final ImageIcon icon;
        private final int value;
        public Card(URL iFilename, int value){
            icon = new ImageIcon(iFilename);
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public ImageIcon getIcon(){
            return icon;
        }
    }
}