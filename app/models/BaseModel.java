package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties({"id","_ebean_intercept", "_$gestor_recetas"})
public class BaseModel extends Model {
    @Id
    public Long id;

    public Long getId() {
        return id;
    }
}
