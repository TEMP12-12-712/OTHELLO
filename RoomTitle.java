import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class RoomTitle extends JFrame{		//�^�C�g�����

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		RoomTitle frame = new RoomTitle("�^�C�g��");
	    frame.setVisible(true);

	}

	RoomTitle(String title){
	    setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    setLayout(new FlowLayout());
	    //�p�l���p��
	    JPanel panel = new JPanel();
	    JPanel panel1 = new JPanel();
	    JPanel panel2 = new JPanel();



	    panel1.setPreferredSize(new Dimension(200, 100));
	    panel2.setPreferredSize(new Dimension(200, 100));
	    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

	    //���x��
	    JLabel label = new JLabel("�I�Z��");
	    label.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 36));
	    panel2.add(label);

	    //�{�^���P
	    JButton button1=new JButton("���O�C��");
	    panel1.add(button1);
	    //�{�^���Q
	    JButton button2=new JButton("�V�K�o�^");
	    panel1.add(button2);

	    panel.add(panel2);
	    panel.add(panel1);




	    Container contentPane = getContentPane();
	    contentPane.add(panel, BorderLayout.CENTER);
	}





}
