package Others.DataMining_BayesNetwork;

import java.util.ArrayList;

/**
 * ��Ҷ˹����ڵ���
 * 
 * @author maxu
 * 
 */
public class Node {
	// �ڵ���������
	String name;
	// �ڵ�ĸ��׽ڵ㣬Ҳ�������νڵ㣬���ܶ��
	ArrayList<Node> parentNodes;
	// �ڵ���ӽڵ㣬Ҳ�������νڵ㣬���ܶ��
	ArrayList<Node> childNodes;

	public Node(String name) {
		this.name = name;

		// ��ʼ������
		this.parentNodes = new ArrayList<>();
		this.childNodes = new ArrayList<>();
	}

	/**
	 * ������ڵ����ӵ�Ŀ���Ľڵ�
	 * 
	 * @param node
	 *            ���νڵ�
	 */
	public void connectNode(Node node) {
		// �����νڵ��������ڵ�ĺ��ӽڵ���
		this.childNodes.add(node);
		// ������ڵ���뵽���νڵ�ĸ��ڵ���
		node.parentNodes.add(this);
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
		if (this.name.equals(node.name)) {
			isEqual = true;
		}

		return isEqual;
	}
}
