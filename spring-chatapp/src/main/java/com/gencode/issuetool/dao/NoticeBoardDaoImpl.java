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
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.NoticeBoardEx;

@Component
public class NoticeBoardDaoImpl extends AbstractDaoImpl implements NoticeBoardDao {

	public NoticeBoardDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(NoticeBoard t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update("INSERT INTO notice_board(title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id)\r\n" + 
				"VALUES(:title,:content,:registerId,NOW(3),NOW(3),:postType,:postLevel,:refId )"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<NoticeBoard> load(long id) {
		NoticeBoard t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(
				"select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from notice_board where id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toNoticeBoard(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public Optional<NoticeBoardEx> loadEx(long id) {
		NoticeBoardEx t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(
				"select nb.id,nb.title,nb.content,nb.register_id,nb.updated_dtm,nb.created_dtm,nb.post_type,nb.post_level,nb.ref_id\r\n" + 
				"      ,ui.id ui_id,ui.login_id ui_login_id,ui.user_name ui_user_name,ui.user_email ui_user_email,ui.role ui_role,ui.agent_id ui_agent_id,ui.biz_id ui_biz_id,ui.group_id ui_group_id,ui.lang ui_lang,ui.country ui_country,ui.first_name ui_first_name,ui.last_name ui_last_name,ui.office_phone ui_office_phone,ui.cell_phone ui_cell_phone,ui.address ui_address,ui.passwd ui_passwd,ui.auth_key ui_auth_key,ui.use_yn ui_use_yn,ui.passwd_update_date ui_passwd_update_date,ui.user_profile ui_user_profile,ui.confirm_yn ui_confirm_yn,ui.profile_url ui_profile_url,ui.access_token ui_access_token,ui.notice_id ui_notice_id,ui.registered_dtm ui_registered_dtm,ui.updated_dtm ui_updated_dtm,ui.created_dtm ui_created_dtm  \r\n" + 
				"  from notice_board nb\r\n" + 
				"left outer join user_info ui on (nb.register_id = ui.id)  \r\n" + 
				"where nb.id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toNoticeBoardEx(resultSet);
				}));
		return Optional.of(t);
	}

	
	@Override
	public void delete(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM notice_board where id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(NoticeBoard t) {
		namedParameterJdbcTemplate.update("UPDATE notice_board SET " +
				"title      =:title,"+ 
				"content    =:content,"+
				"register_id=:registerId,"+
				"updated_dtm =NOW(3),"+
				"post_type  =:postType,"+
				"post_level  =:postLevel,"+
				"ref_id     =:refId "+
				"WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
	}

	@Override
	public Optional<List<NoticeBoard>> loadAll() {
		List<NoticeBoard> list = jdbcTemplate.query(
				"select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from notice_board where 1=1", (resultSet, i) -> {
            return toNoticeBoard(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<NoticeBoard>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<NoticeBoard> t = namedParameterJdbcTemplate.query
				("select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from notice_board where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<NoticeBoard>(NoticeBoard.class));
		return Optional.of(t);
	}

	@Override
	public Optional<PageResultObj<List<NoticeBoard>>> search(PageRequest req) {
		String queryStr = "select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from notice_board where 1=1";
		return internalSearch(queryStr, req, NoticeBoard.class);
	}
	
	@Override
	public Optional<PageResultObj<List<NoticeBoardEx>>> searchEx(PageRequest req) {
		String queryStr = "select nb.id,nb.title,nb.content,nb.register_id,nb.updated_dtm,nb.created_dtm,nb.post_type,nb.post_level,nb.ref_id\r\n" + 
				"      ,ui.id ui_id,ui.login_id ui_login_id,ui.user_name ui_user_name,ui.user_email ui_user_email,ui.role ui_role,ui.agent_id ui_agent_id,ui.biz_id ui_biz_id,ui.group_id ui_group_id,ui.lang ui_lang,ui.country ui_country,ui.first_name ui_first_name,ui.last_name ui_last_name,ui.office_phone ui_office_phone,ui.cell_phone ui_cell_phone,ui.address ui_address,ui.passwd ui_passwd,ui.auth_key ui_auth_key,ui.use_yn ui_use_yn,ui.passwd_update_date ui_passwd_update_date,ui.user_profile ui_user_profile,ui.confirm_yn ui_confirm_yn,ui.profile_url ui_profile_url,ui.access_token ui_access_token,ui.notice_id ui_notice_id,ui.registered_dtm ui_registered_dtm,ui.updated_dtm ui_updated_dtm,ui.created_dtm ui_created_dtm  \r\n" + 
				"  from notice_board nb\r\n" + 
				"left outer join user_info ui on (nb.register_id = ui.id) where 1 =1 ";
		return internalSearch(queryStr, req, (resultSet, i) -> { return toNoticeBoardEx(resultSet);});
	}
	
	private NoticeBoard toNoticeBoard(ResultSet resultSet) throws SQLException {
		NoticeBoard obj = new NoticeBoard();
		obj.setId(resultSet.getLong("ID"));
		obj.setTitle(resultSet.getString("TITLE"));
		obj.setContent(resultSet.getString("CONTENT"));
		obj.setRegisterId(resultSet.getLong("REGISTER_ID"));
		obj.setPostType(resultSet.getString("POST_TYPE"));
		obj.setPostLevel(resultSet.getInt("POST_LEVEL"));
		obj.setRefId(resultSet.getLong("REF_ID"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}

	private NoticeBoardEx toNoticeBoardEx(ResultSet resultSet) throws SQLException {
		NoticeBoardEx obj = new NoticeBoardEx();
		obj.setId(resultSet.getLong("ID"));
		obj.setTitle(resultSet.getString("TITLE"));
		obj.setContent(resultSet.getString("CONTENT"));
		obj.setRegisterId(resultSet.getString("REGISTER_ID"));
		obj.setRegisterUserInfo(ObjMapper.toUserInfo("UI_", resultSet));
		obj.setPostType(resultSet.getString("POST_TYPE"));
		obj.setPostLevel(resultSet.getInt("POST_LEVEL"));
		obj.setRefId(resultSet.getLong("REF_ID"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}
}
