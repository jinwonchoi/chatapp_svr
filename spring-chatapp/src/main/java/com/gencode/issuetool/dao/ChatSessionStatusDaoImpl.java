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

import com.gencode.issuetool.etc.ObjMapper;
import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.BizInfo;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.StatsPerDay;

@Component
public class ChatSessionStatusDaoImpl extends AbstractDaoImpl implements ChatSessionStatusDao {

	final String qeuryExStr = "select css.id, css.last_message_id,css.last_message, css.unread_cnt,css.direction,css.biz_id,css.country,css.lang,css.updated_dtm,css.created_dtm "
			+" ,ci.id ci_id, ci.customer_id ci_customer_id,ci.id_type ci_id_type,ci.biz_id ci_biz_id,ci.country ci_country,ci.lang ci_lang,ci.created_dtm ci_created_dtm "
			+" ,ccd.id ccd_id,ccd.customer_id ccd_customer_id,ccd.agent_id ccd_agent_id,ccd.biz_id ccd_biz_id,ccd.country ccd_country,ccd.lang ccd_lang,ccd.consult_status ccd_consult_status,ccd.ref_session_id ccd_ref_session_id,ccd.prev_chat_id ccd_prev_chat_id,ccd.consult_type ccd_consult_type,ccd.chat_comment ccd_chat_comment,ccd.updated_dtm ccd_updated_dtm,ccd.created_dtm ccd_created_dtm "
			+" ,ui.id ui_id,ui.login_id ui_login_id,ui.user_name ui_user_name,ui.user_email ui_user_email,ui.role ui_role,ui.agent_id ui_agent_id,ui.biz_id ui_biz_id,ui.group_id ui_group_id,ui.lang ui_lang,ui.country ui_country,ui.first_name ui_first_name,ui.last_name ui_last_name,ui.office_phone ui_office_phone,ui.cell_phone ui_cell_phone,ui.address ui_address,ui.passwd ui_passwd,ui.auth_key ui_auth_key,ui.use_yn ui_use_yn,ui.passwd_update_date ui_passwd_update_date,ui.user_profile ui_user_profile,ui.confirm_yn ui_confirm_yn,ui.profile_url ui_profile_url,ui.access_token ui_access_token,ui.notice_id ui_notice_id,ui.registered_dtm ui_registered_dtm,ui.updated_dtm ui_updated_dtm,ui.created_dtm ui_created_dtm "
			+ " from chat_session_status css "
			+ " left outer join user_info ui on (css.agent_id = ui.agent_id) "
			+ " left outer join customer_info ci on (css.customer_id = ci.customer_id) "
			+ " left outer join chat_consult_details ccd on (css.chat_id = ccd.id) ";
	public ChatSessionStatusDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(ChatSessionStatus t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update("INSERT INTO chat_session_status(chat_id,customer_id,agent_id,last_message_id,last_message,unread_cnt,direction,biz_id,country,lang,updated_dtm,created_dtm)\r\n" + 
				"VALUES(:chatId,:customerId,:agentId,:lastMessageId,:lastMessage,:unreadCnt,:direction,:bizId,:country,:lang,now(3) ,now(3) )"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<ChatSessionStatus> load(long id) {
		ChatSessionStatus t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query
				("select id,chat_id,customer_id,agent_id,last_message_id,last_message,unread_cnt,direction,biz_id,country,lang,updated_dtm,created_dtm from chat_session_status where id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toChatSessionStatus(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public Optional<ChatSessionStatusEx> loadEx(long id) {
		ChatSessionStatusEx t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query
				(qeuryExStr+" where css.id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toChatSessionStatusEx(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public Optional<ChatSessionStatusEx> loadExByChatId(long id) {
		ChatSessionStatusEx t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query
				(qeuryExStr+" where css.chat_id = :chat_id",
				new MapSqlParameterSource("chat_id",id ), (resultSet,i)->{
					return toChatSessionStatusEx(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM chat_session_status where id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void deleteByChatId(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM chat_session_status where chat_id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(ChatSessionStatus t) {
		namedParameterJdbcTemplate.update("UPDATE chat_session_status SET " + 
				"chat_id        =:chatId,"+
				"customer_id    =:customerId,"+
				"agent_id       =:agentId,"+
				"last_message_id=:lastMessageId,"+
				"last_message  =:lastMessage,"+
				"unread_cnt   =:unreadCnt,"+
				"direction =:direction,"+
				"biz_id   =:bizId,"+
				"country   =:country,"+
				"lang   =:lang,"+
				"updated_dtm    =now(3) "+
				" WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
	}

	@Override
	public void resetUnreadCnt(long chatId) {
		namedParameterJdbcTemplate.update("UPDATE chat_session_status SET unread_cnt=0 where chat_id = :chatId"
				,new MapSqlParameterSource("chatId",chatId));
	}

	@Override
	public Optional<List<ChatSessionStatus>> loadAll() {
		List<ChatSessionStatus> list = jdbcTemplate.query
				("select id,chat_id,customer_id,agent_id,last_message_id,last_message,unread_cnt,direction,biz_id,country,lang,updated_dtm,created_dtm from chat_session_status where 1=1 ", (resultSet, i) -> {
            return toChatSessionStatus(resultSet);
        });
		return Optional.of(list);
	}


	@Override
	public Optional<List<ChatSessionStatusEx>> findByBizId(String bizId) {
		List<ChatSessionStatusEx> list = namedParameterJdbcTemplate.query
				(qeuryExStr + " where css.biz_id = :bizId order by 1,2"
						,new MapSqlParameterSource("bizId", bizId)
						, (resultSet, i) -> { return toChatSessionStatusEx(resultSet);}
						);
		return Optional.of(list);
	}

	@Override
	public Optional<List<ChatSessionStatusEx>> findByAgentId(String agentId) {
		List<ChatSessionStatusEx> list = namedParameterJdbcTemplate.query
				(qeuryExStr + " where css.agent_id = :agentId order by 1,2"
						,new MapSqlParameterSource("agentId", agentId)
						, (resultSet, i) -> { return toChatSessionStatusEx(resultSet);}
						);
		return Optional.of(list);
	}


	@Override
	public Optional<List<ChatSessionStatus>> findByCustomerId(String customerId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId", customerId);
		return search(map);
	}

	@Override
	public Optional<List<ChatSessionStatus>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<ChatSessionStatus> tasks = namedParameterJdbcTemplate.query
				("select id,chat_id,customer_id,agent_id,last_message_id,last_message,unread_cnt,direction,biz_id,country,lang,updated_dtm,created_dtm from chat_session_status where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<ChatSessionStatus>(ChatSessionStatus.class));
		return Optional.of(tasks);
	}

//
//	@Override
//	public Optional<List<ChatSessionStatus>> search(Map<String, String> map) {
//		SearchMapObj searchMapObj = new SearchMapObj(map);
//		List<ChatSessionStatus> tasks = namedParameterJdbcTemplate.query
//				("select id,chat_id,customer_id,agent_id,last_message_id,last_message,unread_cnt,direction,biz_id,country,lang,updated_dtm,created_dtm from chat_session_status where 1=1"
//						+ searchMapObj.andQuery()
//						, searchMapObj.params()
//						, new BeanPropertyRowMapper<ChatSessionStatus>(ChatSessionStatus.class));
//		return Optional.of(tasks);
//	}
	
	
	@Override
	public Optional<PageResultObj<List<ChatSessionStatus>>> search(PageRequest req) {
		String queryStr = "select id,chat_id,customer_id,agent_id,last_message_id,last_message,unread_cnt,direction,biz_id,country,lang,updated_dtm,created_dtm from chat_session_status where 1=1";
		return internalSearch(queryStr, req, ChatSessionStatus.class);
	}

	private ChatSessionStatus toChatSessionStatus(ResultSet resultSet) throws SQLException {
		ChatSessionStatus obj = new ChatSessionStatus();
		obj.setId(resultSet.getLong("ID"));
		obj.setChatId(resultSet.getLong("CHAT_ID"));
		obj.setCustomerId(resultSet.getString("CUSTOMER_ID"));
		obj.setAgentId(resultSet.getString("AGENT_ID"));
		obj.setLastMessageId(resultSet.getLong("LAST_MESSAGE_ID"));
		obj.setLastMessage(resultSet.getString("LAST_MESSAGE"));
		obj.setUnreadCnt(resultSet.getInt("UNREAD_CNT"));
		obj.setDirection(resultSet.getString("DIRECTION"));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}
	
	private ChatSessionStatusEx toChatSessionStatusEx(ResultSet resultSet) throws SQLException {
		ChatSessionStatusEx obj = new ChatSessionStatusEx();
		obj.setId(resultSet.getLong("ID"));
		obj.setChatConsultDetails(ObjMapper.toChatConsultDetails("CCD_", resultSet));
		obj.setCustomerInfo(ObjMapper.toCustomerInfo("CI_", resultSet));
		obj.setAgentInfo(ObjMapper.toUserInfo("UI_", resultSet));
		obj.setLastMessageId(resultSet.getLong("LAST_MESSAGE_ID"));
		obj.setLastMessage(resultSet.getString("LAST_MESSAGE"));
		obj.setUnreadCnt(resultSet.getInt("UNREAD_CNT"));
		obj.setDirection(resultSet.getString("DIRECTION"));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}

	@Override
	public Optional<List<StatsPerDay>> getCustomerInboundCount(String bizId) {
		List<StatsPerDay> tasks = namedParameterJdbcTemplate.query
				("select DATE_FORMAT(created_dtm, '%Y-%m-%d') stats_date, count(distinct customer_id)  cnt from chat_session_status " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId " + 
						"group by DATE_FORMAT(created_dtm, '%Y-%m-%d') "
						, new MapSqlParameterSource("bizId",bizId ) 
						, new BeanPropertyRowMapper<StatsPerDay>(StatsPerDay.class));
		return Optional.of(tasks);
	}
	
	@Override
	public int getWaitingCountForGoal(String bizId) {
		int waitingCnt = namedParameterJdbcTemplate.queryForObject
				("select count(distinct customer_id)  cust_cnt from chat_session_status " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and chat_id = 0 " +
						"and biz_id = :bizId "
						, new MapSqlParameterSource("bizId",bizId ) 
						, Integer.class);
		return waitingCnt;
	}
}
