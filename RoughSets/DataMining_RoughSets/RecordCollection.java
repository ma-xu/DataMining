package RoughSets.DataMining_RoughSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ��ݼ�¼���ϣ���һЩ��ͬ������
 * 
 * @author lyq
 * 
 */
public class RecordCollection {
	// ���ϰ������
	private HashMap<String, String> attrValues;
	// ��ݼ�¼�б�
	private ArrayList<Record> recordList;

	public RecordCollection() {
		this.attrValues = new HashMap<>();
		this.recordList = new ArrayList<>();
	}

	public RecordCollection(HashMap<String, String> attrValues,
			ArrayList<Record> recordList) {
		this.attrValues = attrValues;
		this.recordList = recordList;
	}

	public ArrayList<Record> getRecord() {
		return this.recordList;
	}

	/**
	 * ���ؼ��ϵ��ַ��������
	 * 
	 * @return
	 */
	public ArrayList<String> getRecordNames() {
		ArrayList<String> names = new ArrayList<>();

		for (int i = 0; i < recordList.size(); i++) {
			names.add(recordList.get(i).getName());
		}

		return names;
	}

	/**
	 * �жϼ����Ƿ���������ƶ�Ӧ������ֵ
	 * 
	 * @param attrName
	 *            ������
	 * @return
	 */
	public boolean isContainedAttrName(String attrName) {
		boolean isContained = false;

		if (this.attrValues.containsKey(attrName)) {
			isContained = true;
		}

		return isContained;
	}

	/**
	 * �ж�2�������Ƿ���ȣ��Ƚϰ����ݼ�¼�Ƿ���ȫһ��
	 * 
	 * @param rc
	 *            ��Ƚϼ���
	 * @return
	 */
	public boolean isCollectionSame(RecordCollection rc) {
		boolean isSame = false;

		for (Record r : recordList) {
			isSame = false;

			for (Record r2 : rc.recordList) {
				if (r.isRecordSame(r2)) {
					isSame = true;
					break;
				}
			}

			// �����1����¼������㼯�ϲ����
			if (!isSame) {
				break;
			}
		}

		return isSame;
	}

	/**
	 * ����֮��Ľ�����
	 * 
	 * @param rc
	 *            ������Ĳ������������һ����
	 * @return
	 */
	public RecordCollection overlapCalculate(RecordCollection rc) {
		String key;
		String value;
		RecordCollection resultCollection = null;
		HashMap<String, String> resultAttrValues = new HashMap<>();
		ArrayList<Record> resultRecords = new ArrayList<>();

		// ���м��ϵĽ����㣬����ͬ�ļ�¼����������
		for (Record record : this.recordList) {
			for (Record record2 : rc.recordList) {
				if (record.isRecordSame(record2)) {
					resultRecords.add(record);
					break;
				}
			}
		}

		// ���û�н�������ֱ�ӷ���
		if (resultRecords.size() == 0) {
			return null;
		}

		// ��2�����ϵ����Խ��кϲ�
		for (Map.Entry entry : this.attrValues.entrySet()) {
			key = (String) entry.getKey();
			value = (String) entry.getValue();

			resultAttrValues.put(key, value);
		}

		for (Map.Entry entry : rc.attrValues.entrySet()) {
			key = (String) entry.getKey();
			value = (String) entry.getValue();

			resultAttrValues.put(key, value);
		}

		resultCollection = new RecordCollection(resultAttrValues, resultRecords);
		return resultCollection;
	}

	/**
	 * �󼯺ϵĲ��������Ա������Ե�����
	 * 
	 * @param rc
	 *            ��ϲ��ļ���
	 * @return
	 */
	public RecordCollection unionCal(RecordCollection rc) {
		RecordCollection resultRc = null;
		ArrayList<Record> records = new ArrayList<>();

		for (Record r1 : this.recordList) {
			records.add(r1);
		}

		for (Record r2 : rc.recordList) {
			records.add(r2);
		}

		resultRc = new RecordCollection(null, records);
		return resultRc;
	}
	
	/**
	 * ��������а��Ԫ��
	 */
	public void printRc(){
		System.out.print("{");
		for (Record r : this.getRecord()) {
			System.out.print(r.getName() + ", ");
		}
		System.out.println("}");
	}
}
