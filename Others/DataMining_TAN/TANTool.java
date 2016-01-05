package Others.DataMining_TAN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * TAN�������ر�Ҷ˹�㷨������
 * 
 * @author lyq
 * 
 */
public class TANTool {
	// ������ݼ���ַ
	private String filePath;
	// ��ݼ���������,����һ������������
	private int attrNum;
	// ����������
	private String classAttrName;
	// �����������
	private String[] attrNames;
	// ��Ҷ˹����ߵķ��������ڵ���ֵΪ�ڵ�id,��i->j
	private int[][] edges;
	// ���������±��ӳ��
	private HashMap<String, Integer> attr2Column;
	// ���ԣ����Զ�ȡֵ����ӳ���
	private HashMap<String, ArrayList<String>> attr2Values;
	// ��Ҷ˹�����ܽڵ��б�
	private ArrayList<Node> totalNodes;
	// �ܵĲ������
	private ArrayList<String[]> totalDatas;

	public TANTool(String filePath) {
		this.filePath = filePath;

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
			String[] array;

			while ((str = in.readLine()) != null) {
				array = str.split(" ");
				dataArray.add(array);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		this.totalDatas = dataArray;
		this.attrNames = this.totalDatas.get(0);
		this.attrNum = this.attrNames.length;
		this.classAttrName = this.attrNames[attrNum - 1];

		Node node;
		this.edges = new int[attrNum][attrNum];
		this.totalNodes = new ArrayList<>();
		this.attr2Column = new HashMap<>();
		this.attr2Values = new HashMap<>();

		// �������Խڵ�id��С��Ϊ0
		node = new Node(0, attrNames[attrNum - 1]);
		this.totalNodes.add(node);
		for (int i = 0; i < attrNames.length; i++) {
			if (i < attrNum - 1) {
				// ������Ҷ˹����ڵ㣬ÿ������һ���ڵ�
				node = new Node(i + 1, attrNames[i]);
				this.totalNodes.add(node);
			}

			// ������Ե����±��ӳ��
			this.attr2Column.put(attrNames[i], i);
		}

		String[] temp;
		ArrayList<String> values;
		// ��������������ֵ�Ե�ӳ��ƥ��
		for (int i = 1; i < this.totalDatas.size(); i++) {
			temp = this.totalDatas.get(i);

			for (int j = 0; j < temp.length; j++) {
				// �ж�map���Ƿ���������
				if (this.attr2Values.containsKey(attrNames[j])) {
					values = this.attr2Values.get(attrNames[j]);
				} else {
					values = new ArrayList<>();
				}

				if (!values.contains(temp[j])) {
					// �����µ�����ֵ
					values.add(temp[j]);
				}

				this.attr2Values.put(attrNames[j], values);
			}
		}
	}

	/**
	 * �����������Ϣ�ȶԹ������Ȩ�ؿ����,���ص�һ���ڵ�Ϊ��ڵ�
	 * 
	 * @param iArray
	 */
	private Node constructWeightTree(ArrayList<Node[]> iArray) {
		Node node1;
		Node node2;
		Node root;
		ArrayList<Node> existNodes;

		existNodes = new ArrayList<>();

		for (Node[] i : iArray) {
			node1 = i[0];
			node2 = i[1];

			// ��2���ڵ��������
			node1.connectNode(node2);
			// ������ֻ�·����
			addIfNotExist(node1, existNodes);
			addIfNotExist(node2, existNodes);

			if (existNodes.size() == attrNum - 1) {
				break;
			}
		}

		// ���ص�һ����Ϊ��ڵ�
		root = existNodes.get(0);
		return root;
	}

	/**
	 * Ϊ���ͽṹȷ���ߵķ��򣬷���Ϊ���Ը�ڵ㷽��ָ���������Խڵ㷽��
	 * 
	 * @param root
	 *            ��ǰ����Ľڵ�
	 */
	private void confirmGraphDirection(Node currentNode) {
		int i;
		int j;
		ArrayList<Node> connectedNodes;

		connectedNodes = currentNode.connectedNodes;

		i = currentNode.id;
		for (Node n : connectedNodes) {
			j = n.id;

			// �ж����Ӵ�2�ڵ�ķ����Ƿ�ȷ��
			if (edges[i][j] == 0 && edges[j][i] == 0) {
				// ���û��ȷ�������ƶ�����Ϊi->j
				edges[i][j] = 1;

				// �ݹ��������
				confirmGraphDirection(n);
			}
		}
	}

	/**
	 * Ϊ���Խڵ���ӷ������Խڵ�Ϊ���ڵ�
	 * 
	 * @param parentNode
	 *            ���ڵ�
	 * @param nodeList
	 *            �ӽڵ��б�
	 */
	private void addParentNode() {
		// �������Խڵ�
		Node parentNode;

		parentNode = null;
		for (Node n : this.totalNodes) {
			if (n.id == 0) {
				parentNode = n;
				break;
			}
		}

		for (Node child : this.totalNodes) {
			parentNode.connectNode(child);

			if (child.id != 0) {
				// ȷ�����ӷ���
				this.edges[0][child.id] = 1;
			}
		}
	}

	/**
	 * �ڽڵ㼯������ӽڵ�
	 * 
	 * @param node
	 *            ����ӽڵ�
	 * @param existNodes
	 *            �Ѵ��ڵĽڵ��б�
	 * @return
	 */
	public boolean addIfNotExist(Node node, ArrayList<Node> existNodes) {
		boolean canAdd;

		canAdd = true;
		for (Node n : existNodes) {
			// ���ڵ��б����Ѿ����нڵ㣬�������ʧ��
			if (n.isEqual(node)) {
				canAdd = false;
				break;
			}
		}

		if (canAdd) {
			existNodes.add(node);
		}

		return canAdd;
	}

	/**
	 * ����ڵ���������
	 * 
	 * @param node
	 *            ����node�ĺ������
	 * @param queryParam
	 *            ��ѯ�����Բ���
	 * @return
	 */
	private double calConditionPro(Node node, HashMap<String, String> queryParam) {
		int id;
		double pro;
		String value;
		String[] attrValue;

		ArrayList<String[]> priorAttrInfos;
		ArrayList<String[]> backAttrInfos;
		ArrayList<Node> parentNodes;

		pro = 1;
		id = node.id;
		parentNodes = new ArrayList<>();
		priorAttrInfos = new ArrayList<>();
		backAttrInfos = new ArrayList<>();

		for (int i = 0; i < this.edges.length; i++) {
			// Ѱ�Ҹ��ڵ�id
			if (this.edges[i][id] == 1) {
				for (Node temp : this.totalNodes) {
					// Ѱ��Ŀ��ڵ�id
					if (temp.id == i) {
						parentNodes.add(temp);
						break;
					}
				}
			}
		}

		// ��ȡ�������Ե�����ֵ,���������������
		value = queryParam.get(node.name);
		attrValue = new String[2];
		attrValue[0] = node.name;
		attrValue[1] = value;
		priorAttrInfos.add(attrValue);

		// ��һ��Ӻ�������
		for (Node p : parentNodes) {
			value = queryParam.get(p.name);
			attrValue = new String[2];
			attrValue[0] = p.name;
			attrValue[1] = value;

			backAttrInfos.add(attrValue);
		}

		pro = queryConditionPro(priorAttrInfos, backAttrInfos);

		return pro;
	}

	/**
	 * ��ѯ��������
	 * 
	 * @param attrValues
	 *            ��������ֵ
	 * @return
	 */
	private double queryConditionPro(ArrayList<String[]> priorValues,
			ArrayList<String[]> backValues) {
		// �ж��Ƿ�������������ֵ����
		boolean hasPrior;
		// �ж��Ƿ������������ֵ����
		boolean hasBack;
		int attrIndex;
		double backPro;
		double totalPro;
		double pro;
		String[] tempData;

		pro = 0;
		totalPro = 0;
		backPro = 0;

		// ����һ�е����������
		for (int i = 1; i < this.totalDatas.size(); i++) {
			tempData = this.totalDatas.get(i);

			hasPrior = true;
			hasBack = true;

			// �ж��Ƿ�������������
			for (String[] array : priorValues) {
				attrIndex = this.attr2Column.get(array[0]);

				// �ж�ֵ�Ƿ���������
				if (!tempData[attrIndex].equals(array[1])) {
					hasPrior = false;
					break;
				}
			}

			// �ж��Ƿ������������
			for (String[] array : backValues) {
				attrIndex = this.attr2Column.get(array[0]);

				// �ж�ֵ�Ƿ���������
				if (!tempData[attrIndex].equals(array[1])) {
					hasBack = false;
					break;
				}
			}

			// ���м���ͳ�ƣ��ֱ��������������Ե�ֵ��ͬʱ���������ĸ���
			if (hasBack) {
				backPro++;
				if (hasPrior) {
					totalPro++;
				}
			} else if (hasPrior && backValues.size() == 0) {
				// ���ֻ�����������Ϊ�����ʵļ���
				totalPro++;
				backPro = 1.0;
			}
		}

		if (backPro == 0) {
			pro = 0;
		} else {
			// �����ܵĸ���=���������/ֻ�������������ʱ�����
			pro = totalPro / backPro;
		}

		return pro;
	}

	/**
	 * �����ѯ����������㷢�����
	 * 
	 * @param queryParam
	 *            ��������
	 * @return
	 */
	public double calHappenedPro(String queryParam) {
		double result;
		double temp;
		// ��������ֵ
		String classAttrValue;
		String[] array;
		String[] array2;
		HashMap<String, String> params;

		result = 1;
		params = new HashMap<>();

		// ���в�ѯ�ַ�Ĳ���ֽ�
		array = queryParam.split(",");
		for (String s : array) {
			array2 = s.split("=");
			params.put(array2[0], array2[1]);
		}

		classAttrValue = params.get(classAttrName);
		// ������Ҷ˹����ṹ
		constructBayesNetWork(classAttrValue);

		for (Node n : this.totalNodes) {
			temp = calConditionPro(n, params);

			// Ϊ�˱��������������Ϊ0�����󣬽�����΢����
			if (temp == 0) {
				temp = 0.001;
			}

			// �������ϸ��ʹ�ʽ�����г˻�����
			result *= temp;
		}

		return result;
	}

	/**
	 * �������ͱ�Ҷ˹����ṹ
	 * 
	 * @param value
	 *            �����ֵ
	 */
	private void constructBayesNetWork(String value) {
		Node rootNode;
		ArrayList<AttrMutualInfo> mInfoArray;
		// ����Ϣ�ȶ�
		ArrayList<Node[]> iArray;

		iArray = null;
		rootNode = null;

		// ��ÿ�����¹�����Ҷ˹����ṹ��ʱ�����ԭ�е����ӽṹ
		for (Node n : this.totalNodes) {
			n.connectedNodes.clear();
		}
		this.edges = new int[attrNum][attrNum];

		// �ӻ���Ϣ������ȡ������ֵ��
		iArray = new ArrayList<>();
		mInfoArray = calAttrMutualInfoArray(value);
		for (AttrMutualInfo v : mInfoArray) {
			iArray.add(v.nodeArray);
		}

		// �������Ȩ�ؿ����
		rootNode = constructWeightTree(iArray);
		// Ϊ����ͼȷ���ߵķ���
		confirmGraphDirection(rootNode);
		// Ϊÿ�����Խڵ���ӷ������Ը��ڵ�
		addParentNode();
	}

	/**
	 * ��������ֵ����������֮��Ļ���Ϣֵ
	 * 
	 * @param value
	 *            �������ֵ
	 * @return
	 */
	private ArrayList<AttrMutualInfo> calAttrMutualInfoArray(String value) {
		double iValue;
		Node node1;
		Node node2;
		AttrMutualInfo mInfo;
		ArrayList<AttrMutualInfo> mInfoArray;

		mInfoArray = new ArrayList<>();

		for (int i = 0; i < this.totalNodes.size() - 1; i++) {
			node1 = this.totalNodes.get(i);
			// ���������Խڵ�
			if (node1.id == 0) {
				continue;
			}

			for (int j = i + 1; j < this.totalNodes.size(); j++) {
				node2 = this.totalNodes.get(j);
				// ���������Խڵ�
				if (node2.id == 0) {
					continue;
				}

				// ����2�����Խڵ�֮��Ļ���Ϣֵ
				iValue = calMutualInfoValue(node1, node2, value);
				mInfo = new AttrMutualInfo(iValue, node1, node2);
				mInfoArray.add(mInfo);
			}
		}

		// �������н������У��û���Ϣֵ�ߵ��������ڹ�����
		Collections.sort(mInfoArray);

		return mInfoArray;
	}

	/**
	 * ����2�����Խڵ�Ļ���Ϣֵ
	 * 
	 * @param node1
	 *            �ڵ�1
	 * @param node2
	 *            �ڵ�2
	 * @param vlaue
	 *            �������ֵ
	 */
	private double calMutualInfoValue(Node node1, Node node2, String value) {
		double iValue;
		double temp;
		// ���ֲ�ͬ�����ĺ������
		double pXiXj;
		double pXi;
		double pXj;
		String[] array1;
		String[] array2;
		ArrayList<String> attrValues1;
		ArrayList<String> attrValues2;
		ArrayList<String[]> priorValues;
		// ������ʣ���������������ֵ
		ArrayList<String[]> backValues;

		array1 = new String[2];
		array2 = new String[2];
		priorValues = new ArrayList<>();
		backValues = new ArrayList<>();

		iValue = 0;
		array1[0] = classAttrName;
		array1[1] = value;
		// �������Զ���������
		backValues.add(array1);

		// ��ȡ�ڵ����Ե�����ֵ����
		attrValues1 = this.attr2Values.get(node1.name);
		attrValues2 = this.attr2Values.get(node2.name);

		for (String v1 : attrValues1) {
			for (String v2 : attrValues2) {
				priorValues.clear();

				array1 = new String[2];
				array1[0] = node1.name;
				array1[1] = v1;
				priorValues.add(array1);

				array2 = new String[2];
				array2[0] = node2.name;
				array2[1] = v2;
				priorValues.add(array2);

				// ����3�������µĸ���
				pXiXj = queryConditionPro(priorValues, backValues);

				priorValues.clear();
				priorValues.add(array1);
				pXi = queryConditionPro(priorValues, backValues);

				priorValues.clear();
				priorValues.add(array2);
				pXj = queryConditionPro(priorValues, backValues);

				// ����������һ���������Ϊ0����ֱ�Ӹ�ֵΪ0����
				if (pXiXj == 0 || pXi == 0 || pXj == 0) {
					temp = 0;
				} else {
					// ���ù�ʽ������Դ�����ֵ����ϵĸ���
					temp = pXiXj * Math.log(pXiXj / (pXi * pXj)) / Math.log(2);
				}

				// ���к�����ֵ����ϵ��ۼӼ�Ϊ������ԵĻ���Ϣֵ
				iValue += temp;
			}
		}

		return iValue;
	}
}
