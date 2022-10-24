create or replace FUNCTION get_datapoints13(interv int,
                                            sTime bigint, -- not startTime, same reason as above
                                            eTime bigint, -- same as above
                                            aggr character varying,
                                            planid bigint,
                                            stepid bigint)
    RETURNS TABLE (
                      duration bigint,
                      start bigint)
    language plpgsql
as
'
    declare
    begin
        RETURN QUERY
            WITH series AS (
                SELECT generate_series(sTime, eTime, interv) AS r_from -- 1950 = min, 2010 = max, 10 = 10 year interval
            ), range AS (
                SELECT r_from, (r_from + (interv-1)) AS r_to FROM series -- 9 = interval (10 years) minus 1
            )
            SELECT (SELECT MAX(dp.duration) FROM datapoint dp WHERE (starttime BETWEEN r_from AND r_to)
                                                                AND planrun_id = planid
                                                                AND step_step_id = stepid ) as duration, r_from as start
            FROM range where (SELECT count(dp.duration) FROM datapoint dp WHERE (starttime BETWEEN r_from AND r_to)
                                                                            AND planrun_id = planid
                                                                            AND step_step_id = stepid ) > 0;
    end;';