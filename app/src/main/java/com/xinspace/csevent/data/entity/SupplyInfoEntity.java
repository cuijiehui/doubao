package com.xinspace.csevent.data.entity;

/**
 * 添加完善资料实体类
 */

public class SupplyInfoEntity {
    private String nickname;//昵称
    private String sex;//性别（0 女 1 男）
    private String birthday;//生日日期
    private String salary;//收入
    private String email;//E-Mail
    private String interest;//兴趣产品

    public SupplyInfoEntity(String nickname, String sex, String birthday, String salary, String email, String interest) {
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
        this.salary = salary;
        this.email = email;
        this.interest = interest;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "SupplyInfoEntity{" +
                "nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", salary='" + salary + '\'' +
                ", email='" + email + '\'' +
                ", interest='" + interest + '\'' +
                '}';
    }
}
