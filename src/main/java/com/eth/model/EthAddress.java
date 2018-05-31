package com.eth.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

import com.eth.model.base.BaseEthAddress;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class EthAddress extends BaseEthAddress<EthAddress> {
	public static final EthAddress dao = new EthAddress().dao();
	private Page<EthTx> txs = new Page<>();
	private BigDecimal minValue = null;
	public void setTxs(Page<EthTx> txs){
		this.txs = txs;
	}
	public Page<EthTx> getTxs(){
		return txs;
	}
	public BigDecimal getMinValue(){
		if(getEthHoldValue()!=null){
			minValue = new BigDecimal(getEthHoldValue()).divide(new BigDecimal("1000000000000000000"), new MathContext(8));
		}
		return minValue;
	}
	@Override
	protected Map<String, Object> _getAttrs(){
		Map<String, Object> attrs = super._getAttrs();
		attrs.put("txs", this.getTxs());
		attrs.put("minValue", this.getMinValue());
		return attrs;
	}
	public Page<EthAddress> getAddressPage(int n, int size) {
		return paginate(n, size, "select eth_address, eth_hold_value", "from eth_address order by (value+0) desc");
	}
	public Page<EthTx> getTxsPage(int n, int size) {
		return EthTx.dao.paginate(n, size, "select hash, blockNumber, timestamp, from_address, to_address, value", "from eth_tx where from_address = ? or to_address = ?  order by blockNumber desc", getEthAddress(), getEthAddress());
	}
	public Page<EthTx> getTopTxs(int n, int size) {
		System.out.println(getEthAddress());
		return EthTx.dao.paginate(n, size, "select hash, blockNumber, timestamp, from_address, to_address, value", "from eth_tx where from_address = ?  and block_date > ? order by blockNumber desc", getEthAddress(), LynxUtil.getFristDayOfThisMonth());
	}
	//首页五个地址
	public List<EthAddress> getLatestAddress() {
		return find("select * from eth_address ORDER BY `value` + 0 desc limit 5");
	}
}
