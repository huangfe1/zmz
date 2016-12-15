package com.dreamer.view.upload;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 
 * 创建人：fantasy <br>
 * 创建时间：2013-12-6 <br>
 * 功能描述： 用户文件上传下载和文件列表<br>
 */
@Controller
@RequestMapping("/userFile")
public class UserFileController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	

	
	public String getFolder(MultipartHttpServletRequest request, HttpServletResponse response, Map<String, Object> result){
	    return "";
	}
	
	/**
	 * 上传文件
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String upload(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException{
		return "";
	}
	
	/**
	 * 处理文件上传
	 */
	public boolean handler(MultipartHttpServletRequest request, HttpServletResponse response, Map<String, Object> result, String folder) throws IOException{
	
		return true;
	}
	
	
	

}