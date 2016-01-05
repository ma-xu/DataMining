package SequentialPatterns.DataMining_GSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * GSP����ģʽ�����㷨
 * 
 * @author lyq
 * 
 */
public class GSPTool {
	// ��������ļ���ַ
	private String filePath;
	// ��С֧�ֶ���ֵ
	private int minSupportCount;
	// ʱ����С���
	private int min_gap;
	// ʱ�������
	private int max_gap;
	// ԭʼ�������
	private ArrayList<Sequence> totalSequences;
	// GSP�㷨�в�������е�Ƶ�������
	private ArrayList<Sequence> totalFrequencySeqs;
	// ���������ֶ�ʱ���ӳ��ͼ����
	private ArrayList<ArrayList<HashMap<Integer, Integer>>> itemNum2Time;

	public GSPTool(String filePath, int minSupportCount, int min_gap,
			int max_gap) {
		this.filePath = filePath;
		this.minSupportCount = minSupportCount;
		this.min_gap = min_gap;
		this.max_gap = max_gap;
		totalFrequencySeqs = new ArrayList<>();
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

		HashMap<Integer, Sequence> mapSeq = new HashMap<>();
		Sequence seq;
		ItemSet itemSet;
		int tID;
		String[] itemStr;
		for (String[] str : dataArray) {
			tID = Integer.parseInt(str[0]);
			itemStr = new String[Integer.parseInt(str[1])];
			System.arraycopy(str, 2, itemStr, 0, itemStr.length);
			itemSet = new ItemSet(itemStr);

			if (mapSeq.containsKey(tID)) {
				seq = mapSeq.get(tID);
			} else {
				seq = new Sequence(tID);
			}
			seq.getItemSetList().add(itemSet);
			mapSeq.put(tID, seq);
		}

		// ������ͼ���뵽����List��
		totalSequences = new ArrayList<>();
		for (Map.Entry entry : mapSeq.entrySet()) {
			totalSequences.add((Sequence) entry.getValue());
		}
	}

	/**
	 * ���1Ƶ���
	 * 
	 * @return
	 */
	private ArrayList<Sequence> generateOneFrequencyItem() {
		int count = 0;
		int currentTransanctionID = 0;
		Sequence tempSeq;
		ItemSet tempItemSet;
		HashMap<Integer, Integer> itemNumMap = new HashMap<>();
		ArrayList<Sequence> seqList = new ArrayList<>();

		for (Sequence seq : totalSequences) {
			for (ItemSet itemSet : seq.getItemSetList()) {
				for (int num : itemSet.getItems()) {
					// ���û�д���������������Ӳ���
					if (!itemNumMap.containsKey(num)) {
						itemNumMap.put(num, 1);
					}
				}
			}
		}
		
		boolean isContain = false;
		int number = 0;
		for (Map.Entry entry : itemNumMap.entrySet()) {
			count = 0;
			number = (int) entry.getKey();
			for (Sequence seq : totalSequences) {
				isContain = false;
				
				for (ItemSet itemSet : seq.getItemSetList()) {
					for (int num : itemSet.getItems()) {
						// ���û�д���������������Ӳ���
						if (num == number) {
							isContain = true;
							break;
						}
					}
					
					if(isContain){
						break;
					}
				}
				
				if(isContain){
					count++;
				}
			}
			
			itemNumMap.put(number, count);
		}
		

		for (Map.Entry entry : itemNumMap.entrySet()) {
			count = (int) entry.getValue();
			if (count >= minSupportCount) {
				tempSeq = new Sequence();
				tempItemSet = new ItemSet(new int[] { (int) entry.getKey() });

				tempSeq.getItemSetList().add(tempItemSet);
				seqList.add(tempSeq);
			}

		}
		// ��������������
		Collections.sort(seqList);
		// ��Ƶ��1�������Ƶ����б���
		totalFrequencySeqs.addAll(seqList);

		return seqList;
	}

