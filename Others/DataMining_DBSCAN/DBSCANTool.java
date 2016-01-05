package Others.DataMining_DBSCAN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * DBSCAN�����ܶȾ����㷨������
 * 
 * @author maxu
 * 
 */
public class DBSCANTool {
	// ��������ļ���ַ
	private String filePath;
	// ��ɨ��뾶
	private double eps;
	// ��С�������ֵ
	private int minPts;
	// ���е��������
	private ArrayList<Point> totalPoints;
	// �۴ؽ��
	private ArrayList<ArrayList<Point>> resultClusters;
	//�������
	private ArrayList<Point> noisePoint;

	public DBSCANTool(String filePath, double eps, int minPts) {
		this.filePath = filePath;
		this.eps = eps;
		this.minPts = minPts;
		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	public void readDataFile() {
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
			p = new Point(array[0], array[1]);
			totalPoints.add(p);
		}
	}

	/**
	 * �ݹ��Ѱ�Ҿ۴�
	 * 
	 * @param pointList
	 *            ��ǰ�ĵ��б�
	 * @param parentCluster
	 *            ���۴�
	 */
	private void recursiveCluster(Point point, ArrayList<Point> parentCluster) {
		double distance = 0;
		ArrayList<Point> cluster;

		// ����Ѿ����ʹ��ˣ������
		if (point.isVisited) {
			return;
		}

		point.isVisited = true;
		cluster = new ArrayList<>();
		for (Point p2 : totalPoints) {
			// ���˵����������
			if (point.isTheSame(p2)) {
				continue;
			}

			distance = point.ouDistance(p2);
			if (distance <= eps) {
				// ������С�ڸ�İ뾶����������
				cluster.add(p2);
			}
		}

		if (cluster.size() >= minPts) {
			// ���Լ�Ҳ���뵽�۴���
			cluster.add(point);
			// ����Ľڵ���������ֵ������뵽���۴���,ͬʱȥ���ظ��ĵ�
			addCluster(parentCluster, cluster);

			for (Point p : cluster) {
				recursiveCluster(p, parentCluster);
			}
		}
	}

	/**
	 * ��۴�����Ӿֲ�������
	 * 
	 * @param parentCluster
	 *            ԭʼ���۴�����
	 * @param cluster
	 *            ��ϲ��ľ۴�
	 */
	private void addCluster(ArrayList<Point> parentCluster,
			ArrayList<Point> cluster) {
		boolean isCotained = false;
		ArrayList<Point> addPoints = new ArrayList<>();

		for (Point p : cluster) {
			isCotained = false;
			for (Point p2 : parentCluster) {
				if (p.isTheSame(p2)) {
					isCotained = true;
					break;
				}
			}

			if (!isCotained) {
				addPoints.add(p);
			}
		}

		parentCluster.addAll(addPoints);
	}

	/**
	 * dbScan�㷨�����ܶȵľ���
	 */
	public void dbScanCluster() {
		ArrayList<Point> cluster = null;
		resultClusters = new ArrayList<>();
		noisePoint = new ArrayList<>();
		
		for (Point p : totalPoints) {
			if(p.isVisited){
				continue;
			}
			
			cluster = new ArrayList<>();
			recursiveCluster(p, cluster);

			if (cluster.size() > 0) {
				resultClusters.add(cluster);
			}else{
				noisePoint.add(p);
			}
		}
		removeFalseNoise();
		
		printClusters();
	}
	
	/**
	 * �Ƴ����������������
	 */
	private void removeFalseNoise(){
		ArrayList<Point> totalCluster = new ArrayList<>();
		ArrayList<Point> deletePoints = new ArrayList<>();
		
		//���۴غϲ�
		for(ArrayList<Point> list: resultClusters){
			totalCluster.addAll(list);
		} 
		
		for(Point p: noisePoint){
			for(Point p2: totalCluster){
				if(p2.isTheSame(p)){
					deletePoints.add(p);
				}
			}
		}
		
		noisePoint.removeAll(deletePoints);
	}

	/**
	 * ���������
	 */
	private void printClusters() {
		int i = 1;
		for (ArrayList<Point> pList : resultClusters) {
			System.out.print("�۴�" + (i++) + ":");
			for (Point p : pList) {
				System.out.print(MessageFormat.format("({0},{1}) ", p.x, p.y));
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.print("�������:");
		for (Point p : noisePoint) {
			System.out.print(MessageFormat.format("({0},{1}) ", p.x, p.y));
		}
		System.out.println();
	}
}
