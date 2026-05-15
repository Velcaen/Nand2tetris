import java.util.Hashtable;

/**
 * @author Velcaen
 * @version 1.0
 */
public class SymbolTable {

    //字符串代表符号，数字代表符号的十进制地址
    Hashtable<String, Integer> table;
    //构建符号表时，需要添加已被定义的符号

    /**
     *
     * 预定义符号
     * SP -- 0
     * LCL -- 1
     * ARG -- 2
     * THIS -- 3
     * THAT -- 4
     * R0-R15 -- 0-15
     * SCREEN -- 16384  0x4000
     * KBD -- 24576     0x6000
     */
    public SymbolTable(){
        this.table = new Hashtable<String, Integer>();
        this.addEntry("SP",0);
        this.addEntry("LCL",1);
        this.addEntry("ARG",2);
        this.addEntry("THIS",3);
        this.addEntry("THAT",4);
        for (int i = 0; i < 16; i++){
            this.addEntry("R" + i, i);
        }
        this.addEntry("SCREEN", 16384);
        this.addEntry("KBD",24576);
    }
    //添加一个（符号，地址）配对
    public void addEntry(String symbol, Integer address){
        this.table.put(symbol, address);
    }

    //检查符号表是否配对符号
    public boolean contains(String symbol){
        if (this.table.get(symbol) != null){
            return true;
        }
        return false;
    }

    //获得给定符号的地址
    public int getAddress(String symbol){
        if (contains(symbol)){
            return this.table.get(symbol);
        }
        return -1;
    }

}
