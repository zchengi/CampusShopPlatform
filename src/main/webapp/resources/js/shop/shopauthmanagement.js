/**
 * Created by cheng on 2018/4/20.
 */

(function () {

    'use strict';
    var
        // 列出该店铺下所有授权信息的 url
        listUrl = '/o2o/shopadmin/listshopauthmapsbyshop?pageIndex=1&pageSize=999',
        // 修改授权信息的 url
        modifyUrl = '/o2o/shopadmin/modifyshopauthmap',
        $shopAuthList = $('#shop-auth-list');

    getList();

    /**
     * 获取该店铺下所有授权信息
     */
    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                handleList(data.shopAuthMapList);
            } else {
            }
        });
    }


    function handleList(data) {
        var shopAuthHtml = '';
        data.map(function (item) {
            var textOp = '启用';
            var contraryStatus = 0;
            if (item.enableStatus === 1) {
                // 如果状态值为1，表明授权是生效的，操作变为禁用（即点击禁用按钮禁止授权使用）
                textOp = '禁用';
                contraryStatus = 0;
            } else {
                contraryStatus = 1;
            }

            shopAuthHtml += '<div class="weui-cell">'
                + '<div class="weui-cell__bd">'
                + '<label class="weui-textarea">' + item.employee.name + '</label>'
                + '</div>';

            if (item.titleFlag !== 0) {
                // 如果不是店家本人的授权信息，则加入编辑以及改变状态等操作
                shopAuthHtml += '<div class="weui-cell__hd">'
                    + '<label class="weui-label">' + item.title + '</label>'
                    + '</div>'
                    + '<div class="weui-cell__ft my-cell__ft">'
                    + '<a href="javascript:;" '
                    + 'data-employee-id="' + item.employee.userId + '" data-auth-id="' + item.shopAuthId
                    + '" class="weui-cell_link my-link edit">编辑</a> '
                    + '<a href="javascript:;" '
                    + 'data-auth-id="' + item.shopAuthId + '" data-status="' + contraryStatus
                    + '" class="weui-cell_link my-link status">' + textOp + '</a>'
                    + '</div>'
                    + '</div>';
            } else {
                // 如果是店家，则不允许操作
                shopAuthHtml += '<div class="weui-cell__hd">'
                    + '<label class="weui-label">' + item.title + '</label>'
                    + '</div>'
                    + '<div class="weui-cell__ft"><label class="weui-label my-label_center">不可操作</label></div>'
                    + '</div>';
            }
        });

        $shopAuthList.html(shopAuthHtml);
    }

    $shopAuthList.on('click', 'a', function (e) {
        var target = e.currentTarget;
        if ($(target).hasClass('edit')) {
            // 授权编辑页跳转
            window.location.href = '/o2o/shopadmin/shopauthoperation?shopAuthId=' + target.dataset.authId;
        } else if ($(target).hasClass('status')) {
            // 更新授权信息的状态
            changeStatus(target.dataset.authId, target.dataset.status);
        }
    });


    function changeStatus(id, status) {
        var shopAuth = {};
        shopAuth.shopAuthId = id;
        shopAuth.enableStatus = status;
        weui.confirm('确定修改吗?', function () {
            $.ajax({
                url: modifyUrl,
                type: 'POST',
                data: {
                    shopAuthMapStr: JSON.stringify(shopAuth),
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
        });
    }
})();