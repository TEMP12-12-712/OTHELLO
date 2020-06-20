import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

 public class Server {

  public static final int port = 10000;  //ポート番号１００００
  public static StringBuffer srlist = new StringBuffer("");
  public static secretroom[] sr = new secretroom[1000];
  public static int srNo = 0;
  public static matchroom[] mr = new matchroom[1000];
  public static int mrNo = 0;

  public static void main(String args[]) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("EchoServerが起動しました(port="
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

  private static Socket socket;
  private static String PlayerName ="";
  private static int myroomNo;

  public EchoThread(Socket socket) {
    this.socket = socket;
    System.out.println("接続されました "
                       + socket.getRemoteSocketAddress());
  }

  public void run() {
    try {
      /*	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String line;
      while ( (line = in.readLine()) != null ) {
        System.out.println(socket.getRemoteSocketAddress()
                           + " 受信: " + line);
        out.println(line);
        System.out.println(socket.getRemoteSocketAddress()
                           + " 送信: " + line);

      }*/
    	//受信ストリームの取得
    	while(true) {
    		DataInputStream in = new DataInputStream(socket.getInputStream());
    		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    		String ClientMessage = in.readUTF();		//クライアントからデータ受信
    		System.out.println("クライアント"+ socket.getRemoteSocketAddress() +ClientMessage + "を受信");
    		String Return = check(ClientMessage);
    		out.writeUTF(Return);	//クライアントに送信
    		out.close();
    		in.close();
    	}
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (socket != null) {
          socket.close();
        }
      } catch (IOException e) {}
      System.out.println("切断されました "
                         + socket.getRemoteSocketAddress());
    }
  }

  public String check(String message) {		//クライアントからきた命令の先頭にある数字をみてメソッドを呼び出し
	  String[] check = message.split(",", 0);
	  int checkNo =  Integer.valueOf(check[0]);
	  String Re = null;
	  switch(checkNo) {
	  case 0:		//新規登録|resister
		  Re = resister(check[1], check[2]);	//成功true：失敗false
		  if(Re.equals("true")) {
			  PlayerName = check[1];
		  }
		  break;

	  case 1:		//ログイン|rogin
		  Re = login(check[1], check[2]);		//成功true:プレイヤー名がないname:パスワードが違うpass
		  break;

	  case 2:		//マッチング|match
		  //未定
		  break;

	  case 21:		//盤面受信
		  field(check[1]);
		  break;

	  case 23:		//投了
		  giveup(check[1]);
		  break;

	  case 24:		//対戦終了
		  newrecord(PlayerName, check[1]);
		  break;

	  case 3:		//対戦記録|record
		  Re = record(PlayerName);
		  break;

	  case 5:		//鍵部屋作成|make
		  make(PlayerName, check[1], check[2], check[3]);//パスワード、チャットフラグ、時間フラグ
		  break;

	  case 6:		//鍵部屋削除|delete
		  //未定
		  break;

	  case 7:		//鍵部屋リストの送信|list
		  Re = list();
		  break;

	  case 8:		//鍵部屋入室|enterroom
		  enterroom(check[1], check[2]);//部屋名、パスワード
		  break;

	  case 9:		//対戦中の部屋リストを受信|watchroomlist
		  //watchroomlist();

	  case 91:		//観戦|watch
		  //未定
		  break;

	  case 92:		//リアクション|reaction
		  //未定

	  default:

	  }
	  return Re;
  }

  public static String resister(String PN, String PW) { 		//0:新規登録
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
				fr.write(PN + "," + PW + ",0,0");
				fr.close();
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return judge;
	}

  	//1:ログイン用
	public static String login(String PN, String PW) {
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


	//21:盤面を送信する
	public static void field(String field) {		//盤面を受信。対戦相手に送る

	}

	//23:投了
	public static void giveup(String message) {

	}

	//24:対局が終了
	public static void newrecord(String PN, String flag) {//flag = 1なら勝ち、2なら負け、3なら引き分け、4なら投了

	}
	//3:対戦記録を閲覧
	public static String record(String PN) {
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
						PD = str;
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
	public static void make(String PN, String password, String chatflag, String timeflag) {
		String str = PN + "."  + chatflag + "." + timeflag + ",";
		Server.srlist.append(str);
		Server.sr[Server.srNo] = new secretroom(PN, socket, password);
		myroomNo = Server.srNo;
		Server.srNo++;
	}

	//7:鍵部屋リストの閲覧
	public static String list() {
		String str = Server.srlist.toString();
		return str;
	}

	//8:鍵部屋に入室
	public static void enterroom(String roomNo, String pass) {
		int No = Integer.parseInt(roomNo);
		String password = Server.sr[No].getpass();
		if(pass.equals(password)) {
			//鍵部屋リストから今の部屋を消す
			Server.sr[No].set2P(PlayerName, socket);
		}
	}

	//9:観戦部屋リスト
	public static void watchroomlist() {

	}

	//91:観戦入室
	public static void watch() {

	}

	//92:観客がリアクションを送る
	public static void reaction() {

	}













}