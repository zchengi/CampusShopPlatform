/**
 * Created by cheng on 2018/4/17.
 */

(function () {

    'use strict';

    var
        // 修改平台密码的 url
        changepwdUrl = '/o2o/local/changelocalpwd',
        // 从地址栏的 url 里获取 userType
        // userType = 1 则为 customer，否则为 shopOwner
        userType = common.getQueryString('userType');

    $('#submit').click(function () {
        var
            // 获取帐号
            username = $('#username').val(),
            // 获取原密码
            password = $('#password').val(),
            // 获取新密码
            newPassword = $('#newPassword').val(),
            // 获取确认的密码
            confirmPassword = $('#confirmPassword').val(),
            // 获取验证码信息
            verifyCodeActual = $('#j_captcha').val();

        if (newPassword !== confirmPassword) {
            weui.topTips("两次输入的新密码不一致!");
            return;
        }

        if (!verifyCodeActual) {
            weui.topTips("请输入验证码!");
            return;
        }

        // 添加表单数据
        var formData = new FormData();
        formData.append('username', username);
        formData.append('password', password);
        formData.append('newPassword', newPassword);
        formData.append('verifyCodeActual', verifyCodeActual);

        // 访问后台，修改密码
        $.ajax({
            url: changepwdUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    weui.toast('修改密码成功!');
                    if (userType === 1) {
                        // 若用户在前端展示系统页面则自动链接到前端展示系统首页
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        // 若用户是在店家管理系统页面则自动链接到店铺列表页中
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    weui.alert('修改密码失败，' + data.errMsg);
                    $('#category_img').click();
                }
            }
        });
    });
})();