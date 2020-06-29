
public class Player {

	private String myName = ""; //�v���C����
	private String myPass = ""; //�p�X���[�h
	private String myColor = "-1"; //�������(����)
	private boolean myChat = true;//�`���b�g�̗L��
	private int myTime = -1;//��������
	private boolean myAssist = true;//�A�V�X�g�̗L��
	private boolean myShowlog = true;//���O�\���̗L��
	private boolean standing = false;//�ϐ탂�[�h

	// ���\�b�h
	public void setName(String name){ // �v���C��������t
		myName = name;
	}
	public String getName(){	// �v���C�������擾
		return myName;
	}
	public void setPass(String pass){ // �v���C��������t
		myPass = pass;
	}
	public String getPass(){	// �v���C�������擾
		return myPass;
	}
	public void setColor(String c){ // �������̎�t
		myColor = c;
	}
	public String getColor(){ // �������̎擾
		return myColor;
	}
	public void setChat(boolean chat){ // �`���b�g�̗L���̎�t
		myChat = chat;
	}
	public boolean getChat(){ // �`���b�g�̗L���̎擾
		return myChat;
	}
	public void setTime(int time){ // �������Ԃ̎�t
		myTime = time;
	}
	public int getTime(){ // �������Ԃ̎擾
		return myTime;
	}
	public void changeAssist(){ // �A�V�X�g�̗L���̕ύX
		if(myAssist==true) myAssist = false;
		else if(myAssist==false) myAssist = true;
	}
	public void setAssist(boolean assist){ // �A�V�X�g�̗L������t
		myAssist = assist;
	}
	public boolean getAssist(){ // �A�V�X�g�̗L���̎擾
		return myAssist;
	}
	public void setShowlog(boolean showlog){ // ���O�\���̗L������t
		myShowlog = showlog;
	}
	public boolean getShowlog(){ // ���O�\���̗L���̎擾
		return myShowlog;
	}
	public void beStand(boolean std){
		standing = std;
	}
	public boolean isStand(){
		return standing;
	}
}