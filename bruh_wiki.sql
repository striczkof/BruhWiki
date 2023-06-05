-- MariaDB dump 10.19  Distrib 10.11.3-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: bruh_wiki
-- ------------------------------------------------------
-- Server version	10.11.3-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `bruh_wiki`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bruh_wiki` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `bruh_wiki`;

--
-- Table structure for table `articles`
--

DROP TABLE IF EXISTS `articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `articles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL DEFAULT 0,
  `made` timestamp NULL DEFAULT current_timestamp(),
  `lastEdited` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `title` tinytext DEFAULT NULL,
  `content` longtext DEFAULT NULL,
  `hidden` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  FULLTEXT KEY `title` (`title`,`content`),
  CONSTRAINT `articles_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articles`
--

LOCK TABLES `articles` WRITE;
/*!40000 ALTER TABLE `articles` DISABLE KEYS */;
INSERT INTO `articles` VALUES
(1,1,'2023-05-30 05:35:38','2023-05-30 05:35:38','Ancient Egypt','Ancient Egypt was a civilization that thrived along the Nile River in northeastern Africa. It emerged around 3100 BCE and lasted for over three millennia. The Egyptians built remarkable pyramids, such as the Great Pyramid of Giza, and developed a complex society with a writing system known as hieroglyphics.',0),
(2,2,'2023-05-30 05:35:38','2023-05-30 05:35:38','The Amazon Rainforest','The Amazon Rainforest is the largest tropical rainforest in the world, covering a vast area in South America. It is home to an incredible diversity of plant and animal species, many of which are found nowhere else on Earth. The Amazon River, running through the rainforest, is the largest river in the world by volume.',0),
(3,3,'2023-05-30 05:35:38','2023-05-30 05:35:38','Theory of Evolution','The theory of evolution, proposed by Charles Darwin, explains how species change over time through the process of natural selection. It states that organisms with traits that are advantageous for their environment are more likely to survive and reproduce, passing those traits on to future generations.',0),
(4,4,'2023-05-30 05:35:38','2023-05-30 05:35:38','Artificial Intelligence','Artificial Intelligence (AI) is a branch of computer science that focuses on creating intelligent machines capable of performing tasks that typically require human intelligence. AI has applications in various fields, including robotics, natural language processing, and machine learning.',0),
(5,5,'2023-05-30 05:35:38','2023-05-30 05:35:38','Renaissance Art','The Renaissance was a period of cultural and artistic rebirth in Europe, spanning roughly from the 14th to the 17th century. Renaissance artists, such as Leonardo da Vinci and Michelangelo, created masterpieces that reflected a renewed interest in humanism, perspective, and the natural world.',0),
(6,6,'2023-05-30 05:35:38','2023-05-30 05:35:38','Football (Soccer)','Football, also known as soccer in some countries, is the most popular sport in the world. It is played by two teams of eleven players each, who aim to score goals by kicking a ball into the opposing team\'s net. The FIFA World Cup is the most prestigious international football tournament.',0),
(7,7,'2023-05-30 05:35:38','2023-05-30 05:35:38','Democracy','Democracy is a system of government in which power is vested in the people, who exercise it through elected representatives or directly. It emphasizes individual freedom, political equality, and participation in decision-making. Democracy has various forms, including representative democracy and direct democracy.',0),
(8,8,'2023-05-30 05:35:38','2023-05-30 05:35:38','Gender Equality','Gender equality refers to the equal rights, opportunities, and treatment of individuals, regardless of their gender. It aims to create a society where men and women have the same access to resources, opportunities, and decision-making power, promoting fairness and inclusivity.',0),
(9,9,'2023-05-30 05:35:38','2023-05-30 05:35:38','Japanese Tea Ceremony','The Japanese tea ceremony, also known as Chanoyu or Sado, is a traditional ritualized practice of serving and drinking tea. It is influenced by Zen Buddhism and focuses on mindfulness, aesthetics, and the appreciation of simplicity. The ceremony embodies harmony, respect, and tranquility.',0),
(10,10,'2023-05-30 05:35:38','2023-05-30 05:35:38','Hinduism','Hinduism is one of the oldest religions in the world, originating in the Indian subcontinent. It is a complex and diverse faith, with a wide range of beliefs and practices. Hinduism encompasses concepts such as karma, dharma, and moksha, and worships various gods and goddesses.',0),
(11,11,'2023-05-30 05:35:38','2023-05-30 05:35:38','Existentialism','Existentialism is a philosophical movement that emphasizes individual existence, freedom, and choice. It explores questions about the meaning of life, human responsibility, and the nature of reality. Existentialist thinkers, such as Jean-Paul Sartre and Friedrich Nietzsche, have made significant contributions to this field.',0),
(12,12,'2023-05-30 05:35:38','2023-05-30 05:35:38','Market Economy','A market economy, also known as a free market economy or capitalism, is an economic system in which resources and prices are determined by market forces, such as supply and demand. It allows individuals and businesses to make decisions based on their self-interest, leading to competition and innovation.',0),
(13,13,'2023-05-30 05:35:38','2023-05-30 05:35:38','Climate Change','Climate change refers to long-term shifts in temperature patterns and weather conditions across the Earth. It is primarily caused by human activities, such as the burning of fossil fuels and deforestation, which release greenhouse gases into the atmosphere. Climate change has far-reaching environmental and societal impacts.',0),
(14,14,'2023-05-30 05:35:38','2023-05-30 05:35:38','Cardiovascular Disease','Cardiovascular disease refers to conditions that affect the heart and blood vessels, such as coronary artery disease, heart attacks, and strokes. Risk factors include high blood pressure, high cholesterol levels, smoking, obesity, and a sedentary lifestyle. Prevention and management strategies include a healthy diet, regular exercise, and medical interventions.',0),
(15,15,'2023-05-30 05:35:38','2023-05-30 05:35:38','Montessori Method','The Montessori method is an educational approach developed by Maria Montessori. It emphasizes self-directed learning, hands-on activities, and individualized instruction. The method promotes independence, creativity, and the development of social and cognitive skills in children.',0),
(16,16,'2023-05-30 05:35:38','2023-05-30 05:35:38','Spanish Language','Spanish is a Romance language spoken by millions of people around the world. It originated in the Castile region of Spain and has since spread to various countries in Latin America and beyond. Spanish is known for its rich vocabulary, melodic sound, and cultural influence.',0),
(17,17,'2023-05-30 05:35:38','2023-05-30 05:35:38','Pride and Prejudice','Pride and Prejudice is a novel written by Jane Austen. Published in 1813, it follows the story of Elizabeth Bennet as she navigates issues of love, marriage, and social status in Georgian-era England. The novel is renowned for its wit, social commentary, and enduring characters.',0),
(18,18,'2023-05-30 05:35:38','2023-05-30 05:35:38','Film Noir','Film noir is a cinematic genre that emerged in the 1940s and 1950s, characterized by its dark, cynical tone and visual style. It often features morally ambiguous characters, femme fatales, and convoluted plots. Classic film noir examples include \"Double Indemnity\" and \"The Maltese Falcon\".',0),
(19,19,'2023-05-30 05:35:38','2023-05-30 05:35:38','Maglev Trains','Maglev trains are a type of transportation that use magnetic levitation to suspend and propel the train without any physical contact with the track. This technology allows for high-speed travel with reduced friction and noise. Maglev trains have been implemented in countries like Japan, China, and Germany.',0),
(20,20,'2023-05-30 05:35:38','2023-05-30 05:35:38','Gothic Architecture','Gothic architecture is a style that originated in medieval Europe during the 12th century. It is characterized by its pointed arches, ribbed vaults, and flying buttresses, which allowed for taller and more elaborate structures. Gothic cathedrals, such as Notre-Dame de Paris and Chartres Cathedral, are iconic examples of this architectural style.',0),
(21,6,'2023-05-30 05:38:10','2023-05-30 05:38:10','Basketball','Basketball is a popular sport played by two teams of five players each. The objective is to shoot the ball through the opponent\'s hoop, scoring points while preventing the other team from doing the same. Basketball is known for its fast-paced gameplay, skillful maneuvers, and iconic players like Michael Jordan and LeBron James.',0),
(22,9,'2023-05-30 05:38:10','2023-05-30 05:38:10','Italian Cuisine','Italian cuisine is renowned worldwide for its diverse flavors and regional specialties. It encompasses a wide range of dishes, such as pasta, pizza, risotto, and gelato. Italian cuisine values fresh, high-quality ingredients and simple preparations that allow the flavors to shine.',0),
(23,14,'2023-05-30 05:38:10','2023-05-30 05:38:10','Mental Health','Mental health refers to a person\'s emotional, psychological, and social well-being. It affects how individuals think, feel, and behave, and it plays a crucial role in their overall quality of life. Mental health conditions, such as anxiety disorders and depression, are common and can be effectively managed with appropriate support and treatment.',0),
(24,3,'2023-05-30 05:38:10','2023-05-30 05:38:10','The Periodic Table','The periodic table is a tabular arrangement of chemical elements, organized based on their atomic number, electron configuration, and recurring chemical properties. It provides a framework for understanding the relationships between different elements and their behavior in chemical reactions.',0),
(25,12,'2023-05-30 05:38:10','2023-05-30 05:38:10','Globalization','Globalization is the process of increasing interconnectedness and interdependence among countries, economies, and societies worldwide. It involves the exchange of goods, services, information, and ideas across borders, leading to greater integration and intercultural interactions.',0),
(26,19,'2023-05-30 05:38:10','2023-05-30 05:38:10','Space Exploration','Space exploration is the human endeavor to explore outer space, including the celestial bodies within it. It involves the use of various technologies, such as rockets, satellites, and space probes, to study and gather data about distant planets, stars, and galaxies.',0),
(27,11,'2023-05-30 05:38:10','2023-05-30 05:38:10','Ethics','Ethics is the branch of philosophy that deals with moral principles, values, and the rightness or wrongness of human conduct. It explores questions of morality, ethical dilemmas, and the foundations of ethical systems. Ethical theories, such as utilitarianism and deontology, provide frameworks for ethical decision-making.',0),
(28,5,'2023-05-30 05:38:10','2023-05-30 05:38:10','Impressionism','Impressionism is an art movement that emerged in the 19th century, primarily in France. It is characterized by its emphasis on capturing the fleeting effects of light and atmosphere, often through loose brushwork and vibrant colors. Impressionist painters, such as Claude Monet and Pierre-Auguste Renoir, revolutionized the art world with their innovative techniques.',0),
(29,8,'2023-05-30 05:38:10','2023-05-30 05:38:10','Social Media','Social media refers to online platforms and websites that allow users to create, share, and interact with content. It has transformed communication, information sharing, and social interactions, enabling people to connect and engage with others globally. Social media platforms include Facebook, Twitter, Instagram, and YouTube.',0),
(30,17,'2023-05-30 05:38:10','2023-05-30 05:38:10','William Shakespeare','William Shakespeare is widely regarded as one of the greatest playwrights and poets in history. He wrote numerous plays, including tragedies like \"Hamlet\" and \"Macbeth,\" comedies like \"A Midsummer Night\'s Dream\" and \"Twelfth Night,\" and sonnets that explored themes of love, beauty, and the passage of time.',0),
(31,8,'2023-05-30 05:47:10','2023-05-30 05:47:10','The Royal Family','The British royal family plays a significant role in the country\'s cultural and historical identity. It includes members such as Queen Elizabeth II, Prince Charles, and Prince William. The royal family carries out ceremonial duties, represents the nation internationally, and has a long-standing tradition of public service.',0),
(32,6,'2023-05-30 05:47:10','2023-05-30 05:47:10','Cricket','Cricket is a popular sport in the United Kingdom, known for its rich history and traditions. It is played between two teams of eleven players each and involves batting, bowling, and fielding. Matches can last several days in test cricket or shorter durations in limited-overs formats like One Day Internationals (ODIs) and Twenty20 (T20) matches.',0),
(33,14,'2023-05-30 05:47:10','2023-05-30 05:47:10','NHS (National Health Service)','The NHS is the publicly funded healthcare system in the United Kingdom. It provides comprehensive medical services, including primary care, hospital care, and specialized treatments, to all residents regardless of their ability to pay. The NHS is often praised for its commitment to universal access and high-quality care.',0),
(35,11,'2023-05-30 05:47:10','2023-05-30 05:47:10','Utilitarianism','Utilitarianism is an ethical theory that emphasizes maximizing overall happiness or utility. It posits that actions should be evaluated based on their consequences and the amount of pleasure or happiness they generate for the greatest number of people. Utilitarianism is associated with philosophers like Jeremy Bentham and John Stuart Mill.',0),
(36,20,'2023-05-30 05:47:10','2023-05-30 05:47:10','Gothic Revival Architecture','Gothic Revival architecture emerged in the 18th and 19th centuries as a revival of medieval Gothic architecture. It is characterized by its pointed arches, ornate detailing, and verticality. Prominent examples of Gothic Revival architecture include the Palace of Westminster in London and the Notre-Dame Basilica in Montreal.',0),
(37,15,'2023-05-30 05:47:10','2023-05-30 05:47:10','Oxford University','Oxford University is one of the oldest and most prestigious universities in the world. Located in Oxford, England, it offers a wide range of academic programs and is known for its academic excellence and tradition of tutorial-based teaching. Oxford University has produced numerous notable alumni, including prime ministers, Nobel laureates, and influential thinkers.',0),
(38,3,'2023-05-30 05:47:10','2023-05-30 05:47:10','The Theory of Relativity','The Theory of Relativity, formulated by Albert Einstein, revolutionized our understanding of space, time, and gravity. It consists of two main theories: the Special Theory of Relativity and the General Theory of Relativity. The Theory of Relativity has had profound implications in the fields of physics, astronomy, and cosmology.',0),
(39,17,'2023-05-30 05:47:10','2023-05-30 05:47:10','Charles Dickens','Charles Dickens was a prominent British author of the Victorian era. His novels, such as \"Great Expectations\" and \"A Tale of Two Cities,\" vividly depicted social issues and conditions of the time. Dickens\'s works are known for their memorable characters, intricate plots, and social commentary.',0),
(40,13,'2023-05-30 05:47:10','2023-05-30 05:47:10','Renewable Energy','Renewable energy refers to energy sources that can be replenished naturally, such as solar energy, wind power, and hydropower. It offers a sustainable alternative to fossil fuels, reducing greenhouse gas emissions and dependence on finite resources. The transition to renewable energy is essential for mitigating climate change and promoting a greener future.',0);
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES
(0,'Unknown'),
(1,'History'),
(2,'Geography'),
(3,'Science'),
(4,'Technology'),
(5,'Arts'),
(6,'Sports'),
(7,'Politics'),
(8,'Society'),
(9,'Culture'),
(10,'Religion'),
(11,'Philosophy'),
(12,'Economy'),
(13,'Environment'),
(14,'Health'),
(15,'Education'),
(16,'Languages'),
(17,'Literature'),
(18,'Media'),
(19,'Transportation'),
(20,'Architecture');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` tinytext NOT NULL,
  `hash` binary(32) NOT NULL,
  `salt` binary(16) NOT NULL,
  `name` tinytext DEFAULT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT 0,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_login` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES
(1,'admin',0xB194D61F7FC49B245F03912957AB40E97967309990134047D31B07B177FE2FF7,0xA58F51A7950DFB07B39C30D28ED39A65,'Administrator',1,'2023-06-02 22:35:23','2023-06-03 23:44:42'),
(2,'user',0x25355DCF944CF4EADBB540088EEB5236938ED33FE739991E0DE80EA337AF64EA,0x22859362F1C855563AD66B953EF5A886,'User',0,'2023-06-03 13:41:00','2023-06-03 13:41:00');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-05 19:20:53
