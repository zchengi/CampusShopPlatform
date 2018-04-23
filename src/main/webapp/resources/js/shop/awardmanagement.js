/**
 * Created by cheng on 2018/4/23.
 */

(function () {

    'use strict';

    var
        // 获取此店铺下的奖品列表的 URL
        listUrl = "/o2o/shopadmin/listawardsbyshop?pageIndex=1&pageSize=999"
        //奖品下架的 URL
        , statusUrl = '/o2o/shopadmin/modifyaward'
        , $award_list = $('#award-list');

    getList();

    /**
     * 获取此店铺下的奖品列表
     */
    function getList() {
        // 从后台获取此店铺的奖品列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                handleList(data.awardList);
            } else {

            }
        });
    }

    function handleList(data) {
        var awardListHtml = '';
        // 遍历每条奖品信息，拼接成一行显示，列信息包括：
        // 奖品名称，积分，上架\下架(含awardId)，编辑按钮(含awardId)
        // 预览(含awardId)
        data.map(function (item) {
            var textOp = '下架';
            var contraryStatus = 0;
            if (item.enableStatus === 0) {
                // 如果状态值为0，表明是已下架的奖品，操作变为上架（即点击上架按钮上架相关奖品）
                textOp = '上架';
                contraryStatus = 1;
            } else {
                contraryStatus = 0;
            }

            // 拼接每件奖品的行信息
            awardListHtml += '<div class="weui-cell now">'
                + '<div class="weui-cell__bd">'
                + '<label class="weui-textarea">' + item.awardName + '</label>'
                + '</div>'
                + '<div class="weui-cell__hd">'
                + '<label class="weui-label">' + item.point + '</label>'
                + '</div>'
                + '<div class="weui-cell__ft">'
                + '<a href="javascript:;" data-id="' + item.awardId + '" class="weui-cell_link my-link edit">编辑</a> '
                + '<a href="javascript:;" '
                + 'data-id="' + item.awardId + '" data-status="' + contraryStatus
                + '" class="weui-cell_link my-link status">' + textOp + '</a> '
                + '<a href="javascript:;" '
                + 'data-id="' + item.awardId + '" data-status="' + item.enableStatus
                + '" class="weui-cell_link my-link preview">预览</a>'
                + '</div>'
                + '</div>';
        });

        // 将拼接好的信息赋值给html控件中
        $award_list.html(awardListHtml);
    }

    $award_list.on('click', 'a', function (e) {
        var target = e.currentTarget;
        if ($(target).hasClass('edit')) {
            // 如果有 class edit 则点击就进入店铺信息编辑页面，并带有 awardId 参数
            window.location.href = '/o2o/shopadmin/awardoperation?awardId=' + target.dataset.id;
        } else if ($(target).hasClass('status')) {
            // 如果有 class status 则调用后台功能上/下架相关奖品，并带有 awardId 参数
            changeItemStatus(target.dataset.id, target.dataset.status);
        } else if ($(target).hasClass('preview')) {
            // 如果有 class preview 则去前台展示系统该奖品详情页预览奖品情况
            window.location.href = '/o2o/frontend/awarddetail?awardId=' + target.dataset.id
        }
    });

    function changeItemStatus(id, enableStatus) {
        // 定义 award json 对象并添加 awardId 以及状态(上架/下架)
        var award = {};
        award.awardId = id;
        award.enableStatus = enableStatus;
        weui.confirm('确定' + '吗?', {
            buttons: [{
                label: '取消',
                type: 'default'
            }, {
                label: '确定',
                type: 'primary',
                onClick: function () {
                    $.ajax({
                        url: statusUrl,
                        type: 'POST',
                        data: {
                            awardStr: JSON.stringify(award),
                            statusChange: true
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (data.success) {
                                weui.toast('操作成功!');
                                getList();
                            } else {
                                weui.alert('操作失败，' + data.errMsg);
                            }
                        }
                    });
                }
            }]
        });
    }
})();