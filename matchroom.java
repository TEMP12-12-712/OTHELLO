import java.io.DataOutputStream;
import java.io.IOException;

public class matchroom {
	//部屋作成者のコンストラクタ
	public matchroom(String PN1, DataOutputStream dos1) {
		PlayerName1 = PN1;
		out1 = dos1;
	}

	private String grids;					//局面データ
	private String PlayerName1 = null;		//プレイヤ１(先手)
	private String PlayerName2 = null;		//プレイヤ２(後手)
	//private Socket socket1, socket2;
	private DataOutputStream out1 = null, out2 = null;
	private String log;						//ログ
	private String[] view;						//観戦者
	//private Socket[] watchsocket = new Socket[10];
	private DataOutputStream[] watchdos = new DataOutputStream[10];
	private int socketNo = 0;
	int flag;						//手番

	public void set1P(String PN1, DataOutputStream dos1) {//プレイヤー1が決まる
		if(PlayerName1 == null) {
			PlayerName1 = PN1;
			out1 = dos1;
		}
	}

	public void set2P(String PN2, DataOutputStream dos2) {//プレイヤー２が決まる
		if(PlayerName2 == null) {
			PlayerName2 = PN2;
			out2 = dos2;

			try {
				//DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("true," + PlayerName2 + ",0");	//クライアントに送信
				//out1.close();
				//DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("true," + PlayerName1 + ",1");	//クライアントに送信
				//out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
	/*		} finally {
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
	*/
		}else {
			try {
				//out2 = new DataOutputStream(s.getOutputStream());
				out2.writeUTF("false");	//クライアントに失敗を送信
				//out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}/*
			finally {
				try {
					if (socket1 != null) {
						socket1.close();
					}else if(s != null) {
						s.close();
					}
				} catch (IOException e) {}
			}*/
		}
	}



	public String getPN1(){
		return PlayerName1;
	}

	public void deletePN1() {
		PlayerName1 = null;
		out1 = null;
	}

	public String getPN2() {
		return PlayerName2;
	}

	public void deletePN2() {
		PlayerName2 = null;
		out2 = null;
	}

	public void deletewatcher() {
		for(int i = 0;i < 10; i++) {
			if(watchdos[i] != null) {
				watchdos[i] = null;
			}
		}
	}

	public void setfield(String PN, String field, String log) {//送信者と盤面を得て、相手に盤面を送信
		grids = field;								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				//DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("21," + grids + "," + log);	//クライアントに送信
				System.out.println("「21," + grids + "," + log + "」を送信");
				//out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("い");
			try {
				//DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("21," + grids + "," +log);	//クライアントに失敗を送信
				System.out.println("「21," + grids + "," + log + "」を送信");
				//out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
		for(int i = 0;i < 10; i++) {
			if(watchdos[i] != null) {
				try{
					//DataOutputStream out = new DataOutputStream(watchsocket[i].getOutputStream());
					watchdos[i].writeUTF("21," + grids + "," + log);
					System.out.println("「21," + grids + "," + log + "」を観客に送信");
					//out.close();
				}catch(IOException e) {
					e.printStackTrace();
					watchout(i);
				}
			}
		}
	}

	public void chat(String PN, String log) {//チャットの送受								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				//DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("25," + log);	//クライアントに送信
				System.out.println("「25," + log + "」を送信");
				//out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				//DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("25," + log);	//クライアントに失敗を送信
				System.out.println("「25," + log + "」を送信");
				//out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
		for(int i = 0;i < 10; i++) {
			if(watchdos[i] != null) {
				try{
					//DataOutputStream out = new DataOutputStream(watchsocket[i].getOutputStream());
					watchdos[i].writeUTF("25," + log);
					System.out.println("「25," + log + "」を観客に送信");
					//out.close();
				}catch(IOException e) {
					e.printStackTrace();
					watchout(i);
				}
			}
		}
	}

	public void sendgiveup(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				//DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("23");	//クライアントに送信
				System.out.println("「23」を送信");
				//out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				//DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("23");	//クライアントに失敗を送信
				System.out.println("「23」を送信");
				//out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
				for(int i = 0;i < 10; i++) {
					if(watchdos[i] != null) {
						try{
							//DataOutputStream out = new DataOutputStream(watchsocket[i].getOutputStream());
							watchdos[i].writeUTF("23");
							System.out.println("「23」を観客に送信");
							//out.close();
						}catch(IOException e) {
							e.printStackTrace();
							watchout(i);
						}
					}
				}
	}

	public String sendfield() {
		return grids;
	}

	public int setwatcher(String PN, DataOutputStream dos) {
		int flag = 0;
		int watchNo = 0;
		if(socketNo != 0 && flag == 0) {
			for(int i = 0; i < socketNo;i++) {
				if(watchdos[i] == null) {
					watchdos[i] = dos;
					flag = 1;
					watchNo = i;
				}
			}
		}
		if(flag == 0) {
			//watchdos[socketNo] = new DataOutputStream();
			watchdos[socketNo] = dos;
			watchNo = socketNo;
			socketNo++;
			flag++;
		}
		return watchNo;
	}

	public void watchout(int No) {
		watchdos[No] = null;
	}

	public void sendpass(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				//DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				out1.writeUTF("22");	//クライアントに送信
				System.out.println("「22」を送信");
				//out1.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				//DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				out2.writeUTF("22");	//クライアントに失敗を送信
				System.out.println("「22」を送信");
				//out2.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {


	}

}