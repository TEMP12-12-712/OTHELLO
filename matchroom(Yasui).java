import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class matchroom {

	//�����쐬�҂̃R���X�g���N�^
	public matchroom(String PN1, DataOutputStream O1) {
		PlayerName1 = PN1;
		this.out1 = O1;
	}

	private String grids;					//�ǖʃf�[�^
	private String PlayerName1 = "";		//�v���C���P(���)
	private String PlayerName2 = "";		//�v���C���Q(���)
	private DataOutputStream out1, out2;
	private String log;						//���O
	private String[] view;						//�ϐ��
	private DataOutputStream[] out = new DataOutputStream[10];
	private int socketNo = 0;
	int flag;						//���

	public void set1P(String PN1, DataOutputStream O1) {//�v���C���[1�����܂�
		if(PlayerName1.equals("")) {
			PlayerName1 = PN1;
			out1 = O1;
		}
	}

	public void set2P(String PN2, DataOutputStream O2) {//�v���C���[�Q�����܂�
		if(PlayerName2.equals("")) {
			PlayerName2 = PN2;
			out2 = O2;

			try {
				out1.writeUTF("true," + PlayerName2 + ",0");	//�N���C�A���g�ɑ��M
				out2.writeUTF("true," + PlayerName1 + ",1");	//�N���C�A���g�ɑ��M
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
		//�ϋq�ɂ�����
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
		//�ϋq�ɂ�����
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
		//�ϋq�ɂ�����
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




	}

}