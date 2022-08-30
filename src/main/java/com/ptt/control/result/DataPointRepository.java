package com.ptt.control.result;

import com.ptt.entity.dto.result.AggregationType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import com.ptt.entity.result.DataPoint;
import io.quarkus.panache.common.Parameters;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.List;

@ApplicationScoped
public class DataPointRepository implements PanacheRepository<DataPoint> {
    public List<DataPoint> findForStep(long planRunId, long stepId, Long from, Long to, Long interval, AggregationType aggregationType) {
        final PanacheQuery<DataPoint> dpQuery;

        if (interval != null) {
            dpQuery = find(
                    "select NEW com.ptt.entity.dto.DataPointDto(dp.planRun.id, dp.step.id, (dp.startTime / "+ interval +"), "
                            + aggregationType.toString() + "(dp.duration)) " +
                            "from DataPoint dp where dp.planRun.id=?1 and dp.step.id=?2 " +
                            "group by (dp.startTime / "+ interval +"), dp.planRun.id, dp.step.id " + // TODO: when using interval as parameter (?3), it throws selected statement not in group by
                            "order by (dp.startTime / "+ interval +")", planRunId, stepId);
        }
        else {
            dpQuery = find("select NEW com.ptt.entity.dto.DataPointDto(dp.id, dp.planRun.id, dp.step.id, " +
                    "dp.startTime, dp.duration) " +
                    "from DataPoint dp where dp.planRun.id=?1 and dp.step.id=?2 " +
                    "order by startTime", planRunId, stepId);
        }

        if (from != null) {
            dpQuery.filter("DataPoint.fromDate", Parameters.with("from", from));
        }
        if (to != null) {
            dpQuery.filter("DataPoint.toDate", Parameters.with("to", to));
        }

        return dpQuery.list();
    }
}
