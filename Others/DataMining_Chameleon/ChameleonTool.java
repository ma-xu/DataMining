package Others.DataMining_Chameleon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Chameleon ���׶ξ����㷨������
 * 
 * @author lyq
 * 
 */
public class ChameleonTool {
	// ������ݵ��ļ���ַ
	private String filePath;
	// ��һ�׶ε�k���ڵ�k��С
	private int k;
	// �ض���������ֵ
	private double minMetric;
	// �ܵ�����ĸ���
	private int pointNum;
	// �ܵ����Ӿ�������,���ű�ʾ���������id��
	public static int[][] edges;
	// �����֮��ıߵ�Ȩ��
	public static double[][] weights;
	// ԭʼ�������
	private ArrayList<Point> totalPoints;
	// ��һ�׶β�������е���ͨ��ͼ��Ϊ���ʼ�ľ���
	private ArrayList<Cluster> initClusters;
	// ���ؽ��
	private ArrayList<Cluster> resultClusters;

	public ChameleonTool(String filePath, int k, double minMetric) {
		this.filePath = filePath;
		this.k = k;
		this.minMetric = minMetric;
		
		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	private void readDataFile() {
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		Point p;
		totalPoints = new ArrayList<>();
		for (String[] array : dataArray) {
			p = new Point(array[0], array[1], array[2]);
			totalPoints.add(p);
		}
		pointNum = totalPoints.size();
	}

	/**
	 * �ݹ�ĺϲ�С�۴�
	 */
	private void combineSubClusters() {
		Cluster cluster = null;

		resultClusters = new ArrayList<>();

		// �����ľ۴�ֻʣ��һ����ʱ�����˳�ѭ��
		while (initClusters.size() > 1) {
			cluster = initClusters.get(0);
			combineAndRemove(cluster, initClusters);
		}
	}

	/**
	 * �ݹ�ĺϲ��۴غ��Ƴ�۴�
	 * 
	 * @param clusterList
	 */
	private ArrayList<Cluster> combineAndRemove(Cluster cluster,
			ArrayList<Cluster> clusterList) {
		ArrayList<Cluster> remainClusters;
		double metric = 0;
		double maxMetric = -Integer.MAX_VALUE;
		Cluster cluster1 = null;
		Cluster cluster2 = null;

		for (Cluster c2 : clusterList) {
			if(cluster.id == c2.id){
				continue;
			}
			
			metric = calMetricfunction(cluster, c2, 1);

			if (metric > maxMetric) {
				maxMetric = metric;
				cluster1 = cluster;
				cluster2 = c2;
			}
		}

		// ����������ֵ������ֵ������кϲ�,������Ѱ���Ժϲ��Ĵ�
		if (maxMetric > minMetric) {
			clusterList.remove(cluster2);
			//���߽�������
			connectClusterToCluster(cluster1, cluster2);
			// ����1�ʹ�2�ϲ�
			cluster1.points.addAll(cluster2.points);
			remainClusters = combineAndRemove(cluster1, clusterList);
		} else {
			clusterList.remove(cluster);
			remainClusters = clusterList;
			resultClusters.add(cluster);
		}

		return remainClusters;
	}
	
	/**
	 * ��2���ؽ��бߵ�����
	 * @param c1
	 * �۴�1
	 * @param c2
	 * �۴�2
	 */
	private void connectClusterToCluster(Cluster c1, Cluster c2){
		ArrayList<int[]> connectedEdges;
		
		connectedEdges = c1.calNearestEdge(c2, 2);
		
		for(int[] array: connectedEdges){
			edges[array[0]][array[1]] = 1;
			edges[array[1]][array[0]] = 1;
		}
	}

	/**
	 * �㷨��һ�׶��γɾֲ�����ͨͼ
	 */
	private void connectedGraph() {
		double distance = 0;
		Point p1;
		Point p2;

		// ��ʼ��Ȩ�ؾ�������Ӿ���
		weights = new double[pointNum][pointNum];
		edges = new int[pointNum][pointNum];
		for (int i = 0; i < pointNum; i++) {
			for (int j = 0; j < pointNum; j++) {
				p1 = totalPoints.get(i);
				p2 = totalPoints.get(j);

				distance = p1.ouDistance(p2);
				if (distance == 0) {
					// ����Ϊ����Ļ�����Ȩ������Ϊ0
					weights[i][j] = 0;
				} else {
					// �ߵ�Ȩ�ز��õ�ֵΪ����ĵ���,����Խ��Ȩ��Խ��
					weights[i][j] = 1.0 / distance;
				}
			}
		}

		double[] tempWeight;
		int[] ids;
		int id1 = 0;
		int id2 = 0;
		// ��ÿ��id���㣬ȡ��Ȩ��ǰk�����ĵ��������
		for (int i = 0; i < pointNum; i++) {
			tempWeight = weights[i];
			// ��������
			ids = sortWeightArray(tempWeight);
			
			// ȡ��ǰk��Ȩ�����ı߽�������
			for (int j = 0; j < ids.length; j++) {
				if (j < k) {
					id1 = i;
					id2 = ids[j];

					edges[id1][id2] = 1;
					edges[id2][id1] = 1;
				}
			}
		}
	}

	/**
	 * Ȩ�ص�ð���㷨����
	 * 
	 * @param array
	 *            ����������
	 */
	private int[] sortWeightArray(double[] array) {
		double[] copyArray = array.clone();
		int[] ids = null;
		int k = 0;
		double maxWeight = -1;
		
		ids = new int[pointNum];
		for(int i=0; i<pointNum; i++){
			maxWeight = -1;
			
			for(int j=0; j<copyArray.length; j++){
				if(copyArray[j] > maxWeight){
					 maxWeight = copyArray[j];
					 k = j;
				}
			}
			
			ids[i] = k;
			//����ǰ�ҵ�������ֵ����Ϊ-1����Ѿ��ҵ�����
			copyArray[k] = -1;
		}
		
		return ids;
	}

	/**
	 * ��ݱߵ���ͨ��ȥ��������������е�С�۴�
	 */
	private void searchSmallCluster() {
		int currentId = 0;
		Point p;
		Cluster cluster;
		initClusters = new ArrayList<>();
		ArrayList<Point> pointList = null;

		// ��id�ķ�ʽ���ȥdfs����
		for (int i = 0; i < pointNum; i++) {
			p = totalPoints.get(i);

			if (p.isVisited) {
				continue;
			}

			pointList = new ArrayList<>();
			pointList.add(p);
			recusiveDfsSearch(p, -1, pointList);
			
			cluster = new Cluster(currentId, pointList);
			initClusters.add(cluster);
			
			currentId++;
		}
	}

	/**
	 * ������ȵķ�ʽ�ҵ����������ŵ���������
	 * 
	 * @param p
	 *            ��ǰ���������
	 * @param lastId
	 *            �˵�ĸ�����
	 * @param pList
	 *            �����б�
	 */
	private void recusiveDfsSearch(Point p, int parentId, ArrayList<Point> pList) {
		int id1 = 0;
		int id2 = 0;
		Point newPoint;

		if (p.isVisited) {
			return;
		}

		p.isVisited = true;
		for (int j = 0; j < pointNum; j++) {
			id1 = p.id;
			id2 = j;

			if (edges[id1][id2] == 1 && id2 != parentId) {
				newPoint = totalPoints.get(j);
				pList.add(newPoint);
				// �Դ˵�Ϊ��㣬����ݹ�����
				recusiveDfsSearch(newPoint, id1, pList);
			}
		}
	}

	/**
	 * ��������2���صıߵ�Ȩ��
	 * 
	 * @param c1
	 *            �۴�1
	 * @param c2
	 *            �۴�2
	 * @return
	 */
	private double calEC(Cluster c1, Cluster c2) {
		double resultEC = 0;
		ArrayList<int[]> connectedEdges = null;

		connectedEdges = c1.calNearestEdge(c2, 2);

		// ��������2���ֵıߵ�Ȩ�غ�
		for (int[] array : connectedEdges) {
			resultEC += weights[array[0]][array[1]];
		}

		return resultEC;
	}

	/**
	 * ����2���ص���Ի�����
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	private double calRI(Cluster c1, Cluster c2) {
		double RI = 0;
		double EC1 = 0;
		double EC2 = 0;
		double EC1To2 = 0;

		EC1 = c1.calEC();
		EC2 = c2.calEC();
		EC1To2 = calEC(c1, c2);

		RI = 2 * EC1To2 / (EC1 + EC2);

		return RI;
	}

	/**
	 * ����ص���Խ��ƶ�
	 * 
	 * @param c1
	 *            ��1
	 * @param c2
	 *            ��2
	 * @return
	 */
	private double calRC(Cluster c1, Cluster c2) {
		double RC = 0;
		double EC1 = 0;
		double EC2 = 0;
		double EC1To2 = 0;
		int pNum1 = c1.points.size();
		int pNum2 = c2.points.size();

		EC1 = c1.calEC();
		EC2 = c2.calEC();
		EC1To2 = calEC(c1, c2);

		RC = EC1To2 * (pNum1 + pNum2) / (pNum2 * EC1 + pNum1 * EC2);

		return RC;
	}

	/**
	 * ������������ֵ
	 * 
	 * @param c1
	 *            ��1
	 * @param c2
	 *            ��2
	 * @param alpha
	 *            �ݵĲ���ֵ
	 * @return
	 */
	private double calMetricfunction(Cluster c1, Cluster c2, int alpha) {
		// ��������ֵ
		double metricValue = 0;
		double RI = 0;
		double RC = 0;

		RI = calRI(c1, c2);
		RC = calRC(c1, c2);
		// ���alpha����1�����������Խ����ԣ����alpha��ң��1��ע����Ի�����
		metricValue = RI * Math.pow(RC, alpha);

		return metricValue;
	}

	/**
	 * ����۴���
	 * @param clusterList
	 * ����۴���
	 */
	private void printClusters(ArrayList<Cluster> clusterList) {
		int i = 1;

		for (Cluster cluster : clusterList) {
			System.out.print("�۴�" + i + ":");
			for (Point p : cluster.points) {
				System.out.print(MessageFormat.format("({0}, {1}) ", p.x, p.y));
			}
			System.out.println();
			i++;
		}

	}

	/**
	 * �����۴�
	 */
	public void buildCluster() {
		// ��һ�׶��γ�С�۴�
		connectedGraph();
		searchSmallCluster();
		System.out.println("��һ�׶��γɵ�С�ؼ��ϣ�");
		printClusters(initClusters);
		
		// �ڶ��׶θ��RI��RC��ֵ�ϲ�С�۴��γ����ս��۴�
		combineSubClusters();
		System.out.println("���յľ۴ؼ��ϣ�");
		printClusters(resultClusters);
	}
}
