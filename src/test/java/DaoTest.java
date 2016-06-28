
import com.gy.crawler.service.LinkUnAvailService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

/**
 * Created by hadoop on 2015/7/21.
 */
public class DaoTest {

    LinkUnAvailService linkUnAvailService;

    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        linkUnAvailService = (LinkUnAvailService) context.getBean("LinkUnAvailService");
    }


    @Test
    public void test() {
      /*  HttpClientBuilder builder = HttpClientBuilder.create();

        HttpClient client = builder.build();

        String uri = "http://127.0.0.1:9090/rs/api/linkunavail?tid=c79e32e275abc8368eb9a2c0fa8e4de4";

        HttpGet post = new HttpGet(uri);


        try {
            HttpResponse response = client.execute(post);
            System.err.println(new String(EntityUtils.toString(response.getEntity()).getBytes("iso8859-1"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Jedis jedis = new Jedis("172.16.100.70", 6379);
        jedis.select(14);
        /*Set<String> keys=jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }*/
        System.out.println(jedis.lindex("err:editdist:习近平", 1));

    }

    @Test
    public void testExportFromRedis() {
        String tableName = "www.gygov.com";
        linkUnAvailService.init(tableName);
        linkUnAvailService.batchFromRedis(tableName);
    }

}
