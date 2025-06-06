package gui.Game;
public class Operator{
    char op; // for identification
    String opString; // for output
    int level; // +- : 1, */ : 2
    int method; // +*: 1, -/ : -1
    public Operator(char op){
        this.op = op;
        opString = Character.toString(op);
        switch (op) {
            case '+': level = 1; method = 1; break;
            case '-': level = 1; method = -1; break;
            case '*': level = 2; method = 1; break;
            case '/': level = 2; method = -1; break;
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
            default -> -128;
        };
    }
}