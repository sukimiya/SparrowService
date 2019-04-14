package io.e2x.sparrow.nest.web.controller.event;

import com.fasterxml.jackson.annotation.JsonView;

@JsonView
public class SampleWebPageEvent {
    public Integer pageSize = 20;
    public Integer page = 0;
    public String time1;
    public String time2;
    public String key;
}
