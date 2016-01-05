package Others.DataMining_GA;

/**
 * Genetic�Ŵ��㷨������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//������Сֵ�����ֵ
		int minNum = 1;
		int maxNum = 7;
		//��ʼȺ���ģ
		int initSetsNum = 4;
		
		GATool tool = new GATool(minNum, maxNum, initSetsNum);
		tool.geneticCal();
	}
}
