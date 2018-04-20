/**
 * Created by cheng on 2018/4/20.
 */

(function () {

    'use strict';
    var
        shopAuthId = common.getQueryString('shopAuthId'),
        // 根据主键获取授权信息的 url
        infoUrl = '/o2o/shopadmin/getshopauthmapbyid?shopAuthId=',
        // 修改授权信息的 url
        shopAuthPostUrl = '/o2o/shopadmin/modifyshopauthmap',
        shopauthmanagementUrl = '/o2o/shopadmin/shopauthmanagement';

    if (shopAuthId) {
        getInfo(shopAuthId);
    } else {
        weui.topTips('用户不存在!');
        window.location.href = '/o2o/shopadmin/shopmanagement';
    }

    /**
     * 获取雇员初始信息
     */
    function getInfo(id) {
        $.getJSON(infoUrl + id, function (data) {
            if (data.success) {
                var shopAuthMap = data.shopAuthMap;
                $('#shop-auth-name').val(shopAuthMap.employee.name);
                $('#shop-auth-title').val(shopAuthMap.title);
            }
        });
    }

    $('#submit').click(function () {

        var shopAuth = {},
            verifyCodeActual = $('#j_captcha').val();

        if (!verifyCodeActual) {
            weui.topTips('请输入验证码!');
            return;
        }

        // 获取要修改的信息并传入后台处理
        shopAuth.employee = {};
        shopAuth.employee.name = $('#shop-auth-name').val();
        shopAuth.title = $('#shop-auth-title').val();
        shopAuth.shopAuthId = shopAuthId;

        $.ajax({
            url: shopAuthPostUrl,
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            data: {
                shopAuthMapStr: JSON.stringify(shopAuth),
                verifyCodeActual: verifyCodeActual
            },
            success: function (data) {
                if (data.success) {
                    weui.toast('提交成功!');
                    window.location.href = shopauthmanagementUrl;
                } else {
                    weui.alert('提交失败,' + data.errMsg);
                    $('#img-verification-code').click();
                }
            }
        });
    });
})();