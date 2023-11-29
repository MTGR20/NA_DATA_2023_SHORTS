import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import org.apache.http.impl.client.HttpClientBuilder;

public class shorts {
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
        /*
        시나리오
        - 사용자는 쇼츠 페이지로 넘겼다
        * 데이터 타입 선택 전에 updated 자료가 있는 데이터 타입 개수를 먼저 체크해야 한다
            (각 타입별로 updated 자료가 몇 개 있는지도 체크하는 게 좋을 것 같다)
        - 랜덤으로 데이터 타입을 선택한다
        - 해당 데이터를 쇼츠로 보여줄 건데, 5번 중에 4번은 내용으로 1번은 투표로 보여준다
        - 최신 데이터가 다 보여졌으면 (updated 자료를 모두 방문했으면) 기존 쇼츠들을 띄운다
            : 즉 전체 쇼츠 개수 limit를 만들어서, limit을 넘지 않을 정도의 기존의 쇼츠 기록도 저장해둬야 한다

        데이터
        - article
        - 발의 법안? (if updated?)
        - ...
        - 추천 데이터: 투표에서 선택 / 검색한 키워드 / 하트를 누른 쇼츠 / 핫 토픽 ... 등 관심 키워드 추천 알고리즘 -> 다른 데이터 (검색) 또 새로운 쇼츠를 만들어 낸다
         */

        // 데이터 타입 개수 (보도자료 제외 updated) 체크
        String localDate = LocalDate.now().toString();
        int data_type_num = setting.getFixedApiUrlNum();

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

        //무조건 맨 처음엔
        //read article
        if (setting.isArticleTrue()) {

            System.out.println("isArticleTrue");

            String article_date = localDate;
            StringBuilder write_date = new StringBuilder();
            String value = write_date.append("&WRITE_DATE='").append(article_date).append("'").toString();
            setting.setApiUrl(setting.getArticle());
            setting.setValue(value, true);

            URL api_url = new URL(setting.getUrlWithType(true));
            List<Map<String, String>> articleList = readData.readArticle(api_url);
//            for (String content : contentList) {
//                System.out.println(content);
//                System.out.println();
//            }

            //TODO: FIX ERROR
            //gen ai와 연동해서 답변을 가져오면 됨!
            Map<String, String> chosen_article = articleList.get(0); // temp
            String chosen_title = chosen_article.get("title");
            String chosen_content = chosen_article.get("content");

//            StringBuilder message = new StringBuilder();
//            message.append("다음 기사의 핵심 내용을 뽑고 그걸 짧은 세 문장으로 요약해줘. 시각 장애인에게 기사를 요약해 읽어주는 서비스에 사용될거야.\n")
//                    .append(chosen_article.get("content"));

            String answer = "";

            URL clova_url = new URL("https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize");
            HttpURLConnection con = (HttpURLConnection) clova_url.openConnection();
            con.setRequestMethod("POST");

            setting.setData(chosen_title, chosen_content);
            String requestBody = setting.getData().toJSONString();
            con.setRequestProperty("Content-Type", setting.getContentType());
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", setting.getClientId());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", setting.getClientSecretKey());

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
                System.out.println(answer);

            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                System.out.println(responseCode);
            }

        }

        // 선택된 데이터 타입에 따라 가져오기
        int idx = updated_idxList.get(data_type);
        List<String> chosen_api = apiList.get(idx);
        System.out.println(chosen_api);

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
