package com.eth.model;

import java.util.List;
import java.util.Map;

import com.eth.model.base.BaseTbcAddress;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class TbcAddress extends BaseTbcAddress<TbcAddress> {
	public static final TbcAddress dao = new TbcAddress().dao();
	private Page<TbcTx> txs = new Page<>();
	private Page<TbcTxInter> txsInter = new Page<>();
	public void setTxs(Page<TbcTx> txs){
		this.txs = txs;
	}
	public Page<TbcTx> getTxs(){
		return txs;
	}
	public void setTxsInter(Page<TbcTxInter> txsInter){
		this.txsInter = txsInter;
	}
	public Page<TbcTxInter> getTxsInter(){
		return txsInter;
	}
	@Override
	protected Map<String, Object> _getAttrs(){
		Map<String, Object> attrs = super._getAttrs();
		attrs.put("txs", this.getTxs());
		attrs.put("txsInter", this.getTxsInter());
		return attrs;
	}
	public Page<TbcTx> getTbcTxsPage(int n, int size){
		return TbcTx.dao.paginate(n, size, "select *", "from tbc_tx where tbcfrom = ? or tbcto = ? order by if(isnull(blockNumber),0,1), blockNumber desc", getAddress(), getAddress());
	}
	public Page<TbcTxInter> getTbcTxsInterPage(int n, int size){
		return TbcTxInter.dao.paginate(n, size, "select *", "from tbc_tx_inter where `from` = ? or `to` = ? order by timestamp desc", getAddress(), getAddress());
	}
	public List<TbcAddress> getAddressRand(){
		return find("select address from tbc_address order by rand() limit 10");
	}
	public TbcAddress findParentAddressByAddress(String address){
		return findFirst("select a.* from tbc_address a, user b where a.address = b.parent_eth_address and b.eth_address = ?", address);
	}
}
