import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RoomRecord4 extends JFrame{

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
			RoomRecord4 frame = new RoomRecord4("総合戦績");
		    frame.setVisible(true);

	}

	RoomRecord4(String title){
		setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	    JPanel p=new JPanel();
	    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	    JPanel p1=new JPanel();
	    JPanel p2=new JPanel();
	    JPanel p3=new JPanel();
	    FlowLayout layout = new FlowLayout();
	    layout.setAlignment(FlowLayout.RIGHT);
	    p3.setLayout(layout);



	    JLabel lb1=new JLabel("勝ち数：");
	    p1.add(lb1);


	    JLabel lb2=new JLabel("投了数：");
	    p1.add(lb2);

	    JLabel lb3=new JLabel("負け数：");
	    p2.add(lb3);

	    JLabel lb4=new JLabel("切断数：：");
	    p2.add(lb4);



	    JButton b1=new JButton("OK");
	    p3.add(b1);



	    p.add(p1);
	    p.add(p2);


	    Container contentPane = getContentPane();
	    contentPane.add(p, BorderLayout.CENTER);
	    getContentPane().add(p3, BorderLayout.PAGE_END);



	}

}
