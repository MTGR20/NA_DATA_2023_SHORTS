import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPT {

    public static String chatGPT(String message) {
        String url = setting.getChatBotEndPoint();
        String apiKey = setting.getChatBotKey();
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", setting.getContentType());

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = bufferReader.readLine()) != null) {
                response.append(line);
            }
            bufferReader.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }

    public static void main(String[] args) {
        System.out.println(chatGPT("다음 기사를 핵심 내용을 포함해 짧은 3문장으로 요약해줘. 정무위 법안심사1소위, 워크아웃 통한 기업개선 추진근거 마련하는 「기업구조개선 촉진법안」 등 의결- 금융채권자 외 신규신용공여 허용, 적극업무 결과에 대한 책임면제 등 포함 -- 개인금융채무자 보호시스템 마련하는 「개인금융채무자보호법안」과 신협 지배구조/운영상 미비점 개선/보완하는 「신협법」 및 손해사정업무 공정성 확보에 관한 「보험업법」 등 개정안도 의결 -  국회 정무위원회는 11월 28일(화) 법안심사제1소위원회(위원장 김종민)를 열어 「기업구조개선 촉진법안」, 「개인금융채권의 관리 및 개인금융채무자의 보호에 관한 법률안」, 「신용협동조합법 일부개정법률안」, 「보험업법 일부개정법률안」을 의결하였다. 주요 내용을 살펴보면 ① 중앙회 임원 중 중앙회장 또는 전문이사를 제외한 나머지 임원은 시/도 단위별로 추천한 후보자 중에서 선출하게 함으로써 중앙회와 지역 간 협력 강화 및 현행 선출방식의 문제점을 해소하고, ② 조합의 손실금 보전시 법정적립금을 사용할 수 있게 하여 조합의 재정건전성을 제고하며, ③ 퇴임/퇴직한 임직원에 대한 중앙회장의 조치내용 통보 권한을 명확화하여 처분을 회피한 자에 대한 사후적 관리/감독을 강화하고, ④ 조합이 이익금의 일부를 배당준비금 목적의 임의적립금으로 적립할 수 있게하여 신용협동조합 활동이 활성화될 것으로 기대된다. 그 외에, 손해사정사 유형별 보험회사의 준수사항과 손해사정사의 의무 위반시 제재를 규정하는 등 손해사정업무의 공정성을 확보할 수 있도록 제도적 기반을 마련한 「보험업법 일부개정법률안」도 함께 의결되었다."));
    }
}
