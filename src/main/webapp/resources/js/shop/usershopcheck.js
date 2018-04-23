/**
 * Created by cheng on 2018/4/23.
 */

(function () {

    'use strict';

    // 获取该店铺用户积分的 url
    var listUrl = '/o2o/shopadmin/listusershopmapsbyshop?pageIndex=0&pageSize=999&userName=',
        userName = '',
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel'),
        $checkList = $('#check-list');

    getList();

    function getList() {
        $.getJSON(listUrl + userName, function (data) {
            if (data.success) {

                var userShopMapList = data.userShopMapList,
                    userShopHtml = '';

                userShopMapList.map(function (item) {
                    userShopHtml += '<div class="weui-cell"><div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.user.name + '</label></div>' +
                        '<div class="weui-cell__hd">' +
                        '<label class="weui-label">' + item.point + '</label></div></div>';
                });

                $checkList.html(userShopHtml);
            } else {
                weui.alert('初始化失败!');
            }
        });
    }

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
        userName = e.target.value;
        $searchResult.empty();

        // 当在搜索框内里输入信息的时候
        // 依据输入的用户名模糊查询该商品的购买记录
        // 清空商品购买记录列表
        $checkList.empty();
        // 再次加载
        getList();
    });

    // 取消搜索
    $searchClear.on('click', function () {
        $searchInput.val('');
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
})();