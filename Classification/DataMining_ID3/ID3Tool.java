package Classification.DataMining_ID3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * ID3�㷨ʵ����
 * 
 * @author lyq
 * 
 */
public class ID3Tool {
	// ���ŵ�ֵ����
	private final String YES = "Yes";
	private final String NO = "No";

	// �������Ե���������,���������dataԴ��ݵ�����
	private int attrNum;
	private String filePath;
	// ��ʼԴ��ݣ���һ����ά�ַ�������ģ�±�����
	private String[][] data;
	// ��ݵ������е�����
	private String[] attrNames;
	// ÿ�����Ե�ֵ��������
	private HashMap<String, ArrayList<String>> attrValue;

	public ID3Tool(String filePath) {
		this.filePath = filePath;
		attrValue = new HashMap<>();
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

		data = new String[dataArray.size()][];
		dataArray.toArray(data);
		attrNum = data[0].length;
		attrNames = data[0];

		/*
		 * for(int i=0; i<data.length;i++){ for(int j=0; j<data[0].length; j++){
		 * System.out.print(" " + data[i][j]); }
		 * 
		 * System.out.print("\n"); }
		 */
	}

	/**
	 * ���ȳ�ʼ��ÿ�����Ե�ֵ���������ͣ����ں���������صļ���ʱ��
	 */
	private void initAttrValue() {
		ArrayList<String> tempValues;

		// �����еķ�ʽ������������
		for (int j = 1; j < attrNum; j++) {
			// ��һ���е������¿�ʼѰ��ֵ
			tempValues = new ArrayList<>();
			for (int i = 1; i < data.length; i++) {
				if (!tempValues.contains(data[i][j])) {
					// ���������Ե�ֵû����ӹ������
					tempValues.add(data[i][j]);
				}
			}

			// һ�����Ե�ֵ�Ѿ�������ϣ����Ƶ�map���Ա���
			attrValue.put(data[0][j], tempValues);
		}

		/*
		 * for(Map.Entry entry : attrValue.entrySet()){
		 * System.out.println("key:value " + entry.getKey() + ":" +
		 * entry.getValue()); }
		 */
	}

	/**
	 * ������ݰ��ղ�ͬ��ʽ���ֵ���
	 * 
	 * @param remainData
	 *            ʣ������
	 * @param attrName
	 *            ��ֵ����ԣ�������Ϣ�����ʱ���ʹ�õ�
	 * @param attrValue
	 *            ���ֵ�������ֵ
	 * @param isParent
	 *            �Ƿ�������Ի��ֻ���ԭ������Ļ���
	 */
	private double computeEntropy(String[][] remainData, String attrName,
			String value, boolean isParent) {
		// ʵ������
		int total = 0;
		// ��ʵ����
		int posNum = 0;
		// ��ʵ����
		int negNum = 0;

		// ���ǰ��д������ұ�������
		for (int j = 1; j < attrNames.length; j++) {
			// �ҵ���ָ��������
			if (attrName.equals(attrNames[j])) {
				for (int i = 1; i < remainData.length; i++) {
					// ����Ǹ����ֱ�Ӽ����ػ�����ͨ�������Ի��ּ����أ���ʱҪ��������ֵ�Ĺ���
					if (isParent
							|| (!isParent && remainData[i][j].equals(value))) {
						if (remainData[i][attrNames.length - 1].equals(YES)) {
							// �жϴ�������Ƿ�Ϊ��ʵ��
							posNum++;
						} else {
							negNum++;
						}
					}
				}
			}
		}

		total = posNum + negNum;
		double posProbobly = (double) posNum / total;
		double negProbobly = (double) negNum / total;

		if (posProbobly == 1 || posProbobly == 0) {
			// ������ȫΪͬ�����ͣ�����Ϊ0�������������Ĺ�ʽ�ᱨ��
			return 0;
		}

		double entropyValue = -posProbobly * Math.log(posProbobly)
				/ Math.log(2.0) - negProbobly * Math.log(negProbobly)
				/ Math.log(2.0);

		// ���ؼ��������
		return entropyValue;
	}

