package GraphMining.DataMining_GSpan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * gSpanƵ����ͼ�ھ��㷨������
 * 
 * @author lyq
 * 
 */
public class GSpanTool {
	// �ļ��������
	public final String INPUT_NEW_GRAPH = "t";
	public final String INPUT_VERTICE = "v";
	public final String INPUT_EDGE = "e";
	// Label��ŵ�����������������źͱ߱��
	public final int LABEL_MAX = 100;

	// ��������ļ���ַ
	private String filePath;
	// ��С֧�ֶ���
	private double minSupportRate;
	// ��С֧�ֶ���ͨ��ͼ��������С֧�ֶ��ʵĳ˻�������
	private int minSupportCount;
	// ��ʼ����ͼ�����
	private ArrayList<GraphData> totalGraphDatas;
	// ���е�ͼ�ṹ���
	private ArrayList<Graph> totalGraphs;
	// �ھ����Ƶ����ͼ
	private ArrayList<Graph> resultGraphs;
	// �ߵ�Ƶ��ͳ��
	private EdgeFrequency ef;
	// �ڵ��Ƶ��
	private int[] freqNodeLabel;
	// �ߵ�Ƶ��
	private int[] freqEdgeLabel;
	// ���±��֮��ĵ�ı����
	private int newNodeLabelNum = 0;
	// ���±�ź�ıߵı����
	private int newEdgeLabelNum = 0;

	public GSpanTool(String filePath, double minSupportRate) {
		this.filePath = filePath;
		this.minSupportRate = minSupportRate;
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

		calFrequentAndRemove(dataArray);
	}

	/**
	 * ͳ�Ʊߺ͵��Ƶ�ȣ����Ƴ�Ƶ���ĵ�ߣ��Ա����Ϊͳ�Ƶı���
	 * 
	 * @param dataArray
	 *            ԭʼ���
	 */
	private void calFrequentAndRemove(ArrayList<String[]> dataArray) {
		int tempCount = 0;
		freqNodeLabel = new int[LABEL_MAX];
		freqEdgeLabel = new int[LABEL_MAX];

		// ����ʼ������
		for (int i = 0; i < LABEL_MAX; i++) {
			// �����Ϊi�Ľڵ�Ŀǰ������Ϊ0
			freqNodeLabel[i] = 0;
			freqEdgeLabel[i] = 0;
		}

		GraphData gd = null;
		totalGraphDatas = new ArrayList<>();
		for (String[] array : dataArray) {
			if (array[0].equals(INPUT_NEW_GRAPH)) {
				if (gd != null) {
					totalGraphDatas.add(gd);
				}

				// �½�ͼ
				gd = new GraphData();
			} else if (array[0].equals(INPUT_VERTICE)) {
				// ÿ��ͼ�е�ÿ��ͼֻͳ��һ��
				if (!gd.getNodeLabels().contains(Integer.parseInt(array[2]))) {
					tempCount = freqNodeLabel[Integer.parseInt(array[2])];
					tempCount++;
					freqNodeLabel[Integer.parseInt(array[2])] = tempCount;
				}

				gd.getNodeLabels().add(Integer.parseInt(array[2]));
				gd.getNodeVisibles().add(true);
			} else if (array[0].equals(INPUT_EDGE)) {
				// ÿ��ͼ�е�ÿ��ͼֻͳ��һ��
				if (!gd.getEdgeLabels().contains(Integer.parseInt(array[3]))) {
					tempCount = freqEdgeLabel[Integer.parseInt(array[3])];
					tempCount++;
					freqEdgeLabel[Integer.parseInt(array[3])] = tempCount;
				}

				int i = Integer.parseInt(array[1]);
				int j = Integer.parseInt(array[2]);

				gd.getEdgeLabels().add(Integer.parseInt(array[3]));
				gd.getEdgeX().add(i);
				gd.getEdgeY().add(j);
				gd.getEdgeVisibles().add(true);
			}
		}
		// �����һ��gd��ݼ���
		totalGraphDatas.add(gd);
		minSupportCount = (int) (minSupportRate * totalGraphDatas.size());

		for (GraphData g : totalGraphDatas) {
			g.removeInFreqNodeAndEdge(freqNodeLabel, freqEdgeLabel,
					minSupportCount);
		}
	}

