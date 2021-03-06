<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>나의 홈페이지</title>
<!-- github /sockjs의 Sockjs client의 getting started가면 있음 -->
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

<script>

	var socket = new SockJS('http://192.168.4.12:8080/socket/chat');
	// socket서버에 접속 시작하라
	socket.onopen = function() {
		
		// socket이 서버에 접속 성공한 후 최초에 실행될 코드
		let userName = prompt(" 채팅방 입장!\n 이름을 입력해주세요!")
		// 창에서 esc를 누르면 null값이 입력됨
		// == userName이 null값이 아니고 ""이 아니면
		if(userName && userName != ""){
			socket.send("userName:" + userName)	// userName:홍길동(':'을 기준으로 문자열을 split했기때문에 띄어쓰면 안됨)
		}
		socket.send("getUserList:" + userName); 
	    // console.log('open');
	    
	    // 얘를 안지우니까 illigalStatementException이 발생함
	    // 우리가 보내는 데이터구조(json)와 여기서 보내는 데이터 구조(String)가 서로 달라서 GSON이 못받아서 난 오류
	    // socket.send('Hello Web-Socket! chat-server');
	};
	
	// 서버로부터 수신되는 이벤트를 처리
	socket.onmessage = function(e) {
	    console.log('message', e.data);
	    // alert(e.data);
		// 문자열형으로 수신된 json문자열을 json 객체로 변환	    
	    let mJson = JSON.parse(e.data)
	    // 서버로부터 전달받은 데이터에 msg라는 key가 있느냐
	    // key가 있으면 key가 userName이냐?
	    if(mJson.msg && mJson.msg == 'userName'){
	    	alert(mJson.userName)
	    	
	    	$('#userName').val(mJson.userName)
	    	$("#userName").prop("readonly", true)
	    	return false;
	    }
		
		// alert(mJson.msg)
		if(mJson.msg && mJson.msg == 'userList'){
			
			// alert(mJson.userList)
			
			let userList = JSON.parse(mJson.userList)
			
			// 사용자가 추가될때마다 리스트가 반복되어서 리스트를 모두 비우고 
			$("#toList").empty()
			
			// 다시 전체 사용자 리스트 추가
			$("#toList").append(
				// 동적 tag를 만드는 jquery코드
				$("<option/>", 
						{
							value:"all", 
							text:"전체"
						})
							
				  )	
				  
			// js코드 내장 객체 method
			// userList 객체 그룹중에서 각 요소의 키값만 추출하여
			// 배열로 만들어 달라
			let userListKeys = Object.keys(userList)
			
			// 각 요소의 key값만 추출
			for(let i = 0; i < userListKeys.length; i++){
				// console.log(userListKeys[i])
				console.log("키값에 따른 사용자 이름 : ", userList[userListKeys[i]].userName)
				$("#toList").append(
						
					// 동적 tag를 만드는 jquery 코드
					$("<option/>",
					{
						// value부분에 session아이디가 추가
						value:userListKeys[i],
						text:userList[userListKeys[i]].userName
					})
				)
				
			}
			// return false를 해주지 않으면 사용자를 추가할때마다 
			// 메시지/username이 수신되어 undefined로 뜸 
			return false;
			
		}
	    
	    let html = "<div class='from col-6 text-left'>" + "<span class='userName'>" + mJson.userName + " >> " + "</span>" + mJson.message + "</div>"
	    
	    $("#message_list").append(html)
	    $("#message_list").scrollTop(
			$("#message_list").prop('scrollHeight')		
		)
	    // sock.close();
	};
	
	// socket서버와 접속 종료
	// sock.onclose = function() {
	//    console.log('close');
	//};
</script>
<script>
	$(function(){
		
		$(document).on("submit", "form", function(){
			event.preventDefault();
			// let message = $("#message").val()
			// $("#message_list").append("me >>  " + message + "<br />")
			// socket.send(message)
		})
		
		
		 $(document).on("click","#btn_send",function(){
			 
			let toUserId = $("#toList").val()
			let toUserName = $("#toList option:checked").text()
			 
			let userName = $("#userName").val()
			
			if(userName == ""){
				alert("이름을 입력하세요!")
				return false
			}
			// userName과 message를 묶어서 JSON데이터로 생성
			let message = {
					userName : userName,
					toUser : toUserId,
					message : $("#message").val()
			}
			
			let html = "<div class='to text-right'>" + "<span class='userName'>" +  "나 >> " + "</span>" + message.message 
				html += "<span>(" + toUserName + ")<span>" + "</div>"
				
			$("#message_list").append(html)
			$("#message_list").scrollTop(
				$("#message_list").prop('scrollHeight')		
			)
			
			// socket을 통해 json 데이터를 보내기 위해
			// json형 문자열로 변환시킨 후 전송
			socket.send(JSON.stringify(message))
			$("#message").val("")
		 })
		 // socket.send("getUserList")
		
	})

</script>
</head>
<style>

#chat-box{
	border: 1px solid black;
	height: 70vh;
	width : 500px;
	overflow: auto;
}

.from, .to {
	padding : 7px;
	margin-bottom: 2rem;
}
.to {
	margin: 0 0 32px 210px;
}

.userName {
	color : blue;
	font-weight: bold;
	font-style: italic;
}
#message_list{
	border : 1px solid red;
	height: 45vh;
	overflow: auto;
}

</style>
<body>
<header class="jumbotron text-center bg-success">
	<h2 class="text-white"> MY CHATTING SERVICE</h2>
</header>


<br/>
<br/>
<section id="chat-box" class="container-fluid">

	<br/>
	<p>Have Fun!</p>
	<hr/>

	<div id="message_list" ></div>
	
	
	<div>
		<form>
			<div class="form-group">
				<input type="hidden" id="userName" class="form-control" placeholder="보내는 사람"><br/>
			</div>
			
			<div class="form-inline">
				<select id="toList" class="form-control col-3">
						<option value="all">전체</option>
				</select>
				<input id="message"  class="form-control col-7" placeholder="메시지를 입력하세요">
				<button id="btn_send" class="btn btn-primary col-2">전송</button>
			</div>
			
		</form>
	</div>
	
</section>




</body>
</html>