package Others.DataMining_KDTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * KD��-kά�ռ�ؼ���ݼ����㷨������
 * 
 * @author maxu
 * 
 */
public class KDTreeTool {
	// �ռ�ƽ��ķ���
	public static final int DIRECTION_X = 0;
	public static final int DIRECTION_Y = 1;

	// ����Ĳ�����������ļ�
	private String filePath;
	// ԭʼ������ݵ����
	private ArrayList<Point> totalDatas;
	// KD����ڵ�
	private TreeNode rootNode;

	public KDTreeTool(String filePath) {
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
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		Point p;
		totalDatas = new ArrayList<>();
		for (String[] array : dataArray) {
			p = new Point(array[0], array[1]);
			totalDatas.add(p);
		}
	}

	/**
	 * ����KD��
	 * 
	 * @return
	 */
	public TreeNode createKDTree() {
		ArrayList<Point> copyDatas;

		rootNode = new TreeNode();
		// ��ݽڵ㿪ʼʱ���ʾ�Ŀռ�ʱ���޴��
		rootNode.range = new Range();
		copyDatas = (ArrayList<Point>) totalDatas.clone();
		recusiveConstructNode(rootNode, copyDatas);

		return rootNode;
	}

	/**
	 * �ݹ����KD���Ĺ���
	 * 
	 * @param node
	 *            ��ǰ���ڹ���Ľڵ�
	 * @param datas
	 *            �ýڵ��Ӧ�����ڴ�������
	 * @return
	 */
	private void recusiveConstructNode(TreeNode node, ArrayList<Point> datas) {
		int direction = 0;
		ArrayList<Point> leftSideDatas;
		ArrayList<Point> rightSideDatas;
		Point p;
		TreeNode leftNode;
		TreeNode rightNode;
		Range range;
		Range range2;

		// ���ֵ���ݵ㼯��ֻ��1����ݣ����ٻ���
		if (datas.size() == 1) {
			node.nodeData = datas.get(0);
			return;
		}

		// �����ڵ�ǰ����ݵ㼯���н��зָ���ѡ��
		direction = selectSplitDrc(datas);
		// ��ݷ���ȡ����λ�����Ϊ���ʸ��
		p = getMiddlePoint(datas, direction);

		node.spilt = direction;
		node.nodeData = p;

		leftSideDatas = getLeftSideDatas(datas, p, direction);
		datas.removeAll(leftSideDatas);
		// ��Ҫȥ������
		datas.remove(p);
		rightSideDatas = datas;

		if (leftSideDatas.size() > 0) {
			leftNode = new TreeNode();
			leftNode.parentNode = node;
			range2 = Range.initLeftRange(p, direction);
			// ��ȡ���ڵ�Ŀռ�ʸ�������н�����������Χ���
			range = node.range.crossOperation(range2);
			leftNode.range = range;

			node.leftNode = leftNode;
			recusiveConstructNode(leftNode, leftSideDatas);
		}

		if (rightSideDatas.size() > 0) {
			rightNode = new TreeNode();
			rightNode.parentNode = node;
			range2 = Range.initRightRange(p, direction);
			// ��ȡ���ڵ�Ŀռ�ʸ�������н�����������Χ���
			range = node.range.crossOperation(range2);
			rightNode.range = range;

			node.rightNode = rightNode;
			recusiveConstructNode(rightNode, rightSideDatas);
		}
	}

	/**
	 * ����������ݵ������
	 * 
	 * @param p
	 *            ��Ƚ�����
	 */
	public Point searchNearestData(Point p) {
		// �ڵ�������ݵ�ľ���
		TreeNode nearestNode = null;
		// ��ջ��¼�����Ľڵ�
		Stack<TreeNode> stackNodes;

		stackNodes = new Stack<>();
		findedNearestLeafNode(p, rootNode, stackNodes);

		// ȡ��Ҷ�ӽڵ㣬��Ϊ��ǰ�ҵ������ڵ�
		nearestNode = stackNodes.pop();
		nearestNode = dfsSearchNodes(stackNodes, p, nearestNode);

		return nearestNode.nodeData;
	}

	/**
	 * ������ȵķ�ʽ��������Ĳ���
	 * 
	 * @param stack
	 *            KD���ڵ�ջ
	 * @param desPoint
	 *            �����ݵ�
	 * @param nearestNode
	 *            ��ǰ�ҵ������ڵ�
	 * @return
	 */
	private TreeNode dfsSearchNodes(Stack<TreeNode> stack, Point desPoint,
			TreeNode nearestNode) {
		// �Ƿ��������ڵ�߽�
		boolean isCollision;
		double minDis;
		double dis;
		TreeNode parentNode;

		// ���ջ�ڽڵ��Ѿ�ȫ����������������
		if (stack.isEmpty()) {
			return nearestNode;
		}

		// ��ȡ���ڵ�
		parentNode = stack.pop();

		minDis = desPoint.ouDistance(nearestNode.nodeData);
		dis = desPoint.ouDistance(parentNode.nodeData);

		// ����뵱ǰ���ݵ��ĸ��ڵ�����̣����������Ľڵ���и���
		if (dis < minDis) {
			minDis = dis;
			nearestNode = parentNode;
		}

		// Ĭ��û����ײ��
		isCollision = false;
		// �ж��Ƿ������˸��ڵ�Ŀռ�ָ���
		if (parentNode.spilt == DIRECTION_X) {
			if (parentNode.nodeData.x > desPoint.x - minDis
					&& parentNode.nodeData.x < desPoint.x + minDis) {
				isCollision = true;
			}
		} else {
			if (parentNode.nodeData.y > desPoint.y - minDis
					&& parentNode.nodeData.y < desPoint.y + minDis) {
				isCollision = true;
			}
		}

		// ����������߽��ˣ����Ҵ˽ڵ�ĺ��ӽڵ㻹δ��ȫ�����꣬����Լ������
		if (isCollision
				&& (!parentNode.leftNode.isVisited || !parentNode.rightNode.isVisited)) {
			TreeNode newNode;
			// �½���ǰ��С�ֲ��ڵ�ջ
			Stack<TreeNode> otherStack = new Stack<>();
			// ��parentNode�������¼���Ѱ��
			findedNearestLeafNode(desPoint, parentNode, otherStack);
			newNode = dfsSearchNodes(otherStack, desPoint, otherStack.pop());

			dis = newNode.nodeData.ouDistance(desPoint);
			if (dis < minDis) {
				nearestNode = newNode;
			}
		}

		// �������ϻ���
		nearestNode = dfsSearchNodes(stack, desPoint, nearestNode);

		return nearestNode;
	}

