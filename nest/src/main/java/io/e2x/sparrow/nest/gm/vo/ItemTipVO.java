package io.e2x.sparrow.nest.gm.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;

@Data
@Document
@JsonView
public class ItemTipVO {

    public ItemTipVO(){}
    public ItemTipVO(Integer typeId, String itemName, String desc){
        this.typeId = typeId;
        this.itemName = itemName;
        this.desc = desc;
    }
    @Id
    @GeneratedValue(strategy= GenerationStrategy.UNIQUE)
    @JsonIgnore
    public BigInteger id;
    @Field(value = "typeid")
    @JsonProperty("typeId")
    public Integer typeId;
    @JsonProperty("itemName")
    public String itemName;
    @JsonProperty("desc")
    public String desc;
}
