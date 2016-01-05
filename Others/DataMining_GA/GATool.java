package Others.DataMining_GA;

import java.util.ArrayList;
import java.util.Random;

/**
 * �Ŵ��㷨������
 * 
 * @author lyq
 * 
 */
public class GATool {
	// ������Сֵ
	private int minNum;
	// �������ֵ
	private int maxNum;
	// ���������ı���λ��
	private int codeNum;
	// ��ʼ��Ⱥ������
	private int initSetsNum;
	// ����������
	private Random random;
	// ��ʼȺ��
	private ArrayList<int[]> initSets;

	public GATool(int minNum, int maxNum, int initSetsNum) {
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.initSetsNum = initSetsNum;

		this.random = new Random();
		produceInitSets();
	}

	/**
	 * �����ʼ��Ⱥ��
	 */
	private void produceInitSets() {
		this.codeNum = 0;
		int num = maxNum;
		int[] array;

		initSets = new ArrayList<>();

		// ȷ������λ��
		while (num != 0) {
			codeNum++;
			num /= 2;
		}

		for (int i = 0; i < initSetsNum; i++) {
			array = produceInitCode();
			initSets.add(array);
		}
	}

	/**
	 * �����ʼ����ı���
	 * 
	 * @return
	 */
	private int[] produceInitCode() {
		int num = 0;
		int num2 = 0;
		int[] tempArray;
		int[] array1;
		int[] array2;

		tempArray = new int[2 * codeNum];
		array1 = new int[codeNum];
		array2 = new int[codeNum];

		num = 0;
		while (num < minNum || num > maxNum) {
			num = random.nextInt(maxNum) + 1;
		}
		numToBinaryArray(array1, num);

		while (num2 < minNum || num2 > maxNum) {
			num2 = random.nextInt(maxNum) + 1;
		}
		numToBinaryArray(array2, num2);

		// ����ܵı���
		for (int i = 0, k = 0; i < tempArray.length; i++, k++) {
			if (k < codeNum) {
				tempArray[i] = array1[k];
			} else {
				tempArray[i] = array2[k - codeNum];
			}
		}

		return tempArray;
	}

	/**
	 * ѡ�����������ֵ�ϸߵĸ��������Ŵ�����һ��
	 * 
	 * @param initCodes
	 *            ��ʼ�������
	 * @return
	 */
	private ArrayList<int[]> selectOperate(ArrayList<int[]> initCodes) {
		double randomNum = 0;
		double sumAdaptiveValue = 0;
		ArrayList<int[]> resultCodes = new ArrayList<>();
		double[] adaptiveValue = new double[initSetsNum];

		for (int i = 0; i < initSetsNum; i++) {
			adaptiveValue[i] = calCodeAdaptiveValue(initCodes.get(i));
			sumAdaptiveValue += adaptiveValue[i];
		}

		// ת�ɸ��ʵ���ʽ������һ������
		for (int i = 0; i < initSetsNum; i++) {
			adaptiveValue[i] = adaptiveValue[i] / sumAdaptiveValue;
		}

		for (int i = 0; i < initSetsNum; i++) {
			randomNum = random.nextInt(100) + 1;
			randomNum = randomNum / 100;
			//��Ϊ1.0���޷��жϵ��ģ�,�ܺͻ����޽ӽ�1.0ȡΪ0.99���ж�
			if(randomNum == 1){
				randomNum = randomNum - 0.01;
			}

			sumAdaptiveValue = 0;
			// ȷ�����
			for (int j = 0; j < initSetsNum; j++) {
				if (randomNum > sumAdaptiveValue
						&& randomNum <= sumAdaptiveValue + adaptiveValue[j]) {
					//���ÿ����ķ�ʽ���������ظ�
					resultCodes.add(initCodes.get(j).clone());
					break;
				} else {
					sumAdaptiveValue += adaptiveValue[j];
				}
			}
		}

		return resultCodes;
	}

	/**
	 * ��������
	 * 
	 * @param selectedCodes
	 *            �ϲ����ѡ���ı���
	 * @return
	 */
	private ArrayList<int[]> crossOperate(ArrayList<int[]> selectedCodes) {
		int randomNum = 0;
		// �����
		int crossPoint = 0;
		ArrayList<int[]> resultCodes = new ArrayList<>();
		// ��������У������������
		ArrayList<int[]> randomCodeSeqs = new ArrayList<>();

		// �����������
		while (selectedCodes.size() > 0) {
			randomNum = random.nextInt(selectedCodes.size());

			randomCodeSeqs.add(selectedCodes.get(randomNum));
			selectedCodes.remove(randomNum);
		}

		int temp = 0;
		int[] array1;
		int[] array2;
		// ����������������
		for (int i = 1; i < randomCodeSeqs.size(); i++) {
			if (i % 2 == 1) {
				array1 = randomCodeSeqs.get(i - 1);
				array2 = randomCodeSeqs.get(i);
				crossPoint = random.nextInt(2 * codeNum - 1) + 1;

				// ���н����λ�ú�ı������
				for (int j = 0; j < 2 * codeNum; j++) {
					if (j >= crossPoint) {
						temp = array1[j];
						array1[j] = array2[j];
						array2[j] = temp;
					}
				}

				// ���뵽������������
				resultCodes.add(array1);
				resultCodes.add(array2);
			}
		}

		return resultCodes;
	}

