import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RoomRecord1 extends JFrame{

	public static void main(String[] args) {
		RoomRecord1 frame = new RoomRecord1("記録選択");
	    frame.setVisible(true);

	}

	RoomRecord1(String title){
		setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //パネルの用意

	    JPanel p=new JPanel();

	    JPanel p1=new JPanel();
	    p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

	    JPanel p2=new JPanel();
	    FlowLayout layout = new FlowLayout();
	    layout.setAlignment(FlowLayout.RIGHT);
	    p2.setLayout(layout);





	    //総合戦績ボタン
	    JButton b1=new JButton("総合戦績");
	    p1.add(b1);

	    //対人戦績ボタン
	    JButton b2=new JButton("対人戦績");
	    p1.add(b2);

	    //戻るボタン
	    JButton b3=new JButton("戻る");
	    p2.add(b3);

	    p.add(p1);
	    p.add(p2);

	    Container contentPane = getContentPane();
	    contentPane.add(p, BorderLayout.CENTER);
	    getContentPane().add(p2, BorderLayout.PAGE_END);



	}

}
