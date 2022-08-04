package tech.razymov.catharsis.controller;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.razymov.catharsis.entity.ParserEntity;
import tech.razymov.catharsis.repo.ParserRepository;

@RestController
@RequestMapping("api/v1/parser")
public class ParserController {

    @Autowired
    ParserRepository parserRepository;

    @SneakyThrows
    @GetMapping
    Iterable<ParserEntity> findAll() {
        return parserRepository.findAllByOrderByIdDesc();
    }

}