import java.io.*;

/**
 * @author Velcaen
 * @version 1.0
 */
public class CodeWriter {

    private File file;
    private File outputFile;
    private BufferedWriter writer;
    //private ByteArrayOutputStream bos;

    int i = 0;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public CodeWriter(File file) throws FileNotFoundException {
        this.setFile(file);
        setFileName(file.getPath());
        this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
    }
//    public CodeWriter(MyFrame frame){
//        String text = frame.jTextAreaLeft.getText();
//
//    }

    public void setFileName(String fileName) {
        String outputFilePath = fileName.substring(0, fileName.lastIndexOf("\\")) + "\\" + fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.indexOf(".")) + ".asm";
        outputFile = new File(outputFilePath);
    }

    //栈顶指针的代码
    private void getTopSP() throws IOException {
        this.writer.write("//get the top element of stack\r\n"
                + "@SP\r\n"
                + "M=M-1\r\n"
                + "A=M\r\n"
                + "D=M\r\n");
    }

    //压入栈顶
    private void pushValueIntoStack(String value) throws IOException {
        this.writer.write("//push the value into stack\r\n"
                + "@SP\r\n"
                + "A=M\r\n"
                + "M=" + value + "\r\n"
                + "@SP\r\n"
                + "M=M+1\r\n");
    }

    private void doPushByArg(String arg1, String arg2) throws IOException {
        this.writer.write("@" + arg1 + "\r\n"
                + "D=M\r\n"
                + "@" + arg2 + "\r\n"
                + "A=D+A\r\n"
                + "D=M\r\n");
        pushValueIntoStack("D");
    }

    //在通用寄存器中存临时值
    private void storeValueInGR(String reg) throws IOException {
        this.writer.write("// store the result temporarily\r\n"
                + "@" + reg + "\r\n"
                + "M=D\r\n");
    }


    //获得栈顶两个值并将它们存在R13,R14
    private void getTopTwoElementOfStackAndStoreThemInReg() throws IOException {
        getTopSP();
        storeValueInGR("R14");
        getTopSP();
        storeValueInGR("R13");
    }

    //将寄存器中的值存入内存
    private void storeTheRegValueIntoMemory() throws IOException {
        this.writer.write("//store the top value\r\n"
                + "R13\r\n"
                + "A=M\r\n"
                + "M=D\r\n");
    }

    //获取R13的值，用于算术
    private void getValueFromR13() throws IOException {
        this.writer.write("@R13\r\n"
                + "D=M\r\n");
    }

    private void R13AddR14() throws IOException {
        getValueFromR13();
        this.writer.write("@R14\r\n"
                + "D=D+M\r\n");
    }

    private void R13MinusR14() throws IOException {
        getValueFromR13();
        this.writer.write("@R14\r\n"
                + "D=D-M\r\n");
    }

    public void writeArithmetic(Parser parser) throws IOException {
        String commandLabel = parser.args1();
        switch (commandLabel) {
            case "add":
                this.writer.write("//wm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                R13AddR14();
                pushValueIntoStack("D");
                this.writer.write("\r\n");
                break;
            case "sub":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                R13MinusR14();
                pushValueIntoStack("D");
                this.writer.write("\r\n");
                break;
            case "eq":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                R13MinusR14();
                this.writer.write("@EQ" + i + "\r\n"
                        + "D;JEQ\r\n");
                pushValueIntoStack("0");
                this.writer.write("@ENDEQ" + i + "\r\n" + "0;JMP" + "\r\n" + "(EQ" + i + ")\r\n");
                //-1 represent true, 0 represent false
                pushValueIntoStack("-1");
                this.writer.write("(ENDEQ" + (i++) + ")\r\n");
                this.writer.write("\r\n");
            case "gt":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                R13MinusR14();
                this.writer.write("@GT" + i + "\r\n"
                                        + "D;JGT\r\n");
                pushValueIntoStack("0");
                this.writer.write("ENDGT" + i + "\r\n"
                                        + "0;JMP\r\n"
                                        + "(GT" + i + ")\r\n");
                pushValueIntoStack("-1");
                this.writer.write("(ENDGT" + (i++) + ")\r\n");
                this.writer.write("\r\n");
                break;
            case "lt":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                R13MinusR14();
                this.writer.write("@LT" + i + "\r\n"
                        + "D;JLT\r\n");
                pushValueIntoStack("0");
                this.writer.write("ENDLT" + i + "\r\n"
                        + "0;JMP\r\n"
                        + "(LT" + i + ")\r\n");
                pushValueIntoStack("-1");
                this.writer.write("(ENDLT" + (i++) + ")\r\n");
                this.writer.write("\r\n");
                break;
            case "and":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                getValueFromR13();
                this.writer.write("@R14\r\n" + "D=D&M\r\n");
                pushValueIntoStack("D");
                this.writer.write("\r\n");
                break;
            case "or":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopTwoElementOfStackAndStoreThemInReg();
                getValueFromR13();
                this.writer.write("@R14\r\n" + "D=D|M\r\n");
                pushValueIntoStack("D");
                this.writer.write("\r\n");
                break;
            case "not":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopSP();
                this.writer.write("D=!D\r\n");
                pushValueIntoStack("D");
                this.writer.write("\r\n");
                break;
            case "neg":
                this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                getTopSP();
                this.writer.write( "@0\r\n" + "D=A-D\r\n");
                pushValueIntoStack("D");
                this.writer.write("\r\n");
                break;
            default:
                break;

        }

    }

    public void writePushPop(Parser parser) throws IOException {
        if(parser.commandType().equals(CommandType.C_PUSH)){
            String args1 = parser.args1();
            String args2 = parser.args2();
            switch (args1){
                case "argument":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    doPushByArg("ARG", args2);
                    this.writer.write("\r\n");
                    break;
                case "local":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    doPushByArg("LCL", args2);
                    this.writer.write("\r\n");
                    break;
                case "static":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    this.writer.write("@" + this.file.getName().substring(0, this.file.getName().indexOf(".") + 1)
                                                + args2 + "\r\n" + "D=M\r\n");
                    pushValueIntoStack("D");
                    this.writer.write("\r\n");
                    break;
                case "constant":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    this.writer.write("@" + args2 + "\r\n" + "D=A\r\n");
                    pushValueIntoStack("D");
                    this.writer.write("\r\n");
                    break;
                case "this":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    doPushByArg("THIS", args2);
                    this.writer.write("\r\n");
                    break;
                case "that":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    doPushByArg("THAT", args2);
                    this.writer.write("\r\n");
                    break;
                case "point":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    this.writer.write("@THIS\r\n"
                                            +"D=A\r\n"
                                            +"@" + args2 + "\r\n"
                                            +"A=D+A\r\n"
                                            +"D=M\r\n");
                    pushValueIntoStack("D");
                    this.writer.write("\r\n");
                    break;
                case "temp":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    this.writer.write("@RS\r\n"
                            +"D=A\r\n"
                            +"@" + args2 + "\r\n"
                            +"A=D+A\r\n"
                            +"D=M\r\n");
                    pushValueIntoStack("D");
                    this.writer.write("\r\n");
                    break;
                default:
                    break;

            }
        } else if (parser.commandType().equals(CommandType.C_POP)) {
            String args1 = parser.args1();
            String args2 = parser.args2();
            switch(args1){
                case "argument":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    this.writer.write("@ARG\r\n"
                            +"D=M\r\n"
                            +"@" + args2 + "\r\n"
                            +"D=D+A\r\n");
                    storeValueInGR("R13");
                    getTopSP();
                    storeTheRegValueIntoMemory();
                    this.writer.write("\r\n");
                    break;
                case "local":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    this.writer.write("@LCL\r\n"
                            +"D=M\r\n"
                            +"@" + args2 + "\r\n"
                            +"D=D+A\r\n");
                    storeValueInGR("R13");
                    getTopSP();
                    storeTheRegValueIntoMemory();
                    this.writer.write("\r\n");
                    break;
                case "static":
                    this.writer.write("//vm command:" + parser.getCurrentCommand());
                    getTopSP();
                    this.writer.write("@" + this.file.getName().substring(0, this.file.getName().indexOf(".") + 1)
                            + args2 + "\r\n" + "D=M\r\n");
                    this.writer.write("\r\n");
                    break;
                case "constant":
                    break;
                case "this":
                    this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                    this.writer.write("@THAT\r\n"+
                            "D=M\r\n"+
                            "@" + args2 + "\r\n"+
                            "D=D+A\r\n");
                    storeValueInGR("R13");
                    getTopSP();
                    storeTheRegValueIntoMemory();
                    this.writer.write("\r\n");
                    break;
                case "that":
                    this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                    this.writer.write("@THAT\r\n"+
                                            "D=M\r\n"+
                                            "@" + args2 + "\r\n"+
                                            "D=D+A\r\n");
                    storeValueInGR("R13");
                    getTopSP();
                    storeTheRegValueIntoMemory();
                    this.writer.write("\r\n");
                    break;
                case "pointer":
                    this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                    this.writer.write("@THIS\r\n"+
                            "D=A\r\n"+
                            "@" + args2 + "\r\n"+
                            "D=D+A\r\n");
                    storeValueInGR("R13");
                    getTopSP();
                    storeTheRegValueIntoMemory();
                    this.writer.write("\r\n");
                    break;
                case "temp":
                    this.writer.write("//vm command:" + parser.getCurrentCommand() + "\r\n");
                    this.writer.write("@5\r\n"+
                            "D=A\r\n"+
                            "@" + args2 + "\r\n"+
                            "D=D+A\r\n");
                    storeValueInGR("R13");
                    getTopSP();
                    storeTheRegValueIntoMemory();
                    this.writer.write("\r\n");
                    break;
                default:
                    break;
            }
        }else {
            System.out.println("this command is not push and pop");
        }
    }
    public void close() throws IOException {
        this.writer.close();
    }
}
