package com.sinnguyen.service.impl;

import java.util.List;

import javax.sql.DataSource;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.RecommendationDao;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.RecommendationService;

@Service
public class RecommendationServiceImp implements RecommendationService {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	RecommendationDao recommendationDao;
	
	@Override
	//@Scheduled(fixedRate = 86400000)
	public ResponseModel getRecommend() {
		try {
			JDBCDataModel dm = new MySQLBooleanPrefJDBCDataModel(dataSource, "favorite","user_id", "song_id", "timestamp");
			ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dm, sim);
			recommendationDao.add(recommender);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
