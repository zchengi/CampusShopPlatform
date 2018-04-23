/**
 * Created by cheng on 2018/4/12.
 */

$(function () {

    'use strict';

    var loading = false,
        // 分页允许返回的最大条数，超过此数则禁止访问后台
        maxItems = 20,
        // 默认一页返回的最大条数
        pageSize = 2,
        // 默认页码
        pageNum = 1,
        // 获取商品列表的 URL
        listUrl = '/o2o/frontend/listproductsbyshop',
        shopId = common.getQueryString('shopId'),
        productCategoryId = '',
        productName = '',
        // 获取本店铺信息以及商品类别信息列表的 URL
        searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId,
        // 商品详情页 url
        productDetailUrl = '/o2o/frontend/productdetail?productId=',
        // 兑换礼品 url
        awardListUrl = '/o2o/frontend/awardlist?shopId=',
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel'),
        $shopListSearch = $('#shopListSearch');

    // 渲染出店铺基本信息以及商品类别列表以供搜索
    getSearchDivData();

    // 预先加载2条商品信息
    addItems(pageSize, pageNum);

    // 给兑换礼品的a标签赋值兑换礼品的 url
    $('#exchangeList').attr('href', awardListUrl + shopId);

    /**
     * 获取本店铺信息以及商品类别信息列表
     */
    function getSearchDivData() {
        $.getJSON(searchDivUrl, function (data) {
            if (data.success) {
                var shop = data.shop,
                    // 获取后台返回的该店铺的商品类别列表
                    productCategoryList = data.productCategoryList,
                    preHtml = '<div class="weui-flex weui-btn-area_inline my-flex">',
                    nextHtml = '</div>';
                $('#shop-cover-pic').attr('src', common.getContextPath() + shop.shopImg).attr('alt', shop.shopName);
                $('#shop-update-time').html(new Date(shop.lastEditTime).Format('yyyy-MM-dd'));
                $('#shop-name').html(shop.shopName);
                $('#product-desc').html(shop.shopDesc);
                $('#shop-addr').html(shop.shopAddr);
                $('#shop-phone').html(shop.phone);
                $('#award-list').attr('href', '/o2o/frontend/awardlist?shopId=' + shopId);

                // 获取后台返回的该店铺的商品类别列表
                var productCategoryHtml = preHtml;
                productCategoryList.map(function (item, index) {
                    if ((index + 1) % 3 === 0) {
                        productCategoryHtml += nextHtml + preHtml;
                    }
                    // 遍历商品列表，生成可以点击搜索相应商品类别下的商品的a标签
                    productCategoryHtml += '<a href="javascript:;" ' +
                        'data-category-id="' + item.productCategoryId + '" ' +
                        'class="weui-btn weui-btn_plain-primary weui-btn_mini my-btn_primary">' +
                        item.productCategoryName + '</a>';
                });

                productCategoryHtml += nextHtml;
                // 将商品类别a标签绑定到相应的HTML组件中
                $('.weui-btn-area').html(productCategoryHtml);
            } else {
                weui.alert('查询失败，' + data.errMsg);
            }
        });
    }

    /**
     * 获取分页展示的商品列表信息
     */
    function addItems(pageSize, pageIndex) {
        // 拼接出查询的 URL，赋空值默认就去掉这个条件的限制，有值就代表按这个条件去查询
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize +
            '&productCategoryId=' + productCategoryId + "&productName=" + productName +
            '&shopId=' + shopId;

        // 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
        loading = true;

        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取当前查询条件下商品的总数
                maxItems = data.count;
                var productListHtml = '';

                // 遍历商品列表，拼接出其HTML
                data.productList.map(function (item) {
                    productListHtml += '<div class="weui-cells" data-product-id="' + item.productId + '">' +
                        '<div class="weui-cell weui-cell__bd">' +
                        '<p>' + item.productName + '</p></div>' +
                        '<div class="weui-cell"><div class="weui-cell_primary">' +
                        '<img width="44" src="' + common.getContextPath() + item.imgAddr + '"></div>' +
                        '<div class="weui-cell__bd my-cell_bd">' +
                        '<p>' + item.productDesc + '</p></div></div>' +
                        '<div class="weui-cell"><div class="weui-cell__bd">' +
                        '<p>' + new Date(item.lastEditTime).Format('yyyy-MM-dd') + ' 更新</p></div>' +
                        '<div class="weui-cell__ft">' +
                        '<a href="javascript:;" class="weui-cell_link">点击查看</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                });

                // 将product集合添加到目标HTML组件中
                $searchResult.append(productListHtml);
                // 获取目前为止已经显示的 product 总数，包含之间加载的
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

                // 否则页码加1，继续load出新的商品
                pageNum += 1;
                // 加载结束，可以再次加载了
                loading = false;
            } else {
                weui.alert('查询失败，' + data.errMsg);
            }
        });
    }

    //下滑屏幕自动进行分页搜索
    $(document.body).infinite().on('infinite', function () {
        if (loading) {
            return;
        }
        addItems(pageSize, pageNum);
    });

    // 点击商品的 div 进入该商品的详情页
    $searchResult.on('click', '.weui-cells', function (e) {
        var productId = e.currentTarget.dataset.productId;
        window.location.href = productDetailUrl + productId;
    });

    // 选择新的商品类别后，重置页码，清空原来的商品列表，按照新的类别去查询
    $shopListSearch.on('click', '.weui-btn', function (e) {
        var target = e.target;
        // 获取商品类别id
        productCategoryId = target.dataset.categoryId;
        if (productCategoryId) {
            // 如果之前已经选定了当前 category，则移除其选定效果，改为选定新的
            if ($(target).hasClass('my-btn_fill')) {
                $(target).removeClass('my-btn_fill');
                productCategoryId = '';
            } else {
                // 清空所有 fill 状态
                $shopListSearch.find('.weui-btn').removeClass('my-btn_fill');
                $(target).addClass('my-btn_fill');
            }

            // 由于查询条件改变，清空商品列表再进行查询
            $searchResult.empty();
            // 重置页码
            pageNum = 1;
            addItems(pageSize, pageNum);
        }
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
        productName = e.target.value;
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
