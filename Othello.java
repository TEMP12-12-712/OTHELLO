public class Othello {
	private int row = 8;	//オセロ版の縦横マス数(2の倍数のみ)
	private int black, white,can; //black,white...各色の数 can...これが1以上なら手番の石が置ける
	private boolean result; //turnの石が8方向にそれぞれとれるかのバイナリ(canPutの8方向メソッドの結果をすべてこれに入れる)
	private String opp; //canPutを使うときに一時的に相手の石を記憶するString
	private String blackName,whiteName; //各手番のプレイヤ名
	private int roomID; //部屋のID番号
	private String [] grids = new String [row * row]; //局面情報
	private String turn; //手番

	// コンストラクタ
	public Othello(){
		turn = "black"; //クロが先手
		for(int i = 0 ; i < row * row ; i++){
			grids[i] = "board"; //始めは石が置かれていない
			int center = row * row / 2;
			grids[center - row / 2 - 1] = "black";
			grids[center + row / 2    ] = "black";
			grids[center - row / 2    ] = "white";
			grids[center + row / 2 - 1] = "white";
		}
		canPutGrids(); //最初に黒が置けるマスを明示
	}

	// メソッド
	public String checkWinner(){	// 勝敗を判断
		if(black > white) return "black";
		else if(black < white) return "white";
		else return "draw";
	}
	public void setBlackName(String name) {
		blackName = name;
	}
	public String getBlackName() {
		return blackName;
	}
	public void setWhiteName(String name) {
		whiteName = name;
	}
	public String getWhiteName() {
		return whiteName;
	}
	public void setRoomID(int number) {
		roomID = number;
	}
	public int getRoomID() {
		return roomID;
	}
	public String getTurn(){ // 手番情報を取得
		return turn;
	}
	public void resetGrids() { //局面情報を初期化
		turn = "black"; //クロが先手
		for(int i = 0 ; i < row * row ; i++){
			grids[i] = "board"; //始めは石が置かれていない
			int center = row * row / 2;
			grids[center - row / 2 - 1] = "black";
			grids[center + row / 2    ] = "black";
			grids[center - row / 2    ] = "white";
			grids[center + row / 2 - 1] = "white";
		}
		canPutGrids(); //最初に黒が置けるマスを明示
	}
	public void setGrids(String[] setting) { //局面情報を入手
		grids = setting;
	}
	public String [] getGrids(){ // 局面情報を取得
		return grids;
	}
	public int getBlack() { //黒石の数を取得
		black = 0;//カウントリセット
		for(int i = 0; i < row * row; i++) {
			if(grids[i] == "black") black++;//gridsの中身がblackならインクリメント
		}
		return black;//出力
	}
	public int getWhite() { //白石の数を取得
		white = 0;
		for(int i = 0; i < row * row; i++) {
			if(grids[i] == "white") white++;
		}
		return white;
	}
	public void changeTurn(){ //手番を変更
		if(turn == "black") {
			turn = "white";
		}
		else turn = "black";
	}
	public boolean isGameover(){ // 対局終了を判断
		//これだと途中でどちらも置けなくなる場合がカバーできない
		if(black + white == row * row || black == 0 || white == 0) return true;//黒＋白が盤面の数か、黒か白がなくなったらtrue
		else return false;
	}
	public void canPutGrids() { //turnの石が置けるコマをgridsに反映
		for(int i = 0; i < row * row; i++) {
			//System.out.println("LoopOnBoard " + i +" = " + grids[i]);
			if(grids[i] == "canPut") grids[i] = "board"; //相手のターンの時の情報をリセット
		}
		for(int i = 0; i < row * row; i++) {
			if(grids[i] == "board") { //もしgrids[i]のマスが空いていて
				if(canPut(i)) { //そこにturnの石が置けるなら
					grids[i] = "canPut"; //置けるという目印に変換する
				}
			}
		}
	}
	public boolean canPut(int cell) { //そのマスが置けるか判断
		can = 0;//置ける数のリセット 0なら置けない 1以上なら置ける
		//8方向判定
		if(canPutLeftUp(cell))can++;//左上方向のマスをとれるか
		if(canPutUp(cell))can++;//上方向
		if(canPutRightUp(cell))can++;//右上方向
		if(canPutLeft(cell))can++;//左方向
		if(canPutRight(cell))can++;//右方向
		if(canPutLeftDown(cell))can++;//左下方向
		if(canPutDown(cell))can++;//下方向
		if(canPutRightDown(cell))can++;//右下方向

		if(can >= 1) return true; //置ける方向が1個以上あるならtrue
		else return false;
	}
	public void turnStone(int cell) { //裏返るはずの石をひっくり返す
		//System.out.println("WillTurnUsing" + cell);
		//8方向判定
		if(canPutLeftUp(cell))	turnLeftUp(cell); //左上方向に裏返せる石をひっくり返す
		if(canPutUp(cell)) 		turnUp(cell); //上方向
		if(canPutRightUp(cell))	turnRightUp(cell); //右上方向
		if(canPutLeft(cell))	turnLeft(cell); //左方向
		if(canPutRight(cell))	turnRight(cell); //右方向
		if(canPutLeftDown(cell))turnLeftDown(cell); //左下方向
		if(canPutDown(cell))	turnDown(cell); //下方向
		if(canPutRightDown(cell))turnRightDown(cell); //右下方向
	}

	public boolean putStone(int i, String color, boolean effect_on){ // (操作を)局面に反映
		if(grids[i] == "canPut"){
			grids[i] = color;//gridsがcanPutなら置く
			turnStone(i);//置くことによって裏返る石をひっくり返す
			return true;
		}
		else return false;
	}
	public int getRow(){ //縦横のマス数を取得
		return row;
	}

	//canPut8方向
		private boolean canPutLeftUp(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell / row >= 2 && cell % row >= 2) { //2行目、2列目以降じゃないと左上には取れない
				int check = cell - row - 1; //1個左上の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do {
						if(grids[check] == opp) check = check - row - 1; //現在地が相手の石なら1つ左上のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループ終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check >= 0 && check % row != row-1); //checkが枠外、または右端の列に行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutUp(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell / row >= 2) { //そもそも2行目以降じゃないと上には取れない
				int check = cell - row; //1個上の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do{
						if(grids[check] == opp) check = check - row; //現在地が相手の石なら1つ上のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループを終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check >= 0); //checkが枠外まで行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutRightUp(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell / row >= 2 && cell % row <= row-3) { //2行目以降、row-3列目以前じゃないと右上には取れない
				int check = cell - row + 1; //1個右上の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do {
						if(grids[check] == opp) check = check - row + 1; //現在地が相手の石なら1つ右上のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループ終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check >= 0 && check % row != 0); //checkが枠外、または左端の列に行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutLeft(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell % row >= 2) { //そもそも2列目以降じゃないと左には取れない
				int check = cell - 1; //1個左の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do{
						if(grids[check] == opp) check--; //現在地が相手の石なら1つ左のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループを終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check >= 0 && check % row != row-1); //checkが枠外か右端の列まで行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutRight(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell % row <= row-3) { //そもそもrow-3列目以前じゃないと右には取れない
				int check = cell + 1; //1個右の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do{
						if(grids[check] == opp) check++; //現在地が相手の石なら1つ右のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループを終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check < row*row && check % row != 0); //checkが左端の列まで行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutLeftDown(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell / row <= row-3 && cell % row >= 2) { //row-3行目以前、3列目以降じゃないと左下には取れない
				int check = cell + row - 1; //1個左下の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do {
						if(grids[check] == opp) check = check + row - 1; //現在地が相手の石なら1つ左下のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループ終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check < row*row && check % row != row-1); //checkが枠外、または右端の列に行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutDown(int cell) {
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell / row <= row-3) { //そもそもrow-3行目以前じゃないと下には取れない
				int check = cell + row; //1個下の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do{
						if(grids[check] == opp) check = check + row; //現在地が相手の石なら1つ前の列へ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループを終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check < row*row); //checkが枠外まで行っていたら終了
				}
			}
			return result;
		}
		private boolean canPutRightDown(int cell){
			result = false; //初期化、これがtrueになったら石が置ける
			if(cell / row <= row-3 && cell % row <= row-3) { //row-3行目以前、row-3列目以前じゃないと右下には取れない
				int check = cell + row + 1; //1個右下の番号を入手
				if(grids[check] != turn && grids[check] != "board" && grids[check] != "canPut") {
					//前のif文が突破されここに入るということはgrids[check]は相手の石
					opp = grids[check]; //相手の色を記憶
					do {
						if(grids[check] == opp) check = check + row + 1; //現在地が相手の石なら1つ右下のマスへ
						else if(grids[check] == turn) {
							result = true; //自分の石なら挟める
							break; //ループ終了
						}
						else break; //boardかcanPutなら挟めないので終了
					}while(check < row*row && check % row != 0); //checkが枠外、または左端の列に行っていたら終了
				}
			}
			return result;
		}
	//turnStone8方向
		private void turnLeftUp(int cell) {
			int turned = cell - row - 1; //1個左上のマス,turnとは反対の色のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned = turned - row - 1; //次のマスへ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnUp(int cell) {
			int turned = cell - row; //1個上のマス、turnとは反対の色の石のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned = turned - row; //次のマスへ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnRightUp(int cell) {
			int turned = cell - row + 1; //1個右上のマス,turnとは反対の色のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned = turned - row + 1; //次のマスへ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnLeft(int cell) {
			int turned = cell - 1; //1個左のマス、turnとは反対の色の石のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned--; //次の列へ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnRight(int cell) {
			int turned = cell + 1; //1個右のマス、turnとは反対の色の石のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned++ ; //次の列へ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnLeftDown(int cell) {
			int turned = cell + row - 1; //1個左下のマス,turnとは反対の色のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned = turned + row - 1; //次のマスへ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnDown(int cell) {
			int turned = cell + row; //1個下のマス、turnとは反対の色の石のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned = turned + row; //次の行へ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}
		private void turnRightDown(int cell) {
			int turned = cell + row + 1; //1個右下のマス,turnとは反対の色のはず
			do {
				grids[turned] = turn; //ひっくり返す
				turned = turned + row + 1; //次のマスへ
			}while(grids[turned] != turn); //turnの石に当たるまで繰り返す
		}

}