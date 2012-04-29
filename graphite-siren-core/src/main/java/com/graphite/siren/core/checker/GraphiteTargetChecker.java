package com.graphite.siren.core.checker;

import javax.inject.Named;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;

import com.graphite.siren.core.domain.Alert;
import com.graphite.siren.core.domain.AlertType;
import com.graphite.siren.core.domain.Check;

@Named
public class GraphiteTargetChecker implements TargetChecker {

	private HttpClient client;

	public GraphiteTargetChecker() {
		client = new HttpClient(new MultiThreadedHttpConnectionManager());
	}

	@Override
	public Alert check(Check check) throws Exception {
		GetMethod get = new GetMethod(check.getTarget());
		try {
			client.executeMethod(get);
			Integer value = Integer.valueOf(get.getResponseBodyAsString().trim());
			if (value >= Integer.valueOf(check.getError())) {
				return createAlert(check, value, AlertType.ERROR);
			}
			if (value >= Integer.valueOf(check.getWarn())) {
				return createAlert(check, value, AlertType.WARN);
			}
			return null;
		} finally {
			get.releaseConnection();
		}
	}

	private Alert createAlert(Check check, Integer value, AlertType type) {
		return new Alert()
			.withValue(value.toString())
			.withWarn(check.getWarn())
			.withError(check.getError())
			.withFromType(AlertType.UNKNOWN)
			.withToType(type)
			.withTimestamp(new DateTime());
	}

}