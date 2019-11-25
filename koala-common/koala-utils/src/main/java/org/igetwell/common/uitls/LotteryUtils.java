package org.igetwell.common.uitls;

import java.util.ArrayList;
import java.util.List;

public class LotteryUtils {

    /**
     * 根据Math.random()产生一个double型的随机数，判断每个奖品出现的概率
     * @param weights 权重比列
     * @return random：奖品列表prizes中的序列（prizes中的第random个就是抽中的奖品）
     */
    public static int lottery(List<Double> weights) {
        try{
            //计算总权重
            double sumWeight = 0d;
            for(Double weight : weights){
                sumWeight += weight;
            }

            //产生随机数
            double random = Math.random();;
            System.err.println(random);

            //根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for(int i=0 ; i < weights.size();i++){
                d2 += weights.get(i) / sumWeight;
                if(i==0){
                    d1 = 0;
                }else{
                    d1 += weights.get(i-1) / sumWeight;
                }
                if(random >= d1 && random <= d2){
                    return i;
                }
            }
        }catch(Exception e){
            System.out.println("生成抽奖随机数出错，出错原因：" +e.getMessage());
        }
        return -1;
    }

    public static void main(String[] args) {
        List<Double> doubles = new ArrayList<>();
        doubles.add(0.1);
        doubles.add(0.9);
        doubles.add(2.0);
        doubles.add(3.0);
        doubles.add(4.0);
        int[] result=new int[5];
        for (int i = 0; i < 20000; i++)// 打印10000个测试概率的准确性
        {
            int selected = lottery(doubles);
            System.out.println("第"+i+"次抽中的奖品为："+ doubles.get(selected).intValue());
            result[selected]++;
            System.out.println("--------------------------------");
        }

        System.out.println("抽奖结束");
        System.out.println("每种奖品抽到的数量为：");
        System.out.println("特等奖："+result[0]);
        System.out.println("一等奖："+result[1]);
        System.out.println("二等奖："+result[2]);
        System.out.println("三等奖："+result[3]);
        System.out.println("四等奖："+result[4]);

    }
}
