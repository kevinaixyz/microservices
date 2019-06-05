package com.prototype.microservice.etl.data;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class BatchJobProcessor extends EtlProcessorTemplate {

    protected File file;
    private Map<String, String> sysColValMap;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setSysColValMap(Map<String, String> sysColValMap) {
        this.sysColValMap = sysColValMap;
    }

    //For batch job
    public int process() {
        if (file != null && file.exists()) {
            ByteArrayInputStream in = null;
            try {
                rptLogger.info("Parsing file ============>" + file.getCanonicalPath());
                checkDataExistByFileName(sysColValMap);
                etlFileParser.setSysColValMap(sysColValMap);
                in = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
                etlFileParser.setIn(in);
                return etlFileParser.parseFile();
            } catch (Exception e) {
                rptLogger.error(e.getMessage());
                e.printStackTrace();
            } finally {
                if (sysColValMap != null) {
                    sysColValMap.clear();
                    sysColValMap = null;
                }
                try {
                    assert in != null;
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
