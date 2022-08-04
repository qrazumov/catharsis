package tech.razymov.catharsis.repo;

import org.springframework.stereotype.Repository;
import tech.razymov.catharsis.entity.ParserEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class ParserRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void save(ParserEntity parser) {
        entityManager.persist(parser);
    }

}
