package Others.DataMining_TAN;

/**
 * TAN�������ر�Ҷ˹�㷨
 * 
 * @author lyq
 * 
 */
public class Client {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		// ������ѯ���
		String queryStr;
		// ���������1
		double classResult1;
		// ���������2
		double classResult2;

		TANTool tool = new TANTool(filePath);
		queryStr = "OutLook=Sunny,Temperature=Hot,Humidity=High,Wind=Weak,PlayTennis=No";
		classResult1 = tool.calHappenedPro(queryStr);

		queryStr = "OutLook=Sunny,Temperature=Hot,Humidity=High,Wind=Weak,PlayTennis=Yes";
		classResult2 = tool.calHappenedPro(queryStr);

		System.out.println(String.format("���Ϊ%s����õĸ���Ϊ%s", "PlayTennis=No",
				classResult1));
		System.out.println(String.format("���Ϊ%s����õĸ���Ϊ%s", "PlayTennis=Yes",
				classResult2));
		if (classResult1 > classResult2) {
			System.out.println("�������ΪPlayTennis=No");
		} else {
			System.out.println("�������ΪPlayTennis=Yes");
		}
	}
}
