$(document).ready(function() {


    var header_height = 50;
    $("#sidebar-toggle").click(function() {
        $(".global-container").toggleClass("open-sidebar");
    });


    $(".swipe-area").swipe({
        swipeStatus:function(event, phase, direction, distance, duration, fingers)
        {

            if (phase=="move" && direction =="right") {
                $(".global-container").addClass("open-sidebar");
                return false;
            }
            if (phase=="move" && direction =="left") {
                $(".global-container").removeClass("open-sidebar");
                return false;
            }
        }
    });

    //initialization
    $(".large-menu").css("width", $(".main-container").width());

    $(window).resize(function () {
        $(".large-menu").css("width", $(".main-container").width());
        if($(window).width()>992 && $(".global-container").hasClass("open-sidebar")){
            $(".global-container").removeClass("open-sidebar");
        }
    });

    $(".main-body").scroll(function () {
        if ($(".main-body").scrollTop() > header_height) {
            $(".large-menu").css("position", "fixed");
            $(".large-menu").css("top", "0");
        }
        else {
            $(".large-menu").css("position", "relative");
        }
    });

});
