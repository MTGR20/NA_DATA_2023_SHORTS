//import com.sun.net.httpserver.Request;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.Socket;
//import java.net.URL;
//import java.net.http.HttpResponse;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
////import org.apache.http.impl.client.HttpClientBuilder;
//
//// shorts main 실행 시
//// 한 줄에 날짜(YYYY-MM-DD)를 작성하고
//// 다음 줄부터 당일 자료를 하나씩 세 줄 요약해서 "PATH/summary.txt"에 한 줄씩 입력
//
//public class getScripts {
//    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException, ParserConfigurationException, SAXException {
//        /*
//        시나리오
//        - 사용자는 쇼츠 페이지로 넘겼다
//        * 데이터 타입 선택 전에 updated 자료가 있는 데이터 타입 개수를 먼저 체크해야 한다
//            (각 타입별로 updated 자료가 몇 개 있는지도 체크하는 게 좋을 것 같다)
//        - 랜덤으로 데이터 타입을 선택한다
//        - 해당 데이터를 쇼츠로 보여줄 건데, 5번 중에 4번은 내용으로 1번은 투표로 보여준다 (X)
//        - 최신 데이터가 다 보여졌으면 (updated 자료를 모두 방문했으면) 기존 쇼츠들을 띄운다
//            : 즉 전체 쇼츠 개수 limit를 만들어서, limit을 넘지 않을 정도의 기존의 쇼츠 기록도 저장해둬야 한다
//        데이터
//        - article
//        - 발의 법안 (if updated)
//        ...
//        - 추천 데이터: 투표에서 선택 / 검색한 키워드 / 하트를 누른 쇼츠 / 핫 토픽 ... 등 관심 키워드 추천 알고리즘 -> 다른 데이터 (검색) 또 새로운 쇼츠를 만들어 낸다
//         */
//
//        //////////////////////////////////////////////////////////////////////////////////////////
//        // update 된 자료에 한함
//
//        String localDate = LocalDate.now().toString();
//        int updated_count = 0;
//        List<List<String>> apiList = readData.readOpenSrvApi();
//        List<Integer> updated_idxList = getDataTypeNum(apiList, localDate);
//        updated_count = updated_idxList.size();
//
////        System.out.println(updated_count)
//        List<String> updated_List = new ArrayList<>();
//        for (int updated : updated_idxList) {
//            updated_List.add(apiList.get(updated).get(4));
//        }
//
//        //////////////////////////////////////////////////////////////////////////////////////////
//
//        String summary = "";
//        // int data_type = 30;
//        // readData.getReqSrvUrl(updated_List.get(data_type), true);
//
//        setting.setApiUrl(setting.getArticle());
//        String date = "2024-01-18";
//        StringBuilder write_date = new StringBuilder();
//        String value = write_date.append("&WRITE_DATE=\'").append(date).append("\'").toString();
//        setting.setValue(value, true);
//        URL api_url = new URL(setting.getUrlWithType(true));
//
//        Object obj = readData.readArticle(api_url);
//        if (obj == null) {
//            System.out.println("해당하는 데이터가 없습니다.");
//            return;
//        }
//
//        List<JSONObject> articleList = (List<JSONObject>) obj;
//        int list_size = articleList.size();
//        int chosen_idx = 0;
//
//        String path = setting.getMyPath() + "summary.txt";
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true));
//        bufferedWriter.write(date);
//        bufferedWriter.newLine();
//        bufferedWriter.close();
//
//        while (chosen_idx < list_size) {
//            JSONObject chosen_article = articleList.get(chosen_idx);
////            summary = getParaphrase(chosen_article);
//            summary = "\"\\uae40\\uc9c4\\ud45c \\uad6d\\ud68c\\uc758\\uc7a5\\uc774 \\uc6b8\\ub4dc \\uc54c\\ub77c\\ubbf8 \\ubaa8\\ub85c\\ucf54 \\ud558\\uc6d0\\uc758\\uc7a5\\uc758 \\ucd08\\uccad\\uc73c\\ub85c \\ubaa8\\ub85c\\ucf54\\ub97c \\uacf5\\uc2dd \\ubc29\\ubb38\\ud588\\uc2b5\\ub2c8\\ub2e4. \\uae40 \\uc758\\uc7a5\\uc740 \\uc54c\\ub77c\\ubbf8 \\uc758\\uc7a5\\uc744 \\ub9cc\\ub098 \\ud604\\uc9c0 \\uc9c4\\ucd9c \\ud55c\\uad6d \\uae30\\uc5c5\\uc758 \\uc560\\ub85c\\uc0ac\\ud56d\\uc744 \\ud574\\uc18c\\ud558\\uace0 \\uc778\\ud504\\ub77c, \\uc5d0\\ub108\\uc9c0, \\uc778\\uc801\\uc790\\uc6d0 \\uac1c\\ubc1c \\ub4f1 \\ubd84\\uc57c\\uc5d0\\uc11c\\uc758 \\ud611\\ub825 \\ud655\\ub300 \\ubc29\\uc548\\uc744 \\ub17c\\uc758\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\uae40 \\ucc28\\uad00\\uc740 \\ud55c\\uad6d\\uc804\\uc7c1 \\ub2f9\\uc2dc \\ubaa8\\ub85c\\ucf54 \\uad6d\\ubbfc\\ub4e4\\uc758 \\ud76c\\uc0dd\\uc5d0 \\uac10\\uc0ac\\ub97c \\ud45c\\ud558\\uace0, \\uad00\\ub828 \\ub0b4\\uc6a9\\uc744 \\ubaa8\\ub85c\\ucf54 \\uad50\\uacfc\\uc11c\\uc5d0 \\uc218\\ub85d\\ud574 \\uc904 \\uac83\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4. \\ub610\\ud55c, \\ubd81\\ud55c\\uc758 \\ubd88\\ubc95\\uc801\\uc778 \\ud575-\\ubbf8\\uc0ac\\uc77c \\uc704\\ud611\\uc5d0 \\ub9de\\uc11c \\uc591\\uad6d\\uc774 \\uc678\\uad50\\uc801 \\ud611\\ub825\\uc744 \\uc9c0\\uc18d \\uac15\\ud654\\ud574 \\ub098\\uac00\\uae30\\ub97c \\ud76c\\ub9dd\\ud55c\\ub2e4\\uace0 \\ubc1d\\ud614\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\ub450 \\uc815\\uc0c1\\uc740 \\ubaa8\\ub85c\\ucf54 \\uc9c4\\ucd9c \\uc6b0\\ub9ac \\uae30\\uc5c5\\ub4e4\\uc758 \\uc560\\ub85c\\uc0ac\\ud56d \\ud574\\uc18c\\ub97c \\uc704\\ud574 \\uc801\\uadf9 \\ub178\\ub825\\ud574 \\ub098\\uac00\\uae30\\ub85c \\ud558\\uace0, \\uc778\\ud504\\ub77c, \\uc5d0\\ub108\\uc9c0, \\uc778\\uc801\\uc790\\uc6d0 \\uac1c\\ubc1c \\ub4f1 \\ub2e4\\uc591\\ud55c \\ubd84\\uc57c\\uc5d0\\uc11c\\uc758 \\ud611\\ub825 \\ud655\\ub300\\ub97c \\uc704\\ud55c \\uc2ec\\ub3c4 \\uc788\\ub294 \\ub17c\\uc758\\ub97c \\uac00\\uc84c\\uc2b5\\ub2c8\\ub2e4. \\ub610\\ud55c \\uae40 \\uc758\\uc7a5\\uc740 \\uc18c\\ud615 \\ubaa8\\ub4c8\\ud615 \\uc6d0\\uc804 \\ub3c4\\uc785\\uc744 \\uc81c\\uc548\\ud558\\uace0, \\uc6b0\\ub9ac \\uae30\\uc5c5\\ub4e4\\uc774 \\ubaa8\\ub85c\\ucf54\\uc758 \\uc870\\uc138\\uc815\\ucc45 \\ubcc0\\ud654\\ub97c \\uc608\\uce21\\ud558\\uace0 \\ub300\\ube44\\ud560 \\uc218 \\uc788\\ub3c4\\ub85d \\ubaa8\\ub85c\\ucf54 \\ud558\\uc6d0\\uc774 \\uc801\\uadf9\\uc801\\uc778 \\uad00\\uc2ec\\uc744 \\uac00\\uc838\\uc904 \\uac83\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\ub300\\ube44 \\uc54c\\ub77c\\ubbf8 \\uc704\\uc6d0\\uc7a5\\uc740 \\ubaa8\\ub85c\\ucf54\\uac00 2030\\ub144\\uae4c\\uc9c0 \\uc778\\ud504\\ub77c, \\ud574\\uc218\\ub2f4\\uc218\\ud654, \\uccad\\uc815\\uc5d0\\ub108\\uc9c0 \\ub4f1 \\ub300\\uaddc\\ubaa8 \\ud504\\ub85c\\uc81d\\ud2b8 \\ucd94\\uc9c4\\uc744 \\uc704\\ud574 \\ud55c\\uad6d\\uacfc\\uc758 \\ud611\\ub825\\uc744 \\ud76c\\ub9dd\\ud55c\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4. \\uae40 \\ucc28\\uad00\\uc740 \\ubaa8\\ub85c\\ucf54 \\uccad\\ub144\\ub4e4\\uc744 \\uc804\\ubb38 \\uc5d4\\uc9c0\\ub2c8\\uc5b4\\ub85c \\uc591\\uc131\\ud558\\ub294 \\uacf5\\uc801\\uac1c\\ubc1c\\uc6d0\\uc870(ODA) \\uc0ac\\uc5c5\\uc744 \\ud655\\ub300\\ud560 \\uac83\\uc744 \\uc81c\\uc548\\ud588\\ub2e4. \\ub610\\ud55c, \\uc62c\\ud574 6\\uc6d4 \\uc11c\\uc6b8\\uc5d0\\uc11c \\ucd5c\\ucd08\\ub85c \\uac1c\\ucd5c\\ub420 \\uc608\\uc815\\uc778 '\\ud55c-\\uc544\\ud504\\ub9ac\\uce74 \\uc815\\uc0c1\\ud68c\\uc758'\\uc5d0 \\ud280\\ub2c8\\uc9c0 \\ucd5c\\uace0\\uc704\\uae09 \\uc778\\uc0ac\\ub4e4\\uc758 \\ucc38\\uc11d\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4.\", \"id\": \"9e66eabf-889d-45a3-98c8-348d255ba8d6\"}, {\"text\": \"\\ub77c\\uc2dc\\ub4dc \\ub2e4\\ube44 \\uc54c\\ub77c\\ubbf8 \\ubaa8\\ub85c\\ucf54 \\ud558\\uc6d0\\uc758\\uc7a5\\uc758 \\ucd08\\uccad\\uc73c\\ub85c \\ubaa8\\ub85c\\ucf54\\ub97c \\ubc29\\ubb38\\ud55c \\uae40\\uc9c4\\ud45c \\uad6d\\ud68c\\uc758\\uc7a5\\uc740 17\\uc77c \\uc624\\uc804(\\ud604\\uc9c0\\uc2dc\\uac04) \\uc54c\\ub77c\\ubbf8 \\ubaa8\\ub85c\\ucf54 \\ud558\\uc6d0\\uc758\\uc7a5\\uacfc \\ud68c\\ub2f4\\uc744 \\uac00\\uc84c\\ub2e4. \\uc774 \\uc790\\ub9ac\\uc5d0\\uc11c \\ub450 \\uc758\\uc7a5\\uc740 \\ud604\\uc9c0 \\uc9c4\\ucd9c \\ud55c\\uad6d \\uae30\\uc5c5\\ub4e4\\uc758 \\uc560\\ub85c\\uc0ac\\ud56d\\uc744 \\ud574\\uacb0\\ud558\\uace0 \\uc778\\ud504\\ub77c, \\uc5d0\\ub108\\uc9c0, \\uc778\\uc801\\uc790\\uc6d0\\uac1c\\ubc1c \\ubd84\\uc57c \\ud611\\ub825\\uc744 \\ud655\\ub300\\ud558\\uae30\\ub85c \\ud569\\uc758\\ud588\\ub2e4. \\uae40 \\uc758\\uc7a5\\uc740 \\ud55c\\uad6d\\uc804\\uc7c1 \\ub2f9\\uc2dc \\ubaa8\\ub85c\\ucf54 \\uc80a\\uc740\\uc774\\ub4e4\\uc758 \\ud76c\\uc0dd\\uc5d0 \\uac10\\uc0ac\\ub97c \\ud45c\\ud558\\uace0, \\ud6c4\\uc138\\ub4e4\\uc774 \\uc11c\\ub85c\\uc5d0 \\ub300\\ud55c \\uc774\\ud574\\ub97c \\ub192\\uc77c \\uc218 \\uc788\\ub3c4\\ub85d \\uad00\\ub828 \\ub0b4\\uc6a9\\uc744 \\ubaa8\\ub85c\\ucf54 \\uad50\\uacfc\\uc11c\\uc5d0 \\uc218\\ub85d\\ud574 \\uc904 \\uac83\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\uc54c\\ub77c\\ubbf8 \\uc758\\uc7a5\\uc740 \\ubaa8\\ub85c\\ucf54 \\uc758\\ud68c \\uac1c\\uc6d0 60\\uc8fc\\ub144\\uc744 \\ub9de\\uc774\\ud55c \\uae40 \\uc758\\uc7a5\\uc744 \\ud658\\uc601\\ud558\\uba70 \\uc11d\\uc720\\uc640 \\uac00\\uc2a4\\uac00 \\uc5c6\\ub294 \\ubaa8\\ub85c\\ucf54\\uc758 \\ubc1c\\uc804\\uc0c1\\uc744 \\uc5b8\\uae09\\ud588\\ub2e4. \\uae40 \\uc758\\uc7a5\\uc740 \\uac11\\uc791\\uc2a4\\ub7ec\\uc6b4 \\uc218\\uc785\\uc138 \\uc778\\uc0c1\\uacfc \\uc778\\uc0b0\\ube44\\ub8cc \\uacf5\\uc7a5 \\ubbf8\\uc218\\uae08 \\ubd84\\uc7c1 \\ud574\\uacb0\\uc744 \\uc704\\ud55c \\ud558\\uc6d0\\uc758 \\uc9c0\\uc6d0\\uc744 \\uc694\\uccad\\ud588\\ub2e4. \\ub610\\ud55c \\uae40 \\ucd1d\\ub9ac\\uc640 \\uc54c\\ub77c\\ubbf8 \\uc758\\uc7a5\\uc740 \\uc778\\ud504\\ub77c, \\ud574\\uc218\\ub2f4\\uc218\\ud654, \\uc18c\\ud615 \\ubaa8\\ub4c8\\ud615 \\uc6d0\\uc804, \\ubaa8\\ub85c\\ucf54 \\uccad\\ub144\\uc778\\ub825 \\uc591\\uc131\\uc744 \\uc704\\ud55c \\uacf5\\uc801\\uac1c\\ubc1c\\uc6d0\\uc870(ODA) \\uc0ac\\uc5c5 \\ub4f1\\uc5d0\\uc11c\\uc758 \\ud611\\ub825 \\ud655\\ub300\\uc5d0 \\ub300\\ud574\\uc11c\\ub3c4 \\ub17c\\uc758\\ud588\\ub2e4.  \\n\\n\\ub9c8\\uc9c0\\ub9c9\\uc73c\\ub85c \\ud280\\ub2c8\\uc2a4\\ub85c \\uc774\\ub3d9\\ud55c \\uae40 \\ucc28\\uad00\\uc740 \\ud280\\ub2c8\\uc9c0 \\ub300\\ud1b5\\ub839\\uacfc \\uad6d\\ud68c\\uc758\\uc7a5\\uc744 \\ub9cc\\ub098 \\ud611\\ub825 \\ud655\\ub300\\ub97c \\ubaa8\\uc0c9\\ud558\\uace0, 6\\uc6d4 \\uc11c\\uc6b8\\uc5d0\\uc11c \\uc5f4\\ub9ac\\ub294 '\\ud55c-\\uc544\\ud504\\ub9ac\\uce74 \\uc815\\uc0c1\\ud68c\\uc758'\\uc5d0 \\ud280\\ub2c8\\uc9c0 \\uace0\\uc704\\uae09 \\uc778\\uc0ac\\ub4e4\\uc758 \\ucc38\\uc11d\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4. \\uc774\\uc5b4\\uc11c \\uc5b8\\ub860 \\ube0c\\ub9ac\\ud551\\uc744 \\uc5f4\\uc5b4 \\ud68c\\ub2f4 \\uacb0\\uacfc\\ub97c \\ubc1c\\ud45c\\ud588\\uc2b5\\ub2c8\\ub2e4.\", \"id\": \"822c9e57-534c-45bb-a598-85dc8312cac4\"}, {\"text\": \"\\uae40\\uc9c4\\ud45c \\uad6d\\ud68c\\uc758\\uc7a5\\uc740 \\ub77c\\uc2dc\\ub4dc \\ub2e4\\ube44 \\uc54c\\ub77c\\ubbf8 \\ud558\\uc6d0\\uc758\\uc7a5\\uc758 \\ucd08\\uccad\\uc73c\\ub85c \\ubaa8\\ub85c\\ucf54\\ub97c \\ubc29\\ubb38\\ud574 17\\uc77c(\\ud604\\uc9c0\\uc2dc\\uac04) \\uc6b8\\ub4dc \\uc54c\\ub77c\\ubbf8 \\ud558\\uc6d0\\uc758\\uc7a5\\uacfc \\ud68c\\ub2f4\\uc744 \\uac00\\uc84c\\ub2e4. \\uc774 \\uc790\\ub9ac\\uc5d0\\uc11c \\uc591\\uad6d \\uc815\\uc0c1\\uc740 \\ud604\\uc9c0 \\uc9c4\\ucd9c \\ud55c\\uad6d \\uae30\\uc5c5\\uc758 \\uc560\\ub85c\\uc0ac\\ud56d\\uc744 \\ud574\\uc18c\\ud558\\uace0 \\uc778\\ud504\\ub77c, \\uc5d0\\ub108\\uc9c0, \\uc778\\uc801\\uc790\\uc6d0 \\uac1c\\ubc1c \\ub4f1\\uc758 \\ubd84\\uc57c\\uc5d0\\uc11c \\ud611\\ub825\\uc744 \\ud655\\ub300\\ud558\\uae30\\ub85c \\ud569\\uc758\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\uae40 \\ucc28\\uad00\\uc740 \\ubaa8\\ub85c\\ucf54 \\uad6d\\ubbfc\\ub4e4\\uc758 \\ud55c\\uad6d\\uc804 \\ucc38\\uc804\\uc5d0 \\uac10\\uc0ac\\ub97c \\ud45c\\ud558\\uace0, \\ubaa8\\ub85c\\ucf54 \\ud559\\uc0dd\\ub4e4\\uc774 \\uad50\\uacfc\\uc11c\\uc5d0\\uc11c \\uc774\\ub97c \\ubc30\\uc6b0\\uae30\\ub97c \\ud76c\\ub9dd\\ud55c\\ub2e4\\uace0 \\ub9d0\\ud588\\uc2b5\\ub2c8\\ub2e4. \\ub610\\ud55c \\ubaa8\\ub85c\\ucf54 \\uc758\\ud68c\\uc758 \\uc9c0\\uc6d0\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\uae40 \\uc758\\uc7a5\\uacfc \\uc54c\\ub77c\\ubbf8 \\uc758\\uc7a5\\uc740 \\uc778\\ud504\\ub77c, \\uc5d0\\ub108\\uc9c0, \\uc778\\uc801\\uc790\\uc6d0 \\uac1c\\ubc1c \\ubd84\\uc57c\\uc5d0\\uc11c\\uc758 \\ud611\\ub825 \\ud655\\ub300 \\ubc29\\uc548\\uc5d0 \\ub300\\ud574\\uc11c\\ub3c4 \\ub17c\\uc758\\ud588\\uc2b5\\ub2c8\\ub2e4. \\uae40 \\uc758\\uc7a5\\uc740 \\uc6b0\\ub9ac \\uae30\\uc5c5\\ub4e4\\uc774 \\ubaa8\\ub85c\\ucf54\\uc758 \\uc870\\uc138\\uc815\\ucc45 \\ubcc0\\ud654\\ub97c \\uc608\\uce21\\ud558\\uace0 \\ub300\\ube44\\ud560 \\uc218 \\uc788\\ub3c4\\ub85d \\ud558\\uc6d0\\uc774 \\uad00\\uc2ec\\uc744 \\uac00\\uc838\\uc904 \\uac83\\uacfc \\uc778\\uc0b0\\ube44\\ub8cc\\uacf5\\uc7a5 \\uac74\\uc124\\uc0ac\\uc5c5 \\uc5f0\\uc7a5\\uc5d0 \\ub530\\ub978 \\ubbf8\\uc218\\uae08 \\ubb38\\uc81c \\ud574\\uacb0\\uc744 \\uc704\\ud574 \\ub178\\ub825\\ud574\\uc904 \\uac83\\uc744 \\uc694\\uccad\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\uc774\\uc5d0 \\ub300\\ud574 \\ub300\\ube44 \\uc54c\\ub77c\\ubbf8 \\ud558\\uc6d0\\uc758\\uc7a5\\uc740 \\ubaa8\\ub85c\\ucf54 \\uc870\\uc138\\uccad\\uc5d0 \\uc790\\uc138\\ud55c \\uc815\\ubcf4\\ub97c \\uc694\\uccad\\ud574 \\uc8fc\\ubaa8\\ub85c\\ucf54 \\ud55c\\uad6d\\ub300\\uc0ac\\uad00\\uacfc \\uacf5\\uc720\\ud558\\uaca0\\ub2e4\\uace0 \\ub2f5\\ud588\\ub2e4. \\ub610\\ud55c \\uae40 \\uc758\\uc7a5\\uc740 \\uc18c\\ud615 \\ubaa8\\ub4c8\\ud615 \\uc6d0\\uc804 \\ub3c4\\uc785\\uacfc \\ubaa8\\ub85c\\ucf54 \\uc80a\\uc740\\uc774\\ub4e4\\uc744 \\uc804\\ubb38 \\uc5d4\\uc9c0\\ub2c8\\uc5b4\\ub85c \\uc591\\uc131\\ud558\\uae30 \\uc704\\ud55c \\uacf5\\uc801\\uac1c\\ubc1c\\uc6d0\\uc870(ODA) \\uc0ac\\uc5c5 \\ud655\\ub300\\ub97c \\uc81c\\uc548\\ud588\\uc2b5\\ub2c8\\ub2e4.\\n\\n\\uae40 \\uc758\\uc7a5\\uc740 \\uba74\\ub2f4 \\ud6c4 \\uae30\\uc790 \\ube0c\\ub9ac\\ud551\\uc744 \\uac16\\uace0 \\ubaa8\\ub85c\\ucf54 \\uad6d\\ud68c\\uc640 \\uc815\\ubd80 \\uad00\\uacc4\\uc790\\ub4e4\\uc758 \\ub178\\uace0\\uc5d0 \\uac10\\uc0ac\\ub97c \\ud45c\\ud588\\uc2b5\\ub2c8\\ub2e4. \\ub610\\ud55c, \\ubd81\\ud55c\\uc758 \\ubd88\\ubc95\\uc801\\uc778 \\ud575-\\ubbf8\\uc0ac\\uc77c \\uc704\\ud611\\uc5d0 \\ub300\\ud55c \\uad6d\\uc81c\\uc0ac\\ud68c\\uc758 \\ub178\\ub825\\uc5d0 \\ub300\\ud55c \\ubaa8\\ub85c\\ucf54\\uc758 \\uad00\\uc2ec\\uacfc \\uc9c0\\uc9c0\\uc5d0 \\uc0ac\\uc758\\ub97c \\ud45c\\ud558\\uace0, \\ub2e4\\uc74c\\uacfc \\uac19\\uc774 \\ub9d0\\ud588\\uc2b5\\ub2c8\\ub2e4.\"";
//            summary = convertString(summary);
//            System.out.println(summary);
//
//
//            if (!summary.isEmpty()) {
//                bufferedWriter = new BufferedWriter(new FileWriter(path, true));
//                bufferedWriter.write(summary);
//                bufferedWriter.newLine();
//                bufferedWriter.close();
//                summary = "";
//            }
//
//            chosen_idx++;
//        }
//
//        System.out.println(list_size + "개의 기사 업데이트 완료\n");
//    }
//
//    public static String getText(String textCortexResponse) throws ParseException {
//
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) jsonParser.parse(textCortexResponse);
//
//        JSONArray jsonArray = (JSONArray) (jsonObject.get("generated_text"));
//        jsonObject = (JSONObject) jsonArray.get(0);
//
//        return (String) jsonObject.get("text");
//
//    }
//
//    // 유니코드에서 String으로 변환
//    public static String convertString(String val) {
//
//        StringBuffer sb = new StringBuffer();
//
//        for (int i = 0; i < val.length(); i++) {
//            if ('\\' == val.charAt(i) && 'u' == val.charAt(i + 1)) {
//                // 그 뒤 네글자는 유니코드의 16진수 코드이다. int형으로 바꾸어서 다시 char 타입으로 강제 변환한다.
//                Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);
//                // 변환된 글자를 버퍼에 넣는다.
//                sb.append(r);
//                // for의 증가 값 1과 5를 합해 6글자를 점프
//                i += 5;
//            } else {
//                // ascii코드면 그대로 버퍼에 넣는다.
//                sb.append(val.charAt(i));
//            }
//        }
//
//        return sb.toString();
//    }
//
//    public static String getParaphrase(JSONObject chosen_article) throws IOException, ParseException {
//
//        URL api_url = new URL(setting.getTextCortexApiUrl());
//        HttpURLConnection con = (HttpURLConnection) api_url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", setting.getContentType());
//        String chosen_content = (String) chosen_article.get("CONTENT");
//        setting.setCortexData(chosen_content);
//        String requestBody = setting.getData();
//
////        System.out.println("requestBody = " + requestBody);
//
//        con.setDoOutput(true);
//        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//        wr.write(requestBody.getBytes("UTF-8"));
//        wr.flush();
//        wr.close();
//
//        int responseCode = con.getResponseCode();
//        BufferedReader bufferedReader;
//        if (responseCode == 200) {
//            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        } else {
//            bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            System.out.println(responseCode);
//        }
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        System.out.println(sb);
//
//        return "";
//
//    }
//
//
//    public static String getShortSummary(String summary){
//        summary = summary.replace("\n", "");
//        StringBuilder message = new StringBuilder();
//        message.append(setting.getChatBotMessage()).append(summary);
////        System.out.println("message = " + message);
//        return ChatGPT.chatGPT(message.toString());
//    }
//
////    public static String makeSignature(String message, String secretKey) {
////
////        String signatureHeader = "";
////
////        try {
////            byte[] secrete_key_bytes = secretKey.getBytes(setting.getCharSet());
////
////            SecretKeySpec secretKeySpec = new SecretKeySpec(secrete_key_bytes, setting.getChatBotKeyAlgorithm());
////            Mac mac = Mac.getInstance(setting.getChatBotKeyAlgorithm());
////            mac.init(secretKeySpec);
////
////            byte[] signature = mac.doFinal(message.getBytes(setting.getCharSet()));
////            signatureHeader = Base64.getEncoder().encodeToString(signature);
////
////            System.out.println(signatureHeader);
////            return signatureHeader;
////
////        } catch (Exception e){
////            System.out.println(e);
////        }
////
////        return signatureHeader;
////
////    }
////
////    public static String getReqMessage(String message) {
////
////        String requestBody = "";
////
////        try {
////
////            JSONObject obj = new JSONObject();
////
////            long timestamp = new Date().getTime();
////            //System.out.println("##"+timestamp);
////
////            obj.put("version", "v2");
////            obj.put("userId", setting.getChatBotUserId());
////            obj.put("timestamp", timestamp);
////
////            JSONObject bubbles_obj = new JSONObject();
////            bubbles_obj.put("type", "text");
////
////            JSONObject data_obj = new JSONObject();
////            data_obj.put("description", message);
//////            data_obj.put("details", message);
////            bubbles_obj.put("type", "text");
////            bubbles_obj.put("data", data_obj);
////
////            JSONArray bubbles_array = new JSONArray();
////            bubbles_array.add(bubbles_obj);
////
////            obj.put("bubbles", bubbles_array);
//////            obj.put("content", bubbles_array);
////            obj.put("event", "send");
////
////            requestBody = obj.toString();
////            System.out.println(requestBody);
////
////        } catch (Exception e){
////            System.out.println("## Exception : " + e);
////        }
////        return requestBody;
////    }
//    public static String getSummary(JSONObject chosen_article, boolean isArticle) throws IOException, ParseException {
//
//        String chosen_title = (String) chosen_article.get("TITLE");
//        String chosen_content = (String) chosen_article.get("CONTENT");
//
//        //gen ai와 연동해서 답변을 가져오면 됨!
//        //summary
//        String answer = "";
//
//        URL clova_url = new URL(setting.getClovaSummaryApiUrl());
//        HttpURLConnection con = (HttpURLConnection) clova_url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Accept", setting.getContentType());
//        con.setRequestProperty("Content-Type", setting.getContentType());
//        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", setting.getClientId());
//        con.setRequestProperty("X-NCP-APIGW-API-KEY", setting.getClientSecretKey());
//
//        setting.setModel(isArticle);
//        setting.setData(chosen_title, chosen_content);
//        String requestBody = setting.getData();
//        System.out.println("requestBody = " + requestBody);
//
//        con.setDoOutput(true);
//        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//        wr.write(requestBody.getBytes("UTF-8"));
//        wr.flush();
//        wr.close();
//
//        int responseCode = con.getResponseCode();
//        BufferedReader bufferedReader;
//
//        if(responseCode==200) {
//            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String decodedString;
//            while ((decodedString = bufferedReader.readLine()) != null) {
//                answer = decodedString;
//            }
//            bufferedReader.close();
//
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObject = (JSONObject) jsonParser.parse(answer);
//            answer = (String) jsonObject.get("summary");
//
//            return answer;
//
//        } else {
//            bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            System.out.println(responseCode);
//
//            return null;
//        }
//    }
//
//    public static List<Integer> getDataTypeNum(List<List<String>> apiList, String today_date) throws java.text.ParseException {
//
//        int count_updated = 0;
//        int idx = 0;
//        List<Integer> updated_idxList = new ArrayList<>();
//        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        //Date today_date = simpleDateFormat.parse(today);
//
//        //api : 이름, 최종수정일자, 출처(표시), 간단설명, 서비스URL, 명세서URL
//
//        for (List<String> api : apiList) {
//
//            String updated_date = api.get(1);
////            System.out.println(updated_date + ": " + api.get(0));
//
//            if (updated_date != null) {
//
//                //Date updated_date = simpleDateFormat.parse(temp);
//
//                if (updated_date.equals(today_date)) {
//
//                    if (api.get(0).equals("보도자료")){
//                        setting.setArticleTrue(true);
//                    }
//                    else {
//                        updated_idxList.add(idx);
//                        count_updated++;
//                    }
//                }
//            }
//
//            idx ++;
//        }
//
//        return updated_idxList;
//    }
//
//}
