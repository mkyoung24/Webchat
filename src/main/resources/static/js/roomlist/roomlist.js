let roomId;

$(function(){
    let $maxChk = $("#maxChk");
    let $maxUserCnt = $("#maxUserCnt");

    // 모달창 열릴 때 이벤트 처리 => roomId 가져오기
    $("#enterRoomModal").on("show.bs.modal", function (event) {
        roomId = $(event.relatedTarget).data('id');
        // console.log("roomId: " + roomId);

    });

    $("#confirmPwdModal").on("show.bs.modal", function (e) {
        roomId = $(e.relatedTarget).data('id');
        // console.log("roomId: " + roomId);

    });

    // 채팅방 설정 시 비밀번호 확인 - keyup 펑션 활용
    $("#confirmPwd").on("keyup", function(){
        let $confirmPwd = $("#confirmPwd").val();
        const $configRoomBtn = $("#configRoomBtn");
        let $confirmLabel = $("#confirmLabel");

        $.ajax({
            type : "post",
            url : "/chat/confirmPwd/"+roomId,
            data : {
                "roomPwd" : $confirmPwd
            },
            success : function(result){
                // console.log("동작완료")

                // result 의 결과에 따라서 아래 내용 실행
                if(result){ // true 일때는
                    // $configRoomBtn 를 활성화 상태로 만들고 비밀번호 확인 완료를 출력
                    $configRoomBtn.attr("class", "btn btn-primary");
                    $configRoomBtn.attr("aria-disabled", false);

                    $confirmLabel.html("<span id='confirm'>비밀번호 확인 완료</span>");
                    $("#confirm").css({
                        "color" : "#0D6EFD",
                        "font-weight" : "bold",
                    });

                }else{ // false 일때는
                    // $configRoomBtn 를 비활성화 상태로 만들고 비밀번호가 틀립니다 문구를 출력
                    $configRoomBtn.attr("class", "btn btn-primary disabled");
                    $configRoomBtn.attr("aria-disabled", true);

                    $confirmLabel.html("<span id='confirm'>비밀번호가 틀립니다</span>");
                    $("#confirm").css({
                        "color" : "#FA3E3E",
                        "font-weight" : "bold",
                    });

                }
            }
        })
    })

    // 기본은 유저 설정 칸 미활성화
    $maxUserCnt.hide();

    // 체크박스 체크에 따라 인원 설정칸 활성화 여부
    $maxChk.change(function(){
        if($maxChk.is(':checked')){
            $maxUserCnt.val('');
            $maxUserCnt.show();
        }else{
            $maxUserCnt.hide();
        }
    })

})


// 채팅방 생성
function createRoom() {

    let name = $("#roomName").val();
    let pwd = $("#roomPwd").val();
    let secret = $("#secret").is(':checked');
    let secretChk = $("#secretChk");
    let $maxUserCnt = $("#maxUserCnt");

    // console.log("name : " + name);
    // console.log("pwd : " + pwd);

    if (name === "") {
        alert("방 이름은 필수입니다")
        return false;
    }
    if ($("#" + name).length > 0) {
        alert("이미 존재하는 방입니다")
        return false;
    }
    if (pwd === "") {
        alert("비밀번호는 필수입니다")
        return false;
    }

    // 최소 방 인원 수는 2
    if($maxUserCnt.val() <= 1){
        alert("혼자서는 채팅이 불가능해요ㅠ.ㅠ");
        return false;
    }

    if (secret) {
        secretChk.attr('value', true);
    } else {
        secretChk.attr('value', false);
    }

    return true;
}

// 채팅방 입장 시 비밀번호 확인
function enterRoom(){
    let $enterPwd = $("#enterPwd").val();

    $.ajax({
        type : "post",
        url : "/chat/confirmPwd/"+roomId,
        async : false,
        data : {
            "roomPwd" : $enterPwd
        },
        success : function(result){
            // console.log("동작완료")
            // console.log("확인 : "+chkRoomUserCnt(roomId))

            if(result){
                if (chkRoomUserCnt(roomId)) {
                    location.href = "/chat/room?roomId="+roomId;
                }
            }else{
                alert("비밀번호가 틀립니다. \n 비밀번호를 확인해주세요")
            }
        }
    })
}

// 채팅방 삭제
function delRoom(){
    location.href = "/chat/delRoom/"+roomId;
}

// 채팅방 입장 시 인원 수에 따라서 입장 여부 결정
function chkRoomUserCnt(roomId){
    let chk;

    // 비동기 처리 설정 false 로 변경 => ajax 통신이 완료된 후 return 문 실행
    // 기본설정 async = true 인 경우에는 ajax 통신 후 결과가 나올 때까지 기다리지 않고 먼저 return 문이 실행되서
    // 제대로된 값 - 원하는 값 - 이 return 되지 않아서 문제가 발생한다.
    $.ajax({
        type : "GET",
        url : "/chat/chkUserCnt/"+roomId,
        async : false,
        success : function(result){

            // console.log("여기가 먼저")
            if (!result) {
                alert("채팅방이 꽉 차서 입장 할 수 없습니다");
            }

            chk = result;
        }
    })
    return chk;
}