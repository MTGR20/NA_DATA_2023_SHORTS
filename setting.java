import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class setting {

    //TODO: PRIVATE DATA
    private static final String PATH = "your path";
    private static final String OPEN_API_KEY = "open api key";                  //OpenAPI
    private static final String CHAT_BOT_KEY = "chat gpt key";                  //ChatGPT
    private static final String CLIENT_ID = "clova Summary client id";          //CLOVA SUMMARY
    private static final String CLIENT_SECRET_KEY = "clova Summary secret key"; //CLOVA SUMMARY

    // 기타 세팅
    private static final String CHAT_BOT_USER_ID = "test01"; //CLOVA CHAT
    private static final String CLOVA_SUMMARY_API_URL = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";
    private static final String CLOVA_CHAT_BOT_API_URL = "https://ex9av8bv0e.apigw.ntruss.com/custom_chatbot/prod/";
    private static final String CHAT_BOT_END_POINT = "https://api.openai.com/v1/chat/completions"; //ChatGPT

    //데이터 종류별 API URL (fixed = 보도기사, 서비스API)
    private static final String OPEN_SRV_API = "https://open.assembly.go.kr/portal/openapi/OPENSRVAPI";
    private static final String ARTICLE = "https://open.assembly.go.kr/portal/openapi/ninnagrlaelvtzfnt";
    //private static final String PROPOSED_BILL = "https://open.assembly.go.kr/portal/openapi/nzmimeepazxkubdpn";
    private static final String[] API_URL = new String[]{OPEN_SRV_API, ARTICLE};

    //기능에 따라 변경해 사용
    //private static final String CHAT_BOT_MESSAGE = "시각 장애인에게 기사를 요약해 읽어주는 서비스에 사용할 것이라서 짧고 핵심적인 내용이 필요해. 다음 세 문장을 최대한 짧은 세 문장으로 요약해줘.\n";
    private static final String CHAT_BOT_MESSAGE = "다음 기사를 핵심 내용을 포함해 짧은 세 문장으로 요약해줘.";
    private static String CHOSEN_API_URL = ARTICLE;
    private static String VALUE = "&AGE=21";
    private static boolean ARTICLE_TRUE = false;
    private static final int SUMMARY_LENGTH = 3; //3줄 요약
    private static final String LANGUAGE = "ko";
    private static final String CONTENT_TYPE = "application/json;UTF-8";
    private static final String CHAT_BOT_KEY_ALGORITHM = "HmacSHA256";
    private static final String CHAR_SET = "UTF-8";
    private static String MODEL = "general";
    private static String DATA;

    public static String getChatBotUserId() {
        return CHAT_BOT_USER_ID;
    }

    public static String getCharSet() {
        return CHAR_SET;
    }

    public static String getChatBotKeyAlgorithm() {
        return CHAT_BOT_KEY_ALGORITHM;
    }

    public String getModel() {
        return MODEL;
    }

    public static void setModel(boolean is_news) {
        if (is_news){
            MODEL = "news";
        }
        else{
            MODEL = "general";
        }
    }

    public static void setData(String title, String content) {

        String data = "{ \"document\": " +
                "{ \"title\": \"" + title + "\", \"content\": \"" + content +"\" }, " +
                  "\"option\": { \"language\": \"" + LANGUAGE + "\", \"model\": \"" + MODEL + "\", \"summaryCount\": \"" + SUMMARY_LENGTH + "\" } }";

        DATA = data;

    }

    public static String getClovaSummaryApiUrl() {
        return CLOVA_SUMMARY_API_URL;
    }

    public static String getClovaChatBotApiUrl() {
        return CLOVA_CHAT_BOT_API_URL;
    }

    public static String getChatBotEndPoint(){
        return CHAT_BOT_END_POINT;
    }

    public static String getChatBotKey() {
        return CHAT_BOT_KEY;
    }

    public static String getData() {
        return DATA;
    }

    public static String getClientSecretKey() {
        return CLIENT_SECRET_KEY;
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getContentType() {
        return CONTENT_TYPE;
    }

    public static void setApiUrl(String api_url){
        CHOSEN_API_URL = api_url;
    }

    public static void setApiUrl(int idx){
        CHOSEN_API_URL = API_URL[idx];
    }

    public static String[] getFixedApiUrlType(){
        return API_URL;
    }

    public static int getFixedApiUrlNum(){
//        return API_URL.length;
        return 1;
    }

    public static void setValue(String value, boolean age21_setTrue){ //"&a=b"이런 식으로만 받아오기
        if (!age21_setTrue){
            VALUE = value;
        }
        else{
            VALUE = "&AGE=21" + value;
        }
    }

    public static String getUrlWithType(boolean type_json_setTrue){
        String Url = "";
        if (!type_json_setTrue){
            Url = CHOSEN_API_URL + "?KEY=" + OPEN_API_KEY + VALUE;
        }
        else {
            Url = CHOSEN_API_URL + "?KEY=" + OPEN_API_KEY + "&Type=json" + VALUE;
        }
        return Url;
    }

//    public static String getChatBotApiUrl() {
//        return CHAT_BOT_API_URL;
//    }
//
//    public static String getChatBotSecretKey() {
//        return CHAT_BOT_SECRET_KEY;
//    }

    public static String getChatBotMessage() {
        return CHAT_BOT_MESSAGE;
    }

    public static String getKey(){
        return OPEN_API_KEY;
    }

    public static String getMyPath(){
        return PATH;
    }

    public static String getArticle(){
        return ARTICLE;
    }

    public static String getChosenApiUrl(){
        return CHOSEN_API_URL;
    }

    public static String getValue(){
        return VALUE;
    }

    public static String getOpenSrvApi(){
        return OPEN_SRV_API;
    }

    public static boolean isArticleTrue() {
        return ARTICLE_TRUE;
    }

    public static void setArticleTrue(boolean articleTrue) {
        ARTICLE_TRUE = articleTrue;
    }
}
