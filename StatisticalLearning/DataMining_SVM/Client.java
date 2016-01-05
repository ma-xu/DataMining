package StatisticalLearning.DataMining_SVM;

/**
 * SVM֧��������������
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		//ѵ��������ļ�·��
		String trainDataPath = "C:\\Users\\lyq\\Desktop\\icon\\trainInput.txt";
		//��������ļ�·��
		String testDataPath = "C:\\Users\\lyq\\Desktop\\icon\\testInput.txt";
		
		SVMTool tool = new SVMTool(trainDataPath);
		//�Բ�����ݽ���svm֧�����������
		tool.svmPredictData(testDataPath);
	}

}