	/**
	 * ��ݱ��Ƶ���Ƚ������������±��
	 */
	private void sortAndReLabel() {
		int label1 = 0;
		int label2 = 0;
		int temp = 0;
		// ���������
		int[] rankNodeLabels = new int[LABEL_MAX];
		// ���������
		int[] rankEdgeLabels = new int[LABEL_MAX];
		// ��Ŷ�Ӧ����
		int[] nodeLabel2Rank = new int[LABEL_MAX];
		int[] edgeLabel2Rank = new int[LABEL_MAX];

		for (int i = 0; i < LABEL_MAX; i++) {
			// ��ʾ�����iλ�ı��Ϊi��[i]�е�i��ʾ����
			rankNodeLabels[i] = i;
			rankEdgeLabels[i] = i;
		}

		for (int i = 0; i < freqNodeLabel.length - 1; i++) {
			int k = 0;
			label1 = rankNodeLabels[i];
			temp = label1;
			for (int j = i + 1; j < freqNodeLabel.length; j++) {
				label2 = rankNodeLabels[j];

				if (freqNodeLabel[temp] < freqNodeLabel[label2]) {
					// ���б�ŵĻ���
					temp = label2;
					k = j;
				}
			}

			if (temp != label1) {
				// ����i��k�����µı�ŶԵ�
				temp = rankNodeLabels[k];
				rankNodeLabels[k] = rankNodeLabels[i];
				rankNodeLabels[i] = temp;
			}
		}

		// �Ա�ͬ���������
		for (int i = 0; i < freqEdgeLabel.length - 1; i++) {
			int k = 0;
			label1 = rankEdgeLabels[i];
			temp = label1;
			for (int j = i + 1; j < freqEdgeLabel.length; j++) {
				label2 = rankEdgeLabels[j];

				if (freqEdgeLabel[temp] < freqEdgeLabel[label2]) {
					// ���б�ŵĻ���
					temp = label2;
					k = j;
				}
			}

			if (temp != label1) {
				// ����i��k�����µı�ŶԵ�
				temp = rankEdgeLabels[k];
				rankEdgeLabels[k] = rankEdgeLabels[i];
				rankEdgeLabels[i] = temp;
			}
		}

		// ������Ա��תΪ��Ŷ�����
		for (int i = 0; i < rankNodeLabels.length; i++) {
			nodeLabel2Rank[rankNodeLabels[i]] = i;
		}

		for (int i = 0; i < rankEdgeLabels.length; i++) {
			edgeLabel2Rank[rankEdgeLabels[i]] = i;
		}

		for (GraphData gd : totalGraphDatas) {
			gd.reLabelByRank(nodeLabel2Rank, edgeLabel2Rank);
		}

		// ��������ҳ�С��֧�ֶ�ֵ���������ֵ
		for (int i = 0; i < rankNodeLabels.length; i++) {
			if (freqNodeLabel[rankNodeLabels[i]] > minSupportCount) {
				newNodeLabelNum = i;
			}
		}
		for (int i = 0; i < rankEdgeLabels.length; i++) {
			if (freqEdgeLabel[rankEdgeLabels[i]] > minSupportCount) {
				newEdgeLabelNum = i;
			}
		}
		//����ű�������1������Ҫ�ӻ���
		newNodeLabelNum++;
		newEdgeLabelNum++;
	}

	/**
	 * ����Ƶ����ͼ���ھ�
	 */
	public void freqGraphMining() {
		long startTime =  System.currentTimeMillis();
		long endTime = 0;
		Graph g;
		sortAndReLabel();

		resultGraphs = new ArrayList<>();
		totalGraphs = new ArrayList<>();
		// ͨ��ͼ��ݹ���ͼ�ṹ
		for (GraphData gd : totalGraphDatas) {
			g = new Graph();
			g = g.constructGraph(gd);
			totalGraphs.add(g);
		}

		// ����µĵ�ߵı�����ʼ����Ƶ���ȶ���
		ef = new EdgeFrequency(newNodeLabelNum, newEdgeLabelNum);
		for (int i = 0; i < newNodeLabelNum; i++) {
			for (int j = 0; j < newEdgeLabelNum; j++) {
				for (int k = 0; k < newNodeLabelNum; k++) {
					for (Graph tempG : totalGraphs) {
						if (tempG.hasEdge(i, j, k)) {
							ef.edgeFreqCount[i][j][k]++;
						}
					}
				}
			}
		}

		Edge edge;
		GraphCode gc;
		for (int i = 0; i < newNodeLabelNum; i++) {
			for (int j = 0; j < newEdgeLabelNum; j++) {
				for (int k = 0; k < newNodeLabelNum; k++) {
					if (ef.edgeFreqCount[i][j][k] >= minSupportCount) {
						gc = new GraphCode();
						edge = new Edge(0, 1, i, j, k);
						gc.getEdgeSeq().add(edge);

						// �����д˱ߵ�ͼid���뵽gc��
						for (int y = 0; y < totalGraphs.size(); y++) {
							if (totalGraphs.get(y).hasEdge(i, j, k)) {
								gc.getGs().add(y);
							}
						}
						// ��ĳ��������ֵ�ı߽����ھ�
						subMining(gc, 2);
					}
				}
			}
		}
		
		endTime = System.currentTimeMillis();
		System.out.println("�㷨ִ��ʱ��"+ (endTime-startTime) + "ms");
		printResultGraphInfo();
	}

