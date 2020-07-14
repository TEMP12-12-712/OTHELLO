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
	private DataOutputStream out1 = null, out2 = null;
	private DataOutputStream[] watchdos = new DataOutputStream[10];
	private int socketNo = 0;		//観戦者のソケット配列の数
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
				out1.writeUTF("true," + PlayerName2 + ",0");	//クライアントに送信
				System.out.println(PlayerName1 + "に「" + "true," + PlayerName2 + ",0」を送信");
				out2.writeUTF("true," + PlayerName1 + ",1");	//クライアントに送信
				System.out.println(PlayerName2 + "に「" + "true," + PlayerName1 + ",0」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("false");	//クライアントに失敗を送信
			}catch (IOException e) {
				e.printStackTrace();
			}
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
		for(int i = 0;i < socketNo; i++) {
			if(watchdos[i] != null) {
				watchdos[i] = null;
			}
		}
	}

	public void setfield(String PN, String field, String log) {//送信者と盤面を得て、相手に盤面とログを送信
		grids = field;								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("21," + grids + "," + log);	//クライアントに送信
				System.out.println(PlayerName1 + "に「21," + grids + "," + log + "」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("い");
			try {
				out2.writeUTF("21," + grids + "," +log);	//クライアントに失敗を送信
				System.out.println(PlayerName2 + "に「21," + grids + "," + log + "」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
		for(int i = 0;i < 10; i++) {
			if(watchdos[i] != null) {
				try{
					watchdos[i].writeUTF("21," + grids + "," + log);
					System.out.println("観客に「21," + grids + "," + log + "」を送信");
				}catch(IOException e) {
					e.printStackTrace();
					watchout(i);
				}
			}
		}
	}

	public void chat(String PN,String No, String chat) {//チャットの送信
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF(No +"," + chat);	//クライアントに送信
				System.out.println(PlayerName1 + "に「"+ No +"," + chat + "」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF(No + "," + chat);	//クライアントに失敗を送信
				System.out.println(PlayerName2 + "に「"+ No +"," + chat + "」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
		for(int i = 0;i < 10; i++) {
			if(watchdos[i] != null) {
				try{
					watchdos[i].writeUTF(No +"," + chat);
					System.out.println("観客に「" + No + "," + chat + "」を送信");
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
				out1.writeUTF("23");	//クライアントに送信
				System.out.println(PlayerName1 + "に「23」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("23");	//クライアントに失敗を送信
				System.out.println(PlayerName2 + "に「23」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
				for(int i = 0;i < 10; i++) {
					if(watchdos[i] != null) {
						try{
							watchdos[i].writeUTF("23");
							System.out.println("「23」を観客に送信");
						}catch(IOException e) {
							e.printStackTrace();
							watchout(i);
						}
					}
				}
	}

	public void sendgameout(String PN) {
		if(PN.equals(PlayerName1)) {
			deletePN1();
			if(PlayerName2 != null) {
				try {
					out2.writeUTF("26");	//クライアントに送信
					System.out.println(PlayerName2 + "に「26」を送信");
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			deletePN2();
			try {
				out1.writeUTF("26");	//クライアントに失敗を送信
				System.out.println(PlayerName1 + "に「26」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
				for(int i = 0;i < 10; i++) {
					if(watchdos[i] != null) {
						try{
							watchdos[i].writeUTF("26");
							System.out.println("「26」を観客に送信");
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
			watchdos[socketNo] = dos;
			watchNo = socketNo;
			socketNo++;
			flag++;
		}
		try {
			watchdos[watchNo].writeUTF(grids);
		}catch(IOException e) {
			e.printStackTrace();
		}

		return watchNo;
	}

	public void watchout(int No) {
		watchdos[No] = null;
	}

	public void sendpass(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("22");	//クライアントに送信
				System.out.println(PlayerName1 + "に「22」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("22");	//クライアントに失敗を送信
				System.out.println(PlayerName2 + "に「22」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {


	}

}