package ru.razymov.catharsis.service;

import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.razymov.catharsis.entity.AvitoEntity;
import ru.razymov.catharsis.repo.AvitoRepositoryImpl;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;


@Service
public class AvitoService {

    @Autowired
    AvitoRepositoryImpl avitoRepository;

    @Scheduled(fixedDelay = 1000)
    public void getMedianPrice() throws IOException, NoSuchAlgorithmException, KeyManagementException {

        System.out.println("parsing start...");


        ArrayList<Double> r = new ArrayList<>();
        Double result = null;
        final String URL = "https://www.avito.ru/moskva/tovary_dlya_kompyutera/komplektuyuschie/videokarty-ASgBAgICAkTGB~pm7gmmZw?cd=1&q=gtx+1660+super";
        final String XPATH = "//*[meta]/div/div[2]/div[3]/span/span/meta[2]";
        final String GOOD = "1660super";
        final int DEEP = 3;

        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, null, new SecureRandom());

        try {

            for (int i = 1; i <= DEEP; i++) {
                String url = URL + "&p=" + i;
                Thread.sleep(this.rnd(50, 100));
                Document doc = Jsoup.connect(URL)
                        .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                        .header("accept-encoding", "gzip, deflate, br")
                        .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("cookie", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("cache-control", "max-age=0")
                        .sslSocketFactory(sc.getSocketFactory()).get();
                doc.selectXpath(XPATH).forEach(j -> {
                    String price = j.attr("content");
                    if(!price.equals("0"))
                        r.add(Double.parseDouble(price));
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

        if(result != null){
            var avitoEntity = new AvitoEntity();
            avitoEntity.setPrice(result);
            avitoEntity.setGood(GOOD);
            avitoEntity.setQuery(null);
            avitoRepository.save(avitoEntity);
        }


    }

    private int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}
