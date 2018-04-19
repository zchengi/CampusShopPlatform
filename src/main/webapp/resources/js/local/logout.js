/**
 * Created by cheng on 2018/4/17.
 */

(function () {

    'use strict';

    $('#log-out').click(function () {

        // 清除 session
        $.ajax({
            url: '/o2o/local/logout',
            type: 'POST',
            async: false,
            cache: false,
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    var userType = $('#log-out').attr('userType');
                    // 清除成功后退出到登录界面
                    window.location.href = '/o2o/local/login?userType=' + userType;
                    return false;
                }
            },
            error: function (data, error) {
                weui.alert(error);
            }
        });
    });
})();