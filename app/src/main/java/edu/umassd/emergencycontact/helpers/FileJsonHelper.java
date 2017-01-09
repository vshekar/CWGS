package edu.umassd.emergencycontact.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jayesh on 10/20/16.
 */

public class FileJsonHelper {

    public String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;

    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public void createJsonFiles(File fileJson, String helper) throws Exception {

        if(!fileJson.exists()){
            try {
                fileJson.createNewFile();
                String jsonString = "{\""+helper+"\":{}}";
                writeJsonFile(fileJson, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {


        }
    }
    public void writeJsonFile(File file, String json)
    {
        BufferedWriter bufferedWriter = null;
        try {

            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
