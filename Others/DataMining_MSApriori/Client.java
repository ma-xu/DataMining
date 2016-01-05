package Others.DataMining_MSApriori;

/**
 * ���ڶ�֧�ֶȵ�Apriori�㷨������
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		//�Ƿ������������
		boolean isTransaction;
		//��������ļ���ַ
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		//��ϵ��������ļ���ַ
		String tableFilePath = "C:\\Users\\lyq\\Desktop\\icon\\input2.txt";
		//��С֧�ֶ���ֵ
		double minSup;
		// ��С���Ŷ���
		double minConf;
		//���֧�ֶȲ����ֵ
		double delta;
		//����Ŀ����С֧�ֶ���,�����е��±��������Ʒ��ID
		double[] mis;
		//msApriori�㷨������
		MSAprioriTool tool;
		
		//Ϊ�˲��Եķ��㣬ȡһ��ƫ�͵����Ŷ�ֵ0.3
		minConf = 0.3;
		minSup = 0.1;
		delta = 0.5;
		//ÿ���֧�ֶ��ʶ�Ĭ��Ϊ0.1����һ�ʹ��
		mis = new double[]{-1, 0.1, 0.1, 0.1, 0.1, 0.1};
		isTransaction = true;
		
		isTransaction = true;
		tool = new MSAprioriTool(filePath, minConf, delta, mis, isTransaction);
		tool.calFItems();
		System.out.println();
		
		isTransaction = false;
		//���³�ʼ�����
		tool = new MSAprioriTool(tableFilePath, minConf, minSup, isTransaction);
		tool.calFItems();
	}	
}
