package Classification.DataMining_ID3;

/**
 * ID3�����������㷨���Գ�����
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		
		ID3Tool tool = new ID3Tool(filePath);
		tool.startBuildingTree(true);
	}
}
