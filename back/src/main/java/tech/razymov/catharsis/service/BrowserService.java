package tech.razymov.catharsis.service;

import com.osiris.headlessbrowser.HBrowser;
import com.osiris.headlessbrowser.exceptions.NodeJsCodeException;
import com.osiris.headlessbrowser.windows.PlaywrightWindow;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.ArrayList;

@Service
public class BrowserService {


    public BrowserService() throws NodeJsCodeException {

/*        System.out.println("BrowserService");
        ArrayList<String> str = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                str.add(this.r());
            }
        System.out.println(str);*/

    }
    private String r() throws NodeJsCodeException {

        HBrowser b = new HBrowser();
        String ss = null;
        ArrayList<String> str = new ArrayList<>();
        try (PlaywrightWindow w = b.openCustomWindow().enableJavaScript(true)
                .headless(true).makeUndetectable(true).buildPlaywrightWindow())
        {

            var x =  w.load("https://auto.ru/cars/volvo/s80/all/displacement-2400/?sort=cr_date-desc");
            Thread.sleep(1000);
            x.executeJS("document.getElementsByClassName('CheckboxCaptcha-Button')[0].click();");
            Thread.sleep(1000);
            ss = x.getBodyInnerHtml().selectXpath("//*[@id=\"advanced-captcha-form\"]/div/div[1]/img").get(0).attr("src");

            Console console = System.console();
            String username = console.readLine("Please enter user name : ");
            x.executeJS("document.getElementById('xuniq-0-1').value='" + username + "'");

        }
        catch (Exception e) {
            return this.r();
        }

        return ss;

    }

}
