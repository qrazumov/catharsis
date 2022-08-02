package ru.razymov.catharsis.repo;

import org.springframework.stereotype.Repository;
import ru.razymov.catharsis.entity.AvitoEntity;
import ru.razymov.catharsis.entity.PostEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class AvitoRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void save(AvitoEntity avito) {
        entityManager.persist(avito);
    }

}
