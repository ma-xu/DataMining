package Classification.DataMining_KNN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * k������㷨������
 * 
 * @author lyq
 * 
 */
public class KNNTool {
	// Ϊ4���������Ȩ�أ�Ĭ��Ȩ�ر�һ��
	public int[] classWeightArray = new int[] { 1, 1, 1, 1 };
	// �������
	private String testDataPath;
	// ѵ������ݵ�ַ
	private String trainDataPath;
	// ����Ĳ�ͬ����
	private ArrayList<String> classTypes;
	// ������
	private ArrayList<Sample> resultSamples;
	// ѵ��������б�����
	private ArrayList<Sample> trainSamples;
	// ѵ�������
	private String[][] trainData;
	// ���Լ����
	private String[][] testData;

	public KNNTool(String trainDataPath, String testDataPath) {
		this.trainDataPath = trainDataPath;
		this.testDataPath = testDataPath;
		readDataFormFile();
	}

	/**
	 * ���ļ����Ķ��������ѵ����ݼ�
	 */
	private void readDataFormFile() {
		ArrayList<String[]> tempArray;

		tempArray = fileDataToArray(trainDataPath);
		trainData = new String[tempArray.size()][];
		tempArray.toArray(trainData);

		classTypes = new ArrayList<>();
		for (String[] s : tempArray) {
			if (!classTypes.contains(s[0])) {
				// �������
				classTypes.add(s[0]);
			}
		}

		tempArray = fileDataToArray(testDataPath);
		testData = new String[tempArray.size()][];
		tempArray.toArray(testData);
	}

	/**
	 * ���ļ�תΪ�б�������
	 * 
	 * @param filePath
	 *            ����ļ�������
	 */
	private ArrayList<String[]> fileDataToArray(String filePath) {
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
	 * ����������������ŷ����þ���
	 * 
	 * @param f1
	 *            ��Ƚ���1
	 * @param f2
	 *            ��Ƚ���2
	 * @return
	 */
	private int computeEuclideanDistance(Sample s1, Sample s2) {
		String[] f1 = s1.getFeatures();
		String[] f2 = s2.getFeatures();
		// ŷ����þ���
		int distance = 0;

		for (int i = 0; i < f1.length; i++) {
			int subF1 = Integer.parseInt(f1[i]);
			int subF2 = Integer.parseInt(f2[i]);

			distance += (subF1 - subF2) * (subF1 - subF2);
		}

		return distance;
	}

	/**
	 * ����K�����
	 * @param k
	 * �ڶ��ٵ�k��Χ��
	 */
	public void knnCompute(int k) {
		String className = "";
		String[] tempF = null;
		Sample temp;
		resultSamples = new ArrayList<>();
		trainSamples = new ArrayList<>();
		// ����������
		HashMap<String, Integer> classCount;
		// ���Ȩ�ر�
		HashMap<String, Integer> classWeight = new HashMap<>();
		// ���Ƚ��������ת������������
		for (String[] s : testData) {
			temp = new Sample(s);
			resultSamples.add(temp);
		}

		for (String[] s : trainData) {
			className = s[0];
			tempF = new String[s.length - 1];
			System.arraycopy(s, 1, tempF, 0, s.length - 1);
			temp = new Sample(className, tempF);
			trainSamples.add(temp);
		}

		// �����������ĵ�ѵ�������
		ArrayList<Sample> kNNSample = new ArrayList<>();
		// ����ѵ����ݼ��������������K��ѵ�������
		for (Sample s : resultSamples) {
			classCount = new HashMap<>();
			int index = 0;
			for (String type : classTypes) {
				// ��ʼʱ����Ϊ0
				classCount.put(type, 0);
				classWeight.put(type, classWeightArray[index++]);
			}
			for (Sample tS : trainSamples) {
				int dis = computeEuclideanDistance(s, tS);
				tS.setDistance(dis);
			}

			Collections.sort(trainSamples);
			kNNSample.clear();
			// ��ѡ��ǰk�������Ϊ�����׼
			for (int i = 0; i < trainSamples.size(); i++) {
				if (i < k) {
					kNNSample.add(trainSamples.get(i));
				} else {
					break;
				}
			}
			// �ж�K��ѵ����ݵĶ���ķ����׼
			for (Sample s1 : kNNSample) {
				int num = classCount.get(s1.getClassName());
				// ���з���Ȩ�صĵ��ӣ�Ĭ�����Ȩ��ƽ�ȣ������иı䣬���Ȩ�ش�Զ��Ȩ��С
				num += classWeight.get(s1.getClassName());
				classCount.put(s1.getClassName(), num);
			}

			int maxCount = 0;
			// ɸѡ��k��ѵ�������������һ������
			for (Map.Entry entry : classCount.entrySet()) {
				if ((Integer) entry.getValue() > maxCount) {
					maxCount = (Integer) entry.getValue();
					s.setClassName((String) entry.getKey());
				}
			}

			System.out.print("�������������");
			for (String s1 : s.getFeatures()) {
				System.out.print(s1 + " ");
			}
			System.out.println("���ࣺ" + s.getClassName());
		}
	}
}
