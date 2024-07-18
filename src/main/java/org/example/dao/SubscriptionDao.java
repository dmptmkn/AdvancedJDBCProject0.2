package org.example.dao;

import org.example.entity.Subscription;

import java.util.List;

public interface SubscriptionDao {

    List<Subscription> findAll();

}
