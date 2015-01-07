package edu.bjtu.nourriture_web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class UploadImageAction extends ActionSupport{
	private File file;
    private String fileFileName;
    
    public void upload(){
        //得到工程保存图片的路径
        String root = ServletActionContext.getRequest().getRealPath("/upload");
        
		try {
			InputStream is = new FileInputStream(file);
			//得到图片保存的位置(根据root来得到图片保存的路径在tomcat下的该工程里)
	        String fileName = this.getFileName();
	        File dir = new File(root);
	        if(!dir.exists())
	        	dir.mkdirs();
	        File destFile = new File(root,fileName);
	        
	        //把图片写入到上面设置的路径里
	        OutputStream os = new FileOutputStream(destFile);
	        byte[] buffer = new byte[400];
	        int length  = 0 ;
	        while((length = is.read(buffer))>0){
	            os.write(buffer, 0, length);
	        }
	        is.close();
	        os.close();
	        
	        PrintWriter out = ServletActionContext.getResponse().getWriter();
	        out.println("http://123.57.36.82/nourriture/upload/" + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private String getFileName(){
    	Date date = new Date();
    	return String.valueOf(date.getTime()) + fileFileName;
    }

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
}
