package gui.Game;
class Calculate{
    Operator[] ops = {
        new Operator('+'), 
        new Operator('-'), 
        new Operator('*'), 
        new Operator('/')
    };
    public String solution(int... nums){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++){
                if (j == i) continue;
                for (int k = 0; k < 4; k++) {
                    if (k == i || k == j) continue;
                    for (int m = 0; m < 4; m++) {
                        if (m == k || m == j || m == i) continue;
                        String way;
                        way = chanceOne(nums[i],nums[j],nums[k],nums[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceTwo(nums[i],nums[j],nums[k],nums[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceThree(nums[i],nums[j],nums[k],nums[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceFour(nums[i],nums[j],nums[k],nums[m]);
                        if (!way.equals("-1")) return way;
                        way = chanceFive(nums[i],nums[j],nums[k],nums[m]);
                        if (!way.equals("-1")) return way;
                    }
                }
            }
        }
        return "-1";
    }
    //((axb)xc)xd
    private String chanceOne(int... nums){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(nums[0], nums[1]);
            for (Operator j: ops){
                temp2 = j.operate(temp1, nums[2]);
                for (Operator k: ops){
                    temp3 = k.operate(temp2, nums[3]);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level <= i.level) par[2] = par[1] = "";
                        if (k.level <= j.level) par[0] = par[3] = "";
                        return par[0] + par[1] + nums[0] + i.opString+nums[1]
                                + par[2] + j.opString + nums[2]
                                + par[3] + k.opString + nums[3];
                    }
                }
            }
        }
        return "-1";
    }
    // (ax(bxc))xd
    private String chanceTwo(int... nums){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(nums[1], nums[2]);
            for (Operator j: ops){
                temp2 = j.operate(nums[0], temp1);
                for (Operator k: ops){
                    temp3 = k.operate(temp2, nums[3]);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level < i.level || (j.level == i.level && j.method == 1)) par[2] = par[1] = "";
                        if (k.level <= j.level) par[0] = par[3] = "";
                        return par[0]+nums[0]+j.opString+par[1]+nums[1]+i.opString+nums[2]+par[2]+par[3]+k.opString+nums[3];
                    }
                }
            }
        }
        return "-1";
    }
    //(axb)x(cxd)
    private String chanceThree(int... nums){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(nums[0], nums[1]);
            for (Operator j: ops){
                temp2 = j.operate(nums[2], nums[3]);
                for (Operator k: ops){
                    temp3 = k.operate(temp1, temp2);
                    if (is24(temp3)){
                        String[] par = {"(",")","(",")"};
                        if (k.level <= i.level) par[0] = par[1] = "";
                        if (k.level < j.level || (k.level == j.level && k.level == 1)) par[2] = par[3] = "";
                        return par[0]+nums[0]+i.opString+nums[1]+par[1]+k.opString+par[2]+nums[2]+j.opString+nums[3]+par[3];
                    }
                }
            }
        }
        return "-1";
    }
    //ax((bxc)xd)
    private String chanceFour(int... nums){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(nums[1], nums[2]);
            for (Operator j: ops){
                temp2 = j.operate(temp1, nums[3]);
                for (Operator k: ops){
                    temp3 = k.operate(nums[0], temp2);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level <= i.level) par[2] = par[1] = "";
                        if (k.level < j.level || (k.level == j.level && k.method == 1)) par[0] = par[3] = "";
                        return nums[0]+k.opString+par[0]+par[1]+nums[1]+i.opString+nums[2]+par[2]+j.opString+nums[3]+par[3];
                    }
                }
            }
        }
        return "-1";
    }
    //ax(bx(cxd))
    private String chanceFive(int... nums){
        double temp1, temp2, temp3;
        for (Operator i: ops){
            temp1 = i.operate(nums[2], nums[3]);
            for (Operator j: ops){
                temp2 = j.operate(nums[1], temp1);
                for (Operator k: ops){
                    temp3 = k.operate(nums[0], temp2);
                    if (is24(temp3)){
                        String[] par = {"(","(",")",")"};
                        if (j.level < i.level || (j.level == i.level && j.method == 1)) par[2] = par[1] = "";
                        if (k.level < j.level || (k.level == j.level && k.method == 1)) par[0] = par[3] = "";
                        return nums[0]+k.opString+par[0]+nums[1]+j.opString+par[1]+nums[2]+i.opString+nums[3]+par[2]+par[3];
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
        int[] nums = new int[4];
        while (solution.equals("-1")) {
            Calculate c = new Calculate();
            for (int i = 0; i < 4; i++) {
                nums[i] = (int) ((Math.random()) * 13) + 1;
            }
            solution = c.solution(nums);
        }
        int[] res = new int[]{-1, -1, -1, -1};
        boolean[] index = new boolean[]{false, false, false, false};
        for (int i = 0; i < 4; i++) {
            int tempIndex;
            do {
                tempIndex = (int) (Math.random() * 4);
            } while (index[tempIndex]);
            res[i] = nums[tempIndex];
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