	/**
	 * Ϊĳ�����Լ�����Ϣ����
	 * 
	 * @param remainData
	 *            ʣ������
	 * @param value
	 *            ��ֵ��������
	 * @return
	 */
	private double computeGain(String[][] remainData, String value) {
		double gainValue = 0;
		// Դ�صĴ�С���������Ի��ֺ���бȽ�
		double entropyOri = 0;
		// �ӻ����غ�
		double childEntropySum = 0;
		// ���������͵ĸ���
		int childValueNum = 0;
		// ����ֵ������
		ArrayList<String> attrTypes = attrValue.get(value);
		// �����Զ�Ӧ��Ȩ�ر�
		HashMap<String, Integer> ratioValues = new HashMap<>();

		for (int i = 0; i < attrTypes.size(); i++) {
			// ���ȶ�ͳһ����Ϊ0
			ratioValues.put(attrTypes.get(i), 0);
		}

		// ���ǰ���һ�У��������ұ���
		for (int j = 1; j < attrNames.length; j++) {
			// �ж��Ƿ��˻��ֵ�������
			if (value.equals(attrNames[j])) {
				for (int i = 1; i <= remainData.length - 1; i++) {
					childValueNum = ratioValues.get(remainData[i][j]);
					// ���Ӹ��������´���
					childValueNum++;
					ratioValues.put(remainData[i][j], childValueNum);
				}
			}
		}

		// ����ԭ�صĴ�С
		entropyOri = computeEntropy(remainData, value, null, true);
		for (int i = 0; i < attrTypes.size(); i++) {
			double ratio = (double) ratioValues.get(attrTypes.get(i))
					/ (remainData.length - 1);
			childEntropySum += ratio
					* computeEntropy(remainData, value, attrTypes.get(i), false);

			// System.out.println("ratio:value: " + ratio + " " +
			// computeEntropy(remainData, value,
			// attrTypes.get(i), false));
		}

		// ���������������Ϣ����
		gainValue = entropyOri - childEntropySum;
		return gainValue;
	}

	/**
	 * ������Ϣ�����
	 * 
	 * @param remainData
	 *            ʣ�����
	 * @param value
	 *            �������
	 * @return
	 */
	private double computeGainRatio(String[][] remainData, String value) {
		double gain = 0;
		double spiltInfo = 0;
		int childValueNum = 0;
		// ����ֵ������
		ArrayList<String> attrTypes = attrValue.get(value);
		// �����Զ�Ӧ��Ȩ�ر�
		HashMap<String, Integer> ratioValues = new HashMap<>();

		for (int i = 0; i < attrTypes.size(); i++) {
			// ���ȶ�ͳһ����Ϊ0
			ratioValues.put(attrTypes.get(i), 0);
		}

		// ���ǰ���һ�У��������ұ���
		for (int j = 1; j < attrNames.length; j++) {
			// �ж��Ƿ��˻��ֵ�������
			if (value.equals(attrNames[j])) {
				for (int i = 1; i <= remainData.length - 1; i++) {
					childValueNum = ratioValues.get(remainData[i][j]);
					// ���Ӹ��������´���
					childValueNum++;
					ratioValues.put(remainData[i][j], childValueNum);
				}
			}
		}

		// ������Ϣ����
		gain = computeGain(remainData, value);
		// ���������Ϣ��������Ϣ����������Ϊ(������Ϣ�����������Է�����ݵĹ�Ⱥ;���)��
		for (int i = 0; i < attrTypes.size(); i++) {
			double ratio = (double) ratioValues.get(attrTypes.get(i))
					/ (remainData.length - 1);
			spiltInfo += -ratio * Math.log(ratio) / Math.log(2.0);
		}

		// �������Ϣ������
		return gain / spiltInfo;
	}

