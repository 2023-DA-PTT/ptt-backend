package com.ptt.entity.result;

import com.ptt.entity.plan.PlanRun;
import com.ptt.entity.step.Step;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@FilterDef(name = "DataPoint.fromDate",
        defaultCondition = "startTime >= :from",
        parameters = @ParamDef(name = "from", type = "long"))
@FilterDef(name = "DataPoint.toDate",
        defaultCondition = "startTime <= :to",
        parameters = @ParamDef(name = "to", type = "long"))
@Filter(name = "DataPoint.toDate")
@Filter(name = "DataPoint.fromDate")
@Entity
public class DataPoint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private PlanRun planRun;
    @ManyToOne
    private Step step;
    private long startTime;
    private long duration;

    public DataPoint() {
    }

    public long getId() {
        return id;
    }

    public PlanRun getPlanRun() {
        return planRun;
    }

    public void setPlanRun(PlanRun planRun) {
        this.planRun = planRun;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DataPoint [duration=" + duration + ", id=" + id + ", planRun=" + planRun + ", startTime=" + startTime
                + ", step=" + step + "]";
    } 
}
