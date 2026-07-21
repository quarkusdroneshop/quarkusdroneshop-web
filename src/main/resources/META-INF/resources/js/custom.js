$(document).ready(function(){
	



// Smooth Scroll to Anchors




$('a[href^="#"]:not(.carousel-control)').on('click',function (e) {
    e.preventDefault();

    var target = this.hash,
    $target = $(target);

    $('html, body').stop().animate({
        'scrollTop': $target.offset().top
    }, 900, 'swing', function () {
        window.location.hash = target;
    });
});




// Nav Shrink




$(window).scroll(function() {
  if ($(document).scrollTop() > 80) {
    $('nav').addClass('shrink');
  } else {
    $('nav').removeClass('shrink');
  }
});




// Back to Top




var $backToTop = $("#back-to-top");
$backToTop.hide();


$(window).on('scroll', function() {
  if ($(this).scrollTop() > 250) {
    $backToTop.fadeIn();
  } else {
    $backToTop.fadeOut();
  }
});

$backToTop.on('click', function(e) {
  $("html, body").animate({scrollTop: 0}, 900);
});




// Match Height




$(function() {
  $('.js-matchheight').matchHeight();
});




// Active Class




    $(window).scroll(function () {

        var y = $(this).scrollTop();

        $('nav a').each(function (event) {
            var element = $($(this).attr('href'));
            if (element !== undefined && element.offset() !== undefined){
                if (y >= element.offset().top - 75) {
                    $('nav a').not(this).removeClass('active');
                    $(this).addClass('active');
                }
            }
        });

    });

    new WOW().init();

    // ナビの #product 等へのアンカージャンプ (クリックによるハッシュ変化、
    // および #product 付き URL への直接アクセスの両方) はブラウザによって
    // native の scroll イベントを発火しないことがあり、その場合 WOW.js の
    // 可視判定が一切走らず該当セクション以下の要素が visibility:hidden の
    // まま残ってしまう (画面が真っ白/空白に見える不具合)。ページ読み込み時に
    // 既にハッシュが付いている場合、およびハッシュ変化のたびに scroll
    // イベントを強制発火して WOW.js に再チェックさせる。
    function forceWowRecheck() {
        setTimeout(function () {
            $(window).trigger('scroll');
        }, 50);
    }
    if (window.location.hash) {
        forceWowRecheck();
    }
    $(window).on('hashchange', forceWowRecheck);

    $(document).on('click', '[data-toggle="lightbox"]', function(event) {
        event.preventDefault();
        $(this).ekkoLightbox();
    });

});