	/**
	 * ͨ��1Ƶ������Ӳ���2Ƶ���
	 * 
	 * @param oneSeq
	 *            1Ƶ�������
	 * @return
	 */
	private ArrayList<Sequence> generateTwoFrequencyItem(
			ArrayList<Sequence> oneSeq) {
		Sequence tempSeq;
		ArrayList<Sequence> resultSeq = new ArrayList<>();
		ItemSet tempItemSet;
		int num1;
		int num2;

		// ���罫<a>,<b>2��1Ƶ�����������ϣ����Է�Ϊ<a a>��<a b>��<b a>,<b b>4������ģʽ
		// ע���ʱ��ÿ�������а�2�������
		for (int i = 0; i < oneSeq.size(); i++) {
			num1 = oneSeq.get(i).getFirstItemSetNum();
			for (int j = 0; j < oneSeq.size(); j++) {
				num2 = oneSeq.get(j).getFirstItemSetNum();

				tempSeq = new Sequence();
				tempItemSet = new ItemSet(new int[] { num1 });
				tempSeq.getItemSetList().add(tempItemSet);
				tempItemSet = new ItemSet(new int[] { num2 });
				tempSeq.getItemSetList().add(tempItemSet);

				if (countSupport(tempSeq) >= minSupportCount) {
					resultSeq.add(tempSeq);
				}
			}
		}

		// �������ӻ���1�������ÿ��������ֻ����һ������������ʱa,b�Ļ�������<(a,a)> <(a,b)> <(b,b)>
		for (int i = 0; i < oneSeq.size(); i++) {
			num1 = oneSeq.get(i).getFirstItemSetNum();
			for (int j = i; j < oneSeq.size(); j++) {
				num2 = oneSeq.get(j).getFirstItemSetNum();

				tempSeq = new Sequence();
				tempItemSet = new ItemSet(new int[] { num1, num2 });
				tempSeq.getItemSetList().add(tempItemSet);

				if (countSupport(tempSeq) >= minSupportCount) {
					resultSeq.add(tempSeq);
				}
			}
		}
		// ͬ��2Ƶ������뵽��Ƶ�����
		totalFrequencySeqs.addAll(resultSeq);

		return resultSeq;
	}

	/**
	 * ����ϴε�Ƶ�������Ӳ����µĺ�ѡ��
	 * 
	 * @param seqList
	 *            �ϴβ���ĺ�ѡ��
	 * @return
	 */
	private ArrayList<Sequence> generateCandidateItem(
			ArrayList<Sequence> seqList) {
		Sequence tempSeq;
		ArrayList<Integer> tempNumArray;
		ArrayList<Sequence> resultSeq = new ArrayList<>();
		// �����������б�
		ArrayList<ArrayList<Integer>> seqNums = new ArrayList<>();

		for (int i = 0; i < seqList.size(); i++) {
			tempNumArray = new ArrayList<>();
			tempSeq = seqList.get(i);
			for (ItemSet itemSet : tempSeq.getItemSetList()) {
				tempNumArray.addAll(itemSet.copyItems());
			}
			seqNums.add(tempNumArray);
		}

		ArrayList<Integer> array1;
		ArrayList<Integer> array2;
		// ����i,j�Ŀ���
		Sequence seqi = null;
		Sequence seqj = null;
		// �ж��Ƿ��ܹ����ӣ�Ĭ��������
		boolean canConnect = true;
		// �����������㣬�����Լ����Լ�����
		for (int i = 0; i < seqNums.size(); i++) {
			for (int j = 0; j < seqNums.size(); j++) {
				array1 = (ArrayList<Integer>) seqNums.get(i).clone();
				array2 = (ArrayList<Integer>) seqNums.get(j).clone();

				// ����һ��������ȥ����һ�����ڶ���������ȥ�����һ�������ʣ�µĲ�����ȣ����������
				array1.remove(0);
				array2.remove(array2.size() - 1);

				canConnect = true;
				for (int k = 0; k < array1.size(); k++) {
					if (array1.get(k) != array2.get(k)) {
						canConnect = false;
						break;
					}
				}

				if (canConnect) {
					seqi = seqList.get(i).copySeqence();
					seqj = seqList.get(j).copySeqence();

					int lastItemNum = seqj.getLastItemSetNum();
					if (seqj.isLastItemSetSingleNum()) {
						// ���j���е�����Ϊ��һֵ�������һ�������Զ��������i����
						ItemSet itemSet = new ItemSet(new int[] { lastItemNum });
						seqi.getItemSetList().add(itemSet);
					} else {
						// ���j���е�����Ϊ�ǵ�һֵ�������һ�����ּ���i�������һ�����
						ItemSet itemSet = seqi.getLastItemSet();
						itemSet.getItems().add(lastItemNum);
					}

					// �ж��Ƿ񳬹���С֧�ֶ���ֵ
					if (isChildSeqContained(seqi)
							&& countSupport(seqi) >= minSupportCount) {
						resultSeq.add(seqi);
					}
				}
			}
		}

		totalFrequencySeqs.addAll(resultSeq);
		return resultSeq;
	}

