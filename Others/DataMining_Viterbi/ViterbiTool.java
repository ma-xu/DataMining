package Others.DataMining_Viterbi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ά�ر��㷨������
 * 
 * @author maxu
 * 
 */
public class ViterbiTool {
	// ״̬ת�Ƹ��ʾ����ļ���ַ
	private String stmFilePath;
	// ��������ļ���ַ
	private String confusionFilePath;
	// ��ʼ״̬����
	private double[] initStatePro;
	// �۲쵽��״̬����
	public String[] observeStates;
	// ״̬ת�ƾ���ֵ
	private double[][] stMatrix;
	// �������ֵ
	private double[][] confusionMatrix;
	// ���������µ�Ǳ����������ֵ
	private double[][] potentialValues;
	// Ǳ������
	private ArrayList<String> potentialAttrs;
	// ����ֵ�����ӳ��ͼ
	private HashMap<String, Integer> name2Index;
	// ���������ֵӳ��ͼ
	private HashMap<Integer, String> index2name;

	public ViterbiTool(String stmFilePath, String confusionFilePath,
			double[] initStatePro, String[] observeStates) {
		this.stmFilePath = stmFilePath;
		this.confusionFilePath = confusionFilePath;
		this.initStatePro = initStatePro;
		this.observeStates = observeStates;

		initOperation();
	}

	/**
	 * ��ʼ����ݲ���
	 */
	private void initOperation() {
		double[] temp;
		int index;
		ArrayList<String[]> smtDatas;
		ArrayList<String[]> cfDatas;

		smtDatas = readDataFile(stmFilePath);
		cfDatas = readDataFile(confusionFilePath);

		index = 0;
		this.stMatrix = new double[smtDatas.size()][];
		for (String[] array : smtDatas) {
			temp = new double[array.length];
			for (int i = 0; i < array.length; i++) {
				try {
					temp[i] = Double.parseDouble(array[i]);
				} catch (NumberFormatException e) {
					temp[i] = -1;
				}
			}

			// ��ת�����ֵ����������
			this.stMatrix[index] = temp;
			index++;
		}

		index = 0;
		this.confusionMatrix = new double[cfDatas.size()][];
		for (String[] array : cfDatas) {
			temp = new double[array.length];
			for (int i = 0; i < array.length; i++) {
				try {
					temp[i] = Double.parseDouble(array[i]);
				} catch (NumberFormatException e) {
					temp[i] = -1;
				}
			}

			// ��ת�����ֵ����������
			this.confusionMatrix[index] = temp;
			index++;
		}

		this.potentialAttrs = new ArrayList<>();
		// ���Ǳ����������
		for (String s : smtDatas.get(0)) {
			this.potentialAttrs.add(s);
		}
		// ȥ��������Ч��
		potentialAttrs.remove(0);

		this.name2Index = new HashMap<>();
		this.index2name = new HashMap<>();

		// �������±�ӳ���ϵ
		for (int i = 1; i < smtDatas.get(0).length; i++) {
			this.name2Index.put(smtDatas.get(0)[i], i);
			// ����±굽��Ƶ�ӳ��
			this.index2name.put(i, smtDatas.get(0)[i]);
		}

		for (int i = 1; i < cfDatas.get(0).length; i++) {
			this.name2Index.put(cfDatas.get(0)[i], i);
		}
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	private ArrayList<String[]> readDataFile(String filePath) {
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

		return dataArray;
	}

	/**
	 * ��ݹ۲������������ص��������ʾ���
	 */
	private void calPotencialProMatrix() {
		String curObserveState;
		// �۲�������Ǳ���������±�
		int osIndex;
		int psIndex;
		double temp;
		double maxPro;
		// ����������ֵ���������Ӱ������ظ���
		double confusionPro;

		this.potentialValues = new double[observeStates.length][potentialAttrs
				.size() + 1];
		for (int i = 0; i < this.observeStates.length; i++) {
			curObserveState = this.observeStates[i];
			osIndex = this.name2Index.get(curObserveState);
			maxPro = -1;

			// ��Ϊ�ǵ�һ���۲�������û��ǰ���Ӱ�죬��ݳ�ʼ״̬����
			if (i == 0) {
				for (String attr : this.potentialAttrs) {
					psIndex = this.name2Index.get(attr);
					confusionPro = this.confusionMatrix[psIndex][osIndex];

					temp = this.initStatePro[psIndex - 1] * confusionPro;
					this.potentialValues[BaseNames.DAY1][psIndex] = temp;
				}
			} else {
				// �����Ǳ��������ǰһ��������Ӱ�죬�Լ���ǰ�Ļ�������Ӱ��
				for (String toDayAttr : this.potentialAttrs) {
					psIndex = this.name2Index.get(toDayAttr);
					confusionPro = this.confusionMatrix[psIndex][osIndex];

					int index;
					maxPro = -1;
					// ͨ������ĸ��ʼ�������������������
					for (String yAttr : this.potentialAttrs) {
						index = this.name2Index.get(yAttr);
						temp = this.potentialValues[i - 1][index]
								* this.stMatrix[index][psIndex];

						// ����õ������Ǳ��������������
						if (temp > maxPro) {
							maxPro = temp;
						}
					}

					this.potentialValues[i][psIndex] = maxPro * confusionPro;
				}
			}
		}
	}

	/**
	 * ���ͬʱ��������ֵ���Ǳ������ֵ
	 */
	private void outputResultAttr() {
		double maxPro;
		int maxIndex;
		ArrayList<String> psValues;

		psValues = new ArrayList<>();
		for (int i = 0; i < this.potentialValues.length; i++) {
			maxPro = -1;
			maxIndex = 0;

			for (int j = 0; j < potentialValues[i].length; j++) {
				if (this.potentialValues[i][j] > maxPro) {
					maxPro = potentialValues[i][j];
					maxIndex = j;
				}
			}

			// ȡ���������±��Ӧ��Ǳ������
			psValues.add(this.index2name.get(maxIndex));
		}

		System.out.println("�۲�����Ϊ��");
		for (String s : this.observeStates) {
			System.out.print(s + ", ");
		}
		System.out.println();

		System.out.println("Ǳ������Ϊ��");
		for (String s : psValues) {
			System.out.print(s + ", ");
		}
		System.out.println();
	}

	/**
	 * ��ݹ۲����ԣ��õ�Ǳ��������Ϣ
	 */
	public void calHMMObserve() {
		calPotencialProMatrix();
		outputResultAttr();
	}
}
