package Others.DataMining_BayesNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ��Ҷ˹�����㷨������
 * 
 * @author maxu
 * 
 */
public class BayesNetWorkTool {
	// ���ϸ��ʷֲ�����ļ���ַ
	private String dataFilePath;
	// �¼���������ļ���ַ
	private String attachFilePath;
	// ����������
	private int columns;
	// ���ʷֲ����
	private String[][] totalData;
	// ������ݶ�
	private ArrayList<String[]> attachData;
	// �ڵ����б�
	private ArrayList<Node> nodes;
	// ������������֮��Ķ�Ӧ��ϵ
	private HashMap<String, Integer> attr2Column;

	public BayesNetWorkTool(String dataFilePath, String attachFilePath) {
		this.dataFilePath = dataFilePath;
		this.attachFilePath = attachFilePath;

		initDatas();
	}

	/**
	 * ��ʼ��������ݺ͸��ʷֲ����
	 */
	private void initDatas() {
		String[] columnValues;
		String[] array;
		ArrayList<String> datas;
		ArrayList<String> adatas;

		// ���ļ��ж�ȡ���
		datas = readDataFile(dataFilePath);
		adatas = readDataFile(attachFilePath);

		columnValues = datas.get(0).split(" ");
		// ���Ը���ƴ���¼�B(����)��E(����)��A(������).M(�ӵ�M�ĵ绰)��JͬM����˼,
		// ����ֵ����y,n���yes�����no������
		this.attr2Column = new HashMap<>();
		for (int i = 0; i < columnValues.length; i++) {
			// �������ȡ����������У�����ֵ����ͼ��
			this.attr2Column.put(columnValues[i], i);
		}

		this.columns = columnValues.length;
		this.totalData = new String[datas.size()][columns];
		for (int i = 0; i < datas.size(); i++) {
			this.totalData[i] = datas.get(i).split(" ");
		}

		this.attachData = new ArrayList<>();
		// ����������ݶ�
		for (String str : adatas) {
			array = str.split(" ");
			this.attachData.add(array);
		}

		// ���챴Ҷ˹����ṹͼ
		constructDAG();
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	private ArrayList<String> readDataFile(String filePath) {
		File file = new File(filePath);
		ArrayList<String> dataArray = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null) {
				dataArray.add(str);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		return dataArray;
	}

	/**
	 * ��ݹ�����ݹ��챴Ҷ˹�����޻�����ͼ
	 */
	private void constructDAG() {
		// �ڵ���ڱ�ʶ
		boolean srcExist;
		boolean desExist;
		String name1;
		String name2;
		Node srcNode;
		Node desNode;

		this.nodes = new ArrayList<>();
		for (String[] array : this.attachData) {
			srcExist = false;
			desExist = false;

			name1 = array[0];
			name2 = array[1];

			// �½��ڵ�
			srcNode = new Node(name1);
			desNode = new Node(name2);

			for (Node temp : this.nodes) {
				// ����ҵ���ͬ�ڵ㣬��ȡ��
				if (srcNode.isEqual(temp)) {
					srcExist = true;
					srcNode = temp;
				} else if (desNode.isEqual(temp)) {
					desExist = true;
					desNode = temp;
				}

				// ���2���ڵ㶼���ҵ��������ѭ��
				if (srcExist && desExist) {
					break;
				}
			}

			// ��2���ڵ��������
			srcNode.connectNode(desNode);

			// ��ݱ�ʶ�ж��Ƿ���Ҫ�����б�������
			if (!srcExist) {
				this.nodes.add(srcNode);
			}

			if (!desExist) {
				this.nodes.add(desNode);
			}
		}
	}

	/**
	 * ��ѯ��������
	 * 
	 * @param attrValues
	 *            ��������ֵ
	 * @return
	 */
	private double queryConditionPro(ArrayList<String[]> attrValues) {
		// �ж��Ƿ�������������ֵ����
		boolean hasPrior;
		// �ж��Ƿ������������ֵ����
		boolean hasBack;
		int priorIndex;
		int attrIndex;
		double backPro;
		double totalPro;
		double pro;
		double currentPro;
		// ��������
		String[] priorValue;
		String[] tempData;

		pro = 0;
		totalPro = 0;
		backPro = 0;
		attrValues.get(0);
		priorValue = attrValues.get(0);
		// �õ��������
		attrValues.remove(0);

		// ȡ���������Ե�����
		priorIndex = this.attr2Column.get(priorValue[0]);
		// ����һ�е����������
		for (int i = 1; i < this.totalData.length; i++) {
			tempData = this.totalData[i];

			hasPrior = false;
			hasBack = true;

			// ��ǰ�еĸ���
			currentPro = Double.parseDouble(tempData[this.columns - 1]);
			// �ж��Ƿ�������������
			if (tempData[priorIndex].equals(priorValue[1])) {
				hasPrior = true;
			}

			for (String[] array : attrValues) {
				attrIndex = this.attr2Column.get(array[0]);

				// �ж�ֵ�Ƿ���������
				if (!tempData[attrIndex].equals(array[1])) {
					hasBack = false;
					break;
				}
			}

			// ���м���ͳ�ƣ��ֱ��������������Ե�ֵ��ͬʱ���������ĸ���
			if (hasBack) {
				backPro += currentPro;
				if (hasPrior) {
					totalPro += currentPro;
				}
			} else if (hasPrior && attrValues.size() == 0) {
				// ���ֻ�����������Ϊ�����ʵļ���
				totalPro += currentPro;
				backPro = 1.0;
			}
		}

		// �����ܵĸ���=���������/ֻ�������������ʱ�����
		pro = totalPro / backPro;

		return pro;
	}

	/**
	 * ��ݱ�Ҷ˹����������
	 * 
	 * @param queryStr
	 *            ��ѯ������
	 * @return
	 */
	public double calProByNetWork(String queryStr) {
		double temp;
		double pro;
		String[] array;
		// ��������ֵ
		String[] preValue;
		// ��������ֵ
		String[] backValue;
		// �������������ͺ�������ֵ������ֵ�Ļ���
		ArrayList<String[]> attrValues;

		// �ж��Ƿ���������ṹ
		if (!satisfiedNewWork(queryStr)) {
			return -1;
		}

		pro = 1;
		// ��������ѯ�����ķֽ�
		array = queryStr.split(",");

		// ���ʵĳ�ֵ���ڵ�һ���¼������������
		attrValues = new ArrayList<>();
		attrValues.add(array[0].split("="));
		pro = queryConditionPro(attrValues);

		for (int i = 0; i < array.length - 1; i++) {
			attrValues.clear();

			// �±�С����ǰ������ں�������
			backValue = array[i].split("=");
			preValue = array[i + 1].split("=");
			attrValues.add(preValue);
			attrValues.add(backValue);

			// �����������ĸ���ֵ
			temp = queryConditionPro(attrValues);
			// ���л�����
			pro *= temp;
		}

		return pro;
	}

	/**
	 * ��֤�¼��Ĳ�ѯ����ϵ�Ƿ����㱴Ҷ˹����
	 * 
	 * @param queryStr
	 *            ��ѯ�ַ�
	 * @return
	 */
	private boolean satisfiedNewWork(String queryStr) {
		String attrName;
		String[] array;
		boolean isExist;
		boolean isSatisfied;
		// ��ǰ�ڵ�
		Node currentNode;
		// ��ѡ�ڵ��б�
		ArrayList<Node> nodeList;

		isSatisfied = true;
		currentNode = null;
		// ����ѯ�ַ�ķֽ�
		array = queryStr.split(",");
		nodeList = this.nodes;

		for (String s : array) {
			// ��ʼʱĬ�����Զ�Ӧ�Ľڵ㲻����
			isExist = false;
			// �õ������¼���
			attrName = s.split("=")[0];

			for (Node n : nodeList) {
				if (n.name.equals(attrName)) {
					isExist = true;

					currentNode = n;
					// ��һ�ֵĺ�ѡ�ڵ�Ϊ��ǰ�ڵ�ĺ��ӽڵ�
					nodeList = currentNode.childNodes;

					break;
				}
			}

			// ������δ�ҵ��Ľڵ㣬��˵�������������ṹ���ѭ��
			if (!isExist) {
				isSatisfied = false;
				break;
			}
		}

		return isSatisfied;
	}
}
