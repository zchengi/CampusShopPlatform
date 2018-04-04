/**
 * Created by cheng on 2018/4/4.
 */

(function () {

    'use strict';

    var listUrl = '/shopadmin/getproductcategorylist',
        addUrl = '/shopadmin/addproductcategories',
        deleteUrl = '/shopadmin/removeproductcategory';

    // 初始化商品分类管理列表
    getList();

    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                handleList(data.data);
            }else {
                // TODO 错误处理
            }
        });
    }

    function handleList(data) {
        var categoryListHtml = '';
        data.map(function (item) {
            categoryListHtml += '<div class="weui-cell">'
                + '<div class="weui-cell__bd">' +
                '<label class="weui-textarea">' + item.productCategoryName + '</label>'
                + '</div>'
                + '<div class="weui-cell__hd">'
                + '<label class="weui-label">' + item.priority + '</label>'
                + '</div>'
                + '<div class="weui-cell__ft">'
                + '<a href="javascript:;" class="weui-label weui-btn weui-btn_mini weui-btn_warn my-btn_warn">删除</a>'
                + '</div>'
                + '</div>';
        });

        $('#category-list').append(categoryListHtml);
    }
})();


