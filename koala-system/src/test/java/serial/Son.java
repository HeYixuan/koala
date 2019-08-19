package serial;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/19 19:50
 */
public class Son extends Father implements UserDetails {

    public Son(int i){
        super(12);
        this.sonAttr = i;
    }

    private Integer sonAttr;

    public Integer getSonAttr() {
        return sonAttr;
    }

    public void setSonAttr(Integer sonAttr) {
        this.sonAttr = sonAttr;
    }

    @Override
    public String toString() {
        return "Son{" +
                "sonAttr=" + sonAttr +
                ", fatherAttr=" + getFatherAttr() +
                '}';
    }
}
