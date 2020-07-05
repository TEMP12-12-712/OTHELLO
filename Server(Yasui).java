import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 public class Server {

  public static final int port = 10000;  //�|�[�g�ԍ��P�O�O�O�O
  public static StringBuffer mrlist = new StringBuffer("");
  public static StringBuffer srlist = new StringBuffer("");
  public static secretroom[] sr = new secretroom[1000];
  public static int srNo = 0;			//��new�ō��ꂽ������+1
  public static matchroom[] mr = new matchroom[1000];
  public static int mrNo = 0;			//��new�ō��ꂽ������+1


  public static void main(String args[]) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server���N�����܂���(port="
                         + serverSocket.getLocalPort() + ")");
      while (true) {
        Socket socket = serverSocket.accept();
        new EchoThread(socket).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {}
    }
  }
}

class EchoThread extends Thread {

  private Socket socket;
  private String PlayerName = "";
  private int myroomNo = 1001;
  private int myroom = 0;	//0�Ȃ�ΐ킵�ĂȂ��A�P�Ȃ�}�b�`�A�Q�Ȃ献�����}�b�`
  private int watchNo = 1001;	//�ϐ킵�Ă���Ƃ��̔ԍ�
  DataInputStream in;
  DataOutputStream out;


  public EchoThread(Socket socket) {
    this.socket = socket;
    System.out.println("�ڑ�����܂��� "
                       + socket.getRemoteSocketAddress());
  }

