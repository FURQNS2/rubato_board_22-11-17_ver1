<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>클래식기타 커뮤니티</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/header.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/footer.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/board_left.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/board_view_main.css">
</head>
<body>
	<% 
		String sessionId = (String)session.getAttribute("memberId");
		//로그인 중이라면 로그인한 아이디가 저장되고 비로그인 중이면 sessionId==null이다.	
	%>
  <div id="wrap">
    <header> <!-- header 시작 -->
      <a href="index"><img id="logo" src="${pageContext.request.contextPath}/resources/img/logo.png"></a>
      <nav id="top_menu">
        HOME | 
     
     <!-- 로그인과 비로그인 구분 -->   
      <% if(sessionId == null){ %>  
        LOGIN
       <%}else{ %>
       	<a href="logout">LOOUT</a>
       <%} %>
        
         | 
 
      <!-- 로그인 중에는 글수정이 뜨게 만들음 -->
       <% if(sessionId == null){ %>	 
         	JOIN 
       <% }else{ %>
       		MODIFY
	   <% } %>     
         
         | NOTICE
      </nav>
      <nav id="main_menu">
        <ul>
          <li><a href="board_list">자유게시판</a></li>
          <li><a href="#">기타 연주</a></li>
          <li><a href="#">공동 구매</a></li>
          <li><a href="#">연주회 안내</a></li>
          <li><a href="#">회원 게시판</a></li>
        </ul>
      </nav>
    </header> <!-- header 끝 -->
    <aside>
      <article id="login_box"> <!-- login box 시작 -->
        <img id="login_title" src="${pageContext.request.contextPath}/resources/img/ttl_login.png">
       
         <% if(sessionId == null){ %>	
        	<form action="loginok">
		        <div id="input_button">
		          <ul id="login_input">
		            <li><input type="text" name="mid"></li>
		            <li><input type="password" name="mpw"></li>
		          </ul>
		          <input type="image" id="login_btn" src="${pageContext.request.contextPath}/resources/img/btn_login.gif">
		        </div>
	        </form>
	    <div class="clear"></div>
        <div id="join_search">
          <a href="member_join"><img src="${pageContext.request.contextPath}/resources/img/btn_join.gif"></a>
          <img src="${pageContext.request.contextPath}/resources/img/btn_search.gif">
        </div>
	    <% }else{ %>
	    	<br><br><br>
	    	<h2>&nbsp;&nbsp;${memberId }님 로그인 중</h2>
	    	&nbsp;&nbsp;
	    	<input type="button" value="로그아웃" onclick="javascript:window.location='logout'">
	    <% } %>   
        
        
      </article> <!-- login box 끝 -->
      <nav id="sub_menu"> <!-- 서브 메뉴 시작 -->
        <ul>
          <li><a href="board_list">+ 자유 게시판</a></li>
          <li><a href="#">+ 방명록</a></li>
          <li><a href="#">+ 공지사항</a></li>
          <li><a href="#">+ 등업 요청</a></li>
          <li><a href="#">+ 포토갤러리</a></li>
        </ul>
      </nav> <!-- 서브 메뉴 끝 -->
      <article id="sub_banner">
        <ul>
          <li><img src="${pageContext.request.contextPath}/resources/img/banner1.png"></li>
          <li><img src="${pageContext.request.contextPath}/resources/img/banner2.png"></li>
          <li><img src="${pageContext.request.contextPath}/resources/img/banner3.png"></li>
        </ul>
      </article>
    </aside>
    <main>
      <section id="main">
        <img src="${pageContext.request.contextPath}/resources/img/comm.gif">
        <h2 id="board_title">자유게시판</h2>
        
        
        <div id="view_title_box">
          <span id="boardTitle">${rfbView.rfbtitle}</span>
          <span id="info">${rfbView.rfbname } | 조회수 : ${rfbView.rfbhit } | ${rfbView.rfbdate }</span>
        </div>   
        <p id="view_content">
          ${rfbView.rfbcontent } <br>
        </p>
        
        <!-- 저장한 파일 불러와서 화면에 보여주기 시작 -->     
        <c:if test="${filedto != null }">
	        <p id="file_info">
	        	<c:choose>
		        	<c:when test="${filedto.fileextension == 'jpg' or filedto.fileextension == 'png' or filedto.fileextension == 'bmp' }">
		        		<a href="file_down?rfbnum=${filedto.fileoriname }">
		        			<img src="${pageContext.request.contextPath}/resources/uploadfiles/${filedto.filename }" width="550" height="300"><br>
		        		</a>
		        		<a href="${pageContext.request.contextPath}/resources/uploadfiles/${filedto.filename }" download>
		        		※첨부파일:${filedto.fileoriname }
		        		</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="${pageContext.request.contextPath}/resources/uploadfiles/${filedto.filename }" download>
		        			※첨부파일:${filedto.fileoriname }
		        		</a>
		        	</c:otherwise>
	        	</c:choose>
	        </p>        
        </c:if>
        <!-- 저장한 파일 불러오기 끝 -->
        
        <!-- 댓글 출력란 시작 -->
        [댓글내용]
        <table width="750" border="1" cellpadding="0" cellspcing="0">
        	
           <c:forEach items="${replylist }" var="replyDto">
	          <tr>	            
	            <td class="col2">${replyDto.rrid}</td>
	            <td class="col3">${replyDto.rrcontent}</td>
	            <td class="col4">${replyDto.rrdate}</td>	   
	          	<td><input type="button" value="삭제" onclick="javascript:window.location='replyDelete?rrorinum=${replyDto.rrorinum }&rrid=${replyDto.rrid}&rrnum=${replyDto.rrnum }'"></td>
	          </tr>
	          
        	</c:forEach>
        	
        </table>
        <!-- 댓글 출력란 끝 -->
        
        <!-- 댓글 작성하는 곳 시작 -->
        <form action="replyOk">
        <input type="hidden" name="rfbnum" value="${rfbView.rfbnum }">
	        <div id="comment_box">
	          
	          <img id="title_comment" src="${pageContext.request.contextPath}/resources/img/title_comment.gif">	         
	          <textarea name="rrcontent"></textarea>	         
	          <input type="image" id="ok_ripple" src="${pageContext.request.contextPath}/resources/img/ok_ripple.gif">
	        </div>
        </form>
 		<!-- 댓글 작성하는 곳 끝 -->
 		
        <div id="buttons">
          <a href="delete?rfbnum=${rfbView.rfbnum}"><img src="${pageContext.request.contextPath}/resources/img/delete.png"></a>
          <a href="board_list"><img src="${pageContext.request.contextPath}/resources/img/list.png"></a>
          <a href="board_write"><img src="${pageContext.request.contextPath}/resources/img/write.png"></a>
        </div>

      </section> <!-- section main 끝 -->
    </main>
    <div class="clear"></div>
    <footer> <!-- footer 시작 -->
      <img id="footer_logo" src="${pageContext.request.contextPath}/resources/img/footer_logo.gif">
      <ul id="address">
        <li>서울시 강남구 삼성동 1234 (우) : 123-1234</li>
        <li>TEL : 02-1234-1234 Email : abc@abc.com</li>
        <li id="copyright">COPYRIGHT(C) 루바토 ALL RIGHTS RESERVED</li>
      </ul>
      <ul id="footer_sns">
        <li><img src="${pageContext.request.contextPath}/resources/img/facebook.gif"></li>
        <li><img src="${pageContext.request.contextPath}/resources/img/blog.gif"></li>
        <li><img src="${pageContext.request.contextPath}/resources/img/twitter.gif"></li>
      </ul>
    </footer> <!-- footer 끝 -->
  </div>
</body>
</html>