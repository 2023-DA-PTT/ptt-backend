package com.ptt.control;

import com.ptt.entity.DataPoint;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataPointRepository implements PanacheRepository<DataPoint> {
}
