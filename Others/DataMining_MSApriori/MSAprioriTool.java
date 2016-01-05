package Others.DataMining_MSApriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import AssociationAnalysis.DataMining_Apriori.FrequentItem;

/**
 * ���ڶ�֧�ֶȵ�Apriori�㷨������
 * 
 * @author maxu
 * 
 */
public class MSAprioriTool {
	// ǰ���жϵĽ��ֵ�����ڹ���������Ƶ�
	public static final int PREFIX_NOT_SUB = -1;
	public static final int PREFIX_EQUAL = 1;
	public static final int PREFIX_IS_SUB = 2;

	// �Ƿ��ȡ�������������
	private boolean isTransaction;
	// ���Ƶ��k���kֵ
	private int initFItemNum;
	// ��������ļ���ַ
	private String filePath;
	// ��С֧�ֶ���ֵ
	private double minSup;
	// ��С���Ŷ���
	private double minConf;
	// ���֧�ֶȲ����ֵ
	private double delta;
	// ����Ŀ����С֧�ֶ���,�����е��±��������Ʒ��ID
	private double[] mis;
	// ÿ�������е���ƷID
	private ArrayList<String[]> totalGoodsIDs;
	// ��ϵ�������ת�����������
	private ArrayList<String[]> transactionDatas;
	// ����м������������Ƶ����б�
	private ArrayList<FrequentItem> resultItem;
	// ����м������Ƶ�����ID����
	private ArrayList<String[]> resultItemID;
	// ���Ե����ֵ�ӳ��ͼ
	private HashMap<String, Integer> attr2Num;
	// ����id��Ӧ���Ե�ӳ��ͼ
	private HashMap<Integer, String> num2Attr;
	// Ƶ�����ǵ�id��ֵ
	private Map<String, int[]> fItem2Id;

	/**
	 * ��������ݹ����ھ��㷨
	 * 
	 * @param filePath
	 * @param minConf
	 * @param delta
	 * @param mis
	 * @param isTransaction
	 */
	public MSAprioriTool(String filePath, double minConf, double delta,
			double[] mis, boolean isTransaction) {
		this.filePath = filePath;
		this.minConf = minConf;
		this.delta = delta;
		this.mis = mis;
		this.isTransaction = isTransaction;
		this.fItem2Id = new HashMap<>();

		readDataFile();
	}

