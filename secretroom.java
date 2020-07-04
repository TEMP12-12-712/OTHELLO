import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class secretroom{

	public secretroom(String PN1, Socket s, String Password, String Chatflag, String Timeflag) {
		PlayerName1 = PN1;
		socket1 = s;
		pass = Password;
		chatflag = Chatflag;
		timeflag = Timeflag;
	}


	private String pass;				//パスワード
	private String PlayerName1 = "", PlayerName2 = "";
	private Socket socket1 = null, socket2 = null;
	private String grids;
	private String chatflag;		//チャットの有無
	private String timeflag;		//時間

	public String getpass() {
		return pass;
	}

	//一度作られた部屋にプレイヤー１を入れる
	public void set1P(String PN1, Socket s, String Password, String Chatflag, String Timeflag) {
			PlayerName1 = PN1;
			socket1 = s;
			pass = Password;
			chatflag = Chatflag;
			timeflag = Timeflag;
	}

	public void set2P(String PN2, Socket s) {//プレイヤー２が決まる
		System.out.println("check1");
		if(PlayerName2.equals("")) {
			PlayerName2 = PN2;
			socket2 = s;
			System.out.println("check2");
			try {
				DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("true,"+ PlayerName2);	//クライアントに送信
				out1.close();
				DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("true," + PlayerName1);	//クライアントに送信
				out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket1 != null) {
						socket1.close();
					}else if(socket2 != null) {
						socket2.close();
					}
				} catch (IOException e) {}
				System.out.println("切断されました "
	                         + socket1.getRemoteSocketAddress());
			}
		}else {
			try {
				DataOutputStream out2 = new DataOutputStream(s.getOutputStream());
				out2.writeUTF("false");	//クライアントに失敗を送信
				out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket1 != null) {
						socket1.close();
					}else if(s != null) {
						s.close();
					}
				} catch (IOException e) {}
			}
		}

	}

	public String getPN1() {
		return PlayerName1;
	}

	public void deletePN1() {
		PlayerName1 = "";
	}

	public String getPN2() {
		return PlayerName2;
	}

	public void deletePN2() {
		PlayerName2 = "";
	}

	public String getlistData() {
		String str = PlayerName1 + "." + chatflag + "." + timeflag + ",";
		return str;
	}

	public void setfield(String PN, String field) {//送信者と盤面を得て、相手に盤面を送信
		grids = field;								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("21" + grids);	//クライアントに送信
				out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("21" + grids);	//クライアントに失敗を送信
				out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setlog(String PN, String log) {//送信者と盤面を得て、相手に盤面を送信								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("25" + log);	//クライアントに送信
				out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("25" + log);	//クライアントに失敗を送信
				out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendgiveup(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("23");	//クライアントに送信
				out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("23");	//クライアントに失敗を送信
				out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendpass(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("23");	//クライアントに送信
				out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("23");	//クライアントに失敗を送信
				out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}

}