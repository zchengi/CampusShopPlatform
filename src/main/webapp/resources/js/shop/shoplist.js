/**
 * Created by cheng on 2018/4/3.
 */

(function () {

    'use strict';

    var shoplistUrl = '/o2o/shop/getshoplist';

    // 初始化店铺信息
    getShopList();

    function getShopList() {
        $.ajax({
            url: shoplistUrl,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    handleList(data.shopList);
                    handleUser(data.user);
                } else {
                    // TODO 错误处理
                }
            }
        });
    }

    function handleList(data) {
        var shopListHtml = '';
        data.map(function (item) {
            shopListHtml += goShop(item.enableStatus, item.shopId)
                + '<div class="weui-cell__bd"><p>' + item.shopName + '</p></div>'
                + '<div class="weui-cell__ft">' + shopStatus(item.enableStatus) + '</div>'
                + '</a>';
        });

        $('#shop-list').html(shopListHtml);
    }

    function handleUser(data) {
        $('#user-name').html('你好,' + data.name);
    }

    function goShop(status, id) {
        if (status == 1) {
            return '<a class="weui-cell weui-cell_access" href="/o2o/shopadmin/shopmanagement?shopId=' + id + '">';
        } else {
            return '<a class="weui-cell">';
        }
    }

    function shopStatus(status) {
        if (status == 0) {
            return '审核中';
        } else if (status == -1) {
            return '店铺非法';
        } else {
            return '审核通过';
        }
    }
})();