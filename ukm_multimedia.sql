CREATE DATABASE ukm_multimedia;
USE ukm_multimedia;

-- Tabel Anggota
CREATE TABLE IF NOT EXISTS anggota (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomor_anggota VARCHAR(20) NOT NULL UNIQUE,
    nim VARCHAR(15) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    kelas VARCHAR(10) NOT NULL,
    divisi VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    foto_path VARCHAR(255),
    role VARCHAR(20) DEFAULT 'Anggota'
);

-- Tabel Portofolio
CREATE TABLE IF NOT EXISTS portofolio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    anggota_id INT,
    judul_karya VARCHAR(100),
    file_path VARCHAR(255),
    FOREIGN KEY (anggota_id) REFERENCES anggota(id) ON DELETE CASCADE
);

-- AKUN SUPER ADMIN 
INSERT INTO anggota (nomor_anggota, nim, nama, kelas, divisi, username, password, role) 
VALUES ('MM-INTI-001', '00000', 'Super Admin', 'Inti', 'Pengurus', 'Saja_Jaya', 'Multimedia26', 'Admin');