public class ServerRoom1 {


	int[][] grids;					//局面データ
	String playerN1;		//プレイヤ１
	String playerN2;		//プレイヤ２
	String log;						//ログ
	int[] view;						//観戦者情報
	int flag;						//手番


	public void updateDisp(int[][] d) {	//盤面保存
		grids=d;

	}

	public void updatelog(String s) {		//ログ保存
		log=s;
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ



	}

}

