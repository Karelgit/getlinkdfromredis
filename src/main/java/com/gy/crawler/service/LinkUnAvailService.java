package com.gy.crawler.service;

import com.gy.crawler.model.External;
import com.gy.crawler.model.ExternalRS;
import com.gy.crawler.utils.ExportXLS;
import com.gy.crawler.utils.JSONUtil;
import com.gy.crawler.utils.PropertyHelper;
import com.yeezhao.guizhou.client.SpellCheckerClient;
import jxl.write.WriteException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadoop on 2015/7/21.
 */

@Service("LinkUnAvailService")
public class LinkUnAvailService {
    private PropertyHelper propertyHelper;
    private JedisPool jedisPool;
    private Configuration hbConfig;
    private SpellCheckerClient client;
    private HTable table;

    public void init(String tablename) {
        propertyHelper = new PropertyHelper("redisconf");
        hbConfig = new Configuration();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(propertyHelper.getValue("MAXTOTAL")));
        config.setMaxIdle(Integer.valueOf(propertyHelper.getValue("IDLE")));
        config.setMaxWaitMillis(Integer.valueOf(propertyHelper.getValue("MAXWAIT")));
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, propertyHelper.getValue("IP"), Integer.valueOf(propertyHelper.getValue("PORT")));
        hbConfig.addResource("hbase-site.xml");
//        hbConfig = HBaseConfiguration.create(hbConfig);
        client = new SpellCheckerClient();
    }

    public String getLinkUnAvail(String taskid) {
        String rs = "";
        Jedis jedis = null;


        List<External> externals = new ArrayList<External>();
        ExternalRS externalRS = new ExternalRS();
        externalRS.setResult(false);
        externalRS.setSize(0);
        externalRS.setData(externals);
        jedis = jedisPool.getResource();
        Set<String> keys = null;
        try {
            jedis.select(4);
            keys = jedis.keys("commons:crawler:external:" + taskid + ":?*");
            Iterator<String> iter = keys.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                External external = new External();
                external.setUrl("http" + key.substring(key.lastIndexOf(":"), key.length()));
                Map<String, String> redismap = jedis.hgetAll(key);
                external.setFromUrl(redismap.get("fromUrl"));
                external.setRootUrl(redismap.get("rootUrl"));
                external.setStatusCode(Integer.valueOf(redismap.get("statusCode")));
                external.setTitle(redismap.get("anchor"));
                externals.add(external);
            }

            externalRS.setSize(keys.size());
            externalRS.setData(externals);
            externalRS.setResult(true);

            rs = JSONUtil.object2JacksonString(externalRS);
        } catch (Exception e) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
            return rs;
        }
    }

    public void hbaseToRedis(String tableName) {
        Jedis jedis = null;
        ResultScanner rs = null;
        try {
            table = new HTable(hbConfig, tableName);
            Scan scan = new Scan();
            scan.setCaching(50);
            scan.addColumn(Bytes.toBytes("url"), Bytes.toBytes("text"));

            rs = table.getScanner(scan);
            jedis = jedisPool.getResource();
            jedis.select(9);

            for (Result r : rs) {
                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("url")));
                String text = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("text")));

                String errorWords = client.query(text);

                jedis.hset(tableName + "_errorwords", url, errorWords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void batchFromRedis(String tableName) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(9);

        Map<String, String> errorWordsMap = new HashMap<String, String>();
        errorWordsMap = jedis.hgetAll(tableName + "_errorwords");
        ExportXLS exportXlS = new ExportXLS();
        int i = 0;
        for (Map.Entry<String, String> entry : errorWordsMap.entrySet()) {
            try {
                exportXlS.createExcel(tableName, i, entry.getKey(), entry.getValue());
                i++;
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void validTest() {
        System.out.println("test valid!");
    }

    //test
    public static void main(String[] args) {
        System.out.println("tableName" + "_errorwords" +
                new SimpleDateFormat("YYYYmmdd-HHmmss").format(new Date()));
    }

}
