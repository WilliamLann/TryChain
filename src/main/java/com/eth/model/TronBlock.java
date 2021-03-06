package com.eth.model;

import java.util.Map;

import com.eth.model.base.BaseTronBlock;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class TronBlock extends BaseTronBlock<TronBlock> {
	public static final TronBlock dao = new TronBlock().dao();
	private Page<TronTx> txs = new Page<>();
	private String time = null;
	private String periodTime = null;
	public void setTxs(Page<TronTx> txs){
		this.txs = txs;
	}
	public Page<TronTx> getTxs(){
		return txs;
	}
	public String getTime(){
		if(getTimestamp()!=null){
			time = LynxUtil.formatDuring(getTimestamp().getTime()/1000);
		}
		return time;
	}
	public String getPeriodTime(){
		if(getTimestamp()!=null){
			periodTime = LynxUtil.format(getTimestamp().getTime()/1000);
		}
		return periodTime;
	}
	@Override
	protected Map<String, Object> _getAttrs(){
		Map<String, Object> attrs = super._getAttrs();
		attrs.put("txs", this.getTxs());
		attrs.put("time", this.getTime());
		attrs.put("periodTime", this.getPeriodTime());
		return attrs;
	}
	public Page<TronBlock> getBlocksPage(int n, int size) {
		return paginate(n, size, "select *", "from tron_block where timestamp > ? order by number desc",LynxUtil.getLastMonth());
	}
	public Page<TronTx> getTxsPage(int n, int size){
		return TronTx.dao.paginate(n, size, "select a.*,b.tran_from,b.tran_to,b.tran_value", "from tron_tx a left join tron_tx_tran b on a.hash = b.hash where block = ?", getNumber());
	}
}
