package Clustering.DataMining_KMeans;

/**
 * K-means��K��ֵ���㷨������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		//�������������趨
		int classNum = 3;
		
		KMeansTool tool = new KMeansTool(filePath, classNum);
		tool.kMeansClustering();
	}
}
