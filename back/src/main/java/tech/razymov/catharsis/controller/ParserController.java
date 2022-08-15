package tech.razymov.catharsis.controller;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.razymov.catharsis.dto.ParserRequest;
import tech.razymov.catharsis.entity.ParserEntity;
import tech.razymov.catharsis.repo.ParserRepository;
import tech.razymov.catharsis.service.ParserService;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/parser")
public class ParserController {

    @Autowired
    ParserRepository parserRepository;
    @Autowired
    ParserService parserService;

    @SneakyThrows
    @GetMapping
    Iterable<ParserEntity> findAll() {
        return parserRepository.findAllByOrderByIdDesc();
    }

    @PostMapping(consumes = "application/json")
    Object postParser(@RequestBody ParserRequest parser) throws IOException{
        return parserService.postParser(parser);
    }


}