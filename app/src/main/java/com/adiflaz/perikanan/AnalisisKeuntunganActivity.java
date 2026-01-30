package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.DecimalFormat;

public class AnalisisKeuntunganActivity extends AppCompatActivity {
    
    private DecimalFormat df = new DecimalFormat("#,##0");
    private DecimalFormat dfDecimal = new DecimalFormat("#,##0.00");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis_keuntungan);
        
        // Komponen
        EditText etTotalBiaya = findViewById(R.id.et_total_biaya);
        EditText etTotalPanen = findViewById(R.id.et_total_panen);
        EditText etHargaJual = findViewById(R.id.et_harga_jual);
        EditText etModal = findViewById(R.id.et_modal);
        EditText etWaktu = findViewById(R.id.et_waktu);
        Button btnHitung = findViewById(R.id.btn_hitung);
        TextView tvHasil = findViewById(R.id.tv_hasil);
        
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungAnalisisKeuntungan();
            }
        });
    }
    
    private void hitungAnalisisKeuntungan() {
        try {
            // Ambil input
            EditText etTotalBiaya = findViewById(R.id.et_total_biaya);
            EditText etTotalPanen = findViewById(R.id.et_total_panen);
            EditText etHargaJual = findViewById(R.id.et_harga_jual);
            EditText etModal = findViewById(R.id.et_modal);
            EditText etWaktu = findViewById(R.id.et_waktu);
            TextView tvHasil = findViewById(R.id.tv_hasil);
            
            // Validasi
            if (etTotalBiaya.getText().toString().isEmpty() ||
                etTotalPanen.getText().toString().isEmpty() ||
                etHargaJual.getText().toString().isEmpty() ||
                etModal.getText().toString().isEmpty() ||
                etWaktu.getText().toString().isEmpty()) {
                
                Toast.makeText(this, "Isi semua data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            double totalBiaya = Double.parseDouble(etTotalBiaya.getText().toString());
            double totalPanen = Double.parseDouble(etTotalPanen.getText().toString());
            double hargaJual = Double.parseDouble(etHargaJual.getText().toString());
            double modal = Double.parseDouble(etModal.getText().toString());
            int waktu = Integer.parseInt(etWaktu.getText().toString()); // dalam bulan
            
            // Perhitungan
            double pendapatan = totalPanen * hargaJual;
            double keuntunganKotor = pendapatan - totalBiaya;
            double hppPerKg = totalBiaya / totalPanen;
            double marginPerKg = hargaJual - hppPerKg;
            double marginPersen = (keuntunganKotor / pendapatan) * 100;
            
            // BEP (Break Even Point)
            double bepKg = totalBiaya / hargaJual;
            double bepPersen = (bepKg / totalPanen) * 100;
            
            // ROI (Return on Investment)
            double roi = (keuntunganKotor / modal) * 100;
            double roiPerBulan = roi / waktu;
            
            // Payback Period
            double payback = modal / keuntunganKotor; // siklus produksi
            double paybackBulan = payback * waktu;
            
            // Profit per Month
            double profitPerMonth = keuntunganKotor / waktu;
            
            // Kategori ROI
            String kategoriRoi;
            String rekomendasi;
            
            if (roi < 0) {
                kategoriRoi = "MERUGI";
                rekomendasi = "Evaluasi total biaya dan harga jual. Mungkin perlu menghentikan usaha.";
            } else if (roi < 20) {
                kategoriRoi = "RENDAH";
                rekomendasi = "Cari cara menurunkan biaya atau tingkatkan harga jual.";
            } else if (roi < 50) {
                kategoriRoi = "SEDANG";
                rekomendasi = "Usaha cukup menguntungkan. Pertahankan dan tingkatkan skala.";
            } else if (roi < 100) {
                kategoriRoi = "TINGGI";
                rekomendasi = "Usaha sangat menguntungkan. Perluas usaha dan diversifikasi.";
            } else {
                kategoriRoi = "SANGAT TINGGI";
                rekomendasi = "Usaha luar biasa! Re-investasi dan kembangkan ke skala industri.";
            }
            
            // Hasil
            String hasil = String.format(
                "ANALISIS KEUANGAN BUDIDAYA\n" +
                "==========================\n\n" +
                "DATA INPUT:\n" +
                "• Total Biaya: Rp %s\n" +
                "• Total Panen: %s kg\n" +
                "• Harga Jual: Rp %s/kg\n" +
                "• Modal Awal: Rp %s\n" +
                "• Waktu Produksi: %d bulan\n\n" +
                "HASIL ANALISIS:\n\n" +
                "PENDAPATAN:\n" +
                "• Pendapatan Kotor: Rp %s\n" +
                "• Keuntungan Kotor: Rp %s\n" +
                "• Keuntungan/Bulan: Rp %s\n\n" +
                "MARGIN:\n" +
                "• HPP per kg: Rp %s\n" +
                "• Margin per kg: Rp %s\n" +
                "• Margin Persen: %s%%\n\n" +
                "BREAK EVEN POINT:\n" +
                "• BEP (kg): %s kg\n" +
                "• BEP (%%): %s%% dari produksi\n\n" +
                "RETURN ON INVESTMENT:\n" +
                "• ROI Total: %s%%\n" +
                "• ROI per Bulan: %s%%\n" +
                "• Kategori: %s\n\n" +
                "PAYBACK PERIOD:\n" +
                "• Siklus Produksi: %s kali\n" +
                "• Waktu (Bulan): %s bulan\n\n" +
                "REKOMENDASI:\n%s\n\n" +
                "TINGKAT KELAYAKAN: %s",
                
                df.format(totalBiaya), df.format(totalPanen),
                df.format(hargaJual), df.format(modal), waktu,
                
                df.format(pendapatan), df.format(keuntunganKotor),
                df.format(profitPerMonth),
                
                dfDecimal.format(hppPerKg), dfDecimal.format(marginPerKg),
                dfDecimal.format(marginPersen),
                
                dfDecimal.format(bepKg), dfDecimal.format(bepPersen),
                
                dfDecimal.format(roi), dfDecimal.format(roiPerBulan),
                kategoriRoi,
                
                dfDecimal.format(payback), dfDecimal.format(paybackBulan),
                
                rekomendasi,
                roi > 25 ? "✅ LAYAK" : "⚠️ PERLU EVALUASI"
            );
            
            tvHasil.setText(hasil);
            
        } catch (Exception e) {
            Toast.makeText(this, "Error dalam perhitungan!", Toast.LENGTH_SHORT).show();
        }
    }
}