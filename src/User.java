public class User {

  private String id;
  private String firstName;
  private String lastName;
  private Integer version;
  private String insuranceCompany;

  public User(
      String id,
      String firstName,
      String lastName,
      Integer version,
      String insuranceCompany
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.version = version;
    this.insuranceCompany = insuranceCompany;
  }

  public String getId() {
    return this.id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public Integer getVersion() {
    return this.version;
  }

  public String getInsuranceCompany() {
    return this.insuranceCompany;
  }
}