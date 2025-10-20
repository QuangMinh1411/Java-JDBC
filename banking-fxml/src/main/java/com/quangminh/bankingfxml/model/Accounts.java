package com.quangminh.bankingfxml.model;

public class Accounts {
    private long account_number;
    private String full_name;
    private String email;
    private double balance;
    private String security_pin;


    public Accounts(long account_number, String full_name, String email, double balance, String security_pin) {
        this.account_number = account_number;
        this.full_name = full_name;
        this.email = email;
        this.balance = balance;
        this.security_pin = security_pin;
    }

    public long getAccount_number() {
        return account_number;
    }

    public void setAccount_number(long account_number) {
        this.account_number = account_number;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getSecurity_pin() {
        return security_pin;
    }

    public void setSecurity_pin(String security_pin) {
        this.security_pin = security_pin;
    }
}
