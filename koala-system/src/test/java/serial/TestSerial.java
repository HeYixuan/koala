package serial;

import java.io.*;
import java.util.Arrays;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/19 19:50
 */
public class TestSerial {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //序列化
        Son son = new Son(1);
        //序列化字节流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //对象读取
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(son);
        //转换成字节
        byte[] bytes = byteArrayOutputStream.toByteArray();
        System.out.println(Arrays.toString(bytes));
        //反序列化
        //直接读取直接，用对象输入流直接读取出来
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        //读取后转成对应对象
        UserDetails u = (UserDetails) objectInputStream.readObject();
        System.out.println(u.toString());

    }

}
