package com.divercity.android.data.entity.profile.profile;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("country")
    private String country;

    @SerializedName("city")
    private String city;

    @SerializedName("account_type")
    private String accountType;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("gender")
    private String gender;

    @SerializedName("ethnicity")
    private String ethnicity;

    @SerializedName("school_id")
    private String schoolId;

    @SerializedName("age_range")
    private String ageRange;

    @SerializedName("job_employer_id")
    private String jobEmployerId;

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getJobEmployerId() {
        return jobEmployerId;
    }

    public void setJobEmployerId(String jobEmployerId) {
        this.jobEmployerId = jobEmployerId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return
                "User{" +
                        "country = '" + country + '\'' +
                        ",account_type = '" + accountType + '\'' +
                        ",birthdate = '" + birthdate + '\'' +
                        ",occupation = '" + occupation + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",ethnicity = '" + ethnicity + '\'' +
                        ",school_id = '" + schoolId + '\'' +
                        ",age_range = '" + ageRange + '\'' +
                        ",job_employer_id = '" + jobEmployerId + '\'' +
                        ",city = '" + city + '\'' +
                        "}";
    }
}