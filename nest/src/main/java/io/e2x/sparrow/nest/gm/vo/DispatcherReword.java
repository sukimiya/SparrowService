package io.e2x.sparrow.nest.gm.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.integration.util.UUIDConverter;

import java.math.BigInteger;
import java.util.Date;

@Data
@Document
@JsonView
public class DispatcherReword {
    public DispatcherReword(){}
    public DispatcherReword(long timestamp, String role, Integer itemType, String itemName,Integer num){
        ssn = Long.toHexString(new Date().getTime());
        this.timestamp = timestamp;
        this.role = role;
        this.itemType = itemType;
        this.itemName = itemName;
        this.num = num;
    }
    @Id
    private BigInteger id;
    @JsonProperty("ssn")
    public String ssn;
    @JsonProperty("timestamp")
    public long timestamp;
    @JsonProperty("role")
    public String role;
    @JsonProperty("itemType")
    public Integer itemType;
    @JsonProperty("itemName")
    public String itemName;
    @JsonProperty("num")
    public Integer num;
    @JsonProperty("sent")
    public boolean sent = false;
}
