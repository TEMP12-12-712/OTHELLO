import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RoomRecord1 extends JFrame{

	public static void main(String[] args) {
		RoomRecord1 frame = new RoomRecord1("�L�^�I��");
	    frame.setVisible(true);

	}

	RoomRecord1(String title){
		setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //�p�l���̗p��

	    JPanel p=new JPanel();

	    JPanel p1=new JPanel();
	    p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

	    JPanel p2=new JPanel();
	    FlowLayout layout = new FlowLayout();
	    layout.setAlignment(FlowLayout.RIGHT);
	    p2.setLayout(layout);





	    //������у{�^��
	    JButton b1=new JButton("�������");
	    p1.add(b1);

	    //�ΐl��у{�^��
	    JButton b2=new JButton("�ΐl���");
	    p1.add(b2);

	    //�߂�{�^��
	    JButton b3=new JButton("�߂�");
	    p2.add(b3);

	    p.add(p1);
	    p.add(p2);

	    Container contentPane = getContentPane();
	    contentPane.add(p, BorderLayout.CENTER);
	    getContentPane().add(p2, BorderLayout.PAGE_END);



	}

}
