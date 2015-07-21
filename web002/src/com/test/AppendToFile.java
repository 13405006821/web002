package com.test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppendToFile {

	public static void appandA(String fileName,String content){
		try {
            // ��һ����������ļ���������д��ʽ
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // �ļ����ȣ��ֽ���
            long fileLength = randomFile.length();
            //��д�ļ�ָ���Ƶ��ļ�β��
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static void main(String[] args) {
	    Date dt = new Date();
	    //����aa��ʾ�����硱�����硱    HH��ʾ24Сʱ��    �������hh��ʾ12Сʱ��
	    SimpleDateFormat sdf = new SimpleDateFormat("2012-04-23");
	    String temp_date=sdf.format(dt);
	    String nowdate=new AppendToFile().GetNowDate();
	    System.out.println(temp_date);
	}
	public String GetNowDate(){
	    String temp_str="";
	    Date dt = new Date();
	    //����aa��ʾ�����硱�����硱    HH��ʾ24Сʱ��    �������hh��ʾ12Сʱ��
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    temp_str=sdf.format(dt);
	    return temp_str;
	}
}
