package BaggingAndBoosting.DataMining_AdaBoost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * AdaBoost�����㷨������
 * 
 * @author maxu
 * 
 */
public class AdaBoostTool {
	// �������𣬳���Ĭ��Ϊ����1�͸���-1
	public static final int CLASS_POSITIVE = 1;
	public static final int CLASS_NEGTIVE = -1;

	// ���ȼ����3��������(������Ӧ�����¶���ݼ�����ѵ���õ�)
	public static final String CLASSIFICATION1 = "X=2.5";
	public static final String CLASSIFICATION2 = "X=7.5";
	public static final String CLASSIFICATION3 = "Y=5.5";

	// ��������
	public static final String[] ClASSIFICATION = new String[] {
			CLASSIFICATION1, CLASSIFICATION2, CLASSIFICATION3 };
	// ����Ȩ����
	private double[] CLASSIFICATION_WEIGHT;

	// ��������ļ���ַ
	private String filePath;
	// �������ֵ
	private double errorValue;
	// ���е���ݵ�
	private ArrayList<Point> totalPoint;

	public AdaBoostTool(String filePath, double errorValue) {
		this.filePath = filePath;
		this.errorValue = errorValue;
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

		Point temp;
		totalPoint = new ArrayList<>();
		for (String[] array : dataArray) {
			temp = new Point(array[0], array[1], array[2]);
			temp.setProbably(1.0 / dataArray.size());
			totalPoint.add(temp);
		}
	}

	/**
	 * ��ݵ�ǰ�����ֵ�����õ�Ȩ��
	 * 
	 * @param errorValue
	 *            ��ǰ���ֵ����������
	 * @return
	 */
	private double calculateWeight(double errorValue) {
		double alpha = 0;
		double temp = 0;

		temp = (1 - errorValue) / errorValue;
		alpha = 0.5 * Math.log(temp);

		return alpha;
	}

	/**
	 * ���㵱ǰ���ֵ������
	 * 
	 * @param pointMap
	 *            ����֮��ĵ㼯
	 * @param weight
	 *            ���λ��ֵõ��ķ�����Ȩ��
	 * @return
	 */
	private double calculateErrorValue(
			HashMap<Integer, ArrayList<Point>> pointMap) {
		double resultValue = 0;
		double temp = 0;
		double weight = 0;
		int tempClassType;
		ArrayList<Point> pList;
		for (Map.Entry entry : pointMap.entrySet()) {
			tempClassType = (int) entry.getKey();

			pList = (ArrayList<Point>) entry.getValue();
			for (Point p : pList) {
				temp = p.getProbably();
				// �������Ͳ���ȣ���?����
				if (tempClassType != p.getClassType()) {
					resultValue += temp;
				}
			}
		}

		weight = calculateWeight(resultValue);
		for (Map.Entry entry : pointMap.entrySet()) {
			tempClassType = (int) entry.getKey();

			pList = (ArrayList<Point>) entry.getValue();
			for (Point p : pList) {
				temp = p.getProbably();
				// �������Ͳ���ȣ���?����
				if (tempClassType != p.getClassType()) {
					// ����ĵ��Ȩ�ر�����
					temp *= Math.exp(weight);
					p.setProbably(temp);
				} else {
					// ���Եĵ��Ȩ�رȼ�С
					temp *= Math.exp(-weight);
					p.setProbably(temp);
				}
			}
		}

		// ��������û��С����ֵ��������
		dataNormalized();

		return resultValue;
	}

	/**
	 * ��������һ������
	 */
	private void dataNormalized() {
		double sumProbably = 0;
		double temp = 0;

		for (Point p : totalPoint) {
			sumProbably += p.getProbably();
		}

		// ��һ������
		for (Point p : totalPoint) {
			temp = p.getProbably();
			p.setProbably(temp / sumProbably);
		}
	}

