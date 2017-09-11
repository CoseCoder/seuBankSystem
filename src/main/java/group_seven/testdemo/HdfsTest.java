package group_seven.testdemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;


public class HdfsTest {
	
	public static void uploadLocalfileHdfs (String  localfile){
		
			Configuration conf = new Configuration();
			conf.set("fs.default.name", "hdfs://yzc:9000");
			conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName()
			    );
			try {
				FileSystem fs= FileSystem.get(conf);

				FsPermission fp= new FsPermission("755");
			     Path pathSrc = new Path(localfile);
		
			     if(!fs.exists(pathSrc)){
			    	  fs.create(pathSrc);
			    	  fs.setPermission(pathSrc, fp);
			     }
			     
			     //上传 文件
			     fs.copyFromLocalFile(new Path(localfile), new Path(localfile));
			   
			     fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}  
			 
	}
	

}
