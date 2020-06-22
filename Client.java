// パッケージのインポート
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;


// クラス定義
public class Client extends JFrame implements MouseListener {
	// 共通して用いるフィールド //////////////////////////////////////////////////////////////////////////////////////////////////
	//関連するクラス
	private Player player;							//プレイヤ
	private Othello othello;						//オセロ
	//表示関連
	final private int WIDTH = 400, HEIGHT = 400;	//ウィンドウの大きさ
	final private int FIELD_W = 200, FIELD_H = 30;	//テキストフィールドの大きさ
	final private String RULE = "";					//遊び方
	final private String TITLE[] = {				//タイトル一覧
		"ようこそ","ログイン","新規登録","メニュー",
		"対局","対局","対局","対局","対局","対局","対局","対局",
		"観戦","観戦",
		"記録","記録","記録","記録",
		"遊び方"
	};
	private Container pane;							//コンテナ
	private String operation;						//オペレーション
	private JPanel[] panel = new JPanel[19];		//パネル
	private JPanel subpanel9,subpanel12;			//リスト表示用パネル
	private JLabel label1_3, label2_3, label15, label17;//ラベル
	private JTextField field1, field2, field16;		//フィールド
	private JPasswordField passfield1, passfield2, passfield10;//パスワードフィールド
	private JRadioButton rb71,rb72;					//ラジオボタン
	private JComboBox<String> box7;
	ImageIcon whiteIcon, blackIcon, boardIcon;		//アイコン
	private JButton buttonArray[];					//オセロ盤
	// ID
	private int panelID;													//画面ID
	private HashMap<String,String> dataID = new HashMap<String,String>(20);	//コマンド-データ対応表
	{dataID.put("Register","0");
	dataID.put("Login","1");
	dataID.put("RandomMatch","2");
	dataID.put("Table","21");
	//dataID.put("Log","22");
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
	dataID.put("Logout","100");}
	//ストリーム
	private PrintWriter out;						//送信用
	private BufferedReader in;						//受信用
	private Receiver receiver;						//スレッド
	// コンストラクタ/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Client() {
		player = new Player();					//プレイヤ生成
		othello = new Othello();				//オセロ生成
		//フレーム設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		//ペイン獲得
		pane = getContentPane();
		pane.setLayout(null);
		//アイコン設定
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");
		/* パネルデザイン *****************************************************************/
		panel = new JPanel[19];
		//タイトル画面
		JButton b01 = new JButton("ログイン");
		b01.setSize(100,50);
		b01.setActionCommand("Switch,1");
		b01.addMouseListener(this);
		JButton b02 = new JButton("新規登録");
		b02.setSize(100,50);
		b02.setActionCommand("Switch,2");
		b02.addMouseListener(this);
		panel[0] = new JPanel();
		panel[0].setSize(WIDTH,HEIGHT);
		panel[0].add(b01);
		panel[0].add(b02);
		//ログイン画面
		JLabel label1_1 = new JLabel("プレイヤ名");
		field1 = new JTextField(20);
		JLabel label1_2 = new JLabel("パスワード");
		passfield1 = new JPasswordField(20);
		label1_3 = new JLabel(" ");
		label1_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		JButton b11 = new JButton("OK");
		b11.setActionCommand("Login,-1");
		b11.addMouseListener(this);
		JButton b12 = new JButton("戻る");
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
		//新規登録画面
		JLabel label2_1 = new JLabel("プレイヤ名");
		field2 = new JTextField(20);
		JLabel label2_2 = new JLabel("パスワード");
		passfield2 = new JPasswordField(20);
		label2_3 = new JLabel(" ");
		label2_3.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		JButton b21 = new JButton("OK");
		b21.setActionCommand("Register,-1");
		b21.addMouseListener(this);
		JButton b22 = new JButton("戻る");
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
		//メニュー画面
		JButton b31 = new JButton("対局する");
		b31.setMaximumSize(new Dimension(150, 50));
		b31.setActionCommand("Switch,4");
		b31.addMouseListener(this);
		JButton b32 = new JButton("観戦する");
		b32.setMaximumSize(new Dimension(150, 50));
		b32.setActionCommand("WatchroomList,-1");
		b32.addMouseListener(this);
		JButton b33 = new JButton("記録を見る");
		b33.setMaximumSize(new Dimension(150, 50));
		b33.setActionCommand("Switch,14");
		b33.addMouseListener(this);
		JButton b34 = new JButton("遊び方");
		b34.setMaximumSize(new Dimension(150, 50));
		b34.setActionCommand("Switch,18");
		b34.addMouseListener(this);
		JButton b35 = new JButton("ログアウト");
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
		//ランダム・プライベート選択画面
		JButton b41 = new JButton("ランダムマッチ");
		b41.setMaximumSize(new Dimension(150, 50));
		b41.setActionCommand("RandomMatch,-1");
		b41.addMouseListener(this);
		JButton b42 = new JButton("プライベートマッチ");
		b42.setMaximumSize(new Dimension(150, 50));
		b42.setActionCommand("Switch,6");
		b42.addMouseListener(this);
		JButton b43 = new JButton("戻る");
		b43.setMaximumSize(new Dimension(150, 50));
		b43.setActionCommand("Switch,3");
		b43.addMouseListener(this);
		panel[4] = new JPanel();
		panel[4].setSize(WIDTH,HEIGHT);
		panel[4].setLayout(new BoxLayout(panel[4], BoxLayout.Y_AXIS));
		panel[4].add(b41);
		panel[4].add(b42);
		panel[4].add(b43);
		//マッチング待機画面
		JLabel label5 = new JLabel("対戦相手を探しています。しばらくお待ちください。");
		JButton b51 = new JButton("戻る");
		b51.setSize(100,50);
		b51.setActionCommand("CancelMatch,-1");
		b51.addMouseListener(this);
		panel[5] = new JPanel();
		panel[5].setSize(WIDTH,HEIGHT);
		panel[5].add(label5);
		panel[5].add(b51);
		//部屋の作成・入室選択画面
		JButton b61 = new JButton("部屋を作る");
		b61.setMaximumSize(new Dimension(150, 50));
		b61.setActionCommand("Switch,7");
		b61.addMouseListener(this);
		JButton b62 = new JButton("部屋に入る");
		b62.setMaximumSize(new Dimension(150, 50));
		b62.setActionCommand("KeyroomList,-1");
		b62.addMouseListener(this);
		JButton b63 = new JButton("戻る");
		b63.setMaximumSize(new Dimension(150, 50));
		b63.setActionCommand("Switch,4");
		b63.addMouseListener(this);
		panel[6] = new JPanel();
		panel[6].setSize(WIDTH,HEIGHT);
		panel[6].setLayout(new BoxLayout(panel[6], BoxLayout.Y_AXIS));
		panel[6].add(b61);
		panel[6].add(b62);
		panel[6].add(b63);
		//部屋の設定画面
		JLabel label7_1 = new JLabel("部屋の設定を決めてください");
		JLabel label7_2 = new JLabel("チャット");
		rb71 = new JRadioButton("ON",true);
		rb72 = new JRadioButton("OFF",false);
		ButtonGroup group = new ButtonGroup();
		group.add(rb71);
		group.add(rb72);
		JLabel label7_3 = new JLabel("制限時間");
		String[] time = {"1","3","5"};
		box7 = new JComboBox<String>(time);
		JLabel label7_4 = new JLabel("分");
		JButton b71 = new JButton("決定");
		b71.setSize(100,50);
		b71.setActionCommand("MakeKeyroom,-1");
		b71.addMouseListener(this);
		JButton b72 = new JButton("戻る");
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
		//相手の入室待機画面
		JLabel label8 = new JLabel("対戦相手の入室を待っています。しばらくお待ちください。");
		JButton b81 = new JButton("戻る");
		b81.setSize(100,50);
		b81.setActionCommand("DeleteKeyroom,-1");
		b81.addMouseListener(this);
		panel[8] = new JPanel();
		panel[8].setSize(WIDTH,HEIGHT);
		panel[8].add(label8);
		panel[8].add(b81);
		//鍵部屋選択画面
		subpanel9 = new JPanel();
		JButton b91 = new JButton("戻る");
		b91.setActionCommand("Switch,6");
		b91.addMouseListener(this);
		panel[9] = new JPanel();
		panel[9].setSize(WIDTH,HEIGHT);
		panel[9].add(subpanel9);
		panel[9].add(b91);
		//パスワード入力画面
		JLabel label10_1 = new JLabel("パスワード");
		passfield10 = new JPasswordField(20);
		JButton b101 = new JButton("OK");
		b101.setSize(100,50);
		b101.setActionCommand("EnterKeyroom,-1");
		b101.addMouseListener(this);
		JButton b102 = new JButton("戻る");
		b102.setSize(100,50);
		b102.setActionCommand("Switch,9");
		b102.addMouseListener(this);
		panel[10] = new JPanel();
		panel[10].setSize(WIDTH,HEIGHT);
		panel[10].setLayout(new BoxLayout(panel[10], BoxLayout.Y_AXIS));
		JPanel panel10_1 = new JPanel();
		panel10_1.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel10_1.add(label10_1);
		panel10_1.add(passfield10);
		JPanel panel10_2 = new JPanel();
		panel10_2.setMaximumSize(new Dimension(WIDTH, FIELD_H));
		panel10_2.add(b101);
		panel10_2.add(b102);
		panel[10].add(panel10_1);
		panel[10].add(panel10_2);
		//対局画面
		JLabel label11 = new JLabel("対局画面");
		panel[11] = new JPanel();
		panel[11].setSize(WIDTH,HEIGHT);
		panel[11].add(label11);
		//観戦部屋選択画面
		subpanel12 = new JPanel();
		JButton b121 = new JButton("戻る");
		b121.setSize(100,50);
		b121.setActionCommand("Switch,3");
		b121.addMouseListener(this);
		panel[12] = new JPanel();
		panel[12].setSize(WIDTH,HEIGHT);
		panel[12].add(subpanel12);
		panel[12].add(b121);
		//観戦画面
		JLabel label13 = new JLabel("観戦画面");
		panel[13] = new JPanel();
		panel[13].setSize(WIDTH,HEIGHT);
		panel[13].add(label13);
		//記録選択画面
		JButton b141 = new JButton("総合戦績");
		b141.setSize(100,50);
		b141.setActionCommand("TotalRecord,-1");
		b141.addMouseListener(this);
		JButton b142 = new JButton("対人戦績");
		b142.setSize(100,50);
		b142.setActionCommand("Switch,16");
		b142.addMouseListener(this);
		JButton b143 = new JButton("戻る");
		b143.setSize(100,50);
		b143.setActionCommand("Switch,3");
		b143.addMouseListener(this);
		panel[14] = new JPanel();
		panel[14].setSize(WIDTH,HEIGHT);
		panel[14].add(b141);
		panel[14].add(b142);
		panel[14].add(b143);
		//総合成績画面
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
		//相手名入力画面
		field16 = new JTextField("opponent");
		field16.setMaximumSize(new Dimension(FIELD_W, FIELD_H));
		JButton b161 = new JButton("OK");
		b161.setSize(100,50);
		b161.setActionCommand("IdRecord,-1");
		b161.addMouseListener(this);
		JButton b162 = new JButton("戻る");
		b162.setSize(100,50);
		b162.setActionCommand("Switch,14");
		b162.addMouseListener(this);
		panel[16] = new JPanel();
		panel[16].setSize(WIDTH,HEIGHT);
		panel[16].add(field16);
		panel[16].add(b161);
		panel[16].add(b162);
		//相手別戦績画面
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
		//遊び方表示画面
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
		//初期画面描画
		panelID = 0;
		switchDisplay();


