package com.sycompany.rubato.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.sycompany.rubato.dao.IDao;
import com.sycompany.rubato.dto.FileDto;
import com.sycompany.rubato.dto.RFBoardDto;
import com.sycompany.rubato.dto.RReplyDto;

@Controller
public class HomeController {

	@Autowired
	private SqlSession sqlSession;
	
	//private IDao dao = sqlSession.getMapper(IDao.class);
	
	@RequestMapping("/")
	public String HOME() {
	
		return "redirect:index";
	}
	
	@RequestMapping("/index")
	public String index(Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		//어레이리스트의 상위인 리스트를 부른다.
		List<RFBoardDto> boardDtos = dao.rfblist();
		
		boardDtos = boardDtos.subList(0, 4); 
		int boardSize = boardDtos.size(); //전체글의 개수
		
		// 게시글의 수가 4보다 작을 때
		if(boardSize > 4) {
			boardDtos = boardDtos.subList(0, 4);			
		}
		
		model.addAttribute("latestDtos", boardDtos);	
		
//		model.addAttribute("freeboard01", boardDtos.get(0));
//		model.addAttribute("freeboard02", boardDtos.get(1));
//		model.addAttribute("freeboard03", boardDtos.get(2));
//		model.addAttribute("freeboard04", boardDtos.get(3));
		
		
		return "index";
	}
	
	@RequestMapping("/board_list")
	public String board_list(Model model) {

		IDao dao = sqlSession.getMapper(IDao.class);
		ArrayList<RFBoardDto> boardDtos = dao.rfblist();
		int boardcount = dao.rfboardAllCount(); // 글 목록 개수
		model.addAttribute("boardList", boardDtos); // 글 목록 전부 보여주기
		model.addAttribute("boardCount", boardcount); //글 목록 총개수
		
		return "board_list";
	}
	
	@RequestMapping("/board_view")
	public String board_view(HttpServletRequest request, Model model, HttpSession session) {

		IDao dao = sqlSession.getMapper(IDao.class);			
		
		String rfbnum = request.getParameter("rfbnum");
		
		RFBoardDto rfboardDto1 = dao.rfboardView(rfbnum);
		
		if(rfboardDto1.getRfbuserid().equals(session.getAttribute("memberId"))) {
			
		}else{
		//글 수정하는 update사용
		//조회수
		//변경 후 dto에 싣어줘야 한다.
		dao.rfbhit(rfbnum);  
		}
		
		// 글 내용보는 select문 사용
		RFBoardDto rfboardDto2 = dao.rfboardView(rfbnum);
		
		String rrorinum = request.getParameter("rfbnum");
		ArrayList<RReplyDto> replyDtos = dao.rrlist(rrorinum);
		
		// 파일 주소 불러오기
		FileDto dto = dao.getFileInfo(rfbnum);
		
		
		model.addAttribute("rfbView", rfboardDto2); // 작성한 모든 글 불러오기
		model.addAttribute("replylist",replyDtos); // 댓글 불러오기
		
		String extension = dto.getFileextension();
		
		if(extension != null) {	
			model.addAttribute("filedto",dto);
		}
			
		return "board_view";
	}
	
	@RequestMapping("/board_write")
	public String board_write(HttpServletResponse response,HttpServletRequest request, HttpSession session) {

		String sessionId = (String)session.getAttribute("memberId");
		if(sessionId == null) {
			PrintWriter out;
			
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 글을 쓸 수 없습니다.');history.go(-1);</script>");
				out.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			
		}
		
		return "board_write";
	}
	
	@RequestMapping("/member_join")
	public String member_join() {

		return "member_join";
	}
	
	@RequestMapping("/joinok")
	public String joinok(HttpServletRequest request, HttpSession session) {

		IDao dao =sqlSession.getMapper(IDao.class);
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		String memberName = request.getParameter("mname");
		String memberEmail = request.getParameter("memail");
		
		dao.joinMember(memberId, memberPw, memberName, memberEmail);
		
		session.setAttribute("memberId", memberId);
		
		return "redirect:index";
	}
	
