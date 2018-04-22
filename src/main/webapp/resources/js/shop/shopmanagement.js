/**
 * Created by cheng on 2018/4/3.
 */

(function () {

    'use strict';

    var shopId = common.getQueryString('shopId'),
        shopInfoUrl = '/o2o/shop/getshopmanagementinfo?shopId=' + shopId;

    $.getJSON(shopInfoUrl, function (data) {
        if (data.redirect) {
            window.location.href = data.url;
        } else {
            if (data.shopId !== undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href', '/o2o/shopadmin/shopoperation?shopId=' + shopId);
            $('#productInfo').attr('href', '/o2o/shopadmin/productmanagement');
            $('#productCategoryManagement').attr('href', '/o2o/shopadmin/productcategorymanagement');
            $('#shopAuthManagement').attr('href', '/o2o/shopadmin/shopauthmanagement');
            $('#productBuyCheck').attr('href', '/o2o/shopadmin/productbuycheck');
        }
    });
})();
