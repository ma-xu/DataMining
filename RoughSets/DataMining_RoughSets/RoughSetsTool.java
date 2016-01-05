package RoughSets.DataMining_RoughSets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * �ֲڼ�����Լ���㷨������
 * 
 * @author lyq
 * 
 */
public class RoughSetsTool {
	// �����������
	public static String DECISION_ATTR_NAME;

	// ��������ļ���ַ
	private String filePath;
	// ������������
	private String[] attrNames;
	// ���е����
	private ArrayList<String[]> totalDatas;
	// ���е���ݼ�¼,�����������Ǽ�¼�������ǿ�Լ��ģ�ԭʼ����ǲ��ܱ��
	private ArrayList<Record> totalRecords;
	// ��������ͼ
	private HashMap<String, ArrayList<String>> conditionAttr;
	// ���Լ�¼����
	private ArrayList<RecordCollection> collectionList;

	public RoughSetsTool(String filePath) {
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

		String[] array;
		Record tempRecord;
		HashMap<String, String> attrMap;
		ArrayList<String> attrList;
		totalDatas = new ArrayList<>();
		totalRecords = new ArrayList<>();
		conditionAttr = new HashMap<>();
		// ��ֵ���������
		attrNames = dataArray.get(0);
		DECISION_ATTR_NAME = attrNames[attrNames.length - 1];
		for (int j = 0; j < dataArray.size(); j++) {
			array = dataArray.get(j);
			totalDatas.add(array);
			if (j == 0) {
				// ���˵���һ����������
				continue;
			}

			attrMap = new HashMap<>();
			for (int i = 0; i < attrNames.length; i++) {
				attrMap.put(attrNames[i], array[i]);

				// Ѱ����������
				if (i > 0 && i < attrNames.length - 1) {
					if (conditionAttr.containsKey(attrNames[i])) {
						attrList = conditionAttr.get(attrNames[i]);
						if (!attrList.contains(array[i])) {
							attrList.add(array[i]);
						}
					} else {
						attrList = new ArrayList<>();
						attrList.add(array[i]);
					}
					conditionAttr.put(attrNames[i], attrList);
				}
			}
			tempRecord = new Record(array[0], attrMap);
			totalRecords.add(tempRecord);
		}
	}

	/**
	 * ����ݼ�¼������Էָ������
	 */
	private void recordSpiltToCollection() {
		String attrName;
		ArrayList<String> attrList;
		ArrayList<Record> recordList;
		HashMap<String, String> collectionAttrValues;
		RecordCollection collection;
		collectionList = new ArrayList<>();

		for (Map.Entry entry : conditionAttr.entrySet()) {
			attrName = (String) entry.getKey();
			attrList = (ArrayList<String>) entry.getValue();

			for (String s : attrList) {
				recordList = new ArrayList<>();
				// Ѱ������Ϊs����ݼ�¼���뵽������
				for (Record record : totalRecords) {
					if (record.isContainedAttr(s)) {
						recordList.add(record);
					}
				}
				collectionAttrValues = new HashMap<>();
				collectionAttrValues.put(attrName, s);
				collection = new RecordCollection(collectionAttrValues,
						recordList);

				collectionList.add(collection);
			}
		}
	}

	/**
	 * �������Լ���ͼ
	 * 
	 * @param reductAttr
	 *            ��ҪԼ�������
	 * @return
	 */
	private HashMap<String, ArrayList<RecordCollection>> constructCollectionMap(
			ArrayList<String> reductAttr) {
		String currentAtttrName;
		ArrayList<RecordCollection> cList;
		// �������Զ�Ӧͼ
		HashMap<String, ArrayList<RecordCollection>> collectionMap = new HashMap<>();

		// ��ȡ���������Բ���
		for (int i = 1; i < attrNames.length - 1; i++) {
			currentAtttrName = attrNames[i];

			// �жϴ��������Ƿ���ҪԼ��
			if (reductAttr != null && reductAttr.contains(currentAtttrName)) {
				continue;
			}

			cList = new ArrayList<>();

			for (RecordCollection c : collectionList) {
				if (c.isContainedAttrName(currentAtttrName)) {
					cList.add(c);
				}
			}

			collectionMap.put(currentAtttrName, cList);
		}

		return collectionMap;
	}

