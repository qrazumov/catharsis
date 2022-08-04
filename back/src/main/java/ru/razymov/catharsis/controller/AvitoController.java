package ru.razymov.catharsis.controller;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.razymov.catharsis.entity.AvitoEntity;
import ru.razymov.catharsis.entity.CategoryEntity;
import ru.razymov.catharsis.repo.AvitoRepository;
import ru.razymov.catharsis.repo.AvitoRepositoryImpl;
import ru.razymov.catharsis.service.AvitoService;
import ru.razymov.catharsis.service.CategoryService;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/avito")
public class AvitoController {

    @Autowired
    AvitoRepository avitoRepository;

    @SneakyThrows
    @GetMapping
    Iterable<AvitoEntity> findAll() {
        return avitoRepository.findAllByOrderByIdDesc();
    }

}