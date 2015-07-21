package com.test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppendToFile {

	public static void appandA(String fileName,String content){
		try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static void main(String[] args) {
	    Date dt = new Date();
	    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
	    SimpleDateFormat sdf = new SimpleDateFormat("2012-04-23");
	    String temp_date=sdf.format(dt);
	    String nowdate=new AppendToFile().GetNowDate();
	    System.out.println(temp_date);
	}
	public String GetNowDate(){
	    String temp_str="";
	    Date dt = new Date();
	    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    temp_str=sdf.format(dt);
	    return temp_str;
	}
}
