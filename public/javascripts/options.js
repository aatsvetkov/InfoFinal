var i = 1;
$("#add").click(function (e) {
    //Append a new row of code to the "#items" div
    var str = "'option["+i+"]'";
    $("#items").append("<div><input class='requ' id='option' type='text' name="+str +"><input type='button' value='REMOVE' class=' delete btn-xs btn-danger'></div>");
    i++;
});
$("body").on("click", ".delete", function (e) {
    $(this).parent("div").remove();
    i--;
});

$(document).on('change', '#type', function () {
    var multiOptionalFields = ["RADIO_BUTTON", "CHECK_BOX", "COMBO_BOX"];
    var fieldValue = $('#type').val();
    if ($.inArray(fieldValue, multiOptionalFields) != -1) {
        $('#opts').removeClass('hidden');
    } else {
        $('#opts').addClass('hidden');
    }
});

$(".cancel").click(function(){
    $(".error").removeClass("error");
    $("#label-error").remove();
    $("#option-error").remove();
});