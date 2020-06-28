// �p�b�P�[�W�̃C���|�[�g
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;


// �N���X��`
public class Client extends JFrame implements MouseListener {
	// ���ʂ��ėp����t�B�[���h //////////////////////////////////////////////////////////////////////////////////////////////////
	//�֘A����N���X
	private Player player;							//�v���C��
	private Othello othello;						//�I�Z��
	//�\���֘A
	final private int WIDTH = 630, HEIGHT = 600;	//�E�B���h�E�̑傫��
	final private int FIELD_W = 200, FIELD_H = 30;	//�e�L�X�g�t�B�[���h�̑傫��
	final private int TABLE_W = 370, TABLE_H = 370;	//�Ֆʂ̑傫��
	final private String RULE = "";					//�V�ѕ�
	final private String TITLE[] = {				//�^�C�g���ꗗ
		"�悤����","���O�C��","�V�K�o�^","���j���[",
		"�΋�","�΋�","�΋�","�΋�","�΋�","�΋�","�΋�","�΋�",
		"�ϐ�","�ϐ�",
		"�L�^","�L�^","�L�^","�L�^",
		"�V�ѕ�"
	};
	private Container pane;							//�R���e�i
	private JPanel[] panel = new JPanel[19];		//��ʃp�l��
	private JPanel listPanel9,listPanel12;			//���X�g�\���p�p�l��
	private JLabel label1_3, label2_3, label10_2, label15, label17;//���x��
	private JTextField field1, field2, field16;		//�e�L�X�g�t�B�[���h
	private JPasswordField passfield1, passfield2, passfield10;//�p�X���[�h�t�B�[���h
	private JRadioButton rb71,rb72;					//���W�I�{�^��
	private JComboBox<String> box7;					//�R���{�{�b�N�X
	private JPanel tablePanel,playerPanel,reactPanel,logPanel,chatPanel;//�Ֆʔz�u�p�p�l���A�΋ǎґ���p�l���A�ϐ�ґ���p�l���A���O�G���A�\���p�l���A�`���b�g�\���p�l��
	private JTextArea logArea;						//���O�G���A
	private JTextField chatField;					//�`���b�g�t�B�[���h
	private JLabel label11_1,label11_2,label11_3,label11_4,label11_5,label11_6;//�΋Ǐ��\�����x��
	ImageIcon whiteIcon, blackIcon, boardIcon, canPutIcon;		//�A�C�R��
	//�����֘A
	private String operation;												//�I�y���[�V����
	private int panelID;													//���ID
	private HashMap<String,String> dataID = new HashMap<String,String>(20);	//�R�}���h-�f�[�^�Ή��\
	{dataID.put("Register","0");
	dataID.put("Login","1");
	dataID.put("RandomMatch","2");
	dataID.put("Table","21");
	dataID.put("Pass","22");
	dataID.put("Giveup","23");
	dataID.put("Finish","24");
	dataID.put("Chat","25");
	dataID.put("TotalRecord","3");
	dataID.put("IdRecord","4");
	dataID.put("MakeKeyroom","5");
	dataID.put("DeleteKeyroom","6");
	dataID.put("KeyroomList","7");
	dataID.put("EnterKeyroom","8");
	dataID.put("WatchroomList","9");
	dataID.put("EnterWatchroom","91");
	dataID.put("Reaction","92");
	dataID.put("GetoutWatchroom","93");
	dataID.put("Logout","100");}
	// �ʐM�p�X�g���[��
	private DataOutputStream out;					//���M�p
	private DataInputStream in;						//��M�p
	private Receiver receiver;						//�X���b�h
	// �R���X�g���N�^/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Client() {
		player = new Player();					//�v���C������
		othello = new Othello();				//�I�Z������
		//�t���[���ݒ�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		//�y�C���l��
		pane = getContentPane();
		pane.setLayout(null);
		//�A�C�R���ݒ�
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");
		canPutIcon = new ImageIcon("canPut.jpg");
		/* �p�l���f�U�C�� *****************************************************************/
		panel = new JPanel[19];
		//�^�C�g�����
		JButton b01 = new JButton("���O�C��");
		b01.setSize(100,50);
		b01.setActionCommand("Switch,1");
		b01.addMouseListener(this);
		JButton b02 = new JButton("�V�K�o�^");
		b02.setSize(100,50);
		b02.setActionCommand("Switch,2");
		b02.addMouseListener(this);
		panel[0] = new JPanel();
		panel[0].setSize(WIDTH,HEIGHT);
		panel[0].add(b01);
		panel[0].add(b02);
		//���O�C�����
		JLabel label1_1 = new JLabel("�v���C����");
		field1 = new JTextField(20);
		JLabel label1_2 = new JLabel("�p�X���[�h");
		passfield1 = new JPasswordField(20);
		label1_3 = new JLabel(" ");
		label1_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		JButton b11 = new JButton("OK");
		b11.setActionCommand("Login,-1");
		b11.addMouseListener(this);
		JButton b12 = new JButton("�߂�");
		b12.setActionCommand("Switch,0");
		b12.addMouseListener(this);
		JPanel panel1_1 = new JPanel();
		panel1_1.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel1_1.add(label1_1);
		panel1_1.add(field1);
		JPanel panel1_2 = new JPanel();
		panel1_2.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel1_2.add(label1_2);
		panel1_2.add(passfield1);
		JPanel panel1_3 = new JPanel();
		panel1_3.setMaximumSize(new Dimension(WIDTH,FIELD_H));
		panel1_3.add(b11);
		panel1_3.add(b12);
		panel[1] = new JPanel();
		panel[1].setSize(WIDTH,HEIGHT);
		panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.Y_AXIS));
		panel[1].add(panel1_1);
		panel[1].add(panel1_2);
		panel[1].add(label1_3);
		panel[1].add(panel1_3);
		//�V�K�o�^���
		JLabel label2_1 = new JLabel("�v���C����");
		field2 = new JTextField(20);
		JLabel label2_2 = new JLabel("�p�X���[�h");
		passfield2 = new JPasswordField(20);
		label2_3 = new JLabel(" ");
		label2_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		JButton b21 = new JButton("OK");
		b21.setActionCommand("Register,-1");
		b21.addMouseListener(this);
		JButton b22 = new JButton("�߂�");
		b22.setActionCommand("Switch,0");
		b22.addMouseListener(this);
		JPanel panel2_1 = new JPanel();
		panel2_1.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel2_1.add(label2_1);
		panel2_1.add(field2);
		JPanel panel2_2 = new JPanel();
		panel2_2.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel2_2.add(label2_2);
		panel2_2.add(passfield2);
		JPanel panel2_3 = new JPanel();
		panel2_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel2_3.add(b21);
		panel2_3.add(b22);
		panel[2] = new JPanel();
		panel[2].setSize(WIDTH,HEIGHT);
		panel[2].setLayout(new BoxLayout(panel[2], BoxLayout.Y_AXIS));
		panel[2].add(panel2_1);
		panel[2].add(panel2_2);
		panel[2].add(label2_3);
		panel[2].add(panel2_3);
		//���j���[���
		JButton b31 = new JButton("�΋ǂ���");
		b31.setMaximumSize(new Dimension(150, 50));
		b31.setActionCommand("Switch,4");
		b31.addMouseListener(this);
		JButton b32 = new JButton("�ϐ킷��");
		b32.setMaximumSize(new Dimension(150, 50));
		b32.setActionCommand("WatchroomList,-1");
		b32.addMouseListener(this);
		JButton b33 = new JButton("�L�^������");
		b33.setMaximumSize(new Dimension(150, 50));
		b33.setActionCommand("Switch,14");
		b33.addMouseListener(this);
		JButton b34 = new JButton("�V�ѕ�");
		b34.setMaximumSize(new Dimension(150, 50));
		b34.setActionCommand("Switch,18");
		b34.addMouseListener(this);
		JButton b35 = new JButton("���O�A�E�g");
		b35.setMaximumSize(new Dimension(150, 50));
		b35.setActionCommand("Logout,-1");
		b35.addMouseListener(this);
		panel[3] = new JPanel();
		panel[3].setSize(WIDTH,HEIGHT);
		panel[3].setLayout(new BoxLayout(panel[3], BoxLayout.Y_AXIS));
		panel[3].add(b31);
		panel[3].add(b32);
		panel[3].add(b33);
		panel[3].add(b34);
		panel[3].add(b35);
		//�����_���E�v���C�x�[�g�I�����
		JButton b41 = new JButton("�����_���}�b�`");
		b41.setMaximumSize(new Dimension(150, 50));
		b41.setActionCommand("RandomMatch,-1");
		b41.addMouseListener(this);
		JButton b42 = new JButton("�v���C�x�[�g�}�b�`");
		b42.setMaximumSize(new Dimension(150, 50));
		b42.setActionCommand("Switch,6");
		b42.addMouseListener(this);
		JButton b43 = new JButton("�߂�");
		b43.setMaximumSize(new Dimension(150, 50));
		b43.setActionCommand("Switch,3");
		b43.addMouseListener(this);
		panel[4] = new JPanel();
		panel[4].setSize(WIDTH,HEIGHT);
		panel[4].setLayout(new BoxLayout(panel[4], BoxLayout.Y_AXIS));
		panel[4].add(b41);
		panel[4].add(b42);
		panel[4].add(b43);
		//�}�b�`���O�ҋ@���
		JLabel label5 = new JLabel("�ΐ푊���T���Ă��܂��B���΂炭���҂����������B");
		JButton b51 = new JButton("�߂�");
		b51.setSize(100,50);
		b51.setActionCommand("CancelMatch,-1");
		b51.addMouseListener(this);
		panel[5] = new JPanel();
		panel[5].setSize(WIDTH,HEIGHT);
		panel[5].add(label5);
		panel[5].add(b51);
		//�����̍쐬�E�����I�����
		JButton b61 = new JButton("���������");
		b61.setMaximumSize(new Dimension(150, 50));
		b61.setActionCommand("Switch,7");
		b61.addMouseListener(this);
		JButton b62 = new JButton("�����ɓ���");
		b62.setMaximumSize(new Dimension(150, 50));
		b62.setActionCommand("KeyroomList,-1");
		b62.addMouseListener(this);
		JButton b63 = new JButton("�߂�");
		b63.setMaximumSize(new Dimension(150, 50));
		b63.setActionCommand("Switch,4");
		b63.addMouseListener(this);
		panel[6] = new JPanel();
		panel[6].setSize(WIDTH,HEIGHT);
		panel[6].setLayout(new BoxLayout(panel[6], BoxLayout.Y_AXIS));
		panel[6].add(b61);
		panel[6].add(b62);
		panel[6].add(b63);
		//�����̐ݒ���
		JLabel label7_1 = new JLabel("�����̐ݒ�����߂Ă�������");
		JLabel label7_2 = new JLabel("�`���b�g");
		rb71 = new JRadioButton("ON",true);
		rb72 = new JRadioButton("OFF",false);
		ButtonGroup group = new ButtonGroup();
		group.add(rb71);
		group.add(rb72);
		JLabel label7_3 = new JLabel("��������");
		String[] time = {"1","3","5"};
		box7 = new JComboBox<String>(time);
		JLabel label7_4 = new JLabel("��");
		JButton b71 = new JButton("����");
		b71.setSize(100,50);
		b71.setActionCommand("MakeKeyroom,-1");
		b71.addMouseListener(this);
		JButton b72 = new JButton("�߂�");
		b72.setSize(100,50);
		b72.setActionCommand("Switch,6");
		b72.addMouseListener(this);
		panel[7] = new JPanel();
		panel[7].setSize(WIDTH,HEIGHT);
		panel[7].setLayout(new BoxLayout(panel[7], BoxLayout.Y_AXIS));
		JPanel panel7_1 = new JPanel();
		panel7_1.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel7_1.setAlignmentX(1.0f);
		panel7_1.add(label7_1);
		JPanel panel7_2 = new JPanel();
		panel7_2.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel7_2.setAlignmentX(1.0f);
		panel7_2.add(label7_2);
		panel7_2.add(rb71);
		panel7_2.add(rb72);
		JPanel panel7_3 = new JPanel();
		panel7_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel7_3.setAlignmentX(1.0f);
		panel7_3.add(label7_3);
		panel7_3.add(box7);
		panel7_3.add(label7_4);
		JPanel panel7_4 = new JPanel();
		panel7_4.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel7_4.setAlignmentX(0.0f);
		panel7_4.add(b71);
		panel7_4.add(b72);
		panel[7].add(panel7_1);
		panel[7].add(panel7_2);
		panel[7].add(panel7_3);
		panel[7].add(panel7_4);
		//����̓����ҋ@���
		JLabel label8 = new JLabel("�ΐ푊��̓�����҂��Ă��܂��B���΂炭���҂����������B");
		JButton b81 = new JButton("�߂�");
		b81.setSize(100,50);
		b81.setActionCommand("DeleteKeyroom,-1");
		b81.addMouseListener(this);
		panel[8] = new JPanel();
		panel[8].setSize(WIDTH,HEIGHT);
		panel[8].add(label8);
		panel[8].add(b81);
		//�������I�����
		listPanel9 = new JPanel();
		listPanel9.setMinimumSize(new Dimension(WIDTH-200,HEIGHT-200));
		listPanel9.setLayout(new BoxLayout(listPanel9,BoxLayout.Y_AXIS));
		JButton b91 = new JButton("�߂�");
		b91.setActionCommand("Switch,6");
		b91.addMouseListener(this);
		panel[9] = new JPanel();
		panel[9].setSize(WIDTH,HEIGHT);
		panel[9].setLayout(new BoxLayout(panel[9], BoxLayout.Y_AXIS));
		JScrollPane sp9 = new JScrollPane(listPanel9,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp9.setMaximumSize(new Dimension(WIDTH-200,HEIGHT-200));
		panel[9].add(sp9);
		JPanel panel9 = new JPanel();
		panel9.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel9.add(b91);
		panel[9].add(panel9);
		//�p�X���[�h���͉��
		JLabel label10_1 = new JLabel("�p�X���[�h");
		passfield10 = new JPasswordField(20);
		JButton b101 = new JButton("OK");
		b101.setSize(100,50);
		b101.setActionCommand("EnterKeyroom,-1");
		b101.addMouseListener(this);
		JButton b102 = new JButton("�߂�");
		b102.setSize(100,50);
		b102.setActionCommand("Switch,9");
		b102.addMouseListener(this);
		label10_2 = new JLabel("");
		panel[10] = new JPanel();
		panel[10].setSize(WIDTH,HEIGHT);
		panel[10].setLayout(new BoxLayout(panel[10], BoxLayout.Y_AXIS));
		JPanel panel10_1 = new JPanel();
		panel10_1.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel10_1.add(label10_1);
		panel10_1.add(passfield10);
		JPanel panel10_2 = new JPanel();
		panel10_2.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel10_2.add(label10_2);
		JPanel panel10_3 = new JPanel();
		panel10_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel10_3.add(b101);
		panel10_3.add(b102);
		panel[10].add(panel10_1);
		panel[10].add(panel10_2);
		panel[10].add(panel10_3);
		//�΋ǉ��
		tablePanel = new JPanel();//�Ֆʕ\���p�l��
		tablePanel.setBounds(10,10,TABLE_W,TABLE_H);
		tablePanel.setLayout(null);
		JPanel infoPanel = new JPanel();//�΋Ǐ��\���p�l��
		infoPanel.setBounds(TABLE_W+20,10,200,160);
		label11_1 = new JLabel("          ");
		label11_2 = new JLabel("���F�Q");
		label11_3 = new JLabel("vs");
		label11_4 = new JLabel("���F�Q");
		label11_5 = new JLabel("          ");
		label11_6 = new JLabel("�������ԁF");
		JPanel infoPanel1 = new JPanel();
		infoPanel1.add(label11_1);
		infoPanel1.add(label11_2);
		JPanel infoPanel2 = new JPanel();
		infoPanel2.add(label11_3);
		JPanel infoPanel3 = new JPanel();
		infoPanel3.add(label11_4);
		infoPanel3.add(label11_5);
		JPanel infoPanel4 = new JPanel();
		infoPanel4.add(label11_6);
		infoPanel.setLayout(new GridLayout(4,1));
		infoPanel.add(infoPanel1);
		infoPanel.add(infoPanel2);
		infoPanel.add(infoPanel3);
		infoPanel.add(infoPanel4);
		playerPanel = new JPanel();//�΋ǎґ���p�l��
		playerPanel.setBounds(TABLE_W+20,170,200,100);
		JButton b11_1 = new JButton("�p�X");
		b11_1.setActionCommand("Pass,-1");
		b11_1.addMouseListener(this);
		JButton b11_2 = new JButton("����");
		b11_2.setActionCommand("Giveup,-1");
		b11_2.addMouseListener(this);
		JButton b11_3 = new JButton("�A�V�X�g");
		b11_3.setActionCommand("Assist,-1");
		b11_3.addMouseListener(this);
		label11_6 = new JLabel("�FON");
		JPanel playerPanel1 = new JPanel();
		playerPanel1.add(b11_1);
		playerPanel1.add(b11_2);
		JPanel playerPanel2 = new JPanel();
		playerPanel2.add(b11_3);
		playerPanel2.add(label11_6);
		playerPanel.add(playerPanel1);
		playerPanel.add(playerPanel2);
		reactPanel = new JPanel();//�ϐ�ґ���p�l��
		reactPanel.setBounds(TABLE_W+20,280,200,100);
		JButton b11_a = new JButton("������");
		b11_a.setActionCommand("Reaction,���񂪂����˂��܂���");
		b11_a.addMouseListener(this);
		JButton b11_b = new JButton("����");
		b11_b.setActionCommand("Reaction,���񂪔���𑗂�܂���");
		b11_b.addMouseListener(this);
		JButton b11_c = new JButton("�H�H");
		b11_c.setActionCommand("Reaction,�F�H�H");
		b11_c.addMouseListener(this);
		JButton b11_d = new JButton("�I�I");
		b11_d.setActionCommand("Reaction,�F�I�I");
		b11_d.addMouseListener(this);
		JButton b11_e = new JButton("�ޏo");
		b11_e.setActionCommand("GetoutWatchroom,-1");
		b11_e.addMouseListener(this);
		reactPanel.add(b11_a);
		reactPanel.add(b11_b);
		reactPanel.add(b11_c);
		reactPanel.add(b11_d);
		reactPanel.add(b11_e);
		logPanel = new JPanel();//���O�\���p�l��
		logPanel.setBounds(0,TABLE_H+20,TABLE_W,170);
		logArea = new JTextArea(8,34);
		logArea.setEditable(false);
		JScrollPane sp = new JScrollPane(logArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logPanel.setLayout(new FlowLayout());
		logPanel.add(sp);
		chatPanel = new JPanel();//�`���b�g�\���p�l��
		chatPanel.setBounds(TABLE_W+20,TABLE_H+90,200,100);
		chatField = new JTextField(17);
		JButton b11_4 = new JButton("���M");
		b11_4.setAlignmentX(0.0f);
		b11_4.setActionCommand("Chat,-1");
		b11_4.addMouseListener(this);
		chatPanel.setLayout(new FlowLayout());
		chatPanel.add(chatField);
		chatPanel.add(b11_4);
		panel[11] = new JPanel();//�ǉ�
		panel[11].setLayout(null);
		panel[11].setBounds(0,0,TABLE_W+200,TABLE_H+170);
		panel[11].add(tablePanel);
		panel[11].add(infoPanel);
		panel[11].add(playerPanel);
		panel[11].add(reactPanel);
		panel[11].add(logPanel);
		panel[11].add(chatPanel);
		othello.resetGrids();
		//�ϐ핔���I�����
		listPanel12 = new JPanel();
		listPanel12.setMinimumSize(new Dimension(WIDTH-200,HEIGHT-200));
		listPanel12.setLayout(new BoxLayout(listPanel12,BoxLayout.Y_AXIS));
		JButton b121 = new JButton("�߂�");
		b121.setActionCommand("Switch,3");
		b121.addMouseListener(this);
		panel[12] = new JPanel();
		panel[12].setSize(WIDTH,HEIGHT);
		panel[12].setLayout(new BoxLayout(panel[12], BoxLayout.Y_AXIS));
		JScrollPane sp12 = new JScrollPane(listPanel12,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp12.setMaximumSize(new Dimension(WIDTH-200,HEIGHT-200));
		panel[12].add(sp12);
		JPanel panel12 = new JPanel();
		panel12.setMaximumSize(new Dimension(WIDTH,FIELD_H));
		panel12.add(b121);
		panel[12].add(panel12);
		//�L�^�I�����
		JButton b141 = new JButton("�������");
		b141.setSize(100,50);
		b141.setActionCommand("TotalRecord,-1");
		b141.addMouseListener(this);
		JButton b142 = new JButton("�ΐl���");
		b142.setSize(100,50);
		b142.setActionCommand("Switch,16");
		b142.addMouseListener(this);
		JButton b143 = new JButton("�߂�");
		b143.setSize(100,50);
		b143.setActionCommand("Switch,3");
		b143.addMouseListener(this);
		panel[14] = new JPanel();
		panel[14].setSize(WIDTH,HEIGHT);
		panel[14].add(b141);
		panel[14].add(b142);
		panel[14].add(b143);
		//�������щ��
		label15 = new JLabel("");
		label15.setMaximumSize(new Dimension(300, 100));
		JButton b151 = new JButton("OK");
		b151.setSize(100,50);
		b151.setActionCommand("Switch,14");
		b151.addMouseListener(this);
		panel[15] = new JPanel();
		panel[15].setSize(WIDTH,HEIGHT);
		panel[15].add(label15);
		panel[15].add(b151);
		//���薼���͉��
		field16 = new JTextField("opponent");
		field16.setMaximumSize(new Dimension(FIELD_W, FIELD_H));
		JButton b161 = new JButton("OK");
		b161.setSize(100,50);
		b161.setActionCommand("IdRecord,-1");
		b161.addMouseListener(this);
		JButton b162 = new JButton("�߂�");
		b162.setSize(100,50);
		b162.setActionCommand("Switch,14");
		b162.addMouseListener(this);
		panel[16] = new JPanel();
		panel[16].setSize(WIDTH,HEIGHT);
		panel[16].add(field16);
		panel[16].add(b161);
		panel[16].add(b162);
		//����ʐ�щ��
		label17 = new JLabel("");
		label17.setMaximumSize(new Dimension(300, 100));
		JButton b171 = new JButton("OK");
		b171.setSize(100,50);
		b171.setActionCommand("Switch,16");
		b171.addMouseListener(this);
		panel[17] = new JPanel();
		panel[17].setSize(WIDTH,HEIGHT);
		panel[17].add(label17);
		panel[17].add(b171);
		//�V�ѕ��\�����
		JLabel label18 = new JLabel(RULE);
		JButton b181 = new JButton("OK");
		b181.setSize(100,50);
		b181.setActionCommand("Switch,3");
		b181.addMouseListener(this);
		panel[18] = new JPanel();
		panel[18].setSize(WIDTH,HEIGHT);
		panel[18].add(label18);
		panel[18].add(b181);
		/**********************************************************************************/
		//������ʕ`��
		panelID = 0;
		switchDisplay();
		resetRoom();
	}
	// �ʐM //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//�T�[�o�ɐڑ�
	public void connectServer(String ipAddress, int port){
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port);				//�ڑ�
			System.out.println("�T�[�o�Ɛڑ����܂���");
			out = new DataOutputStream(socket.getOutputStream());	//�o�̓X�g���[��
			receiver = new Receiver(socket);						//��M�X���b�h
			receiver.start();
		}
		catch (UnknownHostException e) {
			System.err.println("�z�X�g��IP�A�h���X������ł��܂���: " + e);
			System.exit(-1);
		}
		catch (IOException e) {
			System.err.println("�T�[�o�ڑ����ɃG���[���������܂���: " + e);
			System.exit(-1);
		}
	}
	//�f�[�^�̑��M
	public void sendMessage(String msg){
		try{
			out.writeUTF(msg);					//�o�b�t�@�ɏ�������
			System.out.println("���M�F" +msg);	//�e�X�g�o��
		}
		catch (IOException e){
			System.err.println("�f�[�^���M���ɃG���[���������܂���: " + e);
		}
	}
	//�f�[�^�̎�M
	class Receiver extends Thread {
		Receiver (Socket socket){
			try{
				in = new DataInputStream(socket.getInputStream()); //���̓X�g���[��
			}
			catch (IOException e) {
				System.err.println("�T�[�o�ڑ����ɃG���[���������܂���: " + e);
				System.exit(-1);
			}
		}
		public void run(){
			try{
				while(true) {
					try{
						String inputLine = in.readUTF();			//�o�b�t�@��ǂݍ���
						if (inputLine != null){						//�f�[�^��M
							System.out.println("��M�F" +inputLine);//�e�X�g�o��
							receiveMessage(inputLine);				//����
						}
					}
					catch (EOFException e){/*�f�[�^��ǂݍ��܂Ȃ������ꍇ*/}
				}
			}
			catch (IOException e){
				System.err.println("�f�[�^��M���ɃG���[���������܂���: " + e);
			}
		}
	}
	//��M�f�[�^�̔��ʁE����
	public void receiveMessage(String msg){
		String[] c = operation.split(",",2);	//�I�y���[�V�������R�}���h�ƕt�����ɕ���
		String command = c[0];					//�R�}���h
		String subc = c[1];						//�t�����
		//���O�C������
		if(command.equals("Login")){
			if(msg.equals("true")){					//���O�C������
				showDialog("���O�C�����܂���");		//�_�C�A���O�̕\��
				panelID = 3;						//���j���[��ʂ�
				switchDisplay();					//��ʑJ��
			}
			else{																				//���O�C�����s
				if(msg.equals("name")) label1_3.setText("���̂悤�ȃv���C�����͑��݂��܂���");	//���b�Z�[�W�\��
				if(msg.equals("pass")) label1_3.setText("�p�X���[�h���Ⴂ�܂�");				//���b�Z�[�W�\��
				player.setName(null);															//�v���C�������Z�b�g
				player.setPass(null);															//�p�X���[�h���Z�b�g
			}
		}
		//�o�^����
		else if(command.equals("Register")){
			if(msg.equals("true")){					//�o�^����
				showDialog("�o�^���܂���");			//�_�C�A���O�̕\��
				operation = "Login,-1";				//���O�C���I�y���[�V�������s
				sendMessage(dataID.get("Login")+","+player.getName()+","+player.getPass());
			}
			else{														//�o�^���s
				label2_3.setText("���̃v���C�����͊��Ɏg���Ă��܂�");	//���b�Z�[�W�\��
				player.setName(null);									//�v���C�������Z�b�g
				player.setPass(null);									//�p�X���[�h���Z�b�g
			}
		}
		//�����_���}�b�`���O����
		else if(command.equals("RandomMatch")){
			String[] info = msg.split(",",3);
			if(info[0].equals("true")){						//�}�b�`���O����
				if(info[2].equals("0")){
					player.setColor("black");				//��ԕۑ�
					othello.setBlackName(player.getName());	//��薼�ۑ�
					label11_1.setText(player.getName());	//��薼�\��
					othello.setWhiteName(info[1]);			//��薼�ۑ�
					label11_5.setText(info[1]);				//��薼�\��
				}
				if(info[2].equals("1")){
					player.setColor("white");				//��ԕۑ�
					othello.setBlackName(info[1]);			//��薼�ۑ�
					label11_1.setText(info[1]);				//��薼�\��
					othello.setWhiteName(player.getName());	//��薼�ۑ�
					label11_5.setText(player.getName());	//��薼�\��
					playerPanel.setVisible(false);			//���얳����
				}
				reactPanel.setVisible(false);				//���A�N�V�����p�l��������
/**/			player.setChat(true);						//�`���b�g�̗L���ۑ�
/**/			player.setTime(-1);							//�������ԕۑ�
				updateTable();								//�Ֆʕ\��
				panelID = 11;								//�΋ǉ�ʂ�
			}
			if(info[0].equals("false")){					//�}�b�`���O���s
				showDialog("�}�b�`���O�Ɏ��s���܂���");		//�_�C�A���O�̕\��
				panelID = 4;								//�����_���E�v���C�x�[�g�I����ʂ�
			}
			switchDisplay();								//��ʑJ��
		}
		//�������}�b�`���O����
		else if(command.equals("MakeKeyroom")){
			String[] info = msg.split(",",2);
			othello.setRoomID(Integer.parseInt(info[0]));	//�����ԍ��ۑ�
			player.setColor("black");						//��ԕۑ�
			othello.setBlackName(player.getName());			//��薼�ۑ�
			label11_1.setText(player.getName());			//��薼�\��
			othello.setWhiteName(info[1]);					//��薼�ۑ�
			label11_5.setText(info[1]);						//��薼�\��
			updateTable();									//�Ֆʕ\��
			reactPanel.setVisible(false);					//���A�N�V�����p�l��������
			if(!player.getChat()) chatPanel.setVisible(false);//�`���b�g���Ȃ�`���b�g�p�l��������
			panelID = 11;									//�΋ǉ�ʂ�
			switchDisplay();								//��ʑJ��
		}
		//���������X�g
		else if(command.equals("KeyroomList")){
			String[] keyroom = msg.split(",",0);
			String[] info;										//�������
			String strchat;										//�`���b�g�̗L��
			for(int i=0;i<keyroom.length;i++){
				info = keyroom[i].split(Pattern.quote("."),4);
				if(info[2].equals("true")) strchat = "����";
				else strchat = "�Ȃ�";
				JButton bi = new JButton("<html>�쐬�ҁF"+info[1]+"<br/>�`���b�g�F"+strchat+" �������ԁF"+info[3]+"��</html>");
				bi.setActionCommand("SelectKeyroom,"+keyroom[i]);
				bi.addMouseListener(this);
				listPanel9.add(bi);
			}
			panelID = 9;										//���������X�g�\����ʂ�
			switchDisplay();									//��ʑJ��
		}
		//�������ւ̓���
		else if(command.equals("EnterKeyroom")){
			String[] info = msg.split(",",3);
			if(info[0].equals("true")){							//��������
				player.setColor("white");						//��ԕۑ�
				othello.setBlackName(info[2]);					//��薼�ۑ�
				label11_1.setText(info[2]);						//��薼�\��
				othello.setWhiteName(player.getName());			//��薼�ۑ�
				label11_5.setText(player.getName());			//��薼�\��
				updateTable();									//�Ֆʕ\��
				reactPanel.setVisible(false);					//���A�N�V�����p�l��������
				if(!player.getChat()) chatPanel.setVisible(false);//�`���b�g���Ȃ�`���b�g�p�l��������
				playerPanel.setVisible(false);					//���얳����
				panelID = 11;									//�΋ǉ�ʂ�
			}
			if(info[0].equals("false")){						//�������s
				othello.setRoomID(-1);							//�����ԍ����Z�b�g
/**/			player.setChat(true);							//�`���b�g�̗L�����Z�b�g
/**/			player.setTime(-1);								//�������ԃ��Z�b�g
				showDialog("�����Ɏ��s���܂���");				//�_�C�A���O�̕\��
				panelID = 3;									//���j���[��ʂ�
			}
			switchDisplay();									//��ʑJ��
		}
		//�ϐ핔�����X�g
		else if(command.equals("WatchroomList")){
			String[] room = msg.split(",",0);
			String[] info;							//�������
			for(int i=0;i<room.length;i++){			//�I�����{�^������
				info = room[i].split(Pattern.quote("."),5);
				JButton bi = new JButton("<html>���F"+info[1]+" ���F"+info[2]+"<br/>���΁F"+info[3]+" ���΁F"+info[4]+"</html>");
				bi.setActionCommand("EnterWatchroom,"+room[i]);
				bi.addMouseListener(this);
				listPanel12.add(bi);
			}
			panelID = 12;							//�ϐ핔�����X�g�\����ʂ�
			switchDisplay();						//��ʑJ��
		}
		//�ϐ핔���ւ̓���
		else if(command.equals("EnterWatchroom")){
			if(msg.equals("false")){							//�������s
				othello.setRoomID(-1);							//�����ԍ����Z�b�g
				othello.setBlackName(null);						//��薼���Z�b�g
				othello.setWhiteName(null);						//��薼���Z�b�g
				showDialog("�����Ɏ��s���܂���");				//�_�C�A���O�̕\��
				panelID = 3;									//���j���[��ʂ�
			}
			else{												//��������
				player.beStand(true);							//�ϐ탂�[�h��
				player.setAssist(false);						//�A�V�X�g������
				playerPanel.setVisible(false);					//���얳����
				chatPanel.setVisible(false);					//�`���b�g�p�l��������
				player.setColor("black");						//��ԕۑ�
				label11_1.setText(othello.getBlackName());		//��薼�\��
				label11_5.setText(othello.getWhiteName());		//��薼�\��
				String[] grids = msg.split(",");
				for(int i=0;i<othello.getRow()*othello.getRow();i++){//�Ֆʕϊ�
					if(grids[i].equals("0")) grids[i] = "board";	//��
					if(grids[i].equals("1")) grids[i] = "black";	//��
					if(grids[i].equals("2")) grids[i] = "white";	//��
				}
				othello.setGrids(grids);						//�Ֆʔ��f
				updateTable();									//�ՖʍX�V
				sendMessage(dataID.get("Reaction")+","+player.getName()+"���������܂���");
				panelID = 11;									//�ϐ��ʂ�
			}
			switchDisplay();						//��ʑJ��
		}
		//�����L�^
		else if(command.equals("TotalRecord")){
			label15.setText(msg);					//�L�^������
			panelID = 15;							//������щ�ʂ�
			switchDisplay();						//��ʑJ��
		}
		//�ΐl�ʋL�^
		else if(command.equals("IdRecord")){
			label17.setText(msg);					//�L�^������
			panelID = 17;							//����ʐ�щ�ʂ�
			switchDisplay();						//��ʑJ��
		}
		//�΋�(�ϐ�)���
		else if(panelID == 11){
			String[] info = msg.split(",",0);		//�ȉ��A��M���e�ŏꍇ����
			//�ՖʁE���O��M
			if(info[0].equals(dataID.get("Table"))){
				String table = info[1];																//�Ֆʎ擾
				String log = info[2];																//���O�擾
				String[] grids = table.split(Pattern.quote("."),othello.getRow()*othello.getRow()); //�Ֆʕϊ�
				for(int i=0;i<othello.getRow()*othello.getRow();i++){
					if(grids[i].equals("0")) grids[i] = "board";	//��
					if(grids[i].equals("1")) grids[i] = "black";	//��
					if(grids[i].equals("2")) grids[i] = "white";	//��
				}
				othello.setGrids(grids);															//�ՖʍX�V
				updateTable();																		//�Ֆʔ��f
				logArea.setEditable(true);															//���O���f
				logArea.append("\n"+log);
				logArea.setEditable(false);
				playerPanel.setVisible(false);														//����L����
				othello.changeTurn();																//�^�[���ύX
				if(othello.isGameover()) {															//������������
					String result = othello.checkWinner();											//���s�m�F
					if(!player.isStand()){															//�΋ǎ҂̏ꍇ
						if(result.equals("draw")) {						//��������
							sendMessage(dataID.get("Finish")+",3");		//�T�[�o�ɑ��M
							showDialog("��������");						//�_�C�A���O�\��
						}
						if(result.equals(player.getColor())) {			//����
							sendMessage(dataID.get("Finish")+",1");		//�T�[�o�ɑ��M
							showDialog("���Ȃ��̏����ł�");				//�_�C�A���O�\��
						}
						else{											//����
							sendMessage(dataID.get("Finish")+",2");		//�T�[�o�ɑ��M
							showDialog("���Ȃ��̕����ł�");				//�_�C�A���O�\��
						}
					}
					else{																			//�ϐ�҂̏ꍇ
						sendMessage(dataID.get(command));						//�T�[�o�֑��M
						if(result.equals("draw")) showDialog("��������");
						if(result.equals("black")) showDialog("���̏����ł�");
						else showDialog("���̏����ł�");
					}
					panelID = 3;																	//���j���[��ʂ�
					switchDisplay();																//��ʑJ��
					resetRoom();																	//������񃊃Z�b�g
				}
			}
			//�p�X��M
			else if(info[0].equals(dataID.get("Pass"))){
				playerPanel.setVisible(true);									//����L����
				logArea.setEditable(true);										//���O�ɕ\��
				if(player.getColor().equals("black")) logArea.append("\n��肪�p�X���܂���");
				else logArea.append("\n��肪�p�X���܂���");
				logArea.setEditable(false);
				othello.changeTurn();											//�^�[���ύX
			}
			//������M
			else if(info[0].equals(dataID.get("Giveup"))){
				if(!player.isStand()) showDialog("�ΐ푊�肪�������܂���");		//�_�C�A���O�\��
				else showDialog("�ΐ�҂��������܂���");						//�_�C�A���O�\��
				panelID = 3;													//���j���[��ʂ�
				switchDisplay();												//��ʑJ��
				resetRoom();													//������񃊃Z�b�g
			}
			//�`���b�g��M
			else if(info[0].equals(dataID.get("Chat"))){
				String chat = info[1];											//�`���b�g�ۑ�
				logArea.setEditable(true);										//�`���b�g���f
				logArea.append("\n"+chat);
				logArea.setEditable(false);
			}
			//���A�N�V������M
			else if(info[0].equals(dataID.get("Reaction"))){
				String reaction = info[1];										//���A�N�V�����ۑ�
				logArea.setEditable(true);										//���A�N�V�������f���f
				logArea.append("\n> "+reaction);
				logArea.setEditable(false);
			}
		}
	}
	// ���� //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//�}�E�X���X�i�[
	public void mouseClicked(MouseEvent e) {
		JButton button = (JButton)e.getComponent();			//�{�^������
		operation = button.getActionCommand();				//�I�y���[�V�����m�F
		System.out.println("���s�F" +operation);			//�e�X�g�o��
		acceptOperation(operation);							//����
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	//����̎�t�E����
	public void acceptOperation(String operation){
		String[] c = operation.split(",",2);	//�I�y���[�V�������R�}���h�ƕt�����ɕ���
		String command = c[0];					//�R�}���h
		String subc = c[1];						//�t�����
		switch(command){						//�ȉ��A�R�}���h�ŏꍇ����
			//��ʑJ�ڂ̂�
			case "Switch":
				panelID = Integer.parseInt(subc);
				switchDisplay();
				break;
			//���O�C���v��
			case "Login":
				String logname = field1.getText();							//�v���C�����ǂݎ��
				String logpass = new String(passfield1.getPassword());		//�p�X���[�h�ǂݎ��
				if(logname.equals("")) {
					label1_3.setText("�v���C�������󗓂ł�");
					break;
				}
				else if(logpass.equals("")) {
					label1_3.setText("�p�X���[�h���󗓂ł�");
					break;
				}
				player.setName(logname);									//�v���C�����ۑ�
				player.setPass(logpass);									//�p�X���[�h�ۑ�
				sendMessage(dataID.get(command)+","+logname+","+logpass);	//�T�[�o�֑��M
				break;
			//�V�K�o�^�v��
			case "Register":
				String regname = field2.getText();							//�v���C�����ǂݎ��
				String regpass = new String(passfield2.getPassword());		//�p�X���[�h�ǂݎ��
				if(regname.equals("")) {
					label2_3.setText("�v���C�������󗓂ł�");
					break;
				}
				else if(regpass.equals("")) {
					label2_3.setText("�p�X���[�h���󗓂ł�");
					break;
				}
				player.setName(regname);									//�v���C�����ۑ�
				player.setPass(regpass);									//�p�X���[�h�ۑ�
				sendMessage(dataID.get(command)+","+regname+","+regpass);	//�T�[�o�֑��M
				break;
			//�����_���}�b�`���O�v��
			case "RandomMatch":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				panelID = 5;												//�}�b�`���O�ҋ@��ʂ�
				switchDisplay();
				break;
			//�}�b�`���O�L�����Z������
			case "CancelMatch":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				panelID = 4;												//�����_���E�v���C�x�[�g�I����ʂ�
				switchDisplay();
				break;
			//�΂�u��
			case "Table":
				if(player.getColor()==othello.getTurn()){					//�����̔ԂȂ�
					int grid = Integer.parseInt(subc);													//�}�X�ԍ�
					String log = "";																	//���O
					StringBuffer table = new StringBuffer("");											//�Ֆ�
					Boolean canPut = othello.putStone(grid,player.getColor(),true);						//�΂�u��
					if(canPut){																			//�u����}�X�Ȃ�
						updateTable();																	//�Ֆʔ��f
						String[] grids = othello.getGrids();											//�Ֆʏ��擾
						for(int i=0;i<othello.getRow()*othello.getRow();i++){							//�Ֆʏ��ϊ�
							if(grids[i].equals("board")) table.append("0.");
							if(grids[i].equals("black")) table.append("1.");
							if(grids[i].equals("white")) table.append("2.");
						}
						if(player.getColor().equals("black")){											//���O���ϊ�
							log = "���F("+Integer.toString(grid/8+1)+","+Integer.toString(grid%8+1)+")";
						}
						if(player.getColor().equals("white")){
							log = "���F("+Integer.toString(grid/8+1)+","+Integer.toString(grid%8)+")";
						}
						logArea.setEditable(true);														//���O���f
						logArea.append("\n"+log);
						logArea.setEditable(false);
						sendMessage(dataID.get(command)+","+table+","+log);								//�T�[�o�֑��M
						playerPanel.setVisible(false);													//���얳����
						othello.changeTurn();															//�^�[���ύX
						if(othello.isGameover()) {														//������������
							String result = othello.checkWinner();										//���s�m�F
							if(result.equals("draw")) {						//��������
								sendMessage(dataID.get("Finish")+",3");		//�T�[�o�ɑ��M
								showDialog("��������");						//�_�C�A���O�\��
							}
							if(result.equals(player.getColor())) {			//����
								sendMessage(dataID.get("Finish")+",1");		//�T�[�o�ɑ��M
								showDialog("���Ȃ��̏����ł�");				//�_�C�A���O�\��
							}
							else{											//����
								sendMessage(dataID.get("Finish")+",2");		//�T�[�o�ɑ��M
								showDialog("���Ȃ��̕����ł�");				//�_�C�A���O�\��
							}
							panelID = 3;																//���j���[��ʂ�
							switchDisplay();															//��ʑJ��
							resetRoom();																//������񃊃Z�b�g
						}
					}
				}
				break;
			//�p�X
			case "Pass":
				sendMessage(dataID.get(command));						//�T�[�o�֑��M
				playerPanel.setVisible(false);							//���얳����
				logArea.setEditable(true);								//���O�ɕ\��
				if(player.getColor().equals("black")) logArea.append("\n��肪�p�X���܂���");
				else logArea.append("\n��肪�p�X���܂���");
				logArea.setEditable(false);
				othello.changeTurn();
				break;
			//����
			case "Giveup":
				sendMessage(dataID.get(command));						//�T�[�o�֑��M
				showDialog("�������܂���");								//�_�C�A���O�\��
				panelID = 3;											//���j���[��ʂ�
				switchDisplay();										//��ʑJ��
				resetRoom();											//������񃊃Z�b�g
				break;
			//�`���b�g
			case "Chat":
				String str = player.getName()+": "+chatField.getText();	//�`���b�g���e�ǂݎ��
				logArea.setEditable(true);								//�`���b�g���f
				logArea.append("\n"+str);
				logArea.setEditable(false);
				sendMessage(dataID.get(command)+","+str);				//�T�[�o�֑��M
				chatField.setText("");									//�`���b�g�����Z�b�g
				break;
			//�A�V�X�g�ؑ�
			case "Assist":
				player.changeAssist();
				if(player.getAssist()==true) label11_6.setText("�FON");
				if(player.getAssist()==false) label11_6.setText("�FOFF");
				updateTable();
				break;
			//�����L�^�v��
			case "TotalRecord":
				sendMessage(dataID.get(command));						//�T�[�o�֑��M
				break;
			//�ΐl�ʋL�^�v��
			case "IdRecord":
				sendMessage(dataID.get(command)+","+field16.getText());	//�T�[�o�֑��M
				break;
			//�������쐬�v��
			case "MakeKeyroom":
				boolean chat = rb71.isSelected();						//�`���b�g�̗L���ǂݎ��
				int time = Integer.parseInt((String)box7.getSelectedItem());//�������ԓǂݎ��
/**/			player.setChat(chat);									//�`���b�g�̗L���ۑ�
/**/			player.setTime(time);									//�������ԕۑ�
				sendMessage(dataID.get(command)+","+chat+","+time);		//�T�[�o�֑��M
				panelID = 8;											//����̓����ҋ@���
				switchDisplay();
				break;
			//�������폜�v��
			case "DeleteKeyroom":
/**/			player.setChat(true);									//�`���b�g�̗L�����Z�b�g
/**/			player.setTime(-1);										//�������ԃ��Z�b�g
				sendMessage(dataID.get(command));						//�T�[�o�֑��M
				panelID = 4;											//�����_���E�v���C�x�[�g�I����ʂ�
				switchDisplay();										//��ʑJ��
				break;
			//���������X�g�v��
			case "KeyroomList":
				sendMessage(dataID.get(command));						//�T�[�o�֑��M
				break;
			//���������X�g�I��
			case "SelectKeyroom":
				String[] keyinfo = subc.split(Pattern.quote("."),4);
				othello.setRoomID(Integer.parseInt(keyinfo[0]));			//�����ԍ��ۑ�
/**/			player.setChat(Boolean.parseBoolean(keyinfo[2]));			//�`���b�g�̗L���ۑ�
/**/			player.setTime(Integer.parseInt(keyinfo[3]));				//�������ԕۑ�
				panelID = 10;												//�p�X���[�h���͉�ʂ�
				switchDisplay();
				break;
			//�������ւ̓����v��
			case "EnterKeyroom":
				String keypass = new String(passfield10.getPassword());
				if(keypass.equals("")) {
					label10_2.setText("�p�X���[�h���󗓂ł�");
					break;
				}
				sendMessage(dataID.get(command)+","+othello.getRoomID()+","+keypass);//�T�[�o�֑��M
				break;
			//�ϐ핔�����X�g�v��
			case "WatchroomList":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				break;
			//�ϐ핔���ւ̓����v��
			case "EnterWatchroom":
				String[] roominfo = subc.split(Pattern.quote("."),5);
				othello.setRoomID(Integer.parseInt(roominfo[0]));				//�����ԍ��ۑ�
				othello.setBlackName(roominfo[1]);								//��薼�ۑ�
				othello.setWhiteName(roominfo[2]);								//��薼�ۑ�
				sendMessage(dataID.get(command)+","+othello.getRoomID());		//�T�[�o�֑��M
				break;
			//���A�N�V����
			case "Reaction":
				String reaction = player.getName()+subc;						//���A�N�V�����ϊ�
				logArea.setEditable(true);										//���A�N�V�������f
				logArea.append("\n> "+reaction);
				logArea.setEditable(false);
				sendMessage(dataID.get(command)+","+reaction);					//�T�[�o�֑��M
				break;
			//�ϐ�ޏo
			case "GetoutWatchroom":
				sendMessage(dataID.get("Reaction")+","+player.getName()+"���񂪑ޏo���܂���");
				sendMessage(dataID.get(command));						//�T�[�o�֑��M
				panelID = 3;											//���j���[��ʂ�
				switchDisplay();										//��ʑJ��
				resetRoom();											//������񃊃Z�b�g
				break;
			//���O�A�E�g
			case "Logout":
				sendMessage(dataID.get(command));				//�T�[�o�֑��M
				player.setName(null);							//�v���C�������Z�b�g
				player.setPass(null);							//�p�X���[�h���Z�b�g
				panelID = 0;									//�^�C�g����ʂ�
				switchDisplay();
				break;
			default:
				break;
		}
	}
	//�������̃��Z�b�g(�������E������M���E�΋ǏI�����E�ϐ�ޏo���E�ϐ�I����)
	public void resetRoom(){
		othello.setRoomID(-1);				//�����ԍ����Z�b�g
		player.setColor("-1");				//��ԃ��Z�b�g
		othello.setBlackName(null);			//��薼���Z�b�g
		othello.setWhiteName(null);			//��薼���Z�b�g
/**/	player.setChat(true);				//�`���b�g�̗L�����Z�b�g
/**/	player.setTime(-1);					//�������ԃ��Z�b�g
		player.setAssist(true);				//�A�V�X�g�̗L�����Z�b�g
		othello.resetGrids();				//�ՖʁE�^�[�����Z�b�g
		//�ȉ��A�΋ǉ�ʂ̏�����
		logArea.setText("");
		chatField.setText("");
		player.beStand(false);
		playerPanel.setVisible(true);
		reactPanel.setVisible(true);
		logPanel.setVisible(true);
		chatPanel.setVisible(true);
	}
	// �\�� //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//��ʂ̑J�ځE�`��
	public void switchDisplay(){
		pane.removeAll();								//�p�l������
		setTitle(TITLE[panelID]);						//�^�C�g���ύX
		pane.add(panel[panelID]);						//�p�l���ݒu
		System.out.println("���" +panelID+ "�ֈړ�");	//�e�X�g�o��
		pane.revalidate();								//�f�[�^���f
		pane.repaint();									//��ʍX�V
		return;
	}
	//�ՖʍX�V(�΋ǊJ�n���E�ϐ�J�n���E�΂�u�������E�Ֆʎ�M��)
	public void updateTable(){
		tablePanel.removeAll();													//�{�^����S�ď���
		othello.canPutGrids();													//�u����ӏ����X�V
		JButton[] buttonArray = new JButton[othello.getRow()*othello.getRow()];		//�{�^���̔z����쐬
		String[] grids = othello.getGrids();										//�Ֆʎ擾
		for(int i=0;i<othello.getRow()*othello.getRow();i++){
			if(grids[i].equals("board")) buttonArray[i] = new JButton(boardIcon);	//��
			if(grids[i].equals("black")) buttonArray[i] = new JButton(blackIcon);	//��
			if(grids[i].equals("white")) buttonArray[i] = new JButton(whiteIcon);	//��
			if(grids[i].equals("canPut")) {
				if(player.getAssist()==true) buttonArray[i] = new JButton(canPutIcon);	//�u����ӏ�
				if(player.getAssist()==false) buttonArray[i] = new JButton(boardIcon);	//��
			}
			int x = (i%othello.getRow())*45;
			int y = (int) (i/othello.getRow())*45;
			buttonArray[i].setBounds(x, y, 45, 45);
			buttonArray[i].setActionCommand("Table,"+Integer.toString(i));
			if(!player.isStand()) buttonArray[i].addMouseListener(this);			//�ϐ탂�[�h�Ȃ疳��
			tablePanel.add(buttonArray[i]);
		}
		label11_2.setText("���F" + othello.getBlack());			//�΂̐��\��
		label11_4.setText("���F" + othello.getWhite());			//�΂̐��\��
		pane.revalidate();										//�f�[�^���f
		pane.repaint();											//��ʍX�V
	}
	//���b�Z�[�W�_�C�A���O
	class MessageDialog extends JDialog implements ActionListener{
		public MessageDialog(JFrame mainframe,String message){
			super(mainframe,"���b�Z�[�W",ModalityType.APPLICATION_MODAL);//���[�_���ɐݒ�
			this.setSize(300, 150);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			JLabel label = new JLabel(message);
			label.setMaximumSize(new Dimension(300,FIELD_H));
			JButton button = new JButton("OK");
			button.addActionListener(this);
			JPanel subpanel = new JPanel();
			subpanel.setMaximumSize(new Dimension(300,FIELD_H));
			subpanel.add(button);
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(label);
			panel.add(subpanel);
			this.add(panel);
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	//�_�C�A���O�̕\��
	public void showDialog(String str){
		MessageDialog dialog = new MessageDialog(this,str);
		dialog.setVisible(true);
	}
	// ���C�� ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String args[]){

		//String ipAddress = "192.168.1.11";	//IP�A�h���X
		//String ipAddress = "220.215.242.167";	//IP�A�h���X
		int port = 10000;

		Client client = new Client();			//�N���C�A���g����
		client.setVisible(true);				//��ʕ\��
		client.connectServer("localhost", port);	//�T�[�o�ɐڑ�
	}
}