package tech.razymov.catharsis.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParserResponse {
    private Result result;
    private String error;

    @Data
    public static class Result{
        private String title;
        private List<Integer> all;
        private int cnt;
        private int median;
        private int avg;
        private int min;
        private int max;
    }
}
