package StatisticalLearning.DataMining_SVM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import StatisticalLearning.DataMining_SVM.libsvm.svm;
import StatisticalLearning.DataMining_SVM.libsvm.svm_model;
import StatisticalLearning.DataMining_SVM.libsvm.svm_node;
import StatisticalLearning.DataMining_SVM.libsvm.svm_parameter;
import StatisticalLearning.DataMining_SVM.libsvm.svm_problem;

/**
 * SVM֧�����������
 * 
 * @author maxu
 * 
 */
public class SVMTool {
	// ѵ��������ļ�·��
	private String trainDataPath;
	// svm_problem�������ڹ���svm modelģ��
	private svm_problem sProblem;
	// svm����������svm֧������������ͺͲ�ͬ ��svm�ĺ˺�������
	private svm_parameter sParam;

	public SVMTool(String trainDataPath) {
		this.trainDataPath = trainDataPath;

		// ��ʼ��svm��ر���
		sProblem = initSvmProblem();
		sParam = initSvmParam();
	}
	
	/**
	 * ��ʼ�����������ѵ������ݹ������ģ��
	 */
	private void initOperation(){
		
	}

	/**
	 * svm_problem����ѵ������ݵ������Ϣ����
	 * 
	 * @return
	 */
	private svm_problem initSvmProblem() {
		List<Double> label = new ArrayList<Double>();
		List<svm_node[]> nodeSet = new ArrayList<svm_node[]>();
		getData(nodeSet, label, trainDataPath);

		int dataRange = nodeSet.get(0).length;
		svm_node[][] datas = new svm_node[nodeSet.size()][dataRange]; // ѵ������������
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < dataRange; j++) {
				datas[i][j] = nodeSet.get(i)[j];
			}
		}
		double[] lables = new double[label.size()]; // a,b ��Ӧ��lable
		for (int i = 0; i < lables.length; i++) {
			lables[i] = label.get(i);
		}

		// ����svm_problem����
		svm_problem problem = new svm_problem();
		problem.l = nodeSet.size(); // ��������
		problem.x = datas; // ѵ����������
		problem.y = lables; // ��Ӧ��lable����

		return problem;
	}

	/**
	 * ��ʼ��svm֧��������Ĳ������svm�����ͺͺ˺��������
	 * 
	 * @return
	 */
	private svm_parameter initSvmParam() {
		// ����svm_parameter����
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.EPSILON_SVR;
		// ����svm�ĺ˺�������Ϊ����
		param.kernel_type = svm_parameter.LINEAR;
		// ����Ĳ�������ֻ���ѵ���������
		param.cache_size = 100;
		param.eps = 0.00001;
		param.C = 1.9;

		return param;
	}

	/**
	 * ͨ��svm��ʽԤ����ݵ�����
	 * 
	 * @param testDataPath
	 */
	public void svmPredictData(String testDataPath) {
		// ��ȡ�������
		List<Double> testlabel = new ArrayList<Double>();
		List<svm_node[]> testnodeSet = new ArrayList<svm_node[]>();
		getData(testnodeSet, testlabel, testDataPath);
		int dataRange = testnodeSet.get(0).length;

		svm_node[][] testdatas = new svm_node[testnodeSet.size()][dataRange]; // ѵ������������
		for (int i = 0; i < testdatas.length; i++) {
			for (int j = 0; j < dataRange; j++) {
				testdatas[i][j] = testnodeSet.get(i)[j];
			}
		}
		// ������ݵ���ʵֵ���ں��潫����svm��Ԥ��ֵ���Ƚ�
		double[] testlables = new double[testlabel.size()]; // a,b ��Ӧ��lable
		for (int i = 0; i < testlables.length; i++) {
			testlables[i] = testlabel.get(i);
		}

		// ������û�����⣬��svm.svm_check_parameter()�����null,���򷵻�error������
		// ��svm�����ò������֤����Ϊ��Щ����ֻ��Բ��ֵ�֧�������������
		System.out.println(svm.svm_check_parameter(sProblem, sParam));
		System.out.println("------------�������-----------");
		// ѵ��SVM����ģ��
		svm_model model = svm.svm_train(sProblem, sParam);

		// Ԥ�������ݵ�lable
		double err = 0.0;
		for (int i = 0; i < testdatas.length; i++) {
			double truevalue = testlables[i];
			// ���������ʵֵ
			System.out.print(truevalue + " ");
			double predictValue = svm.svm_predict(model, testdatas[i]);
			// �������Ԥ��ֵ
			System.out.println(predictValue);
		}
	}

	/**
	 * ���ļ��л�ȡ���
	 * 
	 * @param nodeSet
	 *            �����ڵ�
	 * @param label
	 *            �ڵ�ֵ����ֵ
	 * @param filename
	 *            ����ļ���ַ
	 */
	private void getData(List<svm_node[]> nodeSet, List<Double> label,
			String filename) {
		try {

			FileReader fr = new FileReader(new File(filename));
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] datas = line.split(",");
				svm_node[] vector = new svm_node[datas.length - 1];
				for (int i = 0; i < datas.length - 1; i++) {
					svm_node node = new svm_node();
					node.index = i + 1;
					node.value = Double.parseDouble(datas[i]);
					vector[i] = node;
				}
				nodeSet.add(vector);
				double lablevalue = Double.parseDouble(datas[datas.length - 1]);
				label.add(lablevalue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
