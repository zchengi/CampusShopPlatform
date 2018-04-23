/**
 * Created by cheng on 2018/4/23.
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
        // 获取奖品列表的 url
        listUrl = '/o2o/frontend/listawardsbyshop',
        // 兑换奖品的 url
        exchangeUrl = '/o2o/frontend/adduserawardmap',
        // 从地址栏 URL 里尝试获取 shopId
        shopId = common.getQueryString('shopId'),
        awardName = '',
        canProceed = false,
        totalPoint = 0,
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel');

    // 预先加载2条奖品信息
    addItems(pageSize, pageNum);

    /**
     * 获取分页展示的奖品列表信息
     */
    function addItems(pageSize, pageIndex) {
        // 拼接出查询的 URL，赋空值默认就去掉这个条件的限制，有值就代表按这个条件去查询
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize +
            '&shopId=' + shopId + '&awardName=' + awardName;

        // 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
        loading = true;

        $.getJSON(url, function (data) {
            if (data.success) {

                // 获取当前查询条件下奖品的总数
                maxItems = data.count;

                var awardListHtml = '';
                // 遍历奖品列表，拼接出卡片集合
                data.awardList.map(function (item) {
                    awardListHtml += '<div class="weui-cells" ' +
                        'data-award-id="' + item.awardId + '" data-point="' + item.point + '">' +
                        '<div class="weui-cell ">' +
                        '<div class="weui-cell__bd">' + item.awardName + '</div>' +
                        '<div class="weui-cell__hd">需要积分' + item.point + '</div>' +
                        '</div>' +
                        '<div class="weui-cell">' +
                        '<div class="weui-cell_primary">' +
                        '<img width="44" src="' + common.getContextPath() + item.awardImg + '">' +
                        '</div>' +
                        '<div class="weui-cell__bd my-cell_bd">' + item.awardDesc + '</div>' +
                        '</div>' +
                        '<div class="weui-cell">' +
                        '<div class="weui-cell__bd">' + new Date(item.lastEditTime).Format('yyyy-MM-dd') + ' 更新</div>';


                    if (item.point !== undefined) {
                        // 如果用户有积分，则显示领取按钮
                        canProceed = true;
                        awardListHtml += '<div class="weui-cell__ft">' +
                            '<a href="javascript:;" class="weui-cell_link">点击领取</a>' +
                            '</div></div></div>';
                    } else {
                        awardListHtml += '</div></div></div>';
                    }
                });

                // 将award集合添加到目标HTML组件中
                $searchResult.append(awardListHtml);
                if (data.totalPoint !== undefined) {
                    // 如果用户在该店铺有积分，则显示
                    $('.weui-media-box').text('当前积分：' + data.totalPoint);
                    totalPoint = data.totalPoint;
                }


                // 获取目前为止已经显示的 award 总数，包含之间加载的
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

    //
    $searchResult.on('click', '.weui-cells', function (e) {

        var target = e.currentTarget;
        if (canProceed && totalPoint > target.dataset.point) {
            weui.confirm('需要消耗' + target.dataset.point + '积分，确定吗?', {
                buttons: [{
                    label: '取消',
                    type: 'default'
                }, {
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                        $.ajax({
                            url: exchangeUrl,
                            type: 'POST',
                            data: {
                                awardId: target.dataset.awardId
                            },
                            dataType: 'json',
                            success: function (data) {
                                if (data.success) {
                                    weui.toast('操作成功!');
                                    totalPoint = totalPoint - target.dataset.point;
                                    $('.weui-media-box').text('当前积分：' + totalPoint);
                                } else {
                                    weui.alert('操作失败，' + data.errMsg);
                                }
                            }
                        });
                    }
                }]
            });
        } else {
            weui.topTips('积分不足或无权操作!');
        }
    });


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
        awardName = e.target.value;
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
