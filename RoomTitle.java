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



public class RoomTitle extends JFrame{		//タイトル画面

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		RoomTitle frame = new RoomTitle("タイトル");
	    frame.setVisible(true);

	}

	RoomTitle(String title){
	    setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    setLayout(new FlowLayout());
	    //パネル用意
	    JPanel panel = new JPanel();
	    JPanel panel1 = new JPanel();
	    JPanel panel2 = new JPanel();



	    panel1.setPreferredSize(new Dimension(200, 100));
	    panel2.setPreferredSize(new Dimension(200, 100));
	    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

	    //ラベル
	    JLabel label = new JLabel("オセロ");
	    label.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 36));
	    panel2.add(label);

	    //ボタン１
	    JButton button1=new JButton("ログイン");
	    panel1.add(button1);
	    //ボタン２
	    JButton button2=new JButton("新規登録");
	    panel1.add(button2);

	    panel.add(panel2);
	    panel.add(panel1);




	    Container contentPane = getContentPane();
	    contentPane.add(panel, BorderLayout.CENTER);
	}





}
