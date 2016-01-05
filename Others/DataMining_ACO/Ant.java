package Others.DataMining_ACO;

import java.util.ArrayList;

/**
 * �����࣬����·������������
 * 
 * @author lyq
 * 
 */
public class Ant implements Comparable<Ant> {
	// ���ϵ�ǰ���ڳ���
	String currentPos;
	// ���ϱ�����ص�ԭ�����õ��ܾ���
	Double sumDistance;
	// ���м����Ϣ��Ũ�Ⱦ�������ʱ�����������
	double[][] pheromoneMatrix;
	// �����Ѿ��߹�ĳ��м���
	ArrayList<String> visitedCitys;
	// ��δ�߹�ĳ��м���
	ArrayList<String> nonVisitedCitys;
	// ���ϵ�ǰ�߹��·��
	ArrayList<String> currentPath;

	public Ant(double[][] pheromoneMatrix, ArrayList<String> nonVisitedCitys) {
		this.pheromoneMatrix = pheromoneMatrix;
		this.nonVisitedCitys = nonVisitedCitys;

		this.visitedCitys = new ArrayList<>();
		this.currentPath = new ArrayList<>();
	}

	/**
	 * ����·�����ܳɱ�(����)
	 * 
	 * @return
	 */
	public double calSumDistance() {
		sumDistance = 0.0;
		String lastCity;
		String currentCity;

		for (int i = 0; i < currentPath.size() - 1; i++) {
			lastCity = currentPath.get(i);
			currentCity = currentPath.get(i + 1);

			// ͨ����������м���
			sumDistance += ACOTool.disMatrix[Integer.parseInt(lastCity)][Integer
					.parseInt(currentCity)];
		}

		return sumDistance;
	}

	/**
	 * ����ѡ��ǰ����һ������
	 * 
	 * @param city
	 *            ��ѡ�ĳ���
	 */
	public void goToNextCity(String city) {
		this.currentPath.add(city);
		this.currentPos = city;
		this.nonVisitedCitys.remove(city);
		this.visitedCitys.add(city);
	}

	/**
	 * �ж������Ƿ��Ѿ������»ص����
	 * 
	 * @return
	 */
	public boolean isBack() {
		boolean isBack = false;
		String startPos;
		String endPos;

		if (currentPath.size() == 0) {
			return isBack;
		}

		startPos = currentPath.get(0);
		endPos = currentPath.get(currentPath.size() - 1);
		if (currentPath.size() > 1 && startPos.equals(endPos)) {
			isBack = true;
		}

		return isBack;
	}

	/**
	 * �ж������ڱ��ε��߹��·�����Ƿ��ӳ���i������j
	 * 
	 * @param cityI
	 *            ����I
	 * @param cityJ
	 *            ����J
	 * @return
	 */
	public boolean pathContained(String cityI, String cityJ) {
		String lastCity;
		String currentCity;
		boolean isContained = false;

		for (int i = 0; i < currentPath.size() - 1; i++) {
			lastCity = currentPath.get(i);
			currentCity = currentPath.get(i + 1);

			// ���ĳһ��·����ʼĩλ��һ�£�����Ϊ�о���˳���
			if ((lastCity.equals(cityI) && currentCity.equals(cityJ))
					|| (lastCity.equals(cityJ) && currentCity.equals(cityI))) {
				isContained = true;
				break;
			}
		}

		return isContained;
	}

	@Override
	public int compareTo(Ant o) {
		// TODO Auto-generated method stub
		return this.sumDistance.compareTo(o.sumDistance);
	}
}
