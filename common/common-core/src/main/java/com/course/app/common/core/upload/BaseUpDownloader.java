package com.course.app.common.core.upload;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.JSON;
import com.course.app.common.core.constant.ApplicationConstant;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.util.ContextUtil;
import com.course.app.common.core.util.MyCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 上传或下载文件抽象父类。
 * 包含存储本地文件的功能，以及上传和下载所需的通用方法。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
public abstract class BaseUpDownloader {

    /**
     * 构建上传文件的完整目录。
     *
     * @param rootBaseDir 文件下载的根目录。
     * @param modelName   所在数据表的实体对象名。
     * @param fieldName   关联字段的实体对象属性名。
     * @param asImage     是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @return 上传文件的完整路径名。
     */
    public String makeFullPath(
            String rootBaseDir, String modelName, String fieldName, Boolean asImage) {
        StringBuilder uploadPathBuilder = new StringBuilder(128);
        if (StringUtils.isNotBlank(rootBaseDir)) {
            uploadPathBuilder.append(rootBaseDir).append("/");
        }
        if (Boolean.TRUE.equals(asImage)) {
            uploadPathBuilder.append(ApplicationConstant.UPLOAD_IMAGE_PARENT_PATH);
        } else {
            uploadPathBuilder.append(ApplicationConstant.UPLOAD_ATTACHMENT_PARENT_PATH);
        }
        uploadPathBuilder.append("/").append(modelName).append("/").append(fieldName);
        return uploadPathBuilder.toString();
    }

    /**
     * 构建上传操作的返回对象。
     *
     * @param serviceContextPath 微服务的上下文路径，如: /admin/upms。
     * @param originalFilename   上传文件的原始文件名(包含扩展名)。
     */
    protected void fillUploadResponseInfo(
            UploadResponseInfo responseInfo, String serviceContextPath, String originalFilename) {
        // 根据请求上传的uri构建下载uri，只是将末尾的/upload改为/download即可。
        HttpServletRequest request = ContextUtil.getHttpRequest();
        String uri = request.getRequestURI();
        uri = StringUtils.removeEnd(uri, "/");
        uri = StringUtils.removeEnd(uri, "/upload");
        String downloadUri;
        if (StringUtils.isBlank(serviceContextPath)) {
            downloadUri = uri + "/download";
        } else {
            downloadUri = serviceContextPath + uri + "/download";
        }
        StringBuilder filenameBuilder = new StringBuilder(64);
        filenameBuilder.append(MyCommonUtil.generateUuid())
                .append(".").append(FilenameUtils.getExtension(originalFilename));
        responseInfo.setDownloadUri(downloadUri);
        responseInfo.setFilename(filenameBuilder.toString());
    }

    /**
     * 执行下载操作，从本地文件系统读取数据，并将读取的数据直接写入到HttpServletResponse应答对象。
     *
     * @param rootBaseDir 文件下载的根目录。
     * @param modelName   所在数据表的实体对象名。
     * @param fieldName   关联字段的实体对象属性名。
     * @param fileName    文件名。
     * @param asImage     是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @param response    Http 应答对象。
     * @throws IOException 操作错误。
     */
    public abstract void doDownload(
            String rootBaseDir,
            String modelName,
            String fieldName,
            String fileName,
            Boolean asImage,
            HttpServletResponse response) throws IOException;

    /**
     * 执行下载操作，从本地文件系统读取数据，并将读取的数据直接写入到HttpServletResponse应答对象。
     *
     * @param rootBaseDir 文件下载的根目录。
     * @param uriPath     uri中的路径名。
     * @param fileName    文件名。
     * @param response    Http 应答对象。
     * @throws IOException 操作错误。
     */
    public abstract void doDownload(
            String rootBaseDir,
            String uriPath,
            String fileName,
            HttpServletResponse response) throws IOException;

    /**
     * 执行文件上传操作，并存入本地文件系统，再将与该文件下载对应的Url直接写入到HttpServletResponse应答对象，返回给前端。
     *
     * @param serviceContextPath 微服务的上下文路径，如: /admin/upms。
     * @param rootBaseDir        存放上传文件的根目录。
     * @param modelName          所在数据表的实体对象名。
     * @param fieldName          关联字段的实体对象属性名。
     * @param uploadFile         Http请求中上传的文件对象。
     * @param asImage            是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @return 存储在本地上传文件名。
     * @throws IOException 操作错误。
     */
    public abstract UploadResponseInfo doUpload(
            String serviceContextPath,
            String rootBaseDir,
            String modelName,
            String fieldName,
            Boolean asImage,
            MultipartFile uploadFile) throws IOException;

    /**
     * 判断filename参数指定的文件名，是否被包含在fileInfoJson参数中。
     *
     * @param fileInfoJson 内部类UploadFileInfo的JSONArray数组。
     * @param filename     被包含的文件名。
     * @return 存在返回true，否则false。
     */
    public static boolean containFile(String fileInfoJson, String filename) {
        if (StringUtils.isAnyBlank(fileInfoJson, filename)) {
            return false;
        }
        List<UploadResponseInfo> fileInfoList = JSON.parseArray(fileInfoJson, UploadResponseInfo.class);
        if (CollectionUtils.isNotEmpty(fileInfoList)) {
            for (UploadResponseInfo fileInfo : fileInfoList) {
                if (StringUtils.equals(filename, fileInfo.getFilename())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected UploadResponseInfo verifyUploadArgument(
            Boolean asImage, MultipartFile uploadFile) throws IOException {
        UploadResponseInfo responseInfo = new UploadResponseInfo();
        if (Objects.isNull(uploadFile) || uploadFile.isEmpty()) {
            responseInfo.setUploadFailed(true);
            responseInfo.setErrorMessage(ErrorCodeEnum.INVALID_UPLOAD_FILE_ARGUMENT.getErrorMessage());
            return responseInfo;
        }
        if (BooleanUtil.isTrue(asImage) && ImageIO.read(uploadFile.getInputStream()) == null) {
            responseInfo.setUploadFailed(true);
            responseInfo.setErrorMessage(ErrorCodeEnum.INVALID_UPLOAD_FILE_FORMAT.getErrorMessage());
            return responseInfo;
        }
        return responseInfo;
    }
}
