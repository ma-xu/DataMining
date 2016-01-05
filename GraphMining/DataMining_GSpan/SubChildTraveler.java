package GraphMining.DataMining_GSpan;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ����ͼ��Ѱ�࣬�ڵ�ǰ�ߵĻ���Ѱ�ҿ��ܵĺ��ӱ�
 * 
 * @author lyq
 * 
 */
public class SubChildTraveler {
	// ��ǰ����Ԫ���
	ArrayList<Edge> edgeSeq;
	// ��ǰ��ͼ
	Graph graph;
	// �����ݣ����ӱ߶�������ͼid��
	ArrayList<Edge> childEdge;
	// ͼ�ĵ�id����Ԫ��id��ʶ��ӳ��
	int[] g2s;
	// ��Ԫ��id��ʶ��ͼ�ĵ�id��ӳ��
	int[] s2g;
	// ͼ�б��Ƿ��õ����
	boolean f[][];
	// ����·����rm[id]��ʾ���Ǵ�id�ڵ�������·���е���һ���ڵ�id
	int[] rm;
	// ��һ����Ԫ���id
	int next;

	public SubChildTraveler(ArrayList<Edge> edgeSeq, Graph graph) {
		this.edgeSeq = edgeSeq;
		this.graph = graph;
		this.childEdge = new ArrayList<>();
	}

	/**
	 * ��ͼ���������ܴ��ڵĺ��ӱ�
	 * 
	 * @param next
	 *            �¼���ߵĽڵ㽫���õ�id
	 */
	public void traveler() {
		this.next = edgeSeq.size() + 1;
		int size = graph.nodeLabels.size();
		// ��idӳ��ĳ�ʼ������
		g2s = new int[size];
		s2g = new int[size];
		f = new boolean[size][size];

		for (int i = 0; i < size; i++) {
			g2s[i] = -1;
			s2g[i] = -1;

			for (int j = 0; j < size; j++) {
				// ����idΪi��idΪj��˱�û�б��ù�
				f[i][j] = false;
			}
		}

		rm = new int[edgeSeq.size()+1];
		for (int i = 0; i < edgeSeq.size()+1; i++) {
			rm[i] = -1;
		}
		// Ѱ������·��
		for (Edge e : edgeSeq) {
			if (e.ix < e.iy && e.iy > rm[e.ix]) {
				rm[e.ix] = e.iy;
			}
		}

		for (int i = 0; i < size; i++) {
			// Ѱ�ҵ�һ�������ȵĵ�
			if (edgeSeq.get(0).x != graph.nodeLabels.get(i)) {
				continue;
			}

			g2s[i] = 0;
			s2g[0] = i;
			dfsSearchEdge(0);
			g2s[i] = -1;
			s2g[0] = -1;
		}

	}

	/**
	 * �ڵ�ǰͼ���������Ѱ����ȷ����ͼ
	 * 
	 * @param currentPosition
	 *            ��ǰ�ҵ���λ��
	 */
	public void dfsSearchEdge(int currentPosition) {
		int rmPosition = 0;
		// ����ҵ����ˣ����ڵ�ǰ����ͼ������·����Ѱ�ҿ��ܵı�
		if (currentPosition >= edgeSeq.size()) {
			rmPosition = 0;
			while (rmPosition >= 0) {
				int gId = s2g[rmPosition];
				// �ڴ˵㸽��Ѱ�ҿ��ܵı�
				for (int i = 0; i < graph.edgeNexts.get(gId).size(); i++) {
					int gId2 = graph.edgeNexts.get(gId).get(i);
					// ����������Ѿ����ù�
					if (f[gId][gId2] || f[gId][gId2]) {
						continue;
					}

					// ������·������ӱ߷�Ϊ2���������һ��Ϊ�����ҽڵ�����ӣ��ڶ���Ϊ������·���� �ĵ����
					// ����ҵ��ĵ�û�б��ù���Խ��бߵ���չ
					if (g2s[gId2] < 0) {
						g2s[gId2] = next;
						Edge e = new Edge(g2s[gId], g2s[gId2],
								graph.nodeLabels.get(gId), graph.edgeLabels
										.get(gId).get(i),
								graph.nodeLabels.get(gId2));
						// ���½����ӱ߼��뼯��
						childEdge.add(e);
					} else {
						boolean flag = true;
						// �������Ѿ����ڣ��ж����ǲ������ҵĵ�
						for (int j = 0; j < graph.edgeNexts.get(gId2).size(); j++) {
							int tempId = graph.edgeNexts.get(gId2).get(j);
							if (g2s[gId2] < g2s[tempId]) {
								flag = false;
								break;
							}
						}

						if (flag) {
							Edge e = new Edge(g2s[gId], g2s[gId2],
									graph.nodeLabels.get(gId), graph.edgeLabels
											.get(gId).get(i),
									graph.nodeLabels.get(gId2));
							// ���½����ӱ߼��뼯��
							childEdge.add(e);
						}
					}
				}
				// һ������·���ϵ����꣬������һ��
				rmPosition = rm[rmPosition];
			}
			return;
		}

		Edge e = edgeSeq.get(currentPosition);
		// �����ӵĵ���
		int y = e.y;
		// �����ӵı߱��
		int a = e.a;
		int gId1 = s2g[e.ix];
		int gId2 = 0;

		for (int i = 0; i < graph.edgeLabels.get(gId1).size(); i++) {
			// �ж������ӵı߶�Ӧ�ı��
			if (graph.edgeLabels.get(gId1).get(i) != a) {
				continue;
			}

			// �ж������ӵĵ�ı��
			int tempId = graph.edgeNexts.get(gId1).get(i);
			if (graph.nodeLabels.get(tempId) != y) {
				continue;
			}

			gId2 = tempId;
			// �����������û�����ù��
			if (g2s[gId2] == -1 && s2g[e.iy] == -1) {
				g2s[gId2] = e.iy;
				s2g[e.iy] = gId2;
				f[gId1][gId2] = true;
				f[gId2][gId1] = true;
				dfsSearchEdge(currentPosition + 1);
				f[gId1][gId2] = false;
				f[gId2][gId1] = false;
				g2s[gId2] = -1;
				s2g[e.iy] = -1;
			} else {
				if (g2s[gId2] != e.iy) {
					continue;
				}
				if (s2g[e.iy] != gId2) {
					continue;
				}
				f[gId1][gId2] = true;
				f[gId2][gId1] = true;
				dfsSearchEdge(currentPosition);
				f[gId1][gId2] = false;
				f[gId2][gId1] = false;
			}
		}

	}

	/**
	 * ��ȡ�����ݶ�
	 * 
	 * @return
	 */
	public ArrayList<Edge> getResultChildEdge() {
		return this.childEdge;
	}

}
