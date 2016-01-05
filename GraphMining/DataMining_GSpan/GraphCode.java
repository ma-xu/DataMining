package GraphMining.DataMining_GSpan;

import java.util.ArrayList;

/**
 * ͼ������
 * @author lyq
 *
 */
public class GraphCode {
	//�ߵļ��ϣ��ߵ��������űߵ���Ӵ���
	ArrayList<Edge> edgeSeq;
	//ӵ����Щ�ߵ�ͼ��id
	ArrayList<Integer> gs;
	
	public GraphCode() {
		this.edgeSeq = new ArrayList<>();
		this.gs = new ArrayList<>();
	}

	public ArrayList<Edge> getEdgeSeq() {
		return edgeSeq;
	}

	public void setEdgeSeq(ArrayList<Edge> edgeSeq) {
		this.edgeSeq = edgeSeq;
	}

	public ArrayList<Integer> getGs() {
		return gs;
	}

	public void setGs(ArrayList<Integer> gs) {
		this.gs = gs;
	}
}
