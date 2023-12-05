import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

//import org.apache.http.impl.client.HttpClientBuilder;

public class shorts {
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
        /*
        시나리오
        - 사용자는 쇼츠 페이지로 넘겼다
        * 데이터 타입 선택 전에 updated 자료가 있는 데이터 타입 개수를 먼저 체크해야 한다
            (각 타입별로 updated 자료가 몇 개 있는지도 체크하는 게 좋을 것 같다)
        - 랜덤으로 데이터 타입을 선택한다
        - 해당 데이터를 쇼츠로 보여줄 건데, 5번 중에 4번은 내용으로 1번은 투표로 보여준다 (X)
        - 최신 데이터가 다 보여졌으면 (updated 자료를 모두 방문했으면) 기존 쇼츠들을 띄운다
            : 즉 전체 쇼츠 개수 limit를 만들어서, limit을 넘지 않을 정도의 기존의 쇼츠 기록도 저장해둬야 한다
        데이터
        - article
        - 발의 법안 (if updated)
        ...
        - 추천 데이터: 투표에서 선택 / 검색한 키워드 / 하트를 누른 쇼츠 / 핫 토픽 ... 등 관심 키워드 추천 알고리즘 -> 다른 데이터 (검색) 또 새로운 쇼츠를 만들어 낸다
         */

        String localDate = LocalDate.now().toString();
        int data_type_num = setting.getFixedApiUrlNum();

        // 데이터 타입 개수 (보도자료 제외 updated) 체크
        // 다른 자료 사용 시 (추가 기능)
        int updated_count = 0;
        List<List<String>> apiList = readData.readOpenSrvApi();
        List<Integer> updated_idxList = getDataTypeNum(apiList, localDate);
        if ((updated_count = updated_idxList.size()) > 0){
            data_type_num = updated_count;
        }

        // 랜덤 생성
        int count_vote = 0;
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        int data_type = 0;
        boolean shorts_type_vote = false;

        // 스크롤 넘길 때마다 (위아래)
        // (추가 기능) -> random 대신 사용자 맞춤형 데이터 선택 : API?
        data_type = rand.nextInt(data_type_num);
        //count++;

        if (!shorts_type_vote) { //vote 아직 x
            shorts_type_vote = rand.nextBoolean();

            if (count_vote >= 5){
                shorts_type_vote = true;
            }
        }
        else { // vote 이미 o
            if (count_vote >= 5) {
                shorts_type_vote = false;
                count_vote = 0;
            }
        }

        String summary = "";

        if (setting.isArticleTrue()) {

            String article_date = localDate;
            StringBuilder write_date = new StringBuilder();
//            String value = write_date.append("&WRITE_DATE='").append(article_date).append("'").toString();
            String test_date = "2023-12-05";
            String value = write_date.append("&WRITE_DATE='").append(test_date).append("'").toString();

            setting.setApiUrl(setting.getArticle());
            setting.setValue(value, true);

            URL api_url = new URL(setting.getUrlWithType(true));

            Object obj = readData.readArticle(api_url);
            if (obj == null) {
                System.out.println("해당하는 데이터가 없습니다.");
                return;
            }
            List<JSONObject> articleList = (List<JSONObject>) obj;
            int list_size = articleList.size();

            int chosen_idx_temp = 0;
//            chosen_idx_temp = list_size - 1;
//            int check = 2^list_size;
//            int chosen_idx_temp = rand.nextInt(articleList.size()); //자유롭게 변경
//            System.out.println(articleList.size() + "개 중 " + (chosen_idx_temp + 1) + "번 째 기사");
            JSONObject chosen_article = articleList.get(chosen_idx_temp);
            summary = getSummary(chosen_article, true);

            if (chosen_idx_temp + 1 < list_size) {
                chosen_idx_temp++;
                System.out.println(chosen_idx_temp);
            }
            else {
                System.out.println("새롭게 업데이트된 기사가 없습니다.");
                return;
            }
        }

