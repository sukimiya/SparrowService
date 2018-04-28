package io.e2x.sparrow.nest.web.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonView
@Document
public class UserDetailAddEvent {
    @JsonProperty("firstname")
    public String firstname;
    @JsonProperty("lastname")
    public String lastname;
    @JsonProperty("username")
    public String username;
    @JsonProperty("password")
    public String password;
    @JsonProperty("email")
    public String email;
    @JsonProperty("enabled")
    public Boolean enabled = true;

    @JsonProperty(value = "id",required = false,access = JsonProperty.Access.READ_ONLY)
    public String id;
    public UserDetailAddEvent(){}
    public UserDetailAddEvent(String firstname, String lastname, String username, String password, String email, Boolean enabled, String id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.id = id;
    }
}
