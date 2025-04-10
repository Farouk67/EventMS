-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: emma_events
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `event_date` datetime NOT NULL,
  `location` varchar(255) NOT NULL,
  `created_by` int DEFAULT NULL,
  `event_type_id` int DEFAULT NULL,
  `attendee_count` int DEFAULT '0',
  `capacity` int DEFAULT '0',
  `registration_required` tinyint(1) DEFAULT '0',
  `ticket_price` decimal(10,2) DEFAULT '0.00',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `event_type_id` (`event_type_id`),
  CONSTRAINT `event_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `event_ibfk_2` FOREIGN KEY (`event_type_id`) REFERENCES `event_type` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'Tech Conference 2025','Annual technology conference featuring the latest innovations and trends','2025-06-15 00:00:00','Convention Center, Downtown',1,NULL,6,10,0,0.00,'2025-04-08 22:49:10','2025-04-09 21:00:20'),(2,'Jazz Night','An evening of smooth jazz music with local artists','2025-04-20 20:00:00','Blue Note Club',2,3,5,100,0,0.00,'2025-04-08 22:49:10','2025-04-09 00:34:51'),(3,'Web Development Workshop','Learn the basics of HTML, CSS, and JavaScript','2025-05-10 13:00:00','Tech Hub Coworking Space',1,2,2,30,1,99.99,'2025-04-08 22:49:10','2025-04-08 22:49:10'),(4,'Networking Mixer','Connect with professionals in your industry','2025-04-25 18:00:00','Grand Hotel Rooftop',3,4,4,75,0,0.00,'2025-04-08 22:49:10','2025-04-09 00:34:59'),(5,'Charity Fun Run','5K run to raise funds for local children\'s hospital','2025-05-30 08:00:00','City Park',4,5,4,200,1,25.00,'2025-04-08 22:49:10','2025-04-08 22:49:10'),(6,'Art Exhibition Opening','Opening reception for new contemporary art exhibit','2025-05-05 19:00:00','Modern Art Gallery',3,6,5,120,0,0.00,'2025-04-08 22:49:10','2025-04-09 00:36:21'),(7,'Mobile App Development Seminar','Industry experts sharing insights on mobile app development','2025-06-05 10:00:00','Innovation Center',1,2,3,50,1,149.99,'2025-04-08 22:49:10','2025-04-08 22:49:10'),(8,'Summer Social Barbecue','Annual summer barbecue and social gathering','2025-07-04 12:00:00','Riverside Park',2,4,2,150,0,0.00,'2025-04-08 22:49:10','2025-04-08 22:49:10'),(9,'Advanced AI Conference','Exploring cutting-edge developments in artificial intelligence','2025-09-15 09:00:00','Tech Innovation Center',5,1,0,300,1,299.99,'2025-04-08 22:49:10','2025-04-08 22:49:10'),(10,'Global Climate Summit','International conference on environmental sustainability','2025-10-22 10:00:00','Convention Center',6,1,1,500,1,199.50,'2025-04-08 22:49:10','2025-04-09 23:37:09'),(11,'AI Conference Summit','Join the global community of innovators, researchers, developers, and industry leaders at the AI Conference Summit 2025 â a premier event exploring the latest advancements, trends, and real-world applications in Artificial Intelligence.','2025-04-30 00:00:00','Coventry',1,NULL,0,500,0,0.00,'2025-04-09 20:45:44','2025-04-09 20:45:44'),(12,'Football Conference 2025 Summit','Get ready for the ultimate gathering of football minds at the Football Conference Summit 2025 â where the beautiful game meets innovation, strategy, and global collaboration.\r\n\r\nThis high-energy summit brings together coaches, players, analysts, scouts, executives, medical professionals, and fans from across the world to explore the evolving landscape of football both on and off the pitch. From cutting-edge performance analytics to grassroots development and the business of football, this event covers it all.','2025-05-30 00:00:00','Convention Center, Downtown',1,NULL,0,1000,0,0.00,'2025-04-09 21:38:56','2025-04-09 21:38:56'),(13,'Marriage Ceremony of The Prime Minister\'s daughter','It is with great joy and heartfelt celebration that the Honourable Prime Minister [Name] and [Spouse\'s Name] invite esteemed guests to witness the sacred union of their beloved daughter, [Bride\'s Full Name], and [Groom\'s Full Name].\r\n\r\nThis momentous occasion marks not only the coming together of two individuals but also the union of families, values, and futures. Surrounded by dignitaries, family, friends, and well-wishers from across the nation and beyond, the ceremony promises to be a reflection of cultural heritage, unity, and love.','2025-04-30 00:00:00','Manchester',1,NULL,0,100,0,0.00,'2025-04-10 00:10:29','2025-04-10 02:20:09'),(14,'Healthy Living Fair','Wellness for Every Body\r\nA day of fitness classes, nutrition workshops, health screenings, and wellness talks designed to inspire a healthier lifestyle for all ages.','2025-04-26 00:00:00','Convention Center, Downtown',10,NULL,0,40,0,0.00,'2025-04-10 01:38:59','2025-04-10 01:38:59');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_type`
--

