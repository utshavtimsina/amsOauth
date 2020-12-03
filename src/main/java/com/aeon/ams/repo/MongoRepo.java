package com.aeon.ams.repo;

import com.aeon.ams.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRepo extends MongoRepository<User,Long> {
    @Query
    User findByUsername(String username);
}
