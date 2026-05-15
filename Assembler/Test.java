import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Velcaen
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Assembler assembler = new Assembler(new File("./add/Add.asm"));
        assembler.firstScanByFile();
        assembler.secondScanByFile();
    }
}
