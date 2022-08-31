create type datapoint_type as
(
    duration bigint,
    start    bigint -- not startTime because it would interract with the DataPoint.startTime column
);

create or replace FUNCTION get_datapoints8(interv int,
                                           sTime bigint, -- not startTime, same reason as above
                                           eTime bigint, -- same as above
                                           aggr character varying,
                                           planid bigint,
                                           stepid bigint)
    RETURNS SETOF datapoint_type
    language plpgsql
as
'
    declare
        v_act_dur bigint;
        v_act     datapoint_type;
    begin
        -- Looping through BigInt would throw an integer out of range exception
        for counter in (interv)..(eTime - sTime) by interv
            loop
                SELECT MAX(dp.duration)
                into v_act_dur
                from datapoint AS dp
                WHERE starttime >= (counter - interv) + sTime
                  AND starttime < counter + sTime
                  AND planrun_id = planid
                  AND step_step_id = stepid;

                IF v_act_dur is not null then
                    SELECT v_act_dur, counter + sTime INTO v_act;
                    RETURN NEXT v_act;
                end if;
            end loop;
    end;';
