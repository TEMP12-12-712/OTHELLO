import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 public class Server {

  public static final int port = 10000;  //ポート番号１００００
  public static StringBuffer mrlist = new StringBuffer("");
  public static StringBuffer srlist = new StringBuffer("");
  public static secretroom[] sr = new secretroom[1000];
  public static int srNo = 0;			//今newで作られた部屋数+1
  public static matchroom[] mr = new matchroom[1000];
  public static int mrNo = 0;			//今newで作られた部屋数+1


  public static void main(String args[]) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Serverが起動しました(port="
                         + serverSocket.getLocalPort() + ")");
      while (true) {
        Socket socket = serverSocket.accept();
        new EchoThread(socket).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {}
    }
  }
}

class EchoThread extends Thread {

  private Socket socket;
  private String PlayerName = "";
  private int myroomNo = 1001;
  private int myroom = 0;	//0なら対戦してない、１ならマッチ、２なら鍵部屋マッチ
  private int watchNo = 1001;	//観戦しているときの番号
  DataInputStream in;
  DataOutputStream out;


  public EchoThread(Socket socket) {
    this.socket = socket;
    System.out.println("接続されました "
                       + socket.getRemoteSocketAddress());
  }

  public void run() {
    try {
    	in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
    	while(true) {
    		String ClientMessage = in.readUTF();		//クライアントからデータ受信
    		System.out.println("クライアント"+PlayerName+"から「"+ClientMessage + "」を受信");
    		String Return = check(ClientMessage);
    		if(!Return.equals("")) {
    			out.writeUTF(Return);	//クライアントに送信
    			System.out.println(Return);
    		}
        	}
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (socket != null) {
          socket.close();
        out.close();
  		in.close();
        }
      } catch (IOException e) {}
      System.out.println("切断されました "
                         + socket.getRemoteSocketAddress());
    }
  }

  public String check(String message) {		//クライアントからきた命令の先頭にある数字をみてメソッドを呼び出し
	  String[] check = message.split(",", 0);
	  String Re = "";
	  switch(check[0]) {
	  case "0":		//新規登録|resister
		  Re = resister(check[1], check[2]);	//成功true：失敗false
		  if(Re.equals("true")) {
			  PlayerName = check[1];
		  }
		  break;

	  case "1":		//ログイン|rogin
		  Re = login(check[1], check[2]);		//成功true:プレイヤー名がないname:パスワードが違うpass
		  break;

	  case "2":		//マッチング|match
		  match();
		  break;

	  case "21":		//盤面受信
		  field(check[1]);
		  break;

	  case "23":		//投了
		  giveup(check[1]);
		  break;

	  case "24":		//対戦終了
		  newrecord(PlayerName, check[1]);
		  break;

	  case "25":		//チャット
		  chat(check[1]);
		  break;

	  case "3":		//対戦記録|record
		  Re = record(PlayerName);
		  break;

	  case "5":		//鍵部屋作成|make
		  make(PlayerName, check[1], check[2], check[3]);//パスワード、チャットフラグ、時間フラグ
		  break;

	  case "6":		//鍵部屋削除|delete
		  delete();
		  break;

	  case "7":		//鍵部屋リストの送信|list
		  Re = list();
		  break;

	  case "8":		//鍵部屋入室|enterroom
		  enterroom(check[1], check[2]);//部屋名、パスワード
		  break;

	  case "9":		//対戦中の部屋リストを受信|watchroomlist
		  Re = watchroomlist();
		  break;

	  case "91":		//観戦|watch
		System.out.print("check\n");
		  watch(check[1]);
		  break;

	  case "92":		//リアクション|reaction
		  //未定
		  break;

	  default:

	  }
	  return Re;
  }

  public String resister(String PN, String PW) { 		//0:新規登録
		String judge = "true";
		try {
			File file = new File("D:\\PL\\java\\OTHELLO\\src\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str;
				while((str = br.readLine()) != null) {
					String[] check = str.split(",", 0);
					if(check[0].equals(PN)) {
						judge = "false";
					}
				}
				br.close();

			}else {
				System.out.println("ファイルなし");
			}

			if(judge.equals("true")) {
				FileWriter fr = new FileWriter(file, true);
				fr.write(PN + "," + PW + ",0,0,0,0\n");
				fr.close();
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return judge;
	}

  	//1:ログイン用
	public String login(String PN, String PW) {
		String judge = "name";
		try {
			File file = new File("D:\\PL\\java\\OTHELLO\\src\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str;
				while((str = br.readLine()) != null) {
					//System.out.println(str);
					String[] check = str.split(",", 0);
					//System.out.println(check[0]);
					if(check[0].equals(PN)) {
						judge = "pass";
						if(check[1].equals(PW)) {
							judge = "true";
							PlayerName = PN;
						}
					}
				}
				br.close();

			}else {
				System.out.println("ファイルなし");
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return judge;
	}

	//2:マッチングをする
	public void match() {
		if(Server.mrNo != 0) {
			if (myroomNo == 1001) {
				//一人しかいない部屋があったらそこに入る
				for(int i = 0;i < Server.mrNo; i++) {
					if (!Server.mr[i].getPN1().equals("") && Server.mr[i].getPN2().equals("")) {
						Server.mr[i].set2P(PlayerName,  socket);
						myroomNo = i;
						break;
					}
				}
			}
			if (myroomNo == 1001) {
				//一人しかいない部屋はなかった
				//二人ともいない部屋があったらその最初の部屋に
				for(int i = 0; i < Server.mrNo; i++) {
					if (Server.mr[i].getPN1().equals("")) {
						Server.mr[i].set1P(PlayerName,  socket);
						myroomNo = i;
						break;
					}
				}
			}
		}
		//空き部屋がなければ新しい部屋を作る
		if (myroomNo == 1001) {
			Server.mr[Server.mrNo] = new matchroom(PlayerName, socket);
			myroomNo = Server.mrNo;
			Server.mrNo++;
			//で、そこの1Pにするんじゃないの？
			Server.mr[myroomNo].set1P(PlayerName, socket);
		}
		myroom = 1;
		System.out.println(PlayerName + "," + myroomNo + ",ok");
	}


	//21:盤面を送信する
	public void field(String field) {		//盤面を受信。対戦相手に送る
		//送ってきたプレイヤーでない方のプレイヤーと観客に盤面を送信
		if(myroom == 1) {
			Server.mr[myroomNo].setfield(PlayerName, field);
		}else if(myroom == 2) {
			Server.sr[myroomNo].setfield(PlayerName, field);
		}
	}

	//22:パスをする
	public void passing() {
		if(myroom == 1) {
			Server.mr[myroomNo].sendpass(PlayerName);
		}else {
			Server.sr[myroomNo].sendpass(PlayerName);
		}
	}

	//23:投了
	public void giveup(String message) {
		//もうひとりのプレイヤーと観客に投了したことを伝える
		if(myroom == 1) {
			Server.mr[myroomNo].sendgiveup(PlayerName);
		}else if(myroom == 2) {
			Server.sr[myroomNo].sendgiveup(PlayerName);
		}
		newrecord(PlayerName, "4");
	}

	//24:対局が終了
	public void newrecord(String PN, String flag) {//flag = 1なら勝ち、2なら負け、3なら引き分け、4なら投了
		String str = "";
		StringBuffer sb = new StringBuffer("");
		try {
			File file = new File("D:\\PL\\java\\OTHELLO\\src\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				int win, lose, draw, giveup;
				while((str = br.readLine()) != null) {
					String[] check = str.split(",", 0);

					if (check[0].equals(PN)) {
						switch(flag) {
							case "1":
								win = Integer.parseInt(check[2]);
								win++;
								check[2] = String.valueOf(win);
								break;

							case "2":
								lose = Integer.parseInt(check[3]);
								lose++;
								check[3] = String.valueOf(lose);
								break;

							case "3":
								draw = Integer.parseInt(check[4]);
								draw++;
								check[4] = String.valueOf(draw);
								break;

							case "4":
								giveup = Integer.parseInt(check[5]);
								giveup++;
								check[5] = String.valueOf(giveup);
								break;

							default:

						}
						str = check[0] + check[1] + check[2] + check[3] + check[4] + check[5];
						sb.append(str);
						sb.append(System.getProperty("line.separator"));
					} else {
						sb.append(str);
						sb.append(System.getProperty("line.separator"));
					}
				}
				FileWriter fw = new FileWriter(file);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				pw.println(sb.toString());
				pw.close();
				br.close();

			}else {
				System.out.println("ファイルなし");
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
	//System.out.println(sb.toString());
	//部屋のプレイヤー名を空にする。
		if(myroom == 1) {
			Server.mr[myroomNo].deletePN1();
			Server.mr[myroomNo].deletePN2();
			Server.mr[myroom].deletewatcher();
		}else if(myroom == 2) {
			Server.sr[myroomNo].deletePN1();
			Server.sr[myroomNo].deletePN2();
		}
		//myroomNoとmyroomを空に
		myroomNo = 1001;
		myroom = 0;
	}

	//25:チャット
	public void chat(String chat){
		if(myroom == 1) {
			Server.mr[myroomNo].setlog(PlayerName, chat);
		}else {
			Server.sr[myroomNo].setlog(PlayerName, chat);
		}
	}

	//3:対戦記録を閲覧
	public String record(String PN) {
		String PD = "no";
		try {
			File file = new File("D:\\PL\\java\\OTHELLO\\src\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str;
				while((str = br.readLine()) != null) {
					String[] check = str.split(",", 0);
					//System.out.println(str);
					if(check[0].equals(PN)) {
						PD = check[2] +"," + check[3] +","+ check[4] +","+ check[5];
						break;
					}
				}
				br.close();
			}else {
				System.out.println("ファイルなし");
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return PD;
	}

	//5:鍵部屋の作成
	public void make(String PN, String password, String chatflag, String timeflag) {
		if(Server.srNo != 0) {
			for(int i = 0; i < Server.srNo;i++) {
				if(Server.sr[i].getPN1().equals("")) {
					Server.sr[i].set1P(PN, socket, password, chatflag, timeflag);
					myroomNo = i;
				}
			}
		}
		if(myroomNo == 1001) {
			Server.sr[Server.srNo] = new secretroom(PN, socket, password, chatflag, timeflag);
			myroomNo = Server.srNo;
			Server.srNo++;
			myroom = 2;		//鍵部屋にいる状態
		}
	}

	//6:鍵部屋からプレイヤーネームを削除
	public void delete() {
		Server.sr[myroomNo].deletePN1();
	}

	//7:鍵部屋リストの閲覧
	public String list() {
		String str = "no";
		if(Server.srNo != 0) {
			for(int i = 0;i < Server.srNo; i++) {
				//プレイヤー１のみの鍵部屋をリストアップ
				if(!Server.sr[i].getPN1().equals("") && Server.sr[i].getPN2().equals("")) {
					Server.srlist.append(Integer.toString(i) + "." + Server.sr[i].getlistData());
				}
			}
			if(!Server.srlist.toString().equals("")) {
				str = Server.srlist.toString();
			}
		}
		System.out.println(str);
		return str;
	}

	//8:鍵部屋に入室
	public void enterroom(String roomNo, String pass) {
		myroomNo = Integer.parseInt(roomNo);
		String password = Server.sr[myroomNo].getpass();
		if(pass.equals(password)) {
			//鍵部屋の2Pにクライアントの名前とソケットを送る
			Server.sr[myroomNo].set2P(PlayerName, socket);
			myroom = 2;		//鍵部屋にいる状態
		}
	}

	//9:観戦部屋リスト
	public String watchroomlist() {
		String field;
		String str = "no";
		char a = '1';
		char b = '2';
		int white = 0;
		int black = 0;
		for(int i = 0;i < Server.mrNo; i++) {
			//プレイヤーがそろった部屋をリストアップ
			if(!Server.mr[i].getPN1().equals("") && !Server.mr[i].getPN2().equals("")) {
				field = Server.mr[i].sendfield();
				for(int j = 0; j < field.length();i++) {
					if(field.charAt(i) == a) {
						black++;
					}else if(field.charAt(i) == b) {
						white++;
					}
				}
				Server.mrlist.append(Integer.toString(i) + "." + Server.sr[i].getlistData() + "."
						+ Integer.toString(black) + "." + Integer.toString(white) + ",");
			}
		}
		if(!Server.mrlist.toString().equals("")) {
			str = Server.mrlist.toString();
		}
		return str;
	}

	//91:観戦入室
	public void watch(String roomNumber) {
		watchNo = Server.mr[Integer.valueOf(roomNumber)].setwatcher(PlayerName, socket);
		myroomNo = Integer.parseInt(roomNumber);
	}

	//92:観客がリアクションを送る
	public void reaction() {

	}

	//93観戦退室
	public void watchout() {
		Server.mr[myroomNo].watchout(watchNo);
	}



}