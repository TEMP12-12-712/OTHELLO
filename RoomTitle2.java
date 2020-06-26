import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoomTitle2 extends JFrame{

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		RoomTitle2 frame = new RoomTitle2("rログイン");
	    frame.setVisible(true);

	}

	RoomTitle2(String title){
		setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel p=new JPanel();
	    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));


	    JPanel p1=new JPanel();
	    JPanel p3=new JPanel();

	    JLabel lb1=new JLabel("ユーザーネーム：");
	    JLabel lb2=new JLabel("パスワード    ：");

	    //ユーザーネーム
	    JTextField tx1=new JTextField();
	    tx1.setPreferredSize(new Dimension(100, 20));
	    //パスワード
	    JTextField tx2=new JTextField();
	    tx2.setPreferredSize(new Dimension(100, 20));

	    JButton b=new JButton("OK");
	    p3.add(b);



	    p1.add(lb1);
	    p1.add(tx1);
	    p1.add(lb2);
	    p1.add(tx2);

	    p.add(p1);
	    p.add(p2);
	    Container contentPane = getContentPane();
	    contentPane.add(p, BorderLayout.CENTER);
	    getContentPane().add(p3, BorderLayout.PAGE_END);



	}

}


