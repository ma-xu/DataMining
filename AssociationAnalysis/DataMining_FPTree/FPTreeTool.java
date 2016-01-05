package AssociationAnalysis.DataMining_FPTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * FPTree�㷨������
 * 
 * @author lyq
 * 
 */
public class FPTreeTool {
	// ��������ļ�λ��
	private String filePath;
	// ��С֧�ֶ���ֵ
	private int minSupportCount;
	// ��������ID��¼
	private ArrayList<String[]> totalGoodsID;
	// ����ID��ͳ����Ŀӳ����������������ʹ��
	private HashMap<String, Integer> itemCountMap;

	public FPTreeTool(String filePath, int minSupportCount) {
		this.filePath = filePath;
		this.minSupportCount = minSupportCount;
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

		String[] temp;
		int count = 0;
		itemCountMap = new HashMap<>();
		totalGoodsID = new ArrayList<>();
		for (String[] a : dataArray) {
			temp = new String[a.length - 1];
			System.arraycopy(a, 1, temp, 0, a.length - 1);
			totalGoodsID.add(temp);
			for (String s : temp) {
				if (!itemCountMap.containsKey(s)) {
					count = 1;
				} else {
					count = ((int) itemCountMap.get(s));
					// ֧�ֶȼ����1
					count++;
				}
				// ���±���
				itemCountMap.put(s, count);
			}
		}
	}

	/**
	 * ��������¼����FP��
	 */
	private void buildFPTree(ArrayList<String> suffixPattern,
			ArrayList<ArrayList<TreeNode>> transctionList) {
		// ����һ���ո�ڵ�
		TreeNode rootNode = new TreeNode(null, 0);
		int count = 0;
		// �ڵ��Ƿ����
		boolean isExist = false;
		ArrayList<TreeNode> childNodes;
		ArrayList<TreeNode> pathList;
		// ��ͬ���ͽڵ����?���ڹ�����µ�FP��
		HashMap<String, ArrayList<TreeNode>> linkedNode = new HashMap<>();
		HashMap<String, Integer> countNode = new HashMap<>();
		// ��������¼��һ��������FP��
		for (ArrayList<TreeNode> array : transctionList) {
			TreeNode searchedNode;
			pathList = new ArrayList<>();
			for (TreeNode node : array) {
				pathList.add(node);
				nodeCounted(node, countNode);
				searchedNode = searchNode(rootNode, pathList);
				childNodes = searchedNode.getChildNodes();

				if (childNodes == null) {
					childNodes = new ArrayList<>();
					childNodes.add(node);
					searchedNode.setChildNodes(childNodes);
					node.setParentNode(searchedNode);
					nodeAddToLinkedList(node, linkedNode);
				} else {
					isExist = false;
					for (TreeNode node2 : childNodes) {
						// ����ҵ������ͬ�������֧�ֶȼ���
						if (node.getName().equals(node2.getName())) {
							count = node2.getCount() + node.getCount();
							node2.setCount(count);
							// ��ʶ���ҵ��ڵ�λ��
							isExist = true;
							break;
						}
					}

					if (!isExist) {
						// ���û���ҵ���������ӽڵ�
						childNodes.add(node);
						node.setParentNode(searchedNode);
						nodeAddToLinkedList(node, linkedNode);
					}
				}

			}
		}

		// ���FP���Ѿ��ǵ���·�����������ʱ��Ƶ��ģʽ
		if (isSinglePath(rootNode)) {
			printFrequentPattern(suffixPattern, rootNode);
			System.out.println("-------");
		} else {
			ArrayList<ArrayList<TreeNode>> tList;
			ArrayList<String> sPattern;
			if (suffixPattern == null) {
				sPattern = new ArrayList<>();
			} else {
				// ����һ�����������⻥�����õ�Ӱ��
				sPattern = (ArrayList<String>) suffixPattern.clone();
			}

			// ���ýڵ����?���µ�����
			for (Map.Entry entry : countNode.entrySet()) {
				// ��ӵ���׺ģʽ��
				sPattern.add((String) entry.getKey());
				//��ȡ��������ģʽ����Ϊ�µ�����
				tList = getTransactionList((String) entry.getKey(), linkedNode);
				
				System.out.print("[��׺ģʽ]��{");
				for(String s: sPattern){
					System.out.print(s + ", ");
				}
				System.out.print("}, ��ʱ������ģʽ��");
				for(ArrayList<TreeNode> tnList: tList){
					System.out.print("{");
					for(TreeNode n: tnList){
						System.out.print(n.getName() + ", ");
					}
					System.out.print("}, ");
				}
				System.out.println();
				// �ݹ鹹��FP��
				buildFPTree(sPattern, tList);
				// �ٴ��Ƴ������첻ͬ�ĺ�׺ģʽ����ֹ�Ժ�����ɸ���
				sPattern.remove((String) entry.getKey());
			}
		}
	}

