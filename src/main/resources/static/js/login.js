document.addEventListener("DOMContentLoaded", function() {
    const errorMessage = document.getElementById("error-message");

    if (errorMessage) {
        setTimeout(() => {
            errorMessage.style.display = "none";
            const url = new URL(window.location);

            url.searchParams.delete("error");
            window.history.replaceState({}, document.title, url);
        }, 2000);
    }
});