	/**
	 * �������
	 * 
	 * @param crossCodes
	 *            ���������Ľ��
	 * @return
	 */
	private ArrayList<int[]> variationOperate(ArrayList<int[]> crossCodes) {
		// �����
		int variationPoint = 0;
		ArrayList<int[]> resultCodes = new ArrayList<>();

		for (int[] array : crossCodes) {
			variationPoint = random.nextInt(codeNum * 2);

			for (int i = 0; i < array.length; i++) {
				// �������б���
				if (i == variationPoint) {
					array[i] = (array[i] == 0 ? 1 : 0);
					break;
				}
			}

			resultCodes.add(array);
		}

		return resultCodes;
	}

	/**
	 * ����תΪ��������ʽ
	 * 
	 * @param binaryArray
	 *            ת����Ķ�����������ʽ
	 * @param num
	 *            ��ת������
	 */
	private void numToBinaryArray(int[] binaryArray, int num) {
		int index = 0;
		int temp = 0;
		while (num != 0) {
			binaryArray[index] = num % 2;
			index++;
			num /= 2;
		}
		
		//��������ǰ��β���ĵ���
		for(int i=0; i<binaryArray.length/2; i++){
			temp = binaryArray[i];
			binaryArray[i] = binaryArray[binaryArray.length - 1 - i];
			binaryArray[binaryArray.length - 1 - i] = temp;
		}
	}

	/**
	 * ����������ת��Ϊ����
	 * 
	 * @param binaryArray
	 *            ��ת������������
	 */
	private int binaryArrayToNum(int[] binaryArray) {
		int result = 0;

		for (int i = binaryArray.length-1, k=0; i >=0 ; i--, k++) {
			if (binaryArray[i] == 1) {
				result += Math.pow(2, k);
			}
		}

		return result;
	}

	/**
	 * �������������ֵ
	 * 
	 * @param codeArray
	 */
	private int calCodeAdaptiveValue(int[] codeArray) {
		int result = 0;
		int x1 = 0;
		int x2 = 0;
		int[] array1 = new int[codeNum];
		int[] array2 = new int[codeNum];

		for (int i = 0, k = 0; i < codeArray.length; i++, k++) {
			if (k < codeNum) {
				array1[k] = codeArray[i];
			} else {
				array2[k - codeNum] = codeArray[i];
			}
		}

		// ������ֵ�ĵ���
		x1 = binaryArrayToNum(array1);
		x2 = binaryArrayToNum(array2);
		result = x1 * x1 + x2 * x2;

		return result;
	}

	/**
	 * �����Ŵ��㷨����
	 */
	public void geneticCal() {
		// �����ֵ
		int maxFitness;
		//����Ŵ�����
		int loopCount = 0;
		boolean canExit = false;
		ArrayList<int[]> initCodes;
		ArrayList<int[]> selectedCodes;
		ArrayList<int[]> crossedCodes;
		ArrayList<int[]> variationCodes;
		
		int[] maxCode = new int[2*codeNum];
		//���������ֵ
		for(int i=0; i<2*codeNum; i++){
			maxCode[i] = 1;
		}
		maxFitness = calCodeAdaptiveValue(maxCode);

		initCodes = initSets;
		while (true) {
			for (int[] array : initCodes) {
				// �Ŵ�������ֹ����Ϊ���ڱ���ﵽ�����ֵ
				if (maxFitness == calCodeAdaptiveValue(array)) {
					canExit = true;
					break;
				}
			}

			if (canExit) {
				break;
			}

			selectedCodes = selectOperate(initCodes);
			crossedCodes = crossOperate(selectedCodes);
			variationCodes = variationOperate(crossedCodes);
			initCodes = variationCodes;
			
			loopCount++;
		}

		System.out.println("�ܹ��Ŵ�����" + loopCount +"��" );
		printFinalCodes(initCodes);
	}

	/**
	 * ������ı��뼯
	 * 
	 * @param finalCodes
	 *            ���Ľ�����
	 */
	private void printFinalCodes(ArrayList<int[]> finalCodes) {
		int j = 0;

		for (int[] array : finalCodes) {
			System.out.print("����" + (j + 1) + ":");
			for (int i = 0; i < array.length; i++) {
				System.out.print(array[i]);
			}
			System.out.println();
			j++;
		}
	}

}
