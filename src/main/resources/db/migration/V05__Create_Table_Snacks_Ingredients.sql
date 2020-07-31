CREATE TABLE IF NOT EXISTS `snack_ingredient` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_snack` bigint(20) NOT NULL,
  `id_ingredient` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_ingredient`) REFERENCES ingredient(`id`),
  FOREIGN KEY (`id_snack`) REFERENCES snack(`id`)
);