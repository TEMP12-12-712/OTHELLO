import java.net.*;
import java.io.*;

public class EchoServer {
    static int port = 10000;
    
    public static void main(String[] args) {
		try {
	    	ServerSocket server = new ServerSocket(port);
	    	Socket sock = null;
	    	System.out.println("�T�[�o���N�����܂���");
			while(true) {
				try {
		    		sock = server.accept(); // �N���C�A���g����̐ڑ���҂�
		    		System.out.println("�N���C�A���g�Ɛڑ����܂���");
					BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		    		PrintWriter out = new PrintWriter(sock.getOutputStream());
		    		String command;
		    		while(true) {
		    			command = in.readLine();
		    			if(command != null){
		    			System.out.println("��M�F" +command);
		    			String[] c = command.split(",",0);
		    			switch( c[0] ) {
						//���O�C������
						case "1":
							out.println("true");
							//out.println("name");
							//out.println("pass");
							out.flush();
							System.out.println("���M�Ftrue");
							//System.out.println("���M�Fname");
							//System.out.println("���M�Fpass");
							break;
						//�V�K�o�^����
						case "0":
							out.println("true");
							//out.println("false");
							out.flush();
							System.out.println("���M�Ftrue");
							break;
						//�����_���}�b�`���O�v��
						case "2":
							try{Thread.sleep(2000);}catch(InterruptedException e){}
							out.println("true,Opponent,0");
							//out.println("false");
							out.flush();
							System.out.println("���M�Ftrue,Opponent,0");
							//System.out.println("���M�Ffalse");
							break;
						//�}�b�`���O�L�����Z������
						case "CancelMatch":
							break;
						//�Ֆ�
						case "21":
							break;
						//����
						case "23":
							break;
						//�I��
						case "24":
							break;
						//�`���b�g
						case "25":
							break;
						//�����L�^�v��
						case "3":
							out.println("12,3,1,0");
							out.flush();
							System.out.println("���M�FTotalRecord");
							break;
						//�ΐl�ʋL�^�v��
						case "4":
							out.println("4,7,0,2");
							out.flush();
							System.out.println("���M�FIdRecord");
							break;
						//�������쐬
						case "5":
							try{Thread.sleep(2000);}catch(InterruptedException e){}
							out.println("1,Opponent");
							out.flush();
							System.out.println("���M�F1,Opponent");
							break;
						//���������X�g�v��
						case "7":
							out.println("0.�v���C���P.ture.3,1.�v���C���Q.false.1,2.�v���C���R.true.5,3.�v���C���S.true.1,4.�v���C���T.false.1,5.�v���C���U.true.3,6.�v���C���V.true.3,7.�v���C���W.true.3,8.�v���C���X.true.3,9.�v���C���P�O.true.3,10.�v���C���P�P.true.3,11.�v���C���P�Q.true.3");
							out.flush();
							System.out.println("���M�FKeyroomList");
							break;
						//�������ւ̓����v��
						case "8":
							out.println("true,1,Opponent");
							//out.println("false");
							out.flush();
							System.out.println("���M�Ftrue,1,Opponent");
							//System.out.println("���M�Ffalse");
							break;
						//�ϐ핔�����X�g
						case "9":
							out.println("0.�g��.���A���[.12.5,1.�e�B�t�@�j�[.���i.17.36");
							out.flush();
							System.out.println("���M�FWatchroomList");
							break;
						//�ϐ핔���ւ̓����v��
						case "91":
							out.println("1,2,0,0,0,1,2,0,1,2,0,2,1,2,1,1,1,1,1,2,1,0,0,0,0,0,0,0,2,0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,0,0,0,0,0,0,0,0,1,1,0,2,1,2,1");
							//out.println("false");
							out.flush();
							System.out.println("���M�FTable");
							//System.out.println("���M�Ffalse");
							break;
						//���A�N�V����
						case "92":
							break;
						case "100":
							//���O�A�E�g����
							System.out.println("�N���C�A���g�����O�A�E�g���܂���");
							break;
						default:
							break;
						}
						}
					}
		    	}
		    	catch (IOException e) {
		    		sock.close(); // �N���C�A���g����̐ڑ���ؒf
		    		System.out.println("�N���C�A���g���ؒf���܂���");
				}
		    }
		}
		catch (IOException e) {
	    	System.err.println(e);
		}
	}
}