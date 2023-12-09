package hu.cubix.logistics.patrik.dto;

import hu.cubix.logistics.patrik.validation.Create;
import hu.cubix.logistics.patrik.validation.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class AddressDto {

    @Null(groups = {Create.class})
    private Long id;

    @Size(min = 2, max = 2, groups = {Create.class, Update.class})
    private String countryISO;

    @NotEmpty(groups = {Create.class, Update.class})
    private String city;

    @NotEmpty(groups = {Create.class, Update.class})
    private String street;

    @NotEmpty(groups = {Create.class, Update.class})
    private String zipCode;

    @NotNull(groups = {Create.class, Update.class})
    private Integer houseNumber;

    private Integer latitude;

    private Integer longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }
}
