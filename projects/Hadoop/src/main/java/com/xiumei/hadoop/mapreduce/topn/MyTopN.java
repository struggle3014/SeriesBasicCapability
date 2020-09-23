package com.xiumei.hadoop.mapreduce.topn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 22:24 2020/6/11
 * @Version: 1.0
 * @Description:
 **/
public class MyTopN {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration(true);

        // 参数工具类
        String[] others = new GenericOptionsParser(conf, args).getRemainingArgs();

        int compare = Integer.compare(1, 2);


    }

}
