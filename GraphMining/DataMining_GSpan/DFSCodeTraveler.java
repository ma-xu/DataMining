package GraphMining.DataMining_GSpan;

import java.util.ArrayList;
import java.util.Stack;

/**
 * ͼ����������������࣬�жϵ�ǰ�����ڸ�ͼ���Ƿ�Ϊ��С����
 * 
 * @author lyq
 * 
 */
public class DFSCodeTraveler {
	// ��ǰ�ı����Ƿ�Ϊ���±����ʶ
	boolean isMin;
	// ��ǰ�ھ��ͼ�ı���Ԫ�������
	ArrayList<Edge> edgeSeqs;
	// ��ǰ��ͼ�ṹ
	Graph graph;
	// ͼ�ڵ�id��Ӧ�ı���Ԫ���е�id��ʶ
	int[] g2s;
	// ���ͼ�еı��Ƿ��õ���
	boolean f[][];

	public DFSCodeTraveler(ArrayList<Edge> edgeSeqs, Graph graph) {
		this.isMin = true;
		this.edgeSeqs = edgeSeqs;
		this.graph = graph;
	}

	public void traveler() {
		int nodeLNums = graph.nodeLabels.size();
		g2s = new int[nodeLNums];
		for (int i = 0; i < nodeLNums; i++) {
			// ����-1���˵㻹δ���������
			g2s[i] = -1;
		}

		f = new boolean[nodeLNums][nodeLNums];
		for (int i = 0; i < nodeLNums; i++) {
			for (int j = 0; j < nodeLNums; j++) {
				f[i][j] = false;
			}
		}

		// ��ÿ���㿪ʼѰ����С������Ԫ��
		for (int i = 0; i < nodeLNums; i++) {
			//��ѡ��ĵ�һ����ı�����ж�
			if(graph.getNodeLabels().get(i) > edgeSeqs.get(0).x){
				continue;
			}
			// ��Ԫ��id��0��ʼ����
			g2s[i] = 0;

			Stack<Integer> s = new Stack<>();
			s.push(i);
			dfsSearch(s, 0, 1);
			if (!isMin) {
				return;
			}
			g2s[i] = -1;
		}
	}

	/**
	 * �������������С������
	 * 
	 * @param stack
	 *            ����Ľڵ�idջ
	 * @param currentPosition
	 *            ��ǰ���еĲ�Σ�����ҵ��ĵڼ�����
	 * @param next
	 *            ��Ԫ�����һ���ߵĵ����ʱ��ʶ
	 */
	private void dfsSearch(Stack<Integer> stack, int currentPosition, int next) {
		if (currentPosition >= edgeSeqs.size()) {
			stack.pop();
			// �Ƚϵ������򷵻�
			return;
		}

		while (!stack.isEmpty()) {
			int x = stack.pop();
			for (int i = 0; i < graph.edgeNexts.get(x).size(); i++) {
				// �Ӵ�id�ڵ������ӵĵ���ѡȡ1������Ϊ��һ����
				int y = graph.edgeNexts.get(x).get(i);
				// �����2������ɵı��Ѿ����ù������
				if (f[x][y] || f[y][x]) {
					continue;
				}

				// ���y�����δ���ù�
				if (g2s[y] < 0) {
					// �½���������Ԫ��
					Edge e = new Edge(g2s[x], next, graph.nodeLabels.get(x),
							graph.edgeLabels.get(x).get(i),
							graph.nodeLabels.get(y));

					// ����Ӧλ�õı����Ƚϣ��������С��ʧ��
					int compareResult = e.compareWith(edgeSeqs
							.get(currentPosition));
					if (compareResult == Edge.EDGE_SMALLER) {
						isMin = false;
						return;
					} else if (compareResult == Edge.EDGE_LARGER) {
						continue;
					}
					// ������������
					g2s[y] = next;
					f[x][y] = true;
					f[y][x] = true;
					stack.push(y);
					dfsSearch(stack, currentPosition + 1, next + 1);
					if (!isMin) {
						return;
					}
					f[x][y] = false;
					f[y][x] = false;
					g2s[y] = -1;
				} else {
					// ������Ѿ����ù��ʱ�򣬲���Ҫ��������Ԫ��id��ʶ
					// �½���������Ԫ��
					Edge e = new Edge(g2s[x], g2s[y], graph.nodeLabels.get(x),
							graph.edgeLabels.get(x).get(i),
							graph.nodeLabels.get(y));

					// ����Ӧλ�õı����Ƚϣ��������С��ʧ��
					int compareResult = e.compareWith(edgeSeqs
							.get(currentPosition));
					if (compareResult == Edge.EDGE_SMALLER) {
						isMin = false;
						return;
					} else if (compareResult == Edge.EDGE_LARGER) {
						continue;
					}
					// ������������
					g2s[y] = next;
					f[x][y] = true;
					f[y][x] = true;
					stack.push(y);
					dfsSearch(stack, currentPosition + 1, next);
					if (!isMin) {
						return;
					}
					f[x][y] = false;
					f[y][x] = false;
				}
			}
		}
	}
}
