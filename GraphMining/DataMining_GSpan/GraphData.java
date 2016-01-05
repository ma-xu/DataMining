package GraphMining.DataMining_GSpan;

import java.util.ArrayList;

/**
 * ͼ�������
 * 
 * @author lyq
 * 
 */
public class GraphData {
	// �ڵ�����
	private ArrayList<Integer> nodeLabels;
	// �ڵ��Ƿ����,���ܱ��Ƴ�
	private ArrayList<Boolean> nodeVisibles;
	// �ߵļ��ϱ��
	private ArrayList<Integer> edgeLabels;
	// �ߵ�һ�ߵ�id
	private ArrayList<Integer> edgeX;
	// �ߵ���һ�ߵĵ�id
	private ArrayList<Integer> edgeY;
	// ���Ƿ����
	private ArrayList<Boolean> edgeVisibles;

	public GraphData() {
		nodeLabels = new ArrayList<>();
		nodeVisibles = new ArrayList<>();

		edgeLabels = new ArrayList<>();
		edgeX = new ArrayList<>();
		edgeY = new ArrayList<>();
		edgeVisibles = new ArrayList<>();
	}

	public ArrayList<Integer> getNodeLabels() {
		return nodeLabels;
	}

	public void setNodeLabels(ArrayList<Integer> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

	public ArrayList<Boolean> getNodeVisibles() {
		return nodeVisibles;
	}

	public void setNodeVisibles(ArrayList<Boolean> nodeVisibles) {
		this.nodeVisibles = nodeVisibles;
	}

	public ArrayList<Integer> getEdgeLabels() {
		return edgeLabels;
	}

	public void setEdgeLabels(ArrayList<Integer> edgeLabels) {
		this.edgeLabels = edgeLabels;
	}

	public ArrayList<Integer> getEdgeX() {
		return edgeX;
	}

	public void setEdgeX(ArrayList<Integer> edgeX) {
		this.edgeX = edgeX;
	}

	public ArrayList<Integer> getEdgeY() {
		return edgeY;
	}

	public void setEdgeY(ArrayList<Integer> edgeY) {
		this.edgeY = edgeY;
	}

	public ArrayList<Boolean> getEdgeVisibles() {
		return edgeVisibles;
	}

	public void setEdgeVisibles(ArrayList<Boolean> edgeVisibles) {
		this.edgeVisibles = edgeVisibles;
	}

	/**
	 * ��ݵ��Ƶ�����Ƴ�ͼ�в�Ƶ���ĵ��
	 * 
	 * @param freqNodeLabel
	 *            ���Ƶ����ͳ��
	 * @param freqEdgeLabel
	 *            �ߵ�Ƶ����ͳ��
	 * @param minSupportCount
	 *            ��С֧�ֶȼ���
	 */
	public void removeInFreqNodeAndEdge(int[] freqNodeLabel,
			int[] freqEdgeLabel, int minSupportCount) {
		int label = 0;
		int x = 0;
		int y = 0;

		for (int i = 0; i < nodeLabels.size(); i++) {
			label = nodeLabels.get(i);
			if (freqNodeLabel[label] < minSupportCount) {
				// ���С��֧�ֶȼ�����˵㲻����
				nodeVisibles.set(i, false);
			}
		}

		for (int i = 0; i < edgeLabels.size(); i++) {
			label = edgeLabels.get(i);

			if (freqEdgeLabel[label] < minSupportCount) {
				// ���С��֧�ֶȼ�����˱߲�����
				edgeVisibles.set(i, false);
				continue;
			}

			// ���˱ߵ�ĳ���˵Ķ˵��Ѿ��������ˣ���˱�Ҳ������,x,y��ʾid��
			x = edgeX.get(i);
			y = edgeY.get(i);
			if (!nodeVisibles.get(x) || !nodeVisibles.get(y)) {
				edgeVisibles.set(i, false);
			}
		}
	}

	/**
	 * ��ݱ���������¶����������ĵ�����±��
	 * 
	 * @param nodeLabel2Rank
	 *            ������
	 * @param edgeLabel2Rank
	 *            ������
	 */
	public void reLabelByRank(int[] nodeLabel2Rank, int[] edgeLabel2Rank) {
		int label = 0;
		int count = 0;
		int temp = 0;
		// �ɵ�id����id�ŵ�ӳ��
		int[] oldId2New = new int[nodeLabels.size()];
		for (int i = 0; i < nodeLabels.size(); i++) {
			label = nodeLabels.get(i);

			// ���ǰ���ǿ��õģ����˱�ŵ��������Ϊ�˵��µı��
			if (nodeVisibles.get(i)) {
				nodeLabels.set(i, nodeLabel2Rank[label]);
				oldId2New[i] = count;
				count++;
			}
		}

		for (int i = 0; i < edgeLabels.size(); i++) {
			label = edgeLabels.get(i);

			// ���ǰ���ǿ��õģ����˱�ŵ��������Ϊ�˵��µı��
			if (edgeVisibles.get(i)) {
				edgeLabels.set(i, edgeLabel2Rank[label]);

				// �Դ˵���x,y��id���滻
				temp = edgeX.get(i);
				edgeX.set(i, oldId2New[temp]);
				temp = edgeY.get(i);
				edgeY.set(i, oldId2New[temp]);
			}
		}
	}
}
