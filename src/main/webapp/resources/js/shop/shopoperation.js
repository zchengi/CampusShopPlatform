/**
 * Created by cheng on 2018/3/31.
 */

;(function () {

    'use strict';

    /* var initUrl = '/o2o/shop/getshopinitinfo'
         , registerShopUrl = '/o2o/shop/registershop'*/

    var initUrl = '/shop/getshopinitinfo'
        , registerShopUrl = '/shop/registershop'
        , shopId = common.getQueryString('shopId')
        // var isEdit = shopId ? true : false;
        , isEdit = !!shopId
        , shopInfoUrl = '/shop/getshopbyid?shopId=' + shopId
        , editShopUrl = '/shop/modifyshop'
        , shoplistUrl = '/shopadmin/shoplist'
        , $picker_shop_category = $('#picker-shop-category')
        , $picker_area = $('#picker-area')
        // 图片最大上传数
        , maxCount = 1
        // 图片文件
        , uploadList;


    // 初始化
    // TODO 表单校验
    if (!isEdit) {
        getShopInitInfo();
    }else {
        getShopInfo(shopId);
    }

    // 初始化图片相关控件(返回图片列表)
    uploadList = common.imgShow(maxCount);

    $('#submit').on('click', function () {
        var shop = {};
        if (isEdit){
            shop.shopId = shopId;
        }

        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.area = {
            areaId: $picker_area.pickerId
        };
        shop.shopCategory = {
            shopCategoryId: $picker_shop_category.pickerId
        };

        // 因为添加新的图片相关控件，获取图片的方法变化
        // var shopImg = $('#shop-img')[0].files[0];
        var shopImg = uploadList[0];


        // TODO 改为手机短信验证
        var verifyCodeActual = $('#j_captcha').val();

        if (!verifyCodeActual) {
            weui.alert('请输入验证码!');
            return;
        }

        var formData = new FormData();
        formData.append('shopStr', JSON.stringify(shop));
        formData.append('shopImg', shopImg);
        formData.append('verifyCodeActual', verifyCodeActual);
        $.ajax({
            url: (isEdit ? editShopUrl : registerShopUrl),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    weui.toast('提交成功!');
                    window.location.href = shoplistUrl;
                } else {
                    weui.alert('提交失败,' + data.errMsg);
                }
                $('#img-verification-code').click();
            }
        });
    });

    /**
     * 获取店铺初始信息
     */
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var areaList = [];
                data.areaList.map(function (item, index) {
                    areaList[index] = {
                        label: item.areaName,
                        value: item.areaId
                    };
                });
                var shopCategoryList = [];
                data.shopCategoryList.map(function (item, index) {
                    shopCategoryList[index] = {
                        label: item.shopCategoryName,
                        value: item.shopCategoryId
                    };
                });

                // 显示初始值(默认为第1个店铺)
                $picker_area.html(areaList[0].label);
                $picker_area.pickerId = areaList[0].value;
                $picker_shop_category.html(shopCategoryList[0].label);
                $picker_shop_category.pickerId = shopCategoryList[0].value;

                // 区域类别选择器
                $picker_area.on('click', function () {
                    common.initPicker(areaList, $picker_area);
                });
                // 店铺分类选择器
                $picker_shop_category.on('click', function () {
                    common.initPicker(shopCategoryList, $picker_shop_category);
                });
            } else {
                // TODO 请求失败处理
            }
        });
    }

    /**
     * 获取商铺信息
     */
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                var shop = data.shop
                    , defaultAreaName
                    , defaultAreaId;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);

                $picker_shop_category.html(shop.shopCategory.shopCategoryName);
                $picker_shop_category.pickerId = shop.shopCategory.shopCategoryId;

                var areaList = [];
                data.areaList.map(function (item, index) {
                    if (shop.area.areaId === item.areaId) {
                        defaultAreaName = item.areaName;
                        defaultAreaId = item.areaId;
                    }
                    areaList[index] = {
                        label: item.areaName,
                        value: item.areaId
                    };
                });

                $picker_area.html(defaultAreaName);
                $picker_area.pickerId = defaultAreaId;
                // 区域类别选择器
                $picker_area.on('click', function () {
                    common.initPicker(areaList, $picker_area, defaultAreaId);
                });
            }
        });
    }
})();