package AssociationAnalysis.DataMining_Apriori;

/**
 * apriori关联规则挖掘算法调用类
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\testInput.txt";
		
		AprioriTool tool = new AprioriTool(filePath, 2);
		tool.printAttachRule(0.7);
	}
}
