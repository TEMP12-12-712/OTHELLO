import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class secretroom{

	public secretroom(String PN1, Socket s, String Password) {
		PlayerName1 = PN1;
		socket1 = s;
		pass = Password;
	}


	private String pass;				//パスワード
	private String PlayerName1 = null, PlayerName2 = null;
	private Socket socket1, socket2;
	private String grids;

	public String getpass() {
		return pass;
	}

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



	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}

}