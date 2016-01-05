package Classification.DataMining_CART;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.lang.model.element.NestingKind;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.html.MinimalHTMLWriter;

/**
 * CART����ع����㷨������
 * 
 * @author maxu
 * 
 */
public class CARTTool {
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

	public CARTTool(String filePath) {
		this.filePath = filePath;
		attrValue = new HashMap<>();
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	public void readDataFile() {
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
		 * for (int i = 0; i < data.length; i++) { for (int j = 0; j <
		 * data[0].length; j++) { System.out.print(" " + data[i][j]); }
		 * System.out.print("\n"); }
		 */

	}

	/**
	 * ���ȳ�ʼ��ÿ�����Ե�ֵ���������ͣ����ں���������صļ���ʱ��
	 */
	public void initAttrValue() {
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
		 * for (Map.Entry entry : attrValue.entrySet()) {
		 * System.out.println("key:value " + entry.getKey() + ":" +
		 * entry.getValue()); }
		 */
	}

	/**
	 * ��������ָ��
	 * 
	 * @param remainData
	 *            ʣ�����
	 * @param attrName
	 *            �������
	 * @param value
	 *            ����ֵ
	 * @param beLongValue
	 *            �����Ƿ����ڴ�����ֵ
	 * @return
	 */
	public double computeGini(String[][] remainData, String attrName,
			String value, boolean beLongValue) {
		// ʵ������
		int total = 0;
		// ��ʵ����
		int posNum = 0;
		// ��ʵ����
		int negNum = 0;
		// ����ָ��
		double gini = 0;

		// ���ǰ��д������ұ�������
		for (int j = 1; j < attrNames.length; j++) {
			// �ҵ���ָ��������
			if (attrName.equals(attrNames[j])) {
				for (int i = 1; i < remainData.length; i++) {
					// ͳ����ʵ�������ںͲ�����ֵ���ͽ��л���
					if ((beLongValue && remainData[i][j].equals(value))
							|| (!beLongValue && !remainData[i][j].equals(value))) {
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
		gini = 1 - posProbobly * posProbobly - negProbobly * negProbobly;

		// ���ؼ������ָ��
		return gini;
	}

	/**
	 * �������Ի��ֵ���С����ָ�����С������ֵ���ֺ���С�Ļ���ָ�����һ��������
	 * 
	 * @param remainData
	 *            ʣ��˭
	 * @param attrName
	 *            �������
	 * @return
	 */
	public String[] computeAttrGini(String[][] remainData, String attrName) {
		String[] str = new String[2];
		// ���ո����ԵĻ�������ֵ
		String spiltValue = "";
		// ��ʱ����
		int tempNum = 0;
		// �������Ե�ֵ����ʱ����С�Ļ���ָ��
		double minGini = Integer.MAX_VALUE;
		ArrayList<String> valueTypes = attrValue.get(attrName);
		// ���ڴ�����ֵ��ʵ����
		HashMap<String, Integer> belongNum = new HashMap<>();

		for (String string : valueTypes) {
			// ���¼����ʱ�����ֹ�0
			tempNum = 0;
			// ���д������ұ�������
			for (int j = 1; j < attrNames.length; j++) {
				// �ҵ���ָ��������
				if (attrName.equals(attrNames[j])) {
					for (int i = 1; i < remainData.length; i++) {
						// ͳ����ʵ�������ںͲ�����ֵ���ͽ��л���
						if (remainData[i][j].equals(string)) {
							tempNum++;
						}
					}
				}
			}

			belongNum.put(string, tempNum);
		}

		double tempGini = 0;
		double posProbably = 1.0;
		double negProbably = 1.0;
		for (String string : valueTypes) {
			tempGini = 0;

			posProbably = 1.0 * belongNum.get(string) / (remainData.length - 1);
			negProbably = 1 - posProbably;

			tempGini += posProbably
					* computeGini(remainData, attrName, string, true);
			tempGini += negProbably
					* computeGini(remainData, attrName, string, false);

			if (tempGini < minGini) {
				minGini = tempGini;
				spiltValue = string;
			}
		}

		str[0] = spiltValue;
		str[1] = minGini + "";

		return str;
	}

	public void buildDecisionTree(AttrNode node, String parentAttrValue,
			String[][] remainData, ArrayList<String> remainAttr,
			boolean beLongParentValue) {
		// ���Ի���ֵ
		String valueType = "";
		// �����������
		String spiltAttrName = "";
		double minGini = Integer.MAX_VALUE;
		double tempGini = 0;
		// ����ָ�����飬�����˻���ָ��ʹ˻���ָ��Ļ�������ֵ
		String[] giniArray;

		if (beLongParentValue) {
			node.setParentAttrValue(parentAttrValue);
		} else {
			node.setParentAttrValue("!" + parentAttrValue);
		}

		if (remainAttr.size() == 0) {
			if (remainData.length > 1) {
				ArrayList<String> indexArray = new ArrayList<>();
				for (int i = 1; i < remainData.length; i++) {
					indexArray.add(remainData[i][0]);
				}
				node.setDataIndex(indexArray);
			}
			System.out.println("attr remain null");
			return;
		}

		for (String str : remainAttr) {
			giniArray = computeAttrGini(remainData, str);
			tempGini = Double.parseDouble(giniArray[1]);

			if (tempGini < minGini) {
				spiltAttrName = str;
				minGini = tempGini;
				valueType = giniArray[0];
			}
		}
		// �Ƴ������
		remainAttr.remove(spiltAttrName);
		node.setAttrName(spiltAttrName);

		// ���ӽڵ�,����ع����У�ÿ�ζ�Ԫ���֣��ֳ�2�����ӽڵ�
		AttrNode[] childNode = new AttrNode[2];
		String[][] rData;

		boolean[] bArray = new boolean[] { true, false };
		for (int i = 0; i < bArray.length; i++) {
			// ��Ԫ������������ֵ�Ļ���
			rData = removeData(remainData, spiltAttrName, valueType, bArray[i]);

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

			childNode[i] = new AttrNode();
			if (!sameClass) {
				// �����µĶ������ԣ������ͬ�����û����
				ArrayList<String> rAttr = new ArrayList<>();
				for (String str : remainAttr) {
					rAttr.add(str);
				}
				buildDecisionTree(childNode[i], valueType, rData, rAttr,
						bArray[i]);
			} else {
				String pAtr = (bArray[i] ? valueType : "!" + valueType);
				childNode[i].setParentAttrValue(pAtr);
				childNode[i].setDataIndex(indexArray);
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
	 * @parame beLongValue �����Ƿ����ڴ�ֵ����
	 */
	private String[][] removeData(String[][] srcData, String attrName,
			String valueType, boolean beLongValue) {
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

		if (beLongValue) {
			desDataArray = new String[selectData.size()][];
			selectData.toArray(desDataArray);
		} else {
			// ��������в��Ƴ�
			selectData.remove(attrNames);
			// ����ǻ��ֲ����ڴ����͵����ʱ�������Ƴ�
			desData.removeAll(selectData);
			desDataArray = new String[desData.size()][];
			desData.toArray(desDataArray);
		}

		return desDataArray;
	}

	public void startBuildingTree() {
		readDataFile();
		initAttrValue();

		ArrayList<String> remainAttr = new ArrayList<>();
		// ������ԣ��������һ����������
		for (int i = 1; i < attrNames.length - 1; i++) {
			remainAttr.add(attrNames[i]);
		}

		AttrNode rootNode = new AttrNode();
		buildDecisionTree(rootNode, "", data, remainAttr, false);
		setIndexAndAlpah(rootNode, 0, false);
		System.out.println("��֦ǰ��");
		showDecisionTree(rootNode, 1);
		setIndexAndAlpah(rootNode, 0, true);
		System.out.println("\n��֦��");
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
			System.out.print("    ");
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

		if (node.getDataIndex() != null && node.getDataIndex().size() > 0) {
			String i = node.getDataIndex().get(0);
			System.out.print("��" + node.getNodeIndex() + "�����:"
					+ data[Integer.parseInt(i)][attrNames.length - 1]);
			System.out.print("[");
			for (String index : node.getDataIndex()) {
				System.out.print(index + ", ");
			}
			System.out.print("]");
		} else {
			// �ݹ���ʾ�ӽڵ�
			System.out.print("��" + node.getNodeIndex() + ":"
					+ node.getAttrName() + "��");
			if (node.getChildAttrNode() != null) {
				for (AttrNode childNode : node.getChildAttrNode()) {
					showDecisionTree(childNode, 2 * blankNum);
				}
			} else {
				System.out.print("��  Child Null��");
			}
		}
	}

	/**
	 * Ϊ�ڵ��������кţ�������ÿ���ڵ������ʣ����ں����֦
	 * 
	 * @param node
	 *            ��ʼ��ʱ������Ǹ�ڵ�
	 * @param index
	 *            ��ʼ������ţ���1��ʼ
	 * @param ifCutNode
	 *            �Ƿ���Ҫ��֦
	 */
	private void setIndexAndAlpah(AttrNode node, int index, boolean ifCutNode) {
		AttrNode tempNode;
		// ��С����۽ڵ㣬��������֦�Ľڵ�
		AttrNode minAlphaNode = null;
		double minAlpah = Integer.MAX_VALUE;
		Queue<AttrNode> nodeQueue = new LinkedList<AttrNode>();

		nodeQueue.add(node);
		while (nodeQueue.size() > 0) {
			index++;
			// �Ӷ���ͷ����ȡ�׸��ڵ�
			tempNode = nodeQueue.poll();
			tempNode.setNodeIndex(index);
			if (tempNode.getChildAttrNode() != null) {
				for (AttrNode childNode : tempNode.getChildAttrNode()) {
					nodeQueue.add(childNode);
				}
				computeAlpha(tempNode);
				if (tempNode.getAlpha() < minAlpah) {
					minAlphaNode = tempNode;
					minAlpah = tempNode.getAlpha();
				} else if (tempNode.getAlpha() == minAlpah) {
					// ��������ֵһ��Ƚϰ��Ҷ�ӽڵ�����֦�ж�Ҷ�ӽڵ���Ľڵ�
					if (tempNode.getLeafNum() > minAlphaNode.getLeafNum()) {
						minAlphaNode = tempNode;
					}
				}
			}
		}

		if (ifCutNode) {
			// �������ļ�֦���������Һ��ӽڵ�Ϊnull
			minAlphaNode.setChildAttrNode(null);
		}
	}

	/**
	 * Ϊ��Ҷ�ӽڵ��������ۣ�����ĺ��֦���õ���CCP��۸��Ӷȼ�֦
	 * 
	 * @param node
	 *            �����ķ�Ҷ�ӽڵ�
	 */
	private void computeAlpha(AttrNode node) {
		double rt = 0;
		double Rt = 0;
		double alpha = 0;
		// ��ǰ�ڵ���������
		int sumNum = 0;
		// ���ٵ�ƫ����
		int minNum = 0;

		ArrayList<String> dataIndex;
		ArrayList<AttrNode> leafNodes = new ArrayList<>();

		addLeafNode(node, leafNodes);
		node.setLeafNum(leafNodes.size());
		for (AttrNode attrNode : leafNodes) {
			dataIndex = attrNode.getDataIndex();

			int num = 0;
			sumNum += dataIndex.size();
			for (String s : dataIndex) {
				// ͳ�Ʒ�������е���ʵ����
				if (data[Integer.parseInt(s)][attrNames.length - 1].equals(YES)) {
					num++;
				}
			}
			minNum += num;

			// ȡС������ֵ����
			if (1.0 * num / dataIndex.size() > 0.5) {
				num = dataIndex.size() - num;
			}

			rt += (1.0 * num / (data.length - 1));
		}
		
		//ͬ��ȡ����ƫ����ǲ���
		if (1.0 * minNum / sumNum > 0.5) {
			minNum = sumNum - minNum;
		}

		Rt = 1.0 * minNum / (data.length - 1);
		alpha = 1.0 * (Rt - rt) / (leafNodes.size() - 1);
		node.setAlpha(alpha);
	}

	/**
	 * ɸѡ���ڵ�����Ҷ�ӽڵ���
	 * 
	 * @param node
	 *            ��ɸѡ�ڵ�
	 * @param leafNode
	 *            Ҷ�ӽڵ��б�����
	 */
	private void addLeafNode(AttrNode node, ArrayList<AttrNode> leafNode) {
		ArrayList<String> dataIndex;

		if (node.getChildAttrNode() != null) {
			for (AttrNode childNode : node.getChildAttrNode()) {
				dataIndex = childNode.getDataIndex();
				if (dataIndex != null && dataIndex.size() > 0) {
					// ˵���˽ڵ�ΪҶ�ӽڵ�
					leafNode.add(childNode);
				} else {
					// ����Ƿ�Ҷ�ӽڵ������ݹ����
					addLeafNode(childNode, leafNode);
				}
			}
		}
	}

}
