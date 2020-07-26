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
import org.springframework.stereotype.Component;

import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.BizInfo;
import com.gencode.issuetool.obj.CommonCode;

@Component
public class CommonCodeDaoImpl extends AbstractDaoImpl implements CommonCodeDao {

	public CommonCodeDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(CommonCode t) {
			namedParameterJdbcTemplate.update("INSERT INTO common_code(group_id,item_key,item_value,description)\r\n" + 
					"VALUES(:groupId,:itemKey,:itemValue,:description )"
					,new BeanPropertySqlParameterSource(t));
			return -1;
	}

	@Override
	public Optional<CommonCode> load(long id) {
		throw new UnsupportedOperationException();
	}

	public Optional<CommonCode> load(String id) {
		CommonCode t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query("select group_id,item_key,item_value,description from common_code where group_id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toCommonCode(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		throw new UnsupportedOperationException();
	}

	public void delete(String id) {
		namedParameterJdbcTemplate.update("DELETE FROM common_code where group_id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(CommonCode t) {
		namedParameterJdbcTemplate.update("UPDATE common_code SET " + 
				"group_id        =:groupId,"+
				"item_key    =:itemKey,"+
				"item_value       =:itemValue,"+
				"description         =:description "+
				"WHERE group_id = :id"
				,new BeanPropertySqlParameterSource(t));		 
	}

	@Override
	public Optional<List<CommonCode>> loadAll() {
		List<CommonCode> list = jdbcTemplate.query("select group_id,item_key,item_value,description from common_code", (resultSet, i) -> {
            return toCommonCode(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<CommonCode>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<CommonCode> t = namedParameterJdbcTemplate.query
				("select group_id,item_key,item_value,description from common_code where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<CommonCode>(CommonCode.class));
		return Optional.of(t);
	}

	@Override
	public Optional<PageResultObj<List<CommonCode>>> search(PageRequest req) {
		String queryStr = "select group_id,item_key,item_value,description from common_code where 1=1";
		return internalSearch(queryStr, req, CommonCode.class);
	}

	
	private CommonCode toCommonCode(ResultSet resultSet) throws SQLException {
		CommonCode obj = new CommonCode();
		obj.setGroupId(resultSet.getString("GROUP_ID"));
		obj.setItemKey(resultSet.getString("ITEM_KEY"));
		obj.setItemValue(resultSet.getString("ITEM_VALUE"));
		obj.setDescription(resultSet.getString("DESCRIPTION"));
		return obj;
	}
}
