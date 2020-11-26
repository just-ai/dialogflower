$(document).ready(function (e) {

    $('#token-modal').on('shown.bs.modal', function (e) {
        $('#token-form input').val('').focus();
    });

    $('#video-modal').on('hide.bs.modal', function (e) {
        var container$ = $('#video-modal .modal-content');
        var content = container$.html();
        container$.empty().append(content);
    });

    $('#token-form').submit(function (e) {
        e.preventDefault();

        $('#token-modal').modal('hide');
        var token = $('#token-form input').val().trim();

        if (token) {
            $.ajax('/dialog', {
                type: 'POST',
                data: $(this).serialize(),
                success: function (data) {
                    $('#webhook-url').val('https://dialogflower.com/webhook/' + data['id']);
                    $('#token-success').modal('show');
                },
                error: function () {
                    $('#token-error').modal('show');
                }
            });
        }

        return false;
    });

});