/**
 * Created by cheng on 2018/4/17.
 */

(function () {

    'use strict';

    var
        // 登录验证的 url
        loginUrl = '/o2o/local/logincheck',
        // 从地址栏的 url 里获取 userType
        // userType = 1 则为 customer，否则为 shopOwner
        userType = common.getQueryString('userType'),
        // 登录次数，累计登录失败两次后自动弹出验证码要求输入
        loginCount = 0;

    $('#submit').click(function () {
        var
            // 获取输入的帐号
            username = $('#username').val(),
            // 获取输入的密码
            password = $('#password').val(),
            // 获取验证码信息
            verifyCodeActual = $('#j_captcha').val(),
            // 是否需要验证码验证，默认为 false
            needVerify = false;

        // 如果登录失败两次
        if (loginCount >= 2) {
            if (!verifyCodeActual) {
                weui.topTips('请输入验证码!');
                return;
            } else {
                needVerify = true;
            }
        }

        // 访问后台进行登录验证
        $.ajax({
            url: loginUrl,
            async: false,
            cache: false,
            type: 'POST',
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCodeActual,
                // 是否需要做验证码校验
                needVerify: needVerify
            },
            success: function (data) {
                if (data.success) {
                    weui.toast('登录成功!');
                    if (userType === 1) {
                        // 若用户在前端展示系统页面则自动链接到前端展示系统首页
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        // 若用户是在店家管理系统页面则自动链接到店铺列表页中
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    weui.alert('登录失败，' + data.errMsg);
                    loginCount++;
                    if (loginCount >= 2) {
                        $('#verifyPart').show();
                        $('#img-verification-code').click();
                    }
                }
            }
        });
    });
})();