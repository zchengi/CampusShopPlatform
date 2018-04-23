/**
 * Created by cheng on 2018/4/17.
 */

(function () {

    'use strict';

    var
        // 绑定帐号的 url
        bindUrl = '/o2o/local/bindlocalauth',
        // 从地址拉的 url 里获取 userType
        // userType = 1 为前端展示系统，其余为店家管理系统
        userType = common.getQueryString('userType');

    $('#submit').click(function () {

        var
            // 获取输入的帐号
            username = $('#username').val(),
            // 获取输入的密码
            password = $('#password').val(),
            // 获取输入的验证码
            verifyCodeActual = $('#j_captcha').val();

        if (!verifyCodeActual) {
            weui.topTips('请输入验证码!');
            return;
        }

        // 访问后台，绑定帐号
        $.ajax({
            url: bindUrl,
            async: false,
            cache: false,
            type: 'POST',
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCodeActual
            },
            success: function (data) {
                if (data.success) {
                    weui.toast('绑定成功!');
                    if (userType == 1) {
                        // 若用户在前端展示系统页面则自动退回到前端展示系统首页
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        // 若用户是在店家管理系统页面则自动回退到店铺列表页中
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    weui.alert('绑定失败,' + data.errMsg);
                    $('#img-verification-code').click();
                }
            }
        });

    });
})();