	/**
	 * �ҵ������ڵ������Ҷ�ӽڵ�
	 * 
	 * @param p
	 *            ��ȽϽڵ�
	 * @param node
	 *            ��ǰ�������Ľڵ�
	 * @param stack
	 *            �����Ľڵ�ջ
	 */
	private void findedNearestLeafNode(Point p, TreeNode node,
			Stack<TreeNode> stack) {
		// �ָ��
		int splitDic;

		// �������Ľڵ����ջ��
		stack.push(node);
		// ���Ϊ���ʹ�
		node.isVisited = true;
		// ���˽ڵ�û�����Һ��ӽڵ�˵���Ѿ���Ҷ�ӽڵ���
		if (node.leftNode == null && node.rightNode == null) {
			return;
		}

		splitDic = node.spilt;
		// ѡ��һ����ϷָΧ�Ľڵ����ݹ���Ѱ
		if ((splitDic == DIRECTION_X && p.x < node.nodeData.x)
				|| (splitDic == DIRECTION_Y && p.y < node.nodeData.y)) {
			if (!node.leftNode.isVisited) {
				findedNearestLeafNode(p, node.leftNode, stack);
			} else {
				// ������ӽڵ��Ѿ����ʹ��������һ��
				findedNearestLeafNode(p, node.rightNode, stack);
			}
		} else if ((splitDic == DIRECTION_X && p.x > node.nodeData.x)
				|| (splitDic == DIRECTION_Y && p.y > node.nodeData.y)) {
			if (!node.rightNode.isVisited) {
				findedNearestLeafNode(p, node.rightNode, stack);
			} else {
				// ����Һ��ӽڵ��Ѿ����ʹ��������һ��
				findedNearestLeafNode(p, node.leftNode, stack);
			}
		}
	}

	/**
	 * ��ݸ����ݵ�ͨ����㷴��ѡ��ķָ��
	 * 
	 * @param datas
	 *            ���ֵļ��ϵ㼯��
	 * @return
	 */
	private int selectSplitDrc(ArrayList<Point> datas) {
		int direction = 0;
		double avgX = 0;
		double avgY = 0;
		double varianceX = 0;
		double varianceY = 0;

		for (Point p : datas) {
			avgX += p.x;
			avgY += p.y;
		}

		avgX /= datas.size();
		avgY /= datas.size();

		for (Point p : datas) {
			varianceX += (p.x - avgX) * (p.x - avgX);
			varianceY += (p.y - avgY) * (p.y - avgY);
		}

		// �����ķ���
		varianceX /= datas.size();
		varianceY /= datas.size();

		// ͨ��ȽϷ���Ĵ�С�����ָ��ѡ�񲨶��ϴ�Ľ��л���
		direction = varianceX > varianceY ? DIRECTION_X : DIRECTION_Y;

		return direction;
	}

	/**
	 * ������㷽λ��������ѡ���м���������
	 * 
	 * @param datas
	 *            ��ݵ㼯��
	 * @param dir
	 *            �������귽��
	 */
	private Point getMiddlePoint(ArrayList<Point> datas, int dir) {
		int index = 0;
		Point middlePoint;

		index = datas.size() / 2;
		if (dir == DIRECTION_X) {
			Collections.sort(datas, new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					// TODO Auto-generated method stub
					return o1.x.compareTo(o2.x);
				}
			});
		} else {
			Collections.sort(datas, new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					// TODO Auto-generated method stub
					return o1.y.compareTo(o2.y);
				}
			});
		}

		// ȡ����λ��
		middlePoint = datas.get(index);

		return middlePoint;
	}

	/**
	 * ��ݷ���õ�ԭ���ֽڵ㼯��������ݵ�
	 * 
	 * @param datas
	 *            ԭʼ��ݵ㼯��
	 * @param nodeData
	 *            ���ʸ��
	 * @param dir
	 *            �ָ��
	 * @return
	 */
	private ArrayList<Point> getLeftSideDatas(ArrayList<Point> datas,
			Point nodeData, int dir) {
		ArrayList<Point> leftSideDatas = new ArrayList<>();

		for (Point p : datas) {
			if (dir == DIRECTION_X && p.x < nodeData.x) {
				leftSideDatas.add(p);
			} else if (dir == DIRECTION_Y && p.y < nodeData.y) {
				leftSideDatas.add(p);
			}
		}

		return leftSideDatas;
	}
}
