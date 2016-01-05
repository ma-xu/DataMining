package Clustering.DataMining_BIRCH;

/**
 * BIRCH�����㷨������
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\testInput.txt";
		//�ڲ��ڵ�ƽ������B
		int B = 2;
		//Ҷ�ӽڵ�ƽ������L
		int L = 2;
		//��ֱ����ֵT
		double T = 0.6;
		
		BIRCHTool tool = new BIRCHTool(filePath, B, L, T);
		tool.startBuilding();
	}
}
