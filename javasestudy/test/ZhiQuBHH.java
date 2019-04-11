import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import poi.PoiUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static poi.PoiUtil.getCellStringVal;
import static poi.PoiUtil.getReadWorkbookType;

public class ZhiQuBHH {

    public static void main(String[] args) {
//        List<Map<String, String>> users = readExcel("/home/liuwei/Desktop/temp/未过黑名单用户名单1.xlsx");
        String url = "http://47.75.57.231:18201/micro/unigw/paipaixin/queryRisk.json";
        Map<String, String> map = new HashMap<>();
        map.put("name", "蒋正栋");
        map.put("phone", "17610768223");
        map.put("idCard", "320404199706204113");

        String result = postResultFForm(url, map);

        System.out.println(result);
        System.out.println(result.contains("\"queryStatus\":\"2\""));
        System.out.println(result.contains("\"queryStatus\":\"1\""));
//        readExcel("/home/liuwei/Desktop/temp/未过黑名单用户名单1.xlsx");

    }

    public static List<Map<String, String>> readExcel(String sourceFilePath) {
        String url = "http://47.75.57.231:18201/micro/unigw/paipaixin/queryRisk.json";

        Workbook workbook = null;

        try {
            workbook = getReadWorkbookType(sourceFilePath);

            List<Map<String, String>> contents = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(0);

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Cell name = row.getCell(0);
                Cell phone = row.getCell(1);
                Cell idCard = row.getCell(2);

                Map<String, String> map = new HashMap<>();
                map.put("name", PoiUtil.getCellStringVal(name));
                map.put("phone", PoiUtil.getCellStringVal(phone));
                map.put("idCard", PoiUtil.getCellStringVal(idCard));

                //send HTTP
                String result = postResultFForm(url, map);

                if (result.contains("\"success\":false")) {
                    rowNum--;
                    continue;
                } else {
                    System.out.print(rowNum + ":  ");
                    if (result.contains("\"queryStatus\":\"2\"")) {
                        row.createCell(5).setCellValue("否");
                    } else if (result.contains("\"queryStatus\":\"1\"")) {
                        row.createCell(5).setCellValue("是");
                    } else {
                        System.out.println("+++++++++" + result);
                    }
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(sourceFilePath)) {
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return contents;
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    public static String postResultFForm(String url, Map<String, String> paramMap) {
        Form form = Form.form();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            form.add(entry.getKey(), entry.getValue());
        }
        try {
            Content content = Request.Post(url)
                    .addHeader("cotent-type", "application/x-www-form-urlencoded")
                    .bodyForm(form.build()).execute().returnContent();
            return content.asString().replaceAll("\\\\", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
