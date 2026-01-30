package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import java.util.HashMap;

public class DatabaseActivity extends AppCompatActivity {
    
    private HashMap<String, String[]> ikanDetail = new HashMap<>();
    private HashMap<String, String[]> kolamDetail = new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        
        initDatabase();
        
        // Komponen
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();
        
        // Tab 1: Database Ikan
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Ikan");
        tab1.setIndicator("JENIS IKAN");
        tab1.setContent(R.id.tab_ikan);
        tabHost.addTab(tab1);
        
        // Tab 2: Database Kolam
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Kolam");
        tab2.setIndicator("JENIS KOLAM");
        tab2.setContent(R.id.tab_kolam);
        tabHost.addTab(tab2);
        
        // Tab 3: Database Pakan
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Pakan");
        tab3.setIndicator("JENIS PAKAN");
        tab3.setContent(R.id.tab_pakan);
        tabHost.addTab(tab3);
        
        // Setup ListView Ikan
        ListView listIkan = findViewById(R.id.list_ikan);
        String[] ikanItems = {
            "1. LELE (Clarias sp.)",
            "2. NILA (Oreochromis niloticus)", 
            "3. GURAME (Osphronemus goramy)",
            "4. MAS (Cyprinus carpio)",
            "5. BAWAL (Colossoma macropomum)",
            "6. PATIN (Pangasius sp.)"
        };
        
