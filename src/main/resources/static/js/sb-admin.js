(function ($) {
    "use strict"; // Start of use strict

    // Toggle the side navigation
    $("#sidebarToggle").on('click', function (e) {
        e.preventDefault();
        $("body").toggleClass("sidebar-toggled");
        $(".sidebar").toggleClass("toggled");
    });

    // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
    $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function (e) {
        if ($(window).width() > 768) {
            var e0 = e.originalEvent,
                delta = e0.wheelDelta || -e0.detail;
            this.scrollTop += (delta < 0 ? 1 : -1) * 30;
            e.preventDefault();
        }
    });

    // Scroll to top button appear
    $(document).on('scroll', function () {
        var scrollDistance = $(this).scrollTop();
        if (scrollDistance > 100) {
            $('.scroll-to-top').fadeIn();
        } else {
            $('.scroll-to-top').fadeOut();
        }
    });

    // Smooth scrolling using jQuery easing
    $(document).on('click', 'a.scroll-to-top', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top)
        }, 1000, 'easeInOutExpo');
        event.preventDefault();
    });

})(jQuery); // End of use strict

/* 初始化数据表格 */
$(document).ready(function () {
    $('#dataTable').DataTable({
        /* 这个也可以从远端加载数据 */
        "language": {
            "aria": {
                "sortAscending": " - 点击/返回升序排列", "sortDescending": " - 点击/返回降序排列"
            },
            "paginate": {
                "first": "首页", "last": "尾页", "next": "下一页", "previous": "上一页"
            },
            "emptyTable": "表内无可用的数据",
            "info": "显示第 _START_ 条至第 _END_ 条 - 共 _TOTAL_ 条数据",
            "infoEmpty": "无数据可显示",
            "infoFiltered": "（从 _MAX_ 条数据过滤）",
            "lengthMenu": "每页显示 _MENU_ 条",
            "loadingRecords": "请稍等，正在加载中..",
            "processing": "请稍等，正在处理中..",
            "search": "过滤：",
            "zeroRecords": "没有找到匹配记录"
        },
        "stateSave": true,
        "columnDefs": [
            {"type": "any-number", "targets": 0}
        ]
    });
});

/* restful 风格的删除请求弹窗 */
$('#deleteModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var uri = button.data('uri');
    var name = button.data('name');
    var modal = $(this);
    modal.find('.modal-title').text('确定要删除 [' + name + '] 吗？');
    $("#deleteForm").attr("action", uri);
});

/* 消息弹窗 */
$('#dialogModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var uri = button.data('uri');
    var name = button.data('name');
    var modal = $(this);
    modal.find('.modal-title').text('确定要删除 [' + name + '] 吗？');
    $("#deleteForm").attr("action", uri);
});
