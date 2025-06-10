import GUI.*;
import java.awt.*;
public class Application {
    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            // open welcome window
            var welcome = new Welcome();
            welcome.setVisible(true);
        });
    }
}