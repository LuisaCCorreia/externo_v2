package com.scb.externo.shared;

public class APICartaoDeCreditoResponseBody {
    private String object;
    private String id;
    private String dateCreated;
    private String name;
    private String email;
    private String company;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String cpfCnpj;
    private String personType;
    private boolean deleted;
    private String[] additionalEmails;
    private String externalReference;
    private boolean notificationDisabled;
    private String observations;
    private String municipalInscription;
    
    public String getObject() {
        return object;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getMobilePhone() {
        return mobilePhone;
    }
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddressNumber() {
        return addressNumber;
    }
    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }
    public String getComplement() {
        return complement;
    }
    public void setComplement(String complement) {
        this.complement = complement;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getCpfCnpj() {
        return cpfCnpj;
    }
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }
    public String getPersonType() {
        return personType;
    }
    public void setPersonType(String personType) {
        this.personType = personType;
    }
    public boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String[] getAdditionalEmails() {
        return additionalEmails;
    }
    public void setAdditionalEmails(String[] additionalEmails) {
        this.additionalEmails = additionalEmails;
    }
    public String getExternalReference() {
        return externalReference;
    }
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }
    public boolean isNotificationDisabled() {
        return notificationDisabled;
    }
    public void setNotificationDisabled(boolean notificationDisabled) {
        this.notificationDisabled = notificationDisabled;
    }
    public String getObservations() {
        return observations;
    }
    public void setObservations(String observations) {
        this.observations = observations;
    }
    public String getMunicipalInscription() {
        return municipalInscription;
    }
    public void setMunicipalInscription(String municipalInscription) {
        this.municipalInscription = municipalInscription;
    }
    public String getStateInscription() {
        return stateInscription;
    }
    public void setStateInscription(String stateInscription) {
        this.stateInscription = stateInscription;
    }
    public boolean isCanDelete() {
        return canDelete;
    }
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
    public String getCannotBeDeletedReason() {
        return cannotBeDeletedReason;
    }
    public void setCannotBeDeletedReason(String cannotBeDeletedReason) {
        this.cannotBeDeletedReason = cannotBeDeletedReason;
    }
    public Boolean getCanEdit() {
        return canEdit;
    }
    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }
    public String getCannotEditReason() {
        return cannotEditReason;
    }
    public void setCannotEditReason(String cannotEditReason) {
        this.cannotEditReason = cannotEditReason;
    }
    public boolean isForeignCustomer() {
        return foreignCustomer;
    }
    public void setForeignCustomer(boolean foreignCustomer) {
        this.foreignCustomer = foreignCustomer;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    private String stateInscription;
    private boolean canDelete;
    private String cannotBeDeletedReason;
    private Boolean canEdit;
    private String cannotEditReason;
    private boolean foreignCustomer;
    private String city;
    private String state;
    private String country;
}
