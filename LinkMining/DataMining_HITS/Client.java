package LinkMining.DataMining_HITS;

/**
 * HITS���ӷ����㷨
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		
		HITSTool tool = new HITSTool(filePath);
		tool.printResultPage();
	}
}
