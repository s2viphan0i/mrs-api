package com.sinnguyen.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinnguyen.dao.RecommendationDao;

@Repository
@Transactional
public class RecommendationDaoImpl implements RecommendationDao{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public boolean add(final GenericItemBasedRecommender recommender) {
		try {
		    System.out.println(System.currentTimeMillis());
			String sql = "INSERT INTO recommendation(song_id, r_song_id, similarity) VALUES(?,?,?)";
			List<Object[]> parameters = new ArrayList<Object[]>();
			for (LongPrimitiveIterator items = recommender.getDataModel().getItemIDs();items.hasNext();) {
				long itemId = items.nextLong();
				List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemId, 5);
				for(RecommendedItem recommendation : recommendations) {
					parameters.add(new Object[] {itemId, recommendation.getItemID(), recommendation.getValue()});
				}
		    }
			String t_sql = "TRUNCATE recommendation";
		    this.jdbcTemplate.update(t_sql);
		    this.jdbcTemplate.batchUpdate(sql, parameters);
		    System.out.println(System.currentTimeMillis());
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}