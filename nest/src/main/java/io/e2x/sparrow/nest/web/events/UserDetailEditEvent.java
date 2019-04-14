package io.e2x.sparrow.nest.web.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonView
@Document
public class UserDetailEditEvent {
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

    @JsonProperty(value = "ssn",required = false,access = JsonProperty.Access.READ_ONLY)
    public String id;

    public UserDetailEditEvent(){}
    public UserDetailEditEvent(String firstname, String lastname, String username, String password, String email, String id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.id = id;
    }
}
