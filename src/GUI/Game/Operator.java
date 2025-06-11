package GUI.Game;
import GUI.ExceptionDial;
public class Operator{
    char op; // for identification
    String opString; // for output
    int level; // +- : 1, */ : 2
    boolean associates; // +*: 1, -/ : -1
    public Operator(char op){
        this.op = op;
        opString = Character.toString(op);
        // level: operate priority (+, - = 1; *, / = 2; () += 10 )
        // associates: +, * = 1 (does not change the operate behind); -, / = -1 (changes)
        switch (op) {
            case '+': level = 1; associates = true; break;
            case '-': level = 1; associates = false; break;
            case '*': level = 2; associates = true; break;
            case '/': level = 2; associates = false; break;
        }
    }
    public String getString(){
        return opString;
    }
    public double operate(double a, double b){
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> {
                new ExceptionDial("运算符推演错误。");
                throw new RuntimeException("Wrong Operator");
            }
        };
    }
}