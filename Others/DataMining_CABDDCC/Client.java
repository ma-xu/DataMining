package Others.DataMining_CABDDCC;

/**
 * ������ͨͼ�ķ��Ѿ����㷨
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] agrs){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\graphData.txt";
		//��ͨ������ֵ
		int length = 3;
		
		CABDDCCTool tool = new CABDDCCTool(filePath, length);
		tool.splitCluster();
	}
}
