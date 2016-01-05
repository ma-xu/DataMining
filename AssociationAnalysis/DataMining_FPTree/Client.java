package AssociationAnalysis.DataMining_FPTree;

/**
 * FPTreeƵ��ģʽ���㷨
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\testInput.txt";
		//��С֧�ֶ���ֵ
		int minSupportCount = 2;
		
		FPTreeTool tool = new FPTreeTool(filePath, minSupportCount);
		tool.startBuildingTree();
	}
}
