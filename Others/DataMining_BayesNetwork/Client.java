package Others.DataMining_BayesNetwork;

import java.text.MessageFormat;

/**
 * ��Ҷ˹���糡��������
 * 
 * @author lyq
 * 
 */
public class Client {
	public static void main(String[] args) {
		String dataFilePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		String attachFilePath = "C:\\Users\\lyq\\Desktop\\icon\\attach.txt";
		// ��ѯ�����
		String queryStr;
		// ������
		double result;

		// ��ѯ�����������¼��ǵ������ˣ������������ˣ����½ӵ�Mary�ĵ绰
		queryStr = "E=y,A=y,M=y";
		BayesNetWorkTool tool = new BayesNetWorkTool(dataFilePath,
				attachFilePath);
		result = tool.calProByNetWork(queryStr);

		if (result == -1) {
			System.out.println("���������¼������㱴Ҷ˹����Ľṹ���޷��������");
		} else {
			System.out.println(String.format("�¼�%s����ĸ���Ϊ%s", queryStr, result));
		}
	}
}
