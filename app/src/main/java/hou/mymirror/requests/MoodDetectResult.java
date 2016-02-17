package hou.mymirror.requests;

/**
 * Created by hou on 2016-1-18.
 */
public class MoodDetectResult {
    private int age;
    private String gender;
    private double smileDegree;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSmileDegree() {
        return smileDegree;
    }

    public void setSmileDegree(double smileDegree) {
        this.smileDegree = smileDegree;
    }

}
