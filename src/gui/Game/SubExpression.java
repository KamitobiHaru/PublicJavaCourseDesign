package gui.Game;

public class SubExpression {
    private double num1, num2;
    private Operator operator;
    private int level;
    private int opIndex;
    private final static int NOT_USED = -1;
    public SubExpression(){}
    public SubExpression(double num1, double num2, Operator operator){
        this.num1 = num1;
        this.num2 = num2;
        this.operator = operator;
        this.level = operator.level;
        this.opIndex = NOT_USED;
    }
    public void setOpIndex(int opIndex){
        this.opIndex = opIndex;
    }
    public int getOpIndex(){
        return opIndex;
    }
    public void setOperator(Operator operator){
        this.operator = operator;
    }
    public void setNum1(double num1){
        this.num1 = num1;
    }
    public void setNum2(double num2){
        this.num2 = num2;
    }
    public double operate(){
        return operator.operate(num1, num2);
    }
    public void setLevel(int level){
        this.level = level;
    }
    public int getLevel(){
        return level;
    }
}
