import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class matchroom {

	//部屋作成者のコンストラクタ
	public matchroom(String PN1, Socket s1) {
		PlayerName1 = PN1;
		socket1 = s1;
	}

	private String grids;					//局面データ
	private String PlayerName1;		//プレイヤ１(先手)
	private String PlayerName2;		//プレイヤ２(後手)
	private Socket socket1, socket2;
	private String log;						//ログ
	private String[] view;						//観戦者
	private Socket[] watchsocket = new Socket[10];
	private int socketNo = 0;
	int flag;						//手番



	public void set2P(String PN2, Socket s) {//プレイヤー２が決まる
		if(PlayerName2.equals(null)) {
			PlayerName2 = PN2;
			socket2 = s;

			try {
				DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF(PlayerName2);	//クライアントに送信
				out1.close();
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



	public String getPN1(){
		return PlayerName1;
	}

	public void deletePN1() {
		PlayerName1 = null;
		socket1 = null;
	}

	public String getPN2() {
		return PlayerName2;
	}

	public void deletePN2() {
		PlayerName2 = null;
		socket2 = null;
	}

	public String setData(String PN) {
		String re = null;
		if(PN.equals(PlayerName1)) {
			re = "true" + "," + PlayerName2 + "," + "0";
		}else if(PN.equals(PlayerName2)) {
			re = "true" + "," + PlayerName1 + "," + "1";
		}
		return re;
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

	public String sendfield() {
		return grids;
	}

	public int setwatcher(String PN, Socket socket) {
		int flag = 0;
		int watchNo = 0;
		if(socketNo != 0 && flag == 0) {
			for(int i = 0; i < socketNo;i++) {
				if(watchsocket[i].equals(null)) {
					watchsocket[i] = socket;
					flag = 1;
					watchNo = i;
				}
			}
		}
		if(flag == 0) {
			watchsocket[socketNo] = new Socket();
			watchsocket[socketNo] = socket;
			watchNo = socketNo;
			socketNo++;
			flag++;
		}
		return watchNo;
	}

	public void watchout(int No) {
		watchsocket[No] = null;
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




	}

}