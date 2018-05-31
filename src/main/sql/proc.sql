CREATE DEFINER=`lynx_develop`@`%` PROCEDURE `create_blocks`()
BEGIN
DECLARE i INT;
SET i=0;
WHILE i<10 DO
SET @sql_del_table = CONCAT('DROP TABLE IF  EXISTS block_', i);
SET @sql_create_table = CONCAT(
'CREATE TABLE IF NOT EXISTS block_', i,
"(
  `number` bigint(40) unsigned NOT NULL,
  `difficulty` char(100) DEFAULT NULL,
  `gasLimit` char(40) DEFAULT NULL,
  `gasUsed` char(40) DEFAULT NULL,
  `hash` char(66) DEFAULT NULL,
  `miner` char(42) DEFAULT NULL,
  `mixHash` char(66) DEFAULT NULL,
  `nonce` char(18) DEFAULT NULL,
  `parentHash` char(66) DEFAULT NULL,
  `receiptsRoot` char(66) DEFAULT NULL,
  `sha3Uncles` char(66) DEFAULT NULL,
  `size` bigint(40) unsigned DEFAULT NULL,
  `stateRoot` char(66) DEFAULT NULL,
  `timestamp` bigint(40) unsigned DEFAULT NULL,
  `totalDifficulty` char(100) DEFAULT NULL,
  `transactionsRoot` char(66) DEFAULT NULL,
  `transactionsNum` int(40) DEFAULT NULL,
  PRIMARY KEY (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
");
PREPARE sql_del_table FROM @sql_del_table;   
EXECUTE sql_del_table;
PREPARE sql_create_table FROM @sql_create_table;   
EXECUTE sql_create_table;
SET i=i+1;
END WHILE;
    END

    
CREATE DEFINER=`lynx_develop`@`%` PROCEDURE `create_txn`()
BEGIN
DECLARE i INT;
SET i=0;
WHILE i<100 DO
SET @sql_del_table = CONCAT('DROP TABLE IF EXISTS tx_', i);
SET @sql_create_table = CONCAT(
'CREATE TABLE IF NOT EXISTS tx_', i,
"(
   `hash` CHAR(66) NOT NULL,
  `blockHash` CHAR(66) DEFAULT NULL,
  `blockNumber` BIGINT(20) UNSIGNED DEFAULT NULL,
  `from` CHAR(66) DEFAULT NULL,
  `gas` BIGINT(20) UNSIGNED DEFAULT NULL,
  `gasPrice` BIGINT(20) UNSIGNED DEFAULT NULL,
  `nonce` CHAR(20) DEFAULT NULL,
  `r` CHAR(66) DEFAULT NULL,
  `s` CHAR(66) DEFAULT NULL,
  `to` CHAR(66) DEFAULT NULL,
  `v` CHAR(20) DEFAULT NULL,
  `value` CHAR(60) DEFAULT NULL,
  PRIMARY KEY (`hash`),
  KEY `blocknumber` (`blockNumber`) USING HASH,
  KEY `blockhash` (`blockHash`) USING HASH,
  KEY `from` (`from`) USING HASH,
  KEY `to` (`to`) USING HASH 
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
");
PREPARE sql_del_table FROM @sql_del_table;   
EXECUTE sql_del_table;
PREPARE sql_create_table FROM @sql_create_table;   
EXECUTE sql_create_table;
SET i=i+1;
END WHILE;
    END
