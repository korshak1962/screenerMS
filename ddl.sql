CREATE TABLE `share_price` (
  `ticker` varchar(20) NOT NULL,
  `date` timestamp(6) NOT NULL,
  `open` double NOT NULL,
  `high` double NOT NULL,
  `low` double NOT NULL,
  `close` double NOT NULL,
  `volume` bigint NOT NULL,
  PRIMARY KEY (`ticker`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
/*!50100 PARTITION BY KEY (ticker)
PARTITIONS 8 */