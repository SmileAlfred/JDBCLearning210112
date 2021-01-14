package com.company.jdbc210112.test2_preparedstatement.bean;

public class ExamStudent {
    private int FlowID;
    private int Type;
    private String IDCard;
    private String ExamCard;
    private String StudentName;
    private String Location;
    private int Grade;

    @Override
    public String toString() {
        return "===========查询结果===========\n" +
                "流水号数:\t" + FlowID +
                "\n四级/六级:\t" + Type +
                "\n身份证号:\t" + IDCard +
                "\n准考证号:\t" + ExamCard +
                "\n学生姓名:\t" + StudentName +
                "\n所在地址:\t" + Location  +
                "\n所得成绩:\t" + Grade+"\n";
    }

    public int getFlowID() {
        return FlowID;
    }

    public void setFlowID(int flowID) {
        FlowID = flowID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return ExamCard;
    }

    public void setExamCard(String examCard) {
        ExamCard = examCard;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int grade) {
        Grade = grade;
    }

    public ExamStudent() {
    }

    public ExamStudent(int flowID, int type,
                       String IDCard, String examCard,
                       String studentName, String location, int grade) {
        FlowID = flowID;
        Type = type;
        this.IDCard = IDCard;
        ExamCard = examCard;
        StudentName = studentName;
        Location = location;
        Grade = grade;
    }
}
