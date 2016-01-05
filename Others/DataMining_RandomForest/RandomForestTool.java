package Others.DataMining_RandomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ���ɭ���㷨������
 * 
 * @author maxu
 * 
 */
public class RandomForestTool {
	// ��������ļ���ַ
	private String filePath;
	// ����������ռ�����ռ����
	private double sampleNumRatio;
	// ����ݵĲɼ���������ռ�������ı���
	private double featureNumRatio;
	// �������Ĳ�������
	private int sampleNum;
	// ����ݵĲɼ�����������
	private int featureNum;
	// ���ɭ���еľ���������Ŀ,�����ܵ������/���ڹ���ÿ��������ݵ�����
	private int treeNum;
	// ����������
	private Random random;
	// ����������������
	private String[] featureNames;
	// ԭʼ���ܵ����
	private ArrayList<String[]> totalDatas;
	// ������ɭ��
	private ArrayList<DecisionTree> decisionForest;

	public RandomForestTool(String filePath, double sampleNumRatio,
			double featureNumRatio) {
		this.filePath = filePath;
		this.sampleNumRatio = sampleNumRatio;
		this.featureNumRatio = featureNumRatio;

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

		totalDatas = dataArray;
		featureNames = totalDatas.get(0);
		sampleNum = (int) ((totalDatas.size() - 1) * sampleNumRatio);
		//������������ʱ����Ҫȥ��id���Ժ;������ԣ����������Լ���
		featureNum = (int) ((featureNames.length -2) * featureNumRatio);
		// ��������ʱ����Ҫȥ���������������
		treeNum = (totalDatas.size() - 1) / sampleNum;
	}

	/**
	 * ���������
	 */
	private DecisionTree produceDecisionTree() {
		int temp = 0;
		DecisionTree tree;
		String[] tempData;
		//������ݵ�����к���
		ArrayList<Integer> sampleRandomNum;
		//������������������к���
		ArrayList<Integer> featureRandomNum;
		ArrayList<String[]> datas;
		
		sampleRandomNum = new ArrayList<>();
		featureRandomNum = new ArrayList<>();
		datas = new ArrayList<>();
		
		for(int i=0; i<sampleNum;){
			temp = random.nextInt(totalDatas.size());
			
			//�����������������У������
			if(temp == 0){
				continue;
			}
			
			if(!sampleRandomNum.contains(temp)){
				sampleRandomNum.add(temp);
				i++;
			}
		}
		
		for(int i=0; i<featureNum;){
			temp = random.nextInt(featureNames.length);
			
			//����ǵ�һ�е����id�Ż����Ǿ��������У������
			if(temp == 0 || temp == featureNames.length-1){
				continue;
			}
			
			if(!featureRandomNum.contains(temp)){
				featureRandomNum.add(temp);
				i++;
			}
		}

		String[] singleRecord;
		String[] headCulumn = null;
		// ��ȡ��������
		for(int dataIndex: sampleRandomNum){
			singleRecord = totalDatas.get(dataIndex);
			
			//ÿ�е�����=��ѡ��������+id��
			tempData = new String[featureNum+2];
			headCulumn = new String[featureNum+2];
			
			for(int i=0,k=1; i<featureRandomNum.size(); i++,k++){
				temp = featureRandomNum.get(i);
				
				headCulumn[k] = featureNames[temp];
				tempData[k] = singleRecord[temp];
			}
			
			//����id�е���Ϣ
			headCulumn[0] = featureNames[0];
			//���Ͼ��߷����е���Ϣ
			headCulumn[featureNum+1] = featureNames[featureNames.length-1];
			tempData[featureNum+1] = singleRecord[featureNames.length-1];
			
			//����������
			datas.add(tempData);
		}
		
		//���������г������
		datas.add(0, headCulumn);
		//��ɸѡ�������������id����
		temp = 0;
		for(String[] array: datas){
			//�ӵ�2�п�ʼ��ֵ
			if(temp > 0){
				array[0] = temp + "";
			}
			
			temp++;
		}
		
		tree = new DecisionTree(datas);
		
		return tree;
	}

	/**
	 * �������ɭ��
	 */
	public void constructRandomTree() {
		DecisionTree tree;
		random = new Random();
		decisionForest = new ArrayList<>();

		System.out.println("���������ɭ���еľ�������");
		// �������������ɭ����
		for (int i = 0; i < treeNum; i++) {
			System.out.println("\n������" + (i+1));
			tree = produceDecisionTree();
			decisionForest.add(tree);
		}
	}

	/**
	 * ��ݸ�����������������ľ���
	 * 
	 * @param features
	 *            �����֪����������
	 * @return
	 */
	public String judgeClassType(String features) {
		// �������ֵ
		String resultClassType = "";
		String classType = "";
		int count = 0;
		Map<String, Integer> type2Num = new HashMap<String, Integer>();

		for (DecisionTree tree : decisionForest) {
			classType = tree.decideClassType(features);
			if (type2Num.containsKey(classType)) {
				// �������Ѿ����ڣ���ʹ������1
				count = type2Num.get(classType);
				count++;
			} else {
				count = 1;
			}

			type2Num.put(classType, count);
		}

		// ѡ���������֧�ּ�������һ�����ֵ
		count = -1;
		for (Map.Entry entry : type2Num.entrySet()) {
			if ((int) entry.getValue() > count) {
				count = (int) entry.getValue();
				resultClassType = (String) entry.getKey();
			}
		}

		return resultClassType;
	}
}
