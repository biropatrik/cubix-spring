package hu.cubix.hr.patrik.dto;

public class SalaryAvgDto {

    private String job;
    private Double avgSalary;

    public SalaryAvgDto() {
    }

    public SalaryAvgDto(String job, Double avgSalary) {
        this.job = job;
        this.avgSalary = avgSalary;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Double getAvgSalary() {
        return avgSalary;
    }

    public void setAvgSalary(Double avgSalary) {
        this.avgSalary = avgSalary;
    }
}