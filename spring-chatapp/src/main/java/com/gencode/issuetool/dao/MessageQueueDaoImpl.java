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

import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.MessageQueue;

@Component
public class MessageQueueDaoImpl extends AbstractDaoImpl implements MessageQueueDao {

	public MessageQueueDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}


	@Override
	public long register(MessageQueue t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update("INSERT INTO message_queue(customer_id,chat_id,direction,biz_id,country,lang,status,message,created_dtm)\r\n" + 
				"VALUES(:customerId,:chatId,:direction,:bizId,:country,:lang,:status,:message,NOW(3) )"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<MessageQueue> load(long id) {
		MessageQueue t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query("select id,customer_id,chat_id,direction,biz_id,country,lang,status,message,created_dtm from message_queue where id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toMessageQueue(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM message_queue where id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(MessageQueue t) {
		namedParameterJdbcTemplate.update("UPDATE message_queue SET " +
				"customer_id=:customerId,"+
				"chat_id    =:chatId,"+
				"direction  =:direction,"+
				"biz_id     =:bizId,"+
				"country    =:country,"+
				"lang       =:lang,"+
				"status     =:status,"+
				"message    =:message "+
				"WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
	}

	@Override
	public Optional<List<MessageQueue>> loadAll() {
		List<MessageQueue> list = jdbcTemplate.query(
				"select id,customer_id,chat_id,direction,biz_id,country,lang,status,message,created_dtm from message_queue where 1=1", (resultSet, i) -> {
            return toMessageQueue(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<MessageQueue>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<MessageQueue> t = namedParameterJdbcTemplate.query
				("select id,customer_id,chat_id,direction,biz_id,country,lang,status,message,created_dtm from message_queue where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<MessageQueue>(MessageQueue.class));
		return Optional.of(t);
	}

	@Override
	public Optional<PageResultObj<List<MessageQueue>>> loadByChatId(long chatId, PageRequest req) {
		String queryStr = String.format("select id,customer_id,chat_id,direction,biz_id,country,lang,status,message,created_dtm from message_queue where chat_id = %d", chatId);
		return internalSearch(queryStr, req, MessageQueue.class);
	}

	@Override
	public Optional<PageResultObj<List<MessageQueue>>> search(PageRequest req) {
		String queryStr = "select id,customer_id,chat_id,direction,biz_id,country,lang,status,message,created_dtm from message_queue where 1=1";
		return internalSearch(queryStr, req, MessageQueue.class);
	}

	private MessageQueue toMessageQueue(ResultSet resultSet) throws SQLException {
		MessageQueue obj = new MessageQueue();
		obj.setId(resultSet.getLong("ID"));
		obj.setCustomerId(resultSet.getString("CUSTOMER_ID"));
		obj.setChatId(resultSet.getLong("CHAT_ID"));
		obj.setDirection(resultSet.getString("DIRECTION"));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setStatus(resultSet.getString("STATUS"));
		obj.setMessage(resultSet.getString("MESSAGE"));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}
}
