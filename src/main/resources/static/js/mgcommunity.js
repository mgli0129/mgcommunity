//时间转换方法
function getTs(time) {
    var arr = time.split(/[- :]/),
        _date = new Date(arr[0], arr[1] - 1, arr[2], arr[3], arr[4], arr[5]),
        timeStr = Date.parse(_date);
    return timeStr;
}

function handlePublishTimeDesc(sourceDate, withTime) {
    // 拿到当前时间戳和发布时的时间戳，然后得出时间戳差
    var curTime = new Date();
    var timeDiff = curTime.getTime() - getTs(sourceDate);

    // 单位换算
    var min = 60 * 1000;
    var hour = min * 60;
    var day = hour * 24;
    var week = day * 7;
    var month = week * 4;
    var year = month * 12;

    // 计算发布时间距离当前时间的周、天、时、分
    var exceedyear = Math.floor(timeDiff / year);
    var exceedmonth = Math.floor(timeDiff / month);
    var exceedWeek = Math.floor(timeDiff / week);
    var exceedDay = Math.floor(timeDiff / day);
    var exceedHour = Math.floor(timeDiff / hour);
    var exceedMin = Math.floor(timeDiff / min);

    //三个月以上的输出为年月日
    var outDate = moment(sourceDate).format('YYYY-MM-DD');
    var outDateWithTime = moment(sourceDate).format('YYYY-MM-DD HH:mm:ss');

    // 最后判断时间差到底是属于哪个区间，然后return

    if (exceedmonth > 3) {
        return (withTime == 'Y') ? outDateWithTime : outDate;
    } else if (exceedmonth < 4 && exceedmonth > 0) {
        return exceedmonth + '月前';
    } else {
        if (exceedWeek < 4 && exceedWeek > 0) {
            return exceedWeek + '星期前';
        } else {
            if (exceedDay < 7 && exceedDay > 0) {
                return exceedDay + '天前';
            } else {
                if (exceedHour < 24 && exceedHour > 0) {
                    return exceedHour + '小时前';
                } else {
                    return exceedMin + '分钟前';
                }
            }
        }
    }
}

//发布评论公共代码
function comComment(parentId, content, type) {
    if (!content) {
        alert("评论的内容不能为空");
        return null;
    }

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
                    "type": type
                }),
        success: function f(response) {
            switch (response.code) {
                case "0000":
                    //$("#comment_area").hide();
                    window.location.reload();
                    break;
                case "2003":
                    if (confirm(response.message)) {
                        window.open("/login");
                        window.localStorage.setItem("closable", "Y");
                    }
                    break;
                default:
                    alert(response.message);
            }
        },
        dataType: 'json'
    })
}

//发布问题的评论
function postComment() {
    var parentId = $("#parentId").val();
    var content = $("#content").val();
    var type = 1;
    if (!content) {
        alert("评论的内容不能为空");
        return null;
    }
    comComment(parentId, content, type);
}

//发布评论的评论（二级评论）
function postSubComment(e) {
    var parentId = e.getAttribute("data-id");
    var content = $("#input-" + parentId).val();
    var type = 2;
    if (!content) {
        alert("评论的内容不能为空");
        return null;
    }
    comComment(parentId, content, type);
}

//点开二级评论
function collapseComments(e) {
    var parentId = e.getAttribute("data-id");
    var comments = $("#comment-" + parentId);
    var collapse = e.getAttribute("data-collapse");

    if (collapse) {
        //折叠评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {//展开评论

        //如果没有提交二级评论再点击折叠，不再重复获取数据；
        if(comments.children("div").length >1){
            comments.addClass("in");
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
            return;
        }

        //获取数据
        $.ajax({
            type:
                'GET',
            contentType:
                'application/json',
            url:
                "/comment/" + parentId,
            success: function f(response) {
                switch (response.code) {
                    case "0000":

                        $.each(response.data.reverse(), function (index, comment) {

                            //格式化评论发布时间
                            var tmpTime = moment(comment.gmtModified).format('YYYY-MM-DD HH:mm:ss');
                            var gmtModified = handlePublishTimeDesc(tmpTime, "N");

                            var mediaBodyContentElement = $("<div/>", {
                                "class": "media-body"
                            }).append($("<span/>", {
                                "class": "comment_content",
                                "html": comment.content
                            }));

                            var mediaBodyElement = $("<div/>", {
                                "class": "media-body"
                            }).append($("<span/>", {
                                "html": comment.user.name
                            })).append($("<span/>", {
                                "class": "pull-right comment_content_date",
                                "html": gmtModified
                            })).append(mediaBodyContentElement);

                            var mediaLeftElement = $("<div/>", {
                                "class": "media-left",
                            }).append($("<img/>", {
                                "class": "media-object img-rounded",
                                "src": comment.user.avatarUrl
                            }));

                            var commentElement = $("<div/>", {
                                "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 sub-commented-2"
                            }).append(mediaLeftElement).append(mediaBodyElement);
                            comments.prepend(commentElement);
                        });

                        comments.addClass("in");
                        e.setAttribute("data-collapse", "in");
                        e.classList.add("active");

                        break;
                    case "2003":
                        if (confirm(response.message)) {
                            window.open("/login");
                            window.localStorage.setItem("closable", "Y");
                        }
                        break;
                    default:
                        alert(response.message);
                }
            },
            dataType: 'json'
        })
    }
}

//设置标签内容
function setTag(e) {
    var data = e.getAttribute("data-sTag");
    var tags = $("#tag").val();
    if(tags){
        tags = tags +","+data;
    }else {
        tags=data;
    }
    $("#tag").val(tags);
}

//显示可选标签框
function showSelectTags(){
    //给第一个li增加active class
    $("#selectTags").children("ul").children("li").eq(0).addClass("active");
    //给第一个sheet设置active class
    $("#selectTags").children("div").eq(0).children("div").eq(0).addClass("active");
    //显示标签框
    $("#selectTags").show();
}

function getHotTab(e) {
    var tag = e.getAttribute("data-topic");
    var id = e.getAttribute("data-id");
    var url = "/hottopics/" + id + "?tag=" + tag;
    window.open(url, "_self");
    return true;
}