DROP TABLE IF EXISTS `event_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` text,
  `icon` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_type`
--

LOCK TABLES `event_type` WRITE;
/*!40000 ALTER TABLE `event_type` DISABLE KEYS */;
INSERT INTO `event_type` VALUES (1,'Conference','Professional gatherings focused on specific industry topics','bi-briefcase'),(2,'Workshop','Interactive sessions focused on skill development','bi-tools'),(3,'Concert','Musical performances and entertainment events','bi-music-note-beamed'),(4,'Social','Casual gatherings focused on networking and socializing','bi-people'),(5,'Sports','Athletic competitions and sporting events','bi-trophy'),(6,'Exhibition','Displays of art, products, or information','bi-easel'),(7,'Other','Miscellaneous events that dont fit other categories','bi-three-dots');
/*!40000 ALTER TABLE `event_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rsvp`
--

DROP TABLE IF EXISTS `rsvp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rsvp` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `event_id` int NOT NULL,
  `status` varchar(20) DEFAULT 'attending',
  `responded_at` datetime NOT NULL,
  `notes` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_rsvp` (`user_id`,`event_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `rsvp_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `rsvp_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rsvp`
--

LOCK TABLES `rsvp` WRITE;
/*!40000 ALTER TABLE `rsvp` DISABLE KEYS */;
INSERT INTO `rsvp` VALUES (1,2,1,'attending','2025-04-08 23:49:10',NULL),(2,3,1,'attending','2025-04-08 23:49:10',NULL),(3,4,1,'attending','2025-04-08 23:49:10',NULL),(4,2,3,'attending','2025-04-08 23:49:10',NULL),(5,3,2,'attending','2025-04-08 23:49:10',NULL),(6,4,4,'attending','2025-04-08 23:49:10',NULL),(7,2,5,'attending','2025-04-08 23:49:10',NULL),(8,3,6,'attending','2025-04-08 23:49:10',NULL),(9,5,7,'attending','2025-04-08 23:49:10',NULL),(10,6,7,'attending','2025-04-08 23:49:10',NULL),(11,1,8,'attending','2025-04-08 23:49:10',NULL),(12,2,8,'attending','2025-04-08 23:49:10',NULL),(13,3,4,'attending','2025-04-08 23:49:10',NULL),(14,4,5,'attending','2025-04-08 23:49:10',NULL),(15,5,6,'attending','2025-04-08 23:49:10',NULL),(16,6,2,'attending','2025-04-08 23:49:10',NULL),(17,7,1,'attending','2025-04-08 23:49:10',NULL),(18,8,3,'attending','2025-04-08 23:49:10',NULL),(19,1,4,'attending','2025-04-08 23:49:10',NULL),(20,2,6,'attending','2025-04-08 23:49:10',NULL),(21,3,5,'attending','2025-04-08 23:49:10',NULL),(22,4,7,'attending','2025-04-08 23:49:10',NULL),(23,5,2,'attending','2025-04-08 23:49:10',NULL),(24,6,5,'attending','2025-04-08 23:49:10',NULL),(25,10,1,'attending','2025-04-08 23:55:49',NULL),(26,10,2,'attending','2025-04-08 23:55:58',NULL),(27,10,6,'attending','2025-04-08 23:56:05',NULL),(28,11,1,'attending','2025-04-09 01:34:45',NULL),(29,11,2,'attending','2025-04-09 01:34:52',NULL),(30,11,4,'attending','2025-04-09 01:35:00',NULL),(31,11,6,'attending','2025-04-09 01:36:21',NULL),(33,1,10,'attending','2025-04-10 00:37:09',NULL);
/*!40000 ALTER TABLE `rsvp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `bio` text,
  `registered_date` datetime NOT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `role` varchar(20) DEFAULT 'user',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin@emmaevents.com','12345678','Admin','User',NULL,'2025-04-08 23:49:10','2025-04-10 12:50:29',1,'admin'),(2,'jdoe','john.doe@example.com','12345678','John','Doe',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(3,'jsmith','jane.smith@example.com','12345678','Jane','Smith',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(4,'mwilliams','mike.williams@example.com','12345678','Mike','Williams',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(5,'alexr','alex.rodriguez@example.com','12345678','Alex','Rodriguez',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(6,'sarahk','sarah.kim@example.com','12345678','Sarah','Kim',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(7,'michaelb','michael.brown@example.com','12345678','Michael','Brown',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(8,'emilyw','emily.wang@example.com','12345678','Emily','Wang',NULL,'2025-04-08 23:49:10',NULL,1,'user'),(9,'davidl','david.lee@example.com','12345678','David','Lee',NULL,'2025-04-08 23:49:10','2025-04-09 01:05:38',1,'user'),(10,'davidakinbami1@gmail.com','davidakinbami1@gmail.com','ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f',NULL,NULL,NULL,'2025-04-08 23:54:01','2025-04-10 02:37:43',1,'user'),(11,'davidedenreal@gmail.com','davidedenreal@gmail.com','ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f',NULL,NULL,NULL,'2025-04-09 01:33:34','2025-04-09 01:34:17',1,'user');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-10 17:04:13
