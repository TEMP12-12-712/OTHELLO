// �p�b�P�[�W�̃C���|�[�g
import javax.swing.*;
import javax.sound.sampled.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;


// �N���X��`
public class Client extends JFrame implements MouseListener, ActionListener{
	// ���ʂ��ėp����t�B�[���h //////////////////////////////////////////////////////////////////////////////////////////////////
	//�N���X�֘A
	private Player player;							//�v���C��
	private Othello othello;						//�I�Z��
	private javax.swing.Timer timer;				//�^�C�}�[
	//�\���֘A
	final private int WIDTH = 630, HEIGHT = 600;	//�t���[���̑傫��
	final private int FIELD_W = 200, FIELD_H = 30;	//�e�L�X�g�t�B�[���h�����܂�c��
	final private int TABLE_W = 370, TABLE_H = 370;	//�Ֆʂ̑傫��
	final private String[] CLM = {"A","B","C","D","E","F","G","H"};//�Ֆʂ̉���
	final private String[] ROW = {"1","2","3","4","5","6","7","8"};//�Ֆʂ̏c��
	final private String RULE = "�V�ѕ�\n\n���I�Z���̃��[��\n�E������U�A������U�ł��B\n�E����̐΂����߂�ꏊ�łȂ��Ƒł��Ƃ��ł��܂���B\n�E����̐΂����ނƑ���̐΂��Ђ�����Ԃ莩���̐΂ɂȂ�܂��B\n�E���ޕ����͏c�A���A�΂߂̂�����ł��\�ł��B\n�E�ǂ��ɂ�������ꏊ���Ȃ��ꍇ�p�X�ɂȂ�܂��B\n�E���݂��ǂ��ɂ��u����ꏊ�������Ȃ������ɐ΂̐��������ق��������ƂȂ�܂��B\n\n�����j���[�ɂ���\n�E�΋ǂ���\n���ʏ�΋ǂƃv���C�x�[�g�΋ǂ�I�ׂ܂��B\n�����ʏ�΋ǂ̓I�����C���Ń����_���Ƀ}�b�`���O��������Ƒ΋ǂ��܂��B\n�����v���C�x�[�g�΋ǂł̓p�X���[�h�t���̑΋Ǖ������쐬�܂��͓������đ΋ǂ��܂��B\n�@�@�΋ǎ��Ԃ�`���b�g�@�\�̗L���ɂ��Đݒ肷�邱�Ƃ��ł��܂��B\n�E�ϐ킷��\n���΋ǒ��̕����̃��X�g����D���ȑ΋ǂ�I�����A���v���C���[�̑΋ǂ��ϐ킷�邱�Ƃ�\n�@�ł��܂��B\n�����΋ǂɑ΂��ă��A�N�V�����𑗐M���邱�Ƃ��ł��܂��B\n�E�L�^������\n������܂ł̐�т̑S�L�^��v���C���[���Ƃ̑΋ǋL�^���{�����邱�Ƃ��ł��܂��B\n\n���΋ǒ��ɂ���\n�E�Ֆʂ��N���b�N���邱�ƂŐ΂�u���܂��B�u���Ȃ��ꏊ���N���b�N���Ă������N���܂���B\n�E�΂�u����ꏊ�ɂ͐Ԃ��\��������܂��B����͐ݒ�ŏ������Ƃ��ł��܂��B\n�E1���ȓ��ɐ΂�u���Ȃ��ƃp�X�ɂȂ�܂��B�v���C�x�[�g�΋ǂł͎��Ԃ�ύX�ł��܂��B\n�E����ƃ`���b�g�����邱�Ƃ��ł��܂��B�{���������Ȃ��ꍇ�̓��O��ʂ���邱�Ƃ�\n�@�ł��܂��B\n�E��������ƕ����ƂȂ�A�΋ǂ��I�����܂��B";//�V�ѕ�
	final private String TITLE[] = {				//�^�C�g���ꗗ
		"�悤����","���O�C��","�V�K�o�^","���j���[",
		"�΋�","�}�b�`���O��","�΋�","�����̐ݒ�","�}�b�`���O��","������I��","�p�X���[�h����","�΋�",
		"������I��","�ϐ�",
		"�L�^��I��","�����L�^","����ʋL�^","����ʋL�^",
		"�V�ѕ�"
	};
	private Container pane;							//�R���e�i
	private ImagePanel[] panel = new ImagePanel[19];//��ʃp�l��
	private JPanel listPanel9,listPanel12;			//���������X�g�\���p�p�l���A�ϐ핔�����X�g�\���p�p�l��
	private JLabel label1, label2, label7_5, label10_2, label16_2;	//���͏��ƍ����ʕ\�����x��
	private JLabel label15_1, label15_2, label15_3, label15_4;//������ѕ\�����x��
	private JLabel label17_1, label17_2, label17_3, label17_4;//�ΐl�ʐ�ѕ\�����x��
	private JTextField field1, field2, passfield7, field16;		//�e�L�X�g���̓t�B�[���h
	private JPasswordField passfield1, passfield2, passfield10;	//�p�X���[�h���̓t�B�[���h
	private JRadioButton rb71,rb72;					//���W�I�{�^��
	private JComboBox<String> box7;					//�R���{�{�b�N�X
	private JPanel tablePanel,playerPanel,reactPanel,logPanel,chatPanel;//�Ֆʔz�u�p�p�l���A�΋ǎґ���p�l���A�ϐ�ґ���p�l���A���O�G���A�\���p�l���A�`���b�g�\���p�l��
	private ImageButton b11_1;							//�p�X�{�^��
	private JTextArea logArea;						//���O�G���A
	private JTextField chatField;					//�`���b�g�t�B�[���h
	private JLabel label11_1,label11_2,label11_4,label11_5,label11_6,label11_7,label11_8;//�΋Ǐ��\�����x��
	//�f��
	final private String SRC_IMG = ".\\img\\";		//�摜�ւ̃p�X
	final private String SRC_SND = ".\\sounds\\";	//���ւ̃p�X
	File gameIcon, standIcon, recordIcon, ruleIcon, logoutIcon;		//���j���[�{�^���p�A�C�R��
	File buttonIcon1, buttonIcon2;									//���̑��{�^���p�A�C�R��
	ImageIcon whiteIcon, blackIcon, boardIcon, canPutIcon;			//�Ֆʃ{�^���p�A�C�R��
	File[] backImage = new File[19];								//�t���[���w�i�摜
	File dialogImage;
	File SE_switch;													//��
	//�����֘A
	private String Operation;												//���s���̃I�y���[�V����
	private int panelID;													//��ʃp�l��ID
	private boolean pathFlag;												//�p�X�t���O
	private int minutes, seconds;											//�΋Ǘp�o�ߎ���
	private HashMap<String,String> dataID = new HashMap<String,String>(20);	//�R�}���h�E�f�[�^�Ή��\
	{dataID.put("Register","0");
	dataID.put("Login","1");
	dataID.put("RandomMatch","2");
	dataID.put("CancelMatch","20");
	dataID.put("Table","21");
	dataID.put("Pass","22");
	dataID.put("Giveup","23");
	dataID.put("Finish","24");
	dataID.put("Chat","25");
	dataID.put("Disconnected","26");
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
	// �ʐM�֘A
	private DataOutputStream out;					//���M�p�X�g���[��
	private DataInputStream in;						//��M�p�X�g���[��
	private Receiver receiver;						//��M�X���b�h
	// �R���X�g���N�^/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Client() {
		//�I�u�W�F�N�g����
		player = new Player();						//�v���C������
		othello = new Othello();					//�I�Z������
		timer = new javax.swing.Timer(1000,this);	//�^�C�}�[����
		//�摜�ǂݍ���
		gameIcon = new File(SRC_IMG+"BUTTON_GAME.jpg");
		standIcon = new File(SRC_IMG+"BUTTON_STAND.jpg");
		recordIcon = new File(SRC_IMG+"BUTTON_RECORD.jpg");
		ruleIcon = new File(SRC_IMG+"BUTTON_RULE.jpg");
		logoutIcon = new File(SRC_IMG+"BUTTON_LOGOUT.jpg");
		buttonIcon1 = new File(SRC_IMG+"BUTTON_1.jpg");
		buttonIcon2 = new File(SRC_IMG+"BUTTON_2.jpg");
		whiteIcon = new ImageIcon(SRC_IMG+"White.jpg");
		blackIcon = new ImageIcon(SRC_IMG+"Black.jpg");
		boardIcon = new ImageIcon(SRC_IMG+"GreenFrame.jpg");
		canPutIcon = new ImageIcon(SRC_IMG+"canPut.jpg");
		backImage[0] = new File(SRC_IMG+"BACK_TITLE.jpg");
		backImage[1] = new File(SRC_IMG+"BACK_LOGIN.jpg");
		backImage[2] = new File(SRC_IMG+"BACK_LOGIN.jpg");
		backImage[3] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[4] = new File(SRC_IMG+"BACK_SELECTMATCH.jpg");
		backImage[5] = new File(SRC_IMG+"BACK_RANDOMWAIT.jpg");
		backImage[6] = new File(SRC_IMG+"BACK_SELECTHOW.jpg");
		backImage[7] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[8] = new File(SRC_IMG+"BACK_KEYROOMWAIT.jpg");
		backImage[9] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[10] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[11] = new File(SRC_IMG+"BACK_GAME.jpg");
		backImage[12] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[13] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[14] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[15] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[16] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[17] = new File(SRC_IMG+"BACK_MENU.jpg");
		backImage[18] = new File(SRC_IMG+"BACK_MENU.jpg");
		dialogImage = new File(SRC_IMG+"BACK_DIALOG.jpg");
		//���ǂݍ���
//		SE_switch = new File(SRC_SND+"keyboard1.wav");
		//�t���[���ݒ�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH+24, HEIGHT+48);
		setResizable(false);
		//�y�C���ݒ�
		pane = getContentPane();
		pane.setLayout(null);
		/* �p�l���f�U�C�� *****************************************************************/
		//�^�C�g�����
		ImageButton b01 = new ImageButton("���O�C��",buttonIcon1,36,false);
		b01.setBounds(WIDTH/2-230, 470, 200, 70);
		b01.setActionCommand("Switch,1");
		b01.addMouseListener(this);
		ImageButton b02 = new ImageButton("�V�K�o�^",buttonIcon1,36,false);
		b02.setBounds(WIDTH/2+30, 470, 200, 70);
		b02.setActionCommand("Switch,2");
		b02.addMouseListener(this);
		panel[0] = new ImagePanel(backImage[0]);
		panel[0].setSize(WIDTH,HEIGHT);
		panel[0].setLayout(null);
		panel[0].add(b01);
		panel[0].add(b02);
		//���O�C�����
		field1 = new JTextField(20);
		field1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		field1.setBounds(260,250,300,40);
		passfield1 = new JPasswordField(20);
		passfield1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		passfield1.setBounds(260,320,300,40);
		label1 = new JLabel(" ");
		label1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,28));
		label1.setForeground(Color.WHITE);
		label1.setBounds(WIDTH/2-300,370,600,28);
		ImageButton b11 = new ImageButton("�߂�",buttonIcon1,24,false);
		b11.setBounds(WIDTH/2-90,0,80,40);
		b11.setActionCommand("Switch,0");
		b11.addMouseListener(this);
		ImageButton b12 = new ImageButton("OK",buttonIcon1,24,false);
		b12.setBounds(WIDTH/2+10,0,80,40);
		b12.setActionCommand("Login,-1");
		b12.addMouseListener(this);
		JPanel panel1 = new JPanel();
		panel1.setBounds(0,430,WIDTH,40);
		panel1.setOpaque(false);
		panel1.setLayout(null);
		panel1.add(b11);
		panel1.add(b12);
		panel[1] = new ImagePanel(backImage[1]);
		panel[1].setBounds(0,0,WIDTH,HEIGHT);
		panel[1].repaint();
		panel[1].setLayout(null);
		panel[1].add(field1);
		panel[1].add(passfield1);
		panel[1].add(label1);
		panel[1].add(panel1);
		//�V�K�o�^���
		field2 = new JTextField(20);
		field2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		field2.setBounds(260,250,300,40);
		passfield2 = new JPasswordField(20);
		passfield2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		passfield2.setBounds(260,320,300,40);
		label2 = new JLabel(" ");
		label2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,28));
		label2.setForeground(Color.WHITE);
		label2.setBounds(WIDTH/2-300,370,600,28);
		ImageButton b21 = new ImageButton("�߂�",buttonIcon1,24,false);
		b21.setBounds(WIDTH/2-90,0,80,40);
		b21.setActionCommand("Switch,0");
		b21.addMouseListener(this);
		ImageButton b22 = new ImageButton("OK",buttonIcon1,24,false);
		b22.setBounds(WIDTH/2+10,0,80,40);
		b22.setActionCommand("Register,-1");
		b22.addMouseListener(this);
		JPanel panel2 = new JPanel();
		panel2.setBounds(0,430,WIDTH,40);
		panel2.setOpaque(false);
		panel2.setLayout(null);
		panel2.add(b21);
		panel2.add(b22);
		panel[2] = new ImagePanel(backImage[2]);
		panel[2].setBounds(0,0,WIDTH,HEIGHT);
		panel[2].repaint();
		panel[2].setLayout(null);
		panel[2].add(field2);
		panel[2].add(passfield2);
		panel[2].add(label2);
		panel[2].add(panel2);
		//���j���[���
		ImageButton b31 = new ImageButton("�΋ǂ���",gameIcon,36,true);
		b31.setBounds(WIDTH/5, HEIGHT/28*2, WIDTH/5*3, HEIGHT/7);
		b31.setActionCommand("Switch,4");
		b31.addMouseListener(this);
		ImageButton b32 = new ImageButton("�ϐ킷��",standIcon,36,true);
		b32.setBounds(WIDTH/5, HEIGHT/28*7, WIDTH/5*3, HEIGHT/7);
		b32.setActionCommand("WatchroomList,-1");
		b32.addMouseListener(this);
		ImageButton b33 = new ImageButton("�L�^������",recordIcon,36,true);
		b33.setBounds(WIDTH/5, HEIGHT/28*12, WIDTH/5*3, HEIGHT/7);
		b33.setActionCommand("Switch,14");
		b33.addMouseListener(this);
		ImageButton b34 = new ImageButton("�V�ѕ�",ruleIcon,36,true);
		b34.setBounds(WIDTH/5, HEIGHT/28*17, WIDTH/5*3, HEIGHT/7);
		b34.setActionCommand("Switch,18");
		b34.addMouseListener(this);
		ImageButton b35 = new ImageButton("���O�A�E�g",logoutIcon,36,true);
		b35.setBounds(WIDTH/5, HEIGHT/28*22, WIDTH/5*3, HEIGHT/7);
		b35.setActionCommand("Logout,-1");
		b35.addMouseListener(this);
		panel[3] = new ImagePanel(backImage[3]);
		panel[3].setSize(WIDTH,HEIGHT);
		panel[3].setLayout(null);
		panel[3].add(b31);
		panel[3].add(b32);
		panel[3].add(b33);
		panel[3].add(b34);
		panel[3].add(b35);
		//�����_���E�v���C�x�[�g�I�����
		ImageButton b41 = new ImageButton("�����_���}�b�`",buttonIcon2,28,false);
		b41.setBounds(WIDTH/2-280,370,270,80);
		b41.setActionCommand("RandomMatch,-1");
		b41.addMouseListener(this);
		ImageButton b42 = new ImageButton("�v���C�x�[�g�}�b�`",buttonIcon2,28,false);
		b42.setBounds(WIDTH/2+10,370,270,80);
		b42.setActionCommand("Switch,6");
		b42.addMouseListener(this);
		ImageButton b43 = new ImageButton("�߂�",buttonIcon1,20,false);
		b43.setBounds(470,480,120,60);
		b43.setActionCommand("Switch,3");
		b43.addMouseListener(this);
		panel[4] = new ImagePanel(backImage[4]);
		panel[4].setSize(WIDTH,HEIGHT);
		panel[4].setLayout(null);
		panel[4].add(b41);
		panel[4].add(b42);
		panel[4].add(b43);
		//�}�b�`���O�ҋ@���
		ImageButton b51 = new ImageButton("�߂�",buttonIcon1,20,false);
		b51.setBounds(470,480,120,60);
		b51.setActionCommand("CancelMatch,-1");
		b51.addMouseListener(this);
		panel[5] = new ImagePanel(backImage[5]);
		panel[5].setSize(WIDTH,HEIGHT);
		panel[5].setLayout(null);
		panel[5].add(b51);
		//�����̍쐬�E�����I�����
		ImageButton b61 = new ImageButton("���������",buttonIcon2,28,false);
		b61.setBounds(WIDTH/2-280,370,270,80);
		b61.setActionCommand("Switch,7");
		b61.addMouseListener(this);
		ImageButton b62 = new ImageButton("�����ɓ���",buttonIcon2,28,false);
		b62.setBounds(WIDTH/2+10,370,270,80);
		b62.setActionCommand("KeyroomList,-1");
		b62.addMouseListener(this);
		ImageButton b63 = new ImageButton("�߂�",buttonIcon1,20,false);
		b63.setBounds(470,480,120,60);
		b63.setActionCommand("Switch,4");
		b63.addMouseListener(this);
		panel[6] = new ImagePanel(backImage[6]);
		panel[6].setSize(WIDTH,HEIGHT);
		panel[6].setLayout(null);
		panel[6].add(b61);
		panel[6].add(b62);
		panel[6].add(b63);
		//�����̐ݒ���
		JLabel label7_1 = new JLabel("�`���b�g");
		label7_1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_1.setForeground(Color.WHITE);
		rb71 = new JRadioButton("ON",true);
		rb71.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		rb71.setForeground(Color.WHITE);
		rb71.setOpaque(false);
		rb72 = new JRadioButton("OFF",false);
		rb72.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		rb72.setForeground(Color.WHITE);
		rb72.setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		group.add(rb71);
		group.add(rb72);
		JLabel label7_2 = new JLabel("��������");
		label7_2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_2.setForeground(Color.WHITE);
		String[] time = {"1","3","5"};
		box7 = new JComboBox<String>(time);
		JLabel label7_3 = new JLabel("��");
		label7_3.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_3.setForeground(Color.WHITE);
		JLabel label7_4 = new JLabel("�p�X���[�h");
		label7_4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_4.setForeground(Color.WHITE);
		passfield7 = new JTextField(20);
		passfield7.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		passfield7.setSize(300,40);
		label7_5 = new JLabel(" ");
		label7_5.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_5.setForeground(Color.WHITE);
		label7_5.setBounds(100,430,WIDTH,50);
		ImageButton b71 = new ImageButton("����",buttonIcon1,20,false);
		b71.setBounds(320,480,120,60);
		b71.setActionCommand("MakeKeyroom,-1");
		b71.addMouseListener(this);
		ImageButton b72 = new ImageButton("�߂�",buttonIcon1,20,false);
		b72.setBounds(470,480,120,60);
		b72.setActionCommand("Switch,6");
		b72.addMouseListener(this);
		JPanel panel7_1 = new JPanel();
		panel7_1.setBounds(0,280,WIDTH,50);
		panel7_1.setOpaque(false);
		panel7_1.add(label7_1);
		panel7_1.add(rb71);
		panel7_1.add(rb72);
		JPanel panel7_2 = new JPanel();
		panel7_2.setBounds(0,330,WIDTH,50);
		panel7_2.setOpaque(false);
		panel7_2.add(label7_2);
		panel7_2.add(box7);
		panel7_2.add(label7_3);
		JPanel panel7_3 = new JPanel();
		panel7_3.setBounds(0,380,WIDTH,50);
		panel7_3.setOpaque(false);
		panel7_3.add(label7_4);
		panel7_3.add(passfield7);
		panel[7] = new ImagePanel(backImage[7]);
		panel[7].setSize(WIDTH,HEIGHT);
		panel[7].setLayout(null);
		panel[7].add(panel7_1);
		panel[7].add(panel7_2);
		panel[7].add(panel7_3);
		panel[7].add(label7_5);
		panel[7].add(b71);
		panel[7].add(b72);
		//����̓����ҋ@���
		ImageButton b81 = new ImageButton("�߂�",buttonIcon1,20,false);
		b81.setBounds(470,480,120,60);
		b81.setActionCommand("DeleteKeyroom,-1");
		b81.addMouseListener(this);
		panel[8] = new ImagePanel(backImage[8]);
		panel[8].setSize(WIDTH,HEIGHT);
		panel[8].setLayout(null);
		panel[8].add(b81);
		//�������I�����
		listPanel9 = new JPanel();
		listPanel9.setMinimumSize(new Dimension(WIDTH-200,HEIGHT-200));
		listPanel9.setLayout(new BoxLayout(listPanel9,BoxLayout.Y_AXIS));
		ImageButton b91 = new ImageButton("�߂�",buttonIcon1,20,false);
		b91.setBounds(470,480,120,60);
		b91.setActionCommand("Switch,6");
		b91.addMouseListener(this);
		JScrollPane sp9 = new JScrollPane(listPanel9,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp9.setMaximumSize(new Dimension(WIDTH-200,HEIGHT-200));
		sp9.setBounds(100,50,WIDTH-200,HEIGHT-200);
		panel[9] = new ImagePanel(backImage[9]);
		panel[9].setSize(WIDTH,HEIGHT);
		panel[9].setLayout(null);
		panel[9].add(sp9);
		panel[9].add(b91);
		//�����p�X���[�h���͉��
		JLabel label10_1 = new JLabel("�p�X���[�h");
		label10_1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label10_1.setForeground(Color.WHITE);
		passfield10 = new JPasswordField(20);
		passfield10.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		passfield10.setSize(300,40);
		label10_2 = new JLabel("");
		label10_2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label10_2.setForeground(Color.WHITE);
		label10_2.setBounds(100,330,WIDTH,50);
		ImageButton b101 = new ImageButton("OK",buttonIcon1,20,false);
		b101.setBounds(320,480,120,60);
		b101.setActionCommand("EnterKeyroom,-1");
		b101.addMouseListener(this);
		ImageButton b102 = new ImageButton("�߂�",buttonIcon1,20,false);
		b102.setBounds(470,480,120,60);
		b102.setActionCommand("Switch,9");
		b102.addMouseListener(this);
		JPanel panel10_1 = new JPanel();
		panel10_1.setBounds(0,280,WIDTH,50);
		panel10_1.setOpaque(false);
		panel10_1.add(label10_1);
		panel10_1.add(passfield10);
		panel[10] = new ImagePanel(backImage[10]);
		panel[10].setSize(WIDTH,HEIGHT);
		panel[10].setLayout(null);
		panel[10].add(panel10_1);
		panel[10].add(label10_2);
		panel[10].add(b101);
		panel[10].add(b102);
		//�΋ǉ��
		tablePanel = new JPanel();//�Ֆʕ\���p�l��
		tablePanel.setBounds(25,25,TABLE_W,TABLE_H);
		tablePanel.setOpaque(false);
		tablePanel.setLayout(null);
		JLabel columnNumber = new JLabel(CLM[0]+"�@�@"+CLM[1]+"�@�@"+CLM[2]+"�@�@"+CLM[3]+"�@�@"+CLM[4]+"�@�@"+CLM[5]+"�@�@"+CLM[6]+"�@�@"+CLM[7]);//��ԍ��̕\��
		columnNumber.setBounds(35, 0, TABLE_W, FIELD_H);
		columnNumber.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		columnNumber.setForeground(Color.WHITE);
		JLabel rowNumber = new JLabel("<html>"+ROW[0]+"<br/><br/>"+ROW[1]+"<br/><br/>"+ROW[2]+"<br/><br/>"+ROW[3]+"<br/><br/>"+ROW[4]+"<br/><br/>"+ROW[5]+"<br/><br/>"+ROW[6]+"<br/><br/>"+ROW[7]+"</html>");//�s�ԍ��̕\��
		rowNumber.setBounds(7, 15, FIELD_W, TABLE_H);
		rowNumber.setFont(new Font(Font.SANS_SERIF,Font.BOLD,17));
		rowNumber.setForeground(Color.WHITE);
		JPanel infoPanel = new JPanel();//�΋Ǐ��\���p�l��
		infoPanel.setBounds(TABLE_W+20,10,200,160);
		infoPanel.setOpaque(false);
		label11_1 = new JLabel("          ");
		label11_1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_1.setForeground(Color.WHITE);
		label11_2 = new JLabel("���F�Q");
		label11_2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_2.setForeground(Color.WHITE);
		JLabel label11_3 = new JLabel("vs");
		label11_3.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_3.setForeground(Color.WHITE);
		label11_4 = new JLabel("���F�Q");
		label11_4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_4.setForeground(Color.WHITE);
		label11_5 = new JLabel("          ");
		label11_5.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_5.setForeground(Color.WHITE);
		label11_6 = new JLabel("�������ԁF0��0�b");
		label11_6.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_6.setForeground(Color.WHITE);
		JPanel infoPanel1 = new JPanel();
		infoPanel1.setOpaque(false);
		infoPanel1.add(label11_1);
		infoPanel1.add(label11_2);
		JPanel infoPanel2 = new JPanel();
		infoPanel2.setOpaque(false);
		infoPanel2.add(label11_3);
		JPanel infoPanel3 = new JPanel();
		infoPanel3.setOpaque(false);
		infoPanel3.add(label11_4);
		infoPanel3.add(label11_5);
		JPanel infoPanel4 = new JPanel();
		infoPanel4.setOpaque(false);
		infoPanel4.add(label11_6);
		infoPanel.setLayout(new GridLayout(4,1));
		infoPanel.add(infoPanel1);
		infoPanel.add(infoPanel2);
		infoPanel.add(infoPanel3);
		infoPanel.add(infoPanel4);
		playerPanel = new JPanel();//�΋ǎґ���p�l��
		playerPanel.setBounds(TABLE_W+20,170,200,100);
		playerPanel.setOpaque(false);
		b11_1 = new ImageButton("�p�X",buttonIcon2,16,false);
		b11_1.setBounds(10,10,90,40);
		b11_1.setActionCommand("Pass,-1");
		b11_1.addMouseListener(this);
		b11_1.setVisible(false);
		ImageButton b11_2 = new ImageButton("����",buttonIcon2,16,false);
		b11_2.setBounds(100,10,90,40);
		b11_2.setActionCommand("Giveup,-1");
		b11_2.addMouseListener(this);
		ImageButton b11_3 = new ImageButton("�A�V�X�g",buttonIcon2,16,false);
		b11_3.setBounds(10,50,90,40);
		b11_3.setActionCommand("Assist,-1");
		b11_3.addMouseListener(this);
		label11_7 = new JLabel("�FON");
		label11_7.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_7.setForeground(Color.WHITE);
		label11_7.setBounds(100,50,90,40);
		playerPanel.setLayout(null);
		playerPanel.add(b11_1);
		playerPanel.add(b11_2);
		playerPanel.add(b11_3);
		playerPanel.add(label11_7);
		reactPanel = new JPanel();//�ϐ�ґ���p�l��
		reactPanel.setBounds(TABLE_W+20,280,200,100);
		reactPanel.setOpaque(false);
		ImageButton b11_a = new ImageButton("������",buttonIcon2,16,false);
		b11_a.setSize(100,40);
		b11_a.setActionCommand("Reaction,���񂪂����˂��܂���");
		b11_a.addMouseListener(this);
		ImageButton b11_b = new ImageButton("����",buttonIcon2,16,false);
		b11_b.setSize(100,40);
		b11_b.setActionCommand("Reaction,���񂪔���𑗂�܂���");
		b11_b.addMouseListener(this);
		ImageButton b11_c = new ImageButton("�H�H",buttonIcon2,16,false);
		b11_c.setSize(100,40);
		b11_c.setActionCommand("Reaction,�F�H�H");
		b11_c.addMouseListener(this);
		ImageButton b11_d = new ImageButton("�I�I",buttonIcon2,16,false);
		b11_d.setSize(100,40);
		b11_d.setActionCommand("Reaction,�F�I�I");
		b11_d.addMouseListener(this);
		ImageButton b11_e = new ImageButton("�ޏo",buttonIcon2,16,false);
		b11_e.setSize(100,40);
		b11_e.setActionCommand("GetoutWatchroom,-1");
		b11_e.addMouseListener(this);
		reactPanel.setLayout(new GridLayout(3,2));
		reactPanel.add(b11_a);
		reactPanel.add(b11_b);
		reactPanel.add(b11_c);
		reactPanel.add(b11_d);
		reactPanel.add(b11_e);
		logPanel = new JPanel();//���O�\���p�l��
		logPanel.setBounds(20,TABLE_H+20,TABLE_W,170);
		logPanel.setOpaque(false);
		logArea = new JTextArea(8,34);
		logArea.setFont(new Font(Font.SANS_SERIF,Font.BOLD,10));
		logArea.setEditable(false);
		JScrollPane sp = new JScrollPane(logArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logPanel.setLayout(new FlowLayout());
		logPanel.add(sp);
		chatPanel = new JPanel();//�`���b�g�\���p�l��
		chatPanel.setBounds(TABLE_W+20,390,200,150);
		chatPanel.setOpaque(false);
		ImageButton b11_4 = new ImageButton("���O�\��",buttonIcon2,16,false);
		b11_4.setBounds(10,10,90,40);
		b11_4.setActionCommand("Showlog,-1");
		b11_4.addMouseListener(this);
		label11_8 = new JLabel("�FON");
		label11_8.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_8.setForeground(Color.WHITE);
		label11_8.setBounds(100,10,90,40);
		chatField = new JTextField(17);
		ImageButton b11_5 = new ImageButton("���M",buttonIcon2,16,false);
		b11_5.setBounds(100,0,90,40);
		b11_5.setActionCommand("Chat,-1");
		b11_5.addMouseListener(this);
		JPanel chatPanel1 = new JPanel();
		chatPanel1.setOpaque(false);
		chatPanel1.setLayout(null);
		chatPanel1.add(b11_4);
		chatPanel1.add(label11_8);
		JPanel chatPanel2 = new JPanel();
		chatPanel2.setOpaque(false);
		chatPanel2.add(chatField);
		JPanel chatPanel3 = new JPanel();
		chatPanel3.setOpaque(false);
		chatPanel3.setLayout(null);
		chatPanel3.add(b11_5);
		chatPanel.setLayout(new GridLayout(3,1));
		chatPanel.add(chatPanel1);
		chatPanel.add(chatPanel2);
		chatPanel.add(chatPanel3);
		panel[11] = new ImagePanel(backImage[11]);//�ǉ�
		panel[11].setLayout(null);
		panel[11].setSize(WIDTH,HEIGHT);
		panel[11].add(tablePanel);
		panel[11].add(columnNumber);
		panel[11].add(rowNumber);
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
		ImageButton b121 = new ImageButton("�߂�",buttonIcon1,20,false);
		b121.setBounds(470,480,120,60);
		b121.setActionCommand("Switch,3");
		b121.addMouseListener(this);
		JScrollPane sp12 = new JScrollPane(listPanel12,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp12.setMaximumSize(new Dimension(WIDTH-200,HEIGHT-200));
		sp12.setBounds(100,50,WIDTH-200,HEIGHT-200);
		panel[12] = new ImagePanel(backImage[12]);
		panel[12].setLayout(null);
		panel[12].add(sp12);
		panel[12].add(b121);
		//�L�^�I�����
		ImageButton b141 = new ImageButton("�������",buttonIcon2,28,false);
		b141.setBounds(WIDTH/2-280,370,270,80);
		b141.setActionCommand("TotalRecord,-1");
		b141.addMouseListener(this);
		ImageButton b142 = new ImageButton("����ʐ��",buttonIcon2,28,false);
		b142.setBounds(WIDTH/2+10,370,270,80);
		b142.setActionCommand("Switch,16");
		b142.addMouseListener(this);
		ImageButton b143 = new ImageButton("�߂�",buttonIcon1,20,false);
		b143.setBounds(470,480,120,60);
		b143.setActionCommand("Switch,3");
		b143.addMouseListener(this);
		panel[14] = new ImagePanel(backImage[14]);
		panel[14].setSize(WIDTH,HEIGHT);
		panel[14].setLayout(null);
		panel[14].add(b141);
		panel[14].add(b142);
		panel[14].add(b143);
		//�������щ��
		label15_1 = new JLabel("�������F");
		label15_1.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label15_1.setForeground(Color.WHITE);
		label15_1.setBounds(50,200,250,100);
		label15_2 = new JLabel("�������F");
		label15_2.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label15_2.setForeground(Color.WHITE);
		label15_2.setBounds(360,200,250,100);
		label15_3 = new JLabel("�������F");
		label15_3.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label15_3.setForeground(Color.WHITE);
		label15_3.setBounds(50,350,250,100);
		label15_4 = new JLabel("�������F�F");
		label15_4.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label15_4.setForeground(Color.WHITE);
		label15_4.setBounds(360,350,250,100);
		ImageButton b151 = new ImageButton("OK",buttonIcon1,20,false);
		b151.setBounds(470,480,120,60);
		b151.setActionCommand("Switch,14");
		b151.addMouseListener(this);
		panel[15] = new ImagePanel(backImage[15]);
		panel[15].setSize(WIDTH,HEIGHT);
		panel[15].setLayout(null);
		panel[15].add(label15_1);
		panel[15].add(label15_2);
		panel[15].add(label15_3);
		panel[15].add(label15_4);
		panel[15].add(b151);
		//���薼���͉��
		JLabel label16_1 = new JLabel("���薼�F");
		label16_1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label16_1.setForeground(Color.WHITE);
		field16 = new JPasswordField(20);
		field16.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		field16.setSize(300,40);
		label16_2 = new JLabel("");
		label16_2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label16_2.setForeground(Color.WHITE);
		label16_2.setBounds(100,330,WIDTH,50);
		ImageButton b161 = new ImageButton("OK",buttonIcon1,20,false);
		b161.setBounds(320,480,120,60);
		b161.setActionCommand("IdRecord,-1");
		b161.addMouseListener(this);
		ImageButton b162 = new ImageButton("�߂�",buttonIcon1,20,false);
		b162.setBounds(470,480,120,60);
		b162.setActionCommand("Switch,14");
		b162.addMouseListener(this);
		JPanel panel16_1 = new JPanel();
		panel16_1.setBounds(0,280,WIDTH,50);
		panel16_1.setOpaque(false);
		panel16_1.add(label16_1);
		panel16_1.add(field16);
		panel[16] = new ImagePanel(backImage[16]);
		panel[16].setSize(WIDTH,HEIGHT);
		panel[16].setLayout(null);
		panel[16].add(panel16_1);
		panel[16].add(label16_2);
		panel[16].add(b161);
		panel[16].add(b162);
		//����ʐ�щ��
		label17_1 = new JLabel("�������F");
		label17_1.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label17_1.setForeground(Color.WHITE);
		label17_1.setBounds(50,200,250,100);
		label17_2 = new JLabel("�������F");
		label17_2.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label17_2.setForeground(Color.WHITE);
		label17_2.setBounds(360,200,250,100);
		label17_3 = new JLabel("�������F");
		label17_3.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label17_3.setForeground(Color.WHITE);
		label17_3.setBounds(50,350,250,100);
		label17_4 = new JLabel("�������F�F");
		label17_4.setFont(new Font("�q���M�m����W�U",Font.BOLD,48));
		label17_4.setForeground(Color.WHITE);
		label17_4.setBounds(360,350,250,100);
		ImageButton b171 = new ImageButton("OK",buttonIcon1,20,false);
		b171.setBounds(470,480,120,60);
		b171.setActionCommand("Switch,16");
		b171.addMouseListener(this);
		panel[17] = new ImagePanel(backImage[15]);
		panel[17].setSize(WIDTH,HEIGHT);
		panel[17].setLayout(null);
		panel[17].add(label17_1);
		panel[17].add(label17_2);
		panel[17].add(label17_3);
		panel[17].add(label17_4);
		panel[17].add(b171);
		//�V�ѕ��\�����
		JTextArea ruleArea = new JTextArea(30,100);
		ruleArea.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		ruleArea.setBackground(new Color(0.1f,0.1f,0.1f));
		ruleArea.setForeground(Color.WHITE);
		ruleArea.setText(RULE);
		ruleArea.setEditable(false);
		JScrollPane sp18 = new JScrollPane(ruleArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp18.setBounds(50,50,WIDTH-100,400);
		ImageButton b181 = new ImageButton("�߂�",buttonIcon1,20,false);
		b181.setBounds(470,480,120,60);
		b181.setActionCommand("Switch,3");
		b181.addMouseListener(this);
		panel[18] = new ImagePanel(backImage[18]);
		panel[18].setSize(WIDTH,HEIGHT);
		panel[18].setLayout(null);
		panel[18].add(sp18);
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
			socket = new Socket(ipAddress, port);					//�T�[�o�ɐڑ��v���𑗐M
			System.out.println("�T�[�o�Ɛڑ����܂���");
			out = new DataOutputStream(socket.getOutputStream());	//�o�̓X�g���[�����쐬
			receiver = new Receiver(socket);						//��M�X���b�h���쐬
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
				in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); //���̓X�g���[�����쐬
			}
			catch (IOException e) {
				System.err.println("�T�[�o�ڑ����ɃG���[���������܂���: " + e);
				System.exit(-1);
			}
		}
		public void run(){
			try{
				while(true) {
					String inputLine = in.readUTF();			//�o�b�t�@��ǂݍ���
					if (inputLine != null){						//�f�[�^��M������
						System.out.println(player.isOppose());
						System.out.println("��M�F" +inputLine);//�e�X�g�o��
						try{
							receiveMessage(inputLine);				//����
						}
						catch (ArrayIndexOutOfBoundsException e){
							System.out.println("��M�f�[�^�������ɃG���[���������܂���: " + e);
							panelID = 3;
							switchDisplay();
							resetRoom();
						}
					}
				}
			}
			catch (IOException e){
				System.err.println("�f�[�^��M���ɃG���[���������܂���: " + e);
				Client.this.connectServer("localhost",10000);
				panelID = 0;
				switchDisplay();
				resetRoom();
			}
		}
	}
	//��M�f�[�^�̔��ʁE����
	public void receiveMessage(String msg) throws ArrayIndexOutOfBoundsException {
		String[] c = Operation.split(",",2);							//�I�y���[�V�������R�}���h�ƕt�����ɕ���
		String command = c[0];	//�R�}���h
		String subc = c[1];		//�t�����
		//�΋Ǐ��ȊO
		if(!player.isOppose()) {
			//���O�C������
			if(command.equals("Login")){
				if(msg.equals("true")){																//���O�C������
					showDialog("���O�C�����܂���");														//�_�C�A���O�̕\��
					panelID = 3;																		//���j���[��ʂ�
					switchDisplay();																	//��ʑJ��
				}
				else{																				//���O�C�����s
					if(msg.equals("name")) label1.setText("���̂悤�ȃv���C�����͑��݂��܂���");		//���b�Z�[�W�\��
					if(msg.equals("pass")) label1.setText("�p�X���[�h���Ⴂ�܂�");						//���b�Z�[�W�\��
					if(msg.equals("login")) label1.setText("���̃��[�U�͊��Ƀ��O�C������Ă��܂�");
					player.setName(null);																//�v���C�������Z�b�g
					player.setPass(null);																//�p�X���[�h���Z�b�g
				}
			}
			//�o�^����
			else if(command.equals("Register")){
				if(msg.equals("true")){										//�o�^����
					showDialog("�V�K�o�^���܂���");								//�_�C�A���O�̕\��
					Operation = "Login,-1";										//���O�C���I�y���[�V�������s
					sendMessage(dataID.get("Login")+","+player.getName()+","+player.getPass());
				}
				else{														//�o�^���s
					label2.setText("���̃v���C�����͊��Ɏg���Ă��܂�");		//���b�Z�[�W�\��
					player.setName(null);										//�v���C�������Z�b�g
					player.setPass(null);										//�p�X���[�h���Z�b�g
				}
			}
			//�����_���}�b�`���O����
			else if(command.equals("RandomMatch")){
				String[] info = msg.split(",",3);
				if(info[0].equals("true")){									//�}�b�`���O����
					if(info[2].equals("0")){									//��肾�����ꍇ
						player.setColor("black");									//��ԕۑ�
						othello.setBlackName(player.getName());						//��薼�ۑ�
						label11_1.setText(player.getName());						//��薼�\��
						othello.setWhiteName(info[1]);								//��薼�ۑ�
						label11_5.setText(info[1]);									//��薼�\��
					}
					if(info[2].equals("1")){									//��肾�����ꍇ
						player.setColor("white");									//��ԕۑ�
						othello.setBlackName(info[1]);								//��薼�ۑ�
						label11_1.setText(info[1]);									//��薼�\��
						othello.setWhiteName(player.getName());						//��薼�ۑ�
						label11_5.setText(player.getName());						//��薼�\��
						playerPanel.setVisible(false);								//���얳����
					}
					player.beOppose(true);										//�΋ǃ��[�h��
					reactPanel.setVisible(false);								//���A�N�V�����p�l��������
					player.setChat(true);										//�`���b�g�̗L���ۑ�
					player.setTime(1);											//�������ԕۑ�
					updateTable();												//�Ֆʕ\��
					resetTimer();												//�^�C�}�[���Z�b�g
					timer.start();												//�^�C�}�[�X�^�[�g
					panelID = 11;												//�΋ǉ�ʂ�
				}
				if(info[0].equals("false")){								//�}�b�`���O���s
					showDialog("�}�b�`���O�Ɏ��s���܂���");						//�_�C�A���O�̕\��
					panelID = 4;												//�����_���E�v���C�x�[�g�I����ʂ�
				}
				switchDisplay();											//��ʑJ��
			}
			//�������}�b�`���O����
			else if(command.equals("MakeKeyroom")){
				String[] info = msg.split(",",2);
				player.beOppose(true);										//�΋ǃ��[�h��
				player.setColor("black");									//��ԕۑ�
				othello.setBlackName(player.getName());						//��薼�ۑ�
				label11_1.setText(player.getName());						//��薼�\��
				othello.setWhiteName(info[1]);								//��薼�ۑ�
				label11_5.setText(info[1]);									//��薼�\��
				updateTable();												//�Ֆʕ\��
				reactPanel.setVisible(false);								//���A�N�V�����p�l��������
				if(!player.getChat()) chatPanel.setVisible(false);			//�`���b�g���Ȃ�`���b�g�p�l��������
				resetTimer();												//�^�C�}�[���Z�b�g
				timer.start();												//�^�C�}�[�X�^�[�g
				panelID = 11;												//�΋ǉ�ʂ�
				switchDisplay();											//��ʑJ��
			}
			//���������X�g
			else if(command.equals("KeyroomList")){
				if(msg.equals("no")){										//���X�g������������
					showDialog("���݁A�����ł��镔��������܂���");				//�_�C�A���O�\��
					panelID = 6;												//�����̍쐬�E�����I����ʂ�
					switchDisplay();											//��ʑJ��
				}
				else{														//���X�g�������
					String[] keyroom = msg.split(",",0);
					String[] info;	//�������
					String strchat;	//�`���b�g�̗L��
					listPanel9.removeAll();										//�I�����{�^�����Z�b�g
					for(int i=0;i<keyroom.length;i++){							//�I�����{�^������
						info = keyroom[i].split(Pattern.quote("."),4);
						if(info[2].equals("true")) strchat = "����";
						else strchat = "�Ȃ�";
						JButton bi = new JButton("<html>�쐬�ҁF"+info[1]+"<br/>�`���b�g�F"+strchat+" �������ԁF"+info[3]+"��</html>");
						bi.setActionCommand("SelectKeyroom,"+keyroom[i]);
						bi.addMouseListener(this);
						listPanel9.add(bi);
					}
					panelID = 9;												//���������X�g�\����ʂ�
					switchDisplay();											//��ʑJ��
				}
			}
			//�������ւ̓���
			else if(command.equals("EnterKeyroom")){
				String[] info = msg.split(",",2);
				if(info[0].equals("true")){									//��������
					player.beOppose(true);										//�΋ǃ��[�h��
					player.setColor("white");									//��ԕۑ�
					othello.setBlackName(info[1]);								//��薼�ۑ�
					label11_1.setText(info[1]);									//��薼�\��
					othello.setWhiteName(player.getName());						//��薼�ۑ�
					label11_5.setText(player.getName());						//��薼�\��
					updateTable();												//�Ֆʕ\��
					reactPanel.setVisible(false);								//���A�N�V�����p�l��������
					if(!player.getChat()) chatPanel.setVisible(false);			//�`���b�g���Ȃ�`���b�g�p�l��������
					playerPanel.setVisible(false);								//���얳����
					resetTimer();												//�^�C�}�[���Z�b�g
					timer.start();												//�^�C�}�[�X�^�[�g
					panelID = 11;												//�΋ǉ�ʂ�
				}
				if(info[0].equals("false")){								//�������s
					resetRoom();												//������񃊃Z�b�g
					showDialog("�����Ɏ��s���܂���");							//�_�C�A���O�̕\��
					panelID = 6;												//���j���[��ʂ�
				}
				switchDisplay();											//��ʑJ��
			}
			//�ϐ핔�����X�g
			else if(command.equals("WatchroomList")){
				if(msg.equals("no")){										//���X�g������������
					showDialog("���݁A�ϐ�ł��镔��������܂���");				//�_�C�A���O�\��
					panelID = 3;												//���j���[��ʂ�
					switchDisplay();											//��ʑJ��
				}
				else{														//���X�g����������
					String[] room = msg.split(",",0);
					String[] info;//�������
					listPanel12.removeAll();									//�I�����{�^�����Z�b�g
					for(int i=0;i<room.length;i++){								//�I�����{�^������
						info = room[i].split(Pattern.quote("."),5);
						JButton bi = new JButton("<html>���F"+info[1]+" ���F"+info[2]+"<br/>���΁F"+info[3]+" ���΁F"+info[4]+"</html>");
						bi.setActionCommand("EnterWatchroom,"+room[i]);
						bi.addMouseListener(this);
						listPanel12.add(bi);
					}
					panelID = 12;												//�ϐ핔�����X�g�\����ʂ�
					switchDisplay();											//��ʑJ��
				}
			}
			//�ϐ핔���ւ̓���
			else if(command.equals("EnterWatchroom")){
				if(msg.equals("false")){									//�������s
					resetRoom();												//������񃊃Z�b�g
					showDialog("�����Ɏ��s���܂���");							//�_�C�A���O�̕\��
					panelID = 3;												//���j���[��ʂ�
				}
				else{														//��������
					player.beOppose(true);										//�΋ǃ��[�h��
					player.beStand(true);										//�ϐ탂�[�h��
					player.setAssist(false);									//�A�V�X�g������
					playerPanel.setVisible(false);								//���얳����
					chatPanel.setVisible(false);								//�`���b�g�p�l��������
					player.setColor("black");									//��ԕۑ�
					label11_1.setText(othello.getBlackName());					//��薼�\��
					label11_5.setText(othello.getWhiteName());					//��薼�\��
					String[] grids = msg.split(",");
					for(int i=0;i<othello.getRow()*othello.getRow();i++){		//�Ֆʕϊ�
						if(grids[i].equals("0")) grids[i] = "board";//��
						if(grids[i].equals("1")) grids[i] = "black";//��
						if(grids[i].equals("2")) grids[i] = "white";//��
					}
					othello.setGrids(grids);									//�Ֆʔ��f
					updateTable();												//�ՖʍX�V
					sendMessage(dataID.get("Reaction")+","+player.getName()+"���������܂���");
					panelID = 11;												//�ϐ��ʂ�
				}
				switchDisplay();											//��ʑJ��
			}
			//�����L�^
			else if(command.equals("TotalRecord")){
				String[] info = msg.split(",",4);
				label15_1.setText("�������F"+info[0]);						//�L�^�\��
				label15_2.setText("�������F"+info[1]);
				label15_3.setText("�������F"+info[2]);
				label15_4.setText("�������F"+info[3]);
				panelID = 15;												//������щ�ʂ�
				switchDisplay();											//��ʑJ��
			}
			//�ΐl�ʋL�^
			else if(command.equals("IdRecord")){
				String[] info = msg.split(",",4);
				label17_1.setText("�������F"+info[0]);						//�L�^�\��
				label17_2.setText("�������F"+info[1]);
				label17_3.setText("�������F"+info[2]);
				label17_4.setText("�������F"+info[3]);
				panelID = 17;												//����ʐ�щ�ʂ�
				switchDisplay();											//��ʑJ��
			}
		}
		//�΋�(�ϐ�)���
		else if(player.isOppose()){
			String[] info = msg.split(",",0);							//�ȉ��A��M���e�ŏꍇ����
			//�ՖʁE���O��M
			if(info[0].equals(dataID.get("Table"))){
				String table = info[1];																//�Ֆʎ擾
				String[] grids = table.split(Pattern.quote("."),othello.getRow()*othello.getRow()); //�Ֆʕϊ�
				for(int i=0;i<othello.getRow()*othello.getRow();i++){
					if(grids[i].equals("0")) grids[i] = "board";//��
					if(grids[i].equals("1")) grids[i] = "black";//��
					if(grids[i].equals("2")) grids[i] = "white";//��
				}
				othello.setGrids(grids);															//�ՖʍX�V
				pathFlag = false;																	//�p�X�t���O���Z�b�g
				othello.changeTurn();																//�^�[���ύX
				if(updateTable()) b11_1.setVisible(false);											//�Ֆʔ��f
				else b11_1.setVisible(true);														//�u����ꏊ��������΃p�X�{�^����L����
				String log = info[2];																//���O�擾
				logArea.setEditable(true);															//���O���f
				logArea.append("\n"+log);
				logArea.setEditable(false);
				playerPanel.setVisible(true);														//����L����
				resetTimer();																		//�^�C�}�[���Z�b�g
				timer.restart();																	//�^�C�}�[���X�^�[�g
				if(othello.isGameover()) {															//������������
					String result = othello.checkWinner();										//���s�m�F
					if(!player.isStand()){													//�΋ǎ҂̏ꍇ
						if(result.equals("draw")) {								//��������
							sendMessage(dataID.get("Finish")+",3");				//�T�[�o�ɑ��M
							showDialog("��������");								//�_�C�A���O�\��
						}
						if(result.equals(player.getColor())) {					//����
							sendMessage(dataID.get("Finish")+",1");				//�T�[�o�ɑ��M
							showDialog("���Ȃ��̏����ł�");						//�_�C�A���O�\��
						}
						else{													//����
							sendMessage(dataID.get("Finish")+",2");				//�T�[�o�ɑ��M
							showDialog("���Ȃ��̕����ł�");						//�_�C�A���O�\��
						}
					}
					else{																	//�ϐ�҂̏ꍇ
						sendMessage(dataID.get("GetoutWatchroom"));				//�T�[�o�ɑ��M
						if(result.equals("draw")) showDialog("��������");		//�_�C�A���O�\��
						if(result.equals("black")) showDialog(othello.getBlackName()+"�̏����ł�");	//�_�C�A���O�\��
						else showDialog(othello.getWhiteName()+"�̏����ł�");						//�_�C�A���O�\��
					}
					panelID = 3;																//���j���[��ʂ�
					switchDisplay();															//��ʑJ��
					resetRoom();																//������񃊃Z�b�g
				}
			}
			//�p�X��M
			else if(info[0].equals(dataID.get("Pass"))){
				playerPanel.setVisible(true);														//����L����
				logArea.setEditable(true);															//���O�ɕ\��
				if(player.getColor().equals("black")) logArea.append("\n��肪�p�X���܂���");
				else logArea.append("\n��肪�p�X���܂���");
				logArea.setEditable(false);
				resetTimer();																		//�^�C�}�[���Z�b�g
				timer.restart();																	//�^�C�}�[���X�^�[�g
				othello.changeTurn();																//�^�[���ύX
				if(pathFlag == true){																//���O�Ɏ������p�X���Ă����玎���I��
					String result = othello.checkWinner();						//���s�m�F
					if(result.equals("draw")) {									//��������
						sendMessage(dataID.get("Finish")+",3");					//�T�[�o�ɑ��M
						showDialog("��������");									//�_�C�A���O�\��
					}
					if(result.equals(player.getColor())) {						//����
						sendMessage(dataID.get("Finish")+",1");					//�T�[�o�ɑ��M
						showDialog("���Ȃ��̏����ł�");							//�_�C�A���O�\��
					}
					else{														//����
						sendMessage(dataID.get("Finish")+",2");					//�T�[�o�ɑ��M
						showDialog("���Ȃ��̕����ł�");							//�_�C�A���O�\��
					}
					panelID = 3;												//���j���[��ʂ�
					switchDisplay();											//��ʑJ��
					resetRoom();												//������񃊃Z�b�g
				}
			}
			//������M
			else if(info[0].equals(dataID.get("Giveup"))){
				if(!player.isStand()) showDialog("�ΐ푊�肪�������܂���");							//�_�C�A���O�\��
				else showDialog("����");															//�_�C�A���O�\��
				sendMessage(dataID.get("Finish")+",1");												//�T�[�o�ɑ��M
				panelID = 3;																		//���j���[��ʂ�
				switchDisplay();																	//��ʑJ��
				resetRoom();																		//������񃊃Z�b�g
			}
			//�`���b�g��M
			else if(info[0].equals(dataID.get("Chat"))){
				String chat = info[1];																//�`���b�g�ۑ�
				logArea.setEditable(true);															//�`���b�g���f
				logArea.append("\n"+chat);
				logArea.setEditable(false);
			}
			//���A�N�V������M
			else if(info[0].equals(dataID.get("Reaction"))){
				String reaction = info[1];															//���A�N�V�����ۑ�
				logArea.setEditable(true);															//���A�N�V�������f
				logArea.append("\n> "+reaction);
				logArea.setEditable(false);
			}
			//�ؒf��M
			else if(info[0].equals(dataID.get("Disconnected"))){
				if(!player.isStand()) showDialog("�ΐ푊�肪�ؒf���܂���");							//�_�C�A���O�\��
				else showDialog("�ؒf���s���܂���");												//�_�C�A���O�\��
				sendMessage(dataID.get("Finish")+",1");												//�T�[�o�ɑ��M
				panelID = 3;																		//���j���[��ʂ�
				switchDisplay();																	//��ʑJ��
				resetRoom();																		//������񃊃Z�b�g
			}
		}
	}
	// ���� //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//�}�E�X���X�i�[
	public void mouseClicked(MouseEvent e) {
		JButton button = (JButton)e.getComponent();			//�{�^������
		Operation = button.getActionCommand();				//���s�I�y���[�V��������
		acceptOperation(Operation);							//����
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	//�^�C�����X�i�[
	public void actionPerformed(ActionEvent e) {
		seconds -= 1;													//��b�o��
		if(seconds < 0){												//60�b��������
			seconds = 59;											//�b���Z�b�g
			minutes -= 1;											//�ꕪ�o��
		}
		if(minutes < 0){												//�������Ԃ��o�߂�����
			if(player.getColor()==othello.getTurn()){				//�����̔ԂȂ�
				String[] grids = othello.getGrids();			//�Ֆʎ擾
				for(int i=0;i<othello.getRow()*othello.getRow();i++){
					if(grids[i].equals("canPut")){				//�u����ꏊ������Ȃ�
						Operation = "Table,"+i;				//�΂�u���I�y���[�V�������s
						acceptOperation(Operation);
						return;
					}
				}												//�u����ꏊ���Ȃ��Ȃ�
				Operation = "Pass,-1";						//�p�X�I�y���[�V�������s
				acceptOperation(Operation);
			}
		}
		else{															//�܂��������ԓ��Ȃ�
			label11_6.setText("�������ԁF"+minutes+"��"+seconds+"�b");	//���ԕ\��
		}
	}
	//�^�C�}�[�̃��Z�b�g
	public void resetTimer(){
		minutes = player.getTime();								//�����Z�b�g
		seconds = 0;											//�b���Z�b�g
		label11_6.setText("�������ԁF0��0�b");					//�\�����Z�b�g
	}
	//����̎�t�E����
	public void acceptOperation(String operation){
		System.out.println("���s�F" +operation);//�e�X�g�o��
		String[] c = operation.split(",",2);								//�I�y���[�V�������R�}���h�ƕt�����ɕ���
		String command = c[0];	//�R�}���h
		String subc = c[1];		//�t�����
		switch(command){													//�ȉ��A�R�}���h�ŏꍇ����
			//��ʑJ�ڂ̂�
			case "Switch":
//				playSound(SE_switch);												//���ʉ��Đ�
				panelID = Integer.parseInt(subc);
				switchDisplay();
				break;
			//���O�C���v��
			case "Login":
				String logname = field1.getText();									//�v���C�����ǂݎ��
				String logpass = new String(passfield1.getPassword());				//�p�X���[�h�ǂݎ��
				if(checkString(logname,"�v���C����",label1)){
					if(checkString(logpass,"�p�X���[�h",label1)){
						player.setName(logname);									//�v���C�����ۑ�
						player.setPass(logpass);									//�p�X���[�h�ۑ�
						sendMessage(dataID.get(command)+","+logname+","+logpass);	//�T�[�o�֑��M
						break;
					}
				}
				break;
			//�V�K�o�^�v��
			case "Register":
				String regname = field2.getText();									//�v���C�����ǂݎ��
				String regpass = new String(passfield2.getPassword());				//�p�X���[�h�ǂݎ��
				if(checkString(regname,"�v���C����",label2)){
					if(checkString(regpass,"�p�X���[�h",label2)){
						player.setName(regname);									//�v���C�����ۑ�
						player.setPass(regpass);									//�p�X���[�h�ۑ�
						sendMessage(dataID.get(command)+","+regname+","+regpass);	//�T�[�o�֑��M
						break;
					}
				}
				break;
			//�����_���}�b�`���O�v��
			case "RandomMatch":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				panelID = 5;												//�}�b�`���O�ҋ@��ʂ�
				switchDisplay();											//��ʑJ��
				break;
			//�}�b�`���O�L�����Z������
			case "CancelMatch":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				panelID = 4;												//�����_���E�v���C�x�[�g�I����ʂ�
				switchDisplay();											//��ʑJ��
				break;
			//�΂�u��
			case "Table":
				if(player.getColor()==othello.getTurn()){													//�����̔ԂȂ�
					int grid = Integer.parseInt(subc);//�u�����}�X�ԍ�
					String log = "";//���O
					StringBuffer table = new StringBuffer("");//�Ֆ�
					Boolean canPut = othello.putStone(grid,player.getColor(),true);						//�΂�u���Ă݂�
					if(canPut){																			//�u����}�X�Ȃ�
						updateTable();															//�Ֆʔ��f
						String[] grids = othello.getGrids();									//�Ֆʏ��擾
						for(int i=0;i<othello.getRow()*othello.getRow();i++){					//�Ֆʏ��ϊ�
							if(grids[i].equals("board")) table.append("0.");
							if(grids[i].equals("canPut")) table.append("0.");
							if(grids[i].equals("black")) table.append("1.");
							if(grids[i].equals("white")) table.append("2.");
						}
						table.setLength(table.length()-1);
						if(player.getColor().equals("black")){									//���O��񐶐�
							log = "���F("+CLM[grid%8]+"-"+ROW[grid/8]+")";
						}
						if(player.getColor().equals("white")){
							log = "���F("+CLM[grid%8]+"-"+ROW[grid/8]+")";
						}
						logArea.setEditable(true);												//���O���f
						logArea.append("\n"+log);
						logArea.setEditable(false);
						playerPanel.setVisible(false);											//���얳����
						resetTimer();															//�^�C�}�[���Z�b�g
						timer.restart();														//�^�C�}�[���X�^�[�g
						othello.changeTurn();													//�^�[���ύX
						sendMessage(dataID.get(command)+","+table+","+log);						//�T�[�o�֑��M
						if(othello.isGameover()) {												//������������
							String result = othello.checkWinner();						//���s�m�F
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
							panelID = 3;												//���j���[��ʂ�
							switchDisplay();											//��ʑJ��
							resetRoom();												//������񃊃Z�b�g
						}
					}
				}
				break;
			//�p�X
			case "Pass":
				playerPanel.setVisible(false);																//���얳����
				pathFlag = true;																			//�p�X�t���O�𗧂Ă�
				logArea.setEditable(true);																	//���O�ɕ\��
				if(player.getColor().equals("black")) logArea.append("\n��肪�p�X���܂���");
				else logArea.append("\n��肪�p�X���܂���");
				logArea.setEditable(false);
				resetTimer();																				//�^�C�}�[���Z�b�g
				timer.restart();																			//�^�C�}�[���X�^�[�g
				othello.changeTurn();																		//��ԕύX
				sendMessage(dataID.get(command));															//�T�[�o�֑��M
				break;
			//����
			case "Giveup":
				sendMessage(dataID.get(command));															//�T�[�o�֑��M
				showDialog("�������܂���");																	//�_�C�A���O�\��
				panelID = 3;																				//���j���[��ʂ�
				switchDisplay();																			//��ʑJ��
				resetRoom();																				//������񃊃Z�b�g
				break;
			//�`���b�g
			case "Chat":
				String str = player.getName()+": "+chatField.getText();										//�`���b�g���e�ǂݎ��
				logArea.setEditable(true);																	//�`���b�g���f
				logArea.append("\n"+str);
				logArea.setEditable(false);
				chatField.setText("");																		//�`���b�g���͗����Z�b�g
				sendMessage(dataID.get(command)+","+str);													//�T�[�o�֑��M
				break;
			//�A�V�X�g�ؑ�
			case "Assist":
				player.changeAssist();																		//�A�V�X�g�ؑ�
				if(player.getAssist()==true) label11_7.setText("�FON");										//�\���ύX
				if(player.getAssist()==false) label11_7.setText("�FOFF");
				updateTable();																				//�Ֆʔ��f
				break;
			//���O�\���ؑ�
			case "Showlog":
				if(player.getShowlog()==true) {																//ON��������
					player.setShowlog(false);														//OFF�ɂ���
					label11_8.setText("�FOFF");														//�\���ύX
					logPanel.setVisible(false);														//���O���\����
				}
				else if(player.getShowlog()==false) {														//OFF��������
					player.setShowlog(true);														//ON�ɂ���
					label11_8.setText("�FON");														//�\���ύX
					logPanel.setVisible(true);														//���O��\��
				}
				break;
			//�������쐬�v��
			case "MakeKeyroom":
				boolean chat = rb71.isSelected();							//�`���b�g�̗L���ǂݎ��
				int time = Integer.parseInt((String)box7.getSelectedItem());//�������ԓǂݎ��
				String setpass = passfield7.getText();						//�p�X���[�h�ǂݎ��
				if(checkString(setpass,"�p�X���[�h",label7_5)){
					player.setChat(chat);									//�`���b�g�̗L���ۑ�
					player.setTime(time);									//�������ԕۑ�
					sendMessage(dataID.get(command)+","+setpass+","+chat+","+time);//�T�[�o�֑��M
					panelID = 8;											//����̓����ҋ@��ʂ�
					switchDisplay();										//��ʑJ��
					break;
				}
				break;
			//�������폜�v��
			case "DeleteKeyroom":
				player.setChat(true);										//�`���b�g�̗L�����Z�b�g
				player.setTime(-1);											//�������ԃ��Z�b�g
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				panelID = 4;												//�����_���E�v���C�x�[�g�I����ʂ�
				switchDisplay();											//��ʑJ��
				break;
			//���������X�g�v��
			case "KeyroomList":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				break;
			//���������X�g�I��
			case "SelectKeyroom":
				String[] keyinfo = subc.split(Pattern.quote("."),4);
				othello.setRoomID(Integer.parseInt(keyinfo[0]));			//�����ԍ��ۑ�
				player.setChat(Boolean.parseBoolean(keyinfo[2]));			//�`���b�g�̗L���ۑ�
				player.setTime(Integer.parseInt(keyinfo[3]));				//�������ԕۑ�
				panelID = 10;												//�p�X���[�h���͉�ʂ�
				switchDisplay();											//��ʑJ��
				break;
			//�������ւ̓����v��
			case "EnterKeyroom":
				String keypass = new String(passfield10.getPassword());						//�p�X���[�h�ǂݎ��
				if(checkString(keypass,"�p�X���[�h",label10_2)) {
					sendMessage(dataID.get(command)+","+othello.getRoomID()+","+keypass);	//�T�[�o�֑��M
					break;
				}
				break;
			//�ϐ핔�����X�g�v��
			case "WatchroomList":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				break;
			//�ϐ핔���ւ̓����v��
			case "EnterWatchroom":
				String[] roominfo = subc.split(Pattern.quote("."),5);
				othello.setRoomID(Integer.parseInt(roominfo[0]));			//�����ԍ��ۑ�
				othello.setBlackName(roominfo[1]);							//��薼�ۑ�
				othello.setWhiteName(roominfo[2]);							//��薼�ۑ�
				sendMessage(dataID.get(command)+","+othello.getRoomID());	//�T�[�o�֑��M
				break;
			//���A�N�V����
			case "Reaction":
				String reaction = player.getName()+subc;					//���A�N�V�����ϊ�
				logArea.setEditable(true);									//���A�N�V�������f
				logArea.append("\n> "+reaction);
				logArea.setEditable(false);
				sendMessage(dataID.get(command)+","+reaction);				//�T�[�o�֑��M
				break;
			//�ϐ�r���ޏo
			case "GetoutWatchroom":
				sendMessage(dataID.get("Reaction")+","+player.getName()+"���񂪑ޏo���܂���");
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				panelID = 3;												//���j���[��ʂ�
				switchDisplay();											//��ʑJ��
				resetRoom();												//������񃊃Z�b�g
				break;
			//�����L�^�v��
			case "TotalRecord":
				sendMessage(dataID.get(command));							//�T�[�o�֑��M
				break;
			//�ΐl�ʋL�^�v��
			case "IdRecord":
				String nameOpponent = field16.getText();
				if(checkString(nameOpponent,"����̖��O",label16_2)) {
					sendMessage(dataID.get(command)+","+nameOpponent);		//�T�[�o�֑��M
					break;
				}
				break;
			//���O�A�E�g
			case "Logout":
				player.setName(null);										//�v���C�������Z�b�g
				player.setPass(null);										//�p�X���[�h���Z�b�g
				panelID = 0;												//�^�C�g����ʂ�
				switchDisplay();											//��ʑJ��
				break;
			default:
				break;
		}
	}
	//�������̃��Z�b�g(�������E������M���E�΋ǏI�����E�ϐ�r���ޏo��)
	public void resetRoom(){
		//�����̏�����
		othello.setRoomID(-1);				//�����ԍ����Z�b�g
		player.setColor("-1");				//��ԃ��Z�b�g
		othello.setBlackName(null);			//��薼���Z�b�g
		othello.setWhiteName(null);			//��薼���Z�b�g
		player.setChat(true);				//�`���b�g�̗L�����Z�b�g
		player.setTime(-1);					//�������ԃ��Z�b�g
		player.setAssist(true);				//�A�V�X�g�̗L�����Z�b�g
		player.setShowlog(true);			//���O�\���̗L���̃��Z�b�g
		pathFlag = false;					//�p�X�t���O�̃��Z�b�g
		othello.resetGrids();				//�ՖʁE�^�[�����Z�b�g
		if(timer.isRunning()) timer.stop();	//�^�C�}�[�X�g�b�v
		resetTimer();						//�^�C�}�[���Z�b�g
		player.beOppose(false);				//�΋ǃ��[�h�I��
		player.beStand(false);				//�ϐ탂�[�h�I��
		//��ʂ̏�����
		logArea.setText("");				//���O�G���A���Z�b�g
		chatField.setText("");				//�`���b�g���͗����Z�b�g
		playerPanel.setVisible(true);		//����L����
		reactPanel.setVisible(true);		//���A�N�V�����p�l���L����
		logPanel.setVisible(true);			//���O�p�l���L����
		chatPanel.setVisible(true);			//�`���b�g�p�l���L����
		b11_1.setVisible(false);			//�p�X�{�^��������
	}
	//���͕�������`�F�b�N����֐�
	public boolean checkString(String str, String item, JLabel label){
		if(str.equals("")){						//�󗓂������ꍇ
			label.setText(item+"���󗓂ł�");
			return false;
		}
		else if(str.length()>=20){				//����������������ꍇ
			label.setText(item+"���������܂�");
			return false;
		}
		else if(str.contains(",")){				//�J���}���܂܂��ꍇ
			label.setText(item+"�ɃJ���}�͊܂߂��܂���");
			return false;
		}
		else{
			label.setText(" ");
			return true;
		}
	}
	//���ʉ��Đ�
	public void playSound(File sound) {
    	try {
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound.getAbsoluteFile());
        	Clip clip = AudioSystem.getClip();
        	clip.open(audioInputStream);
        	clip.start();
    	} catch(Exception e) {
        	System.out.println("���ʉ��Đ����ɃG���[���������܂����F"+e);
    	}
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
	public boolean updateTable(){
		boolean canPut = false;													//�u����t���O
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
				canPut = true;															//�u����t���O�𗧂Ă�
			}
			int x = (i%othello.getRow())*45;
			int y = (int) (i/othello.getRow())*45;
			buttonArray[i].setBounds(x, y, 45, 45);
			buttonArray[i].setActionCommand("Table,"+Integer.toString(i));
			if(!player.isStand()) buttonArray[i].addMouseListener(this);		//�ϐ탂�[�h�Ȃ�A�N�V�������X�i�[�͖���
			tablePanel.add(buttonArray[i]);
		}
		label11_2.setText("���F" + othello.getBlack());							//�΂̐����f
		label11_4.setText("���F" + othello.getWhite());							//�΂̐����f
		pane.revalidate();														//��ʍX�V
		pane.repaint();
		return canPut;
	}
	//���b�Z�[�W�_�C�A���O
	class MessageDialog extends JDialog implements ActionListener{
		public MessageDialog(JFrame mainframe, String message){
			super(mainframe,"���b�Z�[�W",ModalityType.APPLICATION_MODAL);//���[�_���ɐݒ�
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			ImagePanel panel = new ImagePanel(dialogImage);
			panel.setSize(300,180);
			panel.setBounds(0,0,300,150);
			JLabel label = new JLabel(message);
			label.setForeground(Color.WHITE);
			Font font = new Font("�q���M�m����W�U",Font.BOLD,18);
			label.setFont(font);
			FontMetrics fm = label.getFontMetrics(font);
			label.setBounds(150-fm.stringWidth(message)/2,40,300,fm.getHeight());
			ImageButton button = new ImageButton("OK",buttonIcon2,16,false);
			button.setBounds(150-45,90,90,40);
			button.addActionListener(this);
			panel.setLayout(null);
			panel.add(label);
			panel.add(button);
			this.setLayout(null);
			this.add(panel);
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	//�_�C�A���O�̕\��
	public void showDialog(String message){
		MessageDialog dialog = new MessageDialog(this,message);
		dialog.setBounds(getBounds().x+WIDTH/2-150,getBounds().y+HEIGHT/2-75,300+24,180+16);
		dialog.setVisible(true);
	}
	//�w�i���`��ł���g���p�l��
	public class ImagePanel extends JPanel {
		private BufferedImage image;
		public ImagePanel(File image) {
			try {
            	this.image = ImageIO.read(image);
        	}
        	catch (IOException e) {
            	this.image = null;
        	}
    	}
    	public void paintComponent(Graphics g) {
        	Graphics2D g2D = (Graphics2D)g;
        	double imageWidth = image.getWidth();
        	double imageHeight = image.getHeight();
        	double panelWidth = this.getWidth();
        	double panelHeight = this.getHeight();
        	double sx = (panelWidth / imageWidth);
        	double sy = (panelHeight / imageHeight);
        	AffineTransform af = AffineTransform.getScaleInstance(sx, sy);
        	g2D.drawImage(image, af, this);
    	}
    }
    //�摜��\���ł���g���{�^��
    public class ImageButton extends JButton {
    	private BufferedImage image;	//�{�^���̔w�i�摜
    	private String label;			//�\�����镶����(�\���������Ȃ�������""��n��)
    	private int size;				//�����̑傫��
    	boolean isMenu;					//���j���[�{�^���Ȃ�true
    	public ImageButton(String label, File image, int size, boolean isMenu) {
    		this.label = label;
    		this.size = size;
    		this.isMenu = isMenu;
        	try {
            	this.image = ImageIO.read(image);
        	}
        	catch (IOException e) {
            	this.image = null;
        	}
    	}
    	public void paintComponent(Graphics g) {
        	Graphics2D g2D = (Graphics2D)g;
        	double imageWidth = image.getWidth();
        	double imageHeight = image.getHeight();
        	double panelWidth = this.getWidth();
        	double panelHeight = this.getHeight();
        	double sx = (panelWidth / imageWidth);
        	double sy = (panelHeight / imageHeight);
        	AffineTransform af = AffineTransform.getScaleInstance(sx, sy);
        	g2D.drawImage(image, af, this);									//�A�X�y�N�g��𒲐����ĉ摜�`��
        	setForeground(Color.WHITE);										//�����F�w��
        	Font font = new Font(Font.SANS_SERIF,Font.BOLD,size);			//�t�H���g�w��
        	g.setFont(font);
        	if(isMenu){
        		g.drawString(label,(int)(getWidth()*0.35),(int)(getHeight()*0.6));
        	}
        	else{
        		FontMetrics fm = g.getFontMetrics(font);
				g.drawString(label,(int)(getWidth()*0.5-fm.stringWidth(label)*0.5),(int)(getHeight()*0.5+fm.getHeight()*0.3));
        	}
    	}
    }
	// ���C�� ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String args[]){
		int port = 10000;
		Client client = new Client();				//�N���C�A���g����
		client.setVisible(true);					//��ʕ\��
		client.connectServer("localhost", port);	//�T�[�o�ɐڑ�
	}
}