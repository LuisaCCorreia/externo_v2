package com.scb.externo.shared;

public class AsaasCobrancaResponseDTO {
    
        private String object;
        private String id;
        private String dateCreated;
        private String customer;
        private String paymentLink;
        private Float value;
        private Float netValue;
        private Float originalValue;
        private Float interestValue;
        private String description;
        private String billingType;
        private String confirmedDate;
        private APICartaoTokenResponse creditCard;
        private String pixTransaction;
        private String status;
        private String dueDate;
        private String originalDueDate;
        private String paymentDate;
        private String clientPaymentDate;
        private String installmentNumber;
        private String invoiceUrl;
        private String invoiceNumber;
        private String externalReference;
        private boolean deleted;
        private boolean anticipated;
        private boolean anticipable;
        private String creditDate;
        private String estimatedCreditDate;
        private String transactionReceiptUrl;
        private String nossoNumero;
        private String bankSlipUrl;
        private String lastInvoiceViewedDate;
        private String lastBankSlipViewedDate;
        private String postalService;
        private String refunds;
        
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
        public String getCustomer() {
            return customer;
        }
        public void setCustomer(String customer) {
            this.customer = customer;
        }
        public String getPaymentLink() {
            return paymentLink;
        }
        public void setPaymentLink(String paymentLink) {
            this.paymentLink = paymentLink;
        }
        public Float getValue() {
            return value;
        }
        public void setValue(Float value) {
            this.value = value;
        }
        public Float getNetValue() {
            return netValue;
        }
        public void setNetValue(Float netValue) {
            this.netValue = netValue;
        }
        public Float getOriginalValue() {
            return originalValue;
        }
        public void setOriginalValue(Float originalValue) {
            this.originalValue = originalValue;
        }
        public Float getInterestValue() {
            return interestValue;
        }
        public void setInterestValue(Float interestValue) {
            this.interestValue = interestValue;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getBillingType() {
            return billingType;
        }
        public void setBillingType(String billingType) {
            this.billingType = billingType;
        }
        public String getConfirmedDate() {
            return confirmedDate;
        }
        public void setConfirmedDate(String confirmedDate) {
            this.confirmedDate = confirmedDate;
        }
        public APICartaoTokenResponse getCreditCard() {
            return creditCard;
        }
        public void setCreditCard(APICartaoTokenResponse creditCard) {
            this.creditCard = creditCard;
        }
        public String getPixTransaction() {
            return pixTransaction;
        }
        public void setPixTransaction(String pixTransaction) {
            this.pixTransaction = pixTransaction;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getDueDate() {
            return dueDate;
        }
        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
        public String getOriginalDueDate() {
            return originalDueDate;
        }
        public void setOriginalDueDate(String originalDueDate) {
            this.originalDueDate = originalDueDate;
        }
        public String getPaymentDate() {
            return paymentDate;
        }
        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }
        public String getClientPaymentDate() {
            return clientPaymentDate;
        }
        public void setClientPaymentDate(String clientPaymentDate) {
            this.clientPaymentDate = clientPaymentDate;
        }
        public String getInstallmentNumber() {
            return installmentNumber;
        }
        public void setInstallmentNumber(String installmentNumber) {
            this.installmentNumber = installmentNumber;
        }
        public String getInvoiceUrl() {
            return invoiceUrl;
        }
        public void setInvoiceUrl(String invoiceUrl) {
            this.invoiceUrl = invoiceUrl;
        }
        public String getInvoiceNumber() {
            return invoiceNumber;
        }
        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }
        public String getExternalReference() {
            return externalReference;
        }
        public void setExternalReference(String externalReference) {
            this.externalReference = externalReference;
        }
        public boolean isDeleted() {
            return deleted;
        }
        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }
        public boolean isAnticipated() {
            return anticipated;
        }
        public void setAnticipated(boolean anticipated) {
            this.anticipated = anticipated;
        }
        public boolean isAnticipable() {
            return anticipable;
        }
        public void setAnticipable(boolean anticipable) {
            this.anticipable = anticipable;
        }
        public String getCreditDate() {
            return creditDate;
        }
        public void setCreditDate(String creditDate) {
            this.creditDate = creditDate;
        }
        public String getEstimatedCreditDate() {
            return estimatedCreditDate;
        }
        public void setEstimatedCreditDate(String estimatedCreditDate) {
            this.estimatedCreditDate = estimatedCreditDate;
        }
        public String getTransactionReceiptUrl() {
            return transactionReceiptUrl;
        }
        public void setTransactionReceiptUrl(String transactionReceiptUrl) {
            this.transactionReceiptUrl = transactionReceiptUrl;
        }
        public String getNossoNumero() {
            return nossoNumero;
        }
        public void setNossoNumero(String nossoNumero) {
            this.nossoNumero = nossoNumero;
        }
        public String getBankSlipUrl() {
            return bankSlipUrl;
        }
        public void setBankSlipUrl(String bankSlipUrl) {
            this.bankSlipUrl = bankSlipUrl;
        }
        public String getLastInvoiceViewedDate() {
            return lastInvoiceViewedDate;
        }
        public void setLastInvoiceViewedDate(String lastInvoiceViewedDate) {
            this.lastInvoiceViewedDate = lastInvoiceViewedDate;
        }
        public String getLastBankSlipViewedDate() {
            return lastBankSlipViewedDate;
        }
        public void setLastBankSlipViewedDate(String lastBankSlipViewedDate) {
            this.lastBankSlipViewedDate = lastBankSlipViewedDate;
        }
        public String getPostalService() {
            return postalService;
        }
        public void setPostalService(String postalService) {
            this.postalService = postalService;
        }
        public String getRefunds() {
            return refunds;
        }
        public void setRefunds(String refunds) {
            this.refunds = refunds;
        }
    
    
}