	@RequestMapping("/loginok")
	public String loginok(HttpServletRequest request, HttpSession session, Model model) {

		IDao dao = sqlSession.getMapper(IDao.class);
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		
		//int checkIdFlag = dao.checkUserId(memberId);
		
//		if(checkIdFlag == 0) {
//			//없는 아이디
//			return "redirect:index";
//		}else {
			int checkIdFlag =dao.checkUserIdAndPw(memberId, memberPw);
			
			if(checkIdFlag == 1) {
				//로그인 성공
				 session.setAttribute("memberId",memberId);
				 session.setAttribute("memberPw", memberPw);
				model.addAttribute("memberId",memberId);
				return "redirect:index";
				
			}else{
				//로그인 실패
				return "redirect:index";
			}
		//}
		
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();
		
		return "redirect:index";
	}
	
	@RequestMapping(value = "/writeOk", method=RequestMethod.POST)
	public String writeOk(HttpServletRequest request, HttpSession session, @RequestPart MultipartFile files) throws IllegalStateException, IOException {

		IDao dao = sqlSession.getMapper(IDao.class);
		
		//로그인 된 경우 화면에 저장된 세션에서 아이디값을 불러온다. 
		//세션에 저장된 값은 어디서든 불러올 수 있다.
		String sessionId = session.getAttribute("memberId").toString();
		String boardName = request.getParameter("rfbname");
		String boardTitle = request.getParameter("rfbtitle");
		String boardContent = request.getParameter("rfbcontent");
		
		
		
		
		if(files.isEmpty()) { // 파일 비어있음 Empty-빈, 비어있는
			dao.rfbwrite(boardName, boardTitle, boardContent, sessionId,0);	
		} else {
			dao.rfbwrite(boardName, boardTitle, boardContent, sessionId,1);
			
			ArrayList<RFBoardDto> latestBoard = dao.boardLatestInfo(sessionId);
			RFBoardDto dto = latestBoard.get(0);			
			int rfbnum = dto.getRfbnum();
			
			
			// 파일 첨부
			String fileOriName= files.getOriginalFilename(); // 첨부된 파일의 원래 이름
			
			//내가 만든 파일이름에 확장자를 빼냄
			//toLowerCase() => 확장자 추출 후 소문자로 강제 변경
			String fileExtension = FilenameUtils.getExtension(fileOriName).toLowerCase();
			
			File destinationFile; // java.io 절대 톰캣이랑 헷갈리면 안 됨!!
			
			
			String destinationFileName; //실제 서버에 저장된 파일의 변경된 이름이 저장될 변수선언
			
			//첨부된 파일이 저장될 서버의 실제 폴더 경로
			String fileurl = "E:/SpringBoot_workspace/RubatoProject_22-11-17/src/main/resources/static/uploadfiles/";
			
			
			
			
			do {
			//랜덤문자 만들기 보통 4의 배수로 간다(16)
			// 랜덤문자.확장자명 를 만든다. 잘라낸 확장자명을 마지막에 넣는다. 
			destinationFileName = RandomStringUtils.randomAlphabetic(32) + "." + fileExtension;
			
			//열심히 만든 랜덤 문자들을 이어줄 file클래스에 넣어줌
			destinationFile = new File(fileurl + destinationFileName);
			} while(destinationFile.exists());  //exists은 bloolean형태이다.
			//혹시 같은 이름의 파일이름이 존재하는지 확인
		

			
			//업로드할 수 있게 컴퓨터 파일이 열리게 만듦
			destinationFile.getParentFile().mkdir();
			//업로드된 파일이 지정한 폴더로 이동 완료!
			files.transferTo(destinationFile);
			
			dao.fileInfoInsert(rfbnum, fileOriName, destinationFileName, fileExtension, fileurl);
			
		}
	
		return "redirect:board_list";
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		String delete = request.getParameter("rfbnum");

		dao.delete(delete);
		
		return "redirect:board_list";
	}
	
