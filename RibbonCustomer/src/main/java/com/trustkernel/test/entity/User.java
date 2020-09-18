package com.trustkernel.test.entity;
/**
 * 用于测试的pojo
 * @author qdl
 *
 */
public class User {
	private String name;
	private String sex;
	private String phone;
	
	public User() {
		super();
	}
	
	public User(String name, String sex, String phone) {
		super();
		this.name = name;
		this.sex = sex;
		this.phone = phone;
	}
	
	public String toString(){
        return "user:{"
                +"name: " + name + ", "
                +"sex: " + sex + ", "
                +"phone: " + phone
                +" }";
    }
	
	public String getName() {
			return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