	/**
	 * ���ڵ���뵽ͬ���ͽڵ��������
	 * 
	 * @param node
	 *            �����ڵ�
	 * @param linkedList
	 *            ����ͼ
	 */
	private void nodeAddToLinkedList(TreeNode node,
			HashMap<String, ArrayList<TreeNode>> linkedList) {
		String name = node.getName();
		ArrayList<TreeNode> list;

		if (linkedList.containsKey(name)) {
			list = linkedList.get(name);
			// ��node��ӵ��˶�����
			list.add(node);
		} else {
			list = new ArrayList<>();
			list.add(node);
			linkedList.put(name, list);
		}
	}

	/**
	 * ������?����µ�����
	 * 
	 * @param name
	 *            �ڵ����
	 * @param linkedList
	 *            ����
	 * @return
	 */
	private ArrayList<ArrayList<TreeNode>> getTransactionList(String name,
			HashMap<String, ArrayList<TreeNode>> linkedList) {
		ArrayList<ArrayList<TreeNode>> tList = new ArrayList<>();
		ArrayList<TreeNode> targetNode = linkedList.get(name);
		ArrayList<TreeNode> singleTansaction;
		TreeNode temp;

		for (TreeNode node : targetNode) {
			singleTansaction = new ArrayList<>();

			temp = node;
			while (temp.getParentNode().getName() != null) {
				temp = temp.getParentNode();
				singleTansaction.add(new TreeNode(temp.getName(), 1));
			}

			// ����֧�ֶȼ���÷�תһ��
			Collections.reverse(singleTansaction);

			for (TreeNode node2 : singleTansaction) {
				// ֧�ֶȼ��������ģʽ��׺һ��
				node2.setCount(node.getCount());
			}

			if (singleTansaction.size() > 0) {
				tList.add(singleTansaction);
			}
		}

		return tList;
	}

	/**
	 * �ڵ����
	 * 
	 * @param node
	 *            �����ڵ�
	 * @param nodeCount
	 *            ����ӳ��ͼ
	 */
	private void nodeCounted(TreeNode node, HashMap<String, Integer> nodeCount) {
		int count = 0;
		String name = node.getName();

		if (nodeCount.containsKey(name)) {
			count = nodeCount.get(name);
			count++;
		} else {
			count = 1;
		}

		nodeCount.put(name, count);
	}

	/**
	 * ��ʾ������
	 * 
	 * @param node
	 *            ����ʾ�Ľڵ�
	 * @param blankNum
	 *            �пո��������ʾ���ͽṹ
	 */
	private void showFPTree(TreeNode node, int blankNum) {
		System.out.println();
		for (int i = 0; i < blankNum; i++) {
			System.out.print("\t");
		}
		System.out.print("--");
		System.out.print("--");

		if (node.getChildNodes() == null) {
			System.out.print("[");
			System.out.print("I" + node.getName() + ":" + node.getCount());
			System.out.print("]");
		} else {
			// �ݹ���ʾ�ӽڵ�
			// System.out.print("��" + node.getName() + "��");
			for (TreeNode childNode : node.getChildNodes()) {
				showFPTree(childNode, 2 * blankNum);
			}
		}

	}

