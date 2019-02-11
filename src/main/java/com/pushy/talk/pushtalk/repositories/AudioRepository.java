package com.pushy.talk.pushtalk.repositories;

import com.pushy.talk.pushtalk.models.Audio;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AudioRepository extends MongoRepository<Audio, String> {
	Audio findBy_id(ObjectId _id);
}