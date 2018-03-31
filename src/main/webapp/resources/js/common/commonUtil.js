/**
 * Created by cheng on 2018/3/31.
 */

/**
 * 验证码生成
 */
function changeVerifyCode(img) {
    img.src = "/Kaptcha?" + Math.floor(Math.random() * 100);
}
