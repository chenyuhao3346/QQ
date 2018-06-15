

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerSocketFrame extends JFrame {
	private static final long serialVersionUID = -4093003634422003654L;
	private JTextField tf_send;	// 声明文本框对象 tf_send
    private JTextArea ta_info;	// 声明文本域对象 ta_info
    private PrintWriter writer; // 声明打印流（字符处理输出流）PrintWriter对象writer
    private BufferedReader reader; // 声明字符缓冲输入流（处理流）BufferedReader对象reader
    private ServerSocket server; // 声明服务端套接字ServerSocket对象server
    private Socket socket; // 声明Socket对象socket
    
    public void getConnection() {
        try {
            server = new ServerSocket(2017); // 创建服务端套接字ServerSocket对象server
            ta_info.append(" 服务器套接字已经创建成功\n"); // 将提示信息显示在文本域中
            
            while (true) { 	// 如果套接字是连接状态
                ta_info.append(" 等待客户机的连接......\n"); // 将提示信息显示在文本域中
                socket = server.accept(); // 获取Socket对象（当服务端和客户端连接成功之后会返回一个Socket对象）
                /*
                 * 创建字符缓冲输入流（处理流）BufferedReader对象reader
                 * 	1、使用socket对象获取输入流is（该流是字节节点输入流）
                 * 	2、使用转换流 InputStreamReader将is转换为字符流isr
                 *  3、创建字符缓冲处理输入流BufferedReader的对象reader，将isr作为参数传入BufferedReader类的构造方法中
                 */
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                reader = new BufferedReader(isr); 
                
                OutputStream os = socket.getOutputStream();
                writer = new PrintWriter(os, true);
                
                getClientMessage(); 
            }
        } catch (Exception e) {
            e.printStackTrace(); // 将异常信息输出到控制台
        }
    }
    
    private void getClientMessage() {
        try {
            while (true) {
                String line = reader.readLine();// 读取客户端发送的信息
                if (line != null)
                    ta_info.append("接收到客户机发送的信息：" + line + "\n"); // 将提示信息显示在文本域中
            }
        } catch (Exception e) {
            ta_info.append("客户端已退出。\n"); // 将提示信息显示在文本域中
        } finally {
            try {
                if (reader != null) {
                    reader.close();// 关闭流
                }
                if (socket != null) {
                    socket.close(); // 关闭套接字
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) { // 主方法
        ServerSocketFrame frame = new ServerSocketFrame(); // 创建本类对象
        frame.setVisible(true);	// 设置窗体可见（窗体JFrame默认是不显示的）
        frame.getConnection(); // 调用方法
    }
    
    // 构造方法
    public ServerSocketFrame() {
        setTitle("服务器端程序");	// 设置窗体的标题
        setDefaultCloseOperation(EXIT_ON_CLOSE);	// 设置窗体的关闭方式
        setBounds(100, 100, 500, 300);	// 设置窗体的位置和大小
        setLayout(new BorderLayout());	// 设置窗体的布局方式为：边界布局
        
        JLabel title = new JLabel("一对一通信——服务器端程序",JLabel.CENTER);	// 创建JLabel标签对象title，并设置标签上的字体居中
        title.setForeground(Color.BLUE);	// 设置title标签上字体的颜色为蓝色
        title.setFont(new Font("", Font.BOLD, 22));	// 设置title标签上字体的名字、风格、大小
        add(title, BorderLayout.NORTH);	// 将title标签添加到窗体的北部
        
        JScrollPane scrollPane = new JScrollPane();	// 创建滚动面板（滚动条）对象scrollPane
        add(scrollPane, BorderLayout.CENTER);	// 将滚动条scrollPane添加到窗体的中部
        
        ta_info = new JTextArea();	// 创建文本域对象ta_info
        scrollPane.setViewportView(ta_info);	// 将文本域ta_info添加到滚动条scrollPane上面
        
        JPanel panel = new JPanel();	// 创建面板JPanel对象panel（JPanel容器的默认布局为流式布局）
        add(panel, BorderLayout.SOUTH);	// 将面板panel添加到窗体的南部
        
        JLabel label = new JLabel("服务器发送的信息:");	// 创建JLabel标签对象label
        panel.add(label);	// 将标签label添加到面板panel上
        
        tf_send = new JTextField(25);	// 创建文本框JTextField对象tf_send
        panel.add(tf_send);	// 将文本框tf_send添加到面板panel上

        JButton send = new JButton("发  送");	// 创建按钮JButton对象button
        
        // 采用匿名内部类的方式对按钮添加监听事件 
        send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                writer.println(tf_send.getText()); // 将文本框中信息写入流
                ta_info.append("服务器发送的信息是：" + tf_send.getText() + "\n"); // 将文本框中信息显示在文本域中
                tf_send.setText(""); // 清空文本框的内容
            }
        });
        panel.add(send);	// 将按钮button添加到面板panel上
    }
}
