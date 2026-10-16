-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 16, 2026 at 08:23 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_people`
--

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

CREATE TABLE `person` (
  `id` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `thn_lahir` int(11) NOT NULL,
  `kategori` varchar(255) NOT NULL,
  `aktif` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `person`
--

INSERT INTO `person` (`id`, `nama`, `thn_lahir`, `kategori`, `aktif`) VALUES
('2501001', 'Budi Santosoasd', 2005, 'Ilmu Komputer', 1),
('2501002', 'Siti Nurhaliza', 2006, 'Sastra Inggris', 1),
('2501003', 'Rian Hidayat', 2004, 'Arsitektur', 0),
('2501004', 'Dewi Lestari', 2005, 'Akuntansi', 1),
('2501005', 'Ahmad Pratama', 2006, 'Fisika', 1),
('2501006', 'Clarissa Amanda', 2005, 'Musik', 0),
('2501007', 'Farhan Ramadhan', 2005, 'Ilmu Komputer', 1),
('2501008', 'Nabila Putri', 2006, 'Sastra Inggris', 1),
('2501009', 'Rizky Febrian', 2004, 'Arsitektur', 0),
('2501010', 'Maya Indah', 2005, 'Fisika', 1),
('2501011', 'Hendra Wijaya', 2006, 'Akuntansi', 1),
('2501012', 'Gita Gutawa', 2007, 'Musik', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
