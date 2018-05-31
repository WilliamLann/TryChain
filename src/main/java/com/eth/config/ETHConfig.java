package com.eth.config;

import com.eth.controller.EthController;
import com.eth.controller.HelpController;
import com.eth.controller.IndexController;
import com.eth.controller.TbcController;
import com.eth.controller.TronController;
import com.eth.controller.UserController;
import com.eth.jobs.QuartzPlugin;
import com.eth.model._MappingKit;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinalFilter;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

public class ETHConfig extends JFinalConfig {
	JFinalFilter f = new JFinalFilter();
	private Routes routes;
	private Engine engine;

	@Override
	public void configConstant(Constants me) {
		loadProp("application_prod.properties","application_test.properties", "application.properties");		
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setEncoding("UTF-8");
		// RequiresGuest，RequiresAuthentication，RequiresUser验证异常，返回HTTP401状态码
		me.setErrorView(401, "/common/error.html");
		// RequiresRoles，RequiresPermissions授权异常,返回HTTP403状态码
		me.setErrorView(403, "/common/error.html");
	}
	

	@Override
	public void configRoute(Routes me) {
		this.routes = me;
		me.add("/", IndexController.class);
		me.add("/user", UserController.class);
		me.add("/tbc", TbcController.class);
	    me.add("/eth",EthController.class);
	    me.add("/tron",TronController.class);
	    me.add("/help",HelpController.class);
	}

	@Override
	public void configEngine(Engine me) {
	}

	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin druidDefault = new DruidPlugin(PropKit.get("jdbc.url"), PropKit.get("jdbc.username"),
				PropKit.get("jdbc.password"));
		druidDefault.setInitialSize(getPropertyToInt("db.default.poolInitialSize"));
		druidDefault.setMaxActive(100);
		druidDefault.setMaxPoolPreparedStatementPerConnectionSize(getPropertyToInt("db.default.poolMaxSize"));
		druidDefault.setTimeBetweenConnectErrorMillis(getPropertyToInt("db.default.connectionTimeoutMillis"));
		System.out.println(PropKit.get("jdbc.url"));
		System.out.println(getPropertyToInt("db.default.poolMaxSize"));

		me.add(druidDefault);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidDefault);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		arp.setShowSql(false);
		_MappingKit.mapping(arp);
		me.add(arp);
		me.add(new QuartzPlugin());


	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}
	private void loadProp(String pro, String test, String dev) {
		try {
			loadPropertyFile(pro);
			PropKit.use(pro);
		}
		catch (Exception e) {
			try{
				loadPropertyFile(test);
				PropKit.use(test);
			}
			catch(Exception e1) {
				loadPropertyFile(dev);
				PropKit.use(dev);
			}
		}
	}

}