		//String [] grids = game.getGrids(); //局面情報を取得
		//int row = game.getRow(); //オセロ盤の縦横マスの数を取得
		//オセロ盤の生成
		/*
		buttonArray = new JButton[row * row];//ボタンの配列を作成
		for(int i = 0 ; i < row * row ; i++){
			if(grids[i].equals("black")){ buttonArray[i] = new JButton(blackIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i].equals("white")){ buttonArray[i] = new JButton(whiteIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i].equals("board")){ buttonArray[i] = new JButton(boardIcon);}//盤面状態に応じたアイコンを設定
			c.add(buttonArray[i]);//ボタンの配列をペインに貼り付け
			// ボタンを配置する
			int x = (i % row) * 45;
			int y = (int) (i / row) * 45;
			buttonArray[i].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
			buttonArray[i].addMouseListener(this);//マウス操作を認識できるようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンを識別するための名前(番号)を付加する
		}
		//終了ボタン
		stop = new JButton("終了");//終了ボタンを作成
		c.add(stop); //終了ボタンをペインに貼り付け
		stop.setBounds(0, row * 45 + 30, (row * 45 + 10) / 2, 30);//終了ボタンの境界を設定
		stop.addMouseListener(this);//マウス操作を認識できるようにする
		stop.setActionCommand("stop");//ボタンを識別するための名前を付加する
		//パスボタン
		pass = new JButton("パス");//パスボタンを作成
		c.add(pass); //パスボタンをペインに貼り付け
		pass.setBounds((row * 45 + 10) / 2, row * 45 + 30, (row * 45 + 10 ) / 2, 30);//パスボタンの境界を設定
		pass.addMouseListener(this);//マウス操作を認識できるようにする
		pass.setActionCommand("pass");//ボタンを識別するための名前を付加する
		//色表示用ラベル
		String myName = player.getName();
		colorLabel = new JLabel(myName + "さんの色は未定です");//色情報を表示するためのラベルを作成
		colorLabel.setBounds(10, row * 45 + 60 , row * 45 + 10, 30);//境界を設定
		c.add(colorLabel);//色表示用ラベルをペインに貼り付け
		//手番表示用ラベル
		turnLabel = new JLabel("手番は未定です");//手番情報を表示するためのラベルを作成
		turnLabel.setBounds(10, row * 45 + 120, row * 45 + 10, 30);//境界を設定
		c.add(turnLabel);//手番情報ラベルをペインに貼り付け
		*/
	}
	// 通信 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//サーバに接続
	public void connectServer(String ipAddress, int port){
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port);				//接続
			System.out.println("サーバと接続しました");
			out = new PrintWriter(socket.getOutputStream());	//出力ストリーム
			receiver = new Receiver(socket);					//受信スレッド
			receiver.start();
			sendMessage("dummy");								//調整用ダミー
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
		out.println(msg);					//バッファに書き込み
		out.flush();						//データ送信
		System.out.println("送信：" +msg);	//テスト出力
	}
	//データの受信
	class Receiver extends Thread {
		Receiver (Socket socket){
			try{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //入力ストリーム
			}
			catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
		public void run(){
			try{
				while(true) {
					String inputLine = in.readLine();			//バッファを読み込み
					if (inputLine != null){						//データ受信
						System.out.println("受信：" +inputLine);//テスト出力
						receiveMessage(inputLine);				//処理
					}
				}
			}
			catch (IOException e){
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
	}
	//受信データの判別・処理
	public void receiveMessage(String msg){
		String[] c = operation.split(",",2);	//オペレーションをコマンドと付属情報に分解
		String command = c[0];					//コマンド
		String subc = c[1];						//付属情報
		//ログイン結果
		if(command.equals("Login")){
			if(msg.equals("true")){					//ログイン成功
				showDialog("ログインしました");		//ダイアログの表示
				panelID = 3;						//メニュー画面へ
				switchDisplay();					//画面遷移
			}
			else{																				//ログイン失敗
				if(msg.equals("name")) label1_3.setText("そのようなプレイヤ名は存在しません");	//メッセージ表示
				if(msg.equals("pass")) label1_3.setText("パスワードが違います");				//メッセージ表示
/**/			player.setName(null);															//プレイヤ名リセット
/**/			player.setPass(null);															//パスワードリセット
			}
		}
		//登録結果
		else if(command.equals("Register")){
			if(msg.equals("true")){					//登録許可
				showDialog("登録しました");			//ダイアログの表示
				operation = "Login,-1";				//ログインオペレーション実行
/**/			sendMessage(dataID.get("Login")+","+player.getName()+","+player.getPass());
			}
			else{														//登録失敗
				label2_3.setText("そのプレイヤ名は既に使われています");	//メッセージ表示
/**/			player.setName(null);									//プレイヤ名リセット
/**/			player.setPass(null);									//パスワードリセット
			}
		}
		//ランダムマッチング結果
		else if(command.equals("RandomMatch")){
			String[] info = msg.split(",",3);
			if(info[0].equals("true")){						//マッチング成功
/**/			player.setColor(info[2]);					//手番情報保存
				if(info[2]=="0"){
/**/				othello.setName0(player.getName());		//先手名保存
/**/				othello.setName1(info[1]);				//後手名保存
				}
				if(info[2]=="1"){
/**/				othello.setName0(info[1]);				//先手名保存
/**/				othello.setName1(player.getName());		//後手名保存
				}
				panelID = 11;								//対局画面へ
			}
			if(info[0].equals("false")){					//マッチング失敗
				showDialog("マッチングに失敗しました");		//ダイアログの表示
				panelID = 4;								//ランダム・プライベート選択画面へ
			}
			switchDisplay();								//画面遷移
		}
		//鍵部屋マッチング結果
		else if(command.equals("MakeKeyroom")){
			String[] info = msg.split(",",2);
/**/		othello.setRoomID(Integer.parseInt(info[0]));	//部屋番号保存
/**/		player.setColor("0");							//手番保存
/**/		othello.setName0(player.getName());				//先手名保存
/**/		othello.setName1(info[1]);						//後手名保存
			//部屋描画
			panelID = 11;
			switchDisplay();								//画面遷移
		}
		//鍵部屋リスト
		else if(command.equals("KeyroomList")){
			String[] keyroom = msg.split(",",0);
			String[] info;										//部屋情報
			String strchat;										//チャットの有無
			for(int i=0;i<keyroom.length;i++){					//選択肢ボタン生成
				info = keyroom[i].split(Pattern.quote("."),4);
				if(info[2].equals("true")) strchat = "あり";
				else strchat = "なし";
				JButton bi = new JButton("作成者："+info[1]+" チャット："+strchat+" 制限時間："+info[3]+"分");
				bi.setActionCommand("SelectKeyroom,"+keyroom[i]);
				bi.addMouseListener(this);
				subpanel9.add(bi);
			}
			panelID = 9;										//鍵部屋リスト表示画面へ
			switchDisplay();									//画面遷移
		}
		//鍵部屋への入室
		else if(command.equals("EnterKeyroom")){
			String[] info = msg.split(",",3);
			if(info[0].equals("true")){							//入室許可
/**/			othello.setRoomID(Integer.parseInt(info[1]));	//部屋番号保存
/**/			player.setColor("1");							//手番保存
/**/			othello.setName0(info[2]);						//先手名保存
/**/			othello.setName1(player.getName());				//後手名保存
				//部屋描画
				panelID = 11;									//対局画面へ
			}
			if(info[0].equals("false")){						//入室失敗
/**/			player.setConfiguration("true,-1");				//設定情報リセット
				showDialog("入室に失敗しました");				//ダイアログの表示
				panelID = 3;									//メニュー画面へ
			}
			switchDisplay();									//画面遷移
		}
		//観戦部屋リスト
		else if(command.equals("WatchroomList")){
			String[] room = msg.split(",",0);
			String[] info;							//部屋情報
			for(int i=0;i<room.length;i++){			//選択肢ボタン生成
				info = room[i].split(Pattern.quote("."),4);
				System.out.println(info.length);
				JButton bi = new JButton("先手："+info[0]+" 後手："+info[1]+" 黒石："+info[2]+" 白石："+info[3]);
				bi.setActionCommand("EnterWatchroom,-1");
				bi.addMouseListener(this);
				subpanel12.add(bi);
			}
			panelID = 12;							//観戦部屋リスト表示画面へ
			switchDisplay();						//画面遷移
		}
		//観戦部屋への入室
		else if(command.equals("EnterWatchroom")){
			if(msg.equals("true")){					//入室許可
				//部屋描画
				panelID = 13;						//観戦画面へ
			}
			if(msg.equals("false")){				//入室失敗
/**/			othello.setRoomID(-1);				//部屋番号リセット
/**/			othello.setName0(null);				//先手名リセット
/**/			othello.setName1(null);				//後手名リセット
				showDialog("入室に失敗しました");	//ダイアログの表示
				panelID = 3;						//メニュー画面へ
			}
			switchDisplay();						//画面遷移
		}
		//総合記録
		else if(command.equals("TotalRecord")){
			label15.setText(msg);					//記録書込み
			panelID = 15;							//総合戦績画面へ
			switchDisplay();						//画面遷移
		}
		//対人別記録
		else if(command.equals("IdRecord")){
			label17.setText(msg);					//記録書込み
			panelID = 17;							//相手別戦績画面へ
			switchDisplay();						//画面遷移
		}
		//対局情報
		else if(panelID == 11){
			String[] info = msg.split(",",0);		//以下、受信内容で場合分け
			//盤面・ログ受信
			if(info[0].equals(dataID.get("Table"))){
				String table = info[1];				//盤面保存
				String log = info[2];				//ログ保存
				//盤面反映
				//ログ反映
			}
			//投了受信
			else if(info[0].equals(dataID.get("Giveup"))){
/**/			othello.setRoomID(-1);						//部屋番号リセット
/**/			player.setColor("-1");						//手番リセット
/**/			othello.setName0(null);						//先手名リセット
/**/			othello.setName1(null);						//後手名リセット
/**/			player.setConfiguration("true,-1");			//設定情報リセット
				showDialog("対戦相手が投了しました");		//ダイアログ表示
				panelID = 3;								//メニュー画面へ
				switchDisplay();							//画面遷移
			}
			//チャット受信
			else if(info[0].equals(dataID.get("Chat"))){
				String str = info[1];						//チャット保存
				//チャット反映
			}
		}
		//観戦情報
		else if(panelID == 13){
			//観戦中の処理
		}
	}
	// 操作 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//マウスリスナー
	public void mouseClicked(MouseEvent e) {
		JButton button = (JButton)e.getComponent();			//ボタン特定
		operation = button.getActionCommand();				//オペレーション確認
		System.out.println("実行：" +operation);			//テスト出力
		acceptOperation(operation);							//処理
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	//操作の受付・処理
	public void acceptOperation(String operation){
		String[] c = operation.split(",",2);	//オペレーションをコマンドと付属情報に分解
		String command = c[0];					//コマンド
		String subc = c[1];						//付属情報
		switch(command){						//以下、コマンドで場合分け
			//画面遷移のみ
			case "Switch":
				panelID = Integer.parseInt(subc);
				switchDisplay();
				break;
			//ログイン要求
			case "Login":
				String logname = field1.getText();						//プレイヤ名読み取り
				String logpass = new String(passfield1.getPassword());	//パスワード読み取り
				if(logname.equals("")) {
					label1_3.setText("プレイヤ名が空欄です");
					break;
				}
				else if(logpass.equals("")) {
					label1_3.setText("パスワードが空欄です");
					break;
				}
				player.setName(logname);									//プレイヤ名保存
				player.setPass(logpass);									//パスワード保存
				sendMessage(dataID.get(command)+","+logname+","+logpass);	//サーバへ送信
				break;
			//新規登録要求
			case "Register":
				String regname = field2.getText();						//プレイヤ名読み取り
				String regpass = new String(passfield2.getPassword());	//パスワード読み取り
				if(regname.equals("")) {
					label2_3.setText("プレイヤ名が空欄です");
					break;
				}
				else if(regpass.equals("")) {
					label2_3.setText("パスワードが空欄です");
					break;
				}
				player.setName(regname);									//プレイヤ名保存
				player.setPass(regpass);									//パスワード保存
				sendMessage(dataID.get(command)+","+regname+","+regpass);	//サーバへ送信
				break;
			//ランダムマッチング要求
			case "RandomMatch":
				sendMessage(dataID.get(command));				//サーバへ送信
				panelID = 5;									//マッチング待機画面へ
				switchDisplay();
				break;
			//マッチングキャンセル処理
			case "CancelMatch":
				sendMessage(dataID.get(command));				//サーバへ送信
				panelID = 4;									//ランダム・プライベート選択画面へ
				switchDisplay();
				break;
			//石を置く
			case "Table":
				sendMessage(dataID.get(command)+",盤面,ログ内容");	//サーバへ送信
				//盤面反映
				//ログ反映
				break;
			//投了
			case "Giveup":
				sendMessage(dataID.get(command));	//サーバへ送信
/**/			othello.setRoomID(-1);				//部屋番号リセット
/**/			player.setColor("-1");				//手番リセット
/**/			othello.setName0(null);				//先手名リセット
/**/			othello.setName1(null);				//後手名リセット
/**/			player.setConfiguration("true,-1");	//設定情報リセット
				showDialog("投了しました");			//ダイアログ表示
				panelID = 3;						//メニュー画面へ
				switchDisplay();					//画面遷移

				break;
			//チャット
			case "Chat":
				sendMessage(dataID.get(command)+",チャット内容");	//サーバへ送信
				//チャット反映
				break;
			//総合記録要求
			case "TotalRecord":
				sendMessage(dataID.get(command));				//サーバへ送信
				break;
			//対人別記録要求
			case "IdRecord":
				sendMessage(dataID.get(command)+","+field16.getText());//サーバへ送信
				break;
			//鍵部屋作成要求
			case "MakeKeyroom":
				String chat = Boolean.toString(rb71.isSelected());		//チャットの有無読み取り
				String time = (String)box7.getSelectedItem();			//制限時間読み取り
/**/			player.setConfiguration(chat+","+time);					//設定情報保存
				sendMessage(dataID.get(command)+","+chat+","+time);		//サーバへ送信
				panelID = 8;											//相手の入室待機画面
				switchDisplay();
				break;
			//鍵部屋削除要求
			case "DeleteKeyroom":
/**/			player.setConfiguration("true,-1");		//設定情報リセット
				sendMessage(dataID.get(command));		//サーバへ送信
				panelID = 4;							//ランダム・プライベート選択画面へ
				switchDisplay();						//画面遷移
				break;
			//鍵部屋リスト要求
			case "KeyroomList":
				sendMessage(dataID.get(command));				//サーバへ送信
				break;
			//鍵部屋リスト選択
			case "SelectKeyroom":
				String[] info = subc.split(Pattern.quote("."),4);
				player.setConfiguration(info[2]+","+info[3]);	//設定情報保存
				panelID = 10;									//パスワード入力画面へ
				switchDisplay();
				break;
			//鍵部屋への入室要求
			case "EnterKeyroom":
/**/			sendMessage(dataID.get(command)+","+othello.getRoomID()+","+(new String(passfield10.getPassword())));//サーバへ送信
				break;
			//観戦部屋リスト要求
			case "WatchroomList":
				sendMessage(dataID.get(command));				//サーバへ送信
				break;
			//観戦部屋への入室要求
			case "EnterWatchroom":
				sendMessage(dataID.get(command));				//サーバへ送信
				break;
			//リアクション
			case "Reaction":
				sendMessage(dataID.get(command));				//サーバへ送信
				break;
			//ログアウト
			case "Logout":
				sendMessage(dataID.get(command));				//サーバへ送信
/**/			player.setName(null);							//プレイヤ名リセット
/**/			player.setPass(null);							//パスワードリセット
				panelID = 0;									//タイトル画面へ
				switchDisplay();
				break;
			default:
				break;
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
	//メッセージダイアログ
	class MessageDialog extends JDialog implements ActionListener{
		public MessageDialog(JFrame mainframe,String message){
			super(mainframe,"メッセージ",ModalityType.APPLICATION_MODAL);//モーダルに設定
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
	//ダイアログの表示
	public void showDialog(String str){
		MessageDialog dialog = new MessageDialog(this,str);
		dialog.setVisible(true);
	}
	// メイン ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String args[]){

		//String ipAddress = "192.168.1.11";	//IPアドレス
		//String ipAddress = "220.215.242.167";	//IPアドレス
		int port = 10000;

		Client client = new Client();			//クライアント生成
		client.setVisible(true);				//画面表示
		client.connectServer("localhost", port);	//サーバに接続
	}

	//補助関数(文字列の表すものを判定する)
	public boolean isNumber(String str) {
		try {
		    Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
}