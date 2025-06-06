import gui.*;
import java.awt.*;
//main class
public class Application {
    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            // open welcome window
            var welcome = new Welcome();
            welcome.setVisible(true);
        });
    }
}