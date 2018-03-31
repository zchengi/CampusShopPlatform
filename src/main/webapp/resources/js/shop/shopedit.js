/**
 * Created by cheng on 2018/3/31.
 */

;(function () {

    "use strict";

    /* var initUrl = '/o2o/shop/getshopinitinfo'
         , editShopUrl = '/o2o/shop/registershop'*/

    var initUrl = '/shop/getshopinitinfo'
        , editShopUrl = '/shop/registershop'
        , $pickerShopCategory = $('#pickerShopCategory')
        , $pickerArea = $('#pickerArea');

    // 初始化
    // TODO 表单校验
    getCategory();


    $('#submit').on('click', function () {
        var shop = {};
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.area = {
            areaId: $pickerArea.pickerId
        };
        shop.shopCategory = {
            shopCategoryId: $pickerShopCategory.pickerId
        };
        var shopImg = $('#shop-img')[0].files[0];
        // TODO 改为手机短信验证
        var verifyCodeActual = $('#j_captcha').val();

        if (!verifyCodeActual) {
            weui.alert("请输入验证码!");
            return;
        }

        var formData = new FormData();
        formData.append('shopStr', JSON.stringify(shop));
        formData.append('shopImg', shopImg);
        formData.append("verifyCodeActual", verifyCodeActual);
        $.ajax({
            url: editShopUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    weui.toast('提交成功!');
                } else {
                    weui.alert('提交失败,' + data.errMsg + ".");
                }
                $('#j_captcha').click();
            }
        });
    });

    /**
     * 获取商铺初始信息
     */
    function getCategory() {
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

                // 显示初始值(默认为第1个商铺)
                $pickerArea.html(areaList[0].label);
                $pickerArea.pickerId = areaList[0].value;
                $pickerShopCategory.html(shopCategoryList[0].label);
                $pickerShopCategory.pickerId = shopCategoryList[0].value;

                // 区域类别选择器
                $pickerArea.on('click', function () {
                    initPicker(areaList, $pickerArea)
                });
                // 商铺分类选择器
                $pickerShopCategory.on('click', function () {
                    initPicker(shopCategoryList, $pickerShopCategory)
                });
            } else {
                // TODO 请求失败处理
            }
        });
    }

    function initPicker(data, picker) {
        // 单列picker
        weui.picker(data, {
            container: 'body',
            defaultValue: [data[0].value],
            onConfirm: function (result) {
                picker.html(result[0].label);
                picker.pickerId = result[0].value;
            },
            id: picker[0].id
        });
    }
})();