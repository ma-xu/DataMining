package Others.DataMining_KDTree;

import java.text.MessageFormat;

/**
 * KD���㷨������
 * 
 * @author maxu
 * 
 */
public class Client {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		Point queryNode;
		Point searchedNode;
		KDTreeTool tool = new KDTreeTool(filePath);

		// ����KD���Ĺ���
		tool.createKDTree();

		// ͨ��KD��������ݵ�������ѯ
		queryNode = new Point(2.1, 3.1);
		searchedNode = tool.searchNearestData(queryNode);
		System.out.println(MessageFormat.format(
				"�����ѯ��({0}, {1})��������Ϊ({2}, {3})", queryNode.x, queryNode.y,
				searchedNode.x, searchedNode.y));
		
		//���¹���KD��,ȥ��֮ǰ�ķ��ʼ�¼
		tool.createKDTree();
		queryNode = new Point(2, 4.5);
		searchedNode = tool.searchNearestData(queryNode);
		System.out.println(MessageFormat.format(
				"�����ѯ��({0}, {1})��������Ϊ({2}, {3})", queryNode.x, queryNode.y,
				searchedNode.x, searchedNode.y));
	}
}
