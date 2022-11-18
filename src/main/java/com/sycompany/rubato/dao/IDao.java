package com.sycompany.rubato.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sycompany.rubato.dto.RFBoardDto;
import com.sycompany.rubato.dto.RReplyDto;

@Mapper
public interface IDao {
	
	//회원정보
	public void joinMember(String mid, String mpw, String mname, String memail);
	public int checkUserId(String mid);
	public int checkUserIdAndPw(String mid, String mpw);
	
	//글쓰기
	public void rfbwrite(String rfbname, String rfbtitle, String rfbcontent,String rfbuserid);
	
	//글 목록
	public ArrayList<RFBoardDto> rfblist();
	public int rfboardAllCount();
	
	// 글 내용
	public RFBoardDto rfboardView(String rfbnum);
	
	//글 지우기
	public void delete(String rfbnum);
	
	//글 조회수
	public void rfbhit(String rfbnum);
	
	//댓글관련
	public void rrwrite(String rrorinum, String rrid, String rrcontent);
	public ArrayList<RReplyDto> rrlist(String rrorinum);
	//댓글 등록 시 해당글의 댓글 갯수 1증가
	public void rrcount(String rrorinum);
	//댓글 삭제
	public void rrdelete(String rrorinum, String rrid, String rrnum);
	//댓글 삭제 시 해당글의 댓글 -1
	public void rrcountMinus(String rrorinum);
	
	
	//게시판 검색 관련
	public ArrayList<RFBoardDto> rfbSearchTitleList(String searchKey); // 제목
	public ArrayList<RFBoardDto> rfbSearchContentList(String searchKey); // 글내용
	public ArrayList<RFBoardDto> rfbSearchWriterList(String searchKey); //글쓴이
}