	@RequestMapping("/replyOk")
	public String replyOk(HttpServletResponse response,HttpSession session, HttpServletRequest request, Model model) {

		

		String sessionId = session.getAttribute("memberId").toString();
		//댓글이 달린 원글의 번호
		String rrorinum = request.getParameter("rfbnum");
		String rrcontent = request.getParameter("rrcontent");
		
		if(sessionId == null) {
			PrintWriter out;
			
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 글을 쓸 수 없습니다.');history.go(-1);</script>");
				out.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			IDao dao = sqlSession.getMapper(IDao.class);
			//댓글 쓰기
			dao.rrwrite(rrorinum, sessionId, rrcontent);
			//댓글 총 개수 증가
			dao.rrcount(rrorinum); 
			
			//댓글을 달려는 게시물이 보이게 만듦(DB에서 파일 불러옴)
			RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
			model.addAttribute("rfbView",rfboardDto);
			
			//댓글 내용
			ArrayList<RReplyDto> replyDtos = dao.rrlist(rrorinum);
			model.addAttribute("replylist",replyDtos);
		}
		return "board_view";
		
	}
	
	@RequestMapping("/replyDelete")
	public String replyDelete(HttpServletResponse response, Model model ,HttpServletRequest request, HttpSession session) {
		
		String rrorinum = request.getParameter("rrorinum"); // 댓글이 달린 원글의 번호
		String rrid = request.getParameter("rrid");  // 작성자의 아이디
		String rrnum = request.getParameter("rrnum"); //댓글 고유번호
		String sessionId = session.getAttribute("memberId").toString(); // 로그인한 사람의 아이디
		

		
		if(sessionId.equals(rrid)) {
			
			IDao dao = sqlSession.getMapper(IDao.class);
			
			//댓글 개수 감소
			//dao.rrcountMinus(rrorinum);
			
			//댓글을 달려는 게시물이 보이게 만듦(DB에서 파일 불러옴)
			RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
			dao.rrdelete(rrorinum, rrid, rrnum);
			model.addAttribute("rfbView",rfboardDto);
			
			
			
		} else {
			PrintWriter out;
			
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('본인이 쓴 글이 아니면 삭제할 수 없습니다.');history.go(-1);</script>");
				out.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
		return "board_view";
	}
	
	@RequestMapping("/search_list")
	public String search_list(HttpServletRequest request, Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		
		//초기값 설정
		ArrayList<RFBoardDto> boarddtos = null;  
		
		String searchKey = request.getParameter("searchKey");
		String searchOption = request.getParameter("searchOption");
		
		if(searchOption.equals("title")) {
			boarddtos = dao.rfbSearchTitleList(searchKey);			
			
		} else if(searchOption.equals("content")) {
			boarddtos = dao.rfbSearchContentList(searchKey);			
		
		} else if(searchOption.equals("writer")) {
			boarddtos = dao.rfbSearchWriterList(searchKey);			
					
		} else {
			model.addAttribute("없는 결과입니다.",null);
		}
		
		model.addAttribute("boardList",boarddtos);
		//검색 결과 게시물의 개수 반환
		model.addAttribute("boardCount",boarddtos.size());
		
		
		return "board_list";
	}
	
	
	@RequestMapping("/file_down")
	public String file_down(HttpServletRequest request, HttpServletResponse response) {
		
		String rfbnum = request.getParameter("rfbnum");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		FileDto fileDto = dao.getFileInfo(rfbnum);
		
		String filename= fileDto.getFilename();
		
		PrintWriter out;
		
		try {
			response.setContentType("text/html;charset=utf=8");
			out = response.getWriter();
			out.println("<script>window.location='/resources/uploadfiles/'"+ filename + "</script>");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "redirect:board_list";
	}
	
	
}
