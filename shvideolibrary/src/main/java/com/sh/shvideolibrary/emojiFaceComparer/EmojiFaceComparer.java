
package  com.sh.shvideolibrary.emojiFaceComparer;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

//命令行调用函数示范
public class EmojiFaceComparer {
    public static void main(String[] args) throws Exception{
        API_port api = new API_port("./demo.jpeg", 1); //构造
        if (api.callAPI() == -1) { //检测是否检测到人脸
            return;
        }
        boolean result = api.getSimilarity(); //emoji编号
    }
    public static boolean test(String filePath, int emojiNumber) {
        API_port api = new API_port(filePath, emojiNumber); //构造
        if (api.callAPI() == -1) { //检测是否检测到人脸
            return false;
        }
        boolean result = api.getSimilarity(); //emoji编号
        return result;
    }

    public static boolean faceDetect( byte[] data, int emojiNumber) {
        API_port api = new API_port(data, emojiNumber); //构造
        if (api.callAPI() == -1) { //检测是否检测到人脸
            return false;
        }
        boolean result = api.getSimilarity(); //emoji编号
        return result;
    }
}

/**
 * API接口类
 * 封装调用face++ human face detect接口
 * 处理各类返回字符串和报错
 */
class API_port {
    //API接口用变量
    private String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, byte[]> byteMap = new HashMap<>();
    //用户人脸情绪数据
    private static HashMap<String, String> user_emotion_map = new HashMap<>();
    //四个标准表情情绪数据
    private static HashMap<String, String> emoji1_emotion_map = new HashMap<>();
    private static HashMap<String, String> emoji2_emotion_map = new HashMap<>();
    private static HashMap<String, String> emoji3_emotion_map = new HashMap<>();
    private static HashMap<String, String> emoji4_emotion_map = new HashMap<>();
    //表情标号 1-4
    private static int emoji_code;
    private int magic = 39;
    private static String TAG = "face++";

    //无参构造函数,测试用
    API_port(){
        File file = new File("./demo.jpeg");
        byte[] buff = getBytesFromFile(file);
        map.put("api_key", "mw_dRyH_eq4Chi-VrpIcgevCqQCTmZr4");
        map.put("api_secret", "udA5gopl1ZnbXc2zzsKKTmcj9o05dF36");
        map.put("return_landmark", "0");
        map.put("return_attributes", "emotion");
        byteMap.put("image_file", buff);
    }

