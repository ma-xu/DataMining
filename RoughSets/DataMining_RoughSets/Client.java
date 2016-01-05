package RoughSets.DataMining_RoughSets;

/**
 * �ֲڼ�Լ���㷨
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		
		RoughSetsTool tool = new RoughSetsTool(filePath);
		tool.findingReduct();
	}
}
