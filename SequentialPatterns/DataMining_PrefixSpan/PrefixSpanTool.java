package SequentialPatterns.DataMining_PrefixSpan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PrefixSpanTool����ģʽ�����㷨������
 * 
 * @author lyq
 * 
 */
public class PrefixSpanTool {
	// ��������ļ���ַ
	private String filePath;
	// ��С֧�ֶ���ֵ����
	private double minSupportRate;
	// ��С֧�ֶȣ�ͨ���������������ֵ�������
	private int minSupport;
	// ԭʼ������
	private ArrayList<Sequence> totalSeqs;
	// �ھ������������Ƶ��ģʽ
	private ArrayList<Sequence> totalFrequentSeqs;
	// ���еĵ�һ����ڵݹ�ö��
	private ArrayList<String> singleItems;

	public PrefixSpanTool(String filePath, double minSupportRate) {
		this.filePath = filePath;
		this.minSupportRate = minSupportRate;
		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	private void readDataFile() {
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		minSupport = (int) (dataArray.size() * minSupportRate);
		totalSeqs = new ArrayList<>();
		totalFrequentSeqs = new ArrayList<>();
		Sequence tempSeq;
		ItemSet tempItemSet;
		for (String[] str : dataArray) {
			tempSeq = new Sequence();
			for (String s : str) {
				tempItemSet = new ItemSet(s);
				tempSeq.getItemSetList().add(tempItemSet);
			}
			totalSeqs.add(tempSeq);
		}

		System.out.println("ԭʼ������ݣ�");
		outputSeqence(totalSeqs);
	}

	/**
	 * ��������б�����
	 * 
	 * @param seqList
	 *            ����������б�
	 */
	private void outputSeqence(ArrayList<Sequence> seqList) {
		for (Sequence seq : seqList) {
			System.out.print("<");
			for (ItemSet itemSet : seq.getItemSetList()) {
				if (itemSet.getItems().size() > 1) {
					System.out.print("(");
				}

				for (String s : itemSet.getItems()) {
					System.out.print(s + " ");
				}

				if (itemSet.getItems().size() > 1) {
					System.out.print(")");
				}
			}
			System.out.println(">");
		}
	}

	/**
	 * �Ƴ��ʼ�����в�������С֧�ֶ���ֵ�ĵ���
	 */
	private void removeInitSeqsItem() {
		int count = 0;
		HashMap<String, Integer> itemMap = new HashMap<>();
		singleItems = new ArrayList<>();

		for (Sequence seq : totalSeqs) {
			for (ItemSet itemSet : seq.getItemSetList()) {
				for (String s : itemSet.getItems()) {
					if (!itemMap.containsKey(s)) {
						itemMap.put(s, 1);
					}
				}
			}
		}

		String key;
		for (Map.Entry entry : itemMap.entrySet()) {
			count = 0;
			key = (String) entry.getKey();
			for (Sequence seq : totalSeqs) {
				if (seq.strIsContained(key)) {
					count++;
				}
			}

			itemMap.put(key, count);

		}

		for (Map.Entry entry : itemMap.entrySet()) {
			key = (String) entry.getKey();
			count = (int) entry.getValue();

			if (count < minSupport) {
				// ���֧�ֶ���ֵС����õ���С֧�ֶ���ֵ����ɾ�����
				for (Sequence seq : totalSeqs) {
					seq.deleteSingleItem(key);
				}
			} else {
				singleItems.add(key);
			}
		}

		Collections.sort(singleItems);
	}

	/**
	 * �ݹ�������������������ģʽ
	 * 
	 * @param beforeSeq
	 *            ǰ׺����
	 * @param afterSeqList
	 *            ��׺�����б�
	 */
	private void recursiveSearchSeqs(Sequence beforeSeq,
			ArrayList<Sequence> afterSeqList) {
		ItemSet tempItemSet;
		Sequence tempSeq2;
		Sequence tempSeq;
		ArrayList<Sequence> tempSeqList = new ArrayList<>();

		for (String s : singleItems) {
			// �ֳ�2����ʽ�ݹ飬��<a>Ϊ��ʼ���һ��ֱ�Ӽ�����������<a,a>,<a,b> <a,c>..
			if (isLargerThanMinSupport(s, afterSeqList)) {
				tempSeq = beforeSeq.copySeqence();
				tempItemSet = new ItemSet(s);
				tempSeq.getItemSetList().add(tempItemSet);

				totalFrequentSeqs.add(tempSeq);

				tempSeqList = new ArrayList<>();
				for (Sequence seq : afterSeqList) {
					if (seq.strIsContained(s)) {
						tempSeq2 = seq.extractItem(s);
						tempSeqList.add(tempSeq2);
					}
				}

				recursiveSearchSeqs(tempSeq, tempSeqList);
			}

			// �ڶ��ֵݹ�Ϊ��Ԫ�ص���ݼ������������aΪ��<(aa)>,<(ab)>,<(ac)>...
			// a������������Ϊһ��ǰ׺���У���������ǵ���Ԫ�ػ����Ѿ��Ƕ�Ԫ�ص��
			tempSeq = beforeSeq.copySeqence();
			int size = tempSeq.getItemSetList().size();
			tempItemSet = tempSeq.getItemSetList().get(size - 1);
			tempItemSet.getItems().add(s);

			if (isLargerThanMinSupport(tempItemSet, afterSeqList)) {
				tempSeqList = new ArrayList<>();
				for (Sequence seq : afterSeqList) {
					if (seq.compoentItemIsContain(tempItemSet)) {
						tempSeq2 = seq.extractCompoentItem(tempItemSet
								.getItems());
						tempSeqList.add(tempSeq2);
					}
				}
				totalFrequentSeqs.add(tempSeq);

				recursiveSearchSeqs(tempSeq, tempSeqList);
			}
		}
	}

	/**
	 * ��������������������е�֧�ֶ��Ƿ񳬹���ֵ
	 * 
	 * @param s
	 *            ����ƥ�����
	 * @param seqList
	 *            �Ƚ��������
	 * @return
	 */
	private boolean isLargerThanMinSupport(String s, ArrayList<Sequence> seqList) {
		boolean isLarge = false;
		int count = 0;

		for (Sequence seq : seqList) {
			if (seq.strIsContained(s)) {
				count++;
			}
		}

		if (count >= minSupport) {
			isLarge = true;
		}

		return isLarge;
	}

	/**
	 * ����������������е�֧�ֶ��Ƿ������ֵ
	 * 
	 * @param itemSet
	 *            ���Ԫ���
	 * @param seqList
	 *            �Ƚϵ������б�
	 * @return
	 */
	private boolean isLargerThanMinSupport(ItemSet itemSet,
			ArrayList<Sequence> seqList) {
		boolean isLarge = false;
		int count = 0;

		if (seqList == null) {
			return false;
		}

		for (Sequence seq : seqList) {
			if (seq.compoentItemIsContain(itemSet)) {
				count++;
			}
		}

		if (count >= minSupport) {
			isLarge = true;
		}

		return isLarge;
	}

	/**
	 * ����ģʽ��������
	 */
	public void prefixSpanCalculate() {
		Sequence seq;
		Sequence tempSeq;
		ArrayList<Sequence> tempSeqList = new ArrayList<>();
		ItemSet itemSet;
		removeInitSeqsItem();

		for (String s : singleItems) {
			// ���ʼ��a,b,d��ʼ�ݹ�����Ѱ��Ƶ������ģʽ
			seq = new Sequence();
			itemSet = new ItemSet(s);
			seq.getItemSetList().add(itemSet);

			if (isLargerThanMinSupport(s, totalSeqs)) {
				tempSeqList = new ArrayList<>();
				for (Sequence s2 : totalSeqs) {
					// �жϵ�һ���Ƿ�����������У���Ž�����ȡ����
					if (s2.strIsContained(s)) {
						tempSeq = s2.extractItem(s);
						tempSeqList.add(tempSeq);
					}
				}

				totalFrequentSeqs.add(seq);
				recursiveSearchSeqs(seq, tempSeqList);
			}
		}

		printTotalFreSeqs();
	}

	/**
	 * ��ģʽ������Ƶ������ģʽ
	 */
	private void printTotalFreSeqs() {
		System.out.println("����ģʽ�ھ���");
		
		ArrayList<Sequence> seqList;
		HashMap<String, ArrayList<Sequence>> seqMap = new HashMap<>();
		for (String s : singleItems) {
			seqList = new ArrayList<>();
			for (Sequence seq : totalFrequentSeqs) {
				if (seq.getItemSetList().get(0).getItems().get(0).equals(s)) {
					seqList.add(seq);
				}
			}
			seqMap.put(s, seqList);
		}

		int count = 0;
		for (String s : singleItems) {
			count = 0;
			System.out.println();
			System.out.println();

			seqList = (ArrayList<Sequence>) seqMap.get(s);
			for (Sequence tempSeq : seqList) {
				count++;
				System.out.print("<");
				for (ItemSet itemSet : tempSeq.getItemSetList()) {
					if (itemSet.getItems().size() > 1) {
						System.out.print("(");
					}

					for (String str : itemSet.getItems()) {
						System.out.print(str + " ");
					}

					if (itemSet.getItems().size() > 1) {
						System.out.print(")");
					}
				}
				System.out.print(">, ");

				// ÿ5�����л�һ��
				if (count == 5) {
					count = 0;
					System.out.println();
				}
			}

		}
	}

}
