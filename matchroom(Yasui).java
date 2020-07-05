import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class matchroom {

	//部屋作成者のコンストラクタ
	public matchroom(String PN1, DataOutputStream O1) {
		PlayerName1 = PN1;
		this.out1 = O1;
	}

	private String grids;					//局面データ
	private String PlayerName1 = "";		//プレイヤ１(先手)
	private String PlayerName2 = "";		//プレイヤ２(後手)
	private DataOutputStream out1, out2;
	private String log;						//ログ
	private String[] view;						//観戦者
	private DataOutputStream[] out = new DataOutputStream[10];
	private int socketNo = 0;
	int flag;						//手番

	public void set1P(String PN1, DataOutputStream O1) {//プレイヤー1が決まる
		if(PlayerName1.equals("")) {
			PlayerName1 = PN1;
			out1 = O1;
		}
	}

	public void set2P(String PN2, DataOutputStream O2) {//プレイヤー２が決まる
		if(PlayerName2.equals("")) {
			PlayerName2 = PN2;
			out2 = O2;

			try {
				out1.writeUTF("true," + PlayerName2 + ",0");	//クライアントに送信
				out2.writeUTF("true," + PlayerName1 + ",1");	//クライアントに送信
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
		PlayerName1 = "";
		out1 = null;
	}

	public String getPN2() {
		return PlayerName2;
	}

	public void deletePN2() {
		PlayerName2 = "";
		out2 = null;
	}

	public void deletewatcher() {
		for(int i = 0;i < 10; i++) {
			if(!out[i].equals(null)) {
				out[i] = null;
			}
		}
	}

	public void setfield(String PN, String field) {//送信者と盤面を得て、相手に盤面を送信
		grids = field;								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("21" + grids);	//クライアントに送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("21" + grids);	//クライアントに失敗を送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
		for(int i = 0;i < 10; i++) {
			if(!out[i].equals(null)) {
				try{
					out[i].writeUTF("21" + grids);
				}catch(IOException e) {
					e.printStackTrace();
					watchout(i);
				}
			}
		}
	}

	public void setlog(String PN, String log) {//送信者と盤面を得て、相手に盤面を送信								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("25" + log);	//クライアントに送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("25" + log);	//クライアントに失敗を送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
		for(int i = 0;i < 10; i++) {
			if(!out[i].equals(null)) {
				try{
					out[i].writeUTF("25" + log);
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
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("23");	//クライアントに失敗を送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		//観客にも送る
				for(int i = 0;i < 10; i++) {
					if(!out[i].equals(null)) {
						try{
							out[i].writeUTF("23");
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

	public int setwatcher(String PN, DataOutputStream O) {
		int flag = 0;
		int watchNo = 0;
		if(socketNo != 0 && flag == 0) {
			for(int i = 0; i < socketNo;i++) {
				if(out[i].equals(null)) {
					out[i] = O;
					flag = 1;
					watchNo = i;
				}
			}
		}
		if(flag == 0) {
			out[socketNo] = O;
			watchNo = socketNo;
			socketNo++;
			flag++;
		}
		return watchNo;
	}

	public void watchout(int No) {
		out[No] = null;
	}

	public void sendpass(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("23");	//クライアントに送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("23");	//クライアントに失敗を送信
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {




	}

}