	/**
	 * ����Դ��ݹ��������
	 */
	private void buildDecisionTree(AttrNode node, String parentAttrValue,
			String[][] remainData, ArrayList<String> remainAttr, boolean isID3) {
		node.setParentAttrValue(parentAttrValue);

		String attrName = "";
		double gainValue = 0;
		double tempValue = 0;

		// ���ֻ��1��������ֱ�ӷ���
		if (remainAttr.size() == 1) {
			System.out.println("attr null");
			return;
		}

		// ѡ��ʣ����������Ϣ����������Ϊ��һ�����������
		for (int i = 0; i < remainAttr.size(); i++) {
			// �ж��Ƿ���ID3�㷨����C4.5�㷨
			if (isID3) {
				// ID3�㷨���õ��ǰ�����Ϣ�����ֵ����
				tempValue = computeGain(remainData, remainAttr.get(i));
			} else {
				// C4.5�㷨�����˸Ľ��õ�����Ϣ����������,�˷�������Ϣ����ѡ������ʱƫ��ѡ��ȡֵ������ԵĲ���
				tempValue = computeGainRatio(remainData, remainAttr.get(i));
			}

			if (tempValue > gainValue) {
				gainValue = tempValue;
				attrName = remainAttr.get(i);
			}
		}

		node.setAttrName(attrName);
		ArrayList<String> valueTypes = attrValue.get(attrName);
		remainAttr.remove(attrName);

		AttrNode[] childNode = new AttrNode[valueTypes.size()];
		String[][] rData;
		for (int i = 0; i < valueTypes.size(); i++) {
			// �Ƴ�Ǵ�ֵ���͵����
			rData = removeData(remainData, attrName, valueTypes.get(i));

			childNode[i] = new AttrNode();
			boolean sameClass = true;
			ArrayList<String> indexArray = new ArrayList<>();
			for (int k = 1; k < rData.length; k++) {
				indexArray.add(rData[k][0]);
				// �ж��Ƿ�Ϊͬһ���
				if (!rData[k][attrNames.length - 1]
						.equals(rData[1][attrNames.length - 1])) {
					// ֻҪ��1������ȣ��Ͳ���ͬ���͵�
					sameClass = false;
					break;
				}
			}

			if (!sameClass) {
				// �����µĶ������ԣ������ͬ�����û����
				ArrayList<String> rAttr = new ArrayList<>();
				for (String str : remainAttr) {
					rAttr.add(str);
				}

				buildDecisionTree(childNode[i], valueTypes.get(i), rData,
						rAttr, isID3);
			} else {
				// �����ͬ�����ͣ���ֱ��Ϊ��ݽڵ�
				childNode[i].setParentAttrValue(valueTypes.get(i));
				childNode[i].setChildDataIndex(indexArray);
			}

		}
		node.setChildAttrNode(childNode);
	}

	/**
	 * ���Ի�����ϣ�������ݵ��Ƴ�
	 * 
	 * @param srcData
	 *            Դ���
	 * @param attrName
	 *            ���ֵ��������
	 * @param valueType
	 *            ���Ե�ֵ����
	 */
	private String[][] removeData(String[][] srcData, String attrName,
			String valueType) {
		String[][] desDataArray;
		ArrayList<String[]> desData = new ArrayList<>();
		// ��ɾ�����
		ArrayList<String[]> selectData = new ArrayList<>();
		selectData.add(attrNames);

		// �������ת�����б��У������Ƴ�
		for (int i = 0; i < srcData.length; i++) {
			desData.add(srcData[i]);
		}

		// ���Ǵ�������һ���еĲ���
		for (int j = 1; j < attrNames.length; j++) {
			if (attrNames[j].equals(attrName)) {
				for (int i = 1; i < desData.size(); i++) {
					if (desData.get(i)[j].equals(valueType)) {
						// ���ƥ�������ݣ����Ƴ���������
						selectData.add(desData.get(i));
					}
				}
			}
		}

		desDataArray = new String[selectData.size()][];
		selectData.toArray(desDataArray);

		return desDataArray;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @param isID3
	 *            �Ƿ����ID3�㷨���ܾ�����
	 */
	public void startBuildingTree(boolean isID3) {
		readDataFile();
		initAttrValue();

		ArrayList<String> remainAttr = new ArrayList<>();
		// ������ԣ��������һ����������
		for (int i = 1; i < attrNames.length - 1; i++) {
			remainAttr.add(attrNames[i]);
		}

		AttrNode rootNode = new AttrNode();
		buildDecisionTree(rootNode, "", data, remainAttr, isID3);
		showDecisionTree(rootNode, 1);
	}

	/**
	 * ��ʾ������
	 * 
	 * @param node
	 *            ����ʾ�Ľڵ�
	 * @param blankNum
	 *            �пո��������ʾ���ͽṹ
	 */
	private void showDecisionTree(AttrNode node, int blankNum) {
		System.out.println();
		for (int i = 0; i < blankNum; i++) {
			System.out.print("\t");
		}
		System.out.print("--");
		// ��ʾ���������ֵ
		if (node.getParentAttrValue() != null
				&& node.getParentAttrValue().length() > 0) {
			System.out.print(node.getParentAttrValue());
		} else {
			System.out.print("--");
		}
		System.out.print("--");

		if (node.getChildDataIndex() != null
				&& node.getChildDataIndex().size() > 0) {
			String i = node.getChildDataIndex().get(0);
			System.out.print("���:"
					+ data[Integer.parseInt(i)][attrNames.length - 1]);
			System.out.print("[");
			for (String index : node.getChildDataIndex()) {
				System.out.print(index + ", ");
			}
			System.out.print("]");
		} else {
			// �ݹ���ʾ�ӽڵ�
			System.out.print("��" + node.getAttrName() + "��");
			for (AttrNode childNode : node.getChildAttrNode()) {
				showDecisionTree(childNode, 2 * blankNum);
			}
		}

	}

}
