CREATE DATABASE ukm_multimedia;
USE ukm_multimedia;

CREATE TABLE anggota (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomor_anggota VARCHAR(20),
    nim VARCHAR(15) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    kelas VARCHAR(10),
    divisi VARCHAR(50),
    jabatan VARCHAR(50),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    foto_path VARCHAR(255),
    role VARCHAR(20) DEFAULT 'Anggota',
    status VARCHAR(20) DEFAULT 'Active'  
);

CREATE TABLE portofolio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    anggota_id INT,
    judul_karya VARCHAR(100),
    file_path VARCHAR(255),
    FOREIGN KEY (anggota_id) REFERENCES anggota(id) ON DELETE CASCADE
);


INSERT INTO anggota (nomor_anggota, nim, nama, kelas, divisi, jabatan, username, password, role, status) 
VALUES ('MM-KETUA-01', '00000', 'Super Admin', NULL, NULL, 'Ketua Umum', 'Saja_Jaya', 'Multimedia26', 'ketua', 'Active');