
$(function(){
    $(".table_body").on('click','.check-one',function(){
        var checkOne = true;
        $(".table_body").find('.check-one').each(function(){
            if(!this.checked){
                checkOne = false;
            }
        });
        $('.table_head').find('.check-all').each(function(){
            this.checked = checkOne;
        });
    });
    $('.table_head').on('click','.check-all',function(){
        var checkAll = this.checked;
        $('.check').each(function(){
            this.checked = checkAll;
        });
    });
})

function checkVals(){
    var array=new Array();
    $("input:checkbox[class='check check-one']:checked").each(
        function(){
            array.push($(this).val());
        });
    return array+"";
}

