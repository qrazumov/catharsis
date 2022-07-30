package ru.razymov.catharsis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "categories")
@Data
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnoreProperties({"category"})
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<PostEntity> posts;
    private Date createdAt;
}
