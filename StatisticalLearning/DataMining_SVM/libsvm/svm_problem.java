package StatisticalLearning.DataMining_SVM.libsvm;
/**
 * ����ѵ������ݵĻ���Ϣ
 * @author maxu
 *
 */
public class svm_problem implements java.io.Serializable
{
	//�������������ܸ���
	public int l;
	//��������ֵ����
	public double[] y;
	//ѵ����������
	public svm_node[][] x;
}
