/**
 * Created by cheng on 2018/4/19.
 */

(function () {

    'use strict';

    var
        // 从地址栏的 url 里获取 productId
        productId = common.getQueryString('productId'),
        // 获取商品信息的 url
        productIdUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId,
        // 原价
        $normalPrice = $("#normal-price"),
        $promotionPrice = $("#promotion-price");

    // 获取并显示商品信息
    $.getJSON(productIdUrl, function (data) {
        if (data.success) {
            // 获取商品信息
            var product = data.product;

            // 给商品信息相关的HTML空间赋值

            // 商品缩略图
            $('#product-img').attr('src', product.imgAddr);
            // 商品更新时间
            $('#product-time').text(new Date(product.lastEditTime).Format('yyyy-MM-dd'));
            // 积分信息提示
            if (product.point !== undefined) {
                $('#product-point').text('购买可得' + product.point + '积分');
            }
            // 商品名称
            $('#product-name').text(product.productName);
            // 商品简介
            $('#product-desc').text(product.productDesc);
            // 商品价格展示逻辑，主要判断原价现价是否为空，所以都为空则不显示价格控件
            if (product.normalPrice !== undefined && product.promotionPrice !== undefined) {
                // 如果现价和原价都不为空，则都展示，并给原价加个删除符号
                $normalPrice.html('<del>￥' + product.normalPrice + '</del>');
                $promotionPrice.html(product.normalPrice);
            } else if (product.normalPrice !== undefined && product.promotionPrice === undefined) {
                // 如果原价不为空，而现价为空，则只展示原价
                $normalPrice.html(product.normalPrice);
            } else if (product.normalPrice === undefined && product.promotionPrice !== undefined) {
                // 如果原价为空，现价不为空，则只展示现价
                $promotionPrice.html(product.promotionPrice);
            }

            var imgListHtml = '';
            // 遍历商品详情图列表，并生成批量img标签
            product.productImgList.map(function (item, index) {
                    imgListHtml += '<div class="weui-cell"><img class="my-img" src="' + item.imgAddr + '"></div>';
            });

            $(".weui-cells").append(imgListHtml);
        }
    });

})();