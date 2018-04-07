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
        , editShopUrl = '/shopadmin/modifyshop'
        , shoplistUrl = '/shopadmin/shoplist'
        , $pickerShopCategory = $('#pickerShopCategory')
        , $pickerArea = $('#pickerArea')
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
            areaId: $pickerArea.pickerId
        };
        shop.shopCategory = {
            shopCategoryId: $pickerShopCategory.pickerId
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
                    common.changeVerifyCode($('#img-verification-code')[0]);
                }
                $('#j_captcha').click();
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
                $pickerArea.html(areaList[0].label);
                $pickerArea.pickerId = areaList[0].value;
                $pickerShopCategory.html(shopCategoryList[0].label);
                $pickerShopCategory.pickerId = shopCategoryList[0].value;

                // 区域类别选择器
                $pickerArea.on('click', function () {
                    initPicker(areaList, $pickerArea);
                });
                // 店铺分类选择器
                $pickerShopCategory.on('click', function () {
                    initPicker(shopCategoryList, $pickerShopCategory);
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
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);

                $pickerShopCategory.html(shop.shopCategory.shopCategoryName);
                $pickerShopCategory.pickerId = shop.shopCategory.shopCategoryId;
                $pickerArea.html(shop.area.areaName);
                $pickerArea.pickerId = shop.area.areaId;

                var areaList = [];
                data.areaList.map(function (item, index) {
                    areaList[index] = {
                        label: item.areaName,
                        value: item.areaId
                    };
                });
                // 区域类别选择器
                $pickerArea.on('click', function () {
                    initPicker(areaList, $pickerArea);
                });
            }
        });
    }

    /**
     * 初始化 picker
     */
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