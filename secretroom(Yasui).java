import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class secretroom{

	public secretroom(String PN1, DataOutputStream O1, String Password, String Chatflag, String Timeflag) {
		PlayerName1 = PN1;
		out1 = O1;
		pass = Password;
		chatflag = Chatflag;
		timeflag = Timeflag;
	}


	private String pass;				//�p�X���[�h
	private String PlayerName1 = "", PlayerName2 = "";
	private DataOutputStream out1 = null, out2 = null;
	private String grids;
	private String chatflag;		//�`���b�g�̗L��
	private String timeflag;		//����

	public String getpass() {
		return pass;
	}

	//��x���ꂽ�����Ƀv���C���[�P������
	public void set1P(String PN1, DataOutputStream O1, String Password, String Chatflag, String Timeflag) {
			PlayerName1 = PN1;
			out1 = O1;
			pass = Password;
			chatflag = Chatflag;
			timeflag = Timeflag;
	}

	public void set2P(String PN2, DataOutputStream O2) {//�v���C���[�Q�����܂�
		System.out.println("check1");
		if(PlayerName2.equals("")) {
			PlayerName2 = PN2;
			out2 = O2;
			System.out.println("check2");
			try {
				out1.writeUTF("true,"+ PlayerName2);	//�N���C�A���g�ɑ��M
				out2.writeUTF("true," + PlayerName1);	//�N���C�A���g�ɑ��M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("false");	//�N���C�A���g�Ɏ��s�𑗐M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String getPN1() {
		return PlayerName1;
	}

	public void deletePN1() {
		PlayerName1 = "";
	}

	public String getPN2() {
		return PlayerName2;
	}

	public void deletePN2() {
		PlayerName2 = "";
	}

	public String getlistData() {
		String str = PlayerName1 + "." + chatflag + "." + timeflag + ",";
		return str;
	}

	public void setfield(String PN, String field) {//���M�҂ƔՖʂ𓾂āA����ɔՖʂ𑗐M
		grids = field;								//�Ֆʂ�ێ�
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("21" + grids);	//�N���C�A���g�ɑ��M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("21" + grids);	//�N���C�A���g�Ɏ��s�𑗐M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setlog(String PN, String log) {//���M�҂ƔՖʂ𓾂āA����ɔՖʂ𑗐M								//�Ֆʂ�ێ�
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("25" + log);	//�N���C�A���g�ɑ��M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("25" + log);	//�N���C�A���g�Ɏ��s�𑗐M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendgiveup(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("23");	//�N���C�A���g�ɑ��M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("23");	//�N���C�A���g�Ɏ��s�𑗐M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendpass(String PN) {
		if(PN.equals(PlayerName2)) {
			try {
				out1.writeUTF("23");	//�N���C�A���g�ɑ��M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				out2.writeUTF("23");	//�N���C�A���g�Ɏ��s�𑗐M
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}