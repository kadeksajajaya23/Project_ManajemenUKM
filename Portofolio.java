public class Portofolio {
    private int id;
    private String namaAnggota; // Kita butuh nama pengupload
    private String judul;
    private String filePath;

    public Portofolio() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNamaAnggota() { return namaAnggota; }
    public void setNamaAnggota(String namaAnggota) { this.namaAnggota = namaAnggota; }
    
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}