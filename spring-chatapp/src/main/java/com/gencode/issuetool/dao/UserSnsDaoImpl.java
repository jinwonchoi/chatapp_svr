package com.gencode.issuetool.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.UserSns;

@Component
public class UserSnsDaoImpl extends AbstractDaoImpl implements UserSnsDao {

	public UserSnsDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(UserSns t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update("INSERT INTO user_sns(user_id,sns_type,sns_id)\r\n" + 
				"VALUES(:userId,:snsType,:snsId)"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<UserSns> load(long id) {
		UserSns t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(
				"select id,user_id,sns_type,sns_id from user_sns where id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toUserSns(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM user_sns where id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(UserSns t) {
		namedParameterJdbcTemplate.update("UPDATE user_sns SET " +
				"user_id =:userId,"+
				"sns_type=:snsType,"+
				"sns_id  =:snsId "+
				"WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
	}

	@Override
	public Optional<List<UserSns>> loadAll() {
		List<UserSns> list = jdbcTemplate.query(
				"select id,user_id,sns_type,sns_id from user_sns where 1=1", (resultSet, i) -> {
            return toUserSns(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<UserSns>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<UserSns> t = namedParameterJdbcTemplate.query
				("select id,user_id,sns_type,sns_id from user_sns where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<UserSns>(UserSns.class));
		return Optional.of(t);
	}

	@Override
	public Optional<PageResultObj<List<UserSns>>> search(PageRequest req) {
		String queryStr = "select id,user_id,sns_type,sns_id from user_sns where 1=1";
		return internalSearch(queryStr, req, UserSns.class);
	}

	private UserSns toUserSns(ResultSet resultSet) throws SQLException {
		UserSns obj = new UserSns();
		obj.setId(resultSet.getLong("ID"));
		obj.setUserId(resultSet.getLong("USER_ID"));
		obj.setSnsType(resultSet.getString("SNS_TYPE"));
		obj.setSnsId(resultSet.getString("SNS_ID"));
		return obj;
	}
}
