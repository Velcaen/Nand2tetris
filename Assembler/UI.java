import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author Velcaen
 * @version 1.0
 */
public class UI {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        Assembler assembler = new Assembler(frame);
        frame.jButtonTra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    assembler.firstScanByStr();
                    assembler.secondScanByStr();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


}
