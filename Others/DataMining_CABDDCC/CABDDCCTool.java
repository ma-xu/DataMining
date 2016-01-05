package Others.DataMining_CABDDCC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * ������ͨͼ�ķ��Ѿ����㷨
 * 
 * @author lyq
 * 
 */
public class CABDDCCTool {
	// ������ݵ����
	private String filePath;
	// ��ͨͼ������ֵl
	private int length;
	// ԭʼ����
	public static ArrayList<Point> totalPoints;
	// ���������㼯��
	private ArrayList<ArrayList<Point>> resultClusters;
	// ��ͨͼ
	private Graph graph;

	public CABDDCCTool(String filePath, int length) {
		this.filePath = filePath;
		this.length = length;

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
			p = new Point(array[0], array[1], array[2]);
			totalPoints.add(p);
		}

		// �ñߺ͵㹹��ͼ
		graph = new Graph(null, totalPoints);
	}

	/**
	 * ������ͨͼ�õ�����
	 */
	public void splitCluster() {
		// ��ȡ�γ���ͨ��ͼ
		ArrayList<Graph> subGraphs;
		ArrayList<ArrayList<Point>> pointList;
		resultClusters = new ArrayList<>();

		subGraphs = graph.splitGraphByLength(length);

		for (Graph g : subGraphs) {
			// ��ȡÿ����ͨ��ͼ���Ѻ�ľ�����
			pointList = g.getClusterByDivding();
			resultClusters.addAll(pointList);
		}
		
		printResultCluster();
	}

	/**
	 * ������۴�
	 */
	private void printResultCluster() {
		int i = 1;
		for (ArrayList<Point> cluster : resultClusters) {
			System.out.print("�۴�" + i + ":");
			for (Point p : cluster){
				System.out.print(MessageFormat.format("({0}, {1}) ", p.x, p.y));
			}
			System.out.println();
			i++;
		}
		
	}

}
