package group_seven.testdemo.configtest;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * Created by mira on 8/23/17.
 */
public class HiveUDF extends UDF {

    /**
     * 将cityudf.jar加入到HIVE类路径下
     * sql = "add jar /home/hadoop/cityudf.jar";
     * //加入到classpath下
     * sql = "create temporary function cityudf as 'com.hive.utf.CityUDF'";
     *
     * @param s
     * @return
     */
    public String evaluate(String s) {
        if (s == null) {
            return null;
        }
        return s+"_1";
    }

}
