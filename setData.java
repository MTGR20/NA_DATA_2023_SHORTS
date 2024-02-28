import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class setData {

    public static void main(String[] args) throws IOException, ParseException {

        //2년치 데이터 확보
        LocalDate localDate = LocalDate.now();
        LocalDate lastYear = localDate.minusYears(2);

        while (!localDate.isEqual(lastYear)){

            String date = localDate.toString();

            StringBuilder write_date = new StringBuilder();
            String value = write_date.append("&WRITE_DATE='").append(date).append("'").toString();
            setting.setValue(value, true);

            URL api_url = new URL(setting.getUrlWithType(true));
            Object obj = readData.readArticle(api_url);
            if (obj == null) {
                System.out.println("해당하는 데이터가 없습니다.");
                localDate = localDate.minusDays(1);
                continue;
            }

            List<JSONObject> articleList = (List<JSONObject>) obj;
            int list_size = articleList.size();
            int chosen_idx = 0;
            String title, content, wrt;

            while (chosen_idx < list_size) {

                JSONObject chosen_article = articleList.get(chosen_idx);

                String path = setting.getMyPath(false) + date + "-" + chosen_idx + ".txt";
                title = (String) chosen_article.get("TITLE");
                content = (String) chosen_article.get("CONTENT");

                if (content.startsWith("https://")){
                    URL url = new URL(content);
                    content = getContent(url);
                }

                wrt = title + "\n" + content;

//            String pathPdf = setting.getMyPath() + date + "-" + chosen_idx + ".pdf";
//            Document document = new Document();
//            Page page = document.getPages().add();
//            page.getParagraphs().add(new TextFragment(wrt);
//            document.save(pathPdf);

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, false));
                bufferedWriter.write(wrt);
                bufferedWriter.newLine();
                bufferedWriter.close();

                chosen_idx++;
            }

//            System.out.println(list_size + "개의 기사 업데이트\n");
            localDate = localDate.minusDays(1);

        }

    }

    public static String getContent(URL url) throws IOException, ParseException {

        Document doc = Jsoup.connect(String.valueOf(url)).get();

        String viewText = doc.getElementsByClass("viewTxt").text();

        return viewText;
    }


}
