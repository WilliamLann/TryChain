CREATE TABLE `address` (
  `address` char(66) NOT NULL,
  `type` char(10) DEFAULT NULL,
  `value` char(40) DEFAULT NULL,
  PRIMARY KEY (`address`),
  KEY `address` (`address`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `block_latest` (
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


CREATE TABLE `tbc_address` (
  `address` char(66) NOT NULL,
  `value` bigint(20) DEFAULT '0',
  `pending` bigint(20) DEFAULT '0',
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `tbc_tx` (
  `hash` char(66) NOT NULL,
  `status` char(10) DEFAULT 'na',
  `timestamp_submit` char(20) DEFAULT NULL,
  `timestamp` char(20) DEFAULT NULL,
  `blockHash` char(66) DEFAULT NULL,
  `blockNumber` bigint(20) unsigned DEFAULT NULL,
  `from` char(66) DEFAULT NULL,
  `gas` bigint(20) unsigned DEFAULT NULL,
  `gasPrice` bigint(20) unsigned DEFAULT NULL,
  `nonce` char(20) DEFAULT NULL,
  `r` char(66) DEFAULT NULL,
  `s` char(66) DEFAULT NULL,
  `to` char(66) DEFAULT NULL,
  `v` char(20) DEFAULT NULL,
  `value` char(60) DEFAULT NULL,
  `tbcfrom` char(66) DEFAULT NULL,
  `tbcto` char(66) DEFAULT NULL,
  `tbcvalue` char(66) DEFAULT NULL,
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `tx_latest` (
  `hash` char(66) COLLATE utf8mb4_unicode_ci NOT NULL,
  `timestamp` char(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `blockHash` char(66) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `blockNumber` bigint(20) unsigned DEFAULT NULL,
  `from` char(66) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gas` bigint(20) unsigned DEFAULT NULL,
  `gasPrice` bigint(20) unsigned DEFAULT NULL,
  `nonce` char(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `r` char(66) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s` char(66) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to` char(66) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `v` char(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `value` char(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`hash`),
  KEY `blocknumber` (`blockNumber`),
  KEY `blockhash` (`blockHash`),
  KEY `from` (`from`),
  KEY `to` (`to`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci


CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` char(30) DEFAULT NULL,
  `password` char(30) DEFAULT NULL,
  `phone` char(20) DEFAULT NULL,
  `wechat` char(30) DEFAULT NULL,
  `eth_address` char(66) DEFAULT NULL,
  `eth_filename` char(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2147483647 DEFAULT CHARSET=utf8
