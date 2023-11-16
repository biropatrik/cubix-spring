package hu.cubix.hr.patrik;

import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Position;
import hu.cubix.hr.patrik.model.Qualification;
import hu.cubix.hr.patrik.service.InitDbService;
import hu.cubix.hr.patrik.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;

	@Autowired
	InitDbService initDbService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Position position1 = new Position("Java Developer", Qualification.UNIVERSITY);

		Employee employee1 = new Employee(1, "Bryant Chan", position1, 350, LocalDateTime.of(2021,4,11, 8, 0));
		Employee employee2 = new Employee(2, "Kolby Anderson", position1, 350, LocalDateTime.of(2021,9,1, 8, 0));
		Employee employee3 = new Employee(3, "Diana Brewer", position1, 350, LocalDateTime.of(2010,2,1, 8, 0));
		Employee employee4 = new Employee(4, "David Lester", position1, 350, LocalDateTime.of(2018,10,1, 8, 0));

		salaryService.giveNewSalaryForEmployee(employee1);
		salaryService.giveNewSalaryForEmployee(employee2);
		salaryService.giveNewSalaryForEmployee(employee3);
		salaryService.giveNewSalaryForEmployee(employee4);

		System.out.println(employee1.getSalary());
		System.out.println(employee2.getSalary());
		System.out.println(employee3.getSalary());
		System.out.println(employee4.getSalary());

		initDbService.clearDB();
		initDbService.insertTestData();
	}
}
