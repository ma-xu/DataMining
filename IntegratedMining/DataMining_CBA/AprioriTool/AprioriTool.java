package IntegratedMining.DataMining_CBA.AprioriTool;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * apriori�㷨������
 * 
 * @author lyq
 * 
 */
public class AprioriTool {
	// ��С֧�ֶȼ���
	private int minSupportCount;
	// ÿ�������е���ƷID
	private ArrayList<String[]> totalGoodsIDs;
	// ����м������������Ƶ����б�
	private ArrayList<FrequentItem> resultItem;
	// ����м������Ƶ�����ID����
	private ArrayList<String[]> resultItemID;

	public AprioriTool(ArrayList<String[]> totalGoodsIDs, int minSupportCount) {
		this.totalGoodsIDs = totalGoodsIDs;
		this.minSupportCount = minSupportCount;
	}


	/**
	 * �ж��ַ�����array2�Ƿ��������array1��
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public boolean iSStrContain(String[] array1, String[] array2) {
		if (array1 == null || array2 == null) {
			return false;
		}

		boolean iSContain = false;
		for (String s : array2) {
			// �µ���ĸ�Ƚ�ʱ�����³�ʼ������
			iSContain = false;
			// �ж�array2��ÿ���ַ�ֻҪ������array1�� �������
			for (String s2 : array1) {
				if (s.equals(s2)) {
					iSContain = true;
					break;
				}
			}

			// ����Ѿ��жϳ������ˣ���ֱ���ж�ѭ��
			if (!iSContain) {
				break;
			}
		}

		return iSContain;
	}

	/**
	 * �������������
	 */
	public void computeLink() {
		// ���Ӽ������ֹ��k������㵽k-1���Ϊֹ
		int endNum = 0;
		// ��ǰ�Ѿ������������㵽���,��ʼʱ����1�
		int currentNum = 1;
		// ��Ʒ��1Ƶ���ӳ��ͼ
		HashMap<String, FrequentItem> itemMap = new HashMap<>();
		FrequentItem tempItem;
		// ��ʼ�б�
		ArrayList<FrequentItem> list = new ArrayList<>();
		// ����������������Ľ���
		resultItem = new ArrayList<>();
		resultItemID = new ArrayList<>();
		// ��ƷID������
		ArrayList<String> idType = new ArrayList<>();
		for (String[] a : totalGoodsIDs) {
			for (String s : a) {
				if (!idType.contains(s)) {
					tempItem = new FrequentItem(new String[] { s }, 1);
					idType.add(s);
					resultItemID.add(new String[] { s });
				} else {
					// ֧�ֶȼ����1
					tempItem = itemMap.get(s);
					tempItem.setCount(tempItem.getCount() + 1);
				}
				itemMap.put(s, tempItem);
			}
		}
		// ����ʼƵ���ת�뵽�б��У��Ա��������������
		for (Map.Entry entry : itemMap.entrySet()) {
			list.add((FrequentItem) entry.getValue());
		}
		// ������ƷID�������򣬷������Ӽ�����᲻һ�£��������
		Collections.sort(list);
		resultItem.addAll(list);

		String[] array1;
		String[] array2;
		String[] resultArray;
		ArrayList<String> tempIds;
		ArrayList<String[]> resultContainer;
		// �ܹ�Ҫ�㵽endNum�
		endNum = list.size() - 1;

		while (currentNum < endNum) {
			resultContainer = new ArrayList<>();
			for (int i = 0; i < list.size() - 1; i++) {
				tempItem = list.get(i);
				array1 = tempItem.getIdArray();
				for (int j = i + 1; j < list.size(); j++) {
					tempIds = new ArrayList<>();
					array2 = list.get(j).getIdArray();
					for (int k = 0; k < array1.length; k++) {
						// ����Ӧλ���ϵ�ֵ��ȵ�ʱ��ֻȡ����һ��ֵ������һ������ɾ�����
						if (array1[k].equals(array2[k])) {
							tempIds.add(array1[k]);
						} else {
							tempIds.add(array1[k]);
							tempIds.add(array2[k]);
						}
					}
					resultArray = new String[tempIds.size()];
					tempIds.toArray(resultArray);

					boolean isContain = false;
					// ���˲���������ĵ�ID���飬�����ظ��ĺͳ��Ȳ����Ҫ���
					if (resultArray.length == (array1.length + 1)) {
						isContain = isIDArrayContains(resultContainer,
								resultArray);
						if (!isContain) {
							resultContainer.add(resultArray);
						}
					}
				}
			}

			// ��Ƶ����ļ�֦���?���뱣֤�µ�Ƶ��������Ҳ������Ƶ���
			list = cutItem(resultContainer);
			currentNum++;
		}

		// ���Ƶ���
		for (int k = 1; k <= currentNum; k++) {
			System.out.println("Ƶ��" + k + "���");
			for (FrequentItem i : resultItem) {
				if (i.getLength() == k) {
					System.out.print("{");
					for (String t : i.getIdArray()) {
						System.out.print(t + ",");
					}
					System.out.print("},");
				}
			}
			System.out.println();
		}
	}

