package com.ptt.control;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

import com.ptt.entity.DataPoint;

@ApplicationScoped
public class DataPointRepository implements PanacheRepository<DataPoint> {
}
