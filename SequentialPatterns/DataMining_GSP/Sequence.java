package SequentialPatterns.DataMining_GSP;

import java.util.ArrayList;

/**
 * ���У�ÿ�������ڲ������ItemSet�
 * 
 * @author maxu
 * 
 */
public class Sequence implements Comparable<Sequence>, Cloneable {
	// ������������ID
	private int trsanctionID;
	// ��б�
	private ArrayList<ItemSet> itemSetList;

	public Sequence(int trsanctionID) {
		this.trsanctionID = trsanctionID;
		this.itemSetList = new ArrayList<>();
	}

	public Sequence() {
		this.itemSetList = new ArrayList<>();
	}

	public int getTrsanctionID() {
		return trsanctionID;
	}

	public void setTrsanctionID(int trsanctionID) {
		this.trsanctionID = trsanctionID;
	}

	public ArrayList<ItemSet> getItemSetList() {
		return itemSetList;
	}

	public void setItemSetList(ArrayList<ItemSet> itemSetList) {
		this.itemSetList = itemSetList;
	}

	/**
	 * ȡ�������е�һ����ĵ�һ��Ԫ��
	 * 
	 * @return
	 */
	public Integer getFirstItemSetNum() {
		return this.getItemSetList().get(0).getItems().get(0);
	}

	/**
	 * ��ȡ���������һ���
	 * 
	 * @return
	 */
	public ItemSet getLastItemSet() {
		return getItemSetList().get(getItemSetList().size() - 1);
	}

	/**
	 * ��ȡ���������һ��������һ��һ��Ԫ��
	 * 
	 * @return
	 */
	public Integer getLastItemSetNum() {
		ItemSet lastItemSet = getItemSetList().get(getItemSetList().size() - 1);
		int lastItemNum = lastItemSet.getItems().get(
				lastItemSet.getItems().size() - 1);

		return lastItemNum;
	}

	/**
	 * �ж����������һ����Ƿ�Ϊ��һ��ֵ
	 * 
	 * @return
	 */
	public boolean isLastItemSetSingleNum() {
		ItemSet lastItemSet = getItemSetList().get(getItemSetList().size() - 1);
		int size = lastItemSet.getItems().size();

		return size == 1 ? true : false;
	}

	@Override
	public int compareTo(Sequence o) {
		// TODO Auto-generated method stub
		return this.getFirstItemSetNum().compareTo(o.getFirstItemSetNum());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	/**
	 * ����һ��һģһ�������
	 */
	public Sequence copySeqence(){
		Sequence copySeq = new Sequence();
		for(ItemSet itemSet: this.itemSetList){
			copySeq.getItemSetList().add(new ItemSet(itemSet.copyItems()));
		}
		
		return copySeq;
	}

	/**
	 * �Ƚ�2�������Ƿ���ȣ���Ҫ�ж��ڲ���ÿ����Ƿ���ȫһ��
	 * 
	 * @param seq
	 *            �Ƚϵ����ж���
	 * @return
	 */
	public boolean compareIsSame(Sequence seq) {
		boolean result = true;
		ArrayList<ItemSet> itemSetList2 = seq.getItemSetList();
		ItemSet tempItemSet1;
		ItemSet tempItemSet2;

		if (itemSetList2.size() != this.itemSetList.size()) {
			return false;
		}
		for (int i = 0; i < itemSetList2.size(); i++) {
			tempItemSet1 = this.itemSetList.get(i);
			tempItemSet2 = itemSetList2.get(i);

			if (!tempItemSet1.compareIsSame(tempItemSet2)) {
				// ֻҪ����ȣ�ֱ���˳�����
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * ��ɴ����е�����������
	 * 
	 * @return
	 */
	public ArrayList<Sequence> createChildSeqs() {
		ArrayList<Sequence> childSeqs = new ArrayList<>();
		ArrayList<Integer> tempItems;
		Sequence tempSeq = null;
		ItemSet tempItemSet;

		for (int i = 0; i < this.itemSetList.size(); i++) {
			tempItemSet = itemSetList.get(i);
			if (tempItemSet.getItems().size() == 1) {
				tempSeq = this.copySeqence();
				
				// ���ֻ�����ֻ��1��Ԫ�أ���ֱ���Ƴ�
				tempSeq.itemSetList.remove(i);
				childSeqs.add(tempSeq);
			} else {
				tempItems = tempItemSet.getItems();
				for (int j = 0; j < tempItems.size(); j++) {
					tempSeq = this.copySeqence();

					// �ڿ������������Ƴ�һ������
					tempSeq.getItemSetList().get(i).getItems().remove(j);
					childSeqs.add(tempSeq);
				}
			}
		}

		return childSeqs;
	}

}
