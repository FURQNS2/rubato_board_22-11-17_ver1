package com.sycompany.rubato.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sycompany.rubato.dto.FileDto;
import com.sycompany.rubato.dto.RFBoardDto;
import com.sycompany.rubato.dto.RReplyDto;

@Mapper
public interface IDao {
	
	//회원정보
	public void joinMember(String mid, String mpw, String mname, String memail);
	public int checkUserId(String mid);
	public int checkUserIdAndPw(String mid, String mpw);
	
	//글쓰기
	public void rfbwrite(String rfbname, String rfbtitle, String rfbcontent,String rfbuserid, int filecount);
	
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
	
	//파일 업로드 관련
	public void fileInfoInsert(int boardnum, String fileoriname, String filename, String fileextension, String fileurl);
	//현재 파일이 첨부된 글을 쓴 아이디로 검색된 글 목록 => sql문에서 하는 것이 더 확실하나 아직 배우지 않아서 이것으로 대체한다.
	public ArrayList<RFBoardDto> boardLatestInfo(String	rfbuserid);
	//파일 불러오기
	public FileDto getFileInfo(String boardnum);
	
}

