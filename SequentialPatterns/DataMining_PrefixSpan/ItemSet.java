package SequentialPatterns.DataMining_PrefixSpan;

import java.util.ArrayList;

/**
 * �ַ����
 * 
 * @author lyq
 * 
 */
public class ItemSet {
	// ��ڵ��ַ�
	private ArrayList<String> items;

	public ItemSet(String[] str) {
		items = new ArrayList<>();
		for (String s : str) {
			items.add(s);
		}
	}

	public ItemSet(ArrayList<String> itemsList) {
		this.items = itemsList;
	}

	public ItemSet(String s) {
		items = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) {
			items.add(s.charAt(i) + "");
		}
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}

	/**
	 * ��ȡ����1��Ԫ��
	 * 
	 * @return
	 */
	public String getLastValue() {
		int size = this.items.size();

		return this.items.get(size - 1);
	}
}
