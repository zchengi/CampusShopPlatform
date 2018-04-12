/**
 * Created by cheng on 2018/4/11.
 */

$(function () {

    'use strict';

    var loading = false,
        // 分页允许返回的最大条数，超过此数则禁止访问后台
        maxItems = 999,
        // 默认一页返回的最大条数
        pageSize = 2,
        // 默认页码
        pageNum = 1,
        // 获取店铺列表的 URL
        listUrl = '/frontend/listshops',
        // 获取店铺类别列表以及区域列表的 URL
        searchDivUrl = '/frontend/listshopspageinfo',
        // 店铺详情页拼接
        shopdetail = '/frontend/shopdetail?shopId=',
        // 从地址栏 URL 里尝试获取 parent shop category id
        parentId = common.getQueryString('parentId'),
        areaId = '',
        shopCategoryId = '',
        shopName = '',
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel'),
        $shopListSearch = $('#shopListSearch');

    // 渲染出店铺类别列表以及区域列表以供搜索
    getSearchDivData();
    // 预先加载2条店铺信息
    addItems(pageSize, pageNum);

    /**
     * 获取店铺类别列表以及区域列表信息
     */
    function getSearchDivData() {
        // 如果传入了 parentId，则取出此一级类别下面的所有二级类别
        var url = searchDivUrl + '?parentId=' + parentId;

        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取后台返回的店铺类别列表
                var shopCategoryList = data.shopCategoryList,
                    shopCategoryHtml = '',
                    preHtml = '<div class="weui-flex weui-btn-area_inline my-flex">',
                    nextHtml = '</div>',
                    selectOptions = '<option value="">全部街道</option>';

                shopCategoryHtml += preHtml + '<a href="javascript:;" data-category-id="" ' +
                    'class="weui-btn weui-btn_plain-primary weui-btn_mini my-btn_primary">全部类别</a>';

                // 遍历店铺类别列表，并拼接出a标签集
                shopCategoryList.map(function (item, index) {
                    if ((index + 1) % 3 === 0) {
                        shopCategoryHtml += nextHtml + preHtml;
                    }
                    shopCategoryHtml += '<a href="javascript:;" ' +
                        'data-category-id="' + item.shopCategoryId + '" ' +
                        'class="weui-btn weui-btn_plain-primary weui-btn_mini my-btn_primary">' +
                        item.shopCategoryName + '</a>';
                });
                shopCategoryHtml += nextHtml;

                // 将拼接好的类别标签嵌入前台的html组件里
                $('.weui-btn-area').html(shopCategoryHtml);

                // 获取后台返回的区域信息列表
                data.areaList.map(function (item) {
                    selectOptions += '<option value="' + item.areaId + '">' + item.areaName + '</option>';
                });

                // 将标签集添加到 area 列表里
                $('#area-search').html(selectOptions);

            } else {
                weui.alert('查询失败，' + data.errMsg);
            }
        });
    }

    /**
     * 获取分页展示的店铺列表信息
     */
    function addItems(pageSize, pageIndex) {
        // 拼接出查询的 URL，赋空值默认就去掉这个条件的限制，有值就代表按这个条件去查询
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize +
            '&parentId=' + parentId + '&areaId=' + areaId +
            '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;

        // 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
        loading = true;

        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取当前查询条件下店铺的总数
                maxItems = data.count;
                var shopListHtml = '';
                // 遍历店铺列表，拼接出卡片集合
                data.shopList.map(function (item) {
                    shopListHtml += '<div class="weui-cells" data-shop-id="' + item.shopId + '">' +
                        '<div class="weui-cell weui-cell__bd">' +
                        '<p>' + item.shopName + '</p></div>' +
                        '<div class="weui-cell"><div class="weui-cell_primary">' +
                        '<img width="44" src="' + item.shopImg + '"></div>' +
                        '<div class="weui-cell__bd my-cell_bd">' +
                        '<p>' + item.shopDesc + '</p></div></div>' +
                        '<div class="weui-cell"><div class="weui-cell__bd">' +
                        '<p>' + new Date(item.lastEditTime).Format('yyyy-MM-dd') + ' 更新</p></div>' +
                        '<div class="weui-cell__ft">' +
                        '<a href="javascript:;" class="weui-cell_link">点击查看</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
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
                }else {
                    // 显示下滑加载控件
                    $('.weui-loadmore').show();
                }

                // 否则页码加1，继续load出新的店铺
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

    // 点击店铺的 div 进入该店铺的详情页
    $searchResult.on('click', '.weui-cells', function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = shopdetail + shopId;
    });

    // 选择新的店铺类别后，重置页码，清空原来的店铺列表，按照新的类别去查询
    $shopListSearch.on('click', '.weui-btn', function (e) {
        var target = e.target;
        if (parentId) {
            // 如果传递过来的是一个父类下的子类
            shopCategoryId = target.dataset.categoryId;
            // 如果之前已经选定了当前 category，则移除其选定效果，改为选定新的
            if ($(target).hasClass('my-btn_fill')) {
                $(target).removeClass('my-btn_fill');
                shopCategoryId = '';
            } else {
                // 清空所有 fill 状态
                $shopListSearch.find('.weui-btn').removeClass('my-btn_fill');
                $(target).addClass('my-btn_fill');
            }

            // 由于查询条件改变，清空店铺列表再进行查询
            $searchResult.empty();
            // 重置页码
            pageNum = 1;
            addItems(pageSize, pageNum);
        } else {
            // 如果传递过来的父类的为空，则按照父类查询
            parentId = target.dataset.categoryId;
            if ($(target).hasClass('my-btn_fill')) {
                $(target).removeClass('my-btn_fill');
                parentId = '';
            } else {
                // 清空所有 fill 状态
                $shopListSearch.find('.weui-btn').removeClass('my-btn_fill');
                $(target).addClass('my-btn_fill');
            }

            // 由于查询条件改变，清空店铺列表再进行查询
            $searchResult.empty();
            // 重置页码
            pageNum = 1;
            addItems(pageSize, pageNum);
            parentId = '';
        }
    });

    // 区域信息发生变化后，重置页码，清空原先的店铺列表，按照新的区域去查询
    $('#area-search').on('change', function () {
        areaId = $('#area-search').val();
        $searchResult.empty();
        pageNum = 1;
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
