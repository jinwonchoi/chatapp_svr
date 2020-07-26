package com.gencode.issuetool.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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

import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.BizInfo;
import com.gencode.issuetool.obj.CustomerInfo;

@Component
public class CustomerInfoDaoImpl extends AbstractDaoImpl implements CustomerInfoDao {

	public CustomerInfoDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(CustomerInfo t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update("INSERT INTO customer_info(customer_id,id_type,biz_id,country,lang,created_dtm)\r\n" + 
				"VALUES(:customerId,:idType,:bizId,:country,:lang ,now(3) )"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<CustomerInfo> load(long id) {
		CustomerInfo t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query("select id,customer_id,id_type,biz_id,country,lang,created_dtm from customer_info where id=:id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toCustomerInfo(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		/*
		namedParameterJdbcTemplate.update("DELETE FROM biz_info where id = :id",
				new MapSqlParameterSource("id", id));
	 
		 */
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(CustomerInfo t) {
		namedParameterJdbcTemplate.update("UPDATE customer_info SET " + 
				"id         =:id,"+
				"customer_id=:customerId,"+
				"id_type    =:idType,"+
				"biz_id     =:bizId,"+
				"country    =:country,"+
				"lang       =:lang "+
				"WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
	}

	@Override
	public Optional<List<CustomerInfo>> loadAll() {
		List<CustomerInfo> list = jdbcTemplate.query("select id,customer_id,id_type,biz_id,country,lang,created_dtm from customer_info where 1=1", (resultSet, i) -> {
            return toCustomerInfo(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<CustomerInfo>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<CustomerInfo> t = namedParameterJdbcTemplate.query
				("select id,customer_id,id_type,biz_id,country,lang,created_dtm from customer_info where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<CustomerInfo>(CustomerInfo.class));
		return Optional.of(t);
	}

	@Override
	public Optional<List<CustomerInfo>> findByCustomerId(String customerId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId", customerId);
		
		return search(map);
	}

	
	@Override
	public Optional<PageResultObj<List<CustomerInfo>>> search(PageRequest req) {
		String queryStr = "select id,customer_id,id_type,biz_id,country,lang,created_dtm from customer_info where 1=1";
		return internalSearch(queryStr, req, CustomerInfo.class);
	}

	private CustomerInfo toCustomerInfo(ResultSet resultSet) throws SQLException {
		CustomerInfo obj = new CustomerInfo();
		obj.setId(resultSet.getLong("ID"));
		obj.setCustomerId(resultSet.getString("CUSTOMER_ID"));
		obj.setIdType(resultSet.getString("ID_TYPE"));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}
}
