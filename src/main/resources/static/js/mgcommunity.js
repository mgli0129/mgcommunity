function postComment() {
    var parentId = $("#parentId").val();
    var content = $("#content").val();

    $.ajax({
        type:
            'POST',
        contentType:
            'application/json',
        url:
            "/comment",
        data:
            JSON.stringify(
                {
                    "parentId": parentId,
                    "content": content,
                    "type": 1
                }),
        success: function f(response) {
            if(response.code == "0000"){
                $("#comment_area").hide();
            }
        },
        dataType: 'json'
    })
}
