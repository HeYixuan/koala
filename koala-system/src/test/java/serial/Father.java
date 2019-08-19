package serial;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/19 19:50
 */
public class Father {

    public Father(){

    }

    private Integer fatherAttr;

    public Father(int i) {
        fatherAttr = i;
    }

    public Integer getFatherAttr() {
        return fatherAttr;
    }

    public void setFatherAttr(Integer fatherAttr) {
        this.fatherAttr = fatherAttr;
    }

    @Override
    public String toString() {
        return "Father{" +
                "fatherAttr=" + fatherAttr +
                '}';
    }
}
