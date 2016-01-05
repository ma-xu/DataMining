package Classification.DataMining_ID3;

import java.util.ArrayList;

/**
 * ���Խڵ㣬����Ҷ�ӽڵ�
 * @author lyq
 *
 */
public class AttrNode {
	//��ǰ���Ե�����
	private String attrName;
	//���ڵ�ķ�������ֵ
	private String parentAttrValue;
	//�����ӽڵ�
	private AttrNode[] childAttrNode;
	//����Ҷ�ӽڵ�
	private ArrayList<String> childDataIndex;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public AttrNode[] getChildAttrNode() {
		return childAttrNode;
	}

	public void setChildAttrNode(AttrNode[] childAttrNode) {
		this.childAttrNode = childAttrNode;
	}

	public String getParentAttrValue() {
		return parentAttrValue;
	}

	public void setParentAttrValue(String parentAttrValue) {
		this.parentAttrValue = parentAttrValue;
	}

	public ArrayList<String> getChildDataIndex() {
		return childDataIndex;
	}

	public void setChildDataIndex(ArrayList<String> childDataIndex) {
		this.childDataIndex = childDataIndex;
	}
}
