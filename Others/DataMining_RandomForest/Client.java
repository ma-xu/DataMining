package Others.DataMining_RandomForest;

import java.text.MessageFormat;

/**
 * ���ɭ���㷨���Գ���
 * 
 * @author maxu
 * 
 */
public class Client {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		String queryStr = "Age=Youth,Income=Low,Student=No,CreditRating=Fair";
		String resultClassType = "";
		// ����������ռ�����ռ����
		double sampleNumRatio = 0.4;
		// ����ݵĲɼ���������ռ�������ı���
		double featureNumRatio = 0.5;

		RandomForestTool tool = new RandomForestTool(filePath, sampleNumRatio,
				featureNumRatio);
		tool.constructRandomTree();

		resultClassType = tool.judgeClassType(queryStr);

		System.out.println();
		System.out
				.println(MessageFormat.format(
						"��ѯ��������{0},Ԥ��ķ�����ΪBuysCompute:{1}", queryStr,
						resultClassType));
	}
}
