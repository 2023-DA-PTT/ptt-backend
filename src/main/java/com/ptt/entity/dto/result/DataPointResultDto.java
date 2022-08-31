package com.ptt.entity.dto.result;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DataPointResultDto {
    private Long duration;
    private Long start;

    public DataPointResultDto(Long duration,
                              @ProjectedFieldName("startTime") Long start) {
        this.duration = duration;
        this.start = start;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }
}