	/**
	 * �жϴ����е������������Ƿ�Ҳ��Ƶ������
	 * 
	 * @param seq
	 *            ��Ƚ�����
	 * @return
	 */
	private boolean isChildSeqContained(Sequence seq) {
		boolean isContained = false;
		ArrayList<Sequence> childSeqs;

		childSeqs = seq.createChildSeqs();
		for (Sequence tempSeq : childSeqs) {
			isContained = false;

			for (Sequence frequencySeq : totalFrequencySeqs) {
				if (tempSeq.compareIsSame(frequencySeq)) {
					isContained = true;
					break;
				}
			}

			if (!isContained) {
				break;
			}
		}

		return isContained;
	}

	/**
	 * ��ѡ���ж�֧�ֶȵ�ֵ
	 * 
	 * @param seq
	 *            ���ж�����
	 * @return
	 */
	private int countSupport(Sequence seq) {
		int count = 0;
		int matchNum = 0;
		Sequence tempSeq;
		ItemSet tempItemSet;
		HashMap<Integer, Integer> timeMap;
		ArrayList<ItemSet> itemSetList;
		ArrayList<ArrayList<Integer>> numArray = new ArrayList<>();
		// ÿ���Ӧ��ʱ������
		ArrayList<ArrayList<Integer>> timeArray = new ArrayList<>();

		for (ItemSet itemSet : seq.getItemSetList()) {
			numArray.add(itemSet.getItems());
		}

		for (int i = 0; i < totalSequences.size(); i++) {
			timeArray = new ArrayList<>();

			for (int s = 0; s < numArray.size(); s++) {
				ArrayList<Integer> childNum = numArray.get(s);
				ArrayList<Integer> localTime = new ArrayList<>();
				tempSeq = totalSequences.get(i);
				itemSetList = tempSeq.getItemSetList();

				for (int j = 0; j < itemSetList.size(); j++) {
					tempItemSet = itemSetList.get(j);
					matchNum = 0;
					int t = 0;

					if (tempItemSet.getItems().size() == childNum.size()) {
						timeMap = itemNum2Time.get(i).get(j);
						// ֻ�е������ƥ��ʱ��ƥ��
						for (int k = 0; k < childNum.size(); k++) {
							if (timeMap.containsKey(childNum.get(k))) {
								matchNum++;
								t = timeMap.get(childNum.get(k));
							}
						}

						// �����ȫƥ�䣬���¼ʱ��
						if (matchNum == childNum.size()) {
							localTime.add(t);
						}
					}

				}

				if (localTime.size() > 0) {
					timeArray.add(localTime);
				}
			}

			// �ж�ʱ���Ƿ�����ʱ�������СԼ��������㣬�����������ѡ����
			if (timeArray.size() == numArray.size()
					&& judgeTimeInGap(timeArray)) {
				count++;
			}
		}

		return count;
	}

