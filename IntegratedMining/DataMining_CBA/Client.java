package IntegratedMining.DataMining_CBA;

import java.text.MessageFormat;

/**
 * CBA�㷨--���ڹ�������ķ����㷨
 * @author maxu
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		String attrDesc = "Age=Senior,CreditRating=Fair";
		String classification = null;
		
		//��С֧�ֶ���ֵ��
		double minSupportRate = 0.2;
		//��С���Ŷ���ֵ
		double minConf = 0.7;
		
		CBATool tool = new CBATool(filePath, minSupportRate, minConf);
		classification = tool.CBAJudge(attrDesc);
		System.out.println(MessageFormat.format("{0}�Ĺ���������Ϊ{1}", attrDesc, classification));
	}
}
