public class Anggota {
    private int id;
    private String nomorAnggota, nim, nama, kelas, divisi, username, password, role, fotoPath;

    public Anggota() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomorAnggota() { return nomorAnggota; }
    public void setNomorAnggota(String nomorAnggota) { this.nomorAnggota = nomorAnggota; }
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    public String getDivisi() { return divisi; }
    public void setDivisi(String divisi) { this.divisi = divisi; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
}