	/**
	 * ��AdaBoost�㷨�õ�����Ϸ���������ݽ��з���
	 * 
	 */
	public void adaBoostClassify() {
		double value = 0;
		Point p;

		calculateWeightArray();
		for (int i = 0; i < ClASSIFICATION.length; i++) {
			System.out.println(MessageFormat.format("������{0}Ȩ��Ϊ��{1}", (i+1), CLASSIFICATION_WEIGHT[i]));
		}
		
		for (int j = 0; j < totalPoint.size(); j++) {
			p = totalPoint.get(j);
			value = 0;

			for (int i = 0; i < ClASSIFICATION.length; i++) {
				value += 1.0 * classifyData(ClASSIFICATION[i], p)
						* CLASSIFICATION_WEIGHT[i];
			}
			
			//���з���ж�
			if (value > 0) {
				System.out
						.println(MessageFormat.format(
								"��({0}, {1})����Ϸ�����Ϊ��1���õ��ʵ�ʷ���Ϊ{2}", p.getX(), p.getY(),
								p.getClassType()));
			} else {
				System.out.println(MessageFormat.format(
						"��({0}, {1})����Ϸ�����Ϊ��-1���õ��ʵ�ʷ���Ϊ{2}", p.getX(), p.getY(),
						p.getClassType()));
			}
		}
	}

	/**
	 * ���������Ȩ������
	 */
	private void calculateWeightArray() {
		int tempClassType = 0;
		double errorValue = 0;
		ArrayList<Point> posPointList;
		ArrayList<Point> negPointList;
		HashMap<Integer, ArrayList<Point>> mapList;
		CLASSIFICATION_WEIGHT = new double[ClASSIFICATION.length];

		for (int i = 0; i < CLASSIFICATION_WEIGHT.length; i++) {
			mapList = new HashMap<>();
			posPointList = new ArrayList<>();
			negPointList = new ArrayList<>();

			for (Point p : totalPoint) {
				tempClassType = classifyData(ClASSIFICATION[i], p);

				if (tempClassType == CLASS_POSITIVE) {
					posPointList.add(p);
				} else {
					negPointList.add(p);
				}
			}

			mapList.put(CLASS_POSITIVE, posPointList);
			mapList.put(CLASS_NEGTIVE, negPointList);

			if (i == 0) {
				// �ʼ�ĸ������Ȩ��һ�����Դ���0��ʹ��e��0�η�����1
				errorValue = calculateErrorValue(mapList);
			} else {
				// ÿ�ΰ��ϴμ�����õ�Ȩ�ش��룬���и��ʵ��������С
				errorValue = calculateErrorValue(mapList);
			}

			// ���㵱ǰ�����������Ȩ��
			CLASSIFICATION_WEIGHT[i] = calculateWeight(errorValue);
		}
	}

	/**
	 * �ø����ӷ��������з���
	 * 
	 * @param classification
	 *            ���������
	 * @param p
	 *            �������
	 * @return
	 */
	private int classifyData(String classification, Point p) {
		// �ָ������������
		String position;
		// �ָ��ߵ�ֵ
		double value = 0;
		double posProbably = 0;
		double negProbably = 0;
		// �����Ƿ��Ǵ���һ�ߵĻ���
		boolean isLarger = false;
		String[] array;
		ArrayList<Point> pList = new ArrayList<>();

		array = classification.split("=");
		position = array[0];
		value = Double.parseDouble(array[1]);

		if (position.equals("X")) {
			if (p.getX() > value) {
				isLarger = true;
			}

			// ��ѵ�����������������ߵĵ����
			for (Point point : totalPoint) {
				if (isLarger && point.getX() > value) {
					pList.add(point);
				} else if (!isLarger && point.getX() < value) {
					pList.add(point);
				}
			}
		} else if (position.equals("Y")) {
			if (p.getY() > value) {
				isLarger = true;
			}

			// ��ѵ�����������������ߵĵ����
			for (Point point : totalPoint) {
				if (isLarger && point.getY() > value) {
					pList.add(point);
				} else if (!isLarger && point.getY() < value) {
					pList.add(point);
				}
			}
		}

		for (Point p2 : pList) {
			if (p2.getClassType() == CLASS_POSITIVE) {
				posProbably++;
			} else {
				negProbably++;
			}
		}
		
		//���ఴ�����������л���
		if (posProbably > negProbably) {
			return CLASS_POSITIVE;
		} else {
			return CLASS_NEGTIVE;
		}
	}

}
