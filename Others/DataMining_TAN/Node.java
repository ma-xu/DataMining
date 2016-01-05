package Others.DataMining_TAN;

import java.util.ArrayList;

/**
 * ��Ҷ˹����ڵ���
 * 
 * @author lyq
 * 
 */
public class Node {
	//�ڵ�Ψһid���������ڵ����ӷ����ȷ��
	int id;
	// �ڵ���������
	String name;
	// �ýڵ�������Ľڵ�
	ArrayList<Node> connectedNodes;

	public Node(int id, String name) {
		this.id = id;
		this.name = name;

		// ��ʼ������
		this.connectedNodes = new ArrayList<>();
	}

	/**
	 * ������ڵ����ӵ�Ŀ���Ľڵ�
	 * 
	 * @param node
	 *            ���νڵ�
	 */
	public void connectNode(Node node) {
		//������������
		if(this.id == node.id){
			return;
		}
		
		// ���ڵ��������ڵ�Ľڵ��б���
		this.connectedNodes.add(node);
		// ������ڵ���뵽Ŀ��ڵ���б���
		node.connectedNodes.add(this);
	}

	/**
	 * �ж���Ŀ��ڵ��Ƿ���ͬ����Ҫ�Ƚ�����Ƿ���ͬ����
	 * 
	 * @param node
	 *            Ŀ����
	 * @return
	 */
	public boolean isEqual(Node node) {
		boolean isEqual;

		isEqual = false;
		// �ڵ������ͬ����Ϊ���
		if (this.id == node.id) {
			isEqual = true;
		}

		return isEqual;
	}
}
