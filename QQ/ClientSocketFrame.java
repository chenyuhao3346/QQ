

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
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientSocketFrame extends JFrame {
	
	private static final long serialVersionUID = 5960164514103903315L;
	private PrintWriter writer; 	// ������ӡ�����ַ������������PrintWriter����writer
    private BufferedReader reader;  // �����ַ���������������������BufferedReader����reader
    private Socket socket;			 // �����ͻ����׽���Socket����
    private JTextArea ta_info;		 // �����ı������ ta_info
    private JTextField tf_send;		 // �����ı������ tf_send
    
    // ���췽��
    public ClientSocketFrame() { 
        super();
        setTitle("�ͻ��˳���");		// ���ô���ı���
        setDefaultCloseOperation(EXIT_ON_CLOSE);	// ���ô���Ĺرշ�ʽ
        setBounds(100, 100, 500, 300);	// ���ô����λ�úʹ�С
        setLayout(new BorderLayout());	// ���ô���Ĳ��ַ�ʽΪ���߽粼��

        JLabel title = new JLabel("һ��һͨ�š����ͻ��˳���", JLabel.CENTER);  // ����JLabel��ǩ����title�������ñ�ǩ�ϵ��������
        title.setForeground(Color.BLUE);	// ����title��ǩ���������ɫΪ��ɫ
        title.setFont(new Font("", Font.BOLD, 22));	// ����title��ǩ����������֡���񡢴�С
        add(title, BorderLayout.NORTH);	// ��title��ǩ��ӵ�����ı���
        
        JScrollPane scrollPane = new JScrollPane();	// ����������壨������������scrollPane
        add(scrollPane, BorderLayout.CENTER);	// ��������scrollPane��ӵ�������в�
        
        ta_info = new JTextArea();	// �����ı������ta_info
        scrollPane.setViewportView(ta_info);	// ���ı���ta_info��ӵ�������scrollPane����

        JPanel panel = new JPanel();	// �������JPanel����panel��JPanel������Ĭ�ϲ���Ϊ��ʽ���֣�
        add(panel, BorderLayout.SOUTH);	// �����panel��ӵ�������ϲ�
        JLabel label = new JLabel("�ͻ��˷��͵���Ϣ:");   // ����JLabel��ǩ����label
        panel.add(label);	// ����ǩlabel��ӵ����panel��

        tf_send = new JTextField(25);	// �����ı���JTextField����tf_send
        panel.add(tf_send);	// ���ı���tf_send��ӵ����panel��

        JButton send = new JButton("��  ��");	// ������ťJButton����button
        // ���������ڲ���ķ�ʽ�԰�ť��Ӽ����¼�
        send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                writer.println(tf_send.getText()); // ���ı�������Ϣд����
                ta_info.append("�ͻ��˷��͵���Ϣ�ǣ�" + tf_send.getText() + "\n"); 	// ���ı�������Ϣ��ʾ���ı�����
                tf_send.setText(""); 	// ����ı��������
            }
        });
        panel.add(send);	// ����ťbutton��ӵ����panel��
    }
    
    // ���������ӷ���
    private void connect() { 
        ta_info.append("��������......\n"); // ����ʾ��Ϣ��ʾ���ı�����
        try { // ��׽�쳣
        	// ����ָ���� IP ��ַ�Ͷ˿ںŴ����ͻ����׽��ֶ���socket��IP��ַ�Ƿ���˵�IP��ַ���˿ں����ɷ����ָ���ģ�
            socket = new Socket("localhost", 2017); 
            while (true) {
            	OutputStream os = socket.getOutputStream();
                writer = new PrintWriter(os, true);
                /*
                 * �����ַ���������������������BufferedReader����reader
                 * 	1��ʹ��socket�����ȡ������is���������ֽڽڵ���������
                 * 	2��ʹ��ת���� InputStreamReader��isת��Ϊ�ַ���isr
                 *  3�������ַ����崦��������BufferedReader�Ķ���reader����isr��Ϊ��������BufferedReader��Ĺ��췽����
                 */
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                reader = new BufferedReader(isr); 
                ta_info.append("������ӡ�\n"); // ����ʾ��Ϣ��ʾ���ı�����
                readServerMessage();
            }
        } catch (Exception e) {
            e.printStackTrace(); // ����쳣��Ϣ
        }
    }
    
    // ��ȡ����˷��͵���Ϣ
    private void readServerMessage() {
        try {
            while (true) {
                if (reader != null) {
                    String line = reader.readLine();	// ��ȡ���������͵���Ϣ
                    if (line != null)
                        ta_info.append("���յ����������͵���Ϣ��" + line + "\n"); // ��ʾ�������˷��͵���Ϣ
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();	// �ر���
                }
                if (socket != null) {
                    socket.close(); 	// �ر��׽���
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) { 
        ClientSocketFrame clien = new ClientSocketFrame(); 
        clien.setVisible(true); // ���ô���ɼ�
        clien.connect(); 	// �������ӷ���
    }
    
}
