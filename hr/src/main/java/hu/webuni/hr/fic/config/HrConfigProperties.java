package hu.webuni.hr.fic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {

	
	
	private Raise raise = new Raise();
	
	private Jwt jwt = new Jwt();

	private boolean test;
	
	public static class Jwt {
		
		private String secret;
		private String algorithm;
		private int timeout;
		private String issuer;
		
		public String getSecret() {
			return secret;
		}
		public void setSecret(String secret) {
			this.secret = secret;
		}
		public String getAlgorithm() {
			return algorithm;
		}
		public void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}
		public int getTimeout() {
			return timeout;
		}
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
		public String getIssuer() {
			return issuer;
		}
		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}
		
		
	}
	
	
	public static class Raise {

		private Default def = new Default();
		private Smart smart = new Smart();

		public Default getDef() {
			return def;
		}

		public void setDef(Default def) {
			this.def = def;
		}

		public Smart getSmart() {
			return smart;
		}

		public void setSmart(Smart smart) {
			this.smart = smart;
		}

	}

	public static class Default {
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}

	}

	public static class Smart {
		private String years;
		private String percents;

		public String getYears() {
			return years;
		}

		public void setYears(String years) {
			this.years = years;
		}

		public String getPercents() {
			return percents;
		}

		public void setPercents(String percents) {
			this.percents = percents;
		}

	}

	public Raise getRaise() {
		return raise;
	}

	public void setRaise(Raise raise) {
		this.raise = raise;
	}


	public Jwt getJwt() {
		return jwt;
	}

	public void setJwt(Jwt jwt) {
		this.jwt = jwt;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}
	
	

}
