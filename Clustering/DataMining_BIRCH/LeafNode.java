package Clustering.DataMining_BIRCH;

import java.util.ArrayList;

/**
 * CF��Ҷ�ӽڵ�
 * 
 * @author maxu
 * 
 */
public class LeafNode extends ClusteringFeature {
	// ���Ӽ�Ⱥ
	private ArrayList<Cluster> clusterChilds;
	// ���׽ڵ�
	private NonLeafNode parentNode;

	public ArrayList<Cluster> getClusterChilds() {
		return clusterChilds;
	}

	public void setClusterChilds(ArrayList<Cluster> clusterChilds) {
		this.clusterChilds = clusterChilds;
	}

	/**
	 * ��Ҷ�ӽڵ㻮�ֳ�2��
	 * 
	 * @return
	 */
	public LeafNode[] divideLeafNode() {
		LeafNode[] leafNodeArray = new LeafNode[2];
		// �ؼ����������2���أ�����Ĵذ��վͽ�ԭ�򻮷ּ���
		Cluster cluster1 = null;
		Cluster cluster2 = null;
		Cluster tempCluster = null;
		double maxValue = 0;
		double temp = 0;

		// �ҳ����ľ���������2����
		for (int i = 0; i < clusterChilds.size() - 1; i++) {
			tempCluster = clusterChilds.get(i);
			for (int j = i + 1; j < clusterChilds.size(); j++) {
				temp = tempCluster
						.computerClusterDistance(clusterChilds.get(j));

				if (temp > maxValue) {
					maxValue = temp;
					cluster1 = tempCluster;
					cluster2 = clusterChilds.get(j);
				}
			}
		}

		leafNodeArray[0] = new LeafNode();
		leafNodeArray[0].addingCluster(cluster1);
		cluster1.setParentNode(leafNodeArray[0]);
		leafNodeArray[1] = new LeafNode();
		leafNodeArray[1].addingCluster(cluster2);
		cluster2.setParentNode(leafNodeArray[1]);
		clusterChilds.remove(cluster1);
		clusterChilds.remove(cluster2);
		// �ͽ�����
		for (Cluster c : clusterChilds) {
			if (cluster1.computerClusterDistance(c) < cluster2
					.computerClusterDistance(c)) {
				// �ؼ�������ӽ���С�أ��ͼ�����С������Ҷ�ӽڵ�
				leafNodeArray[0].addingCluster(c);
				c.setParentNode(leafNodeArray[0]);
			} else {
				leafNodeArray[1].addingCluster(c);
				c.setParentNode(leafNodeArray[1]);
			}
		}

		return leafNodeArray;
	}

	public NonLeafNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(NonLeafNode parentNode) {
		this.parentNode = parentNode;
	}

	@Override
	public void addingCluster(ClusteringFeature clusteringFeature) {
		//���¾�������ֵ
		directAddCluster(clusteringFeature);
		
		// Ѱ�ҵ���Ŀ�꼯Ⱥ
		Cluster findedCluster = null;
		Cluster cluster = (Cluster) clusteringFeature;
		// ���ڶ���ƽ�����
		double disance = Integer.MAX_VALUE;
		// �ؼ�����ֵ
		double errorDistance = 0;
		boolean needDivided = false;
		if (clusterChilds == null) {
			clusterChilds = new ArrayList<>();
			clusterChilds.add(cluster);
			cluster.setParentNode(this);
		} else {
			for (Cluster c : clusterChilds) {
				errorDistance = c.computerClusterDistance(cluster);
				if (disance > errorDistance) {
					// ѡ���ؼ��������
					disance = errorDistance;
					findedCluster = c;
				}
			}

			ArrayList<double[]> data1 = (ArrayList<double[]>) findedCluster
					.getData().clone();
			ArrayList<double[]> data2 = cluster.getData();
			data1.addAll(data2);
			// �����Ӻ�ľ���Ĵؼ���볬�����ֵ����Ҫ�����½���
			if (findedCluster.computerInClusterDistance(data1) > BIRCHTool.T) {
				// Ҷ�ӽڵ�ĺ������ܳ���ƽ������L
				if (clusterChilds.size() + 1 > BIRCHTool.L) {
					needDivided = true;
				}
				clusterChilds.add(cluster);
				cluster.setParentNode(this);
			} else {
				findedCluster.directAddCluster(cluster);
				cluster.setParentNode(this);
			}
		}
		
		if(needDivided){
			if(parentNode == null){
				parentNode = new NonLeafNode();
			}else{
				parentNode.getLeafChilds().remove(this);
			}
			
			LeafNode[] nodeArray = divideLeafNode();
			for(LeafNode n: nodeArray){
				parentNode.addingCluster(n);
			}
		}
	}

	@Override
	protected void directAddCluster(ClusteringFeature node) {
		// TODO Auto-generated method stub
		if(parentNode != null){
			parentNode.directAddCluster(node);
		}
		
		super.directAddCluster(node);
	}
	
}
