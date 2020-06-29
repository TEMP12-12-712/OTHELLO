import java.net.*;
import java.io.*;

public class EchoServer {
    static int port = 10000;
    
    public static void main(String[] args) {
		try {
	    	ServerSocket server = new ServerSocket(port);
	    	Socket sock = null;
	    	System.out.println("サーバが起動しました");
			while(true) {
				try {
		    		sock = server.accept(); // クライアントからの接続を待つ
		    		System.out.println("クライアントと接続しました");
					DataInputStream in = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		    		DataOutputStream out = new DataOutputStream(sock.getOutputStream());
		    		String command;
		    		while(true) {
		    			command = in.readUTF();
		    			if(command != null){
		    			System.out.println("受信：" +command);
		    			String[] c = command.split(",",0);
		    			switch( c[0] ) {
						//ログイン処理
						case "1":
							out.writeUTF("true");
							//out.println("name");
							//out.println("pass");
							System.out.println("送信：true");
							//System.out.println("送信：name");
							//System.out.println("送信：pass");
							break;
						//新規登録処理
						case "0":
							out.writeUTF("true");
							//out.println("false");
							System.out.println("送信：true");
							break;
						//ランダムマッチング要求
						case "2":
							try{Thread.sleep(2000);}catch(InterruptedException e){}
							out.writeUTF("true,Opponent,0");
							//out.println("false");
							System.out.println("送信：true,Opponent,0");
							//System.out.println("送信：false");
							break;
						//マッチングキャンセル処理
						case "CancelMatch":
							break;
						//盤面
						case "21":
							break;
						//投了
						case "23":
							break;
						//終了
						case "24":
							break;
						//チャット
						case "25":
							break;
						//総合記録要求
						case "3":
							out.writeUTF("12,3,1,0");
							System.out.println("送信：TotalRecord");
							break;
						//対人別記録要求
						case "4":
							out.writeUTF("4,7,0,2");
							System.out.println("送信：IdRecord");
							break;
						//鍵部屋作成
						case "5":
							try{Thread.sleep(2000);}catch(InterruptedException e){}
							out.writeUTF("1,Opponent");
							System.out.println("送信：1,Opponent");
							break;
						//鍵部屋リスト要求
						case "7":
							out.writeUTF("0.プレイヤ１.ture.3,1.プレイヤ２.false.1,2.プレイヤ３.true.5,3.プレイヤ４.true.1,4.プレイヤ５.false.1,5.プレイヤ６.true.3,6.プレイヤ７.true.3,7.プレイヤ８.true.3,8.プレイヤ９.true.3,9.プレイヤ１０.true.3,10.プレイヤ１１.true.3,11.プレイヤ１２.true.3");
							System.out.println("送信：KeyroomList");
							break;
						//鍵部屋への入室要求
						case "8":
							out.writeUTF("true,1,Opponent");
							//out.println("false");
							System.out.println("送信：true,1,Opponent");
							//System.out.println("送信：false");
							break;
						//観戦部屋リスト
						case "9":
							out.writeUTF("0.トム.メアリー.12.5,1.ティファニー.レナ.17.36");
							System.out.println("送信：WatchroomList");
							break;
						//観戦部屋への入室要求
						case "91":
							out.writeUTF("1,2,0,0,0,1,2,0,1,2,0,2,1,2,1,1,1,1,1,2,1,0,0,0,0,0,0,0,2,0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,0,0,0,0,0,0,0,0,1,1,0,2,1,2,1");
							//out.println("false");
							System.out.println("送信：Table");
							//System.out.println("送信：false");
							break;
						//リアクション
						case "92":
							break;
						case "100":
							//ログアウト処理
							System.out.println("クライアントがログアウトしました");
							break;
						default:
							break;
						}
						}
					}
		    	}
		    	catch (IOException e) {
		    		sock.close(); // クライアントからの接続を切断
		    		System.out.println("クライアントが切断しました");
				}
		    }
		}
		catch (IOException e) {
	    	System.err.println(e);
		}
	}
}