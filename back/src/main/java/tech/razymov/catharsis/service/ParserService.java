package tech.razymov.catharsis.service;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.entity.ParserEntity;
import tech.razymov.catharsis.repo.ParserRepositoryImpl;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;


@Service
public class ParserService {

    @Autowired
    ParserRepositoryImpl parserRepository;

    @Scheduled(fixedDelay = 43200000)
    public void getMedianPrice() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        System.out.println("parsing start...");

        ArrayList<Double> r = new ArrayList<>();
        Double result = null;
        final String URL = "https://auto.ru/cars/vaz/granta/2015-year/7684102/all/";
        final String XPATH = "//*[@class='Link ListingItemPrice__link']";
        final String GOOD = "granta";
        final int DEEP = 3;

        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, null, new SecureRandom());

        try {
            for (int i = 1; i <= DEEP; i++) {
                String url = URL + "&page=" + i;
                Thread.sleep(this.rnd(200, 500));
                Document doc = Jsoup.connect(URL)
                        .header("user-agent",
                                "Mozilla/4.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                        .header("accept-encoding", "gzip, deflate")
                        .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en")
                        .header("cookie", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.8")
                        .header("cache-control", "max-age=1")
                        .header("cache-control", "max-age=1")
                        .header("Sec-Fetch-Dest", "document")
                        .header("Host", "auto.ru")
                        .header("Cookie", "suid=74f5fc3024d1799f330ee537ee3d4e3f.da4f5992205c087961efed5cfd6de729; yandexuid=7665565041641840082; my=YwA%3D; _csrf_token=dc513ddf2293bb45361a19f6d5113547100ababf2231d6fd; autoru_sid=a%3Ag62e95fe22i59g1idnea05842o0flavh.6d9c61f17f258d2fb2a13c06b35dba87%7C1659461602131.604800.WUrJA3oaxGt9Gq_9WXALeA.aR4j0ndULHtBoEjSK0Jj7HqaQDoiEhml08sWt3IhUGo; autoruuid=g62e95fe22i59g1idnea05842o0flavh.6d9c61f17f258d2fb2a13c06b35dba87; from=direct; yuidlt=1; crookie=1GZ/EUMBE2pwPgf0c8qnSp0y6UC8Lc/ZwN6JnEEKxq/BzyZTHMjt6r3ZmhC5gVOCjA3VzF2G17WJLOd3YpIGLFOfMog=; cmtchd=MTY1OTQ2MTYwNTQ4NA==; gdpr=0; _ym_uid=1659461608386258863; Session_id=3:1659461607.5.0.1653069435886:R-vAsA:7a.1.2:1|736358018.0.2|61:10006362.34256.15rIGXYSPOHoWQij3y8agRgsFWE; yandex_login=qrazumov; ys=wprid.1657180325048897-10554661654222567995-vla1-5375-vla-l7-balancer-8080-BAL-8273#udn.cDpxcmF6dW1vdg%3D%3D#c_chck.1099440322; i=d5QyU4g6HP7zYds9ow0yB4SX3ZgBnvEUZXYgER6uLFpVF1oEBmi+SOx/cfskdMiq5IBJkPHqroY/sCBmIl1RJsdBbIk=; mda2_beacon=1659461607234; sso_status=sso.passport.yandex.ru:synchronized; _ym_isad=1; los=1; bltsr=1; _yasc=NWWS7pRv8WsEMDafz3fL03mDU+L80EGJGDLjvhOl3NpO/tiY; from_lifetime=1659464998177; _ym_d=1659464998")
                        .sslSocketFactory(sc.getSocketFactory())
                        .get();
                doc.selectXpath(XPATH).forEach(j -> {
                    double price = NumberUtils.toDouble(j.text().replaceAll("[^\\d]", ""));
                    if(price != 0)
                        r.add(price);
                });;
            }

            Median median = new Median();
            double[] arr = r.stream().filter(k -> k > 0).mapToDouble(d -> d).toArray();
            result = median.evaluate(arr);

        }
        catch (Exception e) {
            System.out.println("parse error:" + e.getMessage());
            this.getMedianPrice();

        }
        System.out.println("median price: " + result);

        if(!Double.isNaN(result)){
            var parserEntity = new ParserEntity();
            parserEntity.setPrice(result);
            parserEntity.setGood(GOOD);
            parserEntity.setQuery(null);
            parserRepository.save(parserEntity);
        }


    }

    private int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}