	/**
	 * ����Ƶ����ͼ���ھ�
	 * 
	 * @param gc
	 *            ͼ����
	 * @param next
	 *            ͼ��ĵ�ĸ���
	 */
	public void subMining(GraphCode gc, int next) {
		Edge e;
		Graph graph = new Graph();
		int id1;
		int id2;

		for(int i=0; i<next; i++){
			graph.nodeLabels.add(-1);
			graph.edgeLabels.add(new ArrayList<Integer>());
			graph.edgeNexts.add(new ArrayList<Integer>());
		}

		// ���ȸ��ͼ�����еı���Ԫ�鹹��ͼ
		for (int i = 0; i < gc.getEdgeSeq().size(); i++) {
			e = gc.getEdgeSeq().get(i);
			id1 = e.ix;
			id2 = e.iy;

			graph.nodeLabels.set(id1, e.x);
			graph.nodeLabels.set(id2, e.y);
			graph.edgeLabels.get(id1).add(e.a);
			graph.edgeLabels.get(id2).add(e.a);
			graph.edgeNexts.get(id1).add(id2);
			graph.edgeNexts.get(id2).add(id1);
		}

		DFSCodeTraveler dTraveler = new DFSCodeTraveler(gc.getEdgeSeq(), graph);
		dTraveler.traveler();
		if (!dTraveler.isMin) {
			return;
		}

		// ���ǰ����С�����򽫴�ͼ���뵽�����
		resultGraphs.add(graph);
		Edge e1;
		ArrayList<Integer> gIds;
		SubChildTraveler sct;
		ArrayList<Edge> edgeArray;
		// ���Ǳ�ڵĺ��ӱߣ�ÿ�����ӱ�������ͼid
		HashMap<Edge, ArrayList<Integer>> edge2GId = new HashMap<>();
		for (int i = 0; i < gc.gs.size(); i++) {
			int id = gc.gs.get(i);

			// �ڴ˽ṹ�������£��ڶ��һ���߹�����ͼ�����ھ�
			sct = new SubChildTraveler(gc.edgeSeq, totalGraphs.get(id));
			sct.traveler();
			edgeArray = sct.getResultChildEdge();

			// ����id�ĸ���
			for (Edge e2 : edgeArray) {
				if (!edge2GId.containsKey(e2)) {
					gIds = new ArrayList<>();
				} else {
					gIds = edge2GId.get(e2);
				}

				gIds.add(id);
				edge2GId.put(e2, gIds);
			}
		}

		for (Map.Entry entry : edge2GId.entrySet()) {
			e1 = (Edge) entry.getKey();
			gIds = (ArrayList<Integer>) entry.getValue();

			// ���˱ߵ�Ƶ�ȴ�����С֧�ֶ�ֵ��������ھ�
			if (gIds.size() < minSupportCount) {
				continue;
			}

			GraphCode nGc = new GraphCode();
			nGc.edgeSeq.addAll(gc.edgeSeq);
			// �ڵ�ǰͼ���¼���һ���ߣ������µ���ͼ�����ھ�
			nGc.edgeSeq.add(e1);
			nGc.gs.addAll(gIds);

			if (e1.iy == next) {
				// ���ߵĵ�id������Ϊ��ǰ���ֵ��ʱ����ʼѰ����һ����
				subMining(nGc, next + 1);
			} else {
				// ���˵��Ѿ����ڣ���nextֵ����
				subMining(nGc, next);
			}
		}
	}
	
	/**
	 * ���Ƶ����ͼ�����Ϣ
	 */
	public void printResultGraphInfo(){
		System.out.println(MessageFormat.format("�ھ����Ƶ����ͼ�ĸ���Ϊ��{0}��", resultGraphs.size()));
	}

}
