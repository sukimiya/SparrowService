/*
 * Project:sparrow nest
 * LastModified:18-4-28 下午6:07 by sukimiya
 *
 * Copyright (C) 2018.  e2x.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.e2x.sparrow.nest.web.controller.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonView
@Document
public class StandardResponseEvent {
    @JsonProperty(value = "resultId")
    public int resultId=0;
    @JsonProperty(value = "messages")
    public String messages="";

    public StandardResponseEvent(int resultId, String messages) {
        this.resultId = resultId;
        this.messages = messages;
    }

    public StandardResponseEvent() {
    }
}
