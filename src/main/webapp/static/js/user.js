$(document).ready(function() {

    function handleFormSubmit(formId, action) {
        $(formId).on('submit', function(e) {
            e.preventDefault();

            const formData = $(this).serialize() + `&action=${action}`;

            $.ajax({
                url: 'user',
                method: 'POST',
                data: formData,
                dataType: 'json',
                success: function(response) {
                    if (response.redirect) {
                        window.location.href = response.redirect;
                    } else if (response.error) {
                        showError(formId, response.error);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Error:', status, error);
                    showError(formId, 'An unexpected error occurred.');
                }
            });
        });
    }

    function showError(formId, message) {
        const form = $(formId);
        const existingError = form.find('.alert-error');
        if (existingError.length) {
            existingError.text(message).show();
        } else {
            const errorDiv = $('<div class="alert alert-error" style="display: none;"></div>').text(message);
            form.prepend(errorDiv);
            errorDiv.fadeIn();
        }
    }

    handleFormSubmit('#loginForm', 'login');

    handleFormSubmit('#registerForm', 'register');

$('#logoutButton').on('click', function(e) {
        e.preventDefault();

        $.ajax({
            url: 'user',
            method: 'POST',
            data: { action: 'logout' },
            dataType: 'json',
            success: function(response) {
                if (response.redirect) {
                    window.location.href = response.redirect;
                } else if (response.error) {
                    showError('#logoutButton', response.error);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error:', status, error);
                showError('#logoutButton', 'An unexpected error occurred.');
            }
        });
    });
});
