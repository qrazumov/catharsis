package tech.razymov.catharsis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.dto.ParserRequest;
import tech.razymov.catharsis.dto.ParserResponse;
import tech.razymov.catharsis.repo.ParserRepositoryImpl;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ParserService {

    @Autowired
    ParserRepositoryImpl parserRepository;

    @Scheduled(fixedDelay = 43200000)
    public void getMedianPrice() {


    }

    public ParserResponse postParser(ParserRequest parser) throws IOException {

        return this.parser(parser.getParserurl());

    }

    private ParserResponse parser(String url) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        String blogUrl = "http://razymov.tech:8585/?q=" + url;
        Request request = new Request.Builder().url(blogUrl).build();
        var response = client.newCall(request).execute().body();

        return mapper.readValue(response.string(), ParserResponse.class);

    }

}
