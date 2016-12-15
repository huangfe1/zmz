package com.dreamer.view.upload;

import com.dreamer.repository.system.SystemParameterDAOInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ps.mx.otter.exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by huangfei on 16/4/25.
 */
@RestController
public class UploadImageController {
    @Autowired
    private HttpServletRequest request;
    /**
     * 上传图片
     *
     * @return 返回图片地址
     */
    @RequestMapping("/upload")
    public String uploadImage(@RequestParam(value = "fileList") MultipartFile imageFile) {
        //ArrayList<String> nameList=new ArrayList<>();
        String imageName = null;
        try {
            //for(MultipartFile imageFile : imageFiles) {
                if(Objects.nonNull(imageFile)){
                    byte[] bytes = imageFile.getBytes();
                    imageName=generateFileName(".jpg");//生成图片名字
                    writeImgFile(bytes,imageName);//保存图片
            //        nameList.add(imageName);
                }
          //  }
        } catch (IOException e) {
            e.printStackTrace();
     }
       return  imageName;
//    FileItemFactory itemFactory = new DiskFileItemFactory();
//    ServletFileUpload upload= new ServletFileUpload(itemFactory);
//    List<FileItem> fileItems;
//        try {
//            fileItems=upload.parseRequest(request);
//            Iterator<FileItem> itr=fileItems.iterator();
//            while (itr.hasNext()){
//                FileItem fileItem=itr.next();
//                System.out.println(fileItem.getName());
//            }
//        } catch (FileUploadException e) {
//            e.printStackTrace();
//        }
//return "ss";
    }



    private void writeImgFile(byte[] imgBytes,String fileName) {
        try {
            Path path = Paths.get(systemParameterDAO.getGoodsImgPath());//获取地址
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path imgFile=path.resolve(fileName);
            if(!Files.exists(imgFile)){
                Files.createFile(imgFile);
            }
            Files.write(imgFile, imgBytes, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException iop) {
            //LOG.error("写图片文件失败",iop);
            throw new ApplicationException(iop);
        }
    }


    private String generateFileName(String imgType){
        String s= UUID.randomUUID().toString();
        StringBuilder sbd=new StringBuilder();
        sbd.append(s.substring(0,8)).append(s.substring(9,13)).append(s.substring(14,18)).append(s.substring(19,23)).append(s.substring(24)).append(imgType);
        return sbd.toString();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    private SystemParameterDAOInf systemParameterDAO;
}
