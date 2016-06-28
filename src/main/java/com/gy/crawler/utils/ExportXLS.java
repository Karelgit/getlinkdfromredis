package com.gy.crawler.utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/26.
 */
public class ExportXLS {
    public void createExcel(String xlsName, int index, String url, String error_words) throws WriteException, IOException {
        String path = "C:\\Users\\Administrator\\Desktop\\serve_getLink\\GetLinkDFromRedis\\data\\" + xlsName + ".xls";
        FileOutputStream fos = new FileOutputStream(path, true);
        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(fos);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        Label title_url = new Label(0, 0, "URL");
        sheet.addCell(title_url);
        Label title_error_words = new Label(1, 0, "ERROR_WORDS");
        sheet.addCell(title_error_words);


        //set row
        int row = index + 1;
        Label label_url = new Label(0, row, url);
        sheet.addCell(label_url);
        Label label_error_words = new Label(1, row, error_words);
        sheet.addCell(label_error_words);


        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        fos.close();
    }
}
