

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
	private JTextField tf_send;	// �����ı������ tf_send
    private JTextArea ta_info;	// �����ı������ ta_info
    private PrintWriter writer; // ������ӡ�����ַ������������PrintWriter����writer
    private BufferedReader reader; // �����ַ���������������������BufferedReader����reader
    private ServerSocket server; // ����������׽���ServerSocket����server
    private Socket socket; // ����Socket����socket
    
    public void getConnection() {
        try {
            server = new ServerSocket(2017); // ����������׽���ServerSocket����server
            ta_info.append(" �������׽����Ѿ������ɹ�\n"); // ����ʾ��Ϣ��ʾ���ı�����
            
            while (true) { 	// ����׽���������״̬
                ta_info.append(" �ȴ��ͻ���������......\n"); // ����ʾ��Ϣ��ʾ���ı�����
                socket = server.accept(); // ��ȡSocket���󣨵�����˺Ϳͻ������ӳɹ�֮��᷵��һ��Socket����
                /*
                 * �����ַ���������������������BufferedReader����reader
                 * 	1��ʹ��socket�����ȡ������is���������ֽڽڵ���������
                 * 	2��ʹ��ת���� InputStreamReader��isת��Ϊ�ַ���isr
                 *  3�������ַ����崦��������BufferedReader�Ķ���reader����isr��Ϊ��������BufferedReader��Ĺ��췽����
                 */
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                reader = new BufferedReader(isr); 
                
                OutputStream os = socket.getOutputStream();
                writer = new PrintWriter(os, true);
                
                getClientMessage(); 
            }
        } catch (Exception e) {
            e.printStackTrace(); // ���쳣��Ϣ���������̨
        }
    }
    
    private void getClientMessage() {
        try {
            while (true) {
                String line = reader.readLine();// ��ȡ�ͻ��˷��͵���Ϣ
                if (line != null)
                    ta_info.append("���յ��ͻ������͵���Ϣ��" + line + "\n"); // ����ʾ��Ϣ��ʾ���ı�����
            }
        } catch (Exception e) {
            ta_info.append("�ͻ������˳���\n"); // ����ʾ��Ϣ��ʾ���ı�����
        } finally {
            try {
                if (reader != null) {
                    reader.close();// �ر���
                }
                if (socket != null) {
                    socket.close(); // �ر��׽���
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) { // ������
        ServerSocketFrame frame = new ServerSocketFrame(); // �����������
        frame.setVisible(true);	// ���ô���ɼ�������JFrameĬ���ǲ���ʾ�ģ�
        frame.getConnection(); // ���÷���
    }
    
    // ���췽��
    public ServerSocketFrame() {
        setTitle("�������˳���");	// ���ô���ı���
        setDefaultCloseOperation(EXIT_ON_CLOSE);	// ���ô���Ĺرշ�ʽ
        setBounds(100, 100, 500, 300);	// ���ô����λ�úʹ�С
        setLayout(new BorderLayout());	// ���ô���Ĳ��ַ�ʽΪ���߽粼��
        
        JLabel title = new JLabel("һ��һͨ�š����������˳���",JLabel.CENTER);	// ����JLabel��ǩ����title�������ñ�ǩ�ϵ��������
        title.setForeground(Color.BLUE);	// ����title��ǩ���������ɫΪ��ɫ
        title.setFont(new Font("", Font.BOLD, 22));	// ����title��ǩ����������֡���񡢴�С
        add(title, BorderLayout.NORTH);	// ��title��ǩ��ӵ�����ı���
        
        JScrollPane scrollPane = new JScrollPane();	// ����������壨������������scrollPane
        add(scrollPane, BorderLayout.CENTER);	// ��������scrollPane��ӵ�������в�
        
        ta_info = new JTextArea();	// �����ı������ta_info
        scrollPane.setViewportView(ta_info);	// ���ı���ta_info��ӵ�������scrollPane����
        
        JPanel panel = new JPanel();	// �������JPanel����panel��JPanel������Ĭ�ϲ���Ϊ��ʽ���֣�
        add(panel, BorderLayout.SOUTH);	// �����panel��ӵ�������ϲ�
        
        JLabel label = new JLabel("���������͵���Ϣ:");	// ����JLabel��ǩ����label
        panel.add(label);	// ����ǩlabel��ӵ����panel��
        
        tf_send = new JTextField(25);	// �����ı���JTextField����tf_send
        panel.add(tf_send);	// ���ı���tf_send��ӵ����panel��

        JButton send = new JButton("��  ��");	// ������ťJButton����button
        
        // ���������ڲ���ķ�ʽ�԰�ť��Ӽ����¼� 
        send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                writer.println(tf_send.getText()); // ���ı�������Ϣд����
                ta_info.append("���������͵���Ϣ�ǣ�" + tf_send.getText() + "\n"); // ���ı�������Ϣ��ʾ���ı�����
                tf_send.setText(""); // ����ı��������
            }
        });
        panel.add(send);	// ����ťbutton��ӵ����panel��
    }
}
