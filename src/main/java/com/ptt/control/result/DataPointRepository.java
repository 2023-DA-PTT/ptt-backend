package com.ptt.control.result;

import com.ptt.entity.dto.result.AggregationType;
import com.ptt.entity.dto.result.DataPointResultDto;
import com.ptt.entity.result.DataPoint;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.ParameterMode;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DataPointRepository implements PanacheRepository<DataPoint> {
    @SuppressWarnings("unchecked")
    public List<DataPointResultDto> findWithIntervalPlPgSql(long planRunId, long stepId, Long from, Long to,
                                                            Integer interval, AggregationType aggregationType) {
        List<Object[]> res = getEntityManager().createStoredProcedureQuery("get_datapoints13")
                .registerStoredProcedureParameter("interv", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("sTime", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("eTime", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("aggr", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("planid", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("stepid", Long.class, ParameterMode.IN)
                .setParameter("interv", interval)
                .setParameter("sTime", from)
                .setParameter("eTime", to)
                .setParameter("aggr", aggregationType.toString())
                .setParameter("planid", planRunId)
                .setParameter("stepid", stepId)
                .getResultList();

        return res.stream().map(dbObj -> new DataPointResultDto(
                ((BigInteger)dbObj[0]).longValue(),
                ((BigInteger)dbObj[1]).longValue()))
                .collect(Collectors.toList());
    }

    public Long getAvgPlanRunDuration(long planRunId, long stepId) {
        if(count("planRun.id=?1 and step.id=?2", planRunId, stepId) < 1) {
            return 0L;
        }

        return find("select avg(dp.duration) FROM DataPoint dp where dp.planRun.id=?1 and dp.step.id=?2", planRunId, stepId)
                .project(Double.class)
                .firstResult().longValue();
    }

    public Long getAvgDifferenceBetweenPlanRuns(long planRunId, long stepId, long compareToPlanRunId) {
        return getAvgPlanRunDuration(planRunId, stepId) - getAvgPlanRunDuration(compareToPlanRunId, stepId); // TODO: Implement this using more complex sql query
    }
}
