package tech.razymov.catharsis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.dto.Parser;
import tech.razymov.catharsis.entity.ParserEntity;
import tech.razymov.catharsis.repo.ParserRepositoryImpl;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.regex.Pattern;


@Service
public class ParserService {

    @Autowired
    ParserRepositoryImpl parserRepository;

    @Scheduled(fixedDelay = 43200000)
    public void getMedianPrice() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        final String URL = "https://auto.ru/cars/vaz/granta/2015-year/7684102/all/";
        final String GOOD = "granta";
        Double result = this.parser(URL);

        if(!Double.isNaN(result)){
            var parserEntity = new ParserEntity();
            parserEntity.setPrice(result);
            parserEntity.setGood(GOOD);
            parserEntity.setQuery(null);
            parserRepository.save(parserEntity);
        }

    }

    public Double postParser(Parser parser) throws NoSuchAlgorithmException, KeyManagementException, IOException {

        return this.parser(parser.getParserurl());

    }

    private Double parser(String url) throws NoSuchAlgorithmException, KeyManagementException, IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");

        System.out.println("parsing start...");
        ArrayList<Double> r = new ArrayList<>();
        Double result = null;
        final String XPATH = "//*[@class='Link ListingItemPrice__link']";
        final int DEEP = 3;

        try {
            for (int i = 1; i <= DEEP; i++) {

                String strUrl = Pattern.matches(".+\\?.*", url)
                        ? url + "&page=" + i
                        : url + "?page=" + i;

                Thread.sleep(this.rnd(200, 500));
                Request request = new Request.Builder()
                        .url(strUrl)
                        .addHeader("Cookie", "_csrf_token=7a3910896f75656839c1c15471681d8a6c50011397dc06b0; autoru_sid=a%3Ag62ed67762e4s1ptabki836tr5svcgfd.ec8067a9b6fb8339e4c568c20d4c7e7e%7C1659725686538.604800.mi9wGGzW42s4gU6qfzzqlA.ChkAyoS6jhDBFomMP28CxjFZejUeu-jHV7jmTM3lby8; autoruuid=g62ed67762e4s1ptabki836tr5svcgfd.ec8067a9b6fb8339e4c568c20d4c7e7e; suid=1e12e09e6a04707122388ef5c70db62c.5ef079db81de85eac64c2ee21db472d4; from=direct; _yasc=w1Th9qi6i4VTCzVWGaXOcb5uRwUsVIl40X4BkKXdyc1MhL1j; yuidlt=1; yandexuid=4007520841659725686; gdpr=0; _ym_uid=1659725689346542657; _ym_visorc=b; spravka=dD0xNjU5NzI1NzAwO2k9MTc2LjE5Mi4yMzUuNzE7RD02RTJFMjMwRDVEN0ZENTdGN0E5MTMzNzE0RjY3ODVDMUIyNUM4NjY3NTBGMjVCQTIyRjkzQUZFRjgyQkU5NEFDOTU3MjUwQTg7dT0xNjU5NzI1NzAwNjc3MDMzOTM0O2g9NzM2OTkwMGQwMDkwOGI3MmMyNjQ2ZDIxY2Y5ODM1MzI=; sso_status=sso.passport.yandex.ru:synchronized_no_beacon; _ym_isad=2; from_lifetime=1659725719783; _ym_d=1659725719; cycada=Nr8bqWjRgreuG9rVndoXM7GAgdnVPzztRIcNjrObkiE=; _yasc=unmBRxl+UYMvuhDeprjWYVaNAlL+5VHzgD1+ldnf17EzZd/J; _ym_d=1659726211; _ym_uid=1659725689346542657; from=direct; from_lifetime=1659726211867; spravka=dD0xNjI4MTg5OTIwO2k9MTc2LjE5Mi4yMzUuNzE7RD1DOUFCRjgxQzhERkFFRjU2NDJENzhERjUzQTJDOUY1OTJGQTNENzRERkUxREU1RjFFNjBDQ0VBRTZFNDJBMjZDNTczRTQ1RTk7dT0xNjI4MTg5OTIwMTI2OTQ0Nzc0O2g9MzJhYzdkYTJlMjFlMmJkMTQ0YjExN2EwODU1MzViNWM=")
                        .build();
                Response response = client.newCall(request).execute();
                String o = response.body().string();
                Document doc = Jsoup.parse(o);

                doc.selectXpath(XPATH).forEach(j -> {
                    double price = NumberUtils.toDouble(j.text().replaceAll("[^\\d]", ""));
                    if(price != 0)
                        r.add(price);
                });;
            }

            Median median = new Median();
            double[] arr = r.stream().filter(k -> k > 0).mapToDouble(d -> d).toArray();
            result = median.evaluate(arr);
            if(Double.isNaN(result)){
                throw new RuntimeException("parsing error");
            }

        }
        catch (Exception e) {
            System.out.println("parse error:" + e.getMessage());
            return 0.0;
        }
        System.out.println("median price: " + result);
        return result;

    }

    private int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}
