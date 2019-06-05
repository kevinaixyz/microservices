package com.prototype.microservice.etl.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.etl.request.FileUploadRequestJson;
import com.prototype.microservice.etl.request.FileUploadStatusRequestJson;
import com.prototype.microservice.etl.service.EtlService;

@RestController
@RequestMapping(value = "/upload")
public class FileUploadController extends BaseRestController {

    @Autowired
    @Qualifier("rptService")
    private EtlService rptServiceInCtrl;

    @RequestMapping(path = "/readFile", method = RequestMethod.POST)
    private ResponseJson readExcelByConfig(@RequestBody final FileUploadRequestJson requestJson) throws IOException {
        String file = requestJson.getFile();
        if (StringUtils.isNotBlank(file)) {
            return rptServiceInCtrl.readFileByBase64Sync(file, requestJson.getFileName());
        }
        return null;
    }

    @RequestMapping(path = "/readFileAsync", method = RequestMethod.POST)
    private ResponseJson readExcelByConfigAsync(@RequestBody final FileUploadRequestJson requestJson) throws IOException {
        String file = requestJson.getFile();
        if (StringUtils.isNotBlank(file)) {
            return rptServiceInCtrl.readFileByBase64Async(file, requestJson.getFileName());
        }
        return null;
    }

    @RequestMapping(path = "/checkStatus", method = RequestMethod.POST)
    private ResponseJson checkRptJobStatus(@RequestBody final FileUploadStatusRequestJson requestJson) throws IOException {
        String jobId = requestJson.getJobId();
        if (StringUtils.isNotBlank(jobId)) {
            return rptServiceInCtrl.checkRptJobStatus(jobId);
        }
        return null;
    }
}
