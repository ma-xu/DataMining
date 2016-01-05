package Others.DataMining_CABDDCC;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ��ͨͼ��
 * 
 * @author lyq
 * 
 */
public class Graph {
	// ����֮����������ԣ�������Ϊ���id��
	int[][] edges;
	// ��ͨͼ�ڵ�������
	ArrayList<Point> points;
	// ��ͼ�·ָ��ľ�����ͼ
	ArrayList<ArrayList<Point>> clusters;

	public Graph(int[][] edges) {
		this.edges = edges;
		this.points = getPointByEdges(edges);
	}

	public Graph(int[][] edges, ArrayList<Point> points) {
		this.edges = edges;
		this.points = points;
	}

	public int[][] getEdges() {
		return edges;
	}

	public void setEdges(int[][] edges) {
		this.edges = edges;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	/**
	 * ��ݾ�����ֵ����ͨͼ�Ļ���,������ͨͼ��
	 * 
	 * @param length
	 *            ������ֵ
	 * @return
	 */
	public ArrayList<Graph> splitGraphByLength(int length) {
		int[][] edges;
		Graph tempGraph;
		ArrayList<Graph> graphs = new ArrayList<>();

		for (Point p : points) {
			if (!p.isVisited) {
				// �����е��±�Ϊid��
				edges = new int[points.size()][points.size()];
				dfsExpand(p, length, edges);

				tempGraph = new Graph(edges);
				graphs.add(tempGraph);
			} else {
				continue;
			}
		}

		return graphs;
	}

	/**
	 * ������ȷ�ʽ��չ��ͨͼ
	 * 
	 * @param points
	 *            ��Ҫ�������ѵ�����
	 * @param length
	 *            ������ֵ
	 * @param edges
	 *            ������
	 */
	private void dfsExpand(Point point, int length, int edges[][]) {
		int id1 = 0;
		int id2 = 0;
		double distance = 0;
		ArrayList<Point> tempPoints;

		// �������ˣ������
		if (point.isVisited) {
			return;
		}

		id1 = point.id;
		point.isVisited = true;
		tempPoints = new ArrayList<>();
		for (Point p2 : points) {
			id2 = p2.id;

			if (id1 == id2) {
				continue;
			} else {
				distance = point.ouDistance(p2);
				if (distance <= length) {
					edges[id1][id2] = 1;
					edges[id2][id1] = 1;

					tempPoints.add(p2);
				}
			}
		}

		// ����ݹ�
		for (Point p : tempPoints) {
			dfsExpand(p, length, edges);
		}
	}

	/**
	 * �ж���ͨͼ�Ƿ���Ҫ�ٱ�����
	 * 
	 * @param pointList1
	 *            ���㼯��1
	 * @param pointList2
	 *            ���㼯��2
	 * @return
	 */
	private boolean needDivided(ArrayList<Point> pointList1,
			ArrayList<Point> pointList2) {
		boolean needDivided = false;
		// ����ϵ��t=��ļ��ϵ�������/2�������ӵı���
		double t = 0;
		// ������ֵ����ƽ��ÿ����Ҫ���ܵ�����
		double landa = 0;
		int pointNum1 = pointList1.size();
		int pointNum2 = pointList2.size();
		// �ܱ���
		int totalEdgeNum = 0;
		// ����2���ֵı�����
		int connectedEdgeNum = 0;
		ArrayList<Point> totalPoints = new ArrayList<>();

		totalPoints.addAll(pointList1);
		totalPoints.addAll(pointList2);
		int id1 = 0;
		int id2 = 0;
		for (Point p1 : totalPoints) {
			id1 = p1.id;
			for (Point p2 : totalPoints) {
				id2 = p2.id;

				if (edges[id1][id2] == 1 && id1 < id2) {
					if ((pointList1.contains(p1) && pointList2.contains(p2))
							|| (pointList1.contains(p2) && pointList2
									.contains(p1))) {
						connectedEdgeNum++;
					}
					totalEdgeNum++;
				}
			}
		}

		if (pointNum1 < pointNum2) {
			// ����ϵ��t=��ļ��ϵ�������/����2���ֵı���
			t = 1.0 * pointNum1 / connectedEdgeNum;
		} else {
			t = 1.0 * pointNum2 / connectedEdgeNum;
		}

		// ���������ֵ,������Ϊ�ܱ���/�ܵ������ƽ��ÿ������ܵĵ�����
		landa = 0.5 * Math.exp((1.0 * totalEdgeNum / (pointNum1 + pointNum2)));

		// ������ϵ��С�ڷ�����ֵ��������Ҫ����
		if (t >= landa) {
			needDivided = true;
		}

		return needDivided;
	}

	/**
	 * �ݹ�Ļ�����ͨͼ
	 * 
	 * @param pointList
	 *            ��ֵ���ͨͼ����������
	 */
	public void divideGraph(ArrayList<Point> pointList) {
		// �жϴ����㼯���Ƿ��ܹ����ָ�
		boolean canDivide = false;
		ArrayList<ArrayList<Point>> pointGroup;
		ArrayList<Point> pointList1 = new ArrayList<>();
		ArrayList<Point> pointList2 = new ArrayList<>();

		for (int m = 2; m <= pointList.size() / 2; m++) {
			// ��������ķָ�
			pointGroup = removePoint(pointList, m);
			pointList1 = pointGroup.get(0);
			pointList2 = pointGroup.get(1);

			// �ж��Ƿ������������
			if (needDivided(pointList1, pointList2)) {
				canDivide = true;
				divideGraph(pointList1);
				divideGraph(pointList2);
			}
		}

		// ������еķָ���϶��޷��ָ��˵�����Ѿ���һ������
		if (!canDivide) {
			clusters.add(pointList);
		}
	}

	/**
	 * ��ȡ���ѵõ��ľ�����
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Point>> getClusterByDivding() {
		clusters = new ArrayList<>();
		
		divideGraph(points);

		return clusters;
	}

	/**
	 * ����ǰ���㼯���Ƴ�removeNum���㣬����2�������㼯��
	 * 
	 * @param pointList
	 *            ԭ���ϵ�
	 * @param removeNum
	 *            �Ƴ������
	 */
	private ArrayList<ArrayList<Point>> removePoint(ArrayList<Point> pointList,
			int removeNum) {
		//ǳ����һ��ԭ�������
		ArrayList<Point> copyPointList = (ArrayList<Point>) pointList.clone();
		ArrayList<ArrayList<Point>> pointGroup = new ArrayList<>();
		ArrayList<Point> pointList2 = new ArrayList<>();
		// ���а���������С����
		Collections.sort(copyPointList);

		for (int i = 0; i < removeNum; i++) {
			pointList2.add(copyPointList.get(i));
		}
		copyPointList.removeAll(pointList2);

		pointGroup.add(copyPointList);
		pointGroup.add(pointList2);

		return pointGroup;
	}

	/**
	 * ��ݱߵ������ȡ���еĵ�
	 * 
	 * @param edges
	 *            ��ǰ����֪�ıߵ����
	 * @return
	 */
	private ArrayList<Point> getPointByEdges(int[][] edges) {
		Point p1;
		Point p2;
		ArrayList<Point> pointList = new ArrayList<>();

		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges[0].length; j++) {
				if (edges[i][j] == 1) {
					p1 = CABDDCCTool.totalPoints.get(i);
					p2 = CABDDCCTool.totalPoints.get(j);

					if (!pointList.contains(p1)) {
						pointList.add(p1);
					}

					if (!pointList.contains(p2)) {
						pointList.add(p2);
					}
				}
			}
		}

		return pointList;
	}
}
