package SequentialPatterns.DataMining_GSP;

import java.util.ArrayList;

/**
 * �����е����
 * 
 * @author maxu
 * 
 */
public class ItemSet {
	/**
	 * ��б����������������
	 */
	private ArrayList<Integer> items;

	public ItemSet(String[] itemStr) {
		items = new ArrayList<>();
		for (String s : itemStr) {
			items.add(Integer.parseInt(s));
		}
	}

	public ItemSet(int[] itemNum) {
		items = new ArrayList<>();
		for (int num : itemNum) {
			items.add(num);
		}
	}
	
	public ItemSet(ArrayList<Integer> itemNum) {
		this.items = itemNum;
	}

	public ArrayList<Integer> getItems() {
		return items;
	}

	public void setItems(ArrayList<Integer> items) {
		this.items = items;
	}

	/**
	 * �ж�2����Ƿ����
	 * 
	 * @param itemSet
	 *            �Ƚ϶���
	 * @return
	 */
	public boolean compareIsSame(ItemSet itemSet) {
		boolean result = true;

		if (this.items.size() != itemSet.items.size()) {
			return false;
		}

		for (int i = 0; i < itemSet.items.size(); i++) {
			if (this.items.get(i) != itemSet.items.get(i)) {
				// ֻҪ��ֵ����ȣ�ֱ�����������
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * �������ͬ������һ��
	 * 
	 * @return
	 */
	public ArrayList<Integer> copyItems() {
		ArrayList<Integer> copyItems = new ArrayList<>();

		for (int num : this.items) {
			copyItems.add(num);
		}

		return copyItems;
	}
}
