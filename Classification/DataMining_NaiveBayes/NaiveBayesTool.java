package Classification.DataMining_NaiveBayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ���ر�Ҷ˹�㷨������
 * 
 * @author maxu
 * 
 */
public class NaiveBayesTool {
	// ���Ƿ������Ϊ2�࣬YES��NO
	private String YES = "Yes";
	private String NO = "No";

	// �ѷ���ѵ����ݼ��ļ�·��
	private String filePath;
	// �����������
	private String[] attrNames;
	// ѵ����ݼ�
	private String[][] data;

	// ÿ�����Ե�ֵ��������
	private HashMap<String, ArrayList<String>> attrValue;

	public NaiveBayesTool(String filePath) {
		this.filePath = filePath;

		readDataFile();
		initAttrValue();
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
		attrValue = new HashMap<>();
		ArrayList<String> tempValues;

		// �����еķ�ʽ������������
		for (int j = 1; j < attrNames.length; j++) {
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

	}

	/**
	 * ��classType������£�����condition�����ĸ���
	 * 
	 * @param condition
	 *            ��������
	 * @param classType
	 *            ���������
	 * @return
	 */
	private double computeConditionProbably(String condition, String classType) {
		// ����������
		int count = 0;
		// �������Ե�������
		int attrIndex = 1;
		// yes���Ƿ����
		ArrayList<String[]> yClassData = new ArrayList<>();
		// no���Ƿ����
		ArrayList<String[]> nClassData = new ArrayList<>();
		ArrayList<String[]> classData;

		for (int i = 1; i < data.length; i++) {
			// data��ݰ���yes��no����
			if (data[i][attrNames.length - 1].equals(YES)) {
				yClassData.add(data[i]);
			} else {
				nClassData.add(data[i]);
			}
		}

		if (classType.equals(YES)) {
			classData = yClassData;
		} else {
			classData = nClassData;
		}

		// ���û�����������򣬼�����Ǵ�������¼�����
		if (condition == null) {
			return 1.0 * classData.size() / (data.length - 1);
		}

		// Ѱ�Ҵ�������������
		attrIndex = getConditionAttrName(condition);

		for (String[] s : classData) {
			if (s[attrIndex].equals(condition)) {
				count++;
			}
		}

		return 1.0 * count / classData.size();
	}

	/**
	 * �������ֵ���������������Ե���ֵ
	 * 
	 * @param condition
	 *            ����
	 * @return
	 */
	private int getConditionAttrName(String condition) {
		// ��������������
		String attrName = "";
		// ������������������
		int attrIndex = 1;
		// ��ʱ����ֵ����
		ArrayList<String[]> valueTypes;
		for (Map.Entry entry : attrValue.entrySet()) {
			valueTypes = (ArrayList<String[]>) entry.getValue();
			if (valueTypes.contains(condition)
					&& !((String) entry.getKey()).equals("BuysComputer")) {
				attrName = (String) entry.getKey();
			}
		}

		for (int i = 0; i < attrNames.length - 1; i++) {
			if (attrNames[i].equals(attrName)) {
				attrIndex = i;
				break;
			}
		}

		return attrIndex;
	}

	/**
	 * �������ر�Ҷ˹����
	 * 
	 * @param data
	 *            ��������
	 */
	public String naiveBayesClassificate(String data) {
		// ������ݵ�����ֵ����
		String[] dataFeatures;
		// ��yes�������£�x�¼�����ĸ���
		double xWhenYes = 1.0;
		// ��no�������£�x�¼�����ĸ���
		double xWhenNo = 1.0;
		// ���Ҳ��yes��no������ܸ��ʣ���P(X|Ci)*P(Ci)�Ĺ�ʽ����
		double pYes = 1;
		double pNo = 1;

		dataFeatures = data.split(" ");
		for (int i = 0; i < dataFeatures.length; i++) {
			// ��Ϊ���ر�Ҷ˹�㷨�������������ģ����Կ��Խ����ۻ�ļ���
			xWhenYes *= computeConditionProbably(dataFeatures[i], YES);
			xWhenNo *= computeConditionProbably(dataFeatures[i], NO);
		}

		pYes = xWhenYes * computeConditionProbably(null, YES);
		pNo = xWhenNo * computeConditionProbably(null, NO);
		
		return (pYes > pNo ? YES : NO);
	}

}
