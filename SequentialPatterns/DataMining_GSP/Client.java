package SequentialPatterns.DataMining_GSP;

/**
 * GSP����ģʽ�����㷨
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\testInput.txt";
		//��С֧�ֶ���ֵ
		int minSupportCount = 2;
		//ʱ����С���
		int min_gap = 1;
		//ʩ�������
		int max_gap = 5;
		
		GSPTool tool = new GSPTool(filePath, minSupportCount, min_gap, max_gap);
		tool.gspCalculate();
	}
}
