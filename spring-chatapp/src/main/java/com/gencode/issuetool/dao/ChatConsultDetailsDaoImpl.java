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

import com.gencode.issuetool.etc.ObjMapper;
import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatConsultDetailsEx;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.GroupSum;
import com.gencode.issuetool.obj.StatsPerDay;

@Component
public class ChatConsultDetailsDaoImpl extends AbstractDaoImpl implements ChatConsultDetailsDao {
	final String queryExStr = "select ccd.id,ccd.customer_id,ccd.agent_id,ccd.biz_id,ccd.country,ccd.lang,ccd.consult_status,ccd.ref_session_id,ccd.prev_chat_id,ccd.consult_type,ccd.chat_comment,ccd.updated_dtm,ccd.created_dtm "
			+" ,ci.id ci_id, ci.customer_id ci_customer_id,ci.id_type ci_id_type,ci.biz_id ci_biz_id,ci.country ci_country,ci.lang ci_lang,ci.created_dtm ci_created_dtm "
			+" ,ccda.id ccda_id,ccda.customer_id ccda_customer_id,ccda.agent_id ccda_agent_id,ccda.biz_id ccda_biz_id,ccda.country ccda_country,ccda.lang ccda_lang,ccda.consult_status ccda_consult_status,ccda.ref_session_id ccda_ref_session_id,ccda.prev_chat_id ccda_prev_chat_id,ccda.consult_type ccda_consult_type,ccda.chat_comment ccda_chat_comment,ccda.updated_dtm ccda_updated_dtm,ccda.created_dtm ccda_created_dtm "
			+" ,ui.id ui_id,ui.login_id ui_login_id,ui.user_name ui_user_name,ui.user_email ui_user_email,ui.role ui_role,ui.agent_id ui_agent_id,ui.biz_id ui_biz_id,ui.group_id ui_group_id,ui.lang ui_lang,ui.country ui_country,ui.first_name ui_first_name,ui.last_name ui_last_name,ui.office_phone ui_office_phone,ui.cell_phone ui_cell_phone,ui.address ui_address,ui.passwd ui_passwd,ui.auth_key ui_auth_key,ui.use_yn ui_use_yn,ui.passwd_update_date ui_passwd_update_date,ui.user_profile ui_user_profile,ui.confirm_yn ui_confirm_yn,ui.profile_url ui_profile_url,ui.access_token ui_access_token,ui.notice_id ui_notice_id,ui.registered_dtm ui_registered_dtm,ui.updated_dtm ui_updated_dtm,ui.created_dtm ui_created_dtm "
			+ " from chat_consult_details ccd "
			+ " left outer join user_info ui on (ccd.agent_id = ui.agent_id) "
			+ " left outer join customer_info ci on (ccd.customer_id = ci.customer_id) "
			+ " left outer join chat_consult_details ccda on (ccd.id = ccda.id) ";
	public ChatConsultDetailsDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(ChatConsultDetails t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameterJdbcTemplate.update("INSERT INTO chat_consult_details"
				+ "(customer_id,agent_id,biz_id,country,lang,consult_status,ref_session_id,prev_chat_id,consult_type,chat_comment,updated_dtm,created_dtm)\r\n" + 
				"VALUES(:customerId,:agentId,:bizId,:country,:lang,:consultStatus,:refSessionId,:prevChatId,:consultType,:chatComment,now(3) ,now(3) )"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<ChatConsultDetails> load(long id) {
		
		ChatConsultDetails t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(
				"select id,customer_id,agent_id,biz_id,country,lang,consult_status,ref_session_id,prev_chat_id,consult_type,chat_comment,updated_dtm,created_dtm"
				+ " from chat_consult_details where id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toChatConsultDetails(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public Optional<ChatConsultDetailsEx> loadEx(long id) {
		ChatConsultDetailsEx t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query
				(queryExStr+" where ccd.id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toChatConsultDetailsEx(resultSet);
				}));
		return Optional.of(t);
	}
	
	@Override
	public void delete(long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(ChatConsultDetails t) {
		namedParameterJdbcTemplate.update("UPDATE chat_consult_details SET " + 
				"customer_id=:customerId,"+
				"agent_id=:agentId,"+
				"biz_id=:bizId,"+
				"country=:country,"+
				"lang=:lang,"+
				"consult_status=:consultStatus,"+
				"ref_session_id=:refSessionId,"+
				"prev_chat_id=:prevChatId,"+
				"consult_type=:consultType,"+
				"chat_comment=:chatComment,"+
				"updated_dtm=now(3) "+
				"WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
		 
	}

	@Override
	public Optional<List<ChatConsultDetails>> loadAll() {
		List<ChatConsultDetails> list = jdbcTemplate.query("select id,customer_id,agent_id,biz_id,country,lang,consult_status,ref_session_id,prev_chat_id,consult_type,chat_comment,updated_dtm,created_dtm from chat_consult_details", (resultSet, i) -> {
            return toChatConsultDetails(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<ChatConsultDetails>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<ChatConsultDetails> list = namedParameterJdbcTemplate.query
				("select id,customer_id,agent_id,biz_id,country,lang,consult_status,ref_session_id, prev_chat_id,consult_type,chat_comment,updated_dtm,created_dtm from chat_consult_details where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<ChatConsultDetails>(ChatConsultDetails.class));
		return Optional.of(list);
	}

	@Override
	public Optional<List<ChatConsultDetailsEx>> findBySessionId(long refSessionId) {
		List<ChatConsultDetailsEx> list = namedParameterJdbcTemplate.query
				(queryExStr + " where ccd.ref_session_id = :refSessionId order by 1,2"
						,new MapSqlParameterSource("refSessionId", refSessionId)
						, (resultSet, i) -> { return toChatConsultDetailsEx(resultSet);}
						);
		return Optional.of(list);
	}

	@Override
	public Optional<PageResultObj<List<ChatConsultDetails>>> search(PageRequest req) {
		String queryStr = "select id,customer_id,agent_id,biz_id,country,lang,consult_status,ref_session_id,prev_chat_id,consult_type,chat_comment,updated_dtm,created_dtm from chat_consult_details where 1=1";
		return internalSearch(queryStr, req, ChatConsultDetails.class);
	}

	@Override
	public Optional<PageResultObj<List<ChatConsultDetailsEx>>> searchEx(String bizId, PageRequest req) {
		String queryStr = String.format(queryExStr+" where ccd.biz_id='%s'", bizId);
		return internalSearch(queryStr, "ccd", req, (resultSet, i) -> { return toChatConsultDetailsEx(resultSet);});
	}

	@Override
	public Optional<List<StatsPerDay>> getResponseCountByAgent(String agentId) {
		List<StatsPerDay> tasks = namedParameterJdbcTemplate.query
				("select DATE_FORMAT(created_dtm, '%Y-%m-%d') stats_date, count(*)  cnt from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and agent_id = :agentId " + 
						"group by DATE_FORMAT(created_dtm, '%Y-%m-%d') "
						, new MapSqlParameterSource("agentId",agentId ) 
						, new BeanPropertyRowMapper<StatsPerDay>(StatsPerDay.class));
		return Optional.of(tasks);
	}

	@Override
	public Optional<List<StatsPerDay>> getResponseCountByBiz(String bizId) {
		List<StatsPerDay> tasks = namedParameterJdbcTemplate.query
				("select DATE_FORMAT(created_dtm, '%Y-%m-%d') stats_date, count(*)  cnt from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId " + 
						"group by DATE_FORMAT(created_dtm, '%Y-%m-%d') "
						, new MapSqlParameterSource("bizId",bizId ) 
						, new BeanPropertyRowMapper<StatsPerDay>(StatsPerDay.class));
		return Optional.of(tasks);
	}

	@Override
	public Optional<List<StatsPerDay>> getResolvedCountByAgent(String agentId) {
		List<StatsPerDay> tasks = namedParameterJdbcTemplate.query
				("select DATE_FORMAT(created_dtm, '%Y-%m-%d') stats_date, count(*)  cnt from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and agent_id = :agentId " + 
						"and consult_status <> 'O' " +
						"group by DATE_FORMAT(created_dtm, '%Y-%m-%d') "
						, new MapSqlParameterSource("agentId",agentId ) 
						, new BeanPropertyRowMapper<StatsPerDay>(StatsPerDay.class));
		return Optional.of(tasks);
	}

	@Override
	public Optional<List<StatsPerDay>> getResolvedCountByBiz(String bizId) {
		List<StatsPerDay> tasks = namedParameterJdbcTemplate.query
				("select DATE_FORMAT(created_dtm, '%Y-%m-%d') stats_date, count(*)  cnt from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId " + 
						"and consult_status <> 'O' " +
						"group by DATE_FORMAT(created_dtm, '%Y-%m-%d') "
						, new MapSqlParameterSource("bizId",bizId ) 
						, new BeanPropertyRowMapper<StatsPerDay>(StatsPerDay.class));
		return Optional.of(tasks);
	}

	@Override
	public int getResolvedCountForGoal(String bizId) {
		int resolvedCnt = namedParameterJdbcTemplate.queryForObject
				("select count(*)  cnt from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId " + 
						"and consult_status <> 'O' "
						, new MapSqlParameterSource("bizId",bizId ) 
						, Integer.class);
		return resolvedCnt;
	}

	@Override
	public int getResponseCountForGoal(String bizId) {
		int responseCnt = namedParameterJdbcTemplate.queryForObject
				("select count(*)  cnt from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId "
						, new MapSqlParameterSource("bizId",bizId ) 
						, Integer.class);
		return responseCnt;
	}

	@Override
	public Optional<List<GroupSum>> getConsultTypeCount(String bizId) {
		List<GroupSum> tasks = namedParameterJdbcTemplate.query
				("select consult_type groupType, count(*)  groupCount from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId " + 
						"group by consult_type "
						, new MapSqlParameterSource("bizId",bizId ) 
						, new BeanPropertyRowMapper<GroupSum>(GroupSum.class));
		return Optional.of(tasks);
	}

	@Override
	public Optional<List<GroupSum>> getConsultStatusCount(String bizId) {
		List<GroupSum> tasks = namedParameterJdbcTemplate.query
				("select consult_status groupType, count(*)  groupCount from chat_consult_details " + 
						"where created_dtm >= DATE_ADD(now(), INTERVAL -7 DAY) " + 
						"and biz_id = :bizId " + 
						"group by consult_status "
						, new MapSqlParameterSource("bizId",bizId ) 
						, new BeanPropertyRowMapper<GroupSum>(GroupSum.class));
		return Optional.of(tasks);
	}

	private ChatConsultDetails toChatConsultDetails(ResultSet resultSet) throws SQLException {
		ChatConsultDetails obj = new ChatConsultDetails();
		
		obj.setId(resultSet.getLong("ID"));
		obj.setCustomerId(resultSet.getString("CUSTOMER_ID"));
		obj.setAgentId(resultSet.getString("AGENT_ID"));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setConsultStatus(resultSet.getString("CONSULT_STATUS"));
		obj.setRefSessionId(resultSet.getLong("REF_SESSION_ID"));
		obj.setPrevChatId(resultSet.getLong("PREV_CHAT_ID"));
		obj.setConsultType(resultSet.getString("CONSULT_TYPE"));
		obj.setChatComment(resultSet.getString("CHAT_COMMENT"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));

		return obj;
	}

	private ChatConsultDetailsEx toChatConsultDetailsEx(ResultSet resultSet) throws SQLException {
		ChatConsultDetailsEx obj = new ChatConsultDetailsEx();
		
		obj.setId(resultSet.getLong("ID"));
		obj.setCustomerId(resultSet.getString("CUSTOMER_ID"));
		obj.setCustomerInfo(ObjMapper.toCustomerInfo("CI_", resultSet));
		obj.setAgentId(resultSet.getString("AGENT_ID"));
		obj.setAgentInfo(ObjMapper.toUserInfo("UI_", resultSet));
		obj.setBizId(resultSet.getString("BIZ_ID"));
		obj.setCountry(resultSet.getString("COUNTRY"));
		obj.setLang(resultSet.getString("LANG"));
		obj.setConsultStatus(resultSet.getString("CONSULT_STATUS"));
		obj.setRefSessionId(resultSet.getLong("REF_SESSION_ID"));
		obj.setPrevChatId(resultSet.getLong("PREV_CHAT_ID"));
		obj.setPrevChat(ObjMapper.toChatConsultDetails("CCDA_", resultSet));
		obj.setConsultType(resultSet.getString("CONSULT_TYPE"));
		obj.setChatComment(resultSet.getString("CHAT_COMMENT"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));

		return obj;
	}
}
