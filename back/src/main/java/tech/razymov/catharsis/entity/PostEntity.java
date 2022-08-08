package tech.razymov.catharsis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "posts")
@Data
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnoreProperties({"posts"})
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    private String name;
    private String text;
    private String video;
    private Date createdAt;
    private String img;

}
