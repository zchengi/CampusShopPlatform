/**
 * Created by cheng on 2018/4/4.
 */

(function () {

    'use strict';

    var listUrl = '/o2o/shopadmin/getproductcategorylist',
        addUrl = '/o2o/shopadmin/addproductcategories',
        deleteUrl = '/o2o/shopadmin/removeproductcategory',
        $category_list = $('#category-list');

    // 初始化商品分类管理列表
    getList();

    // 批量添加商品类别
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

    // 提交添加的商品类别
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

    // 当前商品的删除事件
    $category_list.on('click', '.weui-cell.now .my-btn_warn', function (e) {
        var target = e.currentTarget,
            $this = $(this);

        weui.confirm('确定删除吗?', {
            buttons: [{
                label: '取消',
                type: 'default'
            }, {
                label: '确定',
                type: 'primary',
                onClick: function () {
                    $.ajax({
                        url: deleteUrl,
                        type: 'POST',
                        data: {
                            productCategoryId: target.dataset.id
                        },
                        success: function (data) {
                            if (data.success) {
                                weui.toast('删除成功!');
                                $this.parent().parent().remove();
                            } else {
                                weui.alert('删除失败，' + data.errMsg);
                            }
                        }
                    });
                }
            }]
        });
    });

    // 新增未提交的删除事件
    $category_list.on('click', '.weui-cell.temp .my-btn_warn', function () {
        //console.log($(this).parent().parent());
        $(this).parent().parent().remove();
    });

    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                handleList(data.data);
            } else {
                // TODO 错误处理
            }
        });
    }

    function handleList(data) {
        var categoryListHtml = '';
        data.map(function (item) {
            categoryListHtml += '<div class="weui-cell now">'
                + '<div class="weui-cell__bd">'
                + '<label class="weui-textarea">' + item.productCategoryName + '</label>'
                + '</div>'
                + '<div class="weui-cell__hd">'
                + '<label class="weui-label">' + item.priority + '</label>'
                + '</div>'
                + '<div class="weui-cell__ft">'
                + '<a href="javascript:;" data-id="'
                + item.productCategoryId
                + '" class="weui-label weui-btn weui-btn_mini weui-btn_warn my-btn_warn">删除</a>'
                + '</div>'
                + '</div>';
        });
        $category_list.html(categoryListHtml);
    }
})();


