package Others.DataMining_KDTree;

/**
 * ������
 * 
 * @author lyq
 * 
 */
public class Point{
	// ��������
	Double x;
	// ���������
	Double y;

	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Point(String x, String y) {
		this.x = (Double.parseDouble(x));
		this.y = (Double.parseDouble(y));
	}

	/**
	 * ���㵱ǰ�����ƶ���֮���ŷʽ����
	 * 
	 * @param p
	 *            ���������p��
	 * @return
	 */
	public double ouDistance(Point p) {
		double distance = 0;

		distance = (this.x - p.x) * (this.x - p.x) + (this.y - p.y)
				* (this.y - p.y);
		distance = Math.sqrt(distance);

		return distance;
	}

	/**
	 * �ж�2�������Ƿ�Ϊ�ø�����
	 * 
	 * @param p
	 *            ��Ƚ�����
	 * @return
	 */
	public boolean isTheSame(Point p) {
		boolean isSamed = false;

		if (this.x == p.x && this.y == p.y) {
			isSamed = true;
		}

		return isSamed;
	}
}