        if (!summary.isEmpty()){
            System.out.println("[1차 요약]\n" + summary);

            //더 짧게 요약 (chatbot)
            String short_summary = getShortSummary(summary);
            System.out.println("[2차 요약]\n" + short_summary);

            /* vote (추가 기능) - method로 빼기
            // 취소: chat gpt가 기사 내용을 바탕으로 적절한 투표 선택지를 생성하지 X
            // summary 내용을 기반으로 선택지 2개? 3개?
            // 선택한 내용 저장 -> 기록 & 데이터 증강*/

            String path = setting.getMyPath() + "summary.txt";
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true));
            bufferedWriter.write(short_summary);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }

        /*
        // article 후에 다른 데이터도 쇼츠
        // 선택된 데이터 타입에 따라 가져오기
        int idx = updated_idxList.get(data_type);
        List<String> chosen_api = apiList.get(idx);
        System.out.println("\n\n" + chosen_api);
        String summary = getSummary((JSONObject) chosen_api, false);
         */
    }

    public static String getShortSummary(String summary){
        summary = summary.replace("\n", "");
        StringBuilder message = new StringBuilder();
        message.append(setting.getChatBotMessage()).append(summary);
        return ChatGPT.chatGPT(message.toString());
    }

//    public static String makeSignature(String message, String secretKey) {
//
//        String signatureHeader = "";
//
//        try {
//            byte[] secrete_key_bytes = secretKey.getBytes(setting.getCharSet());
//
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secrete_key_bytes, setting.getChatBotKeyAlgorithm());
//            Mac mac = Mac.getInstance(setting.getChatBotKeyAlgorithm());
//            mac.init(secretKeySpec);
//
//            byte[] signature = mac.doFinal(message.getBytes(setting.getCharSet()));
//            signatureHeader = Base64.getEncoder().encodeToString(signature);
//
//            System.out.println(signatureHeader);
//            return signatureHeader;
//
//        } catch (Exception e){
//            System.out.println(e);
//        }
//
//        return signatureHeader;
//
//    }
//
//    public static String getReqMessage(String message) {
//
//        String requestBody = "";
//
//        try {
//
//            JSONObject obj = new JSONObject();
//
//            long timestamp = new Date().getTime();
//            //System.out.println("##"+timestamp);
//
//            obj.put("version", "v2");
//            obj.put("userId", setting.getChatBotUserId());
//            obj.put("timestamp", timestamp);
//
//            JSONObject bubbles_obj = new JSONObject();
//            bubbles_obj.put("type", "text");
//
//            JSONObject data_obj = new JSONObject();
//            data_obj.put("description", message);
////            data_obj.put("details", message);
//            bubbles_obj.put("type", "text");
//            bubbles_obj.put("data", data_obj);
//
//            JSONArray bubbles_array = new JSONArray();
//            bubbles_array.add(bubbles_obj);
//
//            obj.put("bubbles", bubbles_array);
////            obj.put("content", bubbles_array);
//            obj.put("event", "send");
//
//            requestBody = obj.toString();
//            System.out.println(requestBody);
//
//        } catch (Exception e){
//            System.out.println("## Exception : " + e);
//        }
//        return requestBody;
//    }
    public static String getSummary(JSONObject chosen_article, boolean isArticle) throws IOException, ParseException {

        String chosen_title = (String) chosen_article.get("TITLE");
        String chosen_content = (String) chosen_article.get("CONTENT");

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

    public static List<Integer> getDataTypeNum(List<List<String>> apiList, String today_date) throws java.text.ParseException {

        int count_updated = 0;
        int idx = 0;
        List<Integer> updated_idxList = new ArrayList<>();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Date today_date = simpleDateFormat.parse(today);

        //api : 이름, 최종수정일자, 출처(표시), 간단설명, 서비스URL, 명세서URL

        for (List<String> api : apiList) {

            String updated_date = api.get(1);
//            System.out.println(updated_date + ": " + api.get(0));

            if (updated_date != null) {

                //Date updated_date = simpleDateFormat.parse(temp);

                if (updated_date.equals(today_date)) {

                    if (api.get(0).equals("보도자료")){
                        setting.setArticleTrue(true);
                    }
                    else {
                        updated_idxList.add(idx);
                        count_updated++;
                    }
                }
            }

            idx ++;
        }

        return updated_idxList;
    }

}
