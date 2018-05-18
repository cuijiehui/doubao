package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * author:yangtuhua
 * 用户信息中data的实体类
 */
public class ProfileDataEntity implements Serializable{


    private String tel;
    private String registration;
    private String gold;//金币
    private String experience;
    private String integral;
    private String image;
    private String sign_in;//是否签到
    private String nickname;//昵称
    private String sex;//性别
    private String birthday;//生日
    private String salary;//收入
    private String email;//邮箱

    private String mobile;
    private String realname;
    private String avatar;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ProfileDataEntity(String tel, String registration, String gold, String experience, String integral, String image, String sign_in, String nickname, String sex, String birthday, String salary, String email) {
        this.tel = tel;
        this.registration = registration;
        this.gold = gold;
        this.experience = experience;
        this.integral = integral;
        this.image = image;
        this.sign_in = sign_in;
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
        this.salary = salary;
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSign_in() {
        return sign_in;
    }

    public void setSign_in(String sign_in) {
        this.sign_in = sign_in;
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

    @Override
    public String toString() {
        return "ProfileDataEntity{" +
                "tel='" + tel + '\'' +
                ", registration='" + registration + '\'' +
                ", gold='" + gold + '\'' +
                ", experience='" + experience + '\'' +
                ", integral='" + integral + '\'' +
                ", image='" + image + '\'' +
                ", sign_in='" + sign_in + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", salary='" + salary + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
