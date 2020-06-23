import java.net.Socket;

public class matchroom {

	//部屋作成者のコンストラクタ
	public matchroom(String PN1, Socket s2, String Pass) {

	}

	public matchroom(String PN2, Socket s2) {

	}

	int[][] grids;					//局面データ
	String playerN1;		//プレイヤ１(先手)
	String playerN2;		//プレイヤ２(後手)
	String log;						//ログ
	String[] view;						//観戦者
	int flag;						//手番


	public void updateDisp(int[][] d) {	//盤面保存
		grids=d;

	}

	public void updatelog(String s) {		//ログ保存
		log=s;
	}

	public static void main(String[] args) {




	}

}