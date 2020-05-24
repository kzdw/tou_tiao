package com.zhousheng.tou_tiao.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteFileUtil {

    public static List<String> readFileContent(String fileName,String charSet) {
        List<String> result = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in,charSet));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                result.add(tempStr);
            }
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String readFileContentAndReturnSte(String fileName,String charSet) {
        List<String> stringList = ReadWriteFileUtil.readFileContent(fileName,charSet);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < stringList.size(); i++) {
            stringBuffer.append(stringList.get(i));
        }
    return stringBuffer.toString();
    }

    public static void writeTxt(String txtPath,String content){
        FileOutputStream fileOutputStream = null;
        File file = new File(txtPath);
        try {
            if(file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