        ArrayAdapter<String> adapterIkan = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, ikanItems);
        listIkan.setAdapter(adapterIkan);
        
        // Detail ikan
        TextView tvDetailIkan = findViewById(R.id.tv_detail_ikan);
        listIkan.setOnItemClickListener((parent, view, position, id) -> {
            String[] ikanKeys = {"Lele", "Nila", "Gurame", "Mas", "Bawal", "Patin"};
            String detail = getIkanDetail(ikanKeys[position]);
            tvDetailIkan.setText(detail);
        });
        
        // Setup ListView Kolam
        ListView listKolam = findViewById(R.id.list_kolam);
        String[] kolamItems = {
            "1. KOLAM BETON",
            "2. KOLAM TERPAL", 
            "3. KOLAM TANAH",
            "4. KOLAM KARET",
            "5. KOLAM FIBERGLASS",
            "6. SISTEM AKUAPONIK"
        };
        
        ArrayAdapter<String> adapterKolam = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, kolamItems);
        listKolam.setAdapter(adapterKolam);
        
        // Detail kolam
        TextView tvDetailKolam = findViewById(R.id.tv_detail_kolam);
        listKolam.setOnItemClickListener((parent, view, position, id) -> {
            String[] kolamKeys = {"Beton", "Terpal", "Tanah", "Karet", "Fiberglass", "Akuaponik"};
            String detail = getKolamDetail(kolamKeys[position]);
            tvDetailKolam.setText(detail);
        });
        
        // Setup ListView Pakan
        ListView listPakan = findViewById(R.id.list_pakan);
        String[] pakanItems = {
            "1. PELET 28% PROTEIN (Pembesaran)",
            "2. PELET 32% PROTEIN (Juvenil)", 
            "3. PELET 35% PROTEIN (Patin/Bawal)",
            "4. PELET 40% PROTEIN (Larva/Induk)",
            "5. PAKAN BUATAN SENDIRI",
            "6. PAKAN ALAMI (Plankton/Cacing)"
        };
        
        ArrayAdapter<String> adapterPakan = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, pakanItems);
        listPakan.setAdapter(adapterPakan);
        
        // Detail pakan
        TextView tvDetailPakan = findViewById(R.id.tv_detail_pakan);
        listPakan.setOnItemClickListener((parent, view, position, id) -> {
            String[] pakanKeys = {"Pelet28", "Pelet32", "Pelet35", "Pelet40", "Buatan", "Alami"};
            String detail = getPakanDetail(pakanKeys[position]);
            tvDetailPakan.setText(detail);
        });
    }
    
    private void initDatabase() {
        // Detail ikan: {suhu_optimal, ph_optimal, oksigen_min, padat_tebar, panen_hari}
        ikanDetail.put("Lele", new String[]{
            "Suhu Optimal: 26-30°C\n" +
            "pH Optimal: 6.5-8.0\n" +
            "Oksigen Min: 3 mg/L\n" +
            "Padat Tebar: 50-100 ekor/m²\n" +
            "Panen: 2-3 bulan\n" +
            "Ukuran Konsumsi: 8-12 ekor/kg\n" +
            "Kebutuhan Protein:\n" +
            "  • Larva: 45-50%\n" +
            "  • Juvenil: 35-40%\n" +
            "  • Pembesaran: 28-32%"
        });
        
        ikanDetail.put("Nila", new String[]{
            "Suhu Optimal: 25-30°C\n" +
            "pH Optimal: 6.5-8.5\n" +
            "Oksigen Min: 5 mg/L\n" +
            "Padat Tebar: 20-30 ekor/m²\n" +
            "Panen: 4-6 bulan\n" +
            "Ukuran Konsumsi: 3-4 ekor/kg\n" +
            "Kebutuhan Protein:\n" +
            "  • Larva: 40-45%\n" +
            "  • Juvenil: 32-35%\n" +
            "  • Pembesaran: 25-28%"
        });
        
        ikanDetail.put("Gurame", new String[]{
            "Suhu Optimal: 24-28°C\n" +
            "pH Optimal: 6.5-8.0\n" +
            "Oksigen Min: 4 mg/L\n" +
            "Padat Tebar: 5-10 ekor/m²\n" +
            "Panen: 8-12 bulan\n" +
            "Ukuran Konsumsi: 0.5-1 kg/ekor\n" +
            "Kebutuhan Protein:\n" +
            "  • Larva: 35-40%\n" +
            "  • Juvenil: 30-32%\n" +
            "  • Pembesaran: 25-28%"
        });
        
        // Detail kolam: {kelebihan, kekurangan, investasi, umur_teknis, perawatan}
        kolamDetail.put("Beton", new String[]{
            "KELEBIHAN:\n" +
            "• Awet (10+ tahun)\n" +
            "• Mudah dibersihkan\n" +
            "• Kontrol air lebih baik\n" +
            "• Minim kebocoran\n\n" +
            "KEKURANGAN:\n" +
            "• Biaya investasi tinggi\n" +
            "• Tidak fleksibel\n" +
            "• Perlu skill konstruksi\n\n" +
            "INVESTASI: Rp 300.000-400.000/m²\n" +
            "UMUR TEKNIS: 10-15 tahun\n" +
            "PERAWATAN: Pengecekan rutin retakan"
        });
        
        kolamDetail.put("Terpal", new String[]{
            "KELEBIHAN:\n" +
            "• Murah\n" +
            "• Fleksibel lokasi\n" +
            "• Mudah dipindah\n" +
            "• Instalasi cepat\n\n" +
            "KEKURANGAN:\n" +
            "• Tidak awet (2-3 tahun)\n" +
            "• Rentan bocor\n" +
            "• Sulit kontrol suhu\n\n" +
            "INVESTASI: Rp 100.000-200.000/m²\n" +
            "UMUR TEKNIS: 2-3 tahun\n" +
            "PERAWATAN: Hindari benda tajam"
        });
        
        kolamDetail.put("Tanah", new String[]{
            "KELEBIHAN:\n" +
            "• Biaya paling murah\n" +
            "• Ekosistem alami\n" +
            "• Stabilitas suhu\n" +
            "• Nutrisi alami tersedia\n\n" +
            "KEKURANGAN:\n" +
            "• Kontrol air sulit\n" +
            "• Rentan erosi\n" +
            "• Panen lebih sulit\n\n" +
            "INVESTASI: Rp 50.000-100.000/m²\n" +
            "UMUR TEKNIS: 5-8 tahun\n" +
            "PERAWATAN: Perbaikan tanggul rutin"
        });
    }
    
    private String getIkanDetail(String ikan) {
        String[] detail = ikanDetail.get(ikan);
        return detail != null ? detail[0] : "Data tidak tersedia";
    }
    
    private String getKolamDetail(String kolam) {
        String[] detail = kolamDetail.get(kolam);
        return detail != null ? detail[0] : "Data tidak tersedia";
    }
    
    private String getPakanDetail(String pakan) {
        HashMap<String, String> pakanDetail = new HashMap<>();
        pakanDetail.put("Pelet28", 
            "PROTEIN: 28%\n" +
            "LEMAK: 6-8%\n" +
            "SERAT: 4-6%\n" +
            "HARGA: Rp 12.000-15.000/kg\n" +
            "REKOMENDASI:\n" +
            "• Fase pembesaran\n" +
            "• Ikan: Lele, Nila, Mas\n" +
            "• Frekuensi: 3-4x/hari"
        );
        
        pakanDetail.put("Pelet32",
            "PROTEIN: 32%\n" +
            "LEMAK: 8-10%\n" +
            "SERAT: 3-5%\n" +
            "HARGA: Rp 15.000-18.000/kg\n" +
            "REKOMENDASI:\n" +
            "• Fase juvenil\n" +
            "• Ikan: Patin, Bawal\n" +
            "• Frekuensi: 4-5x/hari"
        );
        
        pakanDetail.put("Pelet35",
            "PROTEIN: 35%\n" +
            "LEMAK: 10-12%\n" +
            "SERAT: 2-4%\n" +
            "HARGA: Rp 18.000-22.000/kg\n" +
            "REKOMENDASI:\n" +
            "• Fase larva/juvenil\n" +
            "• Ikan premium: Patin, Bawal\n" +
            "• Frekuensi: 5-6x/hari"
        );
        
        return pakanDetail.getOrDefault(pakan, "Data tidak tersedia");
    }
}