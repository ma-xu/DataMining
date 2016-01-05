package Others.DataMining_GA_Maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * �Ŵ��㷨�����Թ���Ϸ��Ӧ��-�Ŵ��㷨������
 * 
 * @author maxu
 * 
 */
public class GATool {
	// �Թ�����ڱ��
	public static final int MAZE_ENTRANCE_POS = 1;
	public static final int MAZE_EXIT_POS = 2;
	// �����Ӧ�ı�������
	public static final int[][] MAZE_DIRECTION_CODE = new int[][] { { 0, 0 },
			{ 0, 1 }, { 1, 0 }, { 1, 1 }, };
	// ���㷽��ı�
	public static final int[][] MAZE_DIRECTION_CHANGE = new int[][] {
			{ -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, };
	// �������������
	public static final String[] MAZE_DIRECTION_LABEL = new String[] { "��",
			"��", "��", "��" };

	// ��ͼ����ļ���ַ
	private String filePath;
	// ���Թ�����̲���
	private int stepNum;
	// ��ʼ���������
	private int initSetsNum;
	// �Թ����λ��
	private int[] startPos;
	// �Թ�����λ��
	private int[] endPos;
	// �Թ���ͼ���
	private int[][] mazeData;
	// ��ʼ���弯
	private ArrayList<int[]> initSets;
	// ����������
	private Random random;

	public GATool(String filePath, int initSetsNum) {
		this.filePath = filePath;
		this.initSetsNum = initSetsNum;

		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ���
	 */
	public void readDataFile() {
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

		int rowNum = dataArray.size();
		mazeData = new int[rowNum][rowNum];
		for (int i = 0; i < rowNum; i++) {
			String[] data = dataArray.get(i);
			for (int j = 0; j < data.length; j++) {
				mazeData[i][j] = Integer.parseInt(data[j]);

				// ��ֵ��ںͳ���λ��
				if (mazeData[i][j] == MAZE_ENTRANCE_POS) {
					startPos = new int[2];
					startPos[0] = i;
					startPos[1] = j;
				} else if (mazeData[i][j] == MAZE_EXIT_POS) {
					endPos = new int[2];
					endPos[0] = i;
					endPos[1] = j;
				}
			}
		}

		// �����߳��Թ�����̲���
		stepNum = Math.abs(startPos[0] - endPos[0])
				+ Math.abs(startPos[1] - endPos[1]);
	}

	/**
	 * �����ʼ��ݼ�
	 */
	private void produceInitSet() {
		// �������
		int directionCode = 0;
		random = new Random();
		initSets = new ArrayList<>();
		// ÿ������Ĳ�����Ҫ��2λ���ֱ�ʾ
		int[] codeNum;

		for (int i = 0; i < initSetsNum; i++) {
			codeNum = new int[stepNum * 2];
			for (int j = 0; j < stepNum; j++) {
				directionCode = random.nextInt(4);
				codeNum[2 * j] = MAZE_DIRECTION_CODE[directionCode][0];
				codeNum[2 * j + 1] = MAZE_DIRECTION_CODE[directionCode][1];
			}

			initSets.add(codeNum);
		}
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
		double sumFitness = 0;
		ArrayList<int[]> resultCodes = new ArrayList<>();
		double[] adaptiveValue = new double[initSetsNum];

		for (int i = 0; i < initSetsNum; i++) {
			adaptiveValue[i] = calFitness(initCodes.get(i));
			sumFitness += adaptiveValue[i];
		}

		// ת�ɸ��ʵ���ʽ������һ������
		for (int i = 0; i < initSetsNum; i++) {
			adaptiveValue[i] = adaptiveValue[i] / sumFitness;
		}

		for (int i = 0; i < initSetsNum; i++) {
			randomNum = random.nextInt(100) + 1;
			randomNum = randomNum / 100;
			//��Ϊ1.0���޷��жϵ��ģ�,�ܺͻ����޽ӽ�1.0ȡΪ0.99���ж�
			if(randomNum == 1){
				randomNum = randomNum - 0.01;
			}
			
			sumFitness = 0;
			// ȷ�����
			for (int j = 0; j < initSetsNum; j++) {
				if (randomNum > sumFitness
						&& randomNum <= sumFitness + adaptiveValue[j]) {
					// ���ÿ����ķ�ʽ���������ظ�
					resultCodes.add(initCodes.get(j).clone());
					break;
				} else {
					sumFitness += adaptiveValue[j];
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
				crossPoint = random.nextInt(stepNum - 1) + 1;

				// ���н����λ�ú�ı������
				for (int j = 0; j < 2 * stepNum; j++) {
					if (j >= 2 * crossPoint) {
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
			variationPoint = random.nextInt(stepNum);

			for (int i = 0; i < array.length; i += 2) {
				// �������б���
				if (i % 2 == 0 && i / 2 == variationPoint) {
					array[i] = (array[i] == 0 ? 1 : 0);
					array[i + 1] = (array[i + 1] == 0 ? 1 : 0);
					break;
				}
			}

			resultCodes.add(array);
		}

		return resultCodes;
	}

	/**
	 * ��ݱ��������ֵ
	 * 
	 * @param code
	 *            ��ǰ�ı���
	 * @return
	 */
	public double calFitness(int[] code) {
		double fintness = 0;
		// �ɱ��������õ��յ�����
		int endX = 0;
		// �ɱ��������õ��յ������
		int endY = 0;
		// ����Ƭ����������߷���
		int direction = 0;
		// ��ʱ��������
		int tempX = 0;
		// ��ʱ���������
		int tempY = 0;

		endX = startPos[0];
		endY = startPos[1];
		for (int i = 0; i < stepNum; i++) {
			direction = binaryArrayToNum(new int[] { code[2 * i],
					code[2 * i + 1] });

			// ��ݷ���ı�����������ĸı�
			tempX = endX + MAZE_DIRECTION_CHANGE[direction][0];
			tempY = endY + MAZE_DIRECTION_CHANGE[direction][1];

			// �ж������Ƿ�Խ��
			if (tempX >= 0 && tempX < mazeData.length && tempY >= 0
					&& tempY < mazeData[0].length) {
				// �ж������Ƿ��ߵ��谭��
				if (mazeData[tempX][tempY] != -1) {
					endX = tempX;
					endY = tempY;
				}
			}
		}

		// �����ֵ���������ֵ�ļ���
		fintness = 1.0 / (Math.abs(endX - endPos[0])
				+ Math.abs(endY - endPos[1]) + 1);

		return fintness;
	}

	/**
	 * ��ݵ�ǰ�����ж��Ƿ��Ѿ��ҵ�����λ��
	 * 
	 * @param code
	 *            �������ɴ��Ŵ��ı���
	 * @return
	 */
	private boolean ifArriveEndPos(int[] code) {
		boolean isArrived = false;
		// �ɱ��������õ��յ�����
		int endX = 0;
		// �ɱ��������õ��յ������
		int endY = 0;
		// ����Ƭ����������߷���
		int direction = 0;
		// ��ʱ��������
		int tempX = 0;
		// ��ʱ���������
		int tempY = 0;

		endX = startPos[0];
		endY = startPos[1];
		for (int i = 0; i < stepNum; i++) {
			direction = binaryArrayToNum(new int[] { code[2 * i],
					code[2 * i + 1] });

			// ��ݷ���ı�����������ĸı�
			tempX = endX + MAZE_DIRECTION_CHANGE[direction][0];
			tempY = endY + MAZE_DIRECTION_CHANGE[direction][1];

			// �ж������Ƿ�Խ��
			if (tempX >= 0 && tempX < mazeData.length && tempY >= 0
					&& tempY < mazeData[0].length) {
				// �ж������Ƿ��ߵ��谭��
				if (mazeData[tempX][tempY] != -1) {
					endX = tempX;
					endY = tempY;
				}
			}
		}

		if (endX == endPos[0] && endY == endPos[1]) {
			isArrived = true;
		}

		return isArrived;
	}

	/**
	 * ����������ת��Ϊ����
	 * 
	 * @param binaryArray
	 *            ��ת������������
	 */
	private int binaryArrayToNum(int[] binaryArray) {
		int result = 0;

		for (int i = binaryArray.length - 1, k = 0; i >= 0; i--, k++) {
			if (binaryArray[i] == 1) {
				result += Math.pow(2, k);
			}
		}

		return result;
	}

	/**
	 * �����Ŵ��㷨�߳��Թ�
	 */
	public void goOutMaze() {
		// ����Ŵ�����
		int loopCount = 0;
		boolean canExit = false;
		// ���·��
		int[] resultCode = null;
		ArrayList<int[]> initCodes;
		ArrayList<int[]> selectedCodes;
		ArrayList<int[]> crossedCodes;
		ArrayList<int[]> variationCodes;

		// �����ʼ��ݼ�
		produceInitSet();
		initCodes = initSets;

		while (true) {
			for (int[] array : initCodes) {
				// �Ŵ�������ֹ����Ϊ�Ƿ��ҵ�����λ��
				if (ifArriveEndPos(array)) {
					resultCode = array;
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
			
			//����Ŵ������100�Σ����˳�
			if(loopCount >= 100){
				break;
			}
		}

		System.out.println("�ܹ��Ŵ�����" + loopCount + "��");
		printFindedRoute(resultCode);
	}

	/**
	 * ����ҵ���·��
	 * 
	 * @param code
	 */
	private void printFindedRoute(int[] code) {
		if(code == null){
			System.out.println("�����޵��Ŵ�������ڣ�û���ҵ�����·��");
			return;
		}
		
		int tempX = startPos[0];
		int tempY = startPos[1];
		int direction = 0;

		System.out.println(MessageFormat.format(
				"��ʼ��λ��({0},{1}), ���ڵ�λ��({2}, {3})", tempX, tempY, endPos[0],
				endPos[1]));
		
		System.out.print("�������Ľ����룺");
		for(int value: code){
			System.out.print("" + value);
		}
		System.out.println();
		
		for (int i = 0, k = 1; i < code.length; i += 2, k++) {
			direction = binaryArrayToNum(new int[] { code[i], code[i + 1] });

			tempX += MAZE_DIRECTION_CHANGE[direction][0];
			tempY += MAZE_DIRECTION_CHANGE[direction][1];

			System.out.println(MessageFormat.format(
					"��{0}��,����Ϊ{1}{2},��{3}�ƶ����ƶ��󵽴�({4},{5})", k, code[i], code[i+1],
					MAZE_DIRECTION_LABEL[direction],  tempX, tempY));
		}
	}

}