	/**
	 * ������еķ��Ѽ��ϼ���֪ʶϵͳ
	 */
	private ArrayList<RecordCollection> computeKnowledgeSystem(
			HashMap<String, ArrayList<RecordCollection>> collectionMap) {
		String attrName = null;
		ArrayList<RecordCollection> cList = null;
		// ֪ʶϵͳ
		ArrayList<RecordCollection> ksCollections;

		ksCollections = new ArrayList<>();

		// ȡ��1��
		for (Map.Entry entry : collectionMap.entrySet()) {
			attrName = (String) entry.getKey();
			cList = (ArrayList<RecordCollection>) entry.getValue();
			break;
		}
		collectionMap.remove(attrName);

		for (RecordCollection rc : cList) {
			recurrenceComputeKS(ksCollections, collectionMap, rc);
		}

		return ksCollections;
	}

	/**
	 * �ݹ�������е�֪ʶϵͳ��ͨ��������м��ϵĽ���
	 * 
	 * @param ksCollection
	 *            �Ѿ����֪ʶϵͳ�ļ���
	 * @param map
	 *            ��δ����й�����ļ���
	 * @param preCollection
	 *            ǰ���������Ѿ�ͨ�����������ļ���
	 */
	private void recurrenceComputeKS(ArrayList<RecordCollection> ksCollections,
			HashMap<String, ArrayList<RecordCollection>> map,
			RecordCollection preCollection) {
		String attrName = null;
		RecordCollection tempCollection;
		ArrayList<RecordCollection> cList = null;
		HashMap<String, ArrayList<RecordCollection>> mapCopy = new HashMap<>();
		
		//����Ѿ�û������ˣ���ֱ�����
		if(map.size() == 0){
			ksCollections.add(preCollection);
			return;
		}

		for (Map.Entry entry : map.entrySet()) {
			cList = (ArrayList<RecordCollection>) entry.getValue();
			mapCopy.put((String) entry.getKey(), cList);
		}

		// ȡ��1��
		for (Map.Entry entry : map.entrySet()) {
			attrName = (String) entry.getKey();
			cList = (ArrayList<RecordCollection>) entry.getValue();
			break;
		}

		mapCopy.remove(attrName);
		for (RecordCollection rc : cList) {
			// ��ѡ�����Ե�һ�����Ͻ��н����㣬Ȼ���ٴεݹ�
			tempCollection = preCollection.overlapCalculate(rc);

			if (tempCollection == null) {
				continue;
			}

			// ���map���Ѿ�û�������,˵���ݹ鵽ͷ��
			if (mapCopy.size() == 0) {
				ksCollections.add(tempCollection);
			} else {
				recurrenceComputeKS(ksCollections, mapCopy, tempCollection);
			}
		}
	}

	/**
	 * ���дֲڼ�����Լ���㷨
	 */
	public void findingReduct() {
		RecordCollection[] sameClassRcs;
		KnowledgeSystem ks;
		ArrayList<RecordCollection> ksCollections;
		// ��Լ�������
		ArrayList<String> reductAttr = null;
		ArrayList<String> attrNameList;
		// ���տ�Լ���������
		ArrayList<ArrayList<String>> canReductAttrs;
		HashMap<String, ArrayList<RecordCollection>> collectionMap;

		sameClassRcs = selectTheSameClassRC();
		// ���ｲ��ݰ��ո��������С���Ի�����9������
		recordSpiltToCollection();

		collectionMap = constructCollectionMap(reductAttr);
		ksCollections = computeKnowledgeSystem(collectionMap);
		ks = new KnowledgeSystem(ksCollections);
		System.out.println("ԭʼ���Ϸ�������½��Ƽ���");
		ks.getDownSimilarRC(sameClassRcs[0]).printRc();
		ks.getUpSimilarRC(sameClassRcs[0]).printRc();
		ks.getDownSimilarRC(sameClassRcs[1]).printRc();
		ks.getUpSimilarRC(sameClassRcs[1]).printRc();

		attrNameList = new ArrayList<>();
		for (int i = 1; i < attrNames.length - 1; i++) {
			attrNameList.add(attrNames[i]);
		}

		ArrayList<String> remainAttr;
		canReductAttrs = new ArrayList<>();
		reductAttr = new ArrayList<>();
		// �����������Եĵݹ�Լ��
		for (String s : attrNameList) {
			remainAttr = (ArrayList<String>) attrNameList.clone();
			remainAttr.remove(s);
			reductAttr = new ArrayList<>();
			reductAttr.add(s);
			recurrenceFindingReduct(canReductAttrs, reductAttr, remainAttr,
					sameClassRcs);
		}
		
		printRules(canReductAttrs);
	}

