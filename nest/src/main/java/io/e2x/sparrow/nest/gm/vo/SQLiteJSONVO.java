package io.e2x.sparrow.nest.gm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

@JsonView
public class SQLiteJSONVO {
    @JsonProperty("table")
    public String tableName;
    @JsonProperty("columns")
    public List<String> columns;
    @JsonProperty("data")
    public List<ArrayList> data;
}
