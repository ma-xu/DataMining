package IntegratedMining.DataMining_CBA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import IntegratedMining.DataMining_CBA.AprioriTool.AprioriTool;
import IntegratedMining.DataMining_CBA.AprioriTool.FrequentItem;

/**
 * CBA�㷨(�����������)������
 * 
 * @author lyq
 * 
 */
public class CBATool {
	// �������𻮷�
	public final String AGE = "Age";
	public final String AGE_YOUNG = "Young";
	public final String AGE_MIDDLE_AGED = "Middle_aged";
	public final String AGE_Senior = "Senior";

	// ������ݵ�ַ
	private String filePath;
	// ��С֧�ֶ���ֵ��
	private double minSupportRate;
	// ��С���Ŷ���ֵ�������ж��Ƿ��ܹ���Ϊ��������
	private double minConf;
	// ��С֧�ֶ�
	private int minSupportCount;
	// ���������
	private String[] attrNames;
	// ���������������ּ���
	private ArrayList<Integer> classTypes;
	// �ö�ά���鱣��������
	private ArrayList<String[]> totalDatas;
	// Apriori�㷨������
	private AprioriTool aprioriTool;
	// ���Ե����ֵ�ӳ��ͼ
	private HashMap<String, Integer> attr2Num;
	private HashMap<Integer, String> num2Attr;

	public CBATool(String filePath, double minSupportRate, double minConf) {
		this.filePath = filePath;
		this.minConf = minConf;
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

		totalDatas = new ArrayList<>();
		for (String[] array : dataArray) {
			totalDatas.add(array);
		}
		attrNames = totalDatas.get(0);
		minSupportCount = (int) (minSupportRate * totalDatas.size());

		attributeReplace();
	}

	/**
	 * ����ֵ���滻���滻�����ֵ���ʽ���Ա����Ƶ������ھ�
	 */
	private void attributeReplace() {
		int currentValue = 1;
		int num = 0;
		String s;
		// ���������ֵ�ӳ��ͼ
		attr2Num = new HashMap<>();
		num2Attr = new HashMap<>();
		classTypes = new ArrayList<>();

		// ����1���еķ�ʽ�����������ұ�ɨ��,���������к�id��
		for (int j = 1; j < attrNames.length; j++) {
			for (int i = 1; i < totalDatas.size(); i++) {
				s = totalDatas.get(i)[j];
				// �����������ʽ�ģ�����ֻ���������ת��������������������
				if (attrNames[j].equals(AGE)) {
					num = Integer.parseInt(s);
					if (num <= 20 && num > 0) {
						totalDatas.get(i)[j] = AGE_YOUNG;
					} else if (num > 20 && num <= 40) {
						totalDatas.get(i)[j] = AGE_MIDDLE_AGED;
					} else if (num > 40) {
						totalDatas.get(i)[j] = AGE_Senior;
					}
				}

				if (!attr2Num.containsKey(totalDatas.get(i)[j])) {
					attr2Num.put(totalDatas.get(i)[j], currentValue);
					num2Attr.put(currentValue, totalDatas.get(i)[j]);
					if (j == attrNames.length - 1) {
						// ��������һ�У�˵���Ƿ�������У���¼����
						classTypes.add(currentValue);
					}

					currentValue++;
				}
			}
		}

		// ��ԭʼ������������滻��ÿ����¼��Ϊ������������ݵ���ʽ
		for (int i = 1; i < totalDatas.size(); i++) {
			for (int j = 1; j < attrNames.length; j++) {
				s = totalDatas.get(i)[j];
				if (attr2Num.containsKey(s)) {
					totalDatas.get(i)[j] = attr2Num.get(s) + "";
				}
			}
		}
	}

	/**
	 * Apriori����ȫ��Ƶ���
	 * @return
	 */
	private ArrayList<FrequentItem> aprioriCalculate() {
		String[] tempArray;
		ArrayList<FrequentItem> totalFrequentItems;
		ArrayList<String[]> copyData = (ArrayList<String[]>) totalDatas.clone();
		// ȥ�����������
		copyData.remove(0);
		// ȥ������ID
		for (int i = 0; i < copyData.size(); i++) {
			String[] array = copyData.get(i);
			tempArray = new String[array.length - 1];
			System.arraycopy(array, 1, tempArray, 0, tempArray.length);
			copyData.set(i, tempArray);
		}
		aprioriTool = new AprioriTool(copyData, minSupportCount);
		aprioriTool.computeLink();
		totalFrequentItems = aprioriTool.getTotalFrequentItems();

		return totalFrequentItems;
	}

