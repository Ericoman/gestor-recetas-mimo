package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties({"_ebean_intercept", "_$gestor_recetas"})
public class BaseModel extends Model {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}
