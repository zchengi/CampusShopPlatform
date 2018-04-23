/**
 * Created by cheng on 2018/4/.
 */


(function () {

    'use strict';

    var
        // 从 URL 里获取 awardId 参数的值
        awardId = common.getQueryString('awardId')
        // 通过 awardId 获取商品信息的 URL
        , infoUrl = '/o2o/shopadmin/getawardbyid?awardId=' + awardId
        // 更新商品信息的 URL
        , awardPostUrl = '/o2o/shopadmin/addaward'
        // 店铺管理页面
        , awardManagementUrl = '/o2o/shopadmin/awardmanagement'
        // 由于商品添加和编辑使用的是同一个页面，该表示符用来表明本次是添加操作还是编辑操作
        , isEdit = !!awardId
        // 图片最大上传数
        , maxCount = 1
        // 图片文件
        , uploadList;


    // 初始化商品操作页
    if (isEdit) {
        // 如果 awardId 存在则为编辑操作
        getInfo(awardId);
        awardPostUrl = '/o2o/shopadmin/modifyaward';
    }

    // 初始化图片相关控件(返回图片列表)
    uploadList = common.imgShow(maxCount);


    // 提交按钮的事件响应，分别对商品添加和编辑操作作不同的响应
    $('#submit').on('click', function () {
        // 创建商品 json 对象，分别从表单里获取对应的属性值
        var award = {};
        award.awardName = $('#award-name').val();
        award.priority = $('#priority').val();
        award.awardDesc = $('#award-desc').val();
        award.point = $('#point').val();
        award.awardId = awardId ? awardId : '';

        // 获取缩略图文件流
        var thumbnail = uploadList[0];

        // 获取验证码
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            weui.topTips('请输入验证码!');
            return;
        }

        // 生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();

        // 将 award json 对象转换成字符流保存至表单对象 key 为 awardStr的键值对里
        formData.append('awardStr', JSON.stringify(award));
        formData.append('verifyCodeActual', verifyCodeActual);
        formData.append('thumbnail', thumbnail);

        // 将数据提交至后台处理相关操作
        $.ajax({
            url: awardPostUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    weui.toast('提交成功!');
                    window.location.href = awardManagementUrl;
                } else {
                    weui.alert('提交失败,' + data.errMsg);
                    $('#img-verification-code').click();
                }
            }
        });
    });

    // 获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(awardId) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                // 从返回的 JSON 中获取 award 对象的信息，并赋值给表单
                var award = data.award;
                $('#award-name').val(award.awardName);
                $('#priority').val(award.priority);
                $('#award-desc').val(award.awardDesc);
                $('#point').val(award.point);
            } else {
            }
        });
    }


})();