	/**
	 * �ж��б������Ƿ��Ѿ��������
	 * 
	 * @param container
	 *            ID��������
	 * @param array
	 *            ��Ƚ�����
	 * @return
	 */
	private boolean isIDArrayContains(ArrayList<String[]> container,
			String[] array) {
		boolean isContain = true;
		if (container.size() == 0) {
			isContain = false;
			return isContain;
		}

		for (String[] s : container) {
			// �Ƚϵ��Ӻ����뱣֤����һ��
			if (s.length != array.length) {
				continue;
			}

			isContain = true;
			for (int i = 0; i < s.length; i++) {
				// ֻҪ��һ��id���ȣ����㲻���
				if (s[i] != array[i]) {
					isContain = false;
					break;
				}
			}

			// ����Ѿ��ж��ǰ���������ʱ��ֱ���˳�
			if (isContain) {
				break;
			}
		}

		return isContain;
	}

	/**
	 * ��Ƶ�������֦���裬���뱣֤�µ�Ƶ��������Ҳ������Ƶ���
	 */
	private ArrayList<FrequentItem> cutItem(ArrayList<String[]> resultIds) {
		String[] temp;
		// ���Ե�����λ�ã��Դ˹����Ӽ�
		int igNoreIndex = 0;
		FrequentItem tempItem;
		// ��֦����µ�Ƶ���
		ArrayList<FrequentItem> newItem = new ArrayList<>();
		// �����Ҫ���id
		ArrayList<String[]> deleteIdArray = new ArrayList<>();
		// ����Ƿ�ҲΪƵ�����
		boolean isContain = true;

		for (String[] array : resultIds) {
			// �оٳ����е�һ������������жϴ�����Ƶ����б���
			temp = new String[array.length - 1];
			for (igNoreIndex = 0; igNoreIndex < array.length; igNoreIndex++) {
				isContain = true;
				for (int j = 0, k = 0; j < array.length; j++) {
					if (j != igNoreIndex) {
						temp[k] = array[j];
						k++;
					}
				}

				if (!isIDArrayContains(resultItemID, temp)) {
					isContain = false;
					break;
				}
			}

			if (!isContain) {
				deleteIdArray.add(array);
			}
		}

		// �Ƴ���������ID���
		resultIds.removeAll(deleteIdArray);

		// �Ƴ�֧�ֶȼ����id����
		int tempCount = 0;
		for (String[] array : resultIds) {
			tempCount = 0;
			for (String[] array2 : totalGoodsIDs) {
				if (isStrArrayContain(array2, array)) {
					tempCount++;
				}
			}

			// ���֧�ֶȼ�����ڵ�����С��С֧�ֶȼ���������µ�Ƶ���������������
			if (tempCount >= minSupportCount) {
				tempItem = new FrequentItem(array, tempCount);
				newItem.add(tempItem);
				resultItemID.add(array);
				resultItem.add(tempItem);
			}
		}

		return newItem;
	}

