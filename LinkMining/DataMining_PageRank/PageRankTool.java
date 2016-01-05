package LinkMining.DataMining_PageRank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * PageRank��ҳ�����㷨������
 * 
 * @author maxu
 * 
 */
public class PageRankTool {
	// �����������
	private String filePath;
	// ��ҳ������
	private int pageNum;
	// ���ӹ�ϵ����
	private double[][] linkMatrix;
	// ÿ��ҳ��pageRankֵ��ʼ����
	private double[] pageRankVecor;

	// ��ҳ��������
	ArrayList<String> pageClass;

	public PageRankTool(String filePath) {
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
		linkMatrix = new double[pageNum][pageNum];
		pageRankVecor = new double[pageNum];
		for (int k = 0; k < pageNum; k++) {
			// ��ʼÿ��ҳ���pageRankֵΪ1
			pageRankVecor[k] = 1.0;
		}
		for (String[] array : dataArray) {

			i = Integer.parseInt(array[0]);
			j = Integer.parseInt(array[1]);

			// ����linkMatrix[i][j]Ϊ1���i��ҳ��ָ��j��ҳ������
			linkMatrix[i - 1][j - 1] = 1;
		}
	}

	/**
	 * ������ת��
	 */
	private void transferMatrix() {
		int count = 0;
		for (double[] array : linkMatrix) {
			// ����ҳ�����Ӹ���
			count = 0;
			for (double d : array) {
				if (d == 1) {
					count++;
				}
			}
			// �����ʾ��
			for (int i = 0; i < array.length; i++) {
				if (array[i] == 1) {
					array[i] /= count;
				}
			}
		}

		double t = 0;
		// ������ת�û�����Ϊ����ת�ƾ���
		for (int i = 0; i < linkMatrix.length; i++) {
			for (int j = i + 1; j < linkMatrix[0].length; j++) {
				t = linkMatrix[i][j];
				linkMatrix[i][j] = linkMatrix[j][i];
				linkMatrix[j][i] = t;
			}
		}
	}

	/**
	 * �����ݷ�����pageRankֵ
	 */
	public void printPageRankValue() {
		transferMatrix();
		// ����ϵ��
		double damp = 0.5;
		// ���Ӹ��ʾ���
		double[][] A = new double[pageNum][pageNum];
		double[][] e = new double[pageNum][pageNum];

		// ���ù�ʽA=d*q+(1-d)*e/m��mΪ��ҳ�ܸ���,d����damp
		double temp = (1 - damp) / pageNum;
		for (int i = 0; i < e.length; i++) {
			for (int j = 0; j < e[0].length; j++) {
				e[i][j] = temp;
			}
		}

		for (int i = 0; i < pageNum; i++) {
			for (int j = 0; j < pageNum; j++) {
				temp = damp * linkMatrix[i][j] + e[i][j];
				A[i][j] = temp;

			}
		}

		// ���ֵ����Ϊ�ж�������׼
		double errorValue = Integer.MAX_VALUE;
		double[] newPRVector = new double[pageNum];
		// ��ƽ��ÿ��PRֵ���С��0.001ʱ����ﵽ����
		while (errorValue > 0.001 * pageNum) {
			System.out.println("**********");
			for (int i = 0; i < pageNum; i++) {
				temp = 0;
				// ��A*pageRankVector,�����ݷ����,ֱ��pageRankVectorֵ����
				for (int j = 0; j < pageNum; j++) {
					// temp����ÿ����ҳ��iҳ���pageRankֵ
					temp += A[i][j] * pageRankVecor[j];
				}

				// ����temp����i��ҳ����PageRankֵ
				newPRVector[i] = temp;
				System.out.println(temp);
			}

			errorValue = 0;
			for (int i = 0; i < pageNum; i++) {
				errorValue += Math.abs(pageRankVecor[i] - newPRVector[i]);
				// �µ���������ɵ�����
				pageRankVecor[i] = newPRVector[i];
			}
		}

		String name = null;
		temp = 0;
		System.out.println("--------------------");
		for (int i = 0; i < pageNum; i++) {
			System.out.println(MessageFormat.format("��ҳ{0}��pageRankֵ��{1}",
					pageClass.get(i), pageRankVecor[i]));
			if (pageRankVecor[i] > temp) {
				temp = pageRankVecor[i];
				name = pageClass.get(i);
			}
		}
		System.out.println(MessageFormat.format("�ȼ���ߵ���ҳΪ��{0}", name));
	}

}
