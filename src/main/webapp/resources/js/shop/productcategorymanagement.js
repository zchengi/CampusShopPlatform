/**
 * Created by cheng on 2018/4/4.
 */

(function () {

    'use strict';

    var listUrl = '/shopadmin/getproductcategorylist',
        addUrl = '/shopadmin/addproductcategories',
        deleteUrl = '/shopadmin/removeproductcategory',
        $category_list = $('#category-list');

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

    $('#new').on('click', function () {
        var categoryHtml = '<div class="weui-cell temp">'
            + '<div class="weui-cell__bd">'
            + '<input class="weui-textarea weui-input category" type="text" placeholder="分类名"></div>'
            + '<div class="weui-cell__hd">'
            // TODO 控制 number 为4位 添加 js 验证
            + '<input class="weui-label weui-input priority" type="number" placeholder="优先级"></div>'
            + '<div class="weui-cell__ft">'
            + '<a href="javascript:;" class="weui-label weui-btn weui-btn_mini weui-btn_warn my-btn_warn">删除</a>'
            + '</div>'
            + '</div>';

        $category_list.append(categoryHtml);
    });

    $('#submit').on('click', function () {
        var tempArr = $('.temp'),
            productCategoryList = [];

        tempArr.map(function (index, item) {
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj);
            }
        });

        $.ajax({
            url: addUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            success: function (data) {
                if (data.success) {
                    weui.toast('提交成功!');
                    getList();
                } else {
                    weui.alert('提交失败，' + data.errMsg);
                }
            }
        });
    });

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

        $category_list.html(categoryListHtml);
    }
})();