	/**
	 * �����ڵ�ĵִ�λ�ýڵ㣬�Ӹ�ڵ㿪ʼ����Ѱ�Ҵ����ڵ��λ��
	 * 
	 * @param root
	 * @param list
	 * @return
	 */
	private TreeNode searchNode(TreeNode node, ArrayList<TreeNode> list) {
		ArrayList<TreeNode> pathList = new ArrayList<>();
		TreeNode tempNode = null;
		TreeNode firstNode = list.get(0);
		boolean isExist = false;
		// ����תһ�飬�������ͬһ����
		for (TreeNode node2 : list) {
			pathList.add(node2);
		}

		// ���û�к��ӽڵ㣬��ֱ�ӷ��أ��ڴ˽ڵ�������ӽڵ�
		if (node.getChildNodes() == null) {
			return node;
		}

		for (TreeNode n : node.getChildNodes()) {
			if (n.getName().equals(firstNode.getName()) && list.size() == 1) {
				tempNode = node;
				isExist = true;
				break;
			} else if (n.getName().equals(firstNode.getName())) {
				// ��û���ҵ�����λ�ã�������
				pathList.remove(firstNode);
				tempNode = searchNode(n, pathList);
				return tempNode;
			}
		}

		// ���û���ҵ���������ӵ����ӽڵ���
		if (!isExist) {
			tempNode = node;
		}

		return tempNode;
	}

	/**
	 * �ж�Ŀǰ�����FP���Ƿ��ǵ���·����
	 * 
	 * @param rootNode
	 *            ��ǰFP���ĸ�ڵ�
	 * @return
	 */
	private boolean isSinglePath(TreeNode rootNode) {
		// Ĭ���ǵ���·��
		boolean isSinglePath = true;
		ArrayList<TreeNode> childList;
		TreeNode node;
		node = rootNode;

		while (node.getChildNodes() != null) {
			childList = node.getChildNodes();
			if (childList.size() == 1) {
				node = childList.get(0);
			} else {
				isSinglePath = false;
				break;
			}
		}

		return isSinglePath;
	}

	/**
	 * ��ʼ����FP��
	 */
	public void startBuildingTree() {
		ArrayList<TreeNode> singleTransaction;
		ArrayList<ArrayList<TreeNode>> transactionList = new ArrayList<>();
		TreeNode tempNode;
		int count = 0;

		for (String[] idArray : totalGoodsID) {
			singleTransaction = new ArrayList<>();
			for (String id : idArray) {
				count = itemCountMap.get(id);
				tempNode = new TreeNode(id, count);
				singleTransaction.add(tempNode);
			}

			// ���֧�ֶ���Ķ��ٽ�������
			Collections.sort(singleTransaction);
			for (TreeNode node : singleTransaction) {
				// ֧�ֶȼ������¹�Ϊ1
				node.setCount(1);
			}
			transactionList.add(singleTransaction);
		}

		buildFPTree(null, transactionList);
	}

	/**
	 * ����˵���·���µ�Ƶ��ģʽ
	 * 
	 * @param suffixPattern
	 *            ��׺ģʽ
	 * @param rootNode
	 *            ����·��FP����ڵ�
	 */
	private void printFrequentPattern(ArrayList<String> suffixPattern,
			TreeNode rootNode) {
		ArrayList<String> idArray = new ArrayList<>();
		TreeNode temp;
		temp = rootNode;
		// ����������ģʽ
		int length = 0;
		int num = 0;
		int[] binaryArray;

		while (temp.getChildNodes() != null) {
			temp = temp.getChildNodes().get(0);

			// ɸѡ֧�ֶ�ϵ�������С��ֵ��ֵ
			if (temp.getCount() >= minSupportCount) {
				idArray.add(temp.getName());
			}
		}

		length = idArray.size();
		num = (int) Math.pow(2, length);
		for (int i = 0; i < num; i++) {
			binaryArray = new int[length];
			numToBinaryArray(binaryArray, i);

			// ����׺ģʽֻ��1���������������
			if (suffixPattern.size() == 1 && i == 0) {
				continue;
			}

			System.out.print("Ƶ��ģʽ��{����׺ģʽ��");
			// ��������еĺ�׺ģʽ
			if (suffixPattern.size() > 1
					|| (suffixPattern.size() == 1 && idArray.size() > 0)) {
				for (String s : suffixPattern) {
					System.out.print(s + ", ");
				}
			}
			System.out.print("��");
			// ���·���ϵ����ģʽ
			for (int j = 0; j < length; j++) {
				if (binaryArray[j] == 1) {
					System.out.print(idArray.get(j) + ", ");
				}
			}
			System.out.println("}");
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

}
