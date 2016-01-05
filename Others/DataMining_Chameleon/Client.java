package Others.DataMining_Chameleon;

/**
 * Chameleon(��ɫ��)���׶ξ����㷨
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\graphData.txt";
		//k-���ڵ�k����
		int k = 1;
		//����������ֵ
		double minMetric = 0.1;
		
		ChameleonTool tool = new ChameleonTool(filePath, k, minMetric);
		tool.buildCluster();
	}
}
