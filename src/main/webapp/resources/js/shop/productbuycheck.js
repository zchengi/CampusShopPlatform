/**
 * Created by cheng on 2018/4/22.
 */

(function () {

    'use strict';

    var productName = '',
        // 获取用户购买信息的 url
        listUrl = '/o2o/shopadmin/listuserproductmapsbyshop?pageIndex=1&pageSize=999&productName=',
        // 获取该店铺商品7天销量的 url
        listProductSellDailyUrl = '/o2o/shopadmin/listproductselldailyinfobyshop',
        $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel'),
        $checkList = $('#check-list');

    getList();
    getProductSellDailyList();

    function getList() {

        // 访问后台获取该店铺的购买信息列表
        $.getJSON(listUrl+productName, function (data) {
            if (data.success) {
                var userProductMapList = data.userProductMapList,
                    userProductHtml = '';

                userProductMapList.map(function (item) {
                    userProductHtml += '<div class="weui-cell">' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.product.productName + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + new Date(item.createTime).Format('yyyy-MM-dd') + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__bd">' +
                        '<label class="weui-label">' + item.user.name + '</label>' +
                        '</div>' +
                        '<div class="weui-cell__hd">' +
                        '<label class="weui-label">' + item.point + '</label>' +
                        '</div>' +
                        '</div>';
                });

                $checkList.html(userProductHtml);
            } else {
                weui.alert('初始化失败,' + data.errMsg);
            }
        });
    }

    /**
     * 获取商品7天的销量
     */
    function getProductSellDailyList() {

        $.getJSON(listProductSellDailyUrl, function (data) {
            if (data.success) {
                var myChart = echarts.init(document.getElementById('chart'));

                // 生成静态的 Echart 信息的部分
                var option = generateStaticEchartPart();
                // 遍历销量统计列表，动态设定 echarts 的值
                option.legend.data = data.legendData;
                option.xAxis = data.xAxis;
                option.series = data.series;

                // 生成数据
                myChart.setOption(option);
            }
        });
    }

    /**
     * 生成静态的 Echart 信息的部分
     */
    function generateStaticEchartPart() {

        /**
         * echarts 逻辑部分
         */
        var option = {
            // 提示框，鼠标悬浮交互时的信息提示
            tooltip: {
                // 坐标轴指示器，坐标轴触发有效
                trigger: 'axis',
                axisPointer: {
                    // 默认为直线，可以选择 'line' 或 'shadow'
                    type: 'shadow'
                }
            },
            // 图例，每个图标最多仅有一个图例
            legend: {},
            // 直角坐标系内绘制网格
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            // 直角坐标系中横轴数组，数组种每一项代表一条横轴坐标轴
            xAxis: [{}],
            // 直角坐标系中纵轴数组，数组种每一项代表一条纵轴坐标轴
            yAxis: [{
                type: 'value'
            }],
            // 驱动图表生成的数据内容数组，数组中每一项为一个系列的选项及数据
            series: [{}]
        };

        return option;
    }


    /**
     * 搜索逻辑部分
     */

    // 点击搜索框
    $searchText.on('click', function () {
        $searchBar.addClass('weui-search-bar_focusing');
        $searchInput.focus();
    });

    // 失去焦点
    $searchInput.on('blur', function () {
        if (!this.value.length) cancelSearch();
    });

    // 获取焦点
    $searchInput.on('input', function (e) {
        productName = e.target.value;
        $searchResult.empty();

        // 当在搜索框内里输入信息的时候
        // 依据输入的商品名模糊查询该商品的购买记录
        // 清空商品购买记录列表
        $checkList.empty();
        // 再次加载
        // getList();
    });

    // 取消搜索
    $searchClear.on('click', function () {
        $searchInput.val('');
        $searchInput.focus();
    });

    // searchCancel
    $searchCancel.on('click', function () {
        cancelSearch();
        $searchInput.blur();
    });

    // 取消搜索
    function cancelSearch() {
        $searchBar.removeClass('weui-search-bar_focusing');
        $searchText.show();
    }
})();