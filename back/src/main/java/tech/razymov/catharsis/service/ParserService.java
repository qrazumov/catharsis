package tech.razymov.catharsis.service;

import com.osiris.headlessbrowser.HBrowser;
import com.osiris.headlessbrowser.windows.PlaywrightWindow;
import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.Normal;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.dto.Parser;
import tech.razymov.catharsis.entity.ParserEntity;
import tech.razymov.catharsis.repo.ParserRepositoryImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
public class ParserService {

    @Autowired
    ParserRepositoryImpl parserRepository;

    @Scheduled(fixedDelay = 43200000)
    public void getMedianPrice() throws IOException {

        final String URL = "https://auto.ru/cars/vaz/granta/2015-year/7684102/all/";
        final String GOOD = "granta";
        Double result = this.parser(URL);

        if (!Double.isNaN(result)) {
            var parserEntity = new ParserEntity();
            parserEntity.setPrice(result);
            parserEntity.setGood(GOOD);
            parserEntity.setQuery(null);
            parserRepository.save(parserEntity);
        }

    }

    public Double postParser(Parser parser) throws IOException {

        return this.parser(parser.getParserurl());

    }

    private Double parser(String url) throws IOException {

        System.out.println("parsing start...");
        ArrayList<Double> resultArray = new ArrayList<>();
        Double result = null;
        final String XPATH = "//*[@class='Link ListingItemPrice__link']";
        final int DEEP = 3;
        final String X_PATH_HAS_CAPTCHA = "//*[@id=\"root\"]/div/div/form/div[2]/div/div/div[1]/input";
        final String JS_CLICK_TO_NOT_ROBOT = "document.getElementsByClassName('CheckboxCaptcha-Button')[0].click();";
        final String X_PATH_GET_CAPTCHA = "//*[@id=\"advanced-captcha-form\"]/div/div[1]/img";
        final String CAPTCHA_API_KEY = "ace6f1d17044db697ba7ba01e5fb6534";

        HBrowser b = new HBrowser();
        try (PlaywrightWindow w = b.openCustomWindow().enableJavaScript(true)
                .headless(true).makeUndetectable(true).buildPlaywrightWindow()) {

            for (int i = 1; i <= DEEP; i++) {
                String strUrl = Pattern.matches(".+\\?.*", url)
                        ? url + "&page=" + i
                        : url + "?page=" + i;

                Thread.sleep(this.rnd(100, 200));
                PlaywrightWindow window = w.load(strUrl);
                Thread.sleep(this.rnd(100, 200));

                int hasCaptcha = window.getBodyInnerHtml().selectXpath(X_PATH_HAS_CAPTCHA).size();
                if (hasCaptcha == 0) {

                    window.getBodyInnerHtml().selectXpath(XPATH).forEach(j -> {
                        double price = NumberUtils.toDouble(j.text().replaceAll("[^\\d]", ""));
                        if (price != 0) resultArray.add(price);
                    });

                } else {

                    window.executeJS(JS_CLICK_TO_NOT_ROBOT);
                    Thread.sleep(300);
                    String captchaImg = window.getBodyInnerHtml()
                            .selectXpath(X_PATH_GET_CAPTCHA).get(0).attr("src");

                    try (InputStream in = URI.create(captchaImg).toURL().openStream()) {

                        File tempFile = File.createTempFile("captcha", ".jpg");
                        tempFile.deleteOnExit();
                        FileOutputStream out = new FileOutputStream(tempFile);
                        IOUtils.copy(in, out);

                        TwoCaptcha solver = new TwoCaptcha(CAPTCHA_API_KEY);
                        Normal captcha = new Normal();
                        captcha.setFile(tempFile);

                        captcha.setMinLen(4);
                        captcha.setMaxLen(30);
                        captcha.setCaseSensitive(true);
                        captcha.setLang("ru");

                        try {
                            solver.solve(captcha);
                            System.out.println("Captcha solved: " + captcha.getCode());
                            window.executeJS("document.getElementById('xuniq-0-1').value='"
                                    + captcha.getCode() + "'");
                            return this.parser(url);

                        } catch (Exception e) {
                            System.out.println("Error occurred captcha solving: " + e.getMessage());
                            return this.parser(url);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return this.parser(url);
                    }

                }

            }

            Median median = new Median();
            double[] arr = resultArray.stream().filter(k -> k > 0).mapToDouble(d -> d).toArray();
            result = median.evaluate(arr);
            if (Double.isNaN(result)) {
                throw new RuntimeException("double is NaN");
            }

            System.out.println("median price: " + result);
            return result;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return this.parser(url);
        }

    }

    private int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}
