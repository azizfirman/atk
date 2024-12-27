$(document).ready(function() {
    const currentUrl = window.location.pathname;

    // Scanning all menu links
    $('.menu a').each(function() {
        if($(this).attr('href') === currentUrl) {
            $(this).addClass('active');
        }
    });

    // Show profile card
    $('.profile').click(function() {
        $('#profile-card').toggle();
    });

    // Logout
    $('#logout').click(function(event) {
        event.preventDefault();
        $.post({
            url: '/logout',
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', $('input[name="_csrf"]').val());
            },
            success: function() {
                window.location.href = '/';
            }
        });
    });

    // Add item popup functionality
    $('#addBarang').click(function() {
        $('#openAddBarangPopup')[0].reset();
        $('#openAddBarangPopup').show();
        $('.overlay').show();
    });

    // Close add item popup when clicking on close button or overlay
    $('#closeAddBarangPopup, .overlay').click(function() {
        $('#openAddBarangPopup').hide();
        $('.overlay').hide();
    });

    // Update file name display when a file is selected
    $('#photoBarang').change(function () {
        const fileName = $(this)[0].files[0]?.name;
        $('.file-name').text(fileName);
    });

    $('#simpanBarang').click(function (event) {
        event.preventDefault();

        const namaBarang    = $('#namaBarang').val();
        const hargaBarang   = $('#hargaBarang').val();
        const stokBarang    = $('#stokBarang').val();

        // const namaBarang = $('#namaBarang').val(); // String
        // const hargaBarang = parseFloat($('#hargaBarang').val()); // Pastikan ini menjadi float
        // const stokBarang = parseInt($('#stokBarang').val());
        const photoBarang   = $('#photoBarang')[0].files[0];

        // const formBarang = new FormData();
        // const formData = new FormData();

        // formBarang.append('namaBarang', namaBarang);
        // formBarang.append('hargaBarang', hargaBarang);
        // formBarang.append('stokBarang', stokBarang);

        // formData.append("barang", formBarang);
        // formData.append("photoBarang", photoBarang);

        // $.ajax({
        //     url: '/api/barang',
        //     type: 'POST',
        //     data: formData,
        //     contentType: false,
        //     processData: false,
        //     beforeSend: function(xhr) {
        //         xhr.setRequestHeader('X-CSRF-TOKEN', $('input[name="_csrf"]').val());
        //     },
        //     success: function(response) {
        //         alert('Barang berhasil disimpan!' + response);
        //         $('#openAddBarangPopup')[0].reset(); // Reset form jika diperlukan
        //     },
        //     error: function(xhr, status, error) {
        //         alert('Terjadi kesalahan saat menyimpan barang: ' + error);
        //     }
        // });

        const barang = {
            namaBarang: namaBarang,
            hargaBarang: hargaBarang,
            stokBarang: stokBarang
        };

        // Menggunakan FormData untuk mengirim file dan data
        const formData = new FormData();
        formData.append('namaBarang', namaBarang); // Menambahkan objek barang
        formData.append('hargaBarang', hargaBarang); // Menambahkan objek barang
        formData.append('stokBarang', stokBarang); // Menambahkan objek barang
        formData.append('photoBarang', photoBarang); // Menambahkan file

        $.ajax({
            url: '/api/barang', // Endpoint API
            type: 'POST',
            data: formData,
            contentType: false, // Jangan set contentType, jQuery akan menentukannya
            processData: false, // Jangan proses data, karena ini adalah FormData
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', $('input[name="_csrf"]').val());
            },
            success: function(response) {
                alert('Barang berhasil disimpan!');
                $('#openAddBarangPopup')[0].reset(); // Reset form jika diperlukan
            },
            error: function(xhr, status, error) {
                alert('Terjadi kesalahan saat menyimpan barang: ' + error);
            }
        });
    })






    // Add Barang
    // $(document).ready(function () {
    //     $('#simpanBarang').click(function (e) {
    //         e.preventDefault();

    //         const namaBarang    = $('#namaBarang').val();
    //         const hargaBarang   = $('#hargaBarang').val();
    //         const stokBarang    = $('#stokBarang').val();
    //         const photoBarang   = $('#photoBarang')[0].files[0];

    //         alert(photoBarang)

    //         // if (!namaBarang || !hargaBarang || !stokBarang || !photoBarang) {
    //         //     alert('Semua field harus diisi!');
    //         //     return;
    //         // }

    //         // // Buat objek FormData untuk mengirim file dan data lain
    //         // const formData = new FormData();
    //         // formData.append('namaBarang', namaBarang);
    //         // formData.append('hargaBarang', hargaBarang);
    //         // formData.append('stokBarang', stokBarang);
    //         // formData.append('photoBarang', photoBarang);

    //         // // Kirim data ke API
    //         // $.ajax({
    //         //     url: '/api/barang',
    //         //     type: 'POST',
    //         //     data: formData,
    //         //     processData: false, // Jangan ubah data menjadi query string
    //         //     contentType: false, // Jangan set tipe konten secara otomatis
    //         //     success: function (response) {
    //         //         alert('Data berhasil disimpan!');
    //         //         console.log(response); // Log response untuk debugging
    //         //         $('#openAddBarangPopup')[0].reset(); // Reset form setelah sukses
    //         //     },
    //         //     error: function (xhr, status, error) {
    //         //         alert('Terjadi kesalahan: ' + error);
    //         //         console.error(xhr.responseText); // Debug error
    //         //     }
    //         // });
    //     });
    // });

});