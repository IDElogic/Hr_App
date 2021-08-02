package hu.webuni.hr.fic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.fic.config.HrConfigProperties;
import hu.webuni.hr.fic.repository.CompanyRepository;
import hu.webuni.hr.fic.service.InitDbService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	InitDbService initDbService;

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	HrConfigProperties config;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!config.isTest()) {
			initDbService.clearDB();
			initDbService.insertTestData();
		}
	}

}
