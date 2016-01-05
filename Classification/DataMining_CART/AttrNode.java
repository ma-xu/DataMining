package Classification.DataMining_CART;

import java.util.ArrayList;

/**
 * �ع�������ڵ�
 * 
 * @author maxu
 * 
 */
public class AttrNode {
	// �ڵ���������
	private String attrName;
	// �ڵ�������
	private int nodeIndex;
	//���Ҷ�ӽڵ���
	private int leafNum;
	// �ڵ������
	private double alpha;
	// ���׷�������ֵ
	private String parentAttrValue;
	// ���ӽڵ�
	private AttrNode[] childAttrNode;
	// ��ݼ�¼����
	private ArrayList<String> dataIndex;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public int getNodeIndex() {
		return nodeIndex;
	}

	public void setNodeIndex(int nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public String getParentAttrValue() {
		return parentAttrValue;
	}

	public void setParentAttrValue(String parentAttrValue) {
		this.parentAttrValue = parentAttrValue;
	}

	public AttrNode[] getChildAttrNode() {
		return childAttrNode;
	}

	public void setChildAttrNode(AttrNode[] childAttrNode) {
		this.childAttrNode = childAttrNode;
	}

	public ArrayList<String> getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(ArrayList<String> dataIndex) {
		this.dataIndex = dataIndex;
	}

	public int getLeafNum() {
		return leafNum;
	}

	public void setLeafNum(int leafNum) {
		this.leafNum = leafNum;
	}
	
	
	
}
