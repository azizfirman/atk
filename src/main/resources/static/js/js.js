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
    $('.toggle-btn i').click(function () {
        $('.sidebar').toggleClass('collapsed');
        $('.top-bar').toggleClass('collapsed');
        $('.content').toggleClass('collapsed');
        $('footer').toggleClass('collapsed');

        localStorage.setItem('sidebarOpen', !$('.sidebar').hasClass('collapsed'));
    });
    if(localStorage.getItem('sidebarOpen') === 'true') {
        // $('.sidebar, .top-bar, .content').css('transition', 'none');

        $('.sidebar').removeClass('collapsed');
        $('.top-bar').removeClass('collapsed');
        $('.content').removeClass('collapsed');
        $('footer').removeClass('collapsed');

        // $('.sidebar').css('transition', 'width 0.3s');
        // $('.top-bar').css('transition', 'margin-left 0.3s');
        // $('.content').css('transition', 'margin-left 0.3s');
    }
    else{
        // $('.sidebar, .top-bar, .content').css('transition', 'none');

        $('.sidebar').addClass('collapsed');
        $('.top-bar').addClass('collapsed');
        $('.content').addClass('collapsed');
        $('footer').addClass('collapsed');
    }

    // Content menu and user information click
    $('.menu a, .profile-card a').click(function () {
        const href = $(this).attr('href');

        if(href === "/logout") {
            logout(csrfToken);
        }
    });

    // Open dropdown user information
    $('.user-profile').click(function () {
        $('.profile-card').toggleClass('show-profile-card');
    });

    if(currentUrl !== "/" && currentUrl !== "/login"){
        $("#yearFooter").text('© ' + moment().format('YYYY'));

        var toastEl = document.getElementById('successToast');
        var toast = new bootstrap.Toast(toastEl);

        // Show tooltip
        $(function () {
            $('[data-bs-toggle="tooltip"], [data-bs-toggle="modal"]').tooltip();
        });
    }

    if(hasQueryParam("cari")){
        if(getQueryParam("cari") == ""){
            deleteQueryParam("cari");
        }
    }

    window.addEventListener('load', function(event) {
        if (localStorage.getItem('userSaved')) {
            $('.toast').addClass("bg-success");
            toast.show();
            localStorage.removeItem('userSaved'); // Hapus status setelah ditampilkan
        }

        if (localStorage.getItem('userUpdate') == true) {
            $('#notifikasi-body').text("Anda sekarang adalah Admin");
            toast.show();
            localStorage.removeItem('userUpdate'); // Hapus status setelah ditampilkan
        }

        if (localStorage.getItem('deleteBarang')) {
            const deleteBarang = JSON.parse(localStorage.getItem('deleteBarang'));
            $('#notifikasi-body').text(deleteBarang.message);

            if(deleteBarang.success){
                $('.toast').addClass("bg-success");
            }else {
                $('.toast').addClass("bg-danger");
            }

            toast.show();
            localStorage.removeItem('deleteBarang');
        }

        if (localStorage.getItem('deletePelanggan')) {
            const deletePelanggan = JSON.parse(localStorage.getItem('deletePelanggan'));
            $('#notifikasi-body').text(deletePelanggan.message);

            if(deletePelanggan.success){
                $('.toast').addClass("bg-success");
            }else {
                $('.toast').addClass("bg-danger");
            }

            toast.show();
            localStorage.removeItem('deletePelanggan');
        }

        if (localStorage.getItem('deletePengguna')) {
            const deletePengguna = JSON.parse(localStorage.getItem('deletePengguna'));
            $('#notifikasi-body').text(deletePengguna.message);

            if(deletePengguna.success){
                $('.toast').addClass("bg-success");
            }else {
                $('.toast').addClass("bg-danger");
            }

            toast.show();
            localStorage.removeItem('deletePengguna');
        }

        if (localStorage.getItem('savedTransaction')) {
            const savedTransaction = JSON.parse(localStorage.getItem('savedTransaction'));
            $('#notifikasi-body').text(savedTransaction.message);
            $('.toast').addClass("bg-success");

            toast.show();
            localStorage.removeItem('savedTransaction'); // Hapus status setelah ditampilkan
        }
    });

    if(currentUrl === "/pengguna" || currentUrl === "/profil"){
        (function () {
            'use strict';
            var form = document.getElementById('tambahPenggunaForm');
            var modal = document.getElementById('tambahPenggunaModal');
            // var loadingSpinner = document.getElementById('loadingSpinner');
            var toastEl = document.getElementById('successToast');
            var toast = new bootstrap.Toast(toastEl);

            var userId = ""; // Ambil ID pengguna
            var user = "";
            var role = "";
            var reset = "";
            var edit = "";
            var nama = "";
            var photo = "";
            var checkRole = false;
            var checkPassword = false;
            var checkEdit = false;

            // Reset form saat modal dibuka
            modal.addEventListener('show.bs.modal', function (event) {
                form.reset();
                form.classList.remove('was-validated');
                Array.from(form.elements).forEach(function (element) {
                    element.classList.remove('is-invalid', 'is-valid');
                });

                var username = document.getElementById('username');

                const button = event.relatedTarget; // Tombol yang membuka modal

                userId = button.getAttribute('data-id'); // Ambil ID pengguna
                user = button.getAttribute('data-username'); // Ambil username pengguna
                role = button.getAttribute('ganti-role');
                reset = button.getAttribute('reset-password');
                edit = button.getAttribute('edit-pengguna');
                nama = button.getAttribute('data-namaPengguna');
                photo = button.getAttribute('data-photo');

                checkRole = button.getAttribute('role');
                checkPassword = button.getAttribute('password');
                checkEdit = button.getAttribute('edit');

                if(checkRole){
                    username.value = user;
                    $('#modal-title').text("Ganti Role")
                    $('#username').attr('disabled', true);

                    $('#level').val(role.toLowerCase());
                    $('#username').val(user);
                    $('#namaPengguna').val(nama);
                    $('#password').val(reset);
                    $('#confirmPassword').val(reset);

                    $('#username1').show();
                    $('#namaPengguna1').hide();
                    $('#role1').show();
                    $('#password1').hide();
                    $('#confirmPassword1').hide();
                    $('#uploadPhoto1').hide();

                    $('#username1').removeAttr('required');
                    $('#namaPengguna1').removeAttr('required');
                    $('#role1').attr('required',true);
                    $('#password1').removeAttr('required');
                    $('#confirmPassword1').removeAttr('required');
                    $('#uploadPhoto1').removeAttr('required');
                }
                else if(checkPassword){
                    username.value = user;
                    $('#modal-title').text("Reset Password")
                    $('#username').attr('disabled', true);

                    $('#level').val(role.toLowerCase());
                    $('#username').val(user);
                    $('#namaPengguna').val(nama);
                    $('#password').val();
                    $('#confirmPassword').val();

                    $('#username1').show();
                    $('#namaPengguna1').hide();
                    $('#role1').hide();
                    $('#password1').show();
                    $('#confirmPassword1').show();
                    $('#uploadPhoto1').hide();

                    $('#username1').removeAttr('required');
                    $('#namaPengguna1').removeAttr('required');
                    $('#role1').attr('required',true);
                    $('#password1').attr('required', true);
                    $('#confirmPassword1').attr('required', true);
                    $('#uploadPhoto1').removeAttr('required');
                }
                else if(checkEdit){
                    username.value = user;
                    $('#modal-title').text("Edit Pengguna")

                    $('#username').val(user);
                    $('#level').val(role.toLowerCase());
                    $('#namaPengguna').val(nama)
                    // $('#uploadPhoto').val(photo)
                    $('#password').val(reset);
                    $('#confirmPassword').val(reset);

                    $('#username').attr('disabled', true);

                    $('#username1').show();
                    $('#namaPengguna1').show();
                    $('#role1').hide();
                    $('#password1').hide();
                    $('#confirmPassword1').hide();
                    $('#uploadPhoto1').show();
                }
                else {
                    $('#modal-title').text("Tambah Pengguna")
                    $('#username').attr('disabled', false);

                    $('#username1').show();
                    $('#namaPengguna1').show();
                    $('#role1').show();
                    $('#password1').show();
                    $('#confirmPassword1').show();
                    $('#uploadPhoto1').show();
                }

                // Setel username ke dalam modal


                // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat modal dibuka
            });

            // Validasi saat mengetik
            form.addEventListener('input', function (event) {
                if (event.target.checkValidity()) {
                    event.target.classList.remove('is-invalid');
                    event.target.classList.add('is-valid');

                    if (event.target.id === 'username') {
                        const username = event.target.value;
                        const feedbackElement = $('#invalid-feedback-username');

                        // Aturan validasi
                        const pattern = /^[a-zA-Z0-9_]+$/; // Pola username (hanya huruf, angka, dan garis bawah)
                        const minLength = 5;

                        // Validasi lokal: pola dan panjang minimum
                        if (username.length < minLength) {
                            feedbackElement.text('Username minimal 5 karakter.');
                            event.target.classList.remove('is-valid');
                            event.target.classList.add('is-invalid');
                            return;
                        }
                        if (!pattern.test(username)) {
                            feedbackElement.text('Username wajib diisi minimal 5 karakter dan hanya boleh mengandung huruf, angka, dan garis bawah (_).');
                            event.target.classList.remove('is-valid');
                            event.target.classList.add('is-invalid');
                            return;
                        }

                        // Lakukan AJAX request ke server jika validasi lokal lolos
                        $.ajax({
                            url: '/api/pengguna/check-username?username=' + username,
                            type: 'GET',
                            // data: JSON.stringify({ username: username }),
                            contentType: false, // Jangan set contentType, jQuery akan menentukannya
                            processData: false, // Jangan proses data, karena ini adalah FormData
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                            },
                            success: function (isTaken) {
                                if (isTaken) {
                                    feedbackElement.text('Username sudah digunakan');
                                    event.target.classList.remove('is-valid');
                                    event.target.classList.add('is-invalid');
                                }
                            },
                            error: function () {
                                feedbackElement.text('Terjadi kesalahan saat memeriksa username.');
                                event.target.classList.remove('is-valid');
                                event.target.classList.add('is-invalid');
                            }
                        });
                    }
                } else {
                    event.target.classList.remove('is-valid');
                    event.target.classList.add('is-invalid');
                }
            });


            document.getElementById('togglePassword').addEventListener('click', function () {
                const passwordInput = document.getElementById('password');
                const toggleIcon = document.getElementById('togglePasswordIcon');
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    toggleIcon.classList.remove('bi-eye');
                    toggleIcon.classList.add('bi-eye-slash');
                } else {
                    passwordInput.type = 'password';
                    toggleIcon.classList.remove('bi-eye-slash');
                    toggleIcon.classList.add('bi-eye');
                }
            });

            document.getElementById('toggleConfirmPassword').addEventListener('click', function () {
                const confirmPasswordInput = document.getElementById('confirmPassword');
                const toggleConfirmIcon = document.getElementById('toggleConfirmPasswordIcon');
                if (confirmPasswordInput.type === 'password') {
                    confirmPasswordInput.type = 'text';
                    toggleConfirmIcon.classList.remove('bi-eye');
                    toggleConfirmIcon.classList.add('bi-eye-slash');
                } else {
                    confirmPasswordInput.type = 'password';
                    toggleConfirmIcon.classList.remove('bi-eye-slash');
                    toggleConfirmIcon.classList.add('bi-eye');
                }
            });

            document.getElementById('password').addEventListener('input', function () {
                const password = this.value;
                const strengthText = document.getElementById('passwordStrength');
                let strength = '';
                if (password.length < 6) {
                    strength = 'Terlalu pendek';
                } else if (password.match(/[A-Z]/) && password.match(/[0-9]/) && password.match(/[^A-Za-z0-9]/)) {
                    strength = 'Kuat';
                } else {
                    strength = 'Sedang';
                }
                strengthText.textContent = 'Kekuatan Password: ' + strength;
            });

            // Menangani pengiriman form
            document.getElementById('simpanButton').addEventListener('click', function (event) {
                event.preventDefault(); // Mencegah tindakan default tombol

                var password = document.getElementById('password').value;
                var confirmPassword = document.getElementById('confirmPassword').value;
                const invalidfeedbackconfirmpassword = $('#iinvalid-feedback-confirm-password');

                // Cek apakah password dan konfirmasi password sama
                if (password !== confirmPassword) {
                    invalidfeedbackconfirmpassword.text('Password dan Konfirmasi Password tidak sama.');
                    // return; // Menghentikan proses jika tidak cocok
                }

                if (form.checkValidity()) {
                    if(checkRole){
                        var level = document.getElementById('level').value;

                        // Buat objek pengguna
                        const pengguna = {
                            idPengguna: userId,
                            level: level.toUpperCase(),
                        };

                        const formData = new FormData();
                        formData.append('pengguna', new Blob([JSON.stringify(pengguna)], { type: 'application/json' })); // Menggunakan Blob untuk JSON


                        $.ajax({
                            url: '/api/pengguna', // Endpoint API
                            type: 'PUT',
                            data: formData,
                            contentType: false, // Jangan set contentType, jQuery akan menentukannya
                            processData: false, // Jangan proses data, karena ini adalah FormData
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                            },
                            success: function(response) {
                                var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            // toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('userSaved', true);
                            // setTimeout(function() {
                                location.reload();
                            // }, 1000);
                            },
                            error: function(xhr, status, error) {
                                alert('Terjadi kesalahan saat menyimpan data: ' + error);
                                // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                            }
                        });

                    }
                    else if(checkPassword){
                        var password = document.getElementById('password').value;

                        const pengguna = {
                            idPengguna: userId,
                            password: password,
                        };

                        const formData = new FormData();
                        formData.append('pengguna', new Blob([JSON.stringify(pengguna)], { type: 'application/json' })); // Menggunakan Blob untuk JSON


                        $.ajax({
                            url: '/api/pengguna', // Endpoint API
                            type: 'PUT',
                            data: formData,
                            contentType: false, // Jangan set contentType, jQuery akan menentukannya
                            processData: false, // Jangan proses data, karena ini adalah FormData
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                            },
                            success: function(response) {
                                var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            // toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('userSaved', true);
                            // setTimeout(function() {
                                location.reload();
                            // }, 1000);
                            },
                            error: function(xhr, status, error) {
                                alert('Terjadi kesalahan saat menyimpan data: ' + error);
                                // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                            }
                        });
                    }
                    else if(checkEdit){
                        var namaPengguna = document.getElementById('namaPengguna').value;
                        var uploadPhoto = document.getElementById('uploadPhoto').files[0];

                        const pengguna = {
                            idPengguna: userId,
                            namaPengguna: namaPengguna,
                        };

                        const formData = new FormData();
                        formData.append('pengguna', new Blob([JSON.stringify(pengguna)], { type: 'application/json' })); // Menggunakan Blob untuk JSON
                        formData.append('photo', uploadPhoto); // Menambahkan foto


                        $.ajax({
                            url: '/api/pengguna', // Endpoint API
                            type: 'PUT',
                            data: formData,
                            contentType: false, // Jangan set contentType, jQuery akan menentukannya
                            processData: false, // Jangan proses data, karena ini adalah FormData
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                            },
                            success: function(response) {
                                var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                            $('.toast').addClass("bg-success");
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            // toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('userSaved', true);
                            setTimeout(function() {
                                location.reload();
                            }, 1000);
                            },
                            error: function(xhr, status, error) {
                                alert('Terjadi kesalahan saat menyimpan data: ' + error);
                                // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                            }
                        });
                    }
                    else {

                        // loadingSpinner.style.display = 'block'; // Tampilkan spinner saat pengiriman

                        var namaPengguna = document.getElementById('namaPengguna').value;
                        var username = document.getElementById('username').value;
                        var uploadPhoto = document.getElementById('uploadPhoto').files[0];
                        var level = document.getElementById('level').value;

                        // var username = document.getElementById('username').value;
                        const feedbackElement = $('#invalid-feedback-username');

                        $.ajax({
                            url: '/api/pengguna/check-username?username=' + username,
                            type: 'GET',
                            // data: JSON.stringify({ username: username }),
                            contentType: false, // Jangan set contentType, jQuery akan menentukannya
                            processData: false, // Jangan proses data, karena ini adalah FormData
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                            },
                            success: function (isTaken) {
                                if (isTaken) {
                                    feedbackElement.text('Username sudah digunakan');
                                    document.getElementById('username').classList.remove('is-valid');
                                    document.getElementById('username').classList.add('is-invalid');
                                    return;
                                }
                                else {
                                    const pengguna = {
                                        namaPengguna: namaPengguna,
                                        username: username,
                                        password: password,
                                        level: level.toUpperCase(),
                                    };

                                    // Buat FormData untuk mengirim data
                                    const formData = new FormData();
                                    formData.append('pengguna', new Blob([JSON.stringify(pengguna)], { type: 'application/json' })); // Menggunakan Blob untuk JSON
                                    formData.append('photo', uploadPhoto); // Menambahkan foto

                                    $.ajax({
                                        url: '/api/pengguna', // Endpoint API
                                        type: 'POST',
                                        data: formData,
                                        contentType: false, // Jangan set contentType, jQuery akan menentukannya
                                        processData: false, // Jangan proses data, karena ini adalah FormData
                                        beforeSend: function(xhr) {
                                            xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                                        },
                                        success: function(response) {
                                            var modalInstance = bootstrap.Modal.getInstance(modal);
                                            modalInstance.hide();
                                            form.reset(); // Reset form setelah pengiriman
                                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                                            // toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                                            localStorage.setItem('userSaved', true);
                                            setTimeout(function() {
                                                location.reload();
                                            }, 2000);
                                        },
                                        error: function(xhr, status, error) {
                                            alert('Terjadi kesalahan saat menyimpan data: ' + error);
                                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                                        }
                                    });
                                }
                            },
                            error: function () {
                                feedbackElement.text('Terjadi kesalahan saat memeriksa username.');
                                document.getElementById('username').classList.remove('is-valid');
                                document.getElementById('username').classList.add('is-invalid');
                                return;
                            }
                        });
                    }
                } else {
                    // Jika tidak valid, tambahkan kelas untuk menandai form sebagai divalidasi
                    form.classList.add('was-validated');
                }
            });

            if(currentUrl === "/pengguna"){
                const confirmDeleteModal = document.getElementById('confirmDeleteModal');
                const confirmDeleteButton = document.getElementById('confirmDeleteButton');
                const usernameToDeleteElement = document.getElementById('usernameToDelete');
                let userIdToDelete = null;

                confirmDeleteModal.addEventListener('show.bs.modal', function (event) {
                    const button = event.relatedTarget; // Tombol yang membuka modal
                    userIdToDelete = button.getAttribute('data-id'); // Ambil ID pengguna
                    const usernameToDelete = button.getAttribute('data-username'); // Ambil username pengguna
                    // Setel username ke dalam modal
                    usernameToDeleteElement.textContent = usernameToDelete;
                });

                confirmDeleteButton.addEventListener('click', function () {
                    if (userIdToDelete) {
                        $.ajax({
                            url: `/api/pengguna/${userIdToDelete}`, // Endpoint API
                            type: 'DELETE',
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                            },
                            success: function(response) {
                                if(response === ""){
                                    const modal = bootstrap.Modal.getInstance(confirmDeleteModal);
                                    modal.hide();

                                    localStorage.setItem('deletePengguna', JSON.stringify({
                                        success: true,
                                        message: "Berhasil menghapus data Pengguna."
                                    }));
                                    location.reload();
                                }
                                else {
                                    localStorage.setItem('deletePengguna', JSON.stringify({
                                        success: false,
                                        message: response,
                                    }));
                                    toast.show();
                                    location.reload();
                                }
                            },
                            error: function(xhr, status, error) {
                                alert('Gagal menghapus data!');
                            }
                        });
                    }
                });
            }
        })();

        $("#cariPengguna").click(function(){
            addQueryParam("cari", $("#txtCariPengguna").val())
        })
        $('#txtCariPengguna').on('keypress', function (e) {
            if (e.which === 13) {
                addQueryParam("cari", $("#txtCariPengguna").val())
            }
        });
    }

    if(currentUrl === "/pengguna"){
        var disabledHapusAkun = $('#btnHapusAkun').is(':disabled');
        var disabledGantiRole = $('#btnGantiRole').is(':disabled');
        var txtLevelPengguna  = $('#txtLevelPengguna').text()

        if(!hasQueryParam("cari")){
            localStorage.setItem('satuPengguna', disabledGantiRole && disabledHapusAkun && txtLevelPengguna != "ADMIN");
            $("#subtitlePengguna").text("Menampilkan seluruh pengguna.")
        }
        else{
            $("#subtitlePengguna").text("Menampilkan pengguna dengan kata kunci \"" + getQueryParam("cari") + "\".");
            $("#txtCariPengguna").val(getQueryParam("cari"));
        }

        if(disabledGantiRole && disabledHapusAkun && txtLevelPengguna != "ADMIN" && localStorage.getItem("satuPengguna") && !hasQueryParam("cari")){
            var userId  = $('#btnGantiRole').attr("data-id")

            const pengguna = {
                idPengguna: userId,
                level: "ADMIN",
            };

            const formData = new FormData();
            formData.append('pengguna', new Blob([JSON.stringify(pengguna)], { type: 'application/json' })); // Menggunakan Blob untuk JSON

            $.ajax({
                url: '/api/pengguna', // Endpoint API
                type: 'PUT',
                data: formData,
                contentType: false, // Jangan set contentType, jQuery akan menentukannya
                processData: false, // Jangan proses data, karena ini adalah FormData
                beforeSend: function(xhr) {
                    xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                },
                success: function(response) {
                    localStorage.setItem('userUpdate', true);
                    localStorage.setItem('notifikasiBody', 'true');
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert('Terjadi kesalahan ' + error);
                    // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                }
            });
        }
        else {
            $('#btnGantiRole').prop('disabled', false);
            $('#btnHapusAkun').prop('disabled', false);
        }
    }

    if(currentUrl === "/profil"){
        const dataTransaksi = $("#salesChart").data('transaksi');

        // Mengambil tanggal saat ini
        const today = new Date();
        const month = today.getMonth(); // Bulan saat ini (0-11)
        const year = today.getFullYear(); // Tahun saat ini

        // Membuat array untuk singkatan nama bulan
        const monthNames = ["Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agust", "Sep", "Okt", "Nov", "Des"];

        // Menghitung jumlah hari di bulan ini
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        // Membuat label untuk setiap hari di bulan ini
        const labels = Array.from({ length: daysInMonth }, (_, i) =>
            `${i + 1} ${monthNames[month]}` // Format dengan singkatan bulan
        );

        const ctx = document.getElementById('salesChart').getContext('2d');
        new Chart(ctx, {
            type: 'line', // Jenis grafik
            data: {
                labels: labels, // Menggunakan label yang dihasilkan
                datasets: [{
                    label: 'Jumlah Penjualan',
                    data: dataTransaksi, // Data penjualan per hari
                    borderColor: '#4A90E2', // Warna garis
                    backgroundColor: 'rgba(75, 192, 192, 0.2)', // Warna area grafik
                    borderWidth: 2,
                    fill: true // Mengisi area di bawah garis
                }]
            },
            options: {
                responsive: true, // Masih responsif
                maintainAspectRatio: false, // Tidak mempertahankan rasio aspek
                scales: {
                    y: {
                        beginAtZero: true // Mulai dari nol
                    }
                }
            }
        });
    }

    if(currentUrl === "/barang"){
        document.querySelectorAll('.harga-barang').forEach(function (element) {
            let harga = parseFloat(element.innerText);
            element.innerText = 'Rp. ' + harga.toLocaleString('id-ID');
        });

        if(!hasQueryParam("cari") || getQueryParam("cari") == "" ){
            $("#subtitleBarang").text("Menampilkan semua data barang.")
        }
        else{
            $("#subtitleBarang").text("Menampilkan barang dengan kata kunci \"" + getQueryParam("cari") + "\".");
            $("#txtCariBarang").val(getQueryParam("cari"));
        }

        $("#cariBarang").click(function(){
            addQueryParam("cari", $("#txtCariBarang").val())
        })
        $('#txtCariBarang').on('keypress', function (e) {
            if (e.which === 13) {
                addQueryParam("cari", $("#txtCariBarang").val())
            }
        });

        var form = document.getElementById('tambahBarangForm');
        var modal = document.getElementById('tambahBarangModal');
        var toastEl = document.getElementById('successToast');
        var toast = new bootstrap.Toast(toastEl);

        var idPenggunaLogin = "";
        var idBarang = "";
        var namaBarang = "";
        var hargaBarang = "";
        var stokBarang = "";

        var editBarang = false;
        // Reset form saat modal dibuka
        modal.addEventListener('show.bs.modal', function (event) {
            form.reset();
            form.classList.remove('was-validated');
            Array.from(form.elements).forEach(function (element) {
                element.classList.remove('is-invalid', 'is-valid');
            });

            idPenggunaLogin = $('#tambahBarang').data('id-pengguna');

            const button = event.relatedTarget;

            idBarang = button.getAttribute('data-idBarang'); // Ambil ID pengguna
            namaBarang = button.getAttribute('data-namaBarang'); // Ambil username pengguna
            hargaBarang = button.getAttribute('data-hargaBarang');
            stokBarang = button.getAttribute('data-stokBarang');


            editBarang = button.getAttribute('edit');

            if(editBarang){
                $('#modal-title').text("Edit Barang")

                $('#namaBarang').val(namaBarang);
                $('#hargaBarang').val(hargaBarang);
                $('#stokBarang').val(stokBarang);
            }
            else {
                $('#modal-title').text("Tambah Barang")
            }
        });

        // Validasi saat mengetik
        form.addEventListener('input', function (event) {
            if (event.target.checkValidity()) {
                event.target.classList.remove('is-invalid');
                event.target.classList.add('is-valid');
            } else {
                event.target.classList.remove('is-valid');
                event.target.classList.add('is-invalid');
            }
        });

        document.getElementById('simpanButton').addEventListener('click', function (event) {
            event.preventDefault(); // Mencegah tindakan default tombol

            if (form.checkValidity()) {
                var namaBarang = document.getElementById('namaBarang').value;
                var hargaBarang = document.getElementById('hargaBarang').value;
                var stokBarang = document.getElementById('stokBarang').value;

                const pengguna = {
                    idPengguna: idPenggunaLogin,
                };

                const barang = {
                    idBarang: idBarang,
                    namaBarang: namaBarang,
                    hargaBarang: hargaBarang,
                    stokBarang: stokBarang,
                    pengguna: pengguna
                };

                if(editBarang){
                    $.ajax({
                        url: '/api/barang', // Endpoint API
                        type: 'PUT',
                        data: JSON.stringify(barang),
                        contentType: 'application/json', // Jangan set contentType, jQuery akan menentukannya
                        processData: false, // Jangan proses data, karena ini adalah FormData
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                        },
                        success: function(response) {
                            var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('addBarangSaved', 'true');
                            // setTimeout(function() {
                            location.reload();
                            // }, 1000);
                        },
                        error: function(xhr, status, error) {
                            alert('Terjadi kesalahan saat menyimpan data: ' + error);
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                        }
                    });
                }
                else {
                    $.ajax({
                        url: '/api/barang', // Endpoint API
                        type: 'POST',
                        data: JSON.stringify(barang),
                        contentType: 'application/json', // Jangan set contentType, jQuery akan menentukannya
                        processData: false, // Jangan proses data, karena ini adalah FormData
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                        },
                        success: function(response) {
                            var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('addBarangSaved', 'true');
                            // setTimeout(function() {
                            deleteQueryParam("page");
                            location.reload();
                            // }, 1000);
                        },
                        error: function(xhr, status, error) {
                            alert('Terjadi kesalahan saat menyimpan data: ' + error);
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                        }
                    });
                }
            } else {
                // Jika tidak valid, tambahkan kelas untuk menandai form sebagai divalidasi
                form.classList.add('was-validated');
            }
        });

        const confirmDeleteModal = document.getElementById('confirmDeleteModal');
        const confirmDeleteButton = document.getElementById('confirmDeleteButton');
        const usernameToDeleteElement = document.getElementById('usernameToDelete');
        const textToDelete = document.getElementById('textToDelete');

        let IdToDelete = "";

        confirmDeleteModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget; // Tombol yang membuka modal
            IdToDelete = button.getAttribute('data-idBarang'); // Ambil ID pengguna
            const barangToDelete = button.getAttribute('data-namaBarang'); // Ambil username pengguna

            // Setel username ke dalam modal
            textToDelete.textContent = "Apakah Anda yakin ingin menghapus barang "
            usernameToDeleteElement.textContent = barangToDelete;
        });

        confirmDeleteButton.addEventListener('click', function () {
            if (IdToDelete) {
                $.ajax({
                    url: `/api/barang/${IdToDelete}`, // Endpoint API
                    type: 'DELETE',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                    },
                    success: function(response) {
                        if(response === ""){
                            const modal = bootstrap.Modal.getInstance(confirmDeleteModal);
                            modal.hide();

                            localStorage.setItem('deleteBarang', JSON.stringify({
                                success: true,
                                message: "Berhasil menghapus barang."
                            }));
                            location.reload();
                        }
                        else {
                            localStorage.setItem('deleteBarang', JSON.stringify({
                                success: false,
                                message: response,
                            }));
                            toast.show();
                            location.reload();
                        }
                    },
                    error: function(xhr, status, error) {
                        alert('Gagal menghapus data!');
                    }
                });
            }
        });
    }

    if(currentUrl === "/pelanggan"){
        if(!hasQueryParam("cari") || getQueryParam("cari") == "" ){
            $("#subtitlePelanggan").text("Menampilkan semua data pelanggan.")
        }
        else{
            $("#subtitlePelanggan").text("Menampilkan data pelanggan dengan kata kunci \"" + getQueryParam("cari") + "\".");
            $("#txtCariPelanggan").val(getQueryParam("cari"));
        }

        $("#cariPelanggan").click(function(){
            addQueryParam("cari", $("#txtCariPelanggan").val())
        })
        $('#txtCariPelanggan').on('keypress', function (e) {
            if (e.which === 13) {
                addQueryParam("cari", $("#txtCariPelanggan").val())
            }
        });

        var form = document.getElementById('tambahPelangganForm');
        var modal = document.getElementById('tambahPelangganModal');
        var toastEl = document.getElementById('successToast');
        var toast = new bootstrap.Toast(toastEl);

        var idPenggunaLogin = "";
        var idPelanggan = "";
        var namaPelanggan = "";
        var alamatPelanggan = "";
        var teleponPelanggan = "";

        var editPelanggan = false;
        // Reset form saat modal dibuka
        modal.addEventListener('show.bs.modal', function (event) {
            form.reset();
            form.classList.remove('was-validated');
            Array.from(form.elements).forEach(function (element) {
                element.classList.remove('is-invalid', 'is-valid');
            });

            idPenggunaLogin = $('#tambahPelanggan').data('id-pengguna');

            const button = event.relatedTarget;

            idPelanggan = button.getAttribute('data-idPelanggan'); // Ambil ID pengguna
            namaPelanggan = button.getAttribute('data-namaPelanggan'); // Ambil username pengguna
            alamatPelanggan = button.getAttribute('data-alamat');
            teleponPelanggan = button.getAttribute('data-telepon');

            editPelanggan = button.getAttribute('edit');

            if(editPelanggan){
                $('#modal-title').text("Edit Pelanggan")

                $('#namaPelanggan').val(namaPelanggan);
                $('#alamatPelanggan').val(alamatPelanggan);
                $('#teleponPelanggan').val(teleponPelanggan);
            }
            else {
                $('#modal-title').text("Tambah Pelanggan")
            }
        });

        // Validasi saat mengetik
        form.addEventListener('input', function (event) {
            if (event.target.checkValidity()) {
                event.target.classList.remove('is-invalid');
                event.target.classList.add('is-valid');
            } else {
                event.target.classList.remove('is-valid');
                event.target.classList.add('is-invalid');
            }
        });

        document.getElementById('simpanButton').addEventListener('click', function (event) {
            event.preventDefault(); // Mencegah tindakan default tombol

            if (form.checkValidity()) {
                var namaPelanggan = document.getElementById('namaPelanggan').value;
                var alamatPelanggan = document.getElementById('alamatPelanggan').value;
                var teleponPelanggan = document.getElementById('teleponPelanggan').value;

                const pelanggan = {
                    idPelanggan: idPelanggan,
                    namaPelanggan: namaPelanggan,
                    alamat: alamatPelanggan,
                    telepon: teleponPelanggan,
                };

                if(editPelanggan){
                    $.ajax({
                        url: '/api/pelanggan', // Endpoint API
                        type: 'PUT',
                        data: JSON.stringify(pelanggan),
                        contentType: 'application/json', // Jangan set contentType, jQuery akan menentukannya
                        processData: false, // Jangan proses data, karena ini adalah FormData
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                        },
                        success: function(response) {
                            var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('addPelangganSaved', 'true');
                            // setTimeout(function() {
                            location.reload();
                            // }, 1000);
                        },
                        error: function(xhr, status, error) {
                            alert('Terjadi kesalahan saat menyimpan data: ' + error);
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                        }
                    });
                }
                else {
                    $.ajax({
                        url: '/api/pelanggan', // Endpoint API
                        type: 'POST',
                        data: JSON.stringify(pelanggan),
                        contentType: 'application/json', // Jangan set contentType, jQuery akan menentukannya
                        processData: false, // Jangan proses data, karena ini adalah FormData
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                        },
                        success: function(response) {
                            var modalInstance = bootstrap.Modal.getInstance(modal);
                            modalInstance.hide();
                            form.reset(); // Reset form setelah pengiriman
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            toast.show(); // Tampilkan toast setelah data berhasil disimpan/ Reset form jika diperlukan
                        // loadingSpinner.style.display = 'none'; // Sembunyikan spinner setelah selesai
                            localStorage.setItem('addPelangganSaved', 'true');
                            // setTimeout(function() {
                            location.reload();
                            // }, 1000);
                        },
                        error: function(xhr, status, error) {
                            alert('Terjadi kesalahan saat menyimpan data: ' + error);
                            // loadingSpinner.style.display = 'none'; // Sembunyikan spinner saat terjadi kesalahan
                        }
                    });
                }
            } else {
                // Jika tidak valid, tambahkan kelas untuk menandai form sebagai divalidasi
                form.classList.add('was-validated');
            }
        });

        const confirmDeleteModal = document.getElementById('confirmDeleteModal');
        const confirmDeleteButton = document.getElementById('confirmDeleteButton');
        const usernameToDeleteElement = document.getElementById('usernameToDelete');
        const textToDelete = document.getElementById('textToDelete');

        let IdToDelete = "";

        confirmDeleteModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget; // Tombol yang membuka modal
            IdToDelete = button.getAttribute('data-idPelanggan'); // Ambil ID pengguna
            const barangToDelete = button.getAttribute('data-namaPelanggan'); // Ambil username pengguna

            // Setel username ke dalam modal
            textToDelete.textContent = "Apakah Anda yakin ingin menghapus data pelanggan ini dengan nama "
            usernameToDeleteElement.textContent = barangToDelete;
        });

        confirmDeleteButton.addEventListener('click', function () {
            if (IdToDelete) {
                $.ajax({
                    url: `/api/pelanggan/${IdToDelete}`, // Endpoint API
                    type: 'DELETE',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                    },
                    success: function(response) {
                        if(response === ""){
                            const modal = bootstrap.Modal.getInstance(confirmDeleteModal);
                            modal.hide();

                            localStorage.setItem('deletePelanggan', JSON.stringify({
                                success: true,
                                message: "Berhasil menghapus data Pelanggan."
                            }));
                            location.reload();
                        }
                        else {
                            localStorage.setItem('deletePelanggan', JSON.stringify({
                                success: false,
                                message: response,
                            }));
                            toast.show();
                            location.reload();
                        }
                    },
                    error: function(xhr, status, error) {
                        alert('Gagal menghapus data!');
                    }
                });
            }
        });
    }

    if(currentUrl === "/transaksi"){
        if(!hasQueryParam("cari") || getQueryParam("cari") == "" ){
            $("#subtitleTransaksi").text("Menampilkan semua data transaksi.")
        }
        else{
            $("#subtitleTransaksi").text("Menampilkan data transaksi dengan kata kunci \"" + getQueryParam("cari") + "\".");
            $("#txtCariTransaksi").val(getQueryParam("cari"));
        }

        $("#cariTransaksi").click(function(){
            addQueryParam("cari", $("#txtCariTransaksi").val())
        })
        $('#txtCariTransaksi').on('keypress', function (e) {
            if (e.which === 13) {
                addQueryParam("cari", $("#txtCariTransaksi").val())
            }
        });

        $('#accordionTransaksi tr[data-bs-toggle="collapse"]').on('click', function () {
            var target = $(this).data('bs-target');

            $('#accordionTransaksi .collapse').not(target).collapse('hide');
        });

        var form = document.getElementById('tambahTransaksiForm');
        var modal = document.getElementById('tambahTransaksiModal');

        var idBarang = "";
        var namaBarang = "";
        var stokBarang = "";
        var hargaBarang = "";

        var dataBarang = [];
        var totalHarga = 0;

        $("#cariBarang").on("input", function() {
            idBarang = ""
            form.classList.remove('was-validated');

            $('#cariBarang').addClass('is-invalid');
            $('#cariBarang').removeClass('is-valid');

            const keyword = $(this).val();
            $.get(`api/barang?cari=${keyword}&page=0&size=10`, function(data) {
                $("#dropdownBarang").empty(); // Kosongkan dropdown sebelum menambahkan item baru

                // Tampilkan dropdown jika ada hasil
                if(keyword != "") {
                    $("#dropdownBarang").show();
                } else {
                    $("#dropdownBarang").hide();
                }

                data.forEach(barang => {
                    var exists = false;
                    dataBarang.forEach(addBarang => {
                        if(addBarang.idBarang === barang.idBarang){
                            exists = true;
                        }
                    });

                    if(!exists){
                        const disabledClass = barang.stokBarang === 0 ? 'disabled' : '';
                        const highlightedName = barang.namaBarang.replace(new RegExp(keyword, 'gi'), match => `<strong>${match}</strong>`);
                        const hargaBarangDropdown = `<span style='font-size: 10px'>Rp. ${(barang.hargaBarang).toLocaleString("id-ID") + ",-"}</span>`
                        const highlightedStok = barang.stokBarang === 0 ? `<span style='font-size: 10px' class='text-danger'>Stok Habis</span>` : `<span style='font-size: 10px'>Stok: ${barang.stokBarang}</span>`;

                        // Buat elemen <li>
                        const li = $('<li></li>');

                        // Buat elemen <a>
                        const a = $('<a></a>');

                        a.attr('href', '') // Tambahkan atribut href
                        a.addClass(`dropdown-item ${disabledClass}`) // Tambahkan kelas
                        a.html(`${highlightedName} - ${hargaBarangDropdown} (${highlightedStok})`) // Isi HTML
                        a.on('click', function(event) { // Tambahkan event click
                            event.preventDefault();


                            $("#dropdownBarang").hide();
                            $('#cariBarang').addClass('is-valid');
                            $('#cariBarang').removeClass('is-invalid');

                            idBarang = barang.idBarang;
                            namaBarang = barang.namaBarang;
                            stokBarang = barang.stokBarang;
                            hargaBarang = barang.hargaBarang;

                            $('#cariBarang').val(namaBarang + " (stok : " + stokBarang +")");
                            $("#quantity").focus();
                        });

                        // Tambahkan <a> ke <li>
                        li.append(a);

                        // Tambahkan <li> ke dropdown
                        $("#dropdownBarang").append(li);
                        $("#quantity").attr("max", barang.stokBarang);
                    }
                });
            });
        });

        document.getElementById('quantity').addEventListener('keydown', function(event) {
            // Cek jika tombol yang ditekan adalah tanda minus
            if (event.key === '-' || event.key === '+' || event.key === 'e') {
                event.preventDefault(); // Mencegah input tanda minus
            }
        });

        modal.addEventListener('show.bs.modal', function (event) {
            form.reset();
            form.classList.remove('was-validated');
            Array.from(form.elements).forEach(function (element) {
                element.classList.remove('is-invalid', 'is-valid');
            });
        });

        $('#quantity').on('input', function() {
            form.classList.remove('was-validated');

            const quantityValue = parseInt($('#quantity').val(), 10);
            if(isNaN(quantityValue) || quantityValue <= 0) {
                $('#quantity').addClass('is-invalid');
                $('#quantity').removeClass('is-valid');
                $("#quantityInfo").text("Wajib diisi.");
            }
            else if(quantityValue > stokBarang) {
                $('#quantity').addClass('is-invalid');
                $('#quantity').removeClass('is-valid');
                $("#quantityInfo").text("Melebihi Stock.");
            }
            else {
                $('#quantity').removeClass('is-invalid');
                $('#quantity').addClass('is-valid');
            }
        });

        $('#tambahBarangBtn').on('click', function(event) {
            event.preventDefault();

            if ($('#cariBarang').val().trim() === "" || idBarang === "") {
                $('#cariBarang').removeClass('is-valid');
                $('#cariBarang').addClass('is-invalid');
            }

            const quantityValue = parseInt($('#quantity').val(), 10);
            if(isNaN(quantityValue) || quantityValue < 0) {
                $('#quantity').removeClass('is-valid');
                $('#quantity').addClass('is-invalid');
                $("#quantityInfo").text("Wajib diisi.");
            }

            if(quantityValue > stokBarang) {
                $('#quantity').addClass('is-invalid');
                $('#quantity').removeClass('is-valid');
                $("#quantityInfo").text("Melebihi Stock.");
            }

            if(form.checkValidity() && idBarang != "" && quantityValue > 0 && quantityValue <= stokBarang) {
                const index = dataBarang.length + 1;
                const quantity = $("#quantity").val();
                const total = parseInt(quantity) * parseInt(hargaBarang);

                totalHarga = totalHarga + total;
                $("#totalHarga").text("Rp. " + totalHarga.toLocaleString("id-ID") + ",-");

                const tr = $('<tr></tr>');
                tr.attr("id", "row-" +index);

                const tdNo = $('<td></td>').text(index);
                tdNo.addClass("nomor");
                tr.append(tdNo);

                const tdNamaBarang = $('<td></td>').text(namaBarang);
                const spanStok = $('<span></span>').addClass("badge bg-secondary ms-2");

                spanStok.text("stok:" + stokBarang);
                tdNamaBarang.append(spanStok);
                tr.append(tdNamaBarang);

                const tdHargaBarang = $('<td></td>').text("Rp. " + hargaBarang.toLocaleString("id-ID"));
                tr.append(tdHargaBarang);

                const tdQuantity = $('<td></td>')
                const inputQuantity = $('<input></input>');

                inputQuantity.addClass("form-control no-border");
                inputQuantity.attr("data-bs-toggle", "tooltip");
                inputQuantity.attr("data-bs-placement", "top");
                inputQuantity.attr("data-id-barang", idBarang);
                inputQuantity.attr("placeholder", "0");
                inputQuantity.attr("value", quantity);
                inputQuantity.attr("max", stokBarang);
                inputQuantity.attr("type", "number");
                inputQuantity.attr("min", "1");

                new bootstrap.Tooltip(inputQuantity[0], {trigger: 'manual'});

                inputQuantity.keydown(function(event) {
                    if(event.key === '-' || event.key === '+' || event.key === 'e') {
                        event.preventDefault();
                    }
                });

                inputQuantity.on('blur', function () {
                    const tooltipInstance = bootstrap.Tooltip.getInstance(this);
                    if (tooltipInstance) {
                        tooltipInstance.hide();
                    }
                });

                inputQuantity.on("input", function() {
                    const quantity = parseInt($(this).val());
                    const idBarang = $(this).data("id-barang");
                    const tooltipInstance = bootstrap.Tooltip.getInstance(this);

                    if($(this).val() != "" && $(this).val() != 0){
                        dataBarang.forEach((item) => {
                            if(item.idBarang == idBarang){
                                if(quantity > item.stokBarang){
                                    $(this).attr("data-bs-original-title", "Melebihi stok.");
                                    $(this).addClass("tooltip-error");
                                    $(this).addClass("is-invalid");
                                    tooltipInstance.show();
                                }
                                else {
                                    $(this).removeAttr("data-bs-original-title");
                                    $(this).removeClass("tooltip-error");
                                    $(this).removeClass("is-invalid");
                                    tooltipInstance.hide();

                                    item.jumlah = quantity;
                                    item.total = quantity * item.hargaBarang;

                                    $(this).closest('tr').find('td').each(function(index) {
                                        if(index === 4){
                                            $(this).text("Rp. " + item.total.toLocaleString("id-ID"));
                                        }
                                    });
                                    inputQuantity.attr("value", quantity);
                                }
                            }
                        });

                        totalHarga = 0;
                        dataBarang.forEach(item => totalHarga = totalHarga + item.total);
                        $("#totalHarga").text("Rp. " + totalHarga.toLocaleString("id-ID") + ",-");
                    }
                    else {
                        $(this).attr("data-bs-original-title", "Wajib diisi.");
                        $(this).addClass("tooltip-error");
                        $(this).addClass("is-invalid");
                        tooltipInstance.show();
                    }
                });

                tdQuantity.addClass("align-middle");
                tdQuantity.css("width", "100px");
                tdQuantity.addClass("p-0 m-0");

                tdQuantity.append(inputQuantity);
                tr.append(tdQuantity);

                const tdTotal = $('<td></td>').text("Rp. " + total.toLocaleString("id-ID"));
                tr.append(tdTotal);

                const tdAksi = $('<td></td>');
                const btnDelete = $('<button></button>')
                const iconDelete = $('<i></i>')

                iconDelete.addClass("bi bi-trash");

                btnDelete.addClass("btn btn-sm btn-outline-danger")
                btnDelete.attr("data-id-barang", idBarang);
                btnDelete.attr("title", "Hapus Barang");
                btnDelete.tooltip();

                btnDelete.append(iconDelete);
                tdAksi.append(btnDelete);
                tr.append(tdAksi);

                btnDelete.on('click', function(event) {
                    event.preventDefault();
                    const idBarang = $(this).data("id-barang");
                    const tooltipInstance = bootstrap.Tooltip.getInstance(this);

                    // Hapus tooltip dari elemen tombol
                    if (tooltipInstance) {
                        tooltipInstance.dispose();
                    }

                    dataBarang.forEach((item, index) => {
                        if(item.idBarang == idBarang){
                            dataBarang.splice(index, 1);
                            $(this).closest('tr').remove();
                            totalHarga = totalHarga - (item.total);
                            $("#totalHarga").text("Rp. " + totalHarga.toLocaleString("id-ID") + ",-");
                        }
                    });

                    if(dataBarang.length === 0){
                        $("#lanjutTransaksiBtn").addClass("d-none");
                        $("#divTabelBarang").addClass("d-none");
                        $("#divTotalHarga").addClass("d-none");
                    }

                    $('#tabelBarang tr').each(function (index) {
                        $(this).find('.nomor').text(index + 1);
                    });
                });

                dataBarang.push({
                    hargaBarang: hargaBarang,
                    stokBarang: stokBarang,
                    idBarang: idBarang,
                    jumlah: quantity,
                    total: total,
                });

                hargaBarang = "";
                namaBarang = "";
                stokBarang = "";
                idBarang = "";

                $("#tambahTransaksiForm input").removeClass("is-invalid");
                $("#tambahTransaksiForm input").removeClass("is-valid");
                $("#cariBarang, #quantity").val("");
                $("#tabelBarang").append(tr);

                $("#lanjutTransaksiBtn").removeClass("d-none");
                $("#divTabelBarang").removeClass("d-none");
                $("#divTotalHarga").removeClass("d-none");
            }
        });

        $("#btnBatalkanTransaksi").on('click', function() {
            if(dataBarang.length > 0){
                $("#btnCloseKonfirmasi").hide();
                $("#confirmDeleteModal").modal('show');
                $("#textToDelete").text("Apakah Anda yakin ingin membatalkan transaksi ini dan menghapus data barang yang sudah ditambahkan?");
            }
            else {
                $('#tambahTransaksiModal').modal('hide');
            }
        });

        $("#confirmDeleteButton").on('click', function() {
            dataBarang = [];
            totalHarga = 0;

            $('#tabelBarang').empty();
            $("#divTotalHarga").addClass("d-none");
            $("#divTabelBarang").addClass("d-none");
            $("#lanjutTransaksiBtn").addClass("d-none");

            $("#confirmDeleteModal").modal('hide');
            $('#tambahTransaksiModal').modal('hide');
        });

        $('#lanjutTransaksiBtn').on('click', function() {
            $("#transactionDate").text(moment().format("DD/MM/YYYY"));
            $("#totalAmount").text("Rp. " + totalHarga.toLocaleString("id-ID") + ",-");

            $('#tambahTransaksiModal').modal('hide');
            $('#confirmationModal').modal('show');

            $('#cariPelanggan').removeClass('is-invalid');
            $('#cariPelanggan').removeClass('is-valid');
        });

        $("#btnBackToModalTambahTransaksi").on('click', function() {
            $('#confirmationModal').modal('hide');
            $('#tambahTransaksiModal').modal('show');
        });

        var idPelanggan = "";
        var namaPelanggan = "";
        var alamatPelanggan = "";

        $("#cariPelanggan").on("input", function() {
            idPelanggan = "";
            namaPelanggan = "";
            alamatPelanggan = "";

            $('#cariPelanggan').addClass('is-invalid');
            $('#cariPelanggan').removeClass('is-valid');

            const keyword = $(this).val();
            $.get(`api/pelanggan?cari=${keyword}&page=0&size=10`, function(data) {
                $("#dropdownPelanggan").empty(); // Kosongkan dropdown sebelum menambahkan item baru

                // Tampilkan dropdown jika ada hasil
                if(keyword != "") {
                    $("#dropdownPelanggan").show();
                } else {
                    $("#dropdownPelanggan").hide();
                }

                data.forEach(pelanggan => {
                    const highlightedName = pelanggan.namaPelanggan.replace(new RegExp(keyword, 'gi'), match => `<strong>${match}</strong>`);
                    const highlightedAlamat = pelanggan.alamat.replace(new RegExp(keyword, 'gi'), match => `<strong>${match}</strong>`);

                    // Buat elemen <li>
                    const li = $('<li></li>');

                    // Buat elemen <a>
                    const a = $('<a></a>');

                    a.attr('href', '') // Tambahkan atribut href
                    a.addClass(`dropdown-item`) // Tambahkan kelas
                    a.html(`${highlightedName} <span style="font-size: 10px;">(${highlightedAlamat})</span`) // Isi HTML
                    a.on('click', function(event) { // Tambahkan event click
                        event.preventDefault();

                        $("#dropdownPelanggan").hide();
                        $('#cariPelanggan').addClass('is-valid');
                        $('#cariPelanggan').removeClass('is-invalid');

                        $('#cariPelanggan').val(pelanggan.namaPelanggan + " (" + pelanggan.alamat + ")");
                        idPelanggan = pelanggan.idPelanggan;
                        namaPelanggan = pelanggan.namaPelanggan;
                        alamatPelanggan = pelanggan.alamat;
                    });

                    // Tambahkan <a> ke <li>
                    li.append(a);

                    // Tambahkan <li> ke dropdown
                    $("#dropdownPelanggan").append(li);
                });
            });
        });

        $("#btnSimpanTransaksi, #btnCetakTransaksi").on('click', function() {
            if(idPelanggan === ""){
                $('#cariPelanggan').addClass('is-invalid');
                $('#cariPelanggan').removeClass('is-valid');
            }
            else {
                const transaksi = {
                    idPengguna: $(this).data('id-pengguna'),
                    idPelanggan: idPelanggan,
                    barang: dataBarang,
                }

                const button = $(this);

                $.ajax({
                    url: '/api/transaksi', // Endpoint API
                    type: 'POST',
                    data: JSON.stringify(transaksi),
                    contentType: 'application/json', // Jangan set contentType, jQuery akan menentukannya
                    processData: false, // Jangan proses data, karena ini adalah FormData
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                    },
                    success: function(response) {
                        dataBarang = [];
                        totalHarga = 0;

                        $('#tabelBarang').empty();
                        $("#divTotalHarga").addClass("d-none");
                        $("#divTabelBarang").addClass("d-none");
                        $("#lanjutTransaksiBtn").addClass("d-none");

                        $('#confirmationModal').modal('hide');
                        $('#tambahTransaksiModal').modal('hide');

                        if(button.attr("id") == "btnCetakTransaksi"){
                            printNewTransaksi(response);
                        }
                        else {
                            localStorage.setItem('savedTransaction', JSON.stringify({
                                success: true,
                                message: "Transaksi berhasil disimpan."
                            }));
                            location.reload();
                        }
                    },
                    error: function(xhr, status, error) {
                        alert('Terjadi kesalahan saat menyimpan data: ' + error);
                    }
                });
            }
        });
    }

    if(currentUrl === "/home"){
        AOS.init();

        const contextSales = $('#salesChart')[0].getContext('2d');
        const monthlyLabels = ["Minggu 1", "Minggu 2", "Minggu 3", "Minggu 4"];
        const weeklyLabels = ["Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"];

        const weeklyData = $("#salesChart").data("transaksi-week");
        const monthlyData = $("#salesChart").data("transaksi-month");
        const salesChartAdmin = $("#salesChartTitle").data("is-admin");

        $("#salesChartTitle").text("Grafik Penjualan Mingguan" + (salesChartAdmin ? "" : " Anda"));

        const salesChart = new Chart(contextSales, {
            type: "line",
            data: {
                labels: weeklyLabels,
                datasets: [{
                    data: weeklyData,
                    borderColor: '#4CAF50',
                    label: 'Penjualan',
                    borderWidth: 2.5,
                    fill: false
                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    y: {
                        beginAtZero: true,
                    }
                }
            }
        });

        $('#timeFilterGrafik').on('change', function () {
            const selectedTime = $(this).val();

            if (selectedTime === 'week') {
                salesChart.data.labels = weeklyLabels;
                salesChart.data.datasets[0].data = weeklyData;
                $("#salesChartTitle").text("Grafik Penjualan Mingguan" + (salesChartAdmin ? "" : " Anda"));
            } else if (selectedTime === 'month') {
                salesChart.data.labels = monthlyLabels;
                salesChart.data.datasets[0].data = monthlyData;
                $("#salesChartTitle").text("Grafik Penjualan Bulanan" + (salesChartAdmin ? "" : " Anda"));
            }
            salesChart.update();
        });

        const contextProduct = $('#productChart')[0].getContext('2d');
        const productsProportion = $("#productChart").data("products-proportion");

        new Chart(contextProduct, {
            type: "pie",
            data: {
                labels: productsProportion.namaBarang,
                datasets: [{
                    data: productsProportion.jumlah,
                    backgroundColor: [
                        'rgb(75, 192, 192)',
                        'rgb(255, 160, 64)',
                        'rgb(255, 99, 133)',
                        'rgb(153, 102, 255)',
                        'rgb(255, 204, 86)',
                        'rgb(253, 11, 11)'
                    ],
                    label: 'Terjual',
                    fill: false
                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
            }
        });
    }

    if(currentUrl === "/laporan"){
        if(!hasQueryParam("cari") || getQueryParam("cari") == ""){
            $("#subtitleLaporan").text("Menampilkan laporan penjualan.")
        }
        else{
            $("#subtitleLaporan").text("Menampilkan laporan penjualan dengan filter yang diterapkan.");
            const filter = getQueryParam("cari").split(",");
            filter.forEach(item => {
                const [key, value] = item.split(":");
                if(key === "tanggal"){
                    $("#dateRange").val(decodeURIComponent(value));
                }
                else if(key === "barang"){
                    $("#cariBarang").val(decodeURIComponent(value));
                }
                else if(key === "pelanggan"){
                    $("#cariPelanggan").val(decodeURIComponent(value));
                }
                else if(key === "pengguna"){
                    $("#cariPengguna").val(decodeURIComponent(value));
                }
            });
        }

        flatpickr("#dateRange", {
            locale: "id",            // Lokalisasi ke Bahasa Indonesia
            mode: "range",           // Aktifkan mode range
            allowInput: false,       // Izinkan input manual
            dateFormat: "d/m/Y",     // Format tanggal
            onChange: function(selectedDates, dateStr, instance) {
                // Memformat teks untuk menampilkan "-"
                if(selectedDates.length === 2) {
                    instance.input.value = `${instance.formatDate(selectedDates[0], "d/m/Y")} - ${instance.formatDate(selectedDates[1], "d/m/Y")}`;
                }
            },
            onReady: function () {
                // Tambahkan gaya tambahan pada elemen kalender Flatpickr
                document.querySelector(".flatpickr-calendar").classList.add("shadow", "border", "border-secondary", "rounded");
            },
        });

        $("#cariBarang").on("input", function() {
            const keyword = $(this).val();
            $.get(`api/barang?cari=${keyword}&page=0&size=10`, function(data) {
                $("#dropdownBarang").empty(); // Kosongkan dropdown sebelum menambahkan item baru

                // Tampilkan dropdown jika ada hasil
                if(keyword != "") {
                    $("#dropdownBarang").show();
                } else {
                    $("#dropdownBarang").hide();
                }

                data.forEach(barang => {
                    const highlightedName = barang.namaBarang.replace(new RegExp(keyword, 'gi'), match => `<strong>${match}</strong>`);

                    const li = $('<li></li>');
                    const a = $('<a></a>');

                    a.attr('href', '') // Tambahkan atribut href
                    a.addClass(`dropdown-item`) // Tambahkan kelas
                    a.html(`${highlightedName}`) // Isi HTML
                    a.on('click', function(event) { // Tambahkan event click
                        event.preventDefault();

                        $("#dropdownBarang").hide();
                        $('#cariBarang').val(barang.namaBarang);
                    });

                    li.append(a);
                    $("#dropdownBarang").append(li);
                });
            });
        });

        $("#cariPelanggan").on("input", function() {
            const keyword = $(this).val();
            $.get(`api/pelanggan?cari=${keyword}&page=0&size=10`, function(data) {
                $("#dropdownPelanggan").empty(); // Kosongkan dropdown sebelum menambahkan item baru

                // Tampilkan dropdown jika ada hasil
                if(keyword != "") {
                    $("#dropdownPelanggan").show();
                } else {
                    $("#dropdownPelanggan").hide();
                }

                data.forEach(pelanggan => {
                    const highlightedName = pelanggan.namaPelanggan.replace(new RegExp(keyword, 'gi'), match => `<strong>${match}</strong>`);

                    const li = $('<li></li>');
                    const a = $('<a></a>');

                    a.attr('href', '') // Tambahkan atribut href
                    a.addClass(`dropdown-item`) // Tambahkan kelas
                    a.html(`${highlightedName}`) // Isi HTML
                    a.on('click', function(event) { // Tambahkan event click
                        event.preventDefault();

                        $("#dropdownPelanggan").hide();
                        $('#cariPelanggan').val(pelanggan.namaPelanggan);
                    });

                    li.append(a);
                    $("#dropdownPelanggan").append(li);
                });
            });
        });

        $("#cariPengguna").on("input", function() {
            const keyword = $(this).val();
            $.get(`api/pengguna?cari=${keyword}&page=0&size=10`, function(data) {
                $("#dropdownPengguna").empty(); // Kosongkan dropdown sebelum menambahkan item baru

                // Tampilkan dropdown jika ada hasil
                if(keyword != "") {
                    $("#dropdownPengguna").show();
                } else {
                    $("#dropdownPengguna").hide();
                }

                data.forEach(pengguna => {
                    const highlightedName = pengguna.namaPengguna.replace(new RegExp(keyword, 'gi'), match => `<strong>${match}</strong>`);

                    const li = $('<li></li>');
                    const a = $('<a></a>');

                    a.attr('href', '') // Tambahkan atribut href
                    a.addClass(`dropdown-item`) // Tambahkan kelas
                    a.html(`${highlightedName}`) // Isi HTML
                    a.on('click', function(event) { // Tambahkan event click
                        event.preventDefault();

                        $("#dropdownPengguna").hide();
                        $('#cariPengguna').val(pengguna.namaPengguna);
                    });

                    li.append(a);
                    $("#dropdownPengguna").append(li);
                });
            });
        });

        $("#btnFilterLaporan").on('click', function(event) {
            event.preventDefault();

            const dateRange = $("#dateRange").val();
            const keywordBarang = $("#cariBarang").val();
            const keywordPengguna = $("#cariPengguna").val();
            const keywordPelanggan = $("#cariPelanggan").val();

            var cari = "tanggal:" + dateRange +
                        ",barang:" + keywordBarang +
                        ",pelanggan:" + keywordPelanggan +
                        ",pengguna:" + keywordPengguna;

            if(dateRange === "" && keywordBarang === "" && keywordPengguna === "" && keywordPelanggan === ""){
                deleteQueryParam("cari");
                deleteQueryParam("page");
                location.reload();
            }
            else{
                addQueryParam("cari", cari);
            }
        });

        const totalSales = parseFloat($("#totalSales").text());
        $("#totalSales").text("Rp. " + totalSales.toLocaleString("id-ID") + ",-");

        document.querySelectorAll('.harga-barang, .total-harga').forEach(function (element) {
            let harga = parseFloat(element.innerText);
            element.innerText = 'Rp. ' + harga.toLocaleString('id-ID');
        });

        $("#btnExportPDF").on('click', function() {
            addQueryParam("export", "pdf");
        });

        $("#btnExportExcel").on('click', function() {
            addQueryParam("export", "excel");
        });
    }
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

function hasQueryParam(param) {
    // Mendapatkan URL saat ini
    const urlParams = new URLSearchParams(window.location.search);
    // Mengembalikan true jika parameter ada
    return urlParams.has(param);
}

function addQueryParam(param, value) {
    // Mendapatkan URL saat ini
    const url = new URL(window.location);

    if(value){
        // Menambahkan atau memperbarui parameter
        url.searchParams.delete("page")
        url.searchParams.delete("size")
        url.searchParams.set(param, value);
    }
    else{
        url.searchParams.delete(param)
    }
    // Memuat ulang halaman dengan URL baru
    window.location.href = url.toString();
}

function deleteQueryParam(param) {
    // Mendapatkan URL saat ini
    const url = new URL(window.location);

    url.searchParams.delete(param)
    window.history.replaceState({}, document.title, url);
}

function getQueryParam(param) {
    // Mendapatkan query string dari URL saat ini
    const urlParams = new URLSearchParams(window.location.search);
    // Mengembalikan nilai dari parameter yang diberikan, atau null jika tidak ada
    return urlParams.get(param);
}

function printTransaksi(button){
    $(".sidebar").hide();
    $(".top-bar").hide();
    $(".content").hide();
    $("footer").hide();
    $(".nota-cetak").show();

    const transaksi = button.getAttribute('data-transaksi');
    var dataTransaksi = JSON.parse(transaksi);

    $("#kasir").text(dataTransaksi.pengguna.namaPengguna);
    $("#nomor-transaksi").text(dataTransaksi.nomorTransaksi);
    $("#tanggal-transaksi").text(moment(dataTransaksi.tanggalTransaksi, "YYYY, MM, DD").format("DD/MM/YYYY"));

    document.querySelectorAll('#nama-pelanggan').forEach(function (element) {
        element.innerText = dataTransaksi.pelanggan.namaPelanggan;
    });

    // Loop melalui data dan tambahkan setiap baris ke tabel
    const tableBody = document.getElementById('tableBarang');
    const dataBarang = dataTransaksi.barang;

    $('#tableBarang').empty();

    var total = 0;
    var index = 1;

    dataBarang.forEach(function(item) {
        // Buat elemen baris
        const row = document.createElement('tr');

        // Buat elemen sel untuk ID
        const cellId = document.createElement('td');
        cellId.textContent = index++; // Isi dengan ID
        row.appendChild(cellId); // Tambahkan sel ke baris

        const cellnamaBarang = document.createElement('td');
        cellnamaBarang.textContent = item.namaBarang; // Isi dengan ID
        row.appendChild(cellnamaBarang); // Tambahkan sel ke baris

        // Buat elemen sel untuk Nama
        const cellhargaBarang = document.createElement('td');
        cellhargaBarang.textContent = "Rp. " + item.hargaBarang.toLocaleString('id-ID');
        row.appendChild(cellhargaBarang); // Tambahkan sel ke baris

        // Buat elemen sel untuk Umur
        const celljumlah = document.createElement('td');
        celljumlah.textContent = item.jumlah; // Isi dengan Umur
        row.appendChild(celljumlah); // Tambahkan sel ke baris

        total = total + (item.hargaBarang * item.jumlah);

        const cellTotal = document.createElement('td');
        cellTotal.textContent = "Rp. " + (item.hargaBarang * item.jumlah).toLocaleString("id-ID"); // Isi dengan Umur
        row.appendChild(cellTotal);

        // Tambahkan baris ke tabel body
        tableBody.appendChild(row);
    });

    moment.locale('id');

    $("#total").text("Rp. " + total.toLocaleString("id-ID") + ",-");
    $("#tgl-transaksi").text(moment(dataTransaksi.tanggalTransaksi, "YYYY, MM, DD").format("DD MMMM YYYY"));

    const tooltipInstance = bootstrap.Tooltip.getInstance(button);
    if (tooltipInstance) {
        tooltipInstance.hide();
    }
    window.print();

    $(".sidebar").show();
    $(".top-bar").show();
    $(".content").show();
    $("footer").show();
    $(".nota-cetak").hide();
}

function printNewTransaksi(transaksi){
    $("#kasir").text(transaksi[0].pengguna.namaPengguna);
    $("#nomor-transaksi").text(transaksi[0].nomorTransaksi);
    $("#tanggal-transaksi").text(moment(transaksi[0].tanggalTransaksi, "YYYY-MM-DD").format("DD/MM/YYYY"));

    document.querySelectorAll('#nama-pelanggan').forEach(function (element) {
        element.innerText = transaksi[0].pelanggan.namaPelanggan;
    });

    // Loop melalui data dan tambahkan setiap baris ke tabel
    const tableBody = document.getElementById('tableBarang');
    $('#tableBarang').empty();

    var total = 0;
    var index = 1;

    transaksi.forEach(function(item) {
        // Buat elemen baris
        const row = document.createElement('tr');

        // Buat elemen sel untuk ID
        const cellId = document.createElement('td');
        cellId.textContent = index++; // Isi dengan ID
        row.appendChild(cellId); // Tambahkan sel ke baris

        const cellnamaBarang = document.createElement('td');
        cellnamaBarang.textContent = item.barang.namaBarang; // Isi dengan ID
        row.appendChild(cellnamaBarang); // Tambahkan sel ke baris

        // Buat elemen sel untuk Nama
        const cellhargaBarang = document.createElement('td');
        cellhargaBarang.textContent = "Rp. " + item.barang.hargaBarang.toLocaleString('id-ID');
        row.appendChild(cellhargaBarang); // Tambahkan sel ke baris

        // Buat elemen sel untuk Umur
        const celljumlah = document.createElement('td');
        celljumlah.textContent = item.jumlah; // Isi dengan Umur
        row.appendChild(celljumlah); // Tambahkan sel ke baris

        total = total + (item.barang.hargaBarang * item.jumlah);

        const cellTotal = document.createElement('td');
        cellTotal.textContent = "Rp. " + (item.barang.hargaBarang * item.jumlah).toLocaleString("id-ID"); // Isi dengan Umur
        row.appendChild(cellTotal);

        // Tambahkan baris ke tabel body
        tableBody.appendChild(row);
    });

    moment.locale('id');

    $("#total").text("Rp. " + total.toLocaleString("id-ID") + ",-");
    $("#tgl-transaksi").text(moment(transaksi[0].tanggalTransaksi, "YYYY-MM-DD").format("DD MMMM YYYY"));

    $(".sidebar").hide();
    $(".top-bar").hide();
    $(".content").hide();
    $("footer").hide();
    $(".nota-cetak").show();

    setTimeout(function(){
        window.print();
        location.reload();
    }, 500);
}