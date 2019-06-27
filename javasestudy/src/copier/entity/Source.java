package copier.entity;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Source {

    public Source() {
        this.name = randomStr();
        this.nickName = randomStr();
        this.age = randomInt(100);
        this.qq = randomInt(100000000);
        this.wechat = randomStr();
        this.birthday = new Date();
    }

    private String randomStr() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private Integer randomInt(int bound) {
        return new Random().nextInt(bound);
    }

    private String name;

    private String nickName;

    private Integer age;

    private Integer qq;

    private String wechat;

    private Date birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getQq() {
        return qq;
    }

    public void setQq(Integer qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
