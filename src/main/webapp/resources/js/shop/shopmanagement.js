/**
 * Created by cheng on 2018/4/3.
 */

(function () {

    'use strict';

    var shopId = common.getQueryString('shopId'),
        shopInfoUrl = '/shop/getshopmanagementinfo?shopId=' + shopId;

    $.getJSON(shopInfoUrl, function (data) {
        if (data.redirect) {
            window.location.href = data.url;
        } else {
            if (data.shopId !== undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href', '/shopadmin/shopedit?shopId=' + shopId);
            $('#productCategory').attr('href', '/shopadmin/productcategorymanagement');
        }
    });
})();
