package com.recommender.bot.repository;

import com.recommender.bot.entities.InnerId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InnerIdRepository extends MongoRepository<InnerId, String> {
}
