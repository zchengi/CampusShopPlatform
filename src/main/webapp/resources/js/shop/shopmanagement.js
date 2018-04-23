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
            $('#productCategoryManagement').attr('href', '/o2o/shopadmin/productcategorymanagement');
            $('#productInfo').attr('href', '/o2o/shopadmin/productmanagement');
            $('#shopAuthManagement').attr('href', '/o2o/shopadmin/shopauthmanagement');
            $('#awardManagement').attr('href', '/o2o/shopadmin/awardmanagement');
            $('#productBuyCheck').attr('href', '/o2o/shopadmin/productbuycheck');
            $('#userShopCheck').attr('href', '/o2o/shopadmin/usershopcheck');
            $('#awardDeliverCheck').attr('href', '/o2o/shopadmin/awarddelivercheck');
        }
    });
})();
