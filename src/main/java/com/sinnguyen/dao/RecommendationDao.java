package com.sinnguyen.dao;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;

public interface RecommendationDao {
	public boolean add(GenericItemBasedRecommender recommender);
}
