/**
 * Created by cheng on 2018/4/23.
 */

(function () {

    'use strict';
    var
        // 获取奖品映射信息的 URL
        awardUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId=',
        userAwardId = common.getQueryString('userAwardId');

    $.getJSON(awardUrl + userAwardId, function (data) {
        if (data.success) {
            var award = data.award;

            $('#award-cover-pic').attr('src', common.getContextPath() + award.awardImg);
            $('#award-create-time').html(new Date(award.createTime).Format('yyyy-MM-dd'));
            $('#award-name').html(award.awardName);
            $('#award-desc').html(award.awardDesc);

            // 如果没有兑换商品，生成兑换礼品的二维码供商家扫描
            var imgListHtml = '';
            if (data.usedStatus == 0) {
                imgListHtml += '<div class="weui-cell"><img class="my-img"' +
                    ' src="/o2o/frontend/generateqrcode4award?userAwardId=' + userAwardId + '"></div>';
            }

            $(".weui-cells").append(imgListHtml);
        } else {
            weui.alert('查询失败，' + data.errMsg);
        }
    });
})();