	/**
	 * �ݹ��������Լ��
	 * 
	 * @param resultAttr
	 *            �Ѿ��������Լ��������
	 * @param reductAttr
	 *            ��ҪԼ���������
	 * @param remainAttr
	 *            ʣ�������
	 * @param sameClassRc
	 *            ��������½��Ƽ��ϵ�ͬ�༯��
	 */
	private void recurrenceFindingReduct(
			ArrayList<ArrayList<String>> resultAttr,
			ArrayList<String> reductAttr, ArrayList<String> remainAttr,
			RecordCollection[] sameClassRc) {
		KnowledgeSystem ks;
		ArrayList<RecordCollection> ksCollections;
		ArrayList<String> copyRemainAttr;
		ArrayList<String> copyReductAttr;
		HashMap<String, ArrayList<RecordCollection>> collectionMap;
		RecordCollection upRc1;
		RecordCollection downRc1;
		RecordCollection upRc2;
		RecordCollection downRc2;

		collectionMap = constructCollectionMap(reductAttr);
		ksCollections = computeKnowledgeSystem(collectionMap);
		ks = new KnowledgeSystem(ksCollections);
		
		downRc1 = ks.getDownSimilarRC(sameClassRc[0]);
		upRc1 = ks.getUpSimilarRC(sameClassRc[0]);
		downRc2 = ks.getDownSimilarRC(sameClassRc[1]);
		upRc2 = ks.getUpSimilarRC(sameClassRc[1]);

		// ������½���û����ȫ���ԭ��������Ϊ���Բ��ܱ�Լ��
		if (!upRc1.isCollectionSame(sameClassRc[0])
				|| !downRc1.isCollectionSame(sameClassRc[0])) {
			return;
		}
		//����͸��඼��Ƚ�
		if (!upRc2.isCollectionSame(sameClassRc[1])
				|| !downRc2.isCollectionSame(sameClassRc[1])) {
			return;
		}

		// ���뵽�����
		resultAttr.add(reductAttr);
		//ֻʣ��1�����Բ�����Լ��
		if (remainAttr.size() == 1) {
			return;
		}

		for (String s : remainAttr) {
			copyRemainAttr = (ArrayList<String>) remainAttr.clone();
			copyReductAttr = (ArrayList<String>) reductAttr.clone();
			copyRemainAttr.remove(s);
			copyReductAttr.add(s);
			recurrenceFindingReduct(resultAttr, copyReductAttr, copyRemainAttr,
					sameClassRc);
		}
	}

	/**
	 * ѡ����������һ�µļ���
	 * 
	 * @return
	 */
	private RecordCollection[] selectTheSameClassRC() {
		RecordCollection[] resultRc = new RecordCollection[2];
		resultRc[0] = new RecordCollection();
		resultRc[1] = new RecordCollection();
		String attrValue;

		// �ҳ���һ����¼�ľ���������Ϊһ������
		attrValue = totalRecords.get(0).getRecordDecisionClass();
		for (Record r : totalRecords) {
			if (attrValue.equals(r.getRecordDecisionClass())) {
				resultRc[0].getRecord().add(r);
			}else{
				resultRc[1].getRecord().add(r);
			}
		}

		return resultRc;
	}
	
	/**
	 * ������߹���
	 * @param reductAttrArray
	 * Լ��������
	 */
	public void printRules(ArrayList<ArrayList<String>> reductAttrArray){
		//���������Ѿ�������Ĺ��򣬱����ظ����
		ArrayList<String> rulesArray;
		String rule;
		
		for(ArrayList<String> ra: reductAttrArray){
			rulesArray = new ArrayList<>();
			System.out.print("Լ������ԣ�");
			for(String s: ra){
				System.out.print(s + ",");
			}
			System.out.println();
			
			for(Record r: totalRecords){
				rule = r.getDecisionRule(ra);
				if(!rulesArray.contains(rule)){
					rulesArray.add(rule);
					System.out.println(rule);
				}
			}
			System.out.println();
		} 
	}

	/**
	 * �����¼����
	 * 
	 * @param rcList
	 *            �������¼����
	 */
	public void printRecordCollectionList(ArrayList<RecordCollection> rcList) {
		for (RecordCollection rc : rcList) {
			System.out.print("{");
			for (Record r : rc.getRecord()) {
				System.out.print(r.getName() + ", ");
			}
			System.out.println("}");
		}
	}
}
