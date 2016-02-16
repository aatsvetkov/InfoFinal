$(document).ready(function () {
    $('#loginform').validate({
            rules: {
                username: {
                    required: true,
                    minlength: 4,
                    maxlength: 16,

                },
                password: {
                    required: true,
                    minlength: 4,
                    maxlength: 16,
                }
            }
        }
    )
    $('#addfield').validate({
            rules: {
                label: {
                    required: true,

                },
                'option[0]': {
                    required: true,
                }
            }
        }
    )
    $.validator.addClassRules({
        requ: {
            required: true,
            maxlength: 255,
        }
    });
    $("form").validate();

})