	/**
	 * ����array2�Ƿ����array1�У�����Ҫ��ȫһ��
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	private boolean isStrArrayContain(String[] array1, String[] array2) {
		boolean isContain = true;
		for (String s2 : array2) {
			isContain = false;
			for (String s1 : array1) {
				// ֻҪs2�ַ������array1�У�����ַ�������array1��
				if (s2.equals(s1)) {
					isContain = true;
					break;
				}
			}

			// һ�����ֲ�����ַ���array2���鲻����array1��
			if (!isContain) {
				break;
			}
		}

		return isContain;
	}

	/**
	 * ��ݲ����Ƶ��������������
	 * 
	 * @param minConf
	 *            ��С���Ŷ���ֵ
	 */
	public void printAttachRule(double minConf) {
		// �������Ӻͼ�֦����
		computeLink();

		int count1 = 0;
		int count2 = 0;
		ArrayList<String> childGroup1;
		ArrayList<String> childGroup2;
		String[] group1;
		String[] group2;
		// �����һ��Ƶ�����������������
		String[] array = resultItem.get(resultItem.size() - 1).getIdArray();
		// �Ӽ���������ʱ���ȥ����Ϳռ�
		int totalNum = (int) Math.pow(2, array.length);
		String[] temp;
		// ���������飬�����������Ӽ�
		int[] binaryArray;
		// ��ȥͷ��β��
		for (int i = 1; i < totalNum - 1; i++) {
			binaryArray = new int[array.length];
			numToBinaryArray(binaryArray, i);

			childGroup1 = new ArrayList<>();
			childGroup2 = new ArrayList<>();
			count1 = 0;
			count2 = 0;
			// ���ն�����λ��ϵȡ���Ӽ�
			for (int j = 0; j < binaryArray.length; j++) {
				if (binaryArray[j] == 1) {
					childGroup1.add(array[j]);
				} else {
					childGroup2.add(array[j]);
				}
			}

			group1 = new String[childGroup1.size()];
			group2 = new String[childGroup2.size()];

			childGroup1.toArray(group1);
			childGroup2.toArray(group2);

			for (String[] a : totalGoodsIDs) {
				if (isStrArrayContain(a, group1)) {
					count1++;

					// ��group1�������£�ͳ��group2���¼��������
					if (isStrArrayContain(a, group2)) {
						count2++;
					}
				}
			}

			// {A}-->{B}����˼Ϊ��A������·���B�ĸ���
			System.out.print("{");
			for (String s : group1) {
				System.out.print(s + ", ");
			}
			System.out.print("}-->");
			System.out.print("{");
			for (String s : group2) {
				System.out.print(s + ", ");
			}
			System.out.print(MessageFormat.format(
					"},confidence(���Ŷ�)��{0}/{1}={2}", count2, count1, count2
							* 1.0 / count1));
			if (count2 * 1.0 / count1 < minConf) {
				// �����Ҫ�󣬲���ǿ����
				System.out.println("���ڴ˹������Ŷ�δ�ﵽ��С���Ŷȵ�Ҫ�󣬲���ǿ����");
			} else {
				System.out.println("Ϊǿ����");
			}
		}

	}

	/**
	 * ����תΪ��������ʽ
	 * 
	 * @param binaryArray
	 *            ת����Ķ�����������ʽ
	 * @param num
	 *            ��ת������
	 */
	private void numToBinaryArray(int[] binaryArray, int num) {
		int index = 0;
		while (num != 0) {
			binaryArray[index] = num % 2;
			index++;
			num /= 2;
		}
	}
	
	/**
	 * ��ȡ�㷨�ھ������Ƶ���
	 * @return
	 */
	public ArrayList<FrequentItem> getTotalFrequentItems(){
		return this.resultItem;
	}

}
