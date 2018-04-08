/**
 * Created by cheng on 2018/4/7.
 */


(function () {

    var
        // 从 URL 里获取 productId 参数的值
        productId = common.getQueryString('productId')
        // 通过 productId 获取商品信息的 URL
        , infoUrl = '/shopadmin/getproductbyid?productId=' + productId
        // 获取当前店铺设定的商品类别列表的 URL
        , categoryUrl = '/shopadmin/getproductcategorylist'
        // 更新商品信息的 URL
        , productPostUrl = '/shopadmin/addproduct'
        // 店铺管理页面
        , productManagementUrl = '/shopadmin/productmanagement'
        // 由于商品添加和编辑使用的是同一个页面，该表示符用来表明本次是添加操作还是编辑操作
        , isEdit = false
        , $category_list = $('#category-list')
        , $picker_product_category = $('#picker-product-category')
        // 图片最大上传数
        , maxCount = 6
        // 图片文件
        , uploadList;


    // 初始化商品操作页
    if (productId) {
        // 如果 productId 存在则为编辑操作
        getInfo(productId);
        isEdit = true;
        productPostUrl = '/shopadmin/modifyproduct';
    } else {
        getCategory();
    }

    // 初始化图片相关控件(返回图片列表)
    uploadList = common.imgShow(maxCount);


    // 提交按钮的事件响应，分别对商品添加和编辑操作作不同的响应
    $('#submit').on('click', function () {
        // 创建商品 json 对象，分别从表单里获取对应的属性值
        var product = {};
        product.productName = $('#product-name').val();
        product.productDesc = $('#product-desc').val();
        product.priority = $('#priority').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        product.productId = productId;
        // 获取选定的商品类别值
        product.productCategory = {
            productCategoryId: $picker_product_category.pickerId
        };


        // 获取缩略图文件流
        var thumbnail = uploadList[0];

        // 获取验证码
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            weui.alert('请输入验证码!');
            return;
        }

        // 生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();

        // 将 product json 对象转换成字符流保存至表单对象 key 为 productStr的键值对里
        formData.append('productStr', JSON.stringify(product));
        formData.append('verifyCodeActual', verifyCodeActual);
        formData.append('thumbnail', thumbnail);

        // 遍历商品详情图，获取其中的文件流
        for (var i = 1; i < uploadList.length; i++) {
            // 将第i个文件流赋值给 key 为 productImg[i] 的表单键值对里
            formData.append('productImg' + (i - 1), uploadList[i]);
        }

        // 将数据提交至后台处理相关操作
        $.ajax({
            url: productPostUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    weui.toast('提交成功!');
                    // window.location.href = shopmanagementUrl;
                } else {
                    weui.alert('提交失败,' + data.errMsg);
                }
                $('#img-verification-code').click();
            }
        });
    });

    // 获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                // 从返回的 JSON 中获取 product 对象的信息，并赋值给表单
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);

                // 获取原本的商品类别以及该店铺的所有商品类别列表
                var optionHtml = ''
                    , optionArr = data.productCategoryList
                    , optionSelected = product.productCategory.product.productCategoryId;

                // 生成前端的 HTML 商品类别列表，并默认选择编辑前的商品类别
                $category_list.html(optionHtml);
            } else {

            }
        });
    }

    // 为商品添加操作提供该店铺下的所有商品类别列表
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var productCategoryList = [];
                data.data.map(function (item, index) {
                    productCategoryList[index] = {
                        label: item.productCategoryName,
                        value: item.productCategoryId
                    }
                });

                // 目录选择器
                // 显示初始值(默认为第1个店铺)
                $picker_product_category.html(productCategoryList[0].label);
                $picker_product_category.pickerId = productCategoryList[0].value;
                $picker_product_category.on('click', function () {
                    common.initPicker(productCategoryList, $picker_product_category);
                });
            } else {

            }
        });
    }
})();
