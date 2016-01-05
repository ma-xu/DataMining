package Others.DataMining_DBSCAN;

/**
 * ������
 * 
 * @author maxu
 * 
 */
public class Point {
	// ��������
	int x;
	// ���������
	int y;
	// �˽ڵ��Ƿ��Ѿ������ʹ�
	boolean isVisited;

	public Point(String x, String y) {
		this.x = (Integer.parseInt(x));
		this.y = (Integer.parseInt(y));
		this.isVisited = false;
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
