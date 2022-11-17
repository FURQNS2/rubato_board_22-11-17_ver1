package com.sycompany.rubato.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDao {
	
	//회원정보
	public void joinMember(String mid, String mpw, String mname, String memail);
	public int checkUserId(String mid);
	public int checkUserIdAndPw(String mid, String mpw);
	
	//글쓰기
	public void rfbwrite(String rfbname, String rfbtitle, String rfbcontent,String rfbuserid);
}
