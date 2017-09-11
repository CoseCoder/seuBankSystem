package group_seven.testdemo.configtest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

/**
 * Created by mira on 8/23/17.
 */
public class HadoopApi {

    public static void main(String[] aa){
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://localhost:9000");

        try {
            FileSystem fs= FileSystem.get(conf);
            FsPermission fp= new FsPermission("755");
            Path pathSrc = new Path("");

            if(!fs.exists(pathSrc)){
                fs.create(pathSrc);
                fs.setPermission(pathSrc, fp);
            }
            fs.create(new Path(""));
            fs.mkdirs(new Path(""));
            //上传 文件
            fs.copyFromLocalFile(new Path(""), new Path(""));
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
