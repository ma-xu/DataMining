package StatisticalLearning.DataMining_EM;

/**
 * EM��������㷨����������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		
		EMTool tool = new EMTool(filePath);
		tool.readDataFile();
		tool.exceptMaxStep();
	}
}
