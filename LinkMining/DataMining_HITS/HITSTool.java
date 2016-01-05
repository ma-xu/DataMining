package LinkMining.DataMining_HITS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HITS���ӷ����㷨������
 * @author maxu
 *
 */
public class HITSTool {
	//��������ļ���ַ
	private String filePath;
	//��ҳ����
	private int pageNum;
	//��ҳAuthorityȨ��ֵ
	private double[] authority;
	//��ҳhub����ֵ
	private double[] hub;
	//���Ӿ����ϵ
	private int[][] linkMatrix;
	//��ҳ����
	private ArrayList<String> pageClass;
	
	public HITSTool(String filePath){
		this.filePath = filePath;
		readDataFile();
	}
	
	/**
	 * ���ļ��ж�ȡ���
	 */
	private void readDataFile() {
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		pageClass = new ArrayList<>();
		// ͳ����ҳ��������
		for (String[] array : dataArray) {
			for (String s : array) {
				if (!pageClass.contains(s)) {
					pageClass.add(s);
				}
			}
		}

		int i = 0;
		int j = 0;
		pageNum = pageClass.size();
		linkMatrix = new int[pageNum][pageNum];
		authority = new double[pageNum];
		hub = new double[pageNum];
		for(int k=0; k<pageNum; k++){
			//��ʼʱĬ��Ȩ��ֵ������ֵ��Ϊ1
			authority[k] = 1;
			hub[k] = 1;
		}
		
		for (String[] array : dataArray) {

			i = Integer.parseInt(array[0]);
			j = Integer.parseInt(array[1]);

			// ����linkMatrix[i][j]Ϊ1���i��ҳ��ָ��j��ҳ������
			linkMatrix[i - 1][j - 1] = 1;
		}
	}
	
	/**
	 * ������ҳ�棬Ҳ����authorityȨ��ֵ��ߵ�ҳ��
	 */
	public void printResultPage(){
		//���Hub��Authorityֵ�����ں���Ĺ�һ������
		double maxHub = 0;
		double maxAuthority = 0;
		int maxAuthorityIndex =0;
		//���ֵ�����������ж�
		double error = Integer.MAX_VALUE;
		double[] newHub = new double[pageNum];
		double[] newAuthority = new double[pageNum];
		
		
		while(error > 0.01 * pageNum){
			for(int k=0; k<pageNum; k++){
				newHub[k] = 0;
				newAuthority[k] = 0;
			}
			
			//hub��authorityֵ�ĸ��¼���
			for(int i=0; i<pageNum; i++){
				for(int j=0; j<pageNum; j++){
					if(linkMatrix[i][j] == 1){
						newHub[i] += authority[j];
						newAuthority[j] += hub[i];
					}
				}
			}
			
			maxHub = 0;
			maxAuthority = 0;
			for(int k=0; k<pageNum; k++){
				if(newHub[k] > maxHub){
					maxHub = newHub[k];
				}
				
				if(newAuthority[k] > maxAuthority){
					maxAuthority = newAuthority[k];
					maxAuthorityIndex = k;
				}
			}
			
			error = 0;
			//��һ������
			for(int k=0; k<pageNum; k++){
				newHub[k] /= maxHub;
				newAuthority[k] /= maxAuthority;
				
				error += Math.abs(newHub[k] - hub[k]);
				System.out.println(newAuthority[k] + ":" + newHub[k]);
				
				hub[k] = newHub[k];
				authority[k] = newAuthority[k];
			}
			System.out.println("---------");
		}
		
		System.out.println("****������������ҳ��Ȩ��ֵ������ֵ****");
		for(int k=0; k<pageNum; k++){
			System.out.println("��ҳ" + pageClass.get(k) + ":"+ authority[k] + ":" + hub[k]);
		}
		System.out.println("Ȩ��ֵ��ߵ���ҳΪ����ҳ" + pageClass.get(maxAuthorityIndex));
	}

}
