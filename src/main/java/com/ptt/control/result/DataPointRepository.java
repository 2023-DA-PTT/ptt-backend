package com.ptt.control.result;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.result.DataPoint;

@ApplicationScoped
public class DataPointRepository implements PanacheRepository<DataPoint> {
}
