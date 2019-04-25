package com.sinosoft.ops.cimp.vo.to.cadre;

import io.swagger.annotations.ApiModelProperty;

public class CadreBasicInfoVO {

    /**
     * empId
     */
    @ApiModelProperty(value = "empId")
    private String empId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 姓名拼音
     */
    @ApiModelProperty(value = "姓名拼音")
    private String namePinYin;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;
    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private String birthday;
    /**
     * 籍贯
     */
    @ApiModelProperty(value = "籍贯")
    private String birthPlace;
    /**
     * 照片id
     */
    @ApiModelProperty(value = "照片id")
    private String photoId;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinYin() {
        return namePinYin;
    }

    public void setNamePinYin(String namePinYin) {
        this.namePinYin = namePinYin;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}
