$(document).ready(function() {
    const currentUrl = window.location.pathname;
    const csrfToken  = $('meta[name="csrf-token"]').attr('content');

    // Splash to redirect
    if (currentUrl === '/') {
        setTimeout(function() {
            window.location.href = "/login";
        }, 1000);
    }

    // Error show when login
    if (currentUrl === '/login') {
        const errorMessage = $("#error-message");

        if (errorMessage.length) {
            setTimeout(function() {
                errorMessage.hide();
                const url = new URL(window.location);

                url.searchParams.delete("error");
                window.history.replaceState({}, document.title, url);
            }, 2000);
        }
    }

    // Active menu
    $('.menu a').each(function() {
        $(this).removeClass('menu-active');
        if($(this).attr('href') === currentUrl) {
            $(this).addClass('menu-active');
        }
    });

    // Sidebar toggle
    $('.toggle-btn').click(function () {
        $('.sidebar').toggleClass('collapsed');
        $('.top-bar').toggleClass('collapsed');
    });

    // Content menu click
    $('.menu a, .profile-card a').click(function (event) {
        event.preventDefault();
        const href = $(this).attr('href');

        if(href === "/logout") {
            logout(csrfToken);
        }
    });

    // Open dropdown user information
    $('.user-profile').click(function () {
        $('.profile-card').toggleClass('show-profile-card');
    });
});

function logout(token) {
    $.post({
        url: '/logout',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        success: function() {
            window.location.href = '/';
        }
    });
}