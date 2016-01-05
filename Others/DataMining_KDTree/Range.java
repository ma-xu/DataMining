package Others.DataMining_KDTree;

/**
 * �ռ�ʸ������ʾ����Ŀռ䷶Χ
 * 
 * @author lyq
 * 
 */
public class Range {
	// �߽���߽�
	double left;
	// �߽��ұ߽�
	double right;
	// �߽��ϱ߽�
	double top;
	// �߽��±߽�
	double bottom;

	public Range() {
		this.left = -Integer.MAX_VALUE;
		this.right = Integer.MAX_VALUE;
		this.top = Integer.MAX_VALUE;
		this.bottom = -Integer.MAX_VALUE;
	}

	public Range(int left, int right, int top, int bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	/**
	 * �ռ�ʸ�����в�����
	 * 
	 * @param range
	 * @return
	 */
	public Range crossOperation(Range r) {
		Range range = new Range();

		// ȡ�����Ҳ����߽�
		if (r.left > this.left) {
			range.left = r.left;
		} else {
			range.left = this.left;
		}

		// ȡ���������ұ߽�
		if (r.right < this.right) {
			range.right = r.right;
		} else {
			range.right = this.right;
		}

		// ȡ�����²���ϱ߽�
		if (r.top < this.top) {
			range.top = r.top;
		} else {
			range.top = this.top;
		}

		// ȡ�����ϲ���±߽�
		if (r.bottom > this.bottom) {
			range.bottom = r.bottom;
		} else {
			range.bottom = this.bottom;
		}

		return range;
	}

	/**
	 * �������ָ��ȷ�����ռ�ʸ��
	 * 
	 * @param p
	 *            ���ʸ��
	 * @param dir
	 *            �ָ��
	 * @return
	 */
	public static Range initLeftRange(Point p, int dir) {
		Range range = new Range();

		if (dir == KDTreeTool.DIRECTION_X) {
			range.right = p.x;
		} else {
			range.bottom = p.y;
		}

		return range;
	}

	/**
	 * �������ָ��ȷ���Ҳ�ռ�ʸ��
	 * 
	 * @param p
	 *            ���ʸ��
	 * @param dir
	 *            �ָ��
	 * @return
	 */
	public static Range initRightRange(Point p, int dir) {
		Range range = new Range();

		if (dir == KDTreeTool.DIRECTION_X) {
			range.left = p.x;
		} else {
			range.top = p.y;
		}

		return range;
	}
}
