package GraphMining.DataMining_GSpan;

import java.util.ArrayList;

/**
 * ͼ�ṹ��
 * 
 * @author lyq
 * 
 */
public class Graph {
	// ͼ�ڵ�����
	ArrayList<Integer> nodeLabels;
	// ͼ�ı߱����
	ArrayList<ArrayList<Integer>> edgeLabels;
	// ��2ͷ�Ľڵ�id��,������������Ϊ�±��
	ArrayList<ArrayList<Integer>> edgeNexts;

	public Graph() {
		nodeLabels = new ArrayList<>();
		edgeLabels = new ArrayList<>();
		edgeNexts = new ArrayList<>();
	}

	public ArrayList<Integer> getNodeLabels() {
		return nodeLabels;
	}

	public void setNodeLabels(ArrayList<Integer> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

	/**
	 * �ж�ͼ���Ƿ����ĳ����
	 * 
	 * @param x
	 *            �ߵ�һ�˵Ľڵ���
	 * @param a
	 *            �ߵı��
	 * @param y
	 *            �ߵ�����һ�˽ڵ���
	 * @return
	 */
	public boolean hasEdge(int x, int a, int y) {
		boolean isContained = false;
		int t;

		for (int i = 0; i < nodeLabels.size(); i++) {
			// ��Ѱ��2���˵���,t����ҵ��ĵ������һ���˵���
			if (nodeLabels.get(i) == x) {
				t = y;
			} else if (nodeLabels.get(i) == y) {
				t = x;
			} else {
				continue;
			}

			for (int j = 0; j < edgeNexts.get(i).size(); j++) {
				// �Ӵ˶˵�������ӵĵ�ȥ�Ƚ϶�Ӧ�ĵ�ͱ�
				if (edgeLabels.get(i).get(j) == a
						&& nodeLabels.get(edgeNexts.get(i).get(j)) == t) {
					isContained = true;
					return isContained;
				}
			}
		}

		return isContained;
	}

	/**
	 * ��ͼ���Ƴ�ĳ����
	 * 
	 * @param x
	 *            �ߵ�ĳ�˵�һ������
	 * @param a
	 *            �ߵı��
	 * @param y
	 *            �ߵ���һ�˵�һ������
	 */
	public void removeEdge(int x, int a, int y) {
		int t;

		for (int i = 0; i < nodeLabels.size(); i++) {
			// ��Ѱ��2���˵���,t����ҵ��ĵ������һ���˵���
			if (nodeLabels.get(i) == x) {
				t = y;
			} else if (nodeLabels.get(i) == y) {
				t = x;
			} else {
				continue;
			}

			for (int j = 0; j < edgeNexts.get(i).size(); j++) {
				// �Ӵ˶˵�������ӵĵ�ȥ�Ƚ϶�Ӧ�ĵ�ͱ�
				if (edgeLabels.get(i).get(j) == a
						&& nodeLabels.get(edgeNexts.get(i).get(j)) == t) {
					int id;
					// �����ӵĵ���ȥ��õ�
					edgeLabels.get(i).remove(j);

					id = edgeNexts.get(i).get(j);
					edgeNexts.get(i).remove(j);
					for (int k = 0; k < edgeNexts.get(id).size(); k++) {
						if (edgeNexts.get(id).get(k) == i) {
							edgeNexts.get(id).remove(k);
							break;
						}
					}
					break;
				}
			}
		}

	}

	/**
	 * ���ͼ��ݹ���һ��ͼ
	 * 
	 * @param gd
	 *            ͼ���
	 * @return
	 */
	public Graph constructGraph(GraphData gd) {
		Graph graph = new Graph();

		
		// ����һ��ͼ��Ҫ֪��3�㣬1.ͼ������Щ��2.ͼ�е�ÿ������Χ������Щ��3.ÿ������Χ������Щ��
		for (int i = 0; i < gd.getNodeVisibles().size(); i++) {
			if (gd.getNodeVisibles().get(i)) {
				graph.getNodeLabels().add(gd.getNodeLabels().get(i));
			}
			
			// ��Ӷ�Ӧid�µļ���
			// id�ڵ���ж��������ıߵı��
			graph.edgeLabels.add(new ArrayList<Integer>());
			// id�ڵ���ж��������Ľڵ��id
			graph.edgeNexts.add(new ArrayList<Integer>());
		}

		for (int i = 0; i < gd.getEdgeLabels().size(); i++) {
			if (gd.getEdgeVisibles().get(i)) {
				// �ڴ˺������һ���߱��
				graph.edgeLabels.get(gd.getEdgeX().get(i)).add(gd.getEdgeLabels().get(i));
				graph.edgeLabels.get(gd.getEdgeY().get(i)).add(gd.getEdgeLabels().get(i));
				graph.edgeNexts.get(gd.getEdgeX().get(i)).add(
						gd.getEdgeY().get(i));
				graph.edgeNexts.get(gd.getEdgeY().get(i)).add(
						gd.getEdgeX().get(i));
			}
		}

		return graph;
	}
}