	/**
	 * �������͹����ھ�
	 * 
	 * @param filePath
	 * @param minConf
	 * @param minSup
	 * @param isTransaction
	 */
	public MSAprioriTool(String filePath, double minConf, double minSup,
			boolean isTransaction) {
		this.filePath = filePath;
		this.minConf = minConf;
		this.minSup = minSup;
		this.isTransaction = isTransaction;
		this.delta = 1.0;
		this.fItem2Id = new HashMap<>();

		readRDBMSData(filePath);
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	private void readDataFile() {
		String[] temp = null;
		ArrayList<String[]> dataArray;

		dataArray = readLine(filePath);
		totalGoodsIDs = new ArrayList<>();

		for (String[] array : dataArray) {
			temp = new String[array.length - 1];
			System.arraycopy(array, 1, temp, 0, array.length - 1);

			// ������ID�����б����
			totalGoodsIDs.add(temp);
		}
	}

	/**
	 * ���ļ������ж����
	 * 
	 * @param filePath
	 *            ����ļ���ַ
	 * @return
	 */
	private ArrayList<String[]> readLine(String filePath) {
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

		return dataArray;
	}

	/**
	 * ����Ƶ���
	 */
	public void calFItems() {
		FrequentItem fItem;

		computeLink();
		printFItems();

		if (isTransaction) {
			fItem = resultItem.get(resultItem.size() - 1);
			// ȡ�����һ��Ƶ���������������Ƶ�
			System.out.println("���һ��Ƶ���������������Ƶ����");
			printAttachRuls(fItem.getIdArray());
		}
	}

	/**
	 * ���Ƶ���
	 */
	private void printFItems() {
		if (isTransaction) {
			System.out.println("���������Ƶ���������:");
		} else {
			System.out.println("������(��ϵ)�����Ƶ���������:");
		}

		// ���Ƶ���
		for (int k = 1; k <= initFItemNum; k++) {
			System.out.println("Ƶ��" + k + "���");
			for (FrequentItem i : resultItem) {
				if (i.getLength() == k) {
					System.out.print("{");
					for (String t : i.getIdArray()) {
						if (!isTransaction) {
							// ���ԭ���Ƿ���������ݣ���Ҫ�������滻
							t = num2Attr.get(Integer.parseInt(t));
						}

						System.out.print(t + ",");
					}
					System.out.print("},");
				}
			}
			System.out.println();
		}
	}

	/**
	 * �������������
	 */
	private void computeLink() {
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
		for (Map.Entry<String, FrequentItem> entry : itemMap.entrySet()) {
			tempItem = entry.getValue();

			// �ж�1Ƶ����Ƿ�����֧�ֶ���ֵ������
			if (judgeFItem(tempItem.getIdArray())) {
				list.add(tempItem);
			}
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
		initFItemNum = list.size() - 1;

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
		boolean isSatisfied = false;
		for (String[] array : resultIds) {
			isSatisfied = judgeFItem(array);

			// ����Ƶ��������֧�ֶ���ֵ����������֧�ֶȲ�����������������������
			if (isSatisfied) {
				tempItem = new FrequentItem(array, tempCount);
				newItem.add(tempItem);
				resultItemID.add(array);
				resultItem.add(tempItem);
			}
		}

		return newItem;
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
	 * �ж�һ��Ƶ����Ƿ���������
	 * 
	 * @param frequentItem
	 *            ���ж�Ƶ���
	 * @return
	 */
	private boolean judgeFItem(String[] frequentItem) {
		boolean isSatisfied = true;
		int id;
		int count;
		double tempMinSup;
		// ��С��֧�ֶ���ֵ
		double minMis = Integer.MAX_VALUE;
		// ����֧�ֶ���ֵ
		double maxMis = -Integer.MAX_VALUE;

		// �������������ݣ���mis�����жϣ������ͳһ��ͬ�����С֧�ֶ���ֵ�ж�
		if (isTransaction) {
			// Ѱ��Ƶ����е���С֧�ֶ���ֵ
			for (int i = 0; i < frequentItem.length; i++) {
				id = i + 1;

				if (mis[id] < minMis) {
					minMis = mis[id];
				}

				if (mis[id] > maxMis) {
					maxMis = mis[id];
				}
			}
		} else {
			minMis = minSup;
			maxMis = minSup;
		}

		count = calSupportCount(frequentItem);
		tempMinSup = 1.0 * count / totalGoodsIDs.size();
		// �ж�Ƶ�����֧�ֶ���ֵ�Ƿ񳬹���С��֧�ֶ���ֵ
		if (tempMinSup < minMis) {
			isSatisfied = false;
		}

		// ������������֧�ֶȲ��Ҳ�㲻��������
		if (Math.abs(maxMis - minMis) > delta) {
			isSatisfied = false;
		}

		return isSatisfied;
	}

	/**
	 * ͳ�ƺ�ѡƵ�����֧�ֶ�����������Ӽ����м���������ɨ�������ݼ�
	 * 
	 * @param frequentItem
	 *            �����Ƶ���
	 * @return
	 */
	private int calSupportCount(String[] frequentItem) {
		int count = 0;
		int[] ids;
		String key;
		String[] array;
		ArrayList<Integer> newIds;

		key = "";
		for (int i = 1; i < frequentItem.length; i++) {
			key += frequentItem[i];
		}

		newIds = new ArrayList<>();
		// �ҳ�����������ID
		ids = fItem2Id.get(key);

		// ���û���ҵ����������id����ȫ��ɨ����ݼ�
		if (ids == null || ids.length == 0) {
			for (int j = 0; j < totalGoodsIDs.size(); j++) {
				array = totalGoodsIDs.get(j);
				if (isStrArrayContain(array, frequentItem)) {
					count++;
					newIds.add(j);
				}
			}
		} else {
			for (int index : ids) {
				array = totalGoodsIDs.get(index);
				if (isStrArrayContain(array, frequentItem)) {
					count++;
					newIds.add(index);
				}
			}
		}

		ids = new int[count];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = newIds.get(i);
		}

		key = frequentItem[0] + key;
		// ������ֵ����ͼ�У������´εļ���
		fItem2Id.put(key, ids);

		return count;
	}

	/**
	 * ��ݸ��Ƶ��������������
	 * 
	 * @param frequentItems
	 *            Ƶ���
	 */
	public void printAttachRuls(String[] frequentItem) {
		// ��������ǰ��,�����
		Map<ArrayList<String>, ArrayList<String>> rules;
		// ǰ��������ʷ
		Map<ArrayList<String>, ArrayList<String>> searchHistory;
		ArrayList<String> prefix;
		ArrayList<String> suffix;

		rules = new HashMap<ArrayList<String>, ArrayList<String>>();
		searchHistory = new HashMap<>();

		for (int i = 0; i < frequentItem.length; i++) {
			suffix = new ArrayList<>();
			for (int j = 0; j < frequentItem.length; j++) {
				suffix.add(frequentItem[j]);
			}
			prefix = new ArrayList<>();

			recusiveFindRules(rules, searchHistory, prefix, suffix);
		}

		// ��������ҵ��Ĺ�������
		for (Map.Entry<ArrayList<String>, ArrayList<String>> entry : rules
				.entrySet()) {
			prefix = entry.getKey();
			suffix = entry.getValue();

			printRuleDetail(prefix, suffix);
		}
	}

	/**
	 * ���ǰ������������������
	 * 
	 * @param prefix
	 * @param suffix
	 */
	private void printRuleDetail(ArrayList<String> prefix,
			ArrayList<String> suffix) {
		// {A}-->{B}����˼Ϊ��A������·���B�ĸ���
		System.out.print("{");
		for (String s : prefix) {
			System.out.print(s + ", ");
		}
		System.out.print("}-->");
		System.out.print("{");
		for (String s : suffix) {
			System.out.print(s + ", ");
		}
		System.out.println("}");
	}

	/**
	 * �ݹ���չ���������
	 * 
	 * @param rules
	 *            ����������
	 * @param history
	 *            ǰ��������ʷ
	 * @param prefix
	 *            ��������ǰ��
	 * @param suffix
	 *            ����������
	 */
	private void recusiveFindRules(
			Map<ArrayList<String>, ArrayList<String>> rules,
			Map<ArrayList<String>, ArrayList<String>> history,
			ArrayList<String> prefix, ArrayList<String> suffix) {
		int count1;
		int count2;
		int compareResult;
		// ���Ŷȴ�С
		double conf;
		String[] temp1;
		String[] temp2;
		ArrayList<String> copyPrefix;
		ArrayList<String> copySuffix;

		// �����ֻ��1���������
		if (suffix.size() == 1) {
			return;
		}

		for (String s : suffix) {
			count1 = 0;
			count2 = 0;

			copyPrefix = (ArrayList<String>) prefix.clone();
			copyPrefix.add(s);

			copySuffix = (ArrayList<String>) suffix.clone();
			// �������ĺ���Ƴ���ӵ�һ��
			copySuffix.remove(s);

			compareResult = isSubSetInRules(history, copyPrefix);
			if (compareResult == PREFIX_EQUAL) {
				// ������Ѿ��������������
				continue;
			}

			// �ж��Ƿ�Ϊ�Ӽ���������Ӽ����������
			compareResult = isSubSetInRules(rules, copyPrefix);
			if (compareResult == PREFIX_IS_SUB) {
				rules.put(copyPrefix, copySuffix);
				// ���뵽������ʷ��
				history.put(copyPrefix, copySuffix);
				recusiveFindRules(rules, history, copyPrefix, copySuffix);
				continue;
			}

			// ��ʱ�ϲ�Ϊ�ܵļ���
			copySuffix.addAll(copyPrefix);
			temp1 = new String[copyPrefix.size()];
			temp2 = new String[copySuffix.size()];
			copyPrefix.toArray(temp1);
			copySuffix.toArray(temp2);
			// ֮���ٴ��Ƴ�֮ǰ�콣��ǰ��
			copySuffix.removeAll(copyPrefix);

			for (String[] a : totalGoodsIDs) {
				if (isStrArrayContain(a, temp1)) {
					count1++;

					// ��group1�������£�ͳ��group2���¼��������
					if (isStrArrayContain(a, temp2)) {
						count2++;
					}
				}
			}

			conf = 1.0 * count2 / count1;
			if (conf > minConf) {
				// ���ô�ǰ�������£��ܵ�����������
				rules.put(copyPrefix, copySuffix);
			}

			// ���뵽������ʷ��
			history.put(copyPrefix, copySuffix);
			recusiveFindRules(rules, history, copyPrefix, copySuffix);
		}
	}

	/**
	 * �жϵ�ǰ��ǰ���Ƿ�����������Ӽ�
	 * 
	 * @param rules
	 *            ��ǰ�Ѿ��жϳ��Ĺ�������
	 * @param prefix
	 *            ���жϵ�ǰ��
	 * @return
	 */
	private int isSubSetInRules(
			Map<ArrayList<String>, ArrayList<String>> rules,
			ArrayList<String> prefix) {
		int result = PREFIX_NOT_SUB;
		String[] temp1;
		String[] temp2;
		ArrayList<String> tempPrefix;

		for (Map.Entry<ArrayList<String>, ArrayList<String>> entry : rules
				.entrySet()) {
			tempPrefix = entry.getKey();

			temp1 = new String[tempPrefix.size()];
			temp2 = new String[prefix.size()];

			tempPrefix.toArray(temp1);
			prefix.toArray(temp2);

			// �жϵ�ǰ�����ǰ���Ƿ��Ѿ��Ǵ���ǰ�����Ӽ�
			if (isStrArrayContain(temp2, temp1)) {
				if (temp2.length == temp1.length) {
					result = PREFIX_EQUAL;
				} else {
					result = PREFIX_IS_SUB;
				}
			}

			if (result == PREFIX_EQUAL) {
				break;
			}
		}

		return result;
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
	 * ����ϵ���е���ݣ���ת��Ϊ�������
	 * 
	 * @param filePath
	 */
	private void readRDBMSData(String filePath) {
		String str;
		// ���������
		String[] attrNames = null;
		String[] temp;
		String[] newRecord;
		ArrayList<String[]> datas = null;

		datas = readLine(filePath);

		// ��ȡ����
		attrNames = datas.get(0);
		this.transactionDatas = new ArrayList<>();

		// ȥ���������
		for (int i = 1; i < datas.size(); i++) {
			temp = datas.get(i);

			// ���˵�����id��
			for (int j = 1; j < temp.length; j++) {
				str = "";
				// ����������+����ֵ����ʽ������ݵ��ظ�
				str = attrNames[j] + ":" + temp[j];
				temp[j] = str;
			}

			newRecord = new String[attrNames.length - 1];
			System.arraycopy(temp, 1, newRecord, 0, attrNames.length - 1);
			this.transactionDatas.add(newRecord);
		}

		attributeReplace();
		// ��������ת��totalGoodsID����ͳһ����
		this.totalGoodsIDs = transactionDatas;
	}

	/**
	 * ����ֵ���滻���滻�����ֵ���ʽ���Ա����Ƶ������ھ�
	 */
	private void attributeReplace() {
		int currentValue = 1;
		String s;
		// ���������ֵ�ӳ��ͼ
		attr2Num = new HashMap<>();
		num2Attr = new HashMap<>();

		// ����1���еķ�ʽ�����������ұ�ɨ��,���������к�id��
		for (int j = 0; j < transactionDatas.get(0).length; j++) {
			for (int i = 0; i < transactionDatas.size(); i++) {
				s = transactionDatas.get(i)[j];

				if (!attr2Num.containsKey(s)) {
					attr2Num.put(s, currentValue);
					num2Attr.put(currentValue, s);

					transactionDatas.get(i)[j] = currentValue + "";
					currentValue++;
				} else {
					transactionDatas.get(i)[j] = attr2Num.get(s) + "";
				}
			}
		}
	}
}
