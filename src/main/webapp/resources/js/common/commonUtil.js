/**
 * Created by cheng on 2018/3/31.
 */

/**
 * 验证码生成
 */
function changeVerifyCode(img) {
    img.src = '/Kaptcha?' + Math.floor(Math.random() * 100);
}


/**
 * 获取查询 字符串
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}