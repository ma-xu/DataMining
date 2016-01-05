package Others.DataMining_Chameleon;

import java.util.ArrayList;

/**
 * �۴���
 * 
 * @author lyq
 * 
 */
public class Cluster implements Cloneable{
	//��Ψһid��ʶ��
	int id;
	// �۴��ڵ����㼯��
	ArrayList<Point> points;
	// �۴��ڵ����бߵ�Ȩ�غ�
	double weightSum = 0;

	public Cluster(int id, ArrayList<Point> points) {
		this.id = id;
		this.points = points;
	}

	/**
	 * ����۴ص��ڲ��ı�Ȩ�غ�
	 * 
	 * @return
	 */
	public double calEC() {
		int id1 = 0;
		int id2 = 0;
		weightSum = 0;
		
		for (Point p1 : points) {
			for (Point p2 : points) {
				id1 = p1.id;
				id2 = p2.id;

				// Ϊ�˱����ظ����㣬ȡid1С�Ķ�Ӧ���
				if (id1 < id2 && ChameleonTool.edges[id1][id2] == 1) {
					weightSum += ChameleonTool.weights[id1][id2];
				}
			}
		}

		return weightSum;
	}

	/**
	 * ����2����֮������n����
	 * 
	 * @param otherCluster
	 *            ��ȽϵĴ�
	 * @param n
	 *            ���ıߵ���Ŀ
	 * @return
	 */
	public ArrayList<int[]> calNearestEdge(Cluster otherCluster, int n){
		int count = 0;
		double distance = 0;
		double minDistance = Integer.MAX_VALUE;
		Point point1 = null;
		Point point2 = null;
		ArrayList<int[]> edgeList = new ArrayList<>();
		ArrayList<Point> pointList1 = (ArrayList<Point>) points.clone();
		ArrayList<Point> pointList2 = null;
		Cluster c2 = null;
		
		try {
			c2 = (Cluster) otherCluster.clone();
			pointList2 = c2.points;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int[] tempEdge;
		// ѭ�������ÿ�ε�������
		while (count < n) {
			tempEdge = new int[2];
			minDistance = Integer.MAX_VALUE;
			
			for (Point p1 : pointList1) {
				for (Point p2 :  pointList2) {
					distance = p1.ouDistance(p2);
					if (distance < minDistance) {
						point1 = p1;
						point2 = p2;
						tempEdge[0] = p1.id;
						tempEdge[1] = p2.id;

						minDistance = distance;
					}
				}
			}

			pointList1.remove(point1);
			pointList2.remove(point2);
			edgeList.add(tempEdge);
			count++;
		}

		return edgeList;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		
		//������Ҫ�ٴθ��ƣ�ʵ�����
		ArrayList<Point> pointList = (ArrayList<Point>) this.points.clone();
		Cluster cluster = new Cluster(id, pointList);
		
		return cluster;
	}
	
	

}