  public void run() {
    try {
    	in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
    	while(true) {
    		String ClientMessage = in.readUTF();		//�N���C�A���g����f�[�^��M
    		System.out.println("�N���C�A���g"+PlayerName+"����u"+ClientMessage + "�v����M");
    		String Return = check(ClientMessage);
    		if(!Return.equals("")) {
    			out.writeUTF(Return);	//�N���C�A���g�ɑ��M
    			System.out.println(Return);
    		}
        }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (socket != null) {
          socket.close();
        out.close();
  		in.close();
        }
      } catch (IOException e) {}
      System.out.println("�ؒf����܂��� "
                         + socket.getRemoteSocketAddress());
    }
  }

  public String check(String message) {		//�N���C�A���g���炫�����߂̐擪�ɂ��鐔�����݂ă��\�b�h���Ăяo��
	  String[] check = message.split(",", 0);
	  String Re = "";
	  switch(check[0]) {
	  case "0":		//�V�K�o�^|resister
		  Re = resister(check[1], check[2]);	//����true�F���sfalse
		  if(Re.equals("true")) {
			  PlayerName = check[1];
		  }
		  break;

	  case "1":		//���O�C��|rogin
		  Re = login(check[1], check[2]);		//����true:�v���C���[�����Ȃ�name:�p�X���[�h���Ⴄpass
		  break;

	  case "2":		//�}�b�`���O|match
		  match();
		  break;

	  case "21":		//�Ֆʎ�M
		  field(check[1]);
		  break;

	  case "23":		//����
		  giveup(check[1]);
		  break;

	  case "24":		//�ΐ�I��
		  newrecord(PlayerName, check[1]);
		  break;

	  case "25":		//�`���b�g
		  chat(check[1]);
		  break;

	  case "3":		//�ΐ�L�^|record
		  Re = record(PlayerName);
		  break;

	  case "5":		//�������쐬|make
		  make(PlayerName, check[1], check[2], check[3]);//�p�X���[�h�A�`���b�g�t���O�A���ԃt���O
		  break;

	  case "6":		//�������폜|delete
		  delete();
		  break;

	  case "7":		//���������X�g�̑��M|list
		  Re = list();
		  break;

	  case "8":		//����������|enterroom
		  enterroom(check[1], check[2]);//�������A�p�X���[�h
		  break;

	  case "9":		//�ΐ풆�̕������X�g����M|watchroomlist
		  Re = watchroomlist();
		  break;

	  case "91":		//�ϐ�|watch
		System.out.print("check\n");
		  watch(check[1]);
		  break;

	  case "92":		//���A�N�V����|reaction
		  //����
		  break;

	  default:

	  }
	  return Re;
  }

  public String resister(String PN, String PW) { 		//0:�V�K�o�^
		String judge = "true";
		try {
			File file = new File("C:\\Users\\rocky\\Documents\\��w\\�v���W�F�N�g���[�j���O\\����\\Data\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str;
				while((str = br.readLine()) != null) {
					String[] check = str.split(",", 0);
					if(check[0].equals(PN)) {
						judge = "false";
					}
				}
				br.close();

			}else {
				System.out.println("�t�@�C���Ȃ�");
			}

			if(judge.equals("true")) {
				FileWriter fr = new FileWriter(file, true);
				fr.write(PN + "," + PW + ",0,0,0,0\n");
				fr.close();
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return judge;
	}

  	//1:���O�C���p
	public String login(String PN, String PW) {
		String judge = "name";
		try {
			File file = new File("C:\\Users\\rocky\\Documents\\��w\\�v���W�F�N�g���[�j���O\\����\\Data\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str;
				while((str = br.readLine()) != null) {
					//System.out.println(str);
					String[] check = str.split(",", 0);
					//System.out.println(check[0]);
					if(check[0].equals(PN)) {
						judge = "pass";
						if(check[1].equals(PW)) {
							judge = "true";
							PlayerName = PN;
						}
					}
				}
				br.close();

			}else {
				System.out.println("�t�@�C���Ȃ�");
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return judge;
	}

	//2:�}�b�`���O������
	public void match() {
		if(Server.mrNo != 0) {
			if (myroomNo == 1001) {
				//��l�������Ȃ��������������炻���ɓ���
				for(int i = 0;i < Server.mrNo; i++) {
					if (!Server.mr[i].getPN1().equals("") && Server.mr[i].getPN2().equals("")) {
						Server.mr[i].set2P(PlayerName,  out);
						myroomNo = i;
						break;
					}
				}
			}
			if (myroomNo == 1001) {
				//��l�������Ȃ������͂Ȃ�����
				//��l�Ƃ����Ȃ��������������炻�̍ŏ��̕�����
				for(int i = 0; i < Server.mrNo; i++) {
					if (Server.mr[i].getPN1().equals("")) {
						Server.mr[i].set1P(PlayerName,  out);
						myroomNo = i;
						break;
					}
				}
			}
		}
		//�󂫕������Ȃ���ΐV�������������
		if (myroomNo == 1001) {
			Server.mr[Server.mrNo] = new matchroom(PlayerName, out);
			myroomNo = Server.mrNo;
			Server.mrNo++;
			//�ŁA������1P�ɂ���񂶂�Ȃ��́H
			Server.mr[myroomNo].set1P(PlayerName, out);
		}
		myroom = 1;
		System.out.println(PlayerName + "," + myroomNo + ",ok");
	}


	//21:�Ֆʂ𑗐M����
	public void field(String field) {		//�Ֆʂ���M�B�ΐ푊��ɑ���
		//�����Ă����v���C���[�łȂ����̃v���C���[�Ɗϋq�ɔՖʂ𑗐M
		if(myroom == 1) {
			Server.mr[myroomNo].setfield(PlayerName, field);
		}else if(myroom == 2) {
			Server.sr[myroomNo].setfield(PlayerName, field);
		}
	}

	//22:�p�X������
	public void passing() {
		if(myroom == 1) {
			Server.mr[myroomNo].sendpass(PlayerName);
		}else {
			Server.sr[myroomNo].sendpass(PlayerName);
		}
	}

	//23:����
	public void giveup(String message) {
		//�����ЂƂ�̃v���C���[�Ɗϋq�ɓ����������Ƃ�`����
		if(myroom == 1) {
			Server.mr[myroomNo].sendgiveup(PlayerName);
		}else if(myroom == 2) {
			Server.sr[myroomNo].sendgiveup(PlayerName);
		}
		newrecord(PlayerName, "4");
	}

	//24:�΋ǂ��I��
	public void newrecord(String PN, String flag) {//flag = 1�Ȃ珟���A2�Ȃ畉���A3�Ȃ���������A4�Ȃ瓊��
		String str = "";
		StringBuffer sb = new StringBuffer("");
		try {
			File file = new File("C:\\Users\\rocky\\Documents\\��w\\�v���W�F�N�g���[�j���O\\����\\Data\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				int win, lose, draw, giveup;
				while((str = br.readLine()) != null) {
					String[] check = str.split(",", 0);

					if (check[0].equals(PN)) {
						switch(flag) {
							case "1":
								win = Integer.parseInt(check[2]);
								win++;
								check[2] = String.valueOf(win);
								break;

							case "2":
								lose = Integer.parseInt(check[3]);
								lose++;
								check[3] = String.valueOf(lose);
								break;

							case "3":
								draw = Integer.parseInt(check[4]);
								draw++;
								check[4] = String.valueOf(draw);
								break;

							case "4":
								giveup = Integer.parseInt(check[5]);
								giveup++;
								check[5] = String.valueOf(giveup);
								break;

							default:

						}
						str = check[0] + check[1] + check[2] + check[3] + check[4] + check[5];
						sb.append(str);
						sb.append(System.getProperty("line.separator"));
					} else {
						sb.append(str);
						sb.append(System.getProperty("line.separator"));
					}
				}
				FileWriter fw = new FileWriter(file);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				pw.println(sb.toString());
				pw.close();
				br.close();

			}else {
				System.out.println("�t�@�C���Ȃ�");
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
	//System.out.println(sb.toString());
	//�����̃v���C���[������ɂ���B
		if(myroom == 1) {
			Server.mr[myroomNo].deletePN1();
			Server.mr[myroomNo].deletePN2();
			Server.mr[myroom].deletewatcher();
		}else if(myroom == 2) {
			Server.sr[myroomNo].deletePN1();
			Server.sr[myroomNo].deletePN2();
		}
		//myroomNo��myroom�����
		myroomNo = 1001;
		myroom = 0;
	}

	//25:�`���b�g
	public void chat(String chat){
		if(myroom == 1) {
			Server.mr[myroomNo].setlog(PlayerName, chat);
		}else {
			Server.sr[myroomNo].setlog(PlayerName, chat);
		}
	}

	//3:�ΐ�L�^���{��
	public String record(String PN) {
		String PD = "no";
		try {
			File file = new File("C:\\Users\\rocky\\Documents\\��w\\�v���W�F�N�g���[�j���O\\����\\Data\\PlayerData.txt");
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str;
				while((str = br.readLine()) != null) {
					String[] check = str.split(",", 0);
					//System.out.println(str);
					if(check[0].equals(PN)) {
						PD = check[2] +"," + check[3] +","+ check[4] +","+ check[5];
						break;
					}
				}
				br.close();
			}else {
				System.out.println("�t�@�C���Ȃ�");
			}
		}
		catch(IOException e){
				e.printStackTrace();
		}
		return PD;
	}

	//5:�������̍쐬
	public void make(String PN, String password, String chatflag, String timeflag) {
		if(Server.srNo != 0) {
			for(int i = 0; i < Server.srNo;i++) {
				if(Server.sr[i].getPN1().equals("")) {
					Server.sr[i].set1P(PN, out, password, chatflag, timeflag);
					myroomNo = i;
				}
			}
		}
		if(myroomNo == 1001) {
			Server.sr[Server.srNo] = new secretroom(PN, out, password, chatflag, timeflag);
			myroomNo = Server.srNo;
			Server.srNo++;
			myroom = 2;		//�������ɂ�����
		}
	}

	//6:����������v���C���[�l�[�����폜
	public void delete() {
		Server.sr[myroomNo].deletePN1();
	}

	//7:���������X�g�̉{��
	public String list() {
		String str = "no";
		if(Server.srNo != 0) {
			for(int i = 0;i < Server.srNo; i++) {
				//�v���C���[�P�݂̂̌����������X�g�A�b�v
				if(!Server.sr[i].getPN1().equals("") && Server.sr[i].getPN2().equals("")) {
					Server.srlist.append(Integer.toString(i) + "." + Server.sr[i].getlistData());
				}
			}
			if(!Server.srlist.toString().equals("")) {
				str = Server.srlist.toString();
			}
		}
		System.out.println(str);
		return str;
	}

	//8:�������ɓ���
	public void enterroom(String roomNo, String pass) {
		int No = Integer.parseInt(roomNo);
		String password = Server.sr[No].getpass();
		System.out.println(password +","+ pass);
		if(pass.equals(password)) {
			myroomNo = No;
			Server.sr[myroomNo].set2P(PlayerName, out);
			myroom = 2;		//�������ɂ�����
		}else {
			try{
				out.writeUTF("false");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	//9:�ϐ핔�����X�g
	public String watchroomlist() {
		String field;
		String str = "no";
		char a = '1';
		char b = '2';
		int white = 0;
		int black = 0;
		for(int i = 0;i < Server.mrNo; i++) {
			//�v���C���[������������������X�g�A�b�v
			if(!Server.mr[i].getPN1().equals("") && !Server.mr[i].getPN2().equals("")) {
				field = Server.mr[i].sendfield();
				for(int j = 0; j < field.length();i++) {
					if(field.charAt(i) == a) {
						black++;
					}else if(field.charAt(i) == b) {
						white++;
					}
				}
				Server.mrlist.append(Integer.toString(i) + "." + Server.sr[i].getlistData() + "."
						+ Integer.toString(black) + "." + Integer.toString(white) + ",");
			}
		}
		if(!Server.mrlist.toString().equals("")) {
			str = Server.mrlist.toString();
		}
		return str;
	}

	//91:�ϐ����
	public void watch(String roomNumber) {
		watchNo = Server.mr[Integer.valueOf(roomNumber)].setwatcher(PlayerName, out);
		myroomNo = Integer.parseInt(roomNumber);
	}

	//92:�ϋq�����A�N�V�����𑗂�
	public void reaction() {

	}

	//93�ϐ�ގ�
	public void watchout() {
		Server.mr[myroomNo].watchout(watchNo);
	}



}