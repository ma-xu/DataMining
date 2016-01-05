package GraphMining.DataMining_GSpan;

/**
 * �ߵ�Ƶ��ͳ��
 * @author lyq
 *
 */
public class EdgeFrequency {
	//�ڵ�������
	private int nodeLabelNum;
	//�ߵı������
	private int edgeLabelNum;
	//���ڴ�ű߼����3ά����
	public int[][][] edgeFreqCount;
	
	public EdgeFrequency(int nodeLabelNum, int edgeLabelNum){
		this.nodeLabelNum = nodeLabelNum;
		this.edgeLabelNum = edgeLabelNum;
		
		edgeFreqCount = new int[nodeLabelNum][edgeLabelNum][nodeLabelNum];
		//���ʼ������
		for(int i=0; i<nodeLabelNum; i++){
			for(int j=0; j<edgeLabelNum; j++){
				for(int k=0; k<nodeLabelNum; k++){
					edgeFreqCount[i][j][k] = 0;
				}
			}
		}
	}
	
}
