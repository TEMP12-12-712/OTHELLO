public class player {

	private String myName = ""; //プレイヤ名
	private String myPass = "";
	private String myColor = ""; //先手後手情報(白黒)
	private String Connection = "";
	private String Configuration = "";
	private String Enter = "";
	private String Message = "";
	private int Operation;

	// メソッド
	public void setName(String name){ // プレイヤ名を受付
		myName = name;
	}
	public String getName(){	// プレイヤ名を取得
		return myName;
	}
	public void setPass(String pass) {
		myPass = pass;
	}
	public String getPass() {
		return myPass;
	}
	public void setColor(String c){ // 先手後手情報の受付
		myColor = c;
	}
	public String getColor(){ // 先手後手情報の取得
		return myColor;
	}
	public void setConnection(String conn){
		Connection = conn;
	}
	public String getConnection(){
		return Connection;
	}
	public void setConfiguration(String conf){
		Configuration = conf;
	}
	public String getConfiguration() {
		return Configuration;
	}
	public void setEnter(String e) {
		Enter = e;
	}
	public String getEnter() {
		return Enter;
	}
	public void setMessage(String m) {
		Message = m;
	}
	public String getMessage() {
		return Message;
	}
	public void setOperation(int o) {
		Operation = o;
	}
	public int getOperation() {
		return Operation;
	}
}