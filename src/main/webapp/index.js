/**
 * Created by dy on 2017/7/23.
 */

$(document).ready(function(){
    $("#btn").click(function () {
        $.ajax({
            type: "POST",
            url: "./okk",
            async: false, // 使用同步方式
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            // 1 需要使用JSON.stringify 否则格式为 a=2&b=3&now=14...
            // 2 需要强制类型转换，否则格式为 {"a":"2","b":"3"}
            data: JSON.stringify(
                {"a":1,"b":1.0,"c":"我爱你"}
            ),
            success: function (result) {
                if (result.success) {
                    alert("ok")
                } else {
                    alert("bad")
                }
            }
        });
    });
});