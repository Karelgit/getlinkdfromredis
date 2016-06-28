package com.gy.crawler.test;

import com.yeezhao.guizhou.client.SpellCheckerClient;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * Created by hadoop on 2015/8/10.
 */
public class HBaseTest {
    public static void main(String args[]) throws Exception {
        Configuration hbConfig = new Configuration();
        hbConfig.addResource("hbase-site.xml");
        hbConfig = HBaseConfiguration.create(hbConfig);
        HTable table = new HTable(hbConfig, "www.gygov.gov.cn");

        Scan scan = new Scan();
        ResultScanner rs = null;
        Jedis jedis = new Jedis("172.16.100.70", 6379);
        jedis.select(9);
        try {
            rs = table.getScanner(scan);
            for (Result r : rs) {


                //List<Cell> list= r.getColumnCells(Bytes.toBytes("crawl"), Bytes.toBytes("url"));
                HashMap<String, String> data = new HashMap<String, String>();

                String rowkey = Bytes.toString(r.getRow());
                String rootUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawl"), Bytes.toBytes("rootUrl")));
                String fromUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawl"), Bytes.toBytes("fromUrl")));
                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawl"), Bytes.toBytes("url")));
                String round = Bytes.toString(r.getValue(Bytes.toBytes("crawl"), Bytes.toBytes("round")));

                String content = Bytes.toString(r.getValue(Bytes.toBytes("crawl"), Bytes.toBytes("content")));
                JSONObject jsonObject = new JSONObject(content);
                String crawl_time = jsonObject.getString("crawl_time");
                data.put("rootUrl", rootUrl);
                data.put("fromUrl", fromUrl);
                data.put("round", round);
                data.put("crawlTime", crawl_time);
                if (jsonObject.has("text")) {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("text"));
                    String content2 = jsonObject1.getString("text");
                    data.put("content", content2);
                    String errorwords = new SpellCheckerClient().query(content2);
                    if (errorwords != null && !errorwords.equals("")) {
                        data.put("errorwords", errorwords);
                        jedis.hmset("commons:crawler:errorword:" + rowkey.split("|")[0] + ":" + url, data);
                    }
                }
            }
        } finally {
            rs.close();
        }
    }
}
