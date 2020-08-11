import java.io.DataOutputStream;
import java.io.IOException;

public class secretroom{

	public secretroom(String PN1, DataOutputStream dos1, String Password, String Chatflag, String Timeflag) {
		PlayerName1 = PN1;
		out1 = dos1;
		pass = Password;
		chatflag = Chatflag;
		timeflag = Timeflag;
	}

	private String pass;				//パスワード
	private String PlayerName1 = null, PlayerName2 = null;
	private DataOutputStream out1 = null, out2 = null;
	private String chatflag;		//チャットの有無
	private String timeflag;		//時間

	public String getpass() {
		return pass;
	}

	//一度作られた部屋にプレイヤー１を入れる
	public void set1P(String PN1, DataOutputStream dos1, String Password, String Chatflag, String Timeflag) {
		if(PlayerName1 == null) {
			PlayerName1 = PN1;
			out1 = dos1;
			pass = Password;
			chatflag = Chatflag;
			timeflag = Timeflag;
		}
	}

	public void set2P(String PN2, DataOutputStream dos2) {//プレイヤー２が決まる
		if(PlayerName2 == null) {
			PlayerName2 = PN2;
			out2 = dos2;
			try {
				out1.writeUTF("true,"+ PlayerName2);	//クライアントに送信
				System.out.println(PlayerName1 +"に「true,"+ PlayerName2 +"」を送信");
				out2.writeUTF("true," + PlayerName1);	//クライアントに送信
				System.out.println(PlayerName1 +"に「true,"+ PlayerName2 +"」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getPN1() {
		return PlayerName1;
	}

	public void deletePN1() {
		PlayerName1 = null;
	}

	public String getPN2() {
		return PlayerName2;
	}

	public void deletePN2() {
		PlayerName2 = null;
	}

	public String getlistData() {
		String str = PlayerName1 + "." + chatflag + "." + timeflag + ",";
		return str;
	}

	public void setfield(String PN, String field, String log) {//送信者と盤面を得て、相手に盤面を送信
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("21," + field + "," +log);	//クライアントに送信
				System.out.println(PlayerName1 +"に「21," + field + "," +log +"」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("21," + field + "," +log);
				System.out.println(PlayerName2 +"に「21," + field + "," +log +"」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void chat(String PN, String chat) {//送信者と盤面を得て、相手に盤面を送信								//盤面を保持
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("25," + chat);	//クライアントに送信
				System.out.println(PlayerName1 +"に「21," + chat  +"」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("25," + chat);	//クライアントに失敗を送信
				System.out.println(PlayerName2 +"に「21," + chat  +"」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendgiveup(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("23");	//クライアントに送信
				System.out.println(PlayerName1 +"に「23」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("23");
				System.out.println(PlayerName2 +"に「23」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendgameout(String PN) {
		if(PN.equals(PlayerName1)) {
			if(PlayerName2 != null){
				try {
					out2.writeUTF("26");	//クライアントに送信
					System.out.println(PlayerName2 + "に「26」を送信");
				}catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				deletePN1();
			}
		}else {
			try {
				out1.writeUTF("26");	//クライアントに失敗を送信
				System.out.println(PlayerName1 + "に「26」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendpass(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("22");	//クライアントに送信
				System.out.println(PlayerName1 +"に「22」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("22");
				System.out.println(PlayerName2 +"に「22」を送信");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
	}

}