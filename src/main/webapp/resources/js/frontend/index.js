/**
 * Created by cheng on 2018/4/10.
 */

(function () {

    // 定义访问后台，获取头条列表以及一级类别列表的 URL
    var url = '/frontend/listmainpageinfo';

    // 访问后台，获取头条列表以及一级类别列表
    $.getJSON(url, function (data) {
        if (data.success) {
            // 获取后台传递过来的头条列表
            var headLineList = data.headLineList;
            var swiperHtml = '';

            // 遍历头条列表，并拼接出轮播图组
            headLineList.map(function (item) {
                swiperHtml += '<div class="swiper-slide">'
                    + '<a href ="' + item.lineLink + '">'
                    // ' + item.linkImg + ' 替换 src
                    + '<img class="banner-img" src="//gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i1/TB1n3rZHFXXXXX9XFXXXXXXXXXX_!!0-item_pic.jpg_320x320q60.jpg" alt="' + item.lineName + '">'
                    + '</a></div>';
            });

            // 将轮播图组赋值给前端HTML控件
            $('.swiper-wrapper').html(swiperHtml);

            // 设定轮播图轮换时间为3秒
            $('.swiper-container').swiper({
                autoplay: 3000,
                // 用户对轮播图操作时，是否自动停止 autoplay
                autoPlayDisableOnInteraction: false
            });

            // 获取后台传递过来大类列表
            var shopCategoryList = data.shopCategoryList;
            console.log(shopCategoryList);
            var categoryHtml = '';
            // 遍历大类列表，拼接出九宫格
            shopCategoryList.map(function (item) {
                categoryHtml += '<a href="javascript:;" class="weui-grid shop-classify"'
                    + ' data-category="' + item.shopCategoryId + '">'
                    + '<div class="weui-grid__icon">'
                    + '<img src="' + item.shopCategoryImg + '" alt="' + item.shopCategoryDesc + '">'
                    + '</div>'
                    + '<p class="weui-grid__label">' + item.shopCategoryName + '</p>'
                    + '</a>';
            });

            // 将拼接好的类别赋值给前端HTML控件进行展示
            // $('.weui-grids').html(categoryHtml);
        } else {

        }
    });

    // 给每个类别绑定 click 事件
    $('.weui-grid').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/fronted/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });

})();