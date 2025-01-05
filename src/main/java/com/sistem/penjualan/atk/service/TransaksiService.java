package com.sistem.penjualan.atk.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sistem.penjualan.atk.entity.Barang;
import com.sistem.penjualan.atk.entity.Pelanggan;
import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.entity.Transaksi;
import com.sistem.penjualan.atk.repository.BarangRepository;
import com.sistem.penjualan.atk.repository.PelangganRepository;
import com.sistem.penjualan.atk.repository.PenggunaRepository;
import com.sistem.penjualan.atk.repository.TransaksiRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private PelangganRepository pelangganRepository;

    @Autowired
    private PenggunaRepository penggunaRepository;

    @Autowired
    private BarangRepository barangRepository;

    public Page<Map<String, Object>> getAllTransaksi(Pageable pageable) {
        return getGroupedMap(transaksiRepository.findAll(), pageable);
    }

    public Optional<Transaksi> getTransaksiById(UUID idTransaksi) {
        return transaksiRepository.findById(idTransaksi);
    }

    @SuppressWarnings("unchecked")
    public List<Transaksi> saveTransaksi(Map<String, Object> transaksi) {
        List<Transaksi> transaksiList = new ArrayList<>();

        UUID idPengguna = UUID.fromString(transaksi.get("idPengguna").toString());
        UUID idPelanggan = UUID.fromString(transaksi.get("idPelanggan").toString());
        List<Map<String, Object>> barangList = (List<Map<String, Object>>) transaksi.get("barang");

        Pelanggan pelanggan = pelangganRepository.findById(idPelanggan).get();
        Pengguna pengguna = penggunaRepository.findById(idPengguna).get();
        String nomorTransaksi = createNomorTransaksi();
        LocalDate tanggalTransaksi = LocalDate.now();

        for(Map<String, Object> barang : barangList) {
            Transaksi newTransaksi = new Transaksi();
            UUID idBarang = UUID.fromString(barang.get("idBarang").toString());
            Barang barangEntity = barangRepository.findById(idBarang).get();

            int jumlah = Integer.parseInt(barang.get("jumlah").toString());

            barangEntity.setStokBarang(barangEntity.getStokBarang() - jumlah);
            barangRepository.save(barangEntity);

            newTransaksi.setTanggalTransaksi(tanggalTransaksi);
            newTransaksi.setNomorTransaksi(nomorTransaksi);
            newTransaksi.setPelanggan(pelanggan);
            newTransaksi.setPengguna(pengguna);

            newTransaksi.setJumlah(jumlah);
            newTransaksi.setBarang(barangEntity);
            newTransaksi.setCreatedAt(LocalDateTime.now());
            newTransaksi.setUpdateAt(LocalDateTime.now());

            newTransaksi = transaksiRepository.save(newTransaksi);
            transaksiList.add(newTransaksi);
        }
        return transaksiList;
    }

    public Transaksi updateTransaksi(Transaksi transaksi) {
        return transaksiRepository.findById(transaksi.getIdTransaksi()).map(data -> {
            Optional.ofNullable(transaksi.getTanggalTransaksi()).ifPresent(data::setTanggalTransaksi);
            Optional.ofNullable(transaksi.getPelanggan()).ifPresent(data::setPelanggan);
            Optional.ofNullable(transaksi.getBarang()).ifPresent(data::setBarang);
            Optional.ofNullable(transaksi.getJumlah()).ifPresent(data::setJumlah);
            data.setUpdateAt(LocalDateTime.now());

            return transaksiRepository.save(data);
        }).orElse(new Transaksi());
    }

    public void deleteTransaksi(UUID idTransaksi) {
        transaksiRepository.deleteById(idTransaksi);
    }

    public Page<Map<String, Object>> searchTransaksi(String cari, Pageable pageable) {
        Specification<Transaksi> specification = (root, query, criterial) -> {
            var predicates = criterial.conjunction();

            if(!cari.isEmpty()) {
                predicates = criterial.and(predicates, criterial.like(criterial.lower(root.join("pelanggan").get("namaPelanggan")), "%" + cari.toLowerCase() + "%"));
                predicates = criterial.or(predicates, criterial.like(criterial.lower(root.get("nomorTransaksi")), "%" + cari.toLowerCase() + "%"));
                predicates = criterial.or(predicates, criterial.like(criterial.function("DATE_FORMAT", String.class, root.get("tanggalTransaksi"), criterial.literal("%Y-%m-%d")), "%" + cari + "%"));
                try {
                    predicates = criterial.or(predicates, criterial.equal(root.get("jumlah"), Integer.parseInt(cari)));
                } catch (NumberFormatException e) {}
            }
            return predicates;
        };

        return getGroupedMap(transaksiRepository.findAll(specification), pageable);
    }

    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> getGroupedMap(List<Transaksi> listTransaksi, Pageable pageable){
        Map<String, Map<String, Object>> groupedMap = new LinkedHashMap<>();
        List<Transaksi> sortedTransaksi = listTransaksi.stream().sorted(Comparator.comparing(Transaksi::getCreatedAt).reversed()).collect(Collectors.toList());

        for(Transaksi transaksi : sortedTransaksi) {
            String nomorTransaksi = transaksi.getNomorTransaksi();
            Map<String, Object> transaksiData = new HashMap<>();

            if(!groupedMap.containsKey(nomorTransaksi)) {
                transaksiData.put("idTransaksi", transaksi.getIdTransaksi());
                transaksiData.put("nomorTransaksi", transaksi.getNomorTransaksi());
                transaksiData.put("tanggalTransaksi", transaksi.getTanggalTransaksi());

                Map<String, Object> pelangganData = new HashMap<>();
                pelangganData.put("idPelanggan", transaksi.getPelanggan().getIdPelanggan());
                pelangganData.put("namaPelanggan", transaksi.getPelanggan().getNamaPelanggan());
                transaksiData.put("pelanggan", pelangganData);

                Map<String, Object> penggunaData = new HashMap<>();
                penggunaData.put("idPengguna", transaksi.getPengguna().getIdPengguna());
                penggunaData.put("namaPengguna", transaksi.getPengguna().getNamaPengguna());
                transaksiData.put("pengguna", penggunaData);

                List<Map<String, Object>> listBarang = new ArrayList<>();
                transaksiData.put("barang", listBarang);
                groupedMap.put(nomorTransaksi, transaksiData);
            }

            Map<String, Object> barangData = new HashMap<>();
            barangData.put("hargaBarang", transaksi.getBarang().getHargaBarang());
            barangData.put("namaBarang", transaksi.getBarang().getNamaBarang());
            barangData.put("idBarang", transaksi.getBarang().getIdBarang());
            barangData.put("jumlah", transaksi.getJumlah());

            ((List<Map<String, Object>>)groupedMap.get(nomorTransaksi).get("barang")).add(barangData);
        }

        List<Map<String, Object>> allGroupedTransaksi = new ArrayList<>(groupedMap.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allGroupedTransaksi.size());

        if (start >= allGroupedTransaksi.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, allGroupedTransaksi.size());
        }

        List<Map<String, Object>> pagedTransaksi = allGroupedTransaksi.subList(start, end);
        Page<Map<String, Object>> newGrouPage = new PageImpl<>(pagedTransaksi, pageable, allGroupedTransaksi.size());

        return newGrouPage;
    }

    public List<Integer> getTransaksiByPenggunaAndCurrentMonth(UUID idPengguna) {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<Transaksi> transaksiList = transaksiRepository.findByPengguna_IdPenggunaAndTanggalTransaksiBetween(idPengguna, startDate, endDate);

        List<Integer> transaksiPerTanggal = new ArrayList<>();
        LocalDate currentDate = startDate;

        while(!currentDate.isAfter(endDate)) {
            int totalJumlah = 0;

            for(Transaksi transaksi : transaksiList) {
                if(transaksi.getTanggalTransaksi().isEqual(currentDate)) {
                    totalJumlah += transaksi.getJumlah();
                }
            }
            transaksiPerTanggal.add(totalJumlah);
            currentDate = currentDate.plusDays(1);
        }

        return transaksiPerTanggal;
    }

    @Transactional
    public String createNomorTransaksi() {
        LocalDate today = LocalDate.now() ;
        List<Transaksi> transaksiList = transaksiRepository.findByTanggalTransaksi(LocalDate.now());
        long count = transaksiList.stream().map(Transaksi::getNomorTransaksi).distinct().count() + 1;

        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String nomorTransaksi = String.format("TRX%s%03d", formattedDate, count);

        return nomorTransaksi;
    }

    public Pengguna getPenggunaByLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username     = authentication.getName();
            Pengguna pengguna   = penggunaRepository.findByUsername(username).get();

            return pengguna;
        }
        return new Pengguna();
    }

    public Long getCountTotalTransaksiToday() {
        long totalTransaksiToday = 0;
        Pengguna pengguna = getPenggunaByLogin();
        List<Transaksi> transaksiList = transaksiRepository.findByTanggalTransaksi(LocalDate.now());

        if(pengguna.getLevel().equals("ADMIN")) {
            totalTransaksiToday = transaksiList.stream().map(Transaksi::getNomorTransaksi).distinct().count();
        }
        else {
            totalTransaksiToday = transaksiList.stream().filter(transaksi -> transaksi.getPengguna().getIdPengguna()
                .equals(pengguna.getIdPengguna())).map(Transaksi::getNomorTransaksi).distinct().count();
        }
        return totalTransaksiToday;
    }

    public List<Integer> getTransaksiByCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today;

        LocalDate currentDate = startOfWeek;
        Pengguna pengguna = getPenggunaByLogin();
        List<Transaksi> transaksiList = new ArrayList<>();
        List<Integer> transaksiPerHari = new ArrayList<>();

        if(pengguna.getLevel().equals("ADMIN")){
            transaksiList = transaksiRepository.findByTanggalTransaksiBetween(startOfWeek, endOfWeek);
        }
        else {
            transaksiList = transaksiRepository.findByPengguna_IdPenggunaAndTanggalTransaksiBetween(pengguna.getIdPengguna(), startOfWeek, endOfWeek);
        }

        while(!currentDate.isAfter(endOfWeek)) {
            int totalJumlah = 0;

            for(Transaksi transaksi : transaksiList) {
                if(transaksi.getTanggalTransaksi().isEqual(currentDate)) {
                    totalJumlah += transaksi.getJumlah();
                }
            }
            transaksiPerHari.add(totalJumlah);
            currentDate = currentDate.plusDays(1);
        }
        return transaksiPerHari;
    }

    public List<Integer> getTransaksiByCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        // Tentukan awal setiap minggu dalam bulan ini
        List<LocalDate> startOfWeeks = new ArrayList<>();
        for(LocalDate date = startOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); !date.isAfter(endOfMonth); date = date.plusWeeks(1)) {
            startOfWeeks.add(date);
        }

        Pengguna pengguna = getPenggunaByLogin();
        List<Transaksi> transaksiList = new ArrayList<>();
        List<Integer> transaksiPerBulan = new ArrayList<>();

        if(pengguna.getLevel().equals("ADMIN")){
            transaksiList = transaksiRepository.findByTanggalTransaksiBetween(startOfMonth, endOfMonth);
        }
        else {
            transaksiList = transaksiRepository.findByPengguna_IdPenggunaAndTanggalTransaksiBetween(pengguna.getIdPengguna(), startOfMonth, endOfMonth);
        }

        for(int i = 0; i < startOfWeeks.size(); i++) {
            LocalDate startOfWeek = startOfWeeks.get(i);
            LocalDate[] endOfWeek = { startOfWeek.plusDays(6).isAfter(endOfMonth) ? endOfMonth : startOfWeek.plusDays(6) };

            if(endOfWeek[0].isAfter(today)){
                endOfWeek[0] = today; // Batasi hingga hari ini
            }

            int totalMinggu = transaksiList.stream()
                    .filter(t -> !t.getTanggalTransaksi().isBefore(startOfWeek) && !t.getTanggalTransaksi().isAfter(endOfWeek[0]))
                    .mapToInt(Transaksi::getJumlah)
                    .sum();

            transaksiPerBulan.add(totalMinggu);

            if(endOfWeek[0].isEqual(today)) {
                break; // Berhenti jika sudah mencapai minggu berjalan
            }
        }
        return transaksiPerBulan;
    }

    public Map<String, Object> getProductSalesProportion() {
        List<Transaksi> transaksiList = transaksiRepository.findByOrderByBarang_NamaBarangAsc();
        Map<String, Integer> jumlahBarang = new HashMap<>();

        // Menghitung jumlah barang terjual
        for(Transaksi transaksi : transaksiList) {
            String barang = transaksi.getBarang().getNamaBarang();
            jumlahBarang.put(barang, jumlahBarang.getOrDefault(barang, 0) + transaksi.getJumlah());
        }

        // Mengurutkan barang berdasarkan jumlah terjual
        List<Map.Entry<String, Integer>> sortedEntries = jumlahBarang.entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Mengurutkan dari terbesar ke terkecil
            .collect(Collectors.toList());

        List<String> namaBarang = new ArrayList<>();
        List<Integer> jumlah = new ArrayList<>();
        int totalOther = 0;

        // Memisahkan 5 barang terlaris dan menghitung total barang "Lainnya"
        for(int i = 0; i < sortedEntries.size(); i++) {
            if(i < 5) {
                namaBarang.add(sortedEntries.get(i).getKey());
                jumlah.add(sortedEntries.get(i).getValue());
            } else {
                totalOther += sortedEntries.get(i).getValue();
            }
        }

        // Tambahkan kategori "Lainnya" jika ada
        if(totalOther > 0) {
            namaBarang.add("Lainnya");
            jumlah.add(totalOther);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("namaBarang", namaBarang);
        data.put("jumlah", jumlah);
        return data;
    }

    public Page<Transaksi> getAllLaporanTransaksi(Pageable pageable){
        if(pageable == null) {
            pageable = Pageable.unpaged();
        }
        return transaksiRepository.findAll(pageable);
    }

    public Double getCountTotalSales(String cari, List<Transaksi> transaksiList) {
        if(cari.isEmpty()) {
            return transaksiRepository.findAll().stream()
                .mapToDouble(transaksi -> transaksi.getJumlah() * transaksi.getBarang().getHargaBarang()).sum();
        }
        else {
            return transaksiList.stream()
                .mapToDouble(transaksi -> transaksi.getJumlah() * transaksi.getBarang().getHargaBarang()).sum();
        }
    }

    public Page<Transaksi> searchLaporanTransaksi(LocalDate startDate, LocalDate endDate,String namaBarang, String namaPelanggan, String namaPengguna, Pageable pageable) {
        if(pageable == null) {
            pageable = Pageable.unpaged();
        }

        if(startDate == null && endDate == null) {
            return transaksiRepository.findByBarang_NamaBarangContainingIgnoreCaseAndPelanggan_NamaPelangganContainingIgnoreCaseAndPengguna_NamaPenggunaContainingIgnoreCase(
                namaBarang, namaPelanggan, namaPengguna, pageable);
        }
        else {
            return transaksiRepository.findByTanggalTransaksiBetweenAndBarang_NamaBarangContainingIgnoreCaseAndPelanggan_NamaPelangganContainingIgnoreCaseAndPengguna_NamaPenggunaContainingIgnoreCase(
                    startDate, endDate, namaBarang, namaPelanggan, namaPengguna, pageable);
        }
    }

    public void generateExport(String export, HttpServletResponse response, List<Transaksi> data) {
        try{
            String title = "Laporan Penjualan Alat Tulis Kantor";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String[] headerValue = {"No", "Nomor Transaksi", "Tanggal Transaksi", "Nama Pelanggan", "Nama Barang", "Harga", "Jumlah", "Total", "Kasir"};

            int numberData = 0;
            int totalJumlahBarang = 0;
            double totalHargaBarang = 0;
            double totalKeseluruhanHarga = 0;

            if(export.equals("excel")){
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=" + title + ".xlsx");

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Laporan");

                Font titleFont = workbook.createFont();;
                titleFont.setBold(true);
                titleFont.setItalic(false);
                titleFont.setFontName("Arial");
                titleFont.setFontHeightInPoints((short)17);
                titleFont.setColor(IndexedColors.BLACK.getIndex());

                CellStyle titleStyle = workbook.createCellStyle();
                titleStyle.setAlignment(HorizontalAlignment.CENTER);
                titleStyle.setFont(titleFont);

                Row titleRow = sheet.createRow(2);
                Cell titleCell = titleRow.createCell(1);
                titleCell.setCellValue(title);
                titleCell.setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 9));

                Font dateFont = workbook.createFont();
                dateFont.setBold(false);
                dateFont.setItalic(false);
                dateFont.setFontName("Arial");
                dateFont.setFontHeightInPoints((short)7);
                dateFont.setColor(IndexedColors.BLACK.getIndex());

                CellStyle dateStyle = workbook.createCellStyle();
                dateStyle.setAlignment(HorizontalAlignment.RIGHT);
                dateStyle.setFont(dateFont);

                Row dateRow = sheet.createRow(5);
                Cell dateCell = dateRow.createCell(9);
                dateCell.setCellStyle(dateStyle);
                dateCell.setCellValue("Export: " + currentDate);

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setItalic(false);
                headerFont.setFontName("Arial");
                headerFont.setFontHeightInPoints((short)13);
                headerFont.setColor(IndexedColors.BLACK.getIndex());

                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);

                Row headerRow = sheet.createRow(6);
                for(int i = 0; i < headerValue.length; i++) {
                    Cell headerCell = headerRow.createCell(i+1);
                    headerCell.setCellValue(headerValue[i]);
                    headerCell.setCellStyle(headerStyle);
                    sheet.autoSizeColumn(i);
                }

                Font dataFont = workbook.createFont();
                dataFont.setBold(false);
                dataFont.setItalic(false);
                dataFont.setFontName("Arial");
                dataFont.setFontHeightInPoints((short)11);
                dataFont.setColor(IndexedColors.BLACK.getIndex());

                int dataRowNumber = 7;
                for(Transaksi transaksi : data) {
                    Row dataRow = sheet.createRow(dataRowNumber++);
                    numberData++;

                    for(int i = 1; i <= headerValue.length; i++){
                        CellStyle dataStyle = workbook.createCellStyle();
                        dataStyle.setBorderBottom(BorderStyle.THIN);
                        dataStyle.setBorderRight(BorderStyle.THIN);
                        dataStyle.setBorderLeft(BorderStyle.THIN);
                        dataStyle.setBorderTop(BorderStyle.THIN);

                        dataStyle.setFont(dataFont);
                        Cell dataCell = dataRow.createCell(i);

                        if(i == 1){
                            dataStyle.setAlignment(HorizontalAlignment.CENTER);
                            dataCell.setCellValue(numberData);
                            dataCell.setCellStyle(dataStyle);
                        }
                        else if(i == 2){
                            dataStyle.setAlignment(HorizontalAlignment.LEFT);
                            dataCell.setCellValue(transaksi.getNomorTransaksi());
                            dataCell.setCellStyle(dataStyle);
                        }
                        else if(i == 3){
                            dataCell.setCellValue(transaksi.getTanggalTransaksi().format(dateFormatter));
                            dataStyle.setAlignment(HorizontalAlignment.LEFT);
                            dataCell.setCellStyle(dataStyle);
                        }
                        else if(i == 4){
                            dataCell.setCellValue(transaksi.getPelanggan().getNamaPelanggan());
                            dataStyle.setAlignment(HorizontalAlignment.LEFT);
                            dataCell.setCellStyle(dataStyle);
                        }
                        else if(i == 5){
                            dataCell.setCellValue(transaksi.getBarang().getNamaBarang());
                            dataStyle.setAlignment(HorizontalAlignment.LEFT);
                            dataCell.setCellStyle(dataStyle);
                        }
                        else if(i == 6){
                            String formattedPrice = numberFormat.format(transaksi.getBarang().getHargaBarang());
                            dataCell.setCellValue(formattedPrice.replace("Rp", "Rp."));
                            dataStyle.setAlignment(HorizontalAlignment.RIGHT);
                            dataCell.setCellStyle(dataStyle);

                            totalHargaBarang += transaksi.getBarang().getHargaBarang();
                        }
                        else if(i == 7){
                            dataStyle.setAlignment(HorizontalAlignment.CENTER);
                            dataCell.setCellValue(transaksi.getJumlah());
                            dataCell.setCellStyle(dataStyle);

                            totalJumlahBarang += transaksi.getJumlah();
                        }
                        else if(i == 8){
                            double total = transaksi.getBarang().getHargaBarang() * transaksi.getJumlah();
                            String formattedPrice = numberFormat.format(total);

                            dataCell.setCellValue(formattedPrice.replace("Rp", "Rp."));
                            dataStyle.setAlignment(HorizontalAlignment.RIGHT);
                            dataCell.setCellStyle(dataStyle);

                            totalKeseluruhanHarga += total;
                        }
                        else if(i == 9){
                            dataCell.setCellValue(transaksi.getPengguna().getNamaPengguna());
                            dataStyle.setAlignment(HorizontalAlignment.LEFT);
                            dataCell.setCellStyle(dataStyle);
                        }
                        sheet.autoSizeColumn(i);
                    }
                }

                CellStyle totalStyle = workbook.createCellStyle();
                totalStyle.setAlignment(HorizontalAlignment.CENTER);
                totalStyle.setFont(headerFont);

                totalStyle.setBorderBottom(BorderStyle.THIN);
                totalStyle.setBorderRight(BorderStyle.THIN);
                totalStyle.setBorderLeft(BorderStyle.THIN);
                totalStyle.setBorderTop(BorderStyle.THIN);

                String formattedTotalHargaBarang = numberFormat.format(totalHargaBarang).replace("Rp", "Rp.");
                String formattedKeseluruhanHarga = numberFormat.format(totalKeseluruhanHarga).replace("Rp", "Rp.");

                Row totalRow = sheet.createRow(7 + data.size());
                Cell totalHargaBarangCell = totalRow.createCell(6);
                Cell totalJumlahBarangCell = totalRow.createCell(7);
                Cell totalKeseluruhanHargaCell = totalRow.createCell(8);
                Cell textTotalCell = totalRow.createCell(9);

                totalHargaBarangCell.setCellValue(formattedTotalHargaBarang);
                totalHargaBarangCell.setCellStyle(totalStyle);
                sheet.autoSizeColumn(6);

                totalJumlahBarangCell.setCellValue(totalJumlahBarang);
                totalJumlahBarangCell.setCellStyle(totalStyle);
                sheet.autoSizeColumn(7);

                totalKeseluruhanHargaCell.setCellValue(formattedKeseluruhanHarga);
                totalKeseluruhanHargaCell.setCellStyle(totalStyle);
                sheet.autoSizeColumn(8);

                textTotalCell.setCellValue("TOTAL");
                textTotalCell.setCellStyle(totalStyle);
                sheet.autoSizeColumn(9);

                workbook.write(response.getOutputStream());
                workbook.close();
            }
            else if(export.equals("pdf")){
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + title + ".pdf");

                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                // Tambahkan judul
                com.itextpdf.text.Font titleFontPdf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17, BaseColor.BLACK);
                Paragraph titleParagraph = new Paragraph(title, titleFontPdf);
                titleParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(titleParagraph);

                // Tambahkan tanggal
                com.itextpdf.text.Font dateFontPdf = FontFactory.getFont(FontFactory.HELVETICA, 7, BaseColor.BLACK);
                Paragraph dateParagraph = new Paragraph("Export: " + currentDate, dateFontPdf);
                dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                dateParagraph.setSpacingBefore(30);
                document.add(dateParagraph);

                // Tambahkan tabel
                float[] columnWidth = {0.5f, 2f, 2f, 2f, 2f, 2f, 1f, 2f, 2f};
                PdfPTable table = new PdfPTable(headerValue.length);
                table.setWidthPercentage(100);
                table.setWidths(columnWidth);
                table.setSpacingBefore(5);
                table.setSpacingAfter(5);

                // Tambahkan header tabel
                com.itextpdf.text.Font headerFontPdf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                for(String header : headerValue) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFontPdf));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    headerCell.setPadding(3);
                    table.addCell(headerCell);
                }

                // Tambahkan data tabel
                com.itextpdf.text.Font dataFontPdf = FontFactory.getFont(FontFactory.HELVETICA, 9);
                for(Transaksi transaksi : data) {
                    numberData++;

                    PdfPCell numberPdfPCell = new PdfPCell(new Phrase(String.valueOf(numberData), dataFontPdf));
                    numberPdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    numberPdfPCell.setPadding(3);
                    table.addCell(numberPdfPCell);

                    PdfPCell nomorTransaksipdfPCell = new PdfPCell(new Phrase(transaksi.getNomorTransaksi(), dataFontPdf));
                    nomorTransaksipdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    nomorTransaksipdfPCell.setPadding(3);
                    table.addCell(nomorTransaksipdfPCell);

                    PdfPCell tanggalTransaksipdfPCell = new PdfPCell(new Phrase(transaksi.getTanggalTransaksi().format(dateFormatter), dataFontPdf));
                    tanggalTransaksipdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tanggalTransaksipdfPCell.setPadding(3);
                    table.addCell(tanggalTransaksipdfPCell);

                    PdfPCell namaPelangganpdfPCell = new PdfPCell(new Phrase(transaksi.getPelanggan().getNamaPelanggan(), dataFontPdf));
                    namaPelangganpdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    namaPelangganpdfPCell.setPadding(3);
                    table.addCell(namaPelangganpdfPCell);

                    PdfPCell namaBarangpdfPCell = new PdfPCell(new Phrase(transaksi.getBarang().getNamaBarang(), dataFontPdf));
                    namaBarangpdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    namaBarangpdfPCell.setPadding(3);
                    table.addCell(namaBarangpdfPCell);

                    String formattedPrice = numberFormat.format(transaksi.getBarang().getHargaBarang()).replace("Rp", "Rp.");
                    totalHargaBarang += transaksi.getBarang().getHargaBarang();

                    PdfPCell hargaBarangpdfPCell = new PdfPCell(new Phrase(formattedPrice, dataFontPdf));
                    hargaBarangpdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    hargaBarangpdfPCell.setPadding(3);
                    table.addCell(hargaBarangpdfPCell);

                    totalJumlahBarang += transaksi.getJumlah();

                    PdfPCell jumlahpdfPCell = new PdfPCell(new Phrase(String.valueOf(transaksi.getJumlah()), dataFontPdf));
                    jumlahpdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    jumlahpdfPCell.setPadding(3);
                    table.addCell(jumlahpdfPCell);

                    double total = transaksi.getBarang().getHargaBarang() * transaksi.getJumlah();
                    String formattedTotal = numberFormat.format(total);
                    totalKeseluruhanHarga += total;

                    PdfPCell totalpdfPCell = new PdfPCell(new Phrase(formattedTotal.replace("Rp", "Rp."), dataFontPdf));
                    totalpdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    totalpdfPCell.setPadding(3);
                    table.addCell(totalpdfPCell);

                    PdfPCell kasirpdfPCell = new PdfPCell(new Phrase(transaksi.getPengguna().getNamaPengguna(), dataFontPdf));
                    kasirpdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    kasirpdfPCell.setPadding(3);
                    table.addCell(kasirpdfPCell);
                }

                String formattedTotalHargaBarang = numberFormat.format(totalHargaBarang).replace("Rp", "Rp.");
                String formattedKeseluruhanHarga = numberFormat.format(totalKeseluruhanHarga).replace("Rp", "Rp.");

                // Tambahkan total
                PdfPCell blankCell = new PdfPCell();
                blankCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                blankCell.setColspan(5);
                blankCell.setBorder(0);
                table.addCell(blankCell);

                PdfPCell totalHargaBarangCell = new PdfPCell(new Phrase(formattedTotalHargaBarang, headerFontPdf));
                totalHargaBarangCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalHargaBarangCell);

                PdfPCell totalJumlahBarangCell = new PdfPCell(new Phrase(String.valueOf(totalJumlahBarang), headerFontPdf));
                totalJumlahBarangCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(totalJumlahBarangCell);

                PdfPCell keseluruhanHargaCell = new PdfPCell(new Phrase(formattedKeseluruhanHarga, headerFontPdf));
                keseluruhanHargaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(keseluruhanHargaCell);

                PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL", headerFontPdf));
                totalLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(totalLabelCell);

                document.add(table);
                document.close();
            }
        }
        catch(IOException | DocumentException exception){
            exception.printStackTrace();
        }
    }
}