package gui.PlayerSystem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
//this class is responsible for input and output of the player information file
//issue: text file should be replaced by database
public class PlayersIO {
    private File file;
    private ArrayList<String> lines;
    public final static int CREATE_SUCCEED = 0, PLAYER_ALREADY_EXIST = -1;
    public final static String SEPARATOR = "h", GUEST_NAME = "Guest";
    public final static int HIGH_SCORE_NOT_FOUND = -1;
    public PlayersIO(){
        checkFile();
    }
    private void checkFile(){
        try {
            //create if file and/or direction doesn't exist, then set guest account
            File direction = new File(System.getProperty("user.home") + "/Saved Games/TwentyFour");
            file = new File(System.getProperty("user.home") + "/Saved Games/TwentyFour/users.txt");
            lines = new ArrayList<>();
            if (!direction.exists()){
                boolean ignored = direction.mkdirs();
            }
            if (!file.exists()){
                boolean ignored = file.createNewFile();
                setGuest();
            } else
                readFile();
        } catch (IOException e) {
            exceptionDialog("文件路径异常");
        }
    }
    public void setGuest(){
        //create or reset guest profile
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(GUEST_NAME + SEPARATOR + 0 + SEPARATOR + 0);
        } catch (IOException e) {
            exceptionDialog("文件写入异常");
        }
    }
    // return -1 for name already exist
    // return 0 for succeed
    public int newPlayer(String name, char[] password){
        readFile();
        for (String i : lines){
            if (Integer.toHexString(name.hashCode()).equals(i.split(SEPARATOR)[0]))
                return PLAYER_ALREADY_EXIST;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            StringBuilder pass = new StringBuilder();// more efficient than String
            for (char i: password){
                pass.append(i);
            }
            for (String i: lines){
                writer.write(i +"\n");
            }
            writer.write(
                    Integer.toHexString(name.hashCode())
                        + SEPARATOR
                        + Integer.toHexString(pass.toString().hashCode())
                        + SEPARATOR + 0 + SEPARATOR + 0
            );
        } catch (IOException e) {
            exceptionDialog("文件写入异常");
        }
        return CREATE_SUCCEED;
    }
    public boolean checkPlayerPass(String name, char[] password){
        readFile();
        StringBuilder pass = new StringBuilder();
        for (char i: password){
            pass.append(i);
        }
        for (String i: lines){
            String[] temp = i.split(SEPARATOR,3);
            if (Integer.toHexString(name.hashCode()).equals(temp[0])
                && Integer.toHexString(pass.toString().hashCode()).equals(temp[1]))
                return true;
        }
        return false;
    }
    //this method changes high in any case as called. must check high.
    public void setPlayerHigh(String name, int score, int mode){
        readFile();
        ArrayList<String> tempLines = new ArrayList<>();
        if (name.equals(GUEST_NAME)){
            for (String i : lines){
                if (i.split(SEPARATOR)[0].equals(name)){
                	String[] temp = i.split(SEPARATOR);
                    temp[mode + 1] = Integer.toString(score);
                    tempLines.add(temp[0] + SEPARATOR + temp[1] + SEPARATOR + temp[2]);
                }
                else
                	tempLines.add(i) ;           
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String i: tempLines){
                    writer.write(i+"\n");
                }
            } catch (IOException e) {
                exceptionDialog("文件写入异常");
            }
            return;
        }
        for (String i: lines){
            if (i.split(SEPARATOR)[0].equals(Integer.toHexString(name.hashCode()))){
                String[] temp = i.split(SEPARATOR);
                temp[mode+2] = Integer.toString(score);
                tempLines.add(temp[0] + SEPARATOR+temp[1] + SEPARATOR+temp[2] + SEPARATOR + temp[3]);
            }
            else
                tempLines.add(i);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String i: tempLines){
                writer.write(i+"\n");
            }
        } catch (IOException e) {
            exceptionDialog("文件写入异常");
        }
    }
    public int[] getPlayerHigh(String name){
        readFile();
        int[] high = new int[2];
        if (name.equals(GUEST_NAME)){
            for (String i : lines) {
                if (i.split(SEPARATOR)[0].equals(GUEST_NAME)){
                    high[0] = Integer.parseInt(i.split(SEPARATOR)[1]);
                    high[1] = Integer.parseInt(i.split(SEPARATOR)[2]);
                    return high;
                }
            }
        }
        for (String i: lines){
            if (i.split(SEPARATOR)[0].equals(Integer.toHexString(name.hashCode()))){
                high[0] = Integer.parseInt(i.split(SEPARATOR)[2]);
                high[1] = Integer.parseInt(i.split(SEPARATOR)[3]);
                return high;
            }
        }
        high[0] = HIGH_SCORE_NOT_FOUND; high[1] = HIGH_SCORE_NOT_FOUND;
        exceptionDialog("最高分查找异常");
        return high;
    }
    public int[] getUniversalHigh(){
        readFile();
        int[] high = new int[2];
        for (String line: lines){
            String[] temp = line.split(SEPARATOR);
            if (temp[0].equals(GUEST_NAME)){
                high[0] = Math.max(Integer.parseInt(temp[1]), high[0]);
                high[1] = Math.max(Integer.parseInt(temp[2]), high[1]);
            }
            else {
            high[0] = Math.max(Integer.parseInt(temp[2]), high[0]);
            high[1] = Math.max(Integer.parseInt(temp[3]), high[1]);
            }
        }
        return high;
    }
    private void readFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            lines.clear();
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
        } catch (Exception e) {
            exceptionDialog("文件读取异常");
        }
        if (lines.isEmpty())
            resetFile();
        //check if guest account exists
        boolean guestAccountExists = false;
        for (String i : lines) {
            String[] temp = i.split(SEPARATOR);
            if(temp[0].equals(GUEST_NAME))
                if (temp.length == 3)
                    guestAccountExists = true;
            else if (temp.length != 4) 
                resetFile();
        }
        if (!guestAccountExists)
            resetFile();
    }
    private void resetFile(){
        var reset = new JDialog();
        initResetDialUI(reset);
        reset.setVisible(true);
    }
    private void initResetDialUI(JDialog reset){
        reset.setModal(true);
        reset.setTitle("错误");
        reset.setSize(280,190);
        reset.setLocationRelativeTo(null);
        reset.setResizable(false);
        // to avoid disappearing after losing focus
        reset.setAlwaysOnTop(true);
        reset.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        var info = new JLabel("用户数据文件已损坏。");
        var button = new JButton("重设");
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        var pane = reset.getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        var sg1 = gl.createSequentialGroup().addComponent(info);
        var sg2 = gl.createSequentialGroup().addGap(0,0,Integer.MAX_VALUE).addComponent(button);
        var pg1 = gl.createParallelGroup().addComponent(info);
        var pg2 = gl.createParallelGroup().addComponent(button);
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(sg1).addGroup(sg2));
        gl.setVerticalGroup(gl.createSequentialGroup().addGroup(pg1).addGap(0,0,Integer.MAX_VALUE).addGroup(pg2));
        var buttonA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                setGuest();
                reset.dispose();
            }
        };      
        button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"button");
        button.getActionMap().put("button",buttonA);
        button.addActionListener(buttonA);
    }
    public void exceptionDialog(String message){
        var ex = new JDialog();
        ex.setTitle("异常");
        ex.setModal(true);
        ex.setSize(300, 200);
        ex.setAlwaysOnTop(true);
        ex.setResizable(false);
        ex.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ex.setLocationRelativeTo(null);
        var mess = new JLabel(message, SwingConstants.CENTER);
        mess.setVerticalAlignment(SwingConstants.CENTER);
        mess.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        ex.add(mess);
        ex.setVisible(true);
    }
}