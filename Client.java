// パッケージのインポート
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


// クラス定義
public class Client extends JFrame implements MouseListener, ActionListener{
	// 共通して用いるフィールド //////////////////////////////////////////////////////////////////////////////////////////////////
	//クラス関連
	private Player player;							//プレイヤ
	private Othello othello;						//オセロ
	private javax.swing.Timer timer;				//タイマー
	//表示関連
	final private int WIDTH = 630, HEIGHT = 600;	//フレームの大きさ
	final private int FIELD_W = 200, FIELD_H = 30;	//テキストフィールドが収まる縦幅
	final private int TABLE_W = 370, TABLE_H = 370;	//盤面の大きさ
	final private String[] CLM = {"A","B","C","D","E","F","G","H"};//盤面の横軸
	final private String[] ROW = {"1","2","3","4","5","6","7","8"};//盤面の縦軸
	final private String RULE = "遊び方\n\n＊オセロのルール\n・黒が先攻、白が後攻です。\n・相手の石を挟める場所でないと打つことができません。\n・相手の石を挟むと相手の石がひっくり返り自分の石になります。\n・挟む方向は縦、横、斜めのいずれでも可能です。\n・どこにもおける場所がない場合パスになります。\n・お互いどこにも置ける場所が無くなった時に石の数が多いほうが勝ちとなります。\n\n＊メニューについて\n・対局する\n→通常対局とプライベート対局を選べます。\n→→通常対局はオンラインでランダムにマッチングした相手と対局します。\n→→プライベート対局ではパスワード付きの対局部屋を作成または入室して対局します。\n　　対局時間やチャット機能の有無について設定することができます。\n・観戦する\n→対局中の部屋のリストから好きな対局を選択し、他プレイヤーの対局を観戦することが\n　できます。\n→→対局に対してリアクションを送信することができます。\n・記録を見る\n→これまでの戦績の全記録やプレイヤーごとの対局記録を閲覧することができます。\n\n＊対局中について\n・盤面をクリックすることで石を置きます。置けない場所をクリックしても何も起きません。\n・石を置ける場所には赤い表示がされます。これは設定で消すことができます。\n・1分以内に石を置かないとパスになります。プライベート対局では時間を変更できます。\n・相手とチャットをすることができます。閲覧したくない場合はログ画面を閉じることが\n　できます。\n・投了すると負けとなり、対局を終了します。";//遊び方
	final private String TITLE[] = {				//タイトル一覧
		"ようこそ","ログイン","新規登録","メニュー",
		"対局","マッチング中","対局","部屋の設定","マッチング中","部屋を選択","パスワード入力","対局",
		"部屋を選択","観戦",
		"記録を選択","総合記録","相手別記録","相手別記録",
		"遊び方"
	};
	private Container pane;							//コンテナ
	private ImagePanel[] panel = new ImagePanel[19];//画面パネル
	private JPanel listPanel9,listPanel12;			//鍵部屋リスト表示用パネル、観戦部屋リスト表示用パネル
	private JLabel label1, label2, label7_5, label10_2, label16_2;	//入力情報照合結果表示ラベル
	private JLabel label15_1, label15_2, label15_3, label15_4;//総合戦績表示ラベル
	private JLabel label17_1, label17_2, label17_3, label17_4;//対人別戦績表示ラベル
	private JTextField field1, field2, passfield7, field16;		//テキスト入力フィールド
	private JPasswordField passfield1, passfield2, passfield10;	//パスワード入力フィールド
	private JRadioButton rb71,rb72;					//ラジオボタン
	private JComboBox<String> box7;					//コンボボックス
	private JPanel tablePanel,playerPanel,reactPanel,logPanel,chatPanel;//盤面配置用パネル、対局者操作パネル、観戦者操作パネル、ログエリア表示パネル、チャット表示パネル
	private ImageButton b11_1;							//パスボタン
	private JTextArea logArea;						//ログエリア
	private JTextField chatField;					//チャットフィールド
	private JLabel label11_1,label11_2,label11_4,label11_5,label11_6,label11_7,label11_8;//対局情報表示ラベル
	//素材
	final private String SRC_IMG = ".\\img\\";		//画像へのパス
	final private String SRC_SND = ".\\sounds\\";	//音へのパス
	File gameIcon, standIcon, recordIcon, ruleIcon, logoutIcon;		//メニューボタン用アイコン
	File buttonIcon1, buttonIcon2;									//その他ボタン用アイコン
	ImageIcon whiteIcon, blackIcon, boardIcon, canPutIcon;			//盤面ボタン用アイコン
	File[] backImage = new File[19];								//フレーム背景画像
	File dialogImage;
	File SE_switch;													//音
	//処理関連
	private String Operation;												//実行中のオペレーション
	private int panelID;													//画面パネルID
	private boolean pathFlag;												//パスフラグ
	private int minutes, seconds;											//対局用経過時間
	private HashMap<String,String> dataID = new HashMap<String,String>(20);	//コマンド・データ対応表
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
	// 通信関連
	private DataOutputStream out;					//送信用ストリーム
	private DataInputStream in;						//受信用ストリーム
	private Receiver receiver;						//受信スレッド
	// コンストラクタ/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Client() {
		//オブジェクト生成
		player = new Player();						//プレイヤ生成
		othello = new Othello();					//オセロ生成
		timer = new javax.swing.Timer(1000,this);	//タイマー生成
		//画像読み込み
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
		//音読み込み
//		SE_switch = new File(SRC_SND+"keyboard1.wav");
		//フレーム設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH+24, HEIGHT+48);
		setResizable(false);
		//ペイン設定
		pane = getContentPane();
		pane.setLayout(null);
		/* パネルデザイン *****************************************************************/
		//タイトル画面
		ImageButton b01 = new ImageButton("ログイン",buttonIcon1,36,false);
		b01.setBounds(WIDTH/2-230, 470, 200, 70);
		b01.setActionCommand("Switch,1");
		b01.addMouseListener(this);
		ImageButton b02 = new ImageButton("新規登録",buttonIcon1,36,false);
		b02.setBounds(WIDTH/2+30, 470, 200, 70);
		b02.setActionCommand("Switch,2");
		b02.addMouseListener(this);
		panel[0] = new ImagePanel(backImage[0]);
		panel[0].setSize(WIDTH,HEIGHT);
		panel[0].setLayout(null);
		panel[0].add(b01);
		panel[0].add(b02);
		//ログイン画面
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
		ImageButton b11 = new ImageButton("戻る",buttonIcon1,24,false);
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
		//新規登録画面
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
		ImageButton b21 = new ImageButton("戻る",buttonIcon1,24,false);
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
		//メニュー画面
		ImageButton b31 = new ImageButton("対局する",gameIcon,36,true);
		b31.setBounds(WIDTH/5, HEIGHT/28*2, WIDTH/5*3, HEIGHT/7);
		b31.setActionCommand("Switch,4");
		b31.addMouseListener(this);
		ImageButton b32 = new ImageButton("観戦する",standIcon,36,true);
		b32.setBounds(WIDTH/5, HEIGHT/28*7, WIDTH/5*3, HEIGHT/7);
		b32.setActionCommand("WatchroomList,-1");
		b32.addMouseListener(this);
		ImageButton b33 = new ImageButton("記録を見る",recordIcon,36,true);
		b33.setBounds(WIDTH/5, HEIGHT/28*12, WIDTH/5*3, HEIGHT/7);
		b33.setActionCommand("Switch,14");
		b33.addMouseListener(this);
		ImageButton b34 = new ImageButton("遊び方",ruleIcon,36,true);
		b34.setBounds(WIDTH/5, HEIGHT/28*17, WIDTH/5*3, HEIGHT/7);
		b34.setActionCommand("Switch,18");
		b34.addMouseListener(this);
		ImageButton b35 = new ImageButton("ログアウト",logoutIcon,36,true);
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
		//ランダム・プライベート選択画面
		ImageButton b41 = new ImageButton("ランダムマッチ",buttonIcon2,28,false);
		b41.setBounds(WIDTH/2-280,370,270,80);
		b41.setActionCommand("RandomMatch,-1");
		b41.addMouseListener(this);
		ImageButton b42 = new ImageButton("プライベートマッチ",buttonIcon2,28,false);
		b42.setBounds(WIDTH/2+10,370,270,80);
		b42.setActionCommand("Switch,6");
		b42.addMouseListener(this);
		ImageButton b43 = new ImageButton("戻る",buttonIcon1,20,false);
		b43.setBounds(470,480,120,60);
		b43.setActionCommand("Switch,3");
		b43.addMouseListener(this);
		panel[4] = new ImagePanel(backImage[4]);
		panel[4].setSize(WIDTH,HEIGHT);
		panel[4].setLayout(null);
		panel[4].add(b41);
		panel[4].add(b42);
		panel[4].add(b43);
		//マッチング待機画面
		ImageButton b51 = new ImageButton("戻る",buttonIcon1,20,false);
		b51.setBounds(470,480,120,60);
		b51.setActionCommand("CancelMatch,-1");
		b51.addMouseListener(this);
		panel[5] = new ImagePanel(backImage[5]);
		panel[5].setSize(WIDTH,HEIGHT);
		panel[5].setLayout(null);
		panel[5].add(b51);
		//部屋の作成・入室選択画面
		ImageButton b61 = new ImageButton("部屋を作る",buttonIcon2,28,false);
		b61.setBounds(WIDTH/2-280,370,270,80);
		b61.setActionCommand("Switch,7");
		b61.addMouseListener(this);
		ImageButton b62 = new ImageButton("部屋に入る",buttonIcon2,28,false);
		b62.setBounds(WIDTH/2+10,370,270,80);
		b62.setActionCommand("KeyroomList,-1");
		b62.addMouseListener(this);
		ImageButton b63 = new ImageButton("戻る",buttonIcon1,20,false);
		b63.setBounds(470,480,120,60);
		b63.setActionCommand("Switch,4");
		b63.addMouseListener(this);
		panel[6] = new ImagePanel(backImage[6]);
		panel[6].setSize(WIDTH,HEIGHT);
		panel[6].setLayout(null);
		panel[6].add(b61);
		panel[6].add(b62);
		panel[6].add(b63);
		//部屋の設定画面
		JLabel label7_1 = new JLabel("チャット");
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
		JLabel label7_2 = new JLabel("制限時間");
		label7_2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_2.setForeground(Color.WHITE);
		String[] time = {"1","3","5"};
		box7 = new JComboBox<String>(time);
		JLabel label7_3 = new JLabel("分");
		label7_3.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_3.setForeground(Color.WHITE);
		JLabel label7_4 = new JLabel("パスワード");
		label7_4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_4.setForeground(Color.WHITE);
		passfield7 = new JTextField(20);
		passfield7.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		passfield7.setSize(300,40);
		label7_5 = new JLabel(" ");
		label7_5.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		label7_5.setForeground(Color.WHITE);
		label7_5.setBounds(100,430,WIDTH,50);
		ImageButton b71 = new ImageButton("決定",buttonIcon1,20,false);
		b71.setBounds(320,480,120,60);
		b71.setActionCommand("MakeKeyroom,-1");
		b71.addMouseListener(this);
		ImageButton b72 = new ImageButton("戻る",buttonIcon1,20,false);
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
		//相手の入室待機画面
		ImageButton b81 = new ImageButton("戻る",buttonIcon1,20,false);
		b81.setBounds(470,480,120,60);
		b81.setActionCommand("DeleteKeyroom,-1");
		b81.addMouseListener(this);
		panel[8] = new ImagePanel(backImage[8]);
		panel[8].setSize(WIDTH,HEIGHT);
		panel[8].setLayout(null);
		panel[8].add(b81);
		//鍵部屋選択画面
		listPanel9 = new JPanel();
		listPanel9.setMinimumSize(new Dimension(WIDTH-200,HEIGHT-200));
		listPanel9.setLayout(new BoxLayout(listPanel9,BoxLayout.Y_AXIS));
		ImageButton b91 = new ImageButton("戻る",buttonIcon1,20,false);
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
		//入室パスワード入力画面
		JLabel label10_1 = new JLabel("パスワード");
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
		ImageButton b102 = new ImageButton("戻る",buttonIcon1,20,false);
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
		//対局画面
		tablePanel = new JPanel();//盤面表示パネル
		tablePanel.setBounds(25,25,TABLE_W,TABLE_H);
		tablePanel.setOpaque(false);
		tablePanel.setLayout(null);
		JLabel columnNumber = new JLabel(CLM[0]+"　　"+CLM[1]+"　　"+CLM[2]+"　　"+CLM[3]+"　　"+CLM[4]+"　　"+CLM[5]+"　　"+CLM[6]+"　　"+CLM[7]);//列番号の表示
		columnNumber.setBounds(35, 0, TABLE_W, FIELD_H);
		columnNumber.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		columnNumber.setForeground(Color.WHITE);
		JLabel rowNumber = new JLabel("<html>"+ROW[0]+"<br/><br/>"+ROW[1]+"<br/><br/>"+ROW[2]+"<br/><br/>"+ROW[3]+"<br/><br/>"+ROW[4]+"<br/><br/>"+ROW[5]+"<br/><br/>"+ROW[6]+"<br/><br/>"+ROW[7]+"</html>");//行番号の表示
		rowNumber.setBounds(7, 15, FIELD_W, TABLE_H);
		rowNumber.setFont(new Font(Font.SANS_SERIF,Font.BOLD,17));
		rowNumber.setForeground(Color.WHITE);
		JPanel infoPanel = new JPanel();//対局情報表示パネル
		infoPanel.setBounds(TABLE_W+20,10,200,160);
		infoPanel.setOpaque(false);
		label11_1 = new JLabel("          ");
		label11_1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_1.setForeground(Color.WHITE);
		label11_2 = new JLabel("黒：２");
		label11_2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_2.setForeground(Color.WHITE);
		JLabel label11_3 = new JLabel("vs");
		label11_3.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_3.setForeground(Color.WHITE);
		label11_4 = new JLabel("白：２");
		label11_4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_4.setForeground(Color.WHITE);
		label11_5 = new JLabel("          ");
		label11_5.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_5.setForeground(Color.WHITE);
		label11_6 = new JLabel("制限時間：0分0秒");
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
		playerPanel = new JPanel();//対局者操作パネル
		playerPanel.setBounds(TABLE_W+20,170,200,100);
		playerPanel.setOpaque(false);
		b11_1 = new ImageButton("パス",buttonIcon2,16,false);
		b11_1.setBounds(10,10,90,40);
		b11_1.setActionCommand("Pass,-1");
		b11_1.addMouseListener(this);
		b11_1.setVisible(false);
		ImageButton b11_2 = new ImageButton("投了",buttonIcon2,16,false);
		b11_2.setBounds(100,10,90,40);
		b11_2.setActionCommand("Giveup,-1");
		b11_2.addMouseListener(this);
		ImageButton b11_3 = new ImageButton("アシスト",buttonIcon2,16,false);
		b11_3.setBounds(10,50,90,40);
		b11_3.setActionCommand("Assist,-1");
		b11_3.addMouseListener(this);
		label11_7 = new JLabel("：ON");
		label11_7.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_7.setForeground(Color.WHITE);
		label11_7.setBounds(100,50,90,40);
		playerPanel.setLayout(null);
		playerPanel.add(b11_1);
		playerPanel.add(b11_2);
		playerPanel.add(b11_3);
		playerPanel.add(label11_7);
		reactPanel = new JPanel();//観戦者操作パネル
		reactPanel.setBounds(TABLE_W+20,280,200,100);
		reactPanel.setOpaque(false);
		ImageButton b11_a = new ImageButton("いいね",buttonIcon2,16,false);
		b11_a.setSize(100,40);
		b11_a.setActionCommand("Reaction,さんがいいねしました");
		b11_a.addMouseListener(this);
		ImageButton b11_b = new ImageButton("拍手",buttonIcon2,16,false);
		b11_b.setSize(100,40);
		b11_b.setActionCommand("Reaction,さんが拍手を送りました");
		b11_b.addMouseListener(this);
		ImageButton b11_c = new ImageButton("？？",buttonIcon2,16,false);
		b11_c.setSize(100,40);
		b11_c.setActionCommand("Reaction,：？？");
		b11_c.addMouseListener(this);
		ImageButton b11_d = new ImageButton("！！",buttonIcon2,16,false);
		b11_d.setSize(100,40);
		b11_d.setActionCommand("Reaction,：！！");
		b11_d.addMouseListener(this);
		ImageButton b11_e = new ImageButton("退出",buttonIcon2,16,false);
		b11_e.setSize(100,40);
		b11_e.setActionCommand("GetoutWatchroom,-1");
		b11_e.addMouseListener(this);
		reactPanel.setLayout(new GridLayout(3,2));
		reactPanel.add(b11_a);
		reactPanel.add(b11_b);
		reactPanel.add(b11_c);
		reactPanel.add(b11_d);
		reactPanel.add(b11_e);
		logPanel = new JPanel();//ログ表示パネル
		logPanel.setBounds(20,TABLE_H+20,TABLE_W,170);
		logPanel.setOpaque(false);
		logArea = new JTextArea(8,34);
		logArea.setFont(new Font(Font.SANS_SERIF,Font.BOLD,10));
		logArea.setEditable(false);
		JScrollPane sp = new JScrollPane(logArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logPanel.setLayout(new FlowLayout());
		logPanel.add(sp);
		chatPanel = new JPanel();//チャット表示パネル
		chatPanel.setBounds(TABLE_W+20,390,200,150);
		chatPanel.setOpaque(false);
		ImageButton b11_4 = new ImageButton("ログ表示",buttonIcon2,16,false);
		b11_4.setBounds(10,10,90,40);
		b11_4.setActionCommand("Showlog,-1");
		b11_4.addMouseListener(this);
		label11_8 = new JLabel("：ON");
		label11_8.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
		label11_8.setForeground(Color.WHITE);
		label11_8.setBounds(100,10,90,40);
		chatField = new JTextField(17);
		ImageButton b11_5 = new ImageButton("送信",buttonIcon2,16,false);
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
		panel[11] = new ImagePanel(backImage[11]);//追加
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
		//観戦部屋選択画面
		listPanel12 = new JPanel();
		listPanel12.setMinimumSize(new Dimension(WIDTH-200,HEIGHT-200));
		listPanel12.setLayout(new BoxLayout(listPanel12,BoxLayout.Y_AXIS));
		ImageButton b121 = new ImageButton("戻る",buttonIcon1,20,false);
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
		//記録選択画面
		ImageButton b141 = new ImageButton("総合戦績",buttonIcon2,28,false);
		b141.setBounds(WIDTH/2-280,370,270,80);
		b141.setActionCommand("TotalRecord,-1");
		b141.addMouseListener(this);
		ImageButton b142 = new ImageButton("相手別戦績",buttonIcon2,28,false);
		b142.setBounds(WIDTH/2+10,370,270,80);
		b142.setActionCommand("Switch,16");
		b142.addMouseListener(this);
		ImageButton b143 = new ImageButton("戻る",buttonIcon1,20,false);
		b143.setBounds(470,480,120,60);
		b143.setActionCommand("Switch,3");
		b143.addMouseListener(this);
		panel[14] = new ImagePanel(backImage[14]);
		panel[14].setSize(WIDTH,HEIGHT);
		panel[14].setLayout(null);
		panel[14].add(b141);
		panel[14].add(b142);
		panel[14].add(b143);
		//総合成績画面
		label15_1 = new JLabel("勝ち数：");
		label15_1.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
		label15_1.setForeground(Color.WHITE);
		label15_1.setBounds(50,200,250,100);
		label15_2 = new JLabel("負け数：");
		label15_2.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
		label15_2.setForeground(Color.WHITE);
		label15_2.setBounds(360,200,250,100);
		label15_3 = new JLabel("引分数：");
		label15_3.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
		label15_3.setForeground(Color.WHITE);
		label15_3.setBounds(50,350,250,100);
		label15_4 = new JLabel("投了数：：");
		label15_4.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
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
		//相手名入力画面
		JLabel label16_1 = new JLabel("相手名：");
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
		ImageButton b162 = new ImageButton("戻る",buttonIcon1,20,false);
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
		//相手別戦績画面
		label17_1 = new JLabel("勝ち数：");
		label17_1.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
		label17_1.setForeground(Color.WHITE);
		label17_1.setBounds(50,200,250,100);
		label17_2 = new JLabel("負け数：");
		label17_2.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
		label17_2.setForeground(Color.WHITE);
		label17_2.setBounds(360,200,250,100);
		label17_3 = new JLabel("引分数：");
		label17_3.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
		label17_3.setForeground(Color.WHITE);
		label17_3.setBounds(50,350,250,100);
		label17_4 = new JLabel("投了数：：");
		label17_4.setFont(new Font("ヒラギノ明朝W６",Font.BOLD,48));
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
		//遊び方表示画面
		JTextArea ruleArea = new JTextArea(30,100);
		ruleArea.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		ruleArea.setBackground(new Color(0.1f,0.1f,0.1f));
		ruleArea.setForeground(Color.WHITE);
		ruleArea.setText(RULE);
		ruleArea.setEditable(false);
		JScrollPane sp18 = new JScrollPane(ruleArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp18.setBounds(50,50,WIDTH-100,400);
		ImageButton b181 = new ImageButton("戻る",buttonIcon1,20,false);
		b181.setBounds(470,480,120,60);
		b181.setActionCommand("Switch,3");
		b181.addMouseListener(this);
		panel[18] = new ImagePanel(backImage[18]);
		panel[18].setSize(WIDTH,HEIGHT);
		panel[18].setLayout(null);
		panel[18].add(sp18);
		panel[18].add(b181);
		/**********************************************************************************/
		//初期画面描画
		panelID = 0;
		switchDisplay();
		resetRoom();
	}
	// 通信 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//サーバに接続
	public void connectServer(String ipAddress, int port){
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port);					//サーバに接続要求を送信
			System.out.println("サーバと接続しました");
			out = new DataOutputStream(socket.getOutputStream());	//出力ストリームを作成
			receiver = new Receiver(socket);						//受信スレッドを作成
			receiver.start();
		}
		catch (UnknownHostException e) {
			System.err.println("ホストのIPアドレスが判定できません: " + e);
			System.exit(-1);
		}
		catch (IOException e) {
			System.err.println("サーバ接続時にエラーが発生しました: " + e);
			System.exit(-1);
		}
	}
	//データの送信
	public void sendMessage(String msg){
		try{
			out.writeUTF(msg);					//バッファに書き込み
			System.out.println("送信：" +msg);	//テスト出力
		}
		catch (IOException e){
			System.err.println("データ送信時にエラーが発生しました: " + e);
		}
	}
	//データの受信
	class Receiver extends Thread {
		Receiver (Socket socket){
			try{
				in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); //入力ストリームを作成
			}
			catch (IOException e) {
				System.err.println("サーバ接続時にエラーが発生しました: " + e);
				System.exit(-1);
			}
		}
		public void run(){
			try{
				while(true) {
					String inputLine = in.readUTF();			//バッファを読み込み
					if (inputLine != null){						//データ受信したら
						System.out.println(player.isOppose());
						System.out.println("受信：" +inputLine);//テスト出力
						try{
							receiveMessage(inputLine);				//処理
						}
						catch (ArrayIndexOutOfBoundsException e){
							System.out.println("受信データ処理中にエラーが発生しました: " + e);
							panelID = 3;
							switchDisplay();
							resetRoom();
						}
					}
				}
			}
			catch (IOException e){
				System.err.println("データ受信時にエラーが発生しました: " + e);
				Client.this.connectServer("localhost",10000);
				panelID = 0;
				switchDisplay();
				resetRoom();
			}
		}
	}
	//受信データの判別・処理
	public void receiveMessage(String msg) throws ArrayIndexOutOfBoundsException {
		String[] c = Operation.split(",",2);							//オペレーションをコマンドと付属情報に分解
		String command = c[0];	//コマンド
		String subc = c[1];		//付属情報
		//対局情報以外
		if(!player.isOppose()) {
			//ログイン結果
			if(command.equals("Login")){
				if(msg.equals("true")){																//ログイン成功
					showDialog("ログインしました");														//ダイアログの表示
					panelID = 3;																		//メニュー画面へ
					switchDisplay();																	//画面遷移
				}
				else{																				//ログイン失敗
					if(msg.equals("name")) label1.setText("そのようなプレイヤ名は存在しません");		//メッセージ表示
					if(msg.equals("pass")) label1.setText("パスワードが違います");						//メッセージ表示
					if(msg.equals("login")) label1.setText("そのユーザは既にログインされています");
					player.setName(null);																//プレイヤ名リセット
					player.setPass(null);																//パスワードリセット
				}
			}
			//登録結果
			else if(command.equals("Register")){
				if(msg.equals("true")){										//登録許可
					showDialog("新規登録しました");								//ダイアログの表示
					Operation = "Login,-1";										//ログインオペレーション実行
					sendMessage(dataID.get("Login")+","+player.getName()+","+player.getPass());
				}
				else{														//登録失敗
					label2.setText("そのプレイヤ名は既に使われています");		//メッセージ表示
					player.setName(null);										//プレイヤ名リセット
					player.setPass(null);										//パスワードリセット
				}
			}
			//ランダムマッチング結果
			else if(command.equals("RandomMatch")){
				String[] info = msg.split(",",3);
				if(info[0].equals("true")){									//マッチング成功
					if(info[2].equals("0")){									//先手だった場合
						player.setColor("black");									//手番保存
						othello.setBlackName(player.getName());						//先手名保存
						label11_1.setText(player.getName());						//先手名表示
						othello.setWhiteName(info[1]);								//後手名保存
						label11_5.setText(info[1]);									//後手名表示
					}
					if(info[2].equals("1")){									//後手だった場合
						player.setColor("white");									//手番保存
						othello.setBlackName(info[1]);								//先手名保存
						label11_1.setText(info[1]);									//先手名表示
						othello.setWhiteName(player.getName());						//後手名保存
						label11_5.setText(player.getName());						//後手名表示
						playerPanel.setVisible(false);								//操作無効化
					}
					player.beOppose(true);										//対局モードへ
					reactPanel.setVisible(false);								//リアクションパネル無効化
					player.setChat(true);										//チャットの有無保存
					player.setTime(1);											//制限時間保存
					updateTable();												//盤面表示
					resetTimer();												//タイマーリセット
					timer.start();												//タイマースタート
					panelID = 11;												//対局画面へ
				}
				if(info[0].equals("false")){								//マッチング失敗
					showDialog("マッチングに失敗しました");						//ダイアログの表示
					panelID = 4;												//ランダム・プライベート選択画面へ
				}
				switchDisplay();											//画面遷移
			}
			//鍵部屋マッチング結果
			else if(command.equals("MakeKeyroom")){
				String[] info = msg.split(",",2);
				player.beOppose(true);										//対局モードへ
				player.setColor("black");									//手番保存
				othello.setBlackName(player.getName());						//先手名保存
				label11_1.setText(player.getName());						//先手名表示
				othello.setWhiteName(info[1]);								//後手名保存
				label11_5.setText(info[1]);									//後手名表示
				updateTable();												//盤面表示
				reactPanel.setVisible(false);								//リアクションパネル無効化
				if(!player.getChat()) chatPanel.setVisible(false);			//チャット無ならチャットパネル無効化
				resetTimer();												//タイマーリセット
				timer.start();												//タイマースタート
				panelID = 11;												//対局画面へ
				switchDisplay();											//画面遷移
			}
			//鍵部屋リスト
			else if(command.equals("KeyroomList")){
				if(msg.equals("no")){										//リストが無かったら
					showDialog("現在、入室できる部屋がありません");				//ダイアログ表示
					panelID = 6;												//部屋の作成・入室選択画面へ
					switchDisplay();											//画面遷移
				}
				else{														//リストがあれば
					String[] keyroom = msg.split(",",0);
					String[] info;	//部屋情報
					String strchat;	//チャットの有無
					listPanel9.removeAll();										//選択肢ボタンリセット
					for(int i=0;i<keyroom.length;i++){							//選択肢ボタン生成
						info = keyroom[i].split(Pattern.quote("."),4);
						if(info[2].equals("true")) strchat = "あり";
						else strchat = "なし";
						JButton bi = new JButton("<html>作成者："+info[1]+"<br/>チャット："+strchat+" 制限時間："+info[3]+"分</html>");
						bi.setActionCommand("SelectKeyroom,"+keyroom[i]);
						bi.addMouseListener(this);
						listPanel9.add(bi);
					}
					panelID = 9;												//鍵部屋リスト表示画面へ
					switchDisplay();											//画面遷移
				}
			}
			//鍵部屋への入室
			else if(command.equals("EnterKeyroom")){
				String[] info = msg.split(",",2);
				if(info[0].equals("true")){									//入室許可
					player.beOppose(true);										//対局モードへ
					player.setColor("white");									//手番保存
					othello.setBlackName(info[1]);								//先手名保存
					label11_1.setText(info[1]);									//先手名表示
					othello.setWhiteName(player.getName());						//後手名保存
					label11_5.setText(player.getName());						//後手名表示
					updateTable();												//盤面表示
					reactPanel.setVisible(false);								//リアクションパネル無効化
					if(!player.getChat()) chatPanel.setVisible(false);			//チャット無ならチャットパネル無効化
					playerPanel.setVisible(false);								//操作無効化
					resetTimer();												//タイマーリセット
					timer.start();												//タイマースタート
					panelID = 11;												//対局画面へ
				}
				if(info[0].equals("false")){								//入室失敗
					resetRoom();												//部屋情報リセット
					showDialog("入室に失敗しました");							//ダイアログの表示
					panelID = 6;												//メニュー画面へ
				}
				switchDisplay();											//画面遷移
			}
			//観戦部屋リスト
			else if(command.equals("WatchroomList")){
				if(msg.equals("no")){										//リストが無かったら
					showDialog("現在、観戦できる部屋がありません");				//ダイアログ表示
					panelID = 3;												//メニュー画面へ
					switchDisplay();											//画面遷移
				}
				else{														//リストがあったら
					String[] room = msg.split(",",0);
					String[] info;//部屋情報
					listPanel12.removeAll();									//選択肢ボタンリセット
					for(int i=0;i<room.length;i++){								//選択肢ボタン生成
						info = room[i].split(Pattern.quote("."),5);
						JButton bi = new JButton("<html>先手："+info[1]+" 後手："+info[2]+"<br/>黒石："+info[3]+" 白石："+info[4]+"</html>");
						bi.setActionCommand("EnterWatchroom,"+room[i]);
						bi.addMouseListener(this);
						listPanel12.add(bi);
					}
					panelID = 12;												//観戦部屋リスト表示画面へ
					switchDisplay();											//画面遷移
				}
			}
			//観戦部屋への入室
			else if(command.equals("EnterWatchroom")){
				if(msg.equals("false")){									//入室失敗
					resetRoom();												//部屋情報リセット
					showDialog("入室に失敗しました");							//ダイアログの表示
					panelID = 3;												//メニュー画面へ
				}
				else{														//入室許可
					player.beOppose(true);										//対局モードへ
					player.beStand(true);										//観戦モードへ
					player.setAssist(false);									//アシスト無効化
					playerPanel.setVisible(false);								//操作無効化
					chatPanel.setVisible(false);								//チャットパネル無効化
					player.setColor("black");									//手番保存
					label11_1.setText(othello.getBlackName());					//先手名表示
					label11_5.setText(othello.getWhiteName());					//後手名表示
					String[] grids = msg.split(",");
					for(int i=0;i<othello.getRow()*othello.getRow();i++){		//盤面変換
						if(grids[i].equals("0")) grids[i] = "board";//空
						if(grids[i].equals("1")) grids[i] = "black";//黒
						if(grids[i].equals("2")) grids[i] = "white";//白
					}
					othello.setGrids(grids);									//盤面反映
					updateTable();												//盤面更新
					sendMessage(dataID.get("Reaction")+","+player.getName()+"が入室しました");
					panelID = 11;												//観戦画面へ
				}
				switchDisplay();											//画面遷移
			}
			//総合記録
			else if(command.equals("TotalRecord")){
				String[] info = msg.split(",",4);
				label15_1.setText("勝ち数："+info[0]);						//記録表示
				label15_2.setText("負け数："+info[1]);
				label15_3.setText("引分数："+info[2]);
				label15_4.setText("投了数："+info[3]);
				panelID = 15;												//総合戦績画面へ
				switchDisplay();											//画面遷移
			}
			//対人別記録
			else if(command.equals("IdRecord")){
				String[] info = msg.split(",",4);
				label17_1.setText("勝ち数："+info[0]);						//記録表示
				label17_2.setText("負け数："+info[1]);
				label17_3.setText("引分数："+info[2]);
				label17_4.setText("投了数："+info[3]);
				panelID = 17;												//相手別戦績画面へ
				switchDisplay();											//画面遷移
			}
		}
		//対局(観戦)情報
		else if(player.isOppose()){
			String[] info = msg.split(",",0);							//以下、受信内容で場合分け
			//盤面・ログ受信
			if(info[0].equals(dataID.get("Table"))){
				String table = info[1];																//盤面取得
				String[] grids = table.split(Pattern.quote("."),othello.getRow()*othello.getRow()); //盤面変換
				for(int i=0;i<othello.getRow()*othello.getRow();i++){
					if(grids[i].equals("0")) grids[i] = "board";//空
					if(grids[i].equals("1")) grids[i] = "black";//黒
					if(grids[i].equals("2")) grids[i] = "white";//白
				}
				othello.setGrids(grids);															//盤面更新
				pathFlag = false;																	//パスフラグリセット
				othello.changeTurn();																//ターン変更
				if(updateTable()) b11_1.setVisible(false);											//盤面反映
				else b11_1.setVisible(true);														//置ける場所が無ければパスボタンを有効化
				String log = info[2];																//ログ取得
				logArea.setEditable(true);															//ログ反映
				logArea.append("\n"+log);
				logArea.setEditable(false);
				playerPanel.setVisible(true);														//操作有効化
				resetTimer();																		//タイマーリセット
				timer.restart();																	//タイマーリスタート
				if(othello.isGameover()) {															//決着がついたら
					String result = othello.checkWinner();										//勝敗確認
					if(!player.isStand()){													//対局者の場合
						if(result.equals("draw")) {								//引き分け
							sendMessage(dataID.get("Finish")+",3");				//サーバに送信
							showDialog("引き分け");								//ダイアログ表示
						}
						if(result.equals(player.getColor())) {					//勝ち
							sendMessage(dataID.get("Finish")+",1");				//サーバに送信
							showDialog("あなたの勝ちです");						//ダイアログ表示
						}
						else{													//負け
							sendMessage(dataID.get("Finish")+",2");				//サーバに送信
							showDialog("あなたの負けです");						//ダイアログ表示
						}
					}
					else{																	//観戦者の場合
						sendMessage(dataID.get("GetoutWatchroom"));				//サーバに送信
						if(result.equals("draw")) showDialog("引き分け");		//ダイアログ表示
						if(result.equals("black")) showDialog(othello.getBlackName()+"の勝ちです");	//ダイアログ表示
						else showDialog(othello.getWhiteName()+"の勝ちです");						//ダイアログ表示
					}
					panelID = 3;																//メニュー画面へ
					switchDisplay();															//画面遷移
					resetRoom();																//部屋情報リセット
				}
			}
			//パス受信
			else if(info[0].equals(dataID.get("Pass"))){
				playerPanel.setVisible(true);														//操作有効化
				logArea.setEditable(true);															//ログに表示
				if(player.getColor().equals("black")) logArea.append("\n後手がパスしました");
				else logArea.append("\n先手がパスしました");
				logArea.setEditable(false);
				resetTimer();																		//タイマーリセット
				timer.restart();																	//タイマーリスタート
				othello.changeTurn();																//ターン変更
				if(pathFlag == true){																//直前に自分もパスしていたら試合終了
					String result = othello.checkWinner();						//勝敗確認
					if(result.equals("draw")) {									//引き分け
						sendMessage(dataID.get("Finish")+",3");					//サーバに送信
						showDialog("引き分け");									//ダイアログ表示
					}
					if(result.equals(player.getColor())) {						//勝ち
						sendMessage(dataID.get("Finish")+",1");					//サーバに送信
						showDialog("あなたの勝ちです");							//ダイアログ表示
					}
					else{														//負け
						sendMessage(dataID.get("Finish")+",2");					//サーバに送信
						showDialog("あなたの負けです");							//ダイアログ表示
					}
					panelID = 3;												//メニュー画面へ
					switchDisplay();											//画面遷移
					resetRoom();												//部屋情報リセット
				}
			}
			//投了受信
			else if(info[0].equals(dataID.get("Giveup"))){
				if(!player.isStand()) showDialog("対戦相手が投了しました");							//ダイアログ表示
				else showDialog("投了");															//ダイアログ表示
				sendMessage(dataID.get("Finish")+",1");												//サーバに送信
				panelID = 3;																		//メニュー画面へ
				switchDisplay();																	//画面遷移
				resetRoom();																		//部屋情報リセット
			}
			//チャット受信
			else if(info[0].equals(dataID.get("Chat"))){
				String chat = info[1];																//チャット保存
				logArea.setEditable(true);															//チャット反映
				logArea.append("\n"+chat);
				logArea.setEditable(false);
			}
			//リアクション受信
			else if(info[0].equals(dataID.get("Reaction"))){
				String reaction = info[1];															//リアクション保存
				logArea.setEditable(true);															//リアクション反映
				logArea.append("\n> "+reaction);
				logArea.setEditable(false);
			}
			//切断受信
			else if(info[0].equals(dataID.get("Disconnected"))){
				if(!player.isStand()) showDialog("対戦相手が切断しました");							//ダイアログ表示
				else showDialog("切断が行われました");												//ダイアログ表示
				sendMessage(dataID.get("Finish")+",1");												//サーバに送信
				panelID = 3;																		//メニュー画面へ
				switchDisplay();																	//画面遷移
				resetRoom();																		//部屋情報リセット
			}
		}
	}
	// 操作 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//マウスリスナー
	public void mouseClicked(MouseEvent e) {
		JButton button = (JButton)e.getComponent();			//ボタン特定
		Operation = button.getActionCommand();				//実行オペレーション特定
		acceptOperation(Operation);							//処理
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	//タイムリスナー
	public void actionPerformed(ActionEvent e) {
		seconds -= 1;													//一秒経過
		if(seconds < 0){												//60秒たったら
			seconds = 59;											//秒リセット
			minutes -= 1;											//一分経過
		}
		if(minutes < 0){												//制限時間が経過したら
			if(player.getColor()==othello.getTurn()){				//自分の番なら
				String[] grids = othello.getGrids();			//盤面取得
				for(int i=0;i<othello.getRow()*othello.getRow();i++){
					if(grids[i].equals("canPut")){				//置ける場所があるなら
						Operation = "Table,"+i;				//石を置くオペレーション実行
						acceptOperation(Operation);
						return;
					}
				}												//置ける場所がないなら
				Operation = "Pass,-1";						//パスオペレーション実行
				acceptOperation(Operation);
			}
		}
		else{															//まだ制限時間内なら
			label11_6.setText("制限時間："+minutes+"分"+seconds+"秒");	//時間表示
		}
	}
	//タイマーのリセット
	public void resetTimer(){
		minutes = player.getTime();								//分リセット
		seconds = 0;											//秒リセット
		label11_6.setText("制限時間：0分0秒");					//表示リセット
	}
	//操作の受付・処理
	public void acceptOperation(String operation){
		System.out.println("実行：" +operation);//テスト出力
		String[] c = operation.split(",",2);								//オペレーションをコマンドと付属情報に分解
		String command = c[0];	//コマンド
		String subc = c[1];		//付属情報
		switch(command){													//以下、コマンドで場合分け
			//画面遷移のみ
			case "Switch":
//				playSound(SE_switch);												//効果音再生
				panelID = Integer.parseInt(subc);
				switchDisplay();
				break;
			//ログイン要求
			case "Login":
				String logname = field1.getText();									//プレイヤ名読み取り
				String logpass = new String(passfield1.getPassword());				//パスワード読み取り
				if(checkString(logname,"プレイヤ名",label1)){
					if(checkString(logpass,"パスワード",label1)){
						player.setName(logname);									//プレイヤ名保存
						player.setPass(logpass);									//パスワード保存
						sendMessage(dataID.get(command)+","+logname+","+logpass);	//サーバへ送信
						break;
					}
				}
				break;
			//新規登録要求
			case "Register":
				String regname = field2.getText();									//プレイヤ名読み取り
				String regpass = new String(passfield2.getPassword());				//パスワード読み取り
				if(checkString(regname,"プレイヤ名",label2)){
					if(checkString(regpass,"パスワード",label2)){
						player.setName(regname);									//プレイヤ名保存
						player.setPass(regpass);									//パスワード保存
						sendMessage(dataID.get(command)+","+regname+","+regpass);	//サーバへ送信
						break;
					}
				}
				break;
			//ランダムマッチング要求
			case "RandomMatch":
				sendMessage(dataID.get(command));							//サーバへ送信
				panelID = 5;												//マッチング待機画面へ
				switchDisplay();											//画面遷移
				break;
			//マッチングキャンセル処理
			case "CancelMatch":
				sendMessage(dataID.get(command));							//サーバへ送信
				panelID = 4;												//ランダム・プライベート選択画面へ
				switchDisplay();											//画面遷移
				break;
			//石を置く
			case "Table":
				if(player.getColor()==othello.getTurn()){													//自分の番なら
					int grid = Integer.parseInt(subc);//置いたマス番号
					String log = "";//ログ
					StringBuffer table = new StringBuffer("");//盤面
					Boolean canPut = othello.putStone(grid,player.getColor(),true);						//石を置いてみる
					if(canPut){																			//置けるマスなら
						updateTable();															//盤面反映
						String[] grids = othello.getGrids();									//盤面情報取得
						for(int i=0;i<othello.getRow()*othello.getRow();i++){					//盤面情報変換
							if(grids[i].equals("board")) table.append("0.");
							if(grids[i].equals("canPut")) table.append("0.");
							if(grids[i].equals("black")) table.append("1.");
							if(grids[i].equals("white")) table.append("2.");
						}
						table.setLength(table.length()-1);
						if(player.getColor().equals("black")){									//ログ情報生成
							log = "先手：("+CLM[grid%8]+"-"+ROW[grid/8]+")";
						}
						if(player.getColor().equals("white")){
							log = "後手：("+CLM[grid%8]+"-"+ROW[grid/8]+")";
						}
						logArea.setEditable(true);												//ログ反映
						logArea.append("\n"+log);
						logArea.setEditable(false);
						playerPanel.setVisible(false);											//操作無効化
						resetTimer();															//タイマーリセット
						timer.restart();														//タイマーリスタート
						othello.changeTurn();													//ターン変更
						sendMessage(dataID.get(command)+","+table+","+log);						//サーバへ送信
						if(othello.isGameover()) {												//決着がついたら
							String result = othello.checkWinner();						//勝敗確認
							if(result.equals("draw")) {						//引き分け
								sendMessage(dataID.get("Finish")+",3");		//サーバに送信
								showDialog("引き分け");						//ダイアログ表示
							}
							if(result.equals(player.getColor())) {			//勝ち
								sendMessage(dataID.get("Finish")+",1");		//サーバに送信
								showDialog("あなたの勝ちです");				//ダイアログ表示
							}
							else{											//負け
								sendMessage(dataID.get("Finish")+",2");		//サーバに送信
								showDialog("あなたの負けです");				//ダイアログ表示
							}
							panelID = 3;												//メニュー画面へ
							switchDisplay();											//画面遷移
							resetRoom();												//部屋情報リセット
						}
					}
				}
				break;
			//パス
			case "Pass":
				playerPanel.setVisible(false);																//操作無効化
				pathFlag = true;																			//パスフラグを立てる
				logArea.setEditable(true);																	//ログに表示
				if(player.getColor().equals("black")) logArea.append("\n先手がパスしました");
				else logArea.append("\n後手がパスしました");
				logArea.setEditable(false);
				resetTimer();																				//タイマーリセット
				timer.restart();																			//タイマーリスタート
				othello.changeTurn();																		//手番変更
				sendMessage(dataID.get(command));															//サーバへ送信
				break;
			//投了
			case "Giveup":
				sendMessage(dataID.get(command));															//サーバへ送信
				showDialog("投了しました");																	//ダイアログ表示
				panelID = 3;																				//メニュー画面へ
				switchDisplay();																			//画面遷移
				resetRoom();																				//部屋情報リセット
				break;
			//チャット
			case "Chat":
				String str = player.getName()+": "+chatField.getText();										//チャット内容読み取り
				logArea.setEditable(true);																	//チャット反映
				logArea.append("\n"+str);
				logArea.setEditable(false);
				chatField.setText("");																		//チャット入力欄リセット
				sendMessage(dataID.get(command)+","+str);													//サーバへ送信
				break;
			//アシスト切替
			case "Assist":
				player.changeAssist();																		//アシスト切替
				if(player.getAssist()==true) label11_7.setText("：ON");										//表示変更
				if(player.getAssist()==false) label11_7.setText("：OFF");
				updateTable();																				//盤面反映
				break;
			//ログ表示切替
			case "Showlog":
				if(player.getShowlog()==true) {																//ONだったら
					player.setShowlog(false);														//OFFにする
					label11_8.setText("：OFF");														//表示変更
					logPanel.setVisible(false);														//ログを非表示に
				}
				else if(player.getShowlog()==false) {														//OFFだったら
					player.setShowlog(true);														//ONにする
					label11_8.setText("：ON");														//表示変更
					logPanel.setVisible(true);														//ログを表示
				}
				break;
			//鍵部屋作成要求
			case "MakeKeyroom":
				boolean chat = rb71.isSelected();							//チャットの有無読み取り
				int time = Integer.parseInt((String)box7.getSelectedItem());//制限時間読み取り
				String setpass = passfield7.getText();						//パスワード読み取り
				if(checkString(setpass,"パスワード",label7_5)){
					player.setChat(chat);									//チャットの有無保存
					player.setTime(time);									//制限時間保存
					sendMessage(dataID.get(command)+","+setpass+","+chat+","+time);//サーバへ送信
					panelID = 8;											//相手の入室待機画面へ
					switchDisplay();										//画面遷移
					break;
				}
				break;
			//鍵部屋削除要求
			case "DeleteKeyroom":
				player.setChat(true);										//チャットの有無リセット
				player.setTime(-1);											//制限時間リセット
				sendMessage(dataID.get(command));							//サーバへ送信
				panelID = 4;												//ランダム・プライベート選択画面へ
				switchDisplay();											//画面遷移
				break;
			//鍵部屋リスト要求
			case "KeyroomList":
				sendMessage(dataID.get(command));							//サーバへ送信
				break;
			//鍵部屋リスト選択
			case "SelectKeyroom":
				String[] keyinfo = subc.split(Pattern.quote("."),4);
				othello.setRoomID(Integer.parseInt(keyinfo[0]));			//部屋番号保存
				player.setChat(Boolean.parseBoolean(keyinfo[2]));			//チャットの有無保存
				player.setTime(Integer.parseInt(keyinfo[3]));				//制限時間保存
				panelID = 10;												//パスワード入力画面へ
				switchDisplay();											//画面遷移
				break;
			//鍵部屋への入室要求
			case "EnterKeyroom":
				String keypass = new String(passfield10.getPassword());						//パスワード読み取り
				if(checkString(keypass,"パスワード",label10_2)) {
					sendMessage(dataID.get(command)+","+othello.getRoomID()+","+keypass);	//サーバへ送信
					break;
				}
				break;
			//観戦部屋リスト要求
			case "WatchroomList":
				sendMessage(dataID.get(command));							//サーバへ送信
				break;
			//観戦部屋への入室要求
			case "EnterWatchroom":
				String[] roominfo = subc.split(Pattern.quote("."),5);
				othello.setRoomID(Integer.parseInt(roominfo[0]));			//部屋番号保存
				othello.setBlackName(roominfo[1]);							//先手名保存
				othello.setWhiteName(roominfo[2]);							//後手名保存
				sendMessage(dataID.get(command)+","+othello.getRoomID());	//サーバへ送信
				break;
			//リアクション
			case "Reaction":
				String reaction = player.getName()+subc;					//リアクション変換
				logArea.setEditable(true);									//リアクション反映
				logArea.append("\n> "+reaction);
				logArea.setEditable(false);
				sendMessage(dataID.get(command)+","+reaction);				//サーバへ送信
				break;
			//観戦途中退出
			case "GetoutWatchroom":
				sendMessage(dataID.get("Reaction")+","+player.getName()+"さんが退出しました");
				sendMessage(dataID.get(command));							//サーバへ送信
				panelID = 3;												//メニュー画面へ
				switchDisplay();											//画面遷移
				resetRoom();												//部屋情報リセット
				break;
			//総合記録要求
			case "TotalRecord":
				sendMessage(dataID.get(command));							//サーバへ送信
				break;
			//対人別記録要求
			case "IdRecord":
				String nameOpponent = field16.getText();
				if(checkString(nameOpponent,"相手の名前",label16_2)) {
					sendMessage(dataID.get(command)+","+nameOpponent);		//サーバへ送信
					break;
				}
				break;
			//ログアウト
			case "Logout":
				player.setName(null);										//プレイヤ名リセット
				player.setPass(null);										//パスワードリセット
				panelID = 0;												//タイトル画面へ
				switchDisplay();											//画面遷移
				break;
			default:
				break;
		}
	}
	//部屋情報のリセット(投了時・投了受信時・対局終了時・観戦途中退出時)
	public void resetRoom(){
		//処理の初期化
		othello.setRoomID(-1);				//部屋番号リセット
		player.setColor("-1");				//手番リセット
		othello.setBlackName(null);			//先手名リセット
		othello.setWhiteName(null);			//後手名リセット
		player.setChat(true);				//チャットの有無リセット
		player.setTime(-1);					//制限時間リセット
		player.setAssist(true);				//アシストの有無リセット
		player.setShowlog(true);			//ログ表示の有無のリセット
		pathFlag = false;					//パスフラグのリセット
		othello.resetGrids();				//盤面・ターンリセット
		if(timer.isRunning()) timer.stop();	//タイマーストップ
		resetTimer();						//タイマーリセット
		player.beOppose(false);				//対局モード終了
		player.beStand(false);				//観戦モード終了
		//画面の初期化
		logArea.setText("");				//ログエリアリセット
		chatField.setText("");				//チャット入力欄リセット
		playerPanel.setVisible(true);		//操作有効化
		reactPanel.setVisible(true);		//リアクションパネル有効化
		logPanel.setVisible(true);			//ログパネル有効化
		chatPanel.setVisible(true);			//チャットパネル有効化
		b11_1.setVisible(false);			//パスボタン無効化
	}
	//入力文字列をチェックする関数
	public boolean checkString(String str, String item, JLabel label){
		if(str.equals("")){						//空欄だった場合
			label.setText(item+"が空欄です");
			return false;
		}
		else if(str.length()>=20){				//文字数が長すぎる場合
			label.setText(item+"が長すぎます");
			return false;
		}
		else if(str.contains(",")){				//カンマが含まれる場合
			label.setText(item+"にカンマは含められません");
			return false;
		}
		else{
			label.setText(" ");
			return true;
		}
	}
	//効果音再生
	public void playSound(File sound) {
    	try {
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound.getAbsoluteFile());
        	Clip clip = AudioSystem.getClip();
        	clip.open(audioInputStream);
        	clip.start();
    	} catch(Exception e) {
        	System.out.println("効果音再生時にエラーが発生しました："+e);
    	}
	}
	// 表示 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//画面の遷移・描画
	public void switchDisplay(){
		pane.removeAll();								//パネル消去
		setTitle(TITLE[panelID]);						//タイトル変更
		pane.add(panel[panelID]);						//パネル設置
		System.out.println("画面" +panelID+ "へ移動");	//テスト出力
		pane.revalidate();								//データ反映
		pane.repaint();									//画面更新
		return;
	}
	//盤面更新(対局開始時・観戦開始時・石を置いた時・盤面受信時)
	public boolean updateTable(){
		boolean canPut = false;													//置けるフラグ
		tablePanel.removeAll();													//ボタンを全て消去
		othello.canPutGrids();													//置ける箇所を更新
		JButton[] buttonArray = new JButton[othello.getRow()*othello.getRow()];		//ボタンの配列を作成
		String[] grids = othello.getGrids();										//盤面取得
		for(int i=0;i<othello.getRow()*othello.getRow();i++){
			if(grids[i].equals("board")) buttonArray[i] = new JButton(boardIcon);	//空
			if(grids[i].equals("black")) buttonArray[i] = new JButton(blackIcon);	//黒
			if(grids[i].equals("white")) buttonArray[i] = new JButton(whiteIcon);	//白
			if(grids[i].equals("canPut")) {
				if(player.getAssist()==true) buttonArray[i] = new JButton(canPutIcon);	//置ける箇所
				if(player.getAssist()==false) buttonArray[i] = new JButton(boardIcon);	//空
				canPut = true;															//置けるフラグを立てる
			}
			int x = (i%othello.getRow())*45;
			int y = (int) (i/othello.getRow())*45;
			buttonArray[i].setBounds(x, y, 45, 45);
			buttonArray[i].setActionCommand("Table,"+Integer.toString(i));
			if(!player.isStand()) buttonArray[i].addMouseListener(this);		//観戦モードならアクションリスナーは無効
			tablePanel.add(buttonArray[i]);
		}
		label11_2.setText("黒：" + othello.getBlack());							//石の数反映
		label11_4.setText("白：" + othello.getWhite());							//石の数反映
		pane.revalidate();														//画面更新
		pane.repaint();
		return canPut;
	}
	//メッセージダイアログ
	class MessageDialog extends JDialog implements ActionListener{
		public MessageDialog(JFrame mainframe, String message){
			super(mainframe,"メッセージ",ModalityType.APPLICATION_MODAL);//モーダルに設定
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			ImagePanel panel = new ImagePanel(dialogImage);
			panel.setSize(300,180);
			panel.setBounds(0,0,300,150);
			JLabel label = new JLabel(message);
			label.setForeground(Color.WHITE);
			Font font = new Font("ヒラギノ明朝W６",Font.BOLD,18);
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
	//ダイアログの表示
	public void showDialog(String message){
		MessageDialog dialog = new MessageDialog(this,message);
		dialog.setBounds(getBounds().x+WIDTH/2-150,getBounds().y+HEIGHT/2-75,300+24,180+16);
		dialog.setVisible(true);
	}
	//背景が描画できる拡張パネル
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
    //画像を表示できる拡張ボタン
    public class ImageButton extends JButton {
    	private BufferedImage image;	//ボタンの背景画像
    	private String label;			//表示する文字列(表示したくなかったら""を渡す)
    	private int size;				//文字の大きさ
    	boolean isMenu;					//メニューボタンならtrue
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
        	g2D.drawImage(image, af, this);									//アスペクト比を調整して画像描画
        	setForeground(Color.WHITE);										//文字色指定
        	Font font = new Font(Font.SANS_SERIF,Font.BOLD,size);			//フォント指定
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
	// メイン ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String args[]){
		int port = 10000;
		Client client = new Client();				//クライアント生成
		client.setVisible(true);					//画面表示
		client.connectServer("localhost", port);	//サーバに接続
	}
}