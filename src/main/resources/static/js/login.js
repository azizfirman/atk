$(document).ready(function() {
    const errorMessage = $("#error-message");

    if (errorMessage.length) {
        setTimeout(function() {
            errorMessage.hide();
            const url = new URL(window.location);

            url.searchParams.delete("error");
            window.history.replaceState({}, document.title, url);
        }, 2000);
    }
});