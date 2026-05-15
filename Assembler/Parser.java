import javax.crypto.interfaces.PBEKey;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.foreign.SymbolLookup;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Velcaen
 * @version 1.0
 */
//解析器
    //解析汇编语言语句
public class Parser {
    Scanner scanner;
    File file;
    String currentCommand;
    int address = 0;
    //定义一个集合
    List<String> validInstructionSet;
    //构造器
    public Parser(File file) throws FileNotFoundException {
        this.file = file;
        scanner = new Scanner(new FileInputStream(this.file));
        //解决 Cannot invoke "java.util.List.add(Object)" because "this.validInstructionSet" is null
        validInstructionSet = new ArrayList<>();
    }
    public Parser(String str){
        scanner = new Scanner(new ByteArrayInputStream(str.getBytes()));
        validInstructionSet = new ArrayList<>();
    }

    //确定文件是否还有更多的指令
    public boolean hasMoreCommands(){
        if(this.scanner.hasNextLine()){
            return true;
        }
        return false;
    }

    //获得新一行的内容
    public void advance(){
        if(hasMoreCommands()){
            do{
                this.currentCommand = this.scanner.nextLine();
                System.out.println(this.currentCommand);
            }while (!getCommand());
            //当获得A指令或C指令时，让指针加一
            if(this.commandType().equals(CommandType.A_COMMAND) || this.commandType().equals(CommandType.C_COMMAND)){
                this.address++;
            }
            validInstructionSet.add(this.currentCommand);
        }else{
            this.scanner.close();
        }
    }




    //处理指令
    private boolean getCommand(){
        String stringLine = this.currentCommand;
        //1.移除空行
        if(stringLine.equals("")){
            return false;
        }
        //2.移除注释行
        String headChar = stringLine.trim().substring(0,2);
        if(headChar.equals("//")){
            return false;
        }
        //3.移除语句后的注释，获取//前的语句内容
        if(stringLine.contains("//")){
            this.currentCommand = stringLine.substring(0, stringLine.indexOf("//")).trim();
            return true;
        }
        this.currentCommand = stringLine.trim();
        return true;
    }

    //通过第一个字符来确定语句，分辨它是哪条指令
    public CommandType commandType(){
        if(this.currentCommand.substring(0,1).equals("@")){
            return CommandType.A_COMMAND;
        }
        if(this.currentCommand.substring(0, 1).equals("(")){
            return CommandType.L_COMMAND;
        }
        if(this.currentCommand.contains(";")||this.currentCommand.contains("=")){
            return CommandType.C_COMMAND;
        }
        return CommandType.UNKNOWN;
    }



    //判断符号是否是一个数字符号
    public boolean isDigit(String symbol){
        for (int i = 0; i < symbol.length(); i++) {
            if(!Character.isDigit(symbol.charAt(i))){
                return false;
            }
        }
        return true;
    }


    //将位填充位15位
    public String paddingZero(String str){
        while(str.length() <= 15){
            str = "0" + str;
        }
        return str;
    }

    //获取有内容的指令
    public String symbol(){
        String subStr = null;
        if(commandType().equals(CommandType.A_COMMAND)){
            subStr = this.currentCommand.substring(1, this.currentCommand.length());
        }
        if(commandType().equals(CommandType.L_COMMAND)){
            subStr = this.currentCommand.substring(1, this.currentCommand.length() - 1);
        }
        if(subStr == null){
            return null;
        }
        if(isDigit(subStr)){
            Long address = Long.parseLong(subStr);
            if(address < 0 || address > 32767){

            }
            String addressStr = Long.toBinaryString(address);
            return paddingZero(addressStr);
        }
        return subStr;
    }


    //dest域
    public String dest(){
        if (commandType().equals(CommandType.C_COMMAND)){
            String command = this.currentCommand;
            if(command.contains("=")){
                return command.substring(0, command.indexOf("="));
            }else{
                return "";
            }
        }
        return null;
    }

    //comp域
    public String comp(){
        if(commandType().equals(CommandType.C_COMMAND)){
            String command = this.currentCommand;
            if(command.contains("=") && command.contains(";")){
                return command.substring(command.indexOf("=") + 1, command.indexOf(";"));
            } else if (command.contains(";")) {
                return command.substring(0, command.indexOf(";"));
            }else if(command.contains("=")){
                return command.substring(command.indexOf("=")+1, command.length());
            }else {
                return "";
            }
        }
        return null;
    }


    //jump域
    public String jump(){
        if(commandType().equals(CommandType.C_COMMAND)){
            String command = this.currentCommand;
            if(command.contains(";")){
                return command.substring(command.indexOf(";")+1, command.length());
            }else {
                return "";
            }
        }
        return null;
    }



}
