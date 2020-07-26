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
import com.gencode.issuetool.obj.MessageLog;

@Component
public class MessageLogDaoImpl extends AbstractDaoImpl implements MessageLogDao {

	public MessageLogDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(MessageLog t) {
		namedParameterJdbcTemplate.update(
				"INSERT INTO message_log(message_id,customer_id,chat_id,direction,biz_id,country,lang,status,message,que_created_dtm, created_dtm)\r\n"
						+ "VALUES(:messageId,:customerId,:chatId,:direction,:bizId,:country,:lang,:status,:message,STR_TO_DATE(substr(:queCreatedDtm, 1,22),'%Y-%m-%d %H:%i:%s.%f'),NOW(3) )",
				new BeanPropertySqlParameterSource(t));
		return 0;
	}

	@Override
	public Optional<MessageLog> load(long id) {
		MessageLog t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(
				"select message_id,customer_id,chat_id,direction,biz_id,country,lang,status, message,que_created_dtm, created_dtm from message_log where message_id = :id",
				new MapSqlParameterSource("id", id), (resultSet, i) -> {
					return toMessageLog(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM message_log where message_id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(MessageLog t) {
		namedParameterJdbcTemplate.update(
				"UPDATE message_log SET customer_id=:customerId, chat_id =:chatId,"
						+ "direction=:direction, biz_id=:bizId, country=:country,"
						+ "lang=:lang, status=:status,message=:message WHERE message_id = :id",
				new BeanPropertySqlParameterSource(t));
	}
	
	@Override
	public void updateByChatId(long chatId, String status) {
		MessageLog log = new MessageLog();
		log.setChatId(chatId);
		log.setStatus(status);
		namedParameterJdbcTemplate.update(
				"UPDATE message_log SET status=:status WHERE chat_id = :chatId",
				new BeanPropertySqlParameterSource(log));
	}

	@Override
	public Optional<List<MessageLog>> loadAll() {
		List<MessageLog> list = jdbcTemplate.query(
				"select message_id,customer_id,chat_id,direction,biz_id,country,lang,status,message,que_created_dtm,created_dtm from message_log where 1=1",
				(resultSet, i) -> {
					return toMessageLog(resultSet);
				});
		return Optional.of(list);
	}

	@Override
	public Optional<List<MessageLog>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<MessageLog> t = namedParameterJdbcTemplate.query(
				"select message_id,customer_id,chat_id,direction,biz_id,country,lang,status,message,que_created_dtm,created_dtm from message_log where 1=1"
						+ searchMapObj.andQuery(),
				searchMapObj.params(), new BeanPropertyRowMapper<MessageLog>(MessageLog.class));
		return Optional.of(t);
	}

	@Override
	public Optional<PageResultObj<List<MessageLog>>> loadByChatId(long chatId, boolean viewPrevChat, PageRequest req) {
		Map<String, String> reqMap = req.getSearchMap();
		// 이관전의 채팅도 포함
		if (!viewPrevChat) {
			String queryStr = String.format("select message_id,customer_id,chat_id,direction,biz_id,country,lang,status,message,que_created_dtm,created_dtm from message_log where chat_id = %d", chatId);
			return internalSearch(queryStr, req, MessageLog.class);
		} else {
			String queryStr = String.format("select message_id,customer_id,chat_id,direction,biz_id,country,lang,status,message,que_created_dtm,created_dtm "
					+ "from message_log where chat_id in ( " + 
			"select c.id " + 
			"FROM chat_consult_details c, " + 
			"	 chat_consult_details b " +
			"where c.ref_session_id = b.ref_session_id " + 
			"and b.id=%d )", chatId);
			return internalSearch(queryStr, req, MessageLog.class);
		}
	}

	@Override
	public Optional<PageResultObj<List<MessageLog>>> search(PageRequest req) {
		String queryStr = "select message_id,customer_id,chat_id,direction,biz_id,country,lang,status,message,que_created_dtm,created_dtm from message_log where 1=1";
		return internalSearch(queryStr, req, MessageLog.class);
	}

	private MessageLog toMessageLog(ResultSet resultSet) throws SQLException {
		MessageLog obj = new MessageLog();
		obj.setMessageId(resultSet.getLong("MESSAGE_ID"));
		obj.setCustomerId(resultSet.getString("CUSTOMER_ID"));
		obj.setChatId(resultSet.getLong("CHAT_ID"));
		obj.setDirection(resultSet.getString("DIRECTION"));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setStatus(resultSet.getString("STATUS"));
		obj.setMessage(resultSet.getString("MESSAGE"));
		obj.setQueCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("QUE_CREATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}

}
