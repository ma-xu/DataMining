package BaggingAndBoosting.DataMining_AdaBoost;

/**
 * ������
 * 
 * @author maxu
 * 
 */
public class Point {
	// ����x���
	private int x;
	// ����y���
	private int y;
	// ����ķ������
	private int classType;
	//���˽ڵ㱻���?�������ʣ������ø������������Ϊ��ͬ�����Ȩ�ز�һ�����
	private double probably;
	
	public Point(int x, int y, int classType){
		this.x = x;
		this.y = y;
		this.classType = classType;
	}
	
	public Point(String x, String y, String classType){
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		this.classType = Integer.parseInt(classType);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getClassType() {
		return classType;
	}

	public void setClassType(int classType) {
		this.classType = classType;
	}

	public double getProbably() {
		return probably;
	}

	public void setProbably(double probably) {
		this.probably = probably;
	}
}