    //构造函数
    API_port(String file_path, int eCode){
        emoji_code = eCode;
        File file = new File(file_path);
        byte[] buff = getBytesFromFile(file);
        map.put("api_key", "J4ehDyzUD4FB8GC11jh45e2s7NZyTcWE");
        map.put("api_secret", "IKSLsqT1Ps3gc49g1okBRBxAgDjgOZkQ");
        map.put("return_landmark", "0");
        map.put("return_attributes", "emotion");
        byteMap.put("image_file", buff);
        try {
            setStandard();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    API_port(byte[] data, int eCode){
        emoji_code = eCode;
        map.put("api_key", "mw_dRyH_eq4Chi-VrpIcgevCqQCTmZr4");
        map.put("api_secret", "udA5gopl1ZnbXc2zzsKKTmcj9o05dF36");
        map.put("return_landmark", "0");
        map.put("return_attributes", "emotion");
        byteMap.put("image_file", data);
        try {
            setStandard();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     　　* API调用函数
     　　* @return int 1:调用成功 2:人脸识别失败或网络堵塞
     　　* reports changes when the current set is complete
     　　*/
    public int callAPI() {
        try{
            //处理网络堵塞，同时避免无限循环
            for (int i=0; i<3; i++) {
                byte[] bacd = post(url, map, byteMap);
                String str = new String(bacd);
                int message = handleAPIMessage(str);
                if (message == -1) {    //网络堵塞
                    System.out.println("Handled CONCURRENCY_LIMIT_EXCEEDE error");
                    if (i == 2) {return -1;}    //到达尝试次数上限，终止
                    continue;
                }
                else if (message == 0) {    //人脸检测失败
                    System.out.println("Face detection fail");
                    return -1;
                }
                break;  //未触发错误
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     　　* 处理API返回字符串函数
     　　* @return int -1:网络堵塞 0:未检测到人脸 1:人脸识别成功
     　　* 将返回数值填入全局变量user_emotion_map
     　　*/
    private int handleAPIMessage(String str) {
        str = str.substring(1,str.length()-1);
        String[] keyValuePairs = str.split(",");
        if (keyValuePairs.length == 1) {    //网络堵塞
            return -1;
        }
        if (keyValuePairs.length < 6) {     //人脸失败失败
            return 0;
        }
        String[] emotion = new String[7];
        emotion[0] = keyValuePairs[4];
        emotion[0] = emotion[0].substring(magic, emotion[0].length());
        emotion[1] = keyValuePairs[5];
        emotion[2] = keyValuePairs[6];
        emotion[3] = keyValuePairs[7];
        emotion[4] = keyValuePairs[8];
        emotion[5] = keyValuePairs[9];
        emotion[6] = keyValuePairs[10];
        emotion[6] = emotion[6].substring(0, emotion[6].length()-2);
        for (String pair : emotion) {
            String[] entry = pair.split(":");
            String key = entry[0].trim();
            key = key.substring(1,key.length()-1);
            user_emotion_map.put(key, entry[1].trim());
        }
        return 1;
    }

    /**
     　　* 求差函数
     　　* @return boolean true/false
     　　* 欧几里德距离公式：sqrt（sum(xi-yi)^2 for all i）
     　　*/
    public static boolean getSimilarity() {
        double totalDifference = 0.0;
        Iterator iter = user_emotion_map.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            double standard;
            switch (emoji_code) {
                case 1: standard = Double.parseDouble(emoji1_emotion_map.get(key)); break;
                case 2: standard = Double.parseDouble(emoji2_emotion_map.get(key)); break;
                case 3: standard = Double.parseDouble(emoji3_emotion_map.get(key)); break;
                case 4: standard = Double.parseDouble(emoji4_emotion_map.get(key)); break;
                default: return false;
            }
            double diff = Double.parseDouble(user_emotion_map.get(key)) - standard;
            totalDifference += Math.pow(diff,2); //差平方求和
        }
        totalDifference = Math.sqrt(totalDifference); //根号
        Log.d(TAG, "getSimilarity: "+ Double.toString(totalDifference));
        if (totalDifference < 90) { return true; }
        return false;
    }

    //设置四个标准值
    private void setStandard() throws Exception{
        if (emoji_code == 1) {
            emoji1_emotion_map.put("neutral", "25.97");
            emoji1_emotion_map.put("sadness", "13.87");
            emoji1_emotion_map.put("disgust", "0.59");
            emoji1_emotion_map.put("anger", "2.31");
            emoji1_emotion_map.put("surprise", "1.10");
            emoji1_emotion_map.put("fear", "0.87");
            emoji1_emotion_map.put("happiness", "55.30");
        }
        if (emoji_code == 2) {
            emoji2_emotion_map.put("neutral", "0.71");
            emoji2_emotion_map.put("sadness", "0.08");
            emoji2_emotion_map.put("disgust", "0.09");
            emoji2_emotion_map.put("anger", "11.35");
            emoji2_emotion_map.put("surprise", "16.74");
            emoji2_emotion_map.put("fear", "0.10");
            emoji2_emotion_map.put("happiness", "70.92");
        }
        if (emoji_code == 3) {
            emoji3_emotion_map.put("neutral", "68.07");
            emoji3_emotion_map.put("sadness", "14.61");
            emoji3_emotion_map.put("disgust", "0.66");
            emoji3_emotion_map.put("anger", "0.62");
            emoji3_emotion_map.put("surprise", "2.22");
            emoji3_emotion_map.put("fear", "13.13");
            emoji3_emotion_map.put("happiness", "0.69");
        }
        if (emoji_code == 4) {
            emoji4_emotion_map.put("neutral", "10.54");
            emoji4_emotion_map.put("sadness", "0.23");
            emoji4_emotion_map.put("disgust", "1.80");
            emoji4_emotion_map.put("anger", "11.83");
            emoji4_emotion_map.put("surprise", "74.60");
            emoji4_emotion_map.put("fear", "0.44");
            emoji4_emotion_map.put("happiness", "0.56");
        }
    }

    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();
    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    private static String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }

    private static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}
