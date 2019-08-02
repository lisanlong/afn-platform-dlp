$(function(){
    var $lis =$('.header ul li')
/*    $lis.eq(0).click(function(){
        location.href ='3~1密级配置.html';
    })*/
    var $message =$('.header .message')
    var $box =$('.header .message .message_box')
    $message.hover(function(){
        $box.css('display','block');
    },function(){
        $box.css('display','none');
    })
    $box.hover(function(){
        $(this).show();
    })
    var $as =$('.header .message .message_box a')
/*    $as.eq(1).click(function(){
        location.href='1~登录页.html'
    })*/
    $as.each(function(){
        $(this).hover(function(){
            $(this).css('background-color','#f5f6ff')
        },function(){
            $(this).css('background-color','')
        })
    })

})
/*点击弹出按钮*/
function popBox() {
    var popBox = document.getElementById("popBox");
    var popLayer = document.getElementById("popLayer");
    popBox.style.display = "block";
    popLayer.style.display = "block";
};

/*点击关闭按钮*/
function closeBox() {
    var popBox = document.getElementById("popBox");
    var popLayer = document.getElementById("popLayer");
    popBox.style.display = "none";
    popLayer.style.display = "none";
}