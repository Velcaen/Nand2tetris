import com.sun.source.tree.MemberReferenceTree;

import java.awt.geom.CubicCurve2D;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * @author Velcaen
 * @version 1.0
 */
public class Parser {
    private File file;
    private Scanner scanner;
    private String currentCommand;

    public Parser(File file, FileInputStream fiStr) {
        this.file = file;
        this.scanner = new Scanner(fiStr);
    }

    //是否有指令
    public boolean hasMoreCommand(){
        if(scanner.hasNextLine()){
            return true;
        }
        return false;
    }

    //读取
    public void advance(){
        if(hasMoreCommand()){
            do{
                this.currentCommand =this.scanner.nextLine();
            }while(!getCommand());
        }else{
            scanner.close();
        }
    }
    //处理指令
    public boolean getCommand(){
        String strLine = currentCommand;
        //去掉空行
        if(strLine.equals("")){
            return false;
        }
        //处理注释
        if(strLine.trim().substring(0, 2).equals("//")){
            return false;
        }
        //去掉注释
        if(strLine.contains("//")){
            currentCommand = strLine.substring(0, strLine.indexOf("//")).trim();
            return true;
        }
        currentCommand = strLine.trim();
        return true;
    }

    //指令类型
    public CommandType commandType(){
        String commandLabel = "";
        if(currentCommand.contains(" ")){
            commandLabel = currentCommand.substring(0, currentCommand.indexOf(" "));
        }else {
            commandLabel = currentCommand;
        }
        switch (commandLabel){
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                return CommandType.C_ARITHMETIC;
            case "pop":
                return CommandType.C_POP;
            case "push":
                return CommandType.C_PUSH;
            case "label":
                return CommandType.C_LABEL;
            case "if-goto":
                return CommandType.C_IF;
            case "function":
                return CommandType.C_FUNCTION;
            case "call":
                return CommandType.C_CALL;
            case "return":
                return CommandType.C_RETURN;
            default:
                break;
        }
        return null;
    }

    //获得第一个指令参数
    public String args1(){
        if(commandType().equals(CommandType.C_ARITHMETIC)){
            return currentCommand;
        }
        String args1 = "";
        if(commandType().equals(CommandType.C_LABEL)||
                commandType().equals(CommandType.C_GOTO)||
                commandType().equals(CommandType.C_IF)){
            args1 = currentCommand.substring(currentCommand.indexOf(" ") + 1, currentCommand.length());
        }else{
            args1 = currentCommand.substring(currentCommand.indexOf(" ") + 1, currentCommand.lastIndexOf(" "));
        }
        return args1;
    }

    //获得第二个指令参数
    public String args2(){
        return currentCommand.substring(currentCommand.indexOf(" ") + 1, currentCommand.length());
    }



    //定义一些get方法
    public File getFile() {
        return file;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String getCurrentCommand() {
        return currentCommand;
    }
}
