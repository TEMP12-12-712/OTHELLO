import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoomRecord2 extends JFrame implements ActionListener{


	JLabel label1;
	JTextField text;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
			RoomRecord2 frame = new RoomRecord2("記録選択");
		    frame.setVisible(true);

	}

	RoomRecord2(String title){
		setTitle(title);
	    setBounds(100, 100, 300, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	    JPanel p=new JPanel();
	    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	    JPanel p1=new JPanel();
	    JPanel p2=new JPanel();


	    JLabel label=new JLabel("相手プレイヤ名");
	    p1.add(label);

	    label1=new JLabel();

	    text=new JTextField(20);
	    text.setPreferredSize(new Dimension(50, 30));
	    p1.add(text);

	    JButton b1=new JButton("OK");
	    p2.add(b1);
	    b1.addActionListener(this);

	    JButton b2=new JButton("戻る");
	    p2.add(b2);


	    p.add(p1);
	    p.add(p2);


	    Container contentPane = getContentPane();
	    contentPane.add(p, BorderLayout.CENTER);


	}

	  public void actionPerformed(ActionEvent e){
		  label1.setText(text.getText());
	  }

}