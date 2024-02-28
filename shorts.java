import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

// shorts main 실행 시
// 한 줄에 날짜(YYYY-MM-DD)를 작성하고
// 다음 줄부터 당일 자료를 하나씩 세 줄 요약해서 "PATH/summary.txt"에 한 줄씩 입력

public class shorts {
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

        String summary = "";

        String date = "2024-02-27";
        StringBuilder write_date = new StringBuilder();
        String value = write_date.append("&WRITE_DATE='").append(date).append("'").toString();
        setting.setValue(value, true);

        URL api_url = new URL(setting.getUrlWithType(true));
        Object obj = readData.readArticle(api_url);
        if (obj == null) {
            System.out.println("해당하는 데이터가 없습니다.");
            return;
        }

        List<JSONObject> articleList = (List<JSONObject>) obj;
        int list_size = articleList.size();

        int chosen_idx = 0;

        String path = setting.getMyPath(true) + "summary.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true));
        bufferedWriter.write(date);
        bufferedWriter.newLine();
        bufferedWriter.close();

        while (chosen_idx < list_size) {

            JSONObject chosen_article = articleList.get(chosen_idx);
            String title = (String) chosen_article.get("TITLE");
            String content = (String) chosen_article.get("CONTENT");

            if (content.startsWith("https://")){
                System.out.println(content);
                Document doc = Jsoup.connect(String.valueOf(new URL(content))).get();
                String viewText = doc.getElementsByClass("viewTxt").text();         //viewText 맞는지 확인해봐야 한다.
                content = viewText;
            }

            summary = getSummary(title, content, true);

            if (!summary.isEmpty()){
                System.out.println("[1차 요약]\n" + summary);

                String short_summary = getShortSummary(summary);
                System.out.println("[2차 요약]\n" + short_summary);

                bufferedWriter = new BufferedWriter(new FileWriter(path, true));
                bufferedWriter.write(short_summary);
                bufferedWriter.newLine();
                bufferedWriter.close();

                summary = "";
                short_summary = "";
            }

            chosen_idx++;
        }

        System.out.println(list_size + "개의 기사 업데이트 완료\n");

    }

    public static String getShortSummary(String summary){
        summary = summary.replace("\n", "");
        StringBuilder message = new StringBuilder();
        message.append(setting.getChatBotMessage()).append(summary);
        return ChatGPT.chatGPT(message.toString());
    }

    public static String getSummary(String chosen_title, String chosen_content, boolean isArticle) throws IOException, ParseException {

        //gen ai와 연동해서 답변을 가져오면 됨!
        //summary
        String answer = "";

        URL clova_url = new URL(setting.getClovaSummaryApiUrl());
        HttpURLConnection con = (HttpURLConnection) clova_url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", setting.getContentType());
        con.setRequestProperty("Content-Type", setting.getContentType());
        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", setting.getClientId());
        con.setRequestProperty("X-NCP-APIGW-API-KEY", setting.getClientSecretKey());

        setting.setModel(isArticle);
        setting.setData(chosen_title, chosen_content);
        String requestBody = setting.getData();
//        System.out.println("requestBody = " + requestBody);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(requestBody.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader bufferedReader;

        if(responseCode==200) {
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String decodedString;
            while ((decodedString = bufferedReader.readLine()) != null) {
                answer = decodedString;
            }
            bufferedReader.close();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(answer);
            answer = (String) jsonObject.get("summary");

            return answer;

        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            System.out.println(responseCode);

            return null;
        }
    }

}
