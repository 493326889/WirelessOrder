package com.wirelessorder.webservice;

import java.io.IOException;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/*
 * @author jmjiao
 * Android WebService
 */
public class MyService {

	private String nameSpace; //�����ռ�
	private String methodName; //������
	private String url; //������������ip��Ϣ
	private String soapAction; //Ӧ�ó����ַ���
	private String methodNames[]; //������
	private Object methodValues[]; //����ֵ
	private int methodLenth = 0; //��������
	private HashMap<String,Object> maps; //װ�ز�����map
	
	/**
	 * @param nameSpace �����ռ�http://tempuri.org/
	 * @param methodName ������ login
	 * @param url ������������ip��Ϣ  http://172.20.9.82/MyService.asmx
	 * @param methodNames �������� String methodNames[] = new String[]{"x","y","z"}
	 * @param methodValues ����ֵ Object methodValues[] = new Object[]{9,14,8}
	 */
	
	public MyService(String nameSpace,String methodName,String url,String methodNames[],Object methodValues[]){
		this.nameSpace = nameSpace;
		this.url = url;
		this.methodName = methodName;
		this.methodNames = methodNames;
		this.methodValues = methodValues;
		this.soapAction = this.nameSpace + this.methodName;
		this.maps = new HashMap<String, Object>();
	}
	
	/**
	 * ���ز���
	 * @return boolean
	 */
	private boolean isMethodMapsOk(){
		int lenthN = methodNames.length;
		int lenthV = methodValues.length;
		if (lenthN != lenthV) {
			return false;
		}else{
			for (int i = 0; i < lenthV; i++) {
				maps.put(methodNames[i], methodValues[i]);
			}
			this.methodLenth = methodValues.length;
			return true;
		}
	}
	
	/**
	 * ȡ��WebService�����ķ���ֵ
	 * @return SoapSerializationEnvelope���͵ķ���ֵ
	 * @throws IOExceptio io ���쳣
	 * @throws XmlPullParserException XmlPullParser �쳣
	 */
	public SoapSerializationEnvelope getWebServiceReturner () throws IOException,XmlPullParserException{
		//step1  ָ��WebService �������ռ�͵��õķ�����
		SoapObject request = new SoapObject(nameSpace, methodName);
		//step2 ���õ��÷����Ĳ���ֵ������Ĳ���Ҫ��Webservice����ȫһ��
		if (isMethodMapsOk()) {
			for (int i = 0; i < this.methodLenth; i++) {
				request.addProperty(methodNames[i], maps.get(methodNames[i]));
			}
		}
		//step3 ���ɵ���WebService������SOAP������Ϣ����ָ��SOAP�İ汾
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//�����Ƿ���õ���dotNet�µ�WebService
		envelope.dotNet = true;
		//���룬�ȼ���envelope.bodyOut = request
		envelope.setOutputSoapObject(request);//��������
		
		//step4 ����HttpTransportSE����
		HttpTransportSE ht = new HttpTransportSE(url);//�����䷢����ip��Ϣ
		
		//step5 ����WebService
		ht.call(soapAction, envelope);
		
		//step6 ʹ��getResponse�������WebService�����ķ��ؽ��
		if (envelope.getResponse() != null) {
			//ȡֵ
			return envelope;
		}else{
			return null;
		}
		
	}
}
