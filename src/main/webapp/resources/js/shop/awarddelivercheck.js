/**
 * Created by cheng on 2018/4/23.
 */

(function () {

    'use strict';

    // 获取该店铺用户积分的 url
    var listUrl = '/o2o/shopadmin/listuserawardmapsbyshop?pageIndex=0&pageSize=999&awardName=',
        awardName = '',
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel'),
        $checkList = $('#check-list');

    getList();

    function getList() {
        $.getJSON(listUrl + awardName, function (data) {
            if (data.success) {

                var userAwardMapList = data.userAwardMapList,
                    userAwardHtml = '';

                userAwardMapList.map(function (item) {
                    userAwardHtml += '<div class="weui-cell">' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.award.awardName + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-textarea">' +
                        new Date(item.createTime).Format('yyyy-MM-dd hh:mm:ss') + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.user.name + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.point + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.operator.name + '</label>' +
                        '</div>' +
                        '</div>';
                });

                $checkList.html(userAwardHtml);
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
        awardName = e.target.value;
        $searchResult.empty();

        // 当在搜索框内里输入信息的时候
        // 依据输入的奖品名模糊查询该商品的购买记录
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