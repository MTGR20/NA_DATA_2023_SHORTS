import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class readData {

    public static final String REGEXP_PATTERN_SPACE_CHAR = "^[\\s]*$";
    public static final String REGEXP_PATTERN_HTML = "<[^>]*>";
    public static final String REGEXP_PATTERN_AMP_SEMICOLON = "&[^;];";
    private static final String INF = "99999999999";

    public static String connectURL(URL url, String method) throws IOException {

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        int responseCode = con.getResponseCode();

        BufferedReader bufferedReader;
        if (responseCode == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            System.out.println(responseCode);
        }

        return bufferedReader.readLine();
    }

    public static List<List<String>> readOpenSrvApi() throws IOException, ParseException {

        setting.setApiUrl(setting.getOpenSrvApi());
        URL url = new URL(setting.getUrlWithType(true));

        List<List<String>> apiList = new ArrayList<>();
        String result = connectURL(url, "GET");

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

        JSONArray jsonArray = jsonObject2Array(jsonObject);

        long list_total_count = (long) ((JSONObject) (((JSONArray) ((JSONObject) jsonArray.get(0)).get("head")).get(0))).get("list_total_count");

        jsonArray = (JSONArray) ((JSONObject) jsonArray.get(1)).get("row");

        //이름, 최종수정일자, 출처(표시), 간단설명, 서비스URL, 명세서URL
        final String[] keys = {"INF_NM", "LOAD_DTTM", "ORG_NM", "INF_EXP", "SRV_URL", "DDC_URL"};

        for (Object temp : jsonArray){
            List<String> value = new ArrayList<>();
            Iterator<String> stringIterator = Arrays.stream(keys).iterator();
            while (stringIterator.hasNext()){
                String str = stringIterator.next();
                value.add((String) ((JSONObject) temp).get(str));
            }
            apiList.add(value);
        }

        return apiList;
    }


    public static Object readArticle(URL url) throws IOException, ParseException {

        List<JSONObject> articleList = new ArrayList<>();

        String result = connectURL(url, "GET");

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

//        System.out.println(jsonObject);
        JSONObject check200 = (JSONObject) jsonObject.get("RESULT");
        if (check200 != null && (check200.get("CODE").toString().equals("INFO-200"))) {
            return null;
        }
        JSONArray value = jsonObject2Array(jsonObject);
        //JSONObject head = (JSONObject) value.get(0);
        //List<Map<String, String>> headMapList = jsonArray2MapList(jsonObject2Array(head));
        JSONObject row = (JSONObject) value.get(1);
        JSONArray rowArray = jsonObject2Array(row);
        //System.out.println(rowArray);

        int rowSize = rowArray.size();

        for (int i = 0; i < rowSize; i++) {

            JSONObject rowObject = (JSONObject) rowArray.get(i);
            String title = processSpecialSymbols((String) rowObject.get("TITLE"));
            String date = (String) rowObject.get("WRITE_DATE");
            String content = (String) rowObject.get("CONTENT");
            StringBuilder stringBuilder = new StringBuilder();

//            System.out.println(rowObject);

            if (content != null) {
                content = processSpecialSymbols(content);
                content = content.replaceAll(REGEXP_PATTERN_HTML, "");
                content = content.replace("\"", "'");
            }
            else{
                URL contentUrl = new URL ((String) rowObject.get("CONTENT_URL"));
                // buffer read 안되는 경우 ...
                // 일단은 url만 리턴하도록 함
                // 추후 이 Url 다시 다른 방법으로 읽어들여 내용 가져오기
                content = contentUrl.toString();
            }

            JSONObject temp = new JSONObject();
            temp.put("TITLE", title);
            temp.put("CONTENT", content);
            articleList.add(temp);

        }

        return articleList;
    }

    public static void main(String[] args) throws IOException, ParseException {
        //System.out.println("readData.main");
    }

//
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        int responseCode = con.getResponseCode();
//
//        BufferedReader bufferedReader;
//        if (responseCode == 200) {
//            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        } else {
//            bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            System.out.println(responseCode);
//        }
//        String result = bufferedReader.readLine();
//
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
//
//        JSONArray value = jsonObject2Array(jsonObject);
//        //JSONObject head = (JSONObject) value.get(0);
//        //List<Map<String, String>> headMapList = jsonArray2MapList(jsonObject2Array(head));
//        JSONObject row = (JSONObject) value.get(1);
//        JSONArray rowArray = jsonObject2Array(row);
//
//        int rowSize = rowArray.size();
//        for (int i = 0; i < rowSize; i++) {
//            JSONObject rowObject = (JSONObject) rowArray.get(i);
//            String title = processSpecialSymbols((String) rowObject.get("TITLE"));
//            System.out.println("\nTITLE: " + title);
//            String content = (String) rowObject.get("CONTENT");
//
//            if (content != null) {
//                content = processSpecialSymbols(content);
//                content = content.replaceAll(REGEXP_PATTERN_HTML, "");
//                System.out.println(content);
//            }
//            else{
//                URL contentUrl = new URL ((String) rowObject.get("CONTENT_URL"));
//
//            }
//        }


    public static String processSpecialSymbols(String content){
        return  content.replace("&amp;", "&")
                .replace("&amp;", "&").replace("&hellip;", "...")
                .replace("&nbsp;", " ")
                .replace("&lt;", "<").replace("&gt;", ">")
                .replace("&lsquo;", "'").replace("&rsquo;", "'")
                .replace("&ldquo;", "\"").replace("&rdquo;", "\"")
                .replace("&middot;", "/").replace("&quot;", "\"")
                .replaceAll(REGEXP_PATTERN_AMP_SEMICOLON, "");
    }

    public static List<Integer> findIndexListOf(String content, String toFind){
        List<Integer> indexList = new ArrayList<Integer>();
        int idx = content.indexOf(toFind);

        while (idx >= 0){
            indexList.add(idx);
            idx = content.indexOf(toFind, idx + toFind.length());
        }
        return indexList;
    }

    public static String deleteFromA(String content, String A, boolean deleteA){
        int idx = content.indexOf(A);
        if (idx >= 0){
            if (deleteA) {
                content = content.substring(0, idx-1);
            }
            else{
                content = content.substring(0, idx + A.length()-1);
            }
        }
        return content;
    }

    public static String deleteFromAtoB(String content, int idxA, int idxB){
        StringBuffer data = new StringBuffer(content);
        if (idxA >= 0 && idxB >= 0 && idxA < idxB) {
            data.delete(idxA, idxB);
        }
        return data.toString();
    }

//    public static String deleteFromAtoB(String content, String A, String B){
//        StringBuffer data = new StringBuffer(content);
//        int idxA = content.indexOf(A);
//        int idxB = content.indexOf(B);
//        if (idxA >= 0 && idxB >= 0 && idxA < idxB) {
//            data.delete(idxA, idxB + B.length());
//        }
//        return data.toString();
//    }

    public static String deleteUnnecessaryData(String content){
        String[] list = {"lang=\"EN-US\"", "font-family: &amp;quot;맑은 고딕&amp;quot;;", "font-weight: bold;",
                "font-size: 11pt;", "line-height:180%;", "text-align:center;", "mso-pagination:none;",
                "text-autospace:none;", "list-style: none;", "font-size: medium;", "background-color: rgb(255, 255, 255);",
                "font-family: NotoSans, sans-serif;", "word-break:keep-all;",
                "tab-stops:left blank 306.6pt;", "mso-padding-alt:0pt 0pt 0pt 0pt;",
                "letter-spacing: -0.4pt;", "letter-spacing: 0pt;",
                "&amp;", "nbsp;"
        };

        content = deleteFromAtoB(content, "!--[if !supportEmptyParas]-->", "<!--[endif]-->", -1);
        content = deleteFromAtoB(content, "style=\"", "\">", 1);

        for (String str : list){
            content = content.replace(str, "");
        }

//        content = content.replaceAll("<[^>]*>", "");

        return content;
    }

    public static String deleteFromAtoB(String content, String strA, String strB, int end){
        int lenA = strA.length();
        int lenB = strB.length();

        // end
        // -1이면 strB.length()까지 지움.
        // 0이면 strB 살림.
        // 1보다 크거나 같으면 strB + 해당 값만큼까지 지움.
        if (end < 0) end = lenB;

        //A B A B A B 일 경우
        //A 먼저 찾고
        int idxA = content.indexOf(strA);

        while (idxA >= 0) {

            // B를 찾고
            int idxB = content.indexOf(strB);
            if (idxB < 0) break;

            // 앞에서 부터 A B 세트인지 확인한 후
            while (idxA > idxB) {
                idxB = content.indexOf(strB, idxB + lenB);
            }

            // 세트를 지운 다음에
            StringBuffer data = new StringBuffer(content);
            content = data.delete(idxA, idxB + end).toString();

            // 다음 문자열부터 다시 A 를 찾아감
            idxA = content.indexOf(strA, idxA + lenA);
        }

        return content;
    }

    public static String getArticleFromRowObject(JSONObject rowObject) throws ParseException {

        String content = (String) rowObject.get("CONTENT");
        if (content == null){
            return null;
        }

        content = content.replace("&lt;", "<").replace("&gt;", ">")
                .replace("&ldquo;", "LQ").replace("&rdquo;", "RQ").replace("&middot;", "-").replace("&quot;", "\"")
                .replace("</span>", "");


        StringBuilder processed_data = new StringBuilder();
        processed_data.append("{");
        StringTokenizer stringTokenizer = new StringTokenizer(content, "\n", true);
        while (stringTokenizer.hasMoreTokens()) {
            String temp = stringTokenizer.nextToken();
            if (!temp.contains("<") && !temp.replace(" ", "").equals("")) {
                processed_data.append(temp);
            }
        }
        processed_data.append("}");

        JSONParser jsonParser = new JSONParser();
        JSONObject prObject = (JSONObject) jsonParser.parse(String.valueOf(processed_data));
        JSONObject roObject = (JSONObject) prObject.get("ro");

        //printJsonObject(roObject);

        JSONArray processed_objects = new JSONArray();

        for (Object key: roObject.keySet()) {
            if (roObject.get(key).getClass().getName().equals(JSONObject.class.getName())){
                JSONObject tempObject = (JSONObject) roObject.get(key);
                String pp = (String) tempObject.get("pp");
                pp = pp.substring(pp.length()-2);
                String np = (String) tempObject.get("np");
                if (np == null || Pattern.matches(REGEXP_PATTERN_SPACE_CHAR , np)) {
                    np = INF;
                }
                JSONArray tempArray = (JSONArray) tempObject.get("ru");
                tempObject = (JSONObject) tempArray.get(tempArray.size()-1);
                tempArray = (JSONArray) tempObject.get("ch");
                tempObject = (JSONObject) tempArray.get(tempArray.size()-1);

                JSONObject processed_object = new JSONObject();
                processed_object.put("pp", pp);
                processed_object.put("np", np);
                processed_object.put("text", tempObject.get("t"));
                processed_objects.add(processed_object);
            }
        }

        Collections.sort(processed_objects, new Comparator<JSONObject>(){
            @Override
            public int compare(JSONObject object1, JSONObject object2){
                final String key = "np";
                String str1 = "", str2 = "";
                try {
                    str1 = (String) object1.get(key);
                    str2 = (String) object2.get(key);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return str1.compareTo(str2);
            }
        });

        StringBuilder stringBuilder = new StringBuilder();

        for (int i=0; i< processed_objects.size(); i++){
            JSONObject tempObject = (JSONObject) processed_objects.get(i);
            String text = (String) tempObject.get("text");
            if (text != null && !Pattern.matches(REGEXP_PATTERN_SPACE_CHAR, text)) {
                stringBuilder.append(text);
            }
        }

        String article = stringBuilder.toString().replace("RQ", "\"").replace("LQ", "\"")
                .replace("-  ", "\n-").replace("\" -", "\"-\n").replace("\n\n", "\n")
                .replace(".", ".\n").replace("<끝>", "\n");

        //System.out.println(article);

        //printJsonArray(processed_objects);
        //4D 기사
        //4E 세모
        //4C 추가 (-)
        //4B 소제목 (요약)

        return article;
    }


    private static void printJsonArray(JSONArray jsonArray) {
        int size = jsonArray.size();
        for (int i = 0; i<size; i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            printJsonObject(jsonObject);
        }
    }

    public static void printJsonObject(JSONObject jsonObject){
        for (Object key: jsonObject.keySet()){
            System.out.println(key + " : " + jsonObject.get(key));
        }
        System.out.println();
    }

    //참고
    //동일한 기능의 함수로 split()이 있음...
    public static List<String> separateStringBy(String content, String sep){

        List<String> result = new ArrayList<>();

        int idx;
        String substring_front = "";
        String substring_rear = content;

        while ((idx = substring_rear.indexOf(sep)) >= 0){
            substring_front = substring_rear.substring(0, idx);
            substring_rear = substring_rear.substring(idx+1);

            result.add(substring_front);
        }

        return result;
    }


    public static List<Map<String, JSONObject>> jsonArray2KeyJsonObjectMapList(JSONArray jsonArray){

        int size = jsonArray.size();

        List<Map<String, JSONObject>> keyValueMapList = new ArrayList<>();

        for (int i=0; i<size; i++){
            keyValueMapList.add(jsonObject2StringJsonObjectMap((JSONObject) jsonArray.get(i)));
        }

        return keyValueMapList;
    }

    public static Map<String, JSONObject> jsonObject2StringJsonObjectMap(JSONObject jsonObject){

        Map<String, JSONObject> keyValueMap = new HashMap<>();

        Iterator keyIter = jsonObject.keySet().iterator();
        String key = keyIter.next().toString();

        JSONObject value = (JSONObject) jsonObject.get(key);
        keyValueMap.put(key, value);

        return keyValueMap;
    }
    public static Map<String, String> jsonObject2StringStringMap(JSONObject jsonObject){

        Map<String, String> keyValueMap = new HashMap<>();

        Iterator keyIter = jsonObject.keySet().iterator();
        String key = keyIter.next().toString();

        String value = jsonObject.get(key).toString();
        keyValueMap.put(key, value);

        return keyValueMap;
    }

    public static JSONArray jsonObject2Array(JSONObject jsonObject){

        int objectSize = jsonObject.size();

        String key = "";
        JSONArray value = null;

        Iterator<String> keys = jsonObject.keySet().iterator();
        for (int i=0; i<objectSize; i++){
            key = keys.next();
            value = (JSONArray) jsonObject.get(key);
        }

        return value;
    }

}