	/**
	 * �ж������Ƿ�����ʱ��Լ��
	 * 
	 * @param timeArray
	 *            ʱ�����飬ÿ�д�������������еķ���ʱ������
	 * @return
	 */
	private boolean judgeTimeInGap(ArrayList<ArrayList<Integer>> timeArray) {
		boolean result = false;
		int preTime = 0;
		ArrayList<Integer> firstTimes = timeArray.get(0);
		timeArray.remove(0);

		if (timeArray.size() == 0) {
			return false;
		}

		for (int i = 0; i < firstTimes.size(); i++) {
			preTime = firstTimes.get(i);

			if (dfsJudgeTime(preTime, timeArray)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * ������ȱ���ʱ�䣬�ж��Ƿ��з��������ʱ����
	 * 
	 * @param preTime
	 * @param timeArray
	 * @return
	 */
	private boolean dfsJudgeTime(int preTime,
			ArrayList<ArrayList<Integer>> timeArray) {
		boolean result = false;
		ArrayList<ArrayList<Integer>> timeArrayClone = (ArrayList<ArrayList<Integer>>) timeArray
				.clone();
		ArrayList<Integer> firstItemItem = timeArrayClone.get(0);

		for (int i = 0; i < firstItemItem.size(); i++) {
			if (firstItemItem.get(i) - preTime >= min_gap
					&& firstItemItem.get(i) - preTime <= max_gap) {
				// ����2����ʱ������ʱ��Լ����������µݹ�
				preTime = firstItemItem.get(i);
				timeArrayClone.remove(0);

				if (timeArrayClone.size() == 0) {
					return true;
				} else {
					result = dfsJudgeTime(preTime, timeArrayClone);
					if (result) {
						return true;
					}
				}
			}
		}

		return result;
	}

	/**
	 * ��ʼ�������ʱ�������ͼ��Ϊ�˺����ʱ��Լ�����
	 */
	private void initItemNumToTimeMap() {
		Sequence seq;
		itemNum2Time = new ArrayList<>();
		HashMap<Integer, Integer> tempMap;
		ArrayList<HashMap<Integer, Integer>> tempMapList;

		for (int i = 0; i < totalSequences.size(); i++) {
			seq = totalSequences.get(i);
			tempMapList = new ArrayList<>();

			for (int j = 0; j < seq.getItemSetList().size(); j++) {
				ItemSet itemSet = seq.getItemSetList().get(j);
				tempMap = new HashMap<>();
				for (int itemNum : itemSet.getItems()) {
					tempMap.put(itemNum, j + 1);
				}

				tempMapList.add(tempMap);
			}

			itemNum2Time.add(tempMapList);
		}
	}

	/**
	 * ����GSP�㷨����
	 */
	public void gspCalculate() {
		ArrayList<Sequence> oneSeq;
		ArrayList<Sequence> twoSeq;
		ArrayList<Sequence> candidateSeq;

		initItemNumToTimeMap();
		oneSeq = generateOneFrequencyItem();
		twoSeq = generateTwoFrequencyItem(oneSeq);
		candidateSeq = twoSeq;

		// ������������ѡ����ֱ��û�в������ѡ��
		for (;;) {
			candidateSeq = generateCandidateItem(candidateSeq);

			if (candidateSeq.size() == 0) {
				break;
			}
		}

		outputSeqence(totalFrequencySeqs);

	}

	/**
	 * ��������б���Ϣ
	 * 
	 * @param outputSeqList
	 *            ����������б�
	 */
	private void outputSeqence(ArrayList<Sequence> outputSeqList) {
		for (Sequence seq : outputSeqList) {
			System.out.print("<");
			for (ItemSet itemSet : seq.getItemSetList()) {
				System.out.print("(");
				for (int num : itemSet.getItems()) {
					System.out.print(num + ",");
				}
				System.out.print("), ");
			}
			System.out.println(">");
		}
	}

}
