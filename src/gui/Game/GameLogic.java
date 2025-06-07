package gui.Game;
class Calculate{
    Operator[] ops = {
        new Operator('+'), 
        new Operator('-'), 
        new Operator('*'), 
        new Operator('/')
    };
    public String solution(int... values){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++){
                if (j == i) continue;
                for (int k = 0; k < 4; k++) {
                    if (k == i || k == j) continue;
                    for (int m = 0; m < 4; m++) {
                        if (m == k || m == j || m == i) continue;
                        String way;
                        way = chanceOne(values[i], values[j], values[k], values[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceTwo(values[i], values[j], values[k], values[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceThree(values[i], values[j], values[k], values[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceFour(values[i], values[j], values[k], values[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceFive(values[i], values[j], values[k], values[m]);
                        if (!way.equals("-1")) return way;
                    }
                }
            }
        }
        return "-1";
    }
    //((axb)xc)xd
    private String chanceOne(int... values){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(values[0], values[1]);
            for (Operator j: ops){
                temp2 = j.operate(temp1, values[2]);
                for (Operator k: ops){
                    temp3 = k.operate(temp2, values[3]);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level <= i.level) par[2] = par[1] = "";
                        if (k.level <= j.level) par[0] = par[3] = "";
                        return par[0] + par[1] + values[0] + i.opString+ values[1]
                                + par[2] + j.opString + values[2]
                                + par[3] + k.opString + values[3];
                    }
                }
            }
        }
        return "-1";
    }
    // (ax(bxc))xd
    private String chanceTwo(int... values){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(values[1], values[2]);
            for (Operator j: ops){
                temp2 = j.operate(values[0], temp1);
                for (Operator k: ops){
                    temp3 = k.operate(temp2, values[3]);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level < i.level || (j.level == i.level && j.method == 1)) par[2] = par[1] = "";
                        if (k.level <= j.level) par[0] = par[3] = "";
                        return par[0]+ values[0]+j.opString+par[1]+ values[1]+i.opString+ values[2]+par[2]+par[3]+k.opString+ values[3];
                    }
                }
            }
        }
        return "-1";
    }
    //(axb)x(cxd)
    private String chanceThree(int... values){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(values[0], values[1]);
            for (Operator j: ops){
                temp2 = j.operate(values[2], values[3]);
                for (Operator k: ops){
                    temp3 = k.operate(temp1, temp2);
                    if (is24(temp3)){
                        String[] par = {"(",")","(",")"};
                        if (k.level <= i.level) par[0] = par[1] = "";
                        if (k.level < j.level || (k.level == j.level && k.level == 1)) par[2] = par[3] = "";
                        return par[0]+ values[0]+i.opString+ values[1]+par[1]+k.opString+par[2]+ values[2]+j.opString+ values[3]+par[3];
                    }
                }
            }
        }
        return "-1";
    }
    //ax((bxc)xd)
    private String chanceFour(int... values){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(values[1], values[2]);
            for (Operator j: ops){
                temp2 = j.operate(temp1, values[3]);
                for (Operator k: ops){
                    temp3 = k.operate(values[0], temp2);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level <= i.level) par[2] = par[1] = "";
                        if (k.level < j.level || (k.level == j.level && k.method == 1)) par[0] = par[3] = "";
                        return values[0]+k.opString+par[0]+par[1]+ values[1]+i.opString+ values[2]+par[2]+j.opString+ values[3]+par[3];
                    }
                }
            }
        }
        return "-1";
    }
    //ax(bx(cxd))
    private String chanceFive(int... values){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(values[2], values[3]);
            for (Operator j: ops){
                temp2 = j.operate(values[1], temp1);
                for (Operator k: ops){
                    temp3 = k.operate(values[0], temp2);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level < i.level || (j.level == i.level && j.method == 1)) par[2] = par[1] = "";
                        if (k.level < j.level || (k.level == j.level && k.method == 1)) par[0] = par[3] = "";
                        return values[0]+k.opString+par[0]+ values[1]+j.opString+par[1]+ values[2]+i.opString+ values[3]+par[2]+par[3];
                    }
                }
            }
        }
        return "-1";
    }
    private boolean is24 (double num){
        return Math.abs(24 - num) < 1e-3;  
    }
}
public class GameLogic extends Thread {
    //num,num,num,num,solution
    //separated by comma
    public final static String SEPARATOR = ",";
    public String questionString = null;
    //the overridden run method gets a new question set and store it in the string questionString
    @Override
    public void run(){
        String solution = "-1";
        int[] values = new int[4];
        while (solution.equals("-1")) {
            Calculate c = new Calculate();
            for (int i = 0; i < 4; i++) {
                values[i] = (int) ((Math.random()) * 13) + 1;
            }
            solution = c.solution(values);
        }
        int[] res = new int[]{-1, -1, -1, -1};
        boolean[] index = new boolean[]{false, false, false, false};
        for (int i = 0; i < 4; i++) {
            int tempIndex;
            do {
                tempIndex = (int) (Math.random() * 4);
            } while (index[tempIndex]);
            res[i] = values[tempIndex];
            index[tempIndex] = true;
        }
        questionString = res[0] + SEPARATOR
                + res[1] + SEPARATOR
                + res[2] + SEPARATOR
                + res[3] + SEPARATOR
                + solution;

    }
    public String getQuestionString(){
        return questionString;
    }
}