	/**
	 * ���ڹ�������ķ���
	 * 
	 * @param attrValues
	 *            Ԥ��֪����һЩ����
	 * @return
	 */
	public String CBAJudge(String attrValues) {
		int value = 0;
		// ���շ������
		String classType = null;
		String[] tempArray;
		// ��֪������ֵ
		ArrayList<String> attrValueList = new ArrayList<>();
		ArrayList<FrequentItem> totalFrequentItems;

		totalFrequentItems = aprioriCalculate();
		// ����ѯ����������һ���Եķָ�
		String[] array = attrValues.split(",");
		for (String record : array) {
			tempArray = record.split("=");
			value = attr2Num.get(tempArray[1]);
			attrValueList.add(value + "");
		}

		// ��Ƶ�����Ѱ�ҷ����������
		for (FrequentItem item : totalFrequentItems) {
			// ���˵����������Ƶ����
			if (item.getIdArray().length < (attrValueList.size() + 1)) {
				continue;
			}

			// Ҫ��֤��ѯ�����Զ�����Ƶ�����
			if (itemIsSatisfied(item, attrValueList)) {
				tempArray = item.getIdArray();
				classType = classificationBaseRules(tempArray);

				if (classType != null) {
					// �������滻
					classType = num2Attr.get(Integer.parseInt(classType));
					break;
				}
			}
		}

		return classType;
	}

	/**
	 * ���ڹ���������з���
	 * 
	 * @param items
	 *            Ƶ����
	 * @return
	 */
	private String classificationBaseRules(String[] items) {
		String classType = null;
		String[] arrayTemp;
		int count1 = 0;
		int count2 = 0;
		// ���Ŷ�
		double confidenceRate;

		String[] noClassTypeItems = new String[items.length - 1];
		for (int i = 0, k = 0; i < items.length; i++) {
			if (!classTypes.contains(Integer.parseInt(items[i]))) {
				noClassTypeItems[k] = items[i];
				k++;
			} else {
				classType = items[i];
			}
		}

		for (String[] array : totalDatas) {
			// ȥ��ID���ֺ�
			arrayTemp = new String[array.length - 1];
			System.arraycopy(array, 1, arrayTemp, 0, array.length - 1);
			if (isStrArrayContain(arrayTemp, noClassTypeItems)) {
				count1++;

				if (isStrArrayContain(arrayTemp, items)) {
					count2++;
				}
			}
		}

		// �����Ŷȵļ���
		confidenceRate = count1 * 1.0 / count2;
		if (confidenceRate >= minConf) {
			return classType;
		} else {
			// ���������С���Ŷ�Ҫ����˹���������Ч
			return null;
		}
	}

	/**
	 * �жϵ����ַ��Ƿ�����ַ�������
	 * 
	 * @param array
	 *            �ַ�����
	 * @param s
	 *            �жϵĵ��ַ�
	 * @return
	 */
	private boolean strIsContained(String[] array, String s) {
		boolean isContained = false;

		for (String str : array) {
			if (str.equals(s)) {
				isContained = true;
				break;
			}
		}

		return isContained;
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
	 * �ж�Ƶ����Ƿ������ѯ
	 * 
	 * @param item
	 *            ���жϵ�Ƶ���
	 * @param attrValues
	 *            ��ѯ������ֵ�б�
	 * @return
	 */
	private boolean itemIsSatisfied(FrequentItem item,
			ArrayList<String> attrValues) {
		boolean isContained = false;
		String[] array = item.getIdArray();

		for (String s : attrValues) {
			isContained = true;

			if (!strIsContained(array, s)) {
				isContained = false;
				break;
			}

			if (!isContained) {
				break;
			}
		}

		if (isContained) {
			isContained = false;

			// ��Ҫ��֤�Ƿ�Ƶ������Ƿ���������
			for (Integer type : classTypes) {
				if (strIsContained(array, type + "")) {
					isContained = true;
					break;
				}
			}
		}

		return isContained;
	}

}
