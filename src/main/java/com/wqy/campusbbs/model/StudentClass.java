package com.wqy.campusbbs.model;

public class StudentClass {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column STUDENT_CLASS.ID
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column STUDENT_CLASS.SPECIALTY_ID
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    private Long specialtyId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column STUDENT_CLASS.CLASS_NAME
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    private String className;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column STUDENT_CLASS.GMT_CREATE
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    private Long gmtCreate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column STUDENT_CLASS.ID
     *
     * @return the value of STUDENT_CLASS.ID
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column STUDENT_CLASS.ID
     *
     * @param id the value for STUDENT_CLASS.ID
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column STUDENT_CLASS.SPECIALTY_ID
     *
     * @return the value of STUDENT_CLASS.SPECIALTY_ID
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public Long getSpecialtyId() {
        return specialtyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column STUDENT_CLASS.SPECIALTY_ID
     *
     * @param specialtyId the value for STUDENT_CLASS.SPECIALTY_ID
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column STUDENT_CLASS.CLASS_NAME
     *
     * @return the value of STUDENT_CLASS.CLASS_NAME
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public String getClassName() {
        return className;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column STUDENT_CLASS.CLASS_NAME
     *
     * @param className the value for STUDENT_CLASS.CLASS_NAME
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column STUDENT_CLASS.GMT_CREATE
     *
     * @return the value of STUDENT_CLASS.GMT_CREATE
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column STUDENT_CLASS.GMT_CREATE
     *
     * @param gmtCreate the value for STUDENT_CLASS.GMT_CREATE
     *
     * @mbg.generated Fri Apr 16 18:30:08 CST 2021
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}