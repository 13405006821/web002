package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;

public class FileReader {
	public static void readFileByBytes(String filename){
		File file = new File(filename);
		InputStream in= null;
		try {
			System.out.println("���ֽڵ�λ��ȡ�ļ�����,һ�ζ�һ���ֽ�");
			in = new FileInputStream(file);
			int tempbyte;
			while((tempbyte=in.read())!=-1){
				System.out.write(tempbyte);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try{
			System.out.println("\n");
			System.out.println("���ֽ�Ϊ��λ��ȡ�ļ�����,һ�ζ�ȡ����ֽ�");
			byte tempbyte[] = new byte[100];
			int byteread = 0;
			in = new FileInputStream(file);
			FileReader.showAvailableBytes(in);
			while((byteread=in.read(tempbyte))!=-1){
				System.out.write(tempbyte, 0, byteread);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
     * ���ַ�Ϊ��λ��ȡ�ļ��������ڶ��ı������ֵ����͵��ļ�
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
        	System.out.println("\n");
            System.out.println("���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���ֽڣ�");
            // һ�ζ�һ���ַ�
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // ����windows�£�\r\n�������ַ���һ��ʱ����ʾһ�����С�
                // ������������ַ��ֿ���ʾʱ���ỻ�����С�
                // ��ˣ����ε�\r����������\n�����򣬽������ܶ���С�
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�");
            // һ�ζ�����ַ�
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
            while ((charread = reader.read(tempchars)) != -1) {
                // ͬ�����ε�\r����ʾ
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     * @throws FileNotFoundException 
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        InputStream is = null;
        InputStreamReader in = null;
        try {
        	is = new FileInputStream(file);
        	in = new InputStreamReader(is);
        	System.out.println("\n");
            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(in);
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * �����ȡ�ļ�����
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
        	System.out.println("\n");
            System.out.println("�����ȡһ���ļ����ݣ�");
            // ��һ����������ļ�������ֻ����ʽ
            randomFile = new RandomAccessFile(fileName, "r");
            // �ļ����ȣ��ֽ���
            long fileLength = randomFile.length();
            // ���ļ�����ʼλ��
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // һ�ζ�10���ֽڣ�����ļ����ݲ���10���ֽڣ����ʣ�µ��ֽڡ�
            // ��һ�ζ�ȡ���ֽ�������byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	/**
     * ��ʾ�������л�ʣ���ֽ���
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("��ǰ�ֽ��������е��ֽ���Ϊ:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	String fileName = "F:/temp/newTemp.txt";
        FileReader.readFileByBytes(fileName);
        FileReader.readFileByChars(fileName);
        FileReader.readFileByLines(fileName);
        FileReader.readFileByRandomAccess(fileName);
	}
}
