package com.ptt.control.plan;

import com.ptt.entity.plan.PlanRun;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class PlanRunRepository implements PanacheRepository<PlanRun> {
    @Inject
    EntityManager em;

    public Long getFirstDatapointStartTime(String userId, long planRunId) {
        return em.createQuery("SELECT coalesce(MIN(dp.startTime),0) FROM DataPoint dp " +
                        "where dp.planRun.plan.ownerId=?1 and dp.planRun.id=?2", Long.class)
                .setParameter(1, userId)
                .setParameter(2, planRunId).getSingleResult();
    }
}
