function post() {
    const questionId = $("#question_id").val();
    const content = $("#comment_content").val();
    if (!content) {
        alert("不能回复空内容")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code === 200) {
                window.location.reload();
            } else {
                if (response.code === 2003) {
                    const isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("/login");
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}
