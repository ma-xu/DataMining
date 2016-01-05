package Classification.DataMining_KNN;

/**
 * �������
 * 
 * @author maxu
 * 
 */
public class Sample implements Comparable<Sample>{
	// ����ݵķ������
	private String className;
	// ����ݵ���������
	private String[] features;
	//������֮��ļ��ֵ���Դ�������
	private Integer distance;
	
	public Sample(String[] features){
		this.features = features;
	}
	
	public Sample(String className, String[] features){
		this.className = className;
		this.features = features;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String[] getFeatures() {
		return features;
	}

	public void setFeatures(String[] features) {
		this.features = features;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Sample o) {
		// TODO Auto-generated method stub
		return this.getDistance().compareTo(o.getDistance());
	}
	
}

