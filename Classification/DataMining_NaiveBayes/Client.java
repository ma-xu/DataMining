package Classification.DataMining_NaiveBayes;


/**
 * ���ر�Ҷ˹�㷨����������
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		//ѵ�������
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		String testData = "Youth Medium Yes Fair";
		NaiveBayesTool tool = new NaiveBayesTool(filePath);
		System.out.println(testData + " ��ݵķ���Ϊ:" + tool.naiveBayesClassificate(testData));
	}
}
