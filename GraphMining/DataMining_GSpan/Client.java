package GraphMining.DataMining_GSpan;

/**
 * gSpanƵ����ͼ�ھ��㷨
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		//��������ļ���ַ
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		//��С֧�ֶ���
		double minSupportRate = 0.2;
		
		GSpanTool tool = new GSpanTool(filePath, minSupportRate);
		tool.freqGraphMining();
	}
}
