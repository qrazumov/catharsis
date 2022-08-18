package tech.razymov.catharsis.dto;

import lombok.Data;

@Data
public class ParserResponse {
    private Result result;
    private String error;

    @Data
    public static class Result{
        private int median;
        private int avg;
        private int min;
        private int max;
    }
}
