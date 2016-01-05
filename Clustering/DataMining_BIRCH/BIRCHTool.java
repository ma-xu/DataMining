package Clustering.DataMining_BIRCH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * BIRCH�����㷨������
 * 
 * @author lyq
 * 
 */
public class BIRCHTool {
	// �ڵ��������
	public static final String NON_LEAFNODE = "��NonLeafNode��";
	public static final String LEAFNODE = "��LeafNode��";
	public static final String CLUSTER = "��Cluster��";

	// ��������ļ���ַ
	private String filePath;
	// �ڲ��ڵ�ƽ������B
	public static int B;
	// Ҷ�ӽڵ�ƽ������L
	public static int L;
	// ��ֱ����ֵT
	public static double T;
	// �ܵĲ�����ݼ�¼
	private ArrayList<String[]> totalDataRecords;

	public BIRCHTool(String filePath, int B, int L, double T) {
		this.filePath = filePath;
		this.B = B;
		this.L = L;
		this.T = T;
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
				tempArray = str.split("     ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		totalDataRecords = new ArrayList<>();
		for (String[] array : dataArray) {
			totalDataRecords.add(array);
		}
	}

	/**
	 * ����CF����������
	 * 
	 * @return
	 */
	private ClusteringFeature buildCFTree() {
		NonLeafNode rootNode = null;
		LeafNode leafNode = null;
		Cluster cluster = null;

		for (String[] record : totalDataRecords) {
			cluster = new Cluster(record);

			if (rootNode == null) {
				// CF��ֻ��1���ڵ��ʱ������
				if (leafNode == null) {
					leafNode = new LeafNode();
				}
				leafNode.addingCluster(cluster);
				if (leafNode.getParentNode() != null) {
					rootNode = leafNode.getParentNode();
				}
			} else {
				if (rootNode.getParentNode() != null) {
					rootNode = rootNode.getParentNode();
				}

				// �Ӹ�ڵ㿪ʼ����������Ѱ�ҵ��������Ŀ��Ҷ�ӽڵ�
				LeafNode temp = rootNode.findedClosestNode(cluster);
				temp.addingCluster(cluster);
			}
		}

		// ���������ҳ�������Ľڵ�
		LeafNode node = cluster.getParentNode();
		NonLeafNode upNode = node.getParentNode();
		if (upNode == null) {
			return node;
		} else {
			while (upNode.getParentNode() != null) {
				upNode = upNode.getParentNode();
			}

			return upNode;
		}
	}

	/**
	 * ��ʼ����CF����������
	 */
	public void startBuilding() {
		// �����
		int level = 1;
		ClusteringFeature rootNode = buildCFTree();

		setTreeLevel(rootNode, level);
		showCFTree(rootNode);
	}

	/**
	 * ���ýڵ����
	 * 
	 * @param clusteringFeature
	 *            ��ǰ�ڵ�
	 * @param level
	 *            ��ǰ���ֵ
	 */
	private void setTreeLevel(ClusteringFeature clusteringFeature, int level) {
		LeafNode leafNode = null;
		NonLeafNode nonLeafNode = null;

		if (clusteringFeature instanceof LeafNode) {
			leafNode = (LeafNode) clusteringFeature;
		} else if (clusteringFeature instanceof NonLeafNode) {
			nonLeafNode = (NonLeafNode) clusteringFeature;
		}

		if (nonLeafNode != null) {
			nonLeafNode.setLevel(level);
			level++;
			// �����ӽڵ�
			if (nonLeafNode.getNonLeafChilds() != null) {
				for (NonLeafNode n1 : nonLeafNode.getNonLeafChilds()) {
					setTreeLevel(n1, level);
				}
			} else {
				for (LeafNode n2 : nonLeafNode.getLeafChilds()) {
					setTreeLevel(n2, level);
				}
			}
		} else {
			leafNode.setLevel(level);
			level++;
			// �����Ӿ۴�
			for (Cluster c : leafNode.getClusterChilds()) {
				c.setLevel(level);
			}
		}
	}

	/**
	 * ��ʾCF����������
	 * 
	 * @param rootNode
	 *            CF����ڵ�
	 */
	private void showCFTree(ClusteringFeature rootNode) {
		// �ո����������
		int blankNum = 5;
		// ��ǰ�����
		int currentLevel = 1;
		LinkedList<ClusteringFeature> nodeQueue = new LinkedList<>();
		ClusteringFeature cf;
		LeafNode leafNode;
		NonLeafNode nonLeafNode;
		ArrayList<Cluster> clusterList = new ArrayList<>();
		String typeName;

		nodeQueue.add(rootNode);
		while (nodeQueue.size() > 0) {
			cf = nodeQueue.poll();

			if (cf instanceof LeafNode) {
				leafNode = (LeafNode) cf;
				typeName = LEAFNODE;

				if (leafNode.getClusterChilds() != null) {
					for (Cluster c : leafNode.getClusterChilds()) {
						nodeQueue.add(c);
					}
				}
			} else if (cf instanceof NonLeafNode) {
				nonLeafNode = (NonLeafNode) cf;
				typeName = NON_LEAFNODE;

				if (nonLeafNode.getNonLeafChilds() != null) {
					for (NonLeafNode n1 : nonLeafNode.getNonLeafChilds()) {
						nodeQueue.add(n1);
					}
				} else {
					for (LeafNode n2 : nonLeafNode.getLeafChilds()) {
						nodeQueue.add(n2);
					}
				}
			} else {
				clusterList.add((Cluster)cf);
				typeName = CLUSTER;
			}

			if (currentLevel != cf.getLevel()) {
				currentLevel = cf.getLevel();
				System.out.println();
				System.out.println("|");
				System.out.println("|");
			}else if(currentLevel == cf.getLevel() && currentLevel != 1){
				for (int i = 0; i < blankNum; i++) {
					System.out.print("-");
				}
			}
			
			System.out.print(typeName);
			System.out.print("N:" + cf.getN() + ", LS:");
			System.out.print("[");
			for (double d : cf.getLS()) {
				System.out.print(MessageFormat.format("{0}, ",  d));
			}
			System.out.print("]");
		}
		
		System.out.println();
		System.out.println("*******���շֺõľ۴�****");
		//��ʾ�Ѿ��ֺ���ľ۴ص�
		for(int i=0; i<clusterList.size(); i++){
			System.out.println("Cluster" + (i+1) + "��");
			for(double[] point: clusterList.get(i).getData()){
				System.out.print("[");
				for (double d : point) {
					System.out.print(MessageFormat.format("{0}, ",  d));
				}
				System.out.println("]");
			}
		}
	}

}
