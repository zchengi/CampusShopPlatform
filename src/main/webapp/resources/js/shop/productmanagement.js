/**
 * Created by cheng on 2018/4/9.
 */

(function () {

    'use strict';

    var
        // 获取此店铺下的商品列表的 URL
        listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999'
        //商品下架的 URL
        , statusUrl = '/o2o/shopadmin/modifyproduct'
        , $product_list = $('#product-list');

    getList();

    /**
     * 获取此店铺下的商品列表
     */
    function getList() {
        // 从后台获取此店铺的商品列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                handleList(data.productList);
            } else {

            }
        });
    }

    function handleList(data) {
        var productListHtml = '';
        // 遍历每条商品信息，拼接成一行显示，列信息包括：
        // 商品名称，积分，上架\下架(含productId)，编辑按钮(含productId)
        // 预览(含productId)
        data.map(function (item) {
            var textOp = '下架';
            var contraryStatus = 0;
            if (item.enableStatus == 0) {
                // 如果状态值为0，表明是已下架的商品，操作变为上架（即点击上架按钮上架相关商品）
                textOp = '上架';
                contraryStatus = 1;
            } else {
                contraryStatus = 0;
            }

            // 拼接每件商品的行信息
            productListHtml += '<div class="weui-cell now">'
                + '<div class="weui-cell__bd">'
                + '<label class="weui-textarea">' + item.productName + '</label>'
                + '</div>'
                + '<div class="weui-cell__hd">'
                + '<label class="weui-label">' + item.point + '</label>'
                + '</div>'
                + '<div class="weui-cell__ft">'
                + '<a href="javascript:;" data-id="' + item.productId + '" class="weui-cell_link my-link edit">编辑</a> '
                + '<a href="javascript:;" '
                + 'data-id="' + item.productId + '" data-status="' + contraryStatus
                + '" class="weui-cell_link my-link status">' + textOp + '</a> '
                + '<a href="javascript:;" '
                + 'data-id="' + item.productId + '" data-status="' + item.enableStatus
                + '" class="weui-cell_link my-link preview">预览</a>'
                + '</div>'
                + '</div>';
        });

        // 将拼接好的信息赋值给html控件中
        $product_list.html(productListHtml);
    }

    $product_list.on('click', 'a', function (e) {
        var target = e.currentTarget;
        if ($(target).hasClass('edit')) {
            // 如果有 class edit 则点击就进入店铺信息编辑页面，并带有 productId 参数
            window.location.href = '/o2o/shopadmin/productoperation?productId=' + target.dataset.id;
        } else if ($(target).hasClass('status')) {
            // 如果有 class status 则调用后台功能上/下架相关商品，并带有 productId 参数
            changeItemStatus(target.dataset.id, target.dataset.status);
        } else if ($(target).hasClass('preview')) {
            // 如果有 class preview 则去前台展示系统该商品详情页预览商品情况
            window.location.href = '/o2o/frontend/productdetail?productId=' + target.dataset.id
        }
    });

    function changeItemStatus(id, enableStatus) {
        // 定义 product json 对象并添加 productId 以及状态(上架/下架)
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        weui.confirm('确定' + '吗?', {
            buttons: [{
                label: '取消',
                type: 'default'
            }, {
                label: '确定',
                type: 'primary',
                onClick: function () {
                    $.ajax({
                        url: statusUrl,
                        type: 'POST',
                        data: {
                            productStr: JSON.stringify(product),
                            statusChange: true
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (data.success) {
                                weui.toast('操作成功!');
                                getList();
                            } else {
                                weui.alert('操作失败，' + data.errMsg);
                            }
                        }
                    });
                }
            }]
        });
    }
})();