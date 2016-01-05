package BaggingAndBoosting.DataMining_AdaBoost;

/**
 * AdaBoost�����㷨������
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] agrs){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		//�������ֵ
		double errorValue = 0.2;
		
		AdaBoostTool tool = new AdaBoostTool(filePath, errorValue);
		tool.adaBoostClassify();
	}
}
