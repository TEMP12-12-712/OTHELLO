
public class Player {

	private String myName = ""; //プレイヤ名
	private String myPass = ""; //パスワード
	private String myColor = "-1"; //先手後手情報(白黒)
	private boolean myChat = true;//チャットの有無
	private int myTime = -1;//制限時間
	private boolean myAssist = true;//アシストの有無
	private boolean myShowlog = true;//ログ表示の有無
	private boolean standing = false;//観戦モード

	// メソッド
	public void setName(String name){ // プレイヤ名を受付
		myName = name;
	}
	public String getName(){	// プレイヤ名を取得
		return myName;
	}
	public void setPass(String pass){ // プレイヤ名を受付
		myPass = pass;
	}
	public String getPass(){	// プレイヤ名を取得
		return myPass;
	}
	public void setColor(String c){ // 先手後手情報の受付
		myColor = c;
	}
	public String getColor(){ // 先手後手情報の取得
		return myColor;
	}
	public void setChat(boolean chat){ // チャットの有無の受付
		myChat = chat;
	}
	public boolean getChat(){ // チャットの有無の取得
		return myChat;
	}
	public void setTime(int time){ // 制限時間の受付
		myTime = time;
	}
	public int getTime(){ // 制限時間の取得
		return myTime;
	}
	public void changeAssist(){ // アシストの有無の変更
		if(myAssist==true) myAssist = false;
		else if(myAssist==false) myAssist = true;
	}
	public void setAssist(boolean assist){ // アシストの有無を受付
		myAssist = assist;
	}
	public boolean getAssist(){ // アシストの有無の取得
		return myAssist;
	}
	public void setShowlog(boolean showlog){ // ログ表示の有無を受付
		myShowlog = showlog;
	}
	public boolean getShowlog(){ // ログ表示の有無の取得
		return myShowlog;
	}
	public void beStand(boolean std){
		standing = std;
	}
	public boolean isStand(){
		return standing;
	}
}