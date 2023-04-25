package com.course.app.common.core.upload;

import lombok.Data;

/**
 * 数据上传操作的应答信息对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
public class UploadResponseInfo {
    /**
     * 上传是否出现错误。
     */
    private Boolean uploadFailed = false;
    /**
     * 具体错误信息。
     */
    private String errorMessage;
    /**
     * 返回前端的下载url。
     */
    private String downloadUri;
    /**
     * 上传文件所在路径。
     */
    private String uploadPath;
    /**
     * 返回给前端的文件名。
     */
    private String filename;
}
