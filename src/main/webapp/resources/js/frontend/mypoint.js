/**
 * Created by cheng on 2018/4/23.
 */

$(function () {

    'use strict';

    var loading = false,
        maxItems = 999,
        pageSize = 3,
        pageNum = 1,
        // 获取该用户的积分情况的 url
        listUrl = '/o2o/frontend/listusershopmapsbycustomer',
        shopName = '',
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel');

    // 预先加载3条奖品信息
    addItems(pageSize, pageNum);

    /**
     * 获取分页展示的奖品列表信息
     */
    function addItems(pageSize, pageIndex) {
        // 拼接出查询的 URL，赋空值默认就去掉这个条件的限制，有值就代表按这个条件去查询
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize + '&shopName=' + shopName;

        // 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
        loading = true;

        $.getJSON(url, function (data) {
            if (data.success) {

                // 获取当前查询条件下奖品的总数
                maxItems = data.count;

                var shopListHtml = '';
                // 遍历奖品列表，拼接出卡片集合
                data.userShopMapList.map(function (item) {

                    shopListHtml += '<div class="weui-cells" data-shop-id="' + item.shopId + '">' +
                        '<div class="weui-cell">' +
                        '<div class="weui-cell__bd">' + item.shop.shopName + '</div>' +
                        '</div>' +
                        '<div class="weui-cell">' +
                        '<div class="weui-cell__bd my-cell_bd">' + item.shop.shopName + '</div>' +
                        '</div>' +
                        '<div class="weui-cell">' +
                        '<div class="weui-cell__bd">更新时间：' + new Date(item.createTime).Format('yyyy-MM-dd') + '</div>' +
                        '<div class="weui-cell__ft">本店积分：' + item.point + '</div>' +
                        '</div></div>';
                });

                // 将shop集合添加到目标HTML组件中
                $searchResult.append(shopListHtml);

                // 获取目前为止已经显示的 shop 总数，包含之间加载的
                var total = $searchResult.find('.weui-cells').length;

                // 若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
                if (total >= maxItems) {
                    // 隐藏加载提示符
                    $('.weui-loadmore').hide();
                    return;
                } else {
                    // 显示下滑加载控件
                    $('.weui-loadmore').show();
                }

                // 否则页码加1，继续load出新的奖品
                pageNum += 1;
                // 加载结束，可以再次加载了
                loading = false;
            } else {
                weui.alert('查询失败，' + data.errMsg);
            }
        });
    }

    // 下滑屏幕自动进行分页搜索
    $(document.body).infinite().on('infinite', function () {
        if (loading) {
            return;
        }
        addItems(pageSize, pageNum);
    });


    // 点击搜索框
    $searchText.on('click', function () {
        $searchBar.addClass('weui-search-bar_focusing');
        $searchInput.focus();
    });

    // 失去焦点
    $searchInput.on('blur', function () {
        if (!this.value.length) cancelSearch();
    });

    // 获取焦点
    $searchInput.on('input', function (e) {
        shopName = e.target.value;
        $searchResult.empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    // 取消搜索
    $searchClear.on('click', function () {
        $searchInput.focus();
    });

    // searchCancel
    $searchCancel.on('click', function () {
        cancelSearch();
        $searchInput.blur();
    });

    // 取消搜索
    function cancelSearch() {
        $searchBar.removeClass('weui-search-bar_focusing');
        $searchText.show();
    }
});
