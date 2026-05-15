import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author Velcaen
 * @version 1.0
 */
public class MyFrame extends JFrame {
    File fileIn;
    File fileOut;
    JButton jButtonSave;
    JButton jButtonTra;
    JTextArea jTextAreaLeft;
    JTextArea jTextAreaRight;
    JTextArea jTextAreaSymbol;


    public MyFrame() {
        JFrame jFrame = new JFrame("编译器");
        //创建四个面板
        JPanel jPanelLeft = new JPanel();

        JPanel jPanelRight = new JPanel();

        JPanel jPanelButton = new JPanel();

        JPanel jPanelSymbol = new JPanel();

        //创建两个按钮
        jButtonSave = new JButton("save");
        jButtonTra = new JButton("translate");
        //在按钮面板添加按钮
        jPanelButton.add(jButtonSave, BorderLayout.WEST);
        jPanelButton.add(jButtonTra, BorderLayout.EAST);
        jPanelButton.setBackground(Color.GRAY);

        //在界面添加面板
        jFrame.add(jPanelLeft, BorderLayout.WEST);
        jFrame.add(jPanelRight, BorderLayout.EAST);
        jFrame.add(jPanelButton, BorderLayout.SOUTH);
        jFrame.add(jPanelSymbol, BorderLayout.CENTER);


        //创建文本域
        jTextAreaLeft = new JTextArea(32, 30);
        jTextAreaRight = new JTextArea(32, 30);
        jTextAreaSymbol = new JTextArea(25, 40);
        //设置字体大小
        Font font = new Font("宋体", Font.BOLD, 20);
        jTextAreaLeft.setFont(font);
        jTextAreaRight.setFont(font);
        jTextAreaSymbol.setFont(font);
        //将文本域放入滚动窗口
        JScrollPane jspLeft = new JScrollPane(jTextAreaLeft);    //将文本域放入滚动窗口
        Dimension size = jTextAreaLeft.getPreferredSize();    //获得文本域的首选大小
        jspLeft.setBounds(1, 1, size.width, size.height);

        JScrollPane jspRight = new JScrollPane(jTextAreaRight);
        Dimension size1 = jTextAreaRight.getPreferredSize();
        jspRight.setBounds(1, 1, size1.width, size1.height);

        JScrollPane jspCenter = new JScrollPane(jTextAreaSymbol);
        Dimension size2 = jTextAreaRight.getPreferredSize();
        jspCenter.setBounds(1, 1, size2.width, size2.height);
        //将按钮和滚动窗口放入面板
        //创建三个便签
        JLabel jLabel = new JLabel("符号表");
//        //创建两个按钮，显示文本输入内容
//        JButton jButton = new JButton("源码");
//        JButton jButton1 = new JButton("二进制");
//        jButton.setEnabled(false);
//        jButton1.setEnabled(false);
        JLabel jLabel1 = new JLabel("源码");
        JLabel jLabel2 = new JLabel("二进制");
        //再创建一个标签
        JLabel jLabel3 = new JLabel("-------------------------------------->");
        jLabel.setFont(font);
        jLabel1.setFont(font);
        jLabel2.setFont(font);
        jLabel3.setFont(new Font("宋体", Font.BOLD, 35));
        jPanelLeft.add(jLabel1);
        jPanelLeft.add(jspLeft);

        jPanelRight.add(jLabel2);
        jPanelRight.add(jspRight);


        jPanelSymbol.add(jLabel);
        jPanelSymbol.add(jspCenter);
        jPanelSymbol.add(jLabel3);


        //设置文本框面板

        jPanelLeft.setBackground(Color.GRAY);
        jPanelRight.setBackground(Color.GRAY);
        jPanelSymbol.setBackground(Color.GRAY);
        //设置jframe显示
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        //jFrame.setLocationRelativeTo(null);//居中
//        设置窗口全屏,法一
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice gd = ge.getDefaultScreenDevice();

        gd.setFullScreenWindow(jFrame);
        //法二
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        jFrame.setSize(screenSize.width, screenSize.height);

    }
}


//
//        jButtonSave.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    FileInputStream fileInputStream = new FileInputStream(fileIn);
//                    FileOutputStream fileOutputStream = new FileOutputStream(fileOut);
//                    byte[] buffer = new byte[2048];
//                    int length = 0;
//                    while ((length = fileInputStream.read(buffer)) != -1) {
//                        fileOutputStream.write(buffer, 0, length);
//                    }
//                } catch (IOException ex) {
//                    throw new RuntimeException();
//                }
//            }
//        });


//                try {
//                    InputStream in = new ByteArrayInputStream(text.getBytes());
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//                    byte[] buffer = new byte[2048];
//                    int length = 0;
//
//                    while ((length = in.read(buffer)) != -1) {
//                        bos.write(buffer, 0, length);//写入输出流
//                    }
//
//                    in.close();//读取完毕，关闭输入流
//                    bos.close();//关闭输出流
//
//                    str = bos.toString();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//
//                jTextAreaRight.setText(str);
