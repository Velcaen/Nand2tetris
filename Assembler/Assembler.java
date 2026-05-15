import java.awt.*;
import java.io.*;


/**
 * @author Velcaen
 * @version 1.0
 */
public class Assembler {

    File file;
    String strIn;
    private MyFrame myFrame;
    BufferedWriter writer;
    ByteArrayOutputStream bos;
    Code code;
    SymbolTable symbolTable;
    Parser parser;
    int freeVarAddress = 16;

    //认为一个文件有一个符号表
    public Assembler(File file) throws FileNotFoundException {
        this.file = file;
        String fileName = file.getName();
        String binFilePath = file.getParent() + "\\" + fileName.substring(0, fileName.indexOf(".")) + ".cmp";
        this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(binFilePath)));
        this.code = new Code();
        this.symbolTable = new SymbolTable();
    }
    public Assembler(MyFrame frame){
        myFrame =  frame;
       // InputStream in = new ByteArrayInputStream(str.getBytes());
        bos = new ByteArrayOutputStream();
        this.code = new Code();
        this.symbolTable = new SymbolTable();
    }

    //第一次扫描，收集符号并添加到符号表
    public void firstScan(){
        do {
            parser.advance();
            CommandType commandType = parser.commandType();
            switch(commandType){
                case L_COMMAND:
                    String subL = parser.currentCommand.substring(1, parser.currentCommand.length() - 1);
                    if(!parser.isDigit(subL)){
                        this.symbolTable.addEntry(subL, parser.address);
                    }
                    break;
                default:
                    break;
            }
        }while(parser.hasMoreCommands());
    }
    //通过文件扫描
    public void firstScanByFile() throws FileNotFoundException {
        this.parser = new Parser(this.file);
        firstScan();
    }
    //通过字符串扫描
    public void firstScanByStr() throws FileNotFoundException {
        this.strIn = myFrame.jTextAreaLeft.getText();
        this.parser = new Parser(this.strIn);
       firstScan();
    }

    //第二次扫描
    //翻译程序文件，且L指令不需要翻译
    public void secondScanByFile() throws IOException {
        for(String validInstruction : parser.validInstructionSet) {
            parser.currentCommand = validInstruction;
            CommandType commandType = parser.commandType();
            switch(commandType){
                case A_COMMAND :
                    String subStr = parser.currentCommand.substring(1, parser.currentCommand.length());
                    if(parser.isDigit(subStr)){
                        System.out.println(parser.currentCommand+"\t\t"+parser.symbol());
                        writer.write(parser.symbol() + "\n");
                    }else{
                        int address = 0;
                        if(this.symbolTable.contains(subStr)){
                            address = this.symbolTable.getAddress(subStr);
                        }else {
                            this.symbolTable.addEntry(subStr, this.freeVarAddress);
                            address = this.freeVarAddress;
                        }

                        String addressStr = parser.paddingZero(Integer.toBinaryString(address));
                        System.out.println(parser.currentCommand+"\t\t"+addressStr);
                        writer.write(addressStr+"\n");

                    }
                    break;
                case C_COMMAND:
                    System.out.println(parser.currentCommand + "\t\t");
                    String dest = code.dest(parser.dest());
                    String comp = code.comp(parser.comp());
                    String jump = code.jump(parser.jump());
                    //完整C指令
                    String binCmd = "111" + comp + dest + jump;
                    System.out.println(binCmd);
                    writer.write(binCmd+ "\n");
                    break;
                case L_COMMAND:
                    System.out.println(parser.currentCommand);
                    break;
                default:
                    break;
            }
        }
    }

    public void secondScanByStr() throws IOException {
        for(String validInstruction : parser.validInstructionSet) {
            parser.currentCommand = validInstruction;
            CommandType commandType = parser.commandType();
            switch(commandType){
                case A_COMMAND :
                    String subStr = parser.currentCommand.substring(1, parser.currentCommand.length());
                    if(parser.isDigit(subStr)){
                        bos.write((parser.symbol() + "\n").getBytes());
                        myFrame.jTextAreaRight.setText(bos.toString()+"\n");
                    }else{
                        int address = 0;
                        if(this.symbolTable.contains(subStr)){
                            address = this.symbolTable.getAddress(subStr);
                        }else {
                            this.symbolTable.addEntry(subStr, this.freeVarAddress);
                            address = this.freeVarAddress;
                        }

                        String addressStr = parser.paddingZero(Integer.toBinaryString(address));
                        bos.write((addressStr+"\n").getBytes());
                        myFrame.jTextAreaRight.setText(bos.toString()+ "\n");
                    }
                    break;
                case C_COMMAND:
                    System.out.println(parser.currentCommand + "\t\t");
                    String dest = code.dest(parser.dest());
                    String comp = code.comp(parser.comp());
                    String jump = code.jump(parser.jump());
                    //完整C指令
                    String binCmd = "111" + comp + dest + jump;
                    bos.write((binCmd+"\n").getBytes());
                    myFrame.jTextAreaRight.setText(bos.toString()+ "\n");
                    break;
                case L_COMMAND:
                    System.out.println(parser.currentCommand);
                    break;
                default:
                    break;
            }
        }
            myFrame.jTextAreaSymbol.setText(parser.validInstructionSet.toString());